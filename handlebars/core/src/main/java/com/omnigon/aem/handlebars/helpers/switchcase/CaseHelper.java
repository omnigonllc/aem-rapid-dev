package com.omnigon.aem.handlebars.helpers.switchcase;

import com.github.jknack.handlebars.Options;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.omnigon.aem.handlebars.helpers.HandlebarsHelper;

import java.io.IOException;

/**
 * Created by daniil.sheidak on 14.10.2016.
 *
 * Examples:
 *
 *  {{#switch prop}}
 *      {{#case 123}}
 *          Content for 123 case
 *      {{/case}}
 *      {{#case 321}}
 *          Content for 321 case
 *      {{/case}}
 *      {{#default}}
 *      Default content
 *      {{/default}}
 *   {{/switch}}
 *
 */
@Service
@Component
public class CaseHelper implements HandlebarsHelper {

    private static final String NAME = "case";
    private static final String SWITCH_VALUE_NAME = "switchValue";
    private static final String SWITCH_VALUE_FOUND = "switchValueFound";

    /**
     * @return return the helper's name.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * This method process case helper.
     *
     * @param caseValue "case" parameter value
     * @param options   conteins context variables, internal content.
     * @return Returns "case" helper's internal content if matches
     * switch parameter value, or null if not.
     * @throws IOException
     */
    @Override
    public CharSequence apply(final Object caseValue, final Options options) throws IOException {
        if (options == null) {
            return null;
        }
        Object switchValue = options.context.data(SWITCH_VALUE_NAME);
        CharSequence caseContent = null;
        if (switchValue != null && switchValue.equals(caseValue)) {
            /*"case" parameter value matches "switch" parameter value.
            *Therefore "true" value sets to switchValueFound variable*/
            options.context.data(SWITCH_VALUE_FOUND, Boolean.TRUE);
            caseContent = options.fn();
        }
        return caseContent;
    }
}
