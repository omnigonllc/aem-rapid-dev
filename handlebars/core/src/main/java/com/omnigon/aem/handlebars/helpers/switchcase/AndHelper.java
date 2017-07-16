package com.omnigon.aem.handlebars.helpers.switchcase;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.omnigon.aem.handlebars.helpers.HandlebarsHelper;

import java.io.IOException;

/**
 * Created by brenn.hill on 02.15.2017.
 *
 */
@Service
@Component
public class AndHelper implements HandlebarsHelper {

    private static final String NAME = "and";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Object apply(Object context, Options options) throws IOException {
        Object returningValue = Boolean.FALSE;
        if (!options.context.model().equals(context) && isValidValue(context)) {
            returningValue = Boolean.TRUE;
            for (Object param : options.params) {
                if (!isValidValue(param)) {
                    return StringUtils.EMPTY;
                }
            }
        }
        if(returningValue==Boolean.TRUE) {
            CharSequence returnVal = options.fn();
            if(returnVal==null || StringUtils.isEmpty(returnVal)){
                return Boolean.TRUE;
            } else {
                return returnVal;
            }
        }
        return StringUtils.EMPTY;
    }

    private Boolean isValidValue(Object value) {
        if (value == null || StringUtils.EMPTY.equals(value) || Boolean.FALSE.equals(value)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
