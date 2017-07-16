package com.omnigon.aem.handlebars.helpers.comparison;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.omnigon.aem.handlebars.helpers.HandlebarsHelper;

import java.io.IOException;
import java.math.BigDecimal;

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
 *
 */
@Service
@Component
public class ltHelper implements HandlebarsHelper {

    private static final String NAME = "lt";

    /**
     * @return Returns helper's name
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * @param target  target param.
     * @param options the thing being compared.
     * @return true/false or if true and there is an inner block, that content.
     * @throws IOException
     */
    @Override
    public CharSequence apply(final Object target, final Options options) throws IOException {
        if (!isValidComparableStringParam(target) || !isValidPrefixParam(options)) {
            return null;
        }

        Number targetNum = null;
        Number optionsNum = null;

        if (target instanceof CharSequence) {
            targetNum = Float.valueOf((String) target);
        } else if (target instanceof Number){
            targetNum = (Number) target;
        }

        if(options.param(0) instanceof CharSequence) {
            optionsNum = Float.valueOf((String) options.param(0));
        } else if(options.param(0) instanceof Number) {
            optionsNum = (Number) options.param(0);
        }
        if(optionsNum == null) {
            optionsNum = 0;
        }

        if(targetNum !=null && optionsNum !=null) {
            if(compareNums(targetNum, optionsNum) < 0) {
                return options.fn();
            }
        }
        return null;

    }

    protected int compareNums(Number n1, Number n2) {
        BigDecimal b1 = new BigDecimal(n1.doubleValue());
        BigDecimal b2 = new BigDecimal(n2.doubleValue());
        return b1.compareTo(b2);
    }

    protected boolean isValidComparableStringParam(Object context) {/*check comparable string type*/
        return context instanceof CharSequence;
    }

    protected boolean isValidPrefixParam(Options options) {/*check prefix param*/
        return options != null
                && ArrayUtils.isNotEmpty(options.params)
                && ( options.param(0) instanceof Number || options.param(0) instanceof CharSequence);
    }
}
