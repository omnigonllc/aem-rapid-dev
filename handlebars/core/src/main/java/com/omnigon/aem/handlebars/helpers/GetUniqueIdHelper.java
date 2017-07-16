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
import com.github.jknack.handlebars.TagType;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.io.IOException;

// TODO: refactor GetUniqueIdHelper, UniqueId, UniqueIdHelper
@Service
@Component
public class GetUniqueIdHelper implements HandlebarsHelper {

    private static final String NAME = "getUniqueId";
    private static final String ID_PREFIX = "id-";
    private static final Integer ID_SIZE = 9;

    /**
     * @return Returns helper's name
     */
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public CharSequence apply(Object context, Options options) throws IOException {
        if (TagType.VAR == options.tagType || TagType.SUB_EXPRESSION == options.tagType) {
            int contextHashCode = context.hashCode();
            String scriptPath = options.fn.filename();
            String uniqueId = UniqueId.generateUniqueId(scriptPath + contextHashCode);
            return ID_PREFIX + StringUtils.substring(uniqueId, uniqueId.length() - ID_SIZE);
        }
        return StringUtils.EMPTY;
    }
}
