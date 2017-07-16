package com.omnigon.aem.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.day.cq.dam.api.Asset;

import java.util.List;

/**
 * User: Andrey Bardashevsky
 * Date/Time: 06.10.13 21:13
 */
public interface CloudinaryService {

    /**
     * Returns list of available Cloudinary configuration services.
     * @return list of available Cloudinary configuration services.
     */
    List<CloudinaryConfigurationService> getCloudinaryConfigurationServices();

    /**
     * Returns Cloudinary API client instance for the given image path.
     *
     * @param imagePath DAM asset path
     * @return cloudinary instance
     */
    Cloudinary getCloudinary(String imagePath);

    /**
     * Attempts to upload the asset to cloudinary.
     * If successful, sets metadata on the asset:
     * dam:cloudinaryUrl will be set to the public, non-secured url for the image on cloudinary
     * dam:onCloudinary will be set to true
     * dam:cloudinaryStatus will be set to "success"
     * dam:cloudinaryStatusMessage will be set to "Upload complete."
     * <p>
     * if unsuccessful, sets different metadata:
     * dam:onCloudinary will be false
     * dam:cloudinaryStatus will be set to failed
     * dam:cloudinaryStatusMessage will be set to the error message generated on the upload attempt.
     * <p>
     * It is possible, under circumstances where the JCR session times out or some other low-level issue arises where no data is set on the node
     * under success or failure conditions.  This shouldn't happen, and is a sign that something more significant is wrong, especially if it happens regularly
     *
     * @param asset {@link Asset}
     */
    void upload(Asset asset);

    /**
     * Gets cloudinary url from meta properties of existing asset
     *
     * @param path of existing asset
     * @return path to cloudinary url
     */
    String getCloudinaryUrl(String path);

    /**
     * Gets cloudinary url from meta properties of existing asset
     *
     * @param path of existing asset
     * @return path to cloudinary url
     */
    String getCloudinaryUrl(String path, Transformation transformation);

    /**
     * Removes image synced with asset from cloudinary.
     * @param asset DAM asset
     */
    void remove(Asset asset);

    /**
     * Removes image from cloudinary by its publicID.
     * @param imageId cloudinary public id
     * @param imagePath DAM asset path
     */
    void remove(String imageId, String imagePath);

}