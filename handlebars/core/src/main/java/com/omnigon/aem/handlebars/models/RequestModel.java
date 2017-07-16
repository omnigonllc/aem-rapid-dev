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

package com.omnigon.aem.handlebars.models;

import com.day.cq.wcm.api.Page;
import com.omnigon.aem.sling.mvp.model.presenter.JacksonSerializable;
import org.apache.sling.api.SlingHttpServletRequest;

import java.util.Locale;

/**
 * Represents Base Request model with all necessary request attributes.
 */
public interface RequestModel extends JacksonSerializable {

    Page getCurrentPage();

    SlingHttpServletRequest getRequest();

    String getWcmMode();

    String getRequestURI();

    String getQueryString();

    String getServletPath();

    String getPathInfo();

    String getContextPath();

    Locale getLocale();

}
