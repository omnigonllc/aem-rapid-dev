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

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

@Service
@Component
public class UniqueIdHelper implements HandlebarsHelper {

    private static final Logger LOG = LoggerFactory.getLogger(UniqueIdHelper.class);

    private static final String NAME = "uniqueId";
    private static final String MESSAGE_DIGEST_TYPE = "MD5";
    private static final Integer UNIQUE_ID_LENGTH = 10;
    private static final String UNIQUE_ID_PREFIX = "id-";


    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public CharSequence apply(final Object context, final Options options) throws IOException {

        Context ctx = options.context;
        String uniqueId = (String)ctx.get("uniqueId");

        if(StringUtils.isEmpty(uniqueId)) {
            uniqueId = generateUniqueId();

            Object model = ctx.model();
            if(model instanceof Map) {
                Map modelMap = ((Map) ctx.model());
                modelMap.put("uniqueId", uniqueId);
            } else {
                // TODO: issue warning
            }

        }

        return uniqueId;
    }

    private String formatUniqueId(String uniqueId) {
        String formattedUniqueId = StringUtils.substring(UNIQUE_ID_PREFIX + uniqueId, 0, UNIQUE_ID_LENGTH);
        return formattedUniqueId;
    }

    private String generateUniqueId() {
        byte[] bytesDirectoryPath;
        MessageDigest md;
        try {
            bytesDirectoryPath = UUID.randomUUID().toString().getBytes(CharEncoding.UTF_8);
            md = MessageDigest.getInstance(MESSAGE_DIGEST_TYPE);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
            return StringUtils.EMPTY;
        }
        md.reset();
        md.update(bytesDirectoryPath);

        String uniqueId = DatatypeConverter.printHexBinary(md.digest());
        String formattedUniqueId = formatUniqueId(uniqueId);

        return formattedUniqueId;
    }
}
