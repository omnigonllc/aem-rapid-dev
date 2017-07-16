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
package com.omnigon.aem.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Olexiy on 11.02.2015.
 */
public final class ParserUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(ParserUtil.class);

    private ParserUtil() {}

    /**
     *
     * @param array incoming parameters
     * @param type type of Object to map
     * @param <T> -
     * @return Object with mapped parameters
     */
    public static <T> ImmutableList<T> parseJsonArray(String[] array, Class<T> type) {
        final ImmutableList.Builder<T> builder = ImmutableList.builder();
        final ObjectReader reader = OBJECT_MAPPER.reader(type);
        for (String element : ArrayUtils.nullToEmpty(array)) {
            try {
                builder.add(reader.<T>readValue(element));
            } catch (IOException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }

        return builder.build();
    }
}
