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
package com.omnigon.aem.common.utils.url;

import com.day.cq.commons.PathInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.request.RequestPathInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

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
