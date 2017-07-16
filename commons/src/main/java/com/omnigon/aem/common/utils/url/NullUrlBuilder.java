package com.omnigon.aem.common.utils.url;

/**
 * User: Andrey Bardashevsky
 * Date/Time: 23.06.2014 14:29
 */
class NullUrlBuilder extends SlingUrlBuilder {
    @Override
    public SlingUrlBuilder setSuffix(final String suffix) {
        return this;
    }

    @Override
    public SlingUrlBuilder addSelector(final String selector) {
        return this;
    }

    @Override
    public SlingUrlBuilder setExtension(final String extension) {
        return this;
    }

    @Override
    public String build() {
        return null;
    }
}
