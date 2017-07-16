/*
 * Copyright (c) 2017 Omnigon Communications, LLC. All rights reserved.
 *
 * This software is the confidential and proprietary information of Omnigon Communications, LLC
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall
 * in accordance with the terms of the license agreement you entered into with Omnigon Communications, LLC, its
 * subsidiaries, affiliates or authorized licensee. Unless required by applicable law or agreed to in writing, this
 * Confidential Information is provided on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the license agreement for the specific language governing permissions and limitations.
 */
package com.omnigon.aem.handlebars.helpers;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.io.IOException;

/**
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

