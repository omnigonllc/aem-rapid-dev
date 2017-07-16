package com.omnigon.aem.handlebars.helpers;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * This Handlebar's Helper class returns property value from specified context.
 * If name is not specified nothing should be returned.
 * If property with specified name is not exists nothing should be returned.
 * If context argument is omitting current context should be used.
 * <p>
 * Examples:
 * <p>
 * The context is represented as: {foo: 'bar'}
 * So the invocation below will render: bar
 * <p>
 * {{getProperty [context] name}} or (getProperty [context] name)
 */
@Service
@Component
public class GetPropertyHelper implements HandlebarsHelper {

    private static final String NAME = "getProperty";

    /**
     * @return Returns helper's name
     */
    @Override
    public String getName() {
        return NAME;
    }


    @Override
    public Object apply(final Object context, final Options options) throws IOException {
        if (areContextOrOptionsEmpty(context, options)) {
            return StringUtils.EMPTY;
        }
        return getPropertyValue(resolveContext(context, options), resolvePropertyName(context, options));
    }

    private boolean areContextOrOptionsEmpty(Object context, Options options) {
        return context == null || options == null;
    }

    private Map<String, Object> resolveContext(Object context, Options options) {
        if (context instanceof Map && ArrayUtils.isNotEmpty(options.params)) {
            return (Map<String, Object>) context;
        }
        if (options.context.model() instanceof Map) {
            return (Map<String, Object>) options.context.model();
        }
        return Collections.emptyMap();
    }

    private String resolvePropertyName(Object context, Options options) {
        if (ArrayUtils.isNotEmpty(options.params) && options.param(NumberUtils.INTEGER_ZERO) instanceof String) {
            return options.param(NumberUtils.INTEGER_ZERO);
        }
        if (context instanceof String) {
            return (String) context;
        }
        return StringUtils.EMPTY;
    }

    private Object getPropertyValue(Map<String, Object> propertyContext, String propertyName) {
        Object propertyValue = null;
        if (propertyContext.containsKey(propertyName)) {
            propertyValue = propertyContext.get(propertyName);
        }
        return propertyValue != null ? propertyValue : StringUtils.EMPTY;
    }
}