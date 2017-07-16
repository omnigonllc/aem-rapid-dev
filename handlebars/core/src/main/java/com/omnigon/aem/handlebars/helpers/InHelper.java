package com.omnigon.aem.handlebars.helpers;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.io.IOException;

/**
 * Usage is similar to #if helper bu allows matching value against multiple candidates:
 *
 * {{#in value param1 [param2,..]}}
 *    content1
 * {{else}}
 *    content2
 * {{/in}}
 */
@Service
@Component
public class InHelper implements HandlebarsHelper {

    private static final String NAME = "in";

    /**
     * @return return the helper's name.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     *
     * @param target target param.
     * @param options the thing being compared.
     * @return true/false or if true and there is an inner block, that content.
     * @throws IOException
     */
    @Override
    public CharSequence apply(final Object target, final Options options) throws IOException {
        if (!isValidComparableStringParam(target) || !isValidPrefixParam(options)) {
            return null;
        }

        CharSequence comparableString = (CharSequence) target;
        for(Object param : options.params) {
            if( comparableString.equals(param.toString()) ) {
                return options.fn();
            }
        }

        return options.inverse();
    }


    private boolean isValidComparableStringParam(Object context) {/*check comparable string type*/
        return context instanceof CharSequence;
    }

    private boolean isValidPrefixParam(Options options) {/*check prefix param*/
        return options != null && ArrayUtils.isNotEmpty(options.params) && options.param(0) instanceof CharSequence;
    }

}