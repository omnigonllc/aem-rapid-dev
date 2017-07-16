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
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.io.IOException;

@Service
@Component
public class OrHelper implements HandlebarsHelper {

    private static final String NAME = "or";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Object apply(Object context, Options options) throws IOException {
        Object returningValue = StringUtils.EMPTY;
        if (!options.context.model().equals(context) && isValidValue(context)) {
            returningValue = context;
        }
        for (Object param : options.params) {
            if (isValidValue(param)) {
                returningValue = param;
            }
        }
        return returningValue;
    }

    private Boolean isValidValue(Object value) {
        if (value == null || StringUtils.EMPTY.equals(value) || Boolean.FALSE.equals(value)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
