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
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public final class StringUtils {
    private StringUtils() { }
    private final static String PUNCTUATION = ".!?";
    private final static String ELLIPSES = "...";

    /**
     *
     * @param tag - name
     * @param src source string
     * @return string without tag inside
     */
    public static String removeTag(String tag, String src) {
        int start = src.indexOf("<" + tag);
        String resultSrc = src;
        while (start >= 0) {
            int end = resultSrc.lastIndexOf("</" + tag + ">");
            resultSrc = resultSrc.substring(0, start) + (end >= 0 ? resultSrc.substring(end) : "");
            start = resultSrc.indexOf("<" + tag);
        }
        return resultSrc;
    }

    /**
     *
     * @param src source string
     * @return string without any HTML tags inside
     */
    public static String removeHtmlTags(String src) {
        return Jsoup.clean(src, Whitelist.none());
    }

    public static String shortenString(String src, int length, boolean addEllipses) {
        if (isNotBlank(src) && src.length() > length) {
            if (addEllipses) {
                String sub = src.substring(0, length);
                char last = sub.charAt(sub.length() - 1);

                return PUNCTUATION.contains(Character.toString(last)) ? sub : sub + ELLIPSES;
            } else {

                return src.substring(0, length);
            }
        }

        return src;
    }

    /**
     *
     * @param builder StringBuilder object
     * @param isTrue some condition
     * @param appendStrings text list to concat
     */
    public static void builderAppend(StringBuilder builder, boolean isTrue, String... appendStrings) {
        if (isTrue) {
            for (String value : appendStrings) {
                builder.append(value);
            }
        }
    }

}
