/*
 * Copyright (c) 2017 Omnigon Communications, LLC. All rights reserved.
 *
 * This software is the confidential and proprietary information of Omnigon Communications, LLC
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall
 * in accordance with the terms of the license agreement you entered into with Omnigon Communications, LLC, its
 * subsidiaries, affiliates or authorized licensee. Unless required by applicable law or agreed to in writing, this
 * Confidential Information is provided on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the license agreement for the specific language governing permissions and limitations.
 */
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
