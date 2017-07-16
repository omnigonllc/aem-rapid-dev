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
package com.omnigon.aem.handlebars.engine;

import com.day.cq.commons.jcr.JcrConstants;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.TemplateSource;
import com.omnigon.aem.handlebars.template.ReaderTemplateSource;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.scripting.api.AbstractSlingScriptEngine;
import com.omnigon.aem.sling.mvp.context.ContextGeneratorFactory;
import com.omnigon.aem.sling.mvp.context.ScriptContextAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

/**
 * Handlerbars implementation of a Sling Script Engine.
 */
public class HandlebarsScriptEngine extends AbstractSlingScriptEngine {

    private static final Logger LOG = LoggerFactory.getLogger(HandlebarsScriptEngine.class);
    private Collection<ContextGeneratorFactory> contextGeneratorFactories;
    private Handlebars handlebars;

    public HandlebarsScriptEngine(
            HandlebarsScriptEngineFactory handlebarsScriptEngineFactory, Handlebars handlebars) {
        super(handlebarsScriptEngineFactory);
        contextGeneratorFactories = handlebarsScriptEngineFactory.getContextGeneratorFactories();
        this.handlebars=handlebars;
    }

    public Object eval(Reader templateReader, ScriptContext scriptContext) throws ScriptException {
        try {
            Template template = getTemplate(templateReader, scriptContext, handlebars);
            Context context = createContext(scriptContext);
            if (context != null) {
                template.apply(context, scriptContext.getWriter());
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }


    private Context createContext(ScriptContext scriptContext) {
        Context result = null;
        for (ContextGeneratorFactory contextGeneratorFactory : contextGeneratorFactories) {
            Object newContext = contextGeneratorFactory.getContextGenerator().createContext(scriptContext);
            if (newContext instanceof Context) {
                result = (Context) newContext;
                break;
            }
        }
        return result;
    }

    private Template getTemplate(Reader templateReader, ScriptContext scriptContext, Handlebars handlebars) throws IOException {
        ScriptContextAdapter contextAdapter = new ScriptContextAdapter(scriptContext);
        Resource scriptResource = contextAdapter.getScriptHelper().getScript().getScriptResource();

        ValueMap scriptProperties;
        Resource child = scriptResource.getChild(JcrConstants.JCR_CONTENT);
        long lastModified = 0l;
        if(child != null) {
            scriptProperties = child.getValueMap();
            lastModified = scriptProperties.get(JcrConstants.JCR_LASTMODIFIED, lastModified);
        }

        TemplateSource templateSource = new ReaderTemplateSource(scriptResource.getPath(), lastModified, templateReader);
        return handlebars.compile(templateSource);
    }

}
