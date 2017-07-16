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
package com.omnigon.aem.common.sling.impl;

import com.omnigon.aem.common.sling.ResourceResolverFactoryService;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.osgi.PropertiesUtil;

import java.util.HashMap;
import java.util.Map;

@Service
@Component(immediate = true, metatype = true, configurationFactory=true, label="ResourceResolver Factory Service",
        description="ResourceResolver Factory Service", policy = ConfigurationPolicy.REQUIRE)
public class ResourceResolverFactoryServiceImpl implements ResourceResolverFactoryService {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private static final String SERVICE_NAME_PROPERTY = "defaultService";
    private static final String SERVICE_NAME_DEFAULT = "default-service";

    @Property(name = SERVICE_NAME_PROPERTY, value = SERVICE_NAME_DEFAULT)
    private String defaultService;

    /** Use to refer factory services by name, e.g. cloudinary */
    @Property(description="Label for the Factory Service")
    private static final String FACTORY_NAME = "factory.label";

    ResourceResolverFactory getResourceResolverFactory() {
        return resourceResolverFactory;
    }

    @Activate
    @Modified
    void activate(Map<String, Object> properties) {
        defaultService = PropertiesUtil.toString(properties.get(SERVICE_NAME_PROPERTY), SERVICE_NAME_DEFAULT);
    }

    @Override
    public ResourceResolver getDefaultResourceResolver() throws LoginException {
        return getServiceResourceResolver(defaultService);
    }

    @Override
    public ResourceResolver getServiceResourceResolver(String serviceName) throws LoginException {

        Map<String, Object> param = new HashMap<>();
        param.put(ResourceResolverFactory.SUBSERVICE, serviceName);
        ResourceResolver resolver = getResourceResolverFactory().getServiceResourceResolver(param);

        return resolver;
    }

    @Override
    public ResourceResolver getAdminResourceResolver() throws LoginException {
        return getResourceResolverFactory().getAdministrativeResourceResolver(null);
    }

}
