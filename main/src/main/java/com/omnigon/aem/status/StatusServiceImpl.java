package com.omnigon.aem.status;

import com.omnigon.aem.common.sling.ResourceResolverFactoryService;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
@Service
public class StatusServiceImpl implements StatusService {

    private final static Logger LOGGER = LoggerFactory.getLogger(StatusServiceImpl.class);

    @Reference(target = "(factory.label=default)")
    private ResourceResolverFactoryService resolverFactoryService;

    @Activate
    @Modified
    private void activate() {
    }

    @Override
    public String getStatus() {

        String status = "FAILED";
        try(ResourceResolver resourceResolver = resolverFactoryService.getDefaultResourceResolver()) {

            if(resourceResolver != null) {
                status = "OK";
            }

        } catch (LoginException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return status;
    }

}
