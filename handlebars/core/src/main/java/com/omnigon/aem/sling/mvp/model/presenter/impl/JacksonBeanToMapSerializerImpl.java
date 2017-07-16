package com.omnigon.aem.sling.mvp.model.presenter.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.omnigon.aem.sling.mvp.model.presenter.BeanToMapSerializer;
import com.omnigon.aem.sling.mvp.model.presenter.JacksonSerializable;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.xss.XSSAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jackson-based Bean-to-Map serializer implementation.
 */
@Component
@Service
@Properties(value = {
        @Property(name = "service.description", value = "Handlebars: Bean to Map Serializer")}
)
public class JacksonBeanToMapSerializerImpl implements BeanToMapSerializer {

    private static final Logger log = LoggerFactory.getLogger(JacksonBeanToMapSerializerImpl.class);

    //TODO: Inject service
    private XSSAPI xssApi;

    private ObjectMapper objectMapper;

    @Activate
    protected void activate() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
      //  objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SerializerFactory serializerFactory = BeanSerializerFactory
                .instance
                .withSerializerModifier(new XSSSerializerModifier(xssApi));
        objectMapper.setSerializerFactory(serializerFactory);
    }

    private static class XSSSerializerWrapper extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            String safeValue = getSafeValue((String) value);
            //continue with default impl
            provider.findTypedValueSerializer(String.class, true, beanProperty).serialize(safeValue, jgen, provider);
        }

        private String getSafeValue(String originalValue) {
            String result;
            if (StringUtils.isNotBlank(originalValue) && xssApi != null) {
                switch (xssProtection.strategy()) {
                    case NONE:
                        result = originalValue;
                        break;
                    case HTML_FILTER:
                        result = xssApi.filterHTML(originalValue);
                        break;
                    case HTML:
                        result = xssApi.encodeForHTML(originalValue);
                        break;
                    case HTML_ATTR:
                        result = xssApi.encodeForHTMLAttr(originalValue);
                        break;
                    case JS:
                        result = xssApi.encodeForJSString(originalValue);
                        break;
                    case XML:
                        result = xssApi.encodeForXML(originalValue);
                        break;
                    case XML_ATTR:
                        result = xssApi.encodeForXMLAttr(originalValue);
                        break;
                    case HREF:
                        result = xssApi.getValidHref(originalValue);
                        break;
                    case PLAIN_TEXT:
                        result = stripHTML(originalValue);
                        break;
                    default:
                        throw new UnsupportedOperationException(xssProtection.strategy() + " filtering is not yet supported");
                }
            } else {
                result = originalValue;
            }
            return result;
        }

        private BeanProperty beanProperty;
        private XSSAPI xssApi;
        private XSSProtection xssProtection; // TODO: strange usage of the annotation

        public XSSSerializerWrapper(BeanProperty beanProperty, XSSProtection xssProtection, XSSAPI xssApi) {
            this.beanProperty = beanProperty;
            this.xssApi = xssApi;
            this.xssProtection = xssProtection;
        }

        private String stripHTML(String text) {
            if (StringUtils.isNotBlank(text)) {
                return text.replaceAll("<[^>]*>", " ").replaceAll("[ ]{2,}", " ").trim();
            }
            return text;
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertToMap(Object bean) {
        //only serialize objects that implement this marker interface, which excludes properties that are know to cause endless loops
        if(bean instanceof JacksonSerializable){
            try {
                return objectMapper.convertValue(bean, Map.class);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        }
        return new HashMap<>();
    }

    private static class XSSSerializerModifier extends BeanSerializerModifier {
        private XSSAPI xssApi;
        private static final Logger LOG = LoggerFactory.getLogger(XSSSerializerModifier.class);

        public XSSSerializerModifier(XSSAPI xssApi) {
            this.xssApi = xssApi;
        }

        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
            for (BeanPropertyWriter beanPropertyWriter : beanProperties) {
                try {
                    XSSProtection xssProtection = beanPropertyWriter.getAnnotation(XSSProtection.class);
                    if (xssProtection != null) {
                        beanPropertyWriter.assignSerializer(
                                new XSSSerializerWrapper(beanPropertyWriter, xssProtection, xssApi)
                        );
                    }
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            return beanProperties;
        }
    }
}
