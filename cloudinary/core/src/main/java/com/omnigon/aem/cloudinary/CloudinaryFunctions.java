package com.omnigon.aem.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.Url;
import com.day.cq.dam.api.DamConstants;
import com.omnigon.aem.common.utils.ServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrTokenizer;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 * User: Andrey Bardashevsky
 * Date/Time: 03.10.13 17:09
 */
public final class CloudinaryFunctions {

    private CloudinaryFunctions() {}

    /**
     *
     * @param request -
     * @param src image path
     * @return cloudinary image URL
     */
    public static String getImageUrl(final SlingHttpServletRequest request, final String src) {
        return getImageUrl(request, src, null);
    }

    /**
     *
     * @param request -
     * @param src image path
     * @param transformation -
     * @return cloudinary image URL
     */
    public static String getImageUrl(final SlingHttpServletRequest request, final String src, final String transformation) {
        if (StringUtils.contains(src, DamConstants.MOUNTPOINT_ASSETS)) {
            return getAbsoluteImageUrl(request, transformation, truncateDamFolder(src));
        } else {
            return src;
        }
    }

    /**
     *
     * @param src image path
     * @return image path without {@value #DamConstants.MOUNTPOINT_ASSETS} at the beginning
     */
    public static String truncateDamFolder(final String src) {
        if (StringUtils.contains(src, DamConstants.MOUNTPOINT_ASSETS)) {
            return StringUtils.substringAfter(src, DamConstants.MOUNTPOINT_ASSETS + "/");
        } else {
            return src;
        }
    }

    /**
     *
     * @param request -
     * @param path image path
     * @return cloudinary image URL
     */
    public static String cloudinaryURL(final SlingHttpServletRequest request, final String path) {
        CloudinaryService cloudinaryService = ServiceUtils.getService(CloudinaryService.class, request);
        String imagePath = cloudinaryService.getCloudinaryUrl(path);

        return StringUtils.isNotBlank(imagePath) ? imagePath : path;

    }

    /**
     *
     * @param request -
     * @param transformation -
     * @param imagePath -
     * @return cloudinary image URL
     */
	public static String getAbsoluteImageUrl(final SlingHttpServletRequest request, final String transformation, final String imagePath) {
        Url url = getCloudinary(request, imagePath).url();

        return url.transformation(parseTransformation(transformation)).generate(imagePath);
    }

    private static Cloudinary getCloudinary(final SlingHttpServletRequest request, String imagePath) {
        CloudinaryService cloudinaryService = ServiceUtils.getService(CloudinaryService.class, request);
        return cloudinaryService.getCloudinary(imagePath);
    }

    private static Transformation parseTransformation(final String transformationParams) {
        Transformation transformation = new Transformation();

        if (StringUtils.isNotBlank(transformationParams)) {
            StrTokenizer tokenizer = new StrTokenizer(transformationParams, ',');

            while (tokenizer.hasNext()) {
                String next = tokenizer.nextToken();

                if (StringUtils.isNotBlank(next)) {
                    String[] param = StringUtils.splitByWholeSeparator(next, "=");
                    if (param.length == 2 && StringUtils.isNotBlank(param[0]) && StringUtils.isNotBlank(param[1])) {
                        transformation.param(param[0], param[1]);
                    }
                }
            }
        }

        return transformation;
    }
}
