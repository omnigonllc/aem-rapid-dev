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

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * User: Andrey Bardashevsky
 * Date/Time: 08.02.14 13:40
 */
public final class PathExternalizer {

    private PathExternalizer() {}

    private static final Logger LOG = LoggerFactory.getLogger(PathExternalizer.class);

    private static final String DEFAULT_SCHEME = "http";
    private static final String SECURE_SCHEME = "https";
    private static final String SCHEME_SEP = "://";

    /**
     *
     * @param request -
     * @param path URL link
     * @return URL link with domain
     */
    public static String absoluteLink(final SlingHttpServletRequest request, final String path) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }

        StringBuilder url = new StringBuilder();
        url.append(request.getScheme()).append(SCHEME_SEP);

        ResourceResolver resolver = request.getResourceResolver();

        URI uri = URI.create(resolver != null ? resolver.map(request, path) : path);

        if (uri.getAuthority() == null) {
            url.append(getAuthority(request.getScheme(), request.getServerName(), request.getServerPort()));
        } else {
            url.append(uri.getAuthority());
        }

        url.append(request.getContextPath());

        url.append(uri.getPath());

        if (uri.getQuery() != null) {
            url.append("?");
            url.append(uri.getQuery());
        }

        if (uri.getFragment() != null) {
            url.append("#");
            url.append(uri.getFragment());
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("externalizing absolute link (request scope): {} -> {}", path, url);
        }

        return url.toString();
    }

    private static String getAuthority(String scheme, String host, int port) {
        boolean checkPort = port <= 0;
        boolean isDefaultScheme = DEFAULT_SCHEME.equals(scheme) && port == 80;
        boolean isSecureScheme = SECURE_SCHEME.equals(scheme) && port == 443;

        return checkPort || isDefaultScheme || isSecureScheme ? host : host + ":" + port;
    }

}
