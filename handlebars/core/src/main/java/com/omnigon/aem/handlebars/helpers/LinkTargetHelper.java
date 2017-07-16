package com.omnigon.aem.handlebars.helpers;

import com.github.jknack.handlebars.Options;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.io.IOException;

/**
 * Created by daniil.sheidak on 24.10.2016.
 * This Handlebars' Helper class returns "_self" or "_blank".
 * If target equals to "new" helper should return "_blank".
 * The "_self" should be returned in other case (i.e. "same").
 * If target is omitting "same" is default.
 * <p>
 * Examples:
 * <p>
 * {{linkTarget someProp}}
 *
 * Syntax
 * {{linkTarget [target]}}
 *
 */
@Service
@Component
public class LinkTargetHelper implements HandlebarsHelper {

    private static final String NAME = "linkTarget";
    private static final String SELF_RETURN_VALUE = "_self";
    private static final String BLANK_RETURN_VALUE = "_blank";
    private static final String BLANK_TARGET_KEY_VALUE = "new";

    /**
     * @return Returns helper's name
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * This method process linkTarget helper.
     * @param target target param.
     * @param options
     * @return Returns "_self" or "_blank".
     * If target equals to "new" helper should return "_blank".
     * The "_self" should be returned in other case (i.e. "same").
     * If target is omitting "same" is default.
     * @throws IOException
     */
    @Override
    public CharSequence apply(final Object target, final Options options) throws IOException {
        if(BLANK_TARGET_KEY_VALUE.equals(target)) {
            return BLANK_RETURN_VALUE;
        }
        return SELF_RETURN_VALUE;
    }
}
