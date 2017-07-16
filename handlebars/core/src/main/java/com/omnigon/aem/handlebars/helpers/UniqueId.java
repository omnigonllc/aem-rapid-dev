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

import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class UniqueId {

    private static final Logger logger = LoggerFactory.getLogger(UniqueId.class);

    private static final Integer UNIQUE_ID_LENGTH = 12;
    private static final String MESSAGE_DIGEST_TYPE = "MD5";

    public static String generateUniqueId(String directoryPath) {
        byte[] bytesDirectoryPath = null;
        MessageDigest md = null;
        try {
            bytesDirectoryPath = directoryPath.getBytes(CharEncoding.UTF_8);
            md = MessageDigest.getInstance(MESSAGE_DIGEST_TYPE);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            return StringUtils.EMPTY;
        }
        md.reset();
        md.update(bytesDirectoryPath);
        String uniqueId = DatatypeConverter.printHexBinary( md.digest() );
        return StringUtils.substring(uniqueId, 0, UNIQUE_ID_LENGTH);
    }

}
