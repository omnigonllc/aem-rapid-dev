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
 *   Uses two other subhelpers to evaluate switch functions.
 *
 *   {@link CaseHelper}
 *   {@link DefaultHelper}
 *
 */
@Service
@Component
public class SwitchHelper implements HandlebarsHelper {

    private static final String NAME = "switch";
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
     * This method process switch helper.
     * Renders content and stores "switch" parameter value in context
     * Sets "switch" parameter value.
     * @param switchValue "switch" parameter value
     * @param options conteins context variables, internal content.
     * @return Returns switch helper's inserted content(cases, "default" cases, etc).
     * @throws IOException
     */
    @Override
    public CharSequence apply(final Object switchValue, final Options options) throws IOException {
        if (options == null) {
            return null;
        }
        options.context.data(SWITCH_VALUE_NAME, switchValue);
        /*before any "case" parameter value match "switch" parameter value
        * "false" value sets to switchValueFound variable. Variable switchValueFound will be used to define
        * ignore or not "default" case*/
        options.context.data(SWITCH_VALUE_FOUND, Boolean.FALSE);
        CharSequence switchContent = options.fn();
        return switchContent;
    }
}
