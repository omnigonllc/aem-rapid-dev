package com.omnigon.aem.common.utils.url;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.request.RequestPathInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Andrey Bardashevsky
 * Date/Time: 23.06.2014 14:25
 */
class DefaultSlingUrlBuilder extends SlingUrlBuilder {
    public static final String HTML_EXTENSION = ".html";

    private final String contentPath;

    private List<String> selectors = new ArrayList<String>();

    private String suffix;

    private String extension = HTML_EXTENSION;

    DefaultSlingUrlBuilder(final RequestPathInfo pathInfo) {
        this.contentPath = pathInfo.getResourcePath();
        Collections.addAll(this.selectors, pathInfo.getSelectors());
        this.extension = StringUtils.defaultIfBlank(pathInfo.getExtension(), HTML_EXTENSION);
        this.suffix = pathInfo.getSuffix();
    }

    @Override
    public SlingUrlBuilder setSuffix(String suffix) {
        this.suffix = suffix;

        return this;
    }

    @Override
    public SlingUrlBuilder addSelector(String selector) {
        this.selectors.add(selector);

        return this;
    }

    @Override
    public SlingUrlBuilder setExtension(String extension) {
        this.extension = extension;

        return this;
    }

    @Override
    public String build() {
        StringBuilder b = new StringBuilder(contentPath);

        for (String selector : selectors) {
            b.append(".");
            b.append(selector);
        }

        if (StringUtils.isNotBlank(extension)) {
            if (StringUtils.startsWith(extension, ".")) {
                b.append(extension);
            } else {
                b.append(".");
                b.append(extension);
            }
        }

        if (StringUtils.isNotBlank(suffix)) {
            if (StringUtils.startsWith(suffix, "/")) {
                b.append(suffix);
            } else {
                b.append("/");
                b.append(suffix);
            }
        }

        return  b.toString();
    }

}
