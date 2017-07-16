/*
 * Copyright (c) 2016 Omnigon Communications, LLC. All rights reserved.
 *
 * This software is the confidential and proprietary information of Omnigon Communications, LLC
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall access and use it only
 * in accordance with the terms of the license agreement you entered into with Omnigon Communications, LLC, its
 * subsidiaries, affiliates or authorized licensee. Unless required by applicable law or agreed to in writing, this
 * Confidential Information is provided on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the license agreement for the specific language governing permissions and limitations.
 */

package com.omnigon.aem.handlebars.models.impl;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.WCMMode;
import com.omnigon.aem.handlebars.models.RequestModel;
import com.omnigon.aem.sling.mvp.model.presenter.JacksonSerializable;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Locale;

/**
 * Represents Request model with all necessary request attributes.
 */
@Model(adaptables = { SlingHttpServletRequest.class },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL )
public class RequestModelImpl implements RequestModel {

    @Optional
    @Inject
    private transient Page currentPage;

    @Optional
    @Inject
    private transient SlingHttpServletRequest request;

    /*
      The variables below can be injected via @Inject @RequestAttribute("<attribute name>")
      We may avoid initializing most of them if we us Request directly
    */
    private transient WCMMode wcmMode; // name = "com.day.cq.wcm.api.WCMMode"

    private String requestUri; // "javax.servlet.include.request_uri"

    private String queryString; // "javax.servlet.include.query_string"

    private String servletPath; // "javax.servlet.include.servlet_path"

    private String pathInfo; // "javax.servlet.include.path_info"

    private String contextPath; // "javax.servlet.include.context_path"

    private transient RequestPathInfo requestPathInfo; // "org.apache.sling.api.include.request_path_info"

    private Locale locale;

    @PostConstruct
    protected void activate() {
        if(request != null) {
            wcmMode = WCMMode.fromRequest(request);
            requestUri = request.getRequestURI();
            queryString = request.getQueryString();
            contextPath = request.getContextPath();
            pathInfo = request.getPathInfo();
            servletPath = request.getServletPath();
            requestPathInfo = request.getRequestPathInfo();
            locale = request.getLocale();
        }
    }

    public Page getCurrentPage() {
        return currentPage;
    }

    public SlingHttpServletRequest getRequest() {
        return request;
    }

    public String getWcmMode() {
        return wcmMode.name();
    }

    public String getRequestURI() {
        return requestUri;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getServletPath() {
        return servletPath;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public String getContextPath() {
        return contextPath;
    }

    public Locale getLocale() {
        return locale;
    }

}
