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

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;

/**
 * Represents Page Model with page attributes.
 * <p>
 * TODO: Foundation Page component should be rewritten to use Handlebars implementation instead of (JSP or Sightly)
 * TODO: Create Handlebars Resource Resolver Service to access JCR repository
 */
public interface PageModel {

    String getTitle();

    String getPath();

    String getUrl();

    String getLang();

    String getLocale();

    String getDesignPath();

    SlingHttpServletRequest getRequest();

    ValueMap getProperties();

}
