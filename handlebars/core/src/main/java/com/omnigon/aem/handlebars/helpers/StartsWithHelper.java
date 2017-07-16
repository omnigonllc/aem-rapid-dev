package com.omnigon.aem.handlebars.helpers;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.io.IOException;

/**
 * Created by daniil.sheidak on 21.10.2016.
 * This Handlebars' Helper class renders content if haystack starts with needle (case matters).
 * <p>
 * Examples:
 * <p>
 * {{#startsWith foo 'bar'}}
 * This content will be rendered if property type<string> starts with 'bar'
 * {{/startsWith}}
 */
@Service
@Component
public class StartsWithHelper implements HandlebarsHelper {

    private static final String NAME = "startsWith";

    /**
     * @return Returns helper's name
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     *
     * @param context contains comparable string param.
     * @param options contains handlebar's content and
     * prefix param.
     * @return returns handlebar's content if comparable
     * string param starts with otherwise null.
     * @throws IOException
     */
    @Override
    public CharSequence apply(final Object context, final Options options) throws IOException {
        if (!isValidComparableStringParam(context) || !isValidPrefixParam(options)) {
            return StringUtils.EMPTY;
        }
        CharSequence comparableString = (CharSequence) context;
        CharSequence prefix = options.param(0);
        CharSequence content = StringUtils.EMPTY;
        if (StringUtils.startsWith(comparableString, prefix)) {
            content = options.fn();
        }
        return content;
    }

    private boolean isValidComparableStringParam(Object context) {/*check comparable string type*/
        return context instanceof CharSequence;
    }

    private boolean isValidPrefixParam(Options options) {/*check prefix param*/
        return options != null && ArrayUtils.isNotEmpty(options.params) && options.param(0) instanceof CharSequence;
    }

}

