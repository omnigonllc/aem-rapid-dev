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
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;
import com.omnigon.aem.sling.mvp.context.ContextGenerator;
import com.omnigon.aem.sling.mvp.model.ModelGenerator;

import javax.script.Bindings;
import javax.script.ScriptContext;
import java.util.HashMap;
import java.util.Map;

/**
 * The very basic implementation of ContextGenerator for Handlebars.
 * Only passes Sling request, response and resolver objects to the Handlebars Context.
 */
public class DefaultHandlebarsContextGenerator implements ContextGenerator<Context> {

    public static final String REQUEST_KEY = "_request";
    public static final String RESPONSE_KEY = "_response";
    public static final String RESOLVER_KEY = "_resolver";

    private ModelGenerator modelGenerator;

    public DefaultHandlebarsContextGenerator(ModelGenerator modelGenerator) {
        this.modelGenerator = modelGenerator;
    }

    public ModelGenerator getModelGenerator() {
        return modelGenerator;
    }

    protected Map<String, Object> getBaseContext(SlingScriptHelper scriptHelper) {
        Map<String, Object> baseContextMap = new HashMap<String, Object>();

        /* Put Sling core variables into root Handlebars Context */
        SlingHttpServletRequest request = scriptHelper.getRequest();
        baseContextMap.put(REQUEST_KEY, request);
        baseContextMap.put(RESPONSE_KEY, scriptHelper.getResponse());
        baseContextMap.put(RESOLVER_KEY, request.getResourceResolver());

        return baseContextMap;
    }

    protected SlingScriptHelper getSlingScriptHelper(ScriptContext context) {
        final Bindings props = context.getBindings(ScriptContext.ENGINE_SCOPE);
        return (SlingScriptHelper) props.get(SlingBindings.SLING);
    }

    @Override
    public Context createContext(ScriptContext scriptContext) {

        Map<String, Object> model = getModelGenerator().createModel(scriptContext);
        Map<String, Object> baseContextMap = getBaseContext(getSlingScriptHelper(scriptContext));

        if (model != null) {
            model.putAll(baseContextMap);
            Context ctx = Context.newBuilder(model).build();
            return ctx;
        } else {
            return Context.newBuilder(baseContextMap).build();
        }

    }

}
