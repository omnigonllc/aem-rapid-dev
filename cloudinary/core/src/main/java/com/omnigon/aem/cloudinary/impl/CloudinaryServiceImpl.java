package com.omnigon.aem.cloudinary.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.omnigon.aem.cloudinary.CloudinaryConfigurationService;
import com.omnigon.aem.cloudinary.CloudinaryService;
import com.omnigon.aem.common.sling.ResourceResolverFactoryService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.io.File;
import java.io.InputStream;
import java.util.*;

import static com.omnigon.aem.cloudinary.CloudinaryConstants.*;
import static org.apache.felix.scr.annotations.ReferenceCardinality.OPTIONAL_MULTIPLE;
import static org.apache.felix.scr.annotations.ReferencePolicy.DYNAMIC;
import static org.apache.commons.lang3.StringUtils.*;
import static com.day.cq.commons.jcr.JcrConstants.*;

/**
 * user: Brenn Hill
 * Implementation of CloudinaryService.  Creates a common use Cloudinary implementation based on system configurations.
 */
@Component(immediate = true)
@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudinaryServiceImpl.class);

    private static final String DOT_SEPARATOR = ".";
    private static final String SLASH_SEPARATOR = "/";
    private static final String UNDERLINE_SEPARATOR = "_";

    private static final String WHITESPACE = " ";
    private static final String DAM_SEARCH_STRING = "content/dam/";

    private static final String METADATA_NODE = "/" + JcrConstants.JCR_CONTENT + "/metadata";

    private static final String PUBLIC_ID = "public_id";
    private static final String INVALIDATE = "invalidate";
    private static final String OVERWRITE = "overwrite";

    private static final long REUPLOAD_TRIGGER = 5000; // in ms

    @Reference(cardinality = OPTIONAL_MULTIPLE, policy = DYNAMIC,
            referenceInterface = CloudinaryConfigurationService.class, bind = "bind", unbind = "unbind")
    private List<CloudinaryConfigurationService> cloudinaryConfigurationServices;

    private void bind(CloudinaryConfigurationService cloudinaryConfigurationService) {
        if (cloudinaryConfigurationServices == null) {
            cloudinaryConfigurationServices = new ArrayList<>();
        }
        cloudinaryConfigurationServices.add(cloudinaryConfigurationService);
    }

    private void unbind(CloudinaryConfigurationService cloudinaryConfigurationService) {
        cloudinaryConfigurationServices.remove(cloudinaryConfigurationService);
    }

    @Reference(target = "(factory.label=cloudinary)")
    private ResourceResolverFactoryService resolverFactoryService;

    @Activate
    @Modified
    public void activate() {
        LOGGER.info("CloudinaryService Service has been started.");
    }

    public ResourceResolverFactoryService getResolverFactoryService() {
        return resolverFactoryService;
    }

    public List<CloudinaryConfigurationService> getCloudinaryConfigurationServices() {
        return cloudinaryConfigurationServices;
    }

    @Override
    public Cloudinary getCloudinary(String imagePath) {
        Cloudinary cloudinary = getCloudinaryForPath(imagePath);
        if(cloudinary == null) {
            LOGGER.error("Cannot get Cloudinary instance for an image {}", imagePath);
        }
        return cloudinary;
    }

    @Override
    public void upload(Asset asset) {

        String assetPath = asset.getOriginal().getPath();
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Uploading asset at {} to Cloudinary", assetPath);
        }

        try (final ResourceResolver resolver = getResolverFactoryService().getDefaultResourceResolver()) {

            String path = assetPath + "/" + JCR_CONTENT;
            Resource original = resolver.getResource(path);
            Node node = original.adaptTo(Node.class);
            Resource metadataResource = resolver.getResource(asset.getPath() + "/" + JCR_CONTENT);

            if (node != null) {
                LOGGER.debug("Node path {}", node.getPath());

                /* Check to see if we've had a near-instant change to the image. This means the xmp service probably changed it. No need to push again. */
                Map<String, Object> uploadResponse = isOutdated(metadataResource)
                        ? uploadAsset(path, node)
                        : null;

                // Update properties and status
                boolean updated = setCloudinaryProperties(uploadResponse, metadataResource, asset);
                if(updated) {
                    resolver.adaptTo(Session.class).save();
                }

            } else {
                LOGGER.error("Couldn't adapt original resource at path: {} to javax.jcr.Node", path);
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    @Override
    public void remove(Asset asset) {
        String publicId = extractFileName(asset.getOriginal().getPath());
        remove(publicId, asset.getPath());
    }

    @Override
    public void remove(String imageId, String imagePath) {
        try {

            Cloudinary cloudinary = getCloudinaryForPath(imagePath);
            // Invalidate must be true for the image to truly be purged from the Cloudinary CDN.  This is not the default.
            Map uploadOptions = ObjectUtils.asMap(
                    PUBLIC_ID, imageId,
                    INVALIDATE, Boolean.TRUE);

            if(cloudinary != null) {
                cloudinary.uploader().destroy(imageId, uploadOptions);
            }
        } catch (Exception e) {
            LOGGER.warn("Cannot remove image {} from Cloudinary", imageId, e);
        }
    }

    @Override
    public String getCloudinaryUrl(String path) {
        return getCloudinaryUrl(path, null);
    }

    @Override
    public String getCloudinaryUrl(String path, Transformation transformation) {

        String url = EMPTY;

        // Lets get Cloudinary URL generated during upload
        if (StringUtils.isNotEmpty(path) && transformation == null) {

            try (ResourceResolver resolver = getResolverFactoryService().getDefaultResourceResolver()) {

                Resource resource = resolver.getResource(path + "/" + JCR_CONTENT);
                if (resource != null) {
                    url = resource.adaptTo(ValueMap.class).get(CLOUDINARY_URL_KEY, EMPTY);
                    // may be in old location before moving due to XMP writeback.
                    if (url.equals(EMPTY)) {
                        String metadataPath = path + METADATA_NODE;
                        resource = resolver.getResource(metadataPath);
                        if (resource != null) {
                            url = resource.adaptTo(ValueMap.class).get("dam:cloudinaryUrl", EMPTY);
                        }
                    }
                }

            } catch (LoginException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (url.equals(EMPTY) || transformation != null) {
            try {
                Cloudinary cloudinary = getCloudinary(path);
                url = cloudinary
                        .url()
                        .transformation(transformation)
                        .generate(path);

                if(url == null) {
                    url = EMPTY;
                }
            } catch (Exception e) {
                LOGGER.error("Cannot get cloudinary instance for image at: {}", path);
            }
        }

        if (url.equals(EMPTY)) {
            LOGGER.error("Empty Cloudinary url, using {} as last resort", path);
            url = path;
        }

        return url;
    }

    private boolean setCloudinaryProperties(Map uploadResponse, Resource metadataResource, Asset asset) {

        ModifiableValueMap metadata = metadataResource.adaptTo(ModifiableValueMap.class);

        String cloudinaryUrl;
        if (null != uploadResponse &&
                (cloudinaryUrl = (String)uploadResponse.get("url")) != null) {
            try {

                boolean needsSave = setProperty(metadata, ON_CLOUDINARY_KEY, true);
                needsSave |= setProperty(metadata, CLOUDINARY_URL_KEY, cloudinaryUrl);
                needsSave |= setProperty(metadata, CLOUDINARY_STATUS, SUCCESS);
                needsSave |= setProperty(metadata, CLOUDINARY_STATUS_MSG, COMPLETE);
                needsSave |= setProperty(metadata, CLOUDINARY_LAST_MODIFIED, SUCCESS);

                if (needsSave) {
                    metadata.put(CLOUDINARY_LAST_MODIFIED, Calendar.getInstance());
                }

                return needsSave;

            } catch (Exception e) {
                LOGGER.error("CloudinaryUploader cannot set image meta", e);
                setErrorStatus(asset, e.getMessage());
            }
        }

        return false;
    }

    private boolean setProperty(ModifiableValueMap metadata, String property, Object newValue) {
        Object currentValue = metadata.get(property, Object.class);
        if(null == currentValue || !currentValue.equals(newValue)) {
            metadata.put(property, newValue);
            return true;
        }
        return false;
    }

    private void setErrorStatus(Asset asset, String error) {

        try (final ResourceResolver resourceResolver = getResolverFactoryService().getDefaultResourceResolver()) {
            String metadataPath = asset.getPath() + METADATA_NODE;
            Node meta = resourceResolver.getResource(metadataPath).adaptTo(Node.class);
            if(meta != null) {
                meta.setProperty(ON_CLOUDINARY_KEY, false);
                meta.setProperty(CLOUDINARY_STATUS, STATUS_FAILED);
                meta.setProperty(CLOUDINARY_STATUS_MSG, error);
                resourceResolver.adaptTo(Session.class).save();
            } else {
                LOGGER.error("Cannot get metadata node {}", metadataPath);
            }
        } catch (Exception ex) {
            LOGGER.error("Couldn't set failure info {}", ex.getMessage(), ex);
        }

    }

    /**
     * Checks to see if it's been more than 5 seconds since the image was modified.  If not, don't reupload.
     * A short time just means XMP service or something has updated the binary with extra metadata.  We don't care.
     *
     * @return {@code true}, if image re-upload should be triggered
     */
    private boolean isOutdated(Resource image) {

        ValueMap metadata = image.adaptTo(ValueMap.class);
        Calendar lastModified = metadata.get(JcrConstants.JCR_LASTMODIFIED, Calendar.class); // last modified timestamp in DAM
        // last upload timestamp in Cloudinary
        Calendar cloudinaryLastModified = metadata.get(CLOUDINARY_LAST_MODIFIED, Calendar.class);

        if (null == lastModified || null == cloudinaryLastModified) {
            return true;
        }

        long diff = lastModified.getTimeInMillis() - cloudinaryLastModified.getTimeInMillis();
        if (diff > REUPLOAD_TRIGGER) {
            return true;
        }

        LOGGER.info("very recent upload of same image. Ignoring. Likely XMP update.");

        return false;
    }

    /**
     * Uploads asset at the given path to Cloudinary.
     * @param path image path
     * @param node image node
     */
    private Map<String, Object> uploadAsset(String path, Node node) {

        File tmpFile = null;
        InputStream inputStream = null;
        try {
            tmpFile = File.createTempFile("dam.omnigon", ".process.tmp");

            inputStream = node.getProperty(JcrConstants.JCR_DATA).getBinary().getStream();
            FileUtils.copyInputStreamToFile(inputStream, tmpFile);

            Cloudinary cloudinary = getCloudinaryForPath(path);
            if(cloudinary != null) {

                final String publicID = extractFileName(path);
                Map uploadOptions = ObjectUtils.asMap(
                        PUBLIC_ID, publicID,
                        INVALIDATE, Boolean.TRUE,
                        OVERWRITE, Boolean.TRUE);

                Uploader uploader = cloudinary.uploader();
                Map<String, Object> uploadResponse = (Map<String, Object>)uploader.upload(tmpFile, uploadOptions);

                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Node path: {}", node.getPath());
                    LOGGER.debug("Image upload response: {}", uploadResponse);
                }

                return uploadResponse;

            } else {
                LOGGER.error("Cloudinary is not configured for path {}", path);
            }

        } catch (Exception e) {
            LOGGER.error("Upload to Cloudinary failed. Error while processing asset [{}]: ", path, e);
        } finally {
            FileUtils.deleteQuietly(tmpFile);
            IOUtils.closeQuietly(inputStream); // unless closed by copyInputStreamToFile
        }

        return Collections.emptyMap();
    }

    /**
     * Prepare Service to work with corresponding cloudinary configuration.
     * @param path DAM asset path.
     */
    private Cloudinary getCloudinaryForPath(String path) {

        for (CloudinaryConfigurationService configurationService : cloudinaryConfigurationServices) {
            if(path.startsWith(configurationService.getDamFolder())) {
                return new Cloudinary(configurationService.getConfiguration());
            }
        }

        return null;
    }

    /**
     * @param path Extract file name from the path and replace all dots and whitespaces.
     */
    private static String extractFileName(String path) {
        if (!path.isEmpty() && path.contains(DOT_SEPARATOR)) {
            String temp = StringUtils.replaceOnce(path, DAM_SEARCH_STRING, StringUtils.EMPTY);

            if (StringUtils.countMatches(temp, DOT_SEPARATOR) > 1) {
                temp = StringUtils.substringBeforeLast(temp, DOT_SEPARATOR);
                temp = StringUtils.replace(temp, DOT_SEPARATOR, UNDERLINE_SEPARATOR);
                temp = String.format("%s" + DOT_SEPARATOR, temp);
            }
            temp = StringUtils.substringBetween(temp, SLASH_SEPARATOR, DOT_SEPARATOR);

            if (StringUtils.containsWhitespace(temp)) {
                temp = temp.replaceAll(WHITESPACE, UNDERLINE_SEPARATOR);
            }

            return temp;
        }

        return path;
    }

}