package com.omnigon.aem.cloudinary;

import java.util.Map;

/**
 * Configuration service for cloudinary.
 */
public interface CloudinaryConfigurationService {
    /**
     * Gets the cloudinary configuration for use by other services in AEM.
     *
     * @return a map of the cloudinary configuration parameters for SERVER SIDE use of cloudinary.
     */
    Map<String, Object> getConfiguration();

    /**
     * Returns the CLIENT SIDE configuration for cloudinary for use by javascript libraries on the browser.
     *
     * @return a map of cloudinary configurations for client side.
     */
    Map<String, Object> getClientConfiguration();

    /**
     * Cloud name in Cloudinary
     *
     * @return cloud name
     */
    String getCloudName();

    /**
     * DAM folder associated with this cloudinary configuration
     *
     * @return mapped DAM folder
     */
    String getDamFolder();

    /**
     * True, if DAM folder should be synchronized regularly.
     * @return true, if DAM folder should be synchronized regularly
     */
    boolean getDamSynchronized();

}