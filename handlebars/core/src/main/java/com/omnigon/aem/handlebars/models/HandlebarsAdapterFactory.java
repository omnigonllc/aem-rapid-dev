package com.omnigon.aem.handlebars.models;

import com.omnigon.aem.handlebars.models.impl.PageModelImpl;
import com.omnigon.aem.handlebars.models.impl.RequestModelImpl;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.adapter.AdapterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

@Component(metatype = false)
@Service(value = AdapterFactory.class)
public class HandlebarsAdapterFactory implements AdapterFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(HandlebarsAdapterFactory.class);

    @Property(name = "adapters")
    public static final String[] ADAPTER_CLASSES = {
            PageModel.class.getName(), RequestModel.class.getName()
    };

    @Property(name = "adaptables")
    protected static final String[] ADAPTABLE_CLASSES = { SlingHttpServletRequest.class.getName() };

    public <AdapterType> AdapterType getAdapter(Object adaptable, Class<AdapterType> type) {
        if (adaptable instanceof SlingHttpServletRequest) {
            return this.getAdapter((SlingHttpServletRequest /* to avoid infinite cycle*/) adaptable, type);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <AdapterType> AdapterType getAdapter(SlingHttpServletRequest request, Class<AdapterType> type) {

        if(type.equals(PageModel.class)) {
            return request.adaptTo((Class<AdapterType>)PageModelImpl.class);
        }

        if(type.equals(RequestModel.class)) {
            return request.adaptTo((Class<AdapterType>)RequestModelImpl.class);
        }

        LOGGER.error("Cannot adapt request {} ", request.getPathInfo());
        return null;
    }

}
