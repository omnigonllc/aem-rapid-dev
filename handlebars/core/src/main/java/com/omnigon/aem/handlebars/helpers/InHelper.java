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