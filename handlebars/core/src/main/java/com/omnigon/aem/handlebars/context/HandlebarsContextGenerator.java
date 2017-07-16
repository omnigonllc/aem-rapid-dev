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
package com.omnigon.aem.handlebars.context;

import com.github.jknack.handlebars.Context;
import com.omnigon.aem.handlebars.models.PageModel;
import com.omnigon.aem.handlebars.models.RequestModel;
import com.omnigon.aem.handlebars.models.impl.PageModelImpl;
import com.omnigon.aem.handlebars.models.impl.RequestModelImpl;
import com.omnigon.aem.sling.mvp.context.ContextGenerator;
import com.omnigon.aem.sling.mvp.model.ModelGenerator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingScriptHelper;

import java.util.Map;

/**
 * This handlebars context generator implementation provides passing custom Request and Page models
 * to the Handlebars Context.
 */
public class HandlebarsContextGenerator extends DefaultHandlebarsContextGenerator implements ContextGenerator<Context> {

    public static final String REQUEST_MODEL_KEY = "_r"; // cannot use 'request' as it is already used by AEM
    public static final String PAGE_MODEL_KEY = "page";
    private static final String HANDLEBARS_REQUEST_MODEL_KEY = "request";
    private static final String HANDLEBARS_PAGE_MODEL_KEY = "page";

    public HandlebarsContextGenerator(ModelGenerator modelGenerator) {
        super(modelGenerator);
    }

    private RequestModel getRequestModel(SlingHttpServletRequest request) {
        RequestModel requestModel = (RequestModel)request.getAttribute(REQUEST_MODEL_KEY);
        if (requestModel == null) { // no RequestModel created yet, so create a default one
            requestModel = request.adaptTo(RequestModelImpl.class);
            request.setAttribute(REQUEST_MODEL_KEY, requestModel);
        }
        return requestModel;
    }

    private PageModel getPageModel(SlingHttpServletRequest request) {
        PageModel pageModel = (PageModel)request.getAttribute(PAGE_MODEL_KEY);
        if (pageModel == null) { // no PageModel created yet, so create a default one
            pageModel = request.adaptTo(PageModelImpl.class);
            request.setAttribute(PAGE_MODEL_KEY, pageModel);
        }
        return pageModel;
    }

    protected Map<String, Object> getBaseContext(SlingScriptHelper scriptHelper) {
        Map<String, Object> baseContextMap = super.getBaseContext(scriptHelper);
        SlingHttpServletRequest request = scriptHelper.getRequest();

        /* Put Request metadata into root Handlebars Context */
        RequestModel requestModel = getRequestModel(request);
        if (requestModel != null) {
            baseContextMap.put(HANDLEBARS_REQUEST_MODEL_KEY, request); // or baseContextMap.put("request", requestModel);
        }

        /* Put Page metadata into root Handlebars Context */
        PageModel pageModel = getPageModel(request);
        if (pageModel != null) {
            baseContextMap.put(HANDLEBARS_PAGE_MODEL_KEY, pageModel);
        }

        return baseContextMap;
    }

}
