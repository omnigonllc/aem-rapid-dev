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
