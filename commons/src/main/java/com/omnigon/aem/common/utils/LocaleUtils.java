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

import com.day.cq.commons.LanguageUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class LocaleUtils {

    private LocaleUtils() {}

    public static boolean isValidAndNonEnglish(Locale locale) {
        return locale != null && StringUtils.isNotBlank(locale.getLanguage())
                && !StringUtils.equalsIgnoreCase(Locale.ENGLISH.getLanguage(), locale.getLanguage());
    }

    public static String getLanguageCountryCode(Locale locale) {
        if (locale !=  null) {
            StringBuilder b = new StringBuilder();

            if (StringUtils.isNotBlank(locale.getLanguage())) {
                b.append(locale.getLanguage().toLowerCase());
            }

            if (StringUtils.isNotBlank(locale.getCountry())) {
                b.append("_");
                b.append(locale.getCountry().toLowerCase());
            }

            return b.toString();
        } else {
            return StringUtils.EMPTY;
        }
    }

    public static Locale getLocale(String code) {
        return StringUtils.isNotBlank(code) ? LanguageUtil.getLocale(code) : null;
    }
}
