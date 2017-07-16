/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The SF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.omnigon.aem.handlebars.engine.impl;

import com.github.jknack.handlebars.Handlebars;
import com.omnigon.aem.common.sling.ResourceResolverFactoryService;
import com.omnigon.aem.handlebars.template.SlingTemplateLoader;
import com.omnigon.aem.handlebars.engine.HandlebarsScriptEngine;
import com.omnigon.aem.handlebars.engine.HandlebarsScriptEngineFactory;
import com.omnigon.aem.handlebars.helpers.HandlebarsHelper;
import com.omnigon.aem.sling.mvp.context.ContextGeneratorFactory;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.Order;
import org.apache.sling.commons.osgi.RankedServices;
import org.apache.sling.scripting.api.AbstractScriptEngineFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import javax.script.ScriptEngine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component(
        label = "Apache Sling Scripting Handlebars",
        description = "Scripting engine for Handlebars templates",
        immediate = true,
        metatype = true
)
@Service
@Properties({
        @Property(name = Constants.SERVICE_VENDOR, value = "The Apache Software Foundation"),
        @Property(name = Constants.SERVICE_DESCRIPTION, value = "Scripting engine for Handlebars templates"),
        @Property(name = Constants.SERVICE_RANKING, intValue = 0, propertyPrivate = false)
})
public class HandlebarsScriptEngineFactoryImpl extends AbstractScriptEngineFactory implements HandlebarsScriptEngineFactory {

    @Reference(name = "contextGeneratorFactory",
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
            referenceInterface = ContextGeneratorFactory.class,
            policy = ReferencePolicy.DYNAMIC
    )
    private RankedServices<ContextGeneratorFactory> contextGeneratorFactories = new RankedServices<>(Order.ASCENDING);

    // Allows to auto-register helpers
    @Reference(cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, referenceInterface = HandlebarsHelper.class,
            policy = ReferencePolicy.DYNAMIC, bind = "bindHelper", unbind = "unbindHelper")
    private List<HandlebarsHelper> helpers = new ArrayList<>();

    public void bindHelper(HandlebarsHelper helper) {
        helpers.add(helper);
    }

    public void unbindHelper(HandlebarsHelper helper) {
        helpers.remove(helper);
    }

    public void bindContextGeneratorFactory(ContextGeneratorFactory contextGeneratorFactory, Map<String, Object> props) {
        contextGeneratorFactories.bind(contextGeneratorFactory, props);
    }

    public void unbindContextGeneratorFactory(ContextGeneratorFactory contextGeneratorFactory, Map<String, Object> props) {
        contextGeneratorFactories.unbind(contextGeneratorFactory, props);
    }

    // TODO: this does not resolve (works only for SCR defined properties?)
    @Reference(target = "(factory.label=handlebars)")
    private ResourceResolverFactoryService resolverFactory;

    public HandlebarsScriptEngineFactoryImpl() {
        setExtensions(HANDLEBARS_SCRIPT_EXTENSION);
        setMimeTypes(HANDLEBARS_MIME_TYPE);
        setNames(HANDLEBARS_SHORT_NAME);
    }

    @Activate
    @Modified
    void activate(BundleContext bundleContext) {

    }

    public String getLanguageName() {
        return "apachehandlebars";
    }

    public String getLanguageVersion() {
        return "1.0";
    }

    public List<ContextGeneratorFactory> getContextGeneratorFactories() {
        return contextGeneratorFactories.getList();
    }

    public ScriptEngine getScriptEngine() {
        Handlebars handlebars = createTemplateEngine();
        return new HandlebarsScriptEngine(this, handlebars);
    }

    private Handlebars createTemplateEngine() {
        Handlebars handlebars = new Handlebars(new SlingTemplateLoader(resolverFactory));
        for (HandlebarsHelper helper : helpers) {
            handlebars.registerHelper(helper.getName(), helper);
        }

        return handlebars;
    }

    @Deactivate
    void deactivate() {

    }

}
