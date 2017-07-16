package com.omnigon.aem.handlebars.helpers;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.io.IOException;

@Service
@Component
public class OrHelper implements HandlebarsHelper {

    private static final String NAME = "or";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Object apply(Object context, Options options) throws IOException {
        Object returningValue = StringUtils.EMPTY;
        if (!options.context.model().equals(context) && isValidValue(context)) {
            returningValue = context;
        }
        for (Object param : options.params) {
            if (isValidValue(param)) {
                returningValue = param;
            }
        }
        return returningValue;
    }

    private Boolean isValidValue(Object value) {
        if (value == null || StringUtils.EMPTY.equals(value) || Boolean.FALSE.equals(value)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
