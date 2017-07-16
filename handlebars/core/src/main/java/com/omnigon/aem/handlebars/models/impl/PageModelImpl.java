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

import com.day.cq.commons.Externalizer;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Design;
import com.omnigon.aem.handlebars.models.PageModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.Locale;

/**
 * Represents Page Model with page attributes.
 * <p>
 */
@Model(adaptables = {SlingHttpServletRequest.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageModelImpl implements PageModel {

    private static Logger LOGGER = LoggerFactory.getLogger(PageModelImpl.class);

    @Self
    private SlingHttpServletRequest request;

    @Inject
    protected transient Page currentPage;

    /**
     * Iif there is no cq:designPath in jcr:content, it will be set to /etc/designs/default.
     */
    @Inject
    private transient Design currentDesign;

    @Inject
    protected Externalizer externalizer;

    /**
     * Page title to be displayed in &lt;title&gt; tag
     */
    @Inject @Named(JcrConstants.JCR_TITLE)
    private String title;

    /**
     * Relative page path
     */
    protected String path;

    /**
     * External relative URL
     */
    private String url;

    /**
     * Current page's locale
     */
    private Locale locale;

    /**
     * Current page design path
     */
    private String designPath;

    @PostConstruct
    protected void activate() {
        locale = request.getLocale();
        path = getCurrentPage().getPath();
        designPath = currentDesign.getPath();
        // Obtain ResourceResolver from Handlebars Resource Resolver Service
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected Page getCurrentPage() {
        return currentPage;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getUrl() {
        return url;
    }

    public String getLang() {
        return locale.getLanguage();
    }

    public String getLocale() {
        return locale.toString();
    }

    public String getDesignPath() {
        return designPath;
    }

    public SlingHttpServletRequest getRequest() {
        return request;
    }

    public ValueMap getProperties() {
        return getCurrentPage().getProperties();
    }


}
