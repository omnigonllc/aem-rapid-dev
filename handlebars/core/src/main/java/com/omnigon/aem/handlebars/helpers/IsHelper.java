package com.omnigon.aem.handlebars.helpers;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.io.IOException;

/**
 * Created by Brenn Hill
 *
 * "is" renders content if a strictly equals to b.
 * In case of omitting b expression is true if a isn’t null, undefined, false, 0 or "".
 * Invocation as sub-expression (i.e. (helper ...) invocation) returns bool result.
 * "isnt" is inverse.
 *
 * Syntax
 * {{#is a [b]}}…{{/is}} or (is a [b])
 *
 * Parameters
 * a - any supported value or property
 * b - (optional) any supported value or property
 *
 * Example (handlebars file)
 *
 * {{#is foo 'bar'}}
 * This content will be rendered if property foo<string> equals to string 'bar'
 * {{/is}}
 * {{someHelperWhichUsesBoolean (is foo 'bar')}}
 *
 * TODO: this true/false version may need tweaks.
 *
 */
@Service
@Component
public class IsHelper implements HandlebarsHelper {

    private static final String NAME = "is";

    /**
     * @return Returns helper's name
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
        CharSequence prefix = options.param(0);
        CharSequence content = null;
        if(target.equals(options.param(0))) {
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
