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
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
@Component
public class HashmapHelper implements HandlebarsHelper {

    private static final String NAME = "hashMap";

    /**
     * @return Returns helper's name
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     *
     * @param context Contains resource properties
     * @param options Contains context variables.
     * @return Returns map that consists listed pairs.
     * If no pairs specified the map will be empty
     * @throws IOException
     */
    @Override
    public Map<String, Object> apply(final Object context, final Options options) throws IOException {
        if (options == null) {
            return new HashMap<String, Object>();
        }
        return options.hash;
    }
}
