package com.omnigon.aem.common.sling;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;

/**
 * Allows obtaining a ResourceResolver instance for
 *
 * http://sling.apache.org/documentation/the-sling-engine/service-authentication.html#implementation
 * https://helpx.adobe.com/experience-manager/using/querying-experience-manager-sling.html
 */
public interface ResourceResolverFactoryService {

    /**
     * Returns a ResourceResolver instance using a default service configuration mapped to a System User.
     * @return ResourceResolver instance
     * @throws LoginException
     */
    ResourceResolver getDefaultResourceResolver() throws LoginException;

    /**
     * Returns a ResourceResolver instance using provided service name mapped to a System User.
     * @return ResourceResolver instance
     * @throws LoginException
     */
    ResourceResolver getServiceResourceResolver(String serviceName) throws LoginException;

    /**
     * Returns a ResourceResolver instance using a Admin User. Use with caution.
     * @return ResourceResolver instance
     * @throws LoginException
     */
    ResourceResolver getAdminResourceResolver() throws LoginException;

}
