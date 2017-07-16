package com.omnigon.aem.common.utils.url;

import com.day.cq.commons.PathInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.request.RequestPathInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * User: Andrey Bardashevsky
 * Date/Time: 23.06.2014 14:24
 */
public abstract class SlingUrlBuilder {

    static final Logger LOG = LoggerFactory.getLogger(SlingUrlBuilder.class);

    public static SlingUrlBuilder create(final String uri) {
        if (StringUtils.isNotBlank(uri)) {
            try {
                URI u = URLUtils.createURI(uri);
                String path = u.getPath();

                if (u.isAbsolute() || StringUtils.isBlank(path)) {
                    return new AbsoluteUrlBuilder(u.toASCIIString());
                } else {
                    if (!StringUtils.startsWith(path, "/")) {
                        path = "/" + path;
                    }

                    return create(new PathInfo(path));
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return new NullUrlBuilder();
    }

    public static SlingUrlBuilder create(final RequestPathInfo pathInfo) {
        return new DefaultSlingUrlBuilder(pathInfo);
    }

    public abstract SlingUrlBuilder setSuffix(String suffix);

    public abstract SlingUrlBuilder addSelector(String selector);

    public abstract SlingUrlBuilder setExtension(String extension);

    public abstract String build();
}
