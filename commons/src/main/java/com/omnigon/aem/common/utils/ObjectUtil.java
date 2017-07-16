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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.Collections;

/**
 * Created by brenn.hill on 03/11/2014.
 */
public final class ObjectUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ObjectUtil.class);

    /* private constructor for static class */
    private ObjectUtil() {

    }

    /**
     * Converts an object that contains an array of strings into a string array.
     *
     * @param o the object that is an array
     * @return converted Object to string array.
     */
    public static String[] extractStringArrayFromObject(Object o) {
        String[] strings = new String[0];
        Class oClass = o.getClass();
        if (oClass.isArray()) {
            strings = new String[Array.getLength(o)];
            for (int i = 0; i < Array.getLength(o); i++) {
                try {
                    strings[i] = (String) Array.get(o, i);
                } catch (Exception e) {
                    //not a string, shouldn't happen.
                    LOG.error("Failure trying to convert object to strings.  Possible data corruption. ");
                }
            }
        } else {
            try {
                strings = new String[1];
                strings[0] = (String) o;
            } catch (Exception e) {
                LOG.error("Failure trying to convert object to strings.  Possible data corruption. ");
            }
        }
        return strings;
    }

    /**
     * Takes an object and attempts to convert it to type of Class c
     *
     * @param o the object that is an array
     * @param c Class that you believe makes up the array
     * @return an array of the passed class or null if the conversion fails.
     */
    public static <T> T[] extractArrayFromObject(Object o, Class<T> c) {
        if (null == o) {
            if (c != null) {
                return (T[]) Array.newInstance(c, 0);
            } else {
                return null;
            }
        }
        T[] objectArray = null;
        Class oClass = o.getClass();
        if (oClass.isArray()) {
            objectArray = (T[]) Array.newInstance(c, Array.getLength(o));
            for (int i = 0; i < Array.getLength(o); i++) {
                try {
                    objectArray[i] = (T) Array.get(o, i);
                } catch (Exception e) {
                    //not a string, shouldn't happen.
                    LOG.error("Failure trying to convert object to type.  Possible data corruption. ");
                }
            }
        } else {
            try {
                objectArray = (T[]) Array.newInstance(c, 1);
                objectArray[0] = (T) o;
            } catch (Exception e) {
                LOG.error("Failure trying to convert object to type.  Possible data corruption. ");
            }
        }
        return objectArray;
    }

    /**
     *
     * Null check for looping collections
     *
     * @param iterable collection
     * @param <T> type
     * @return collection (empty or the same)
     */
    public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }

}