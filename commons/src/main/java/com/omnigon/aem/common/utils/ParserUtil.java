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
