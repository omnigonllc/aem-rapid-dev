package com.omnigon.aem.common.utils;

import com.day.cq.commons.LanguageUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * User: Andrey Bardashevsky
 * Date/Time: 01.04.2014 22:46
 */
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
