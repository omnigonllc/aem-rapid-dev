package com.omnigon.aem.common.utils.url;

/**
 * User: Andrey Bardashevsky
 * Date/Time: 23.06.2014 14:29
 */
class AbsoluteUrlBuilder extends SlingUrlBuilder {
    private final String url;

    AbsoluteUrlBuilder(final String url) {
        this.url = url;
    }

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
        return url;
    }
}
