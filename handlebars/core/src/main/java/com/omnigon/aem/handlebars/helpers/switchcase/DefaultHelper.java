package com.omnigon.aem.handlebars.helpers.switchcase;

import com.github.jknack.handlebars.Options;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import com.omnigon.aem.handlebars.helpers.HandlebarsHelper;

import java.io.IOException;

/**
 * Created by daniil.sheidak on 17.10.2016.
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
public class DefaultHelper implements HandlebarsHelper {

    private static final String NAME = "default";
    private static final String SWITCH_VALUE_FOUND = "switchValueFound";

    /**
     * @return return the helper's name.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * This method process default helper.
     *
     * @param context Equals null, because
     *                default helper hasn't got parameters.
     * @param options conteins context variables, internal content.
     * @return Returns default helper's internal content if previous
     * case helper's parameter values didn't match switch parameter value
     * @throws IOException
     */
    @Override
    public CharSequence apply(final Object context, final Options options) throws IOException {
        if (options == null) {
            return null;
        }
        CharSequence caseContent = null;
        Object isSwitchValueFound = options.context.data(SWITCH_VALUE_FOUND);
        /*if there is no "case" which parameter values matches "switch" parameter value.
        * And as a result variable switchValueFound equals false. In this case "default"
        * case will be processed*/
        if (Boolean.FALSE.equals(isSwitchValueFound)) {
            caseContent = options.fn();
        }
        return caseContent;
    }
}
