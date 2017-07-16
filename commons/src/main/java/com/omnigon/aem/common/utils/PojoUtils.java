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
