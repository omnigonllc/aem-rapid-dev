package com.omnigon.aem.common.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Yuri Kozub
 */
public final class PojoUtils {

    private PojoUtils(){};

    public static String[] safeArray(String[] array) {
        return array != null ? array.clone() : ArrayUtils.EMPTY_STRING_ARRAY;
    }

    public static <T> List<T> safeUnmodifiableList(List<T> list) {
        return CollectionUtils.isNotEmpty(list) ? Collections.unmodifiableList(list) : Collections.<T>emptyList();
    }

    public static Date unsafeDate(Date date) {
        return date != null ? (Date) date.clone() : null;
    }
}
