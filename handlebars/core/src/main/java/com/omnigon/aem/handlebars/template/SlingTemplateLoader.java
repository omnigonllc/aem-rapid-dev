package com.omnigon.aem.handlebars.template;

import com.day.cq.commons.jcr.JcrConstants;
import com.github.jknack.handlebars.io.AbstractTemplateLoader;
import com.github.jknack.handlebars.io.StringTemplateSource;
import com.github.jknack.handlebars.io.TemplateSource;
import com.omnigon.aem.common.sling.ResourceResolverFactoryService;
import com.omnigon.aem.handlebars.engine.HandlebarsScriptEngineFactory;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*
 * SlingTemplateLoader class provides Handlebars template resolving in JCR.
 * All script paths are relative to /apps folder to mirror how the Sling expects resourceType provided.
 * IMPORTANT:
 *  - no overlays are supported.
 *  - no relative paths supported.
 *
 * TODO: implement caching TemplateLoader by using GuavaCachedTemplateLoader as an example
 * Problem with GuavaCachedTemplateLoader is that it uses expiration instead of invalidation based on resource jcr:lastModified.
 * Better implementation would check listen changes to the hbs template on JCR level and would invalidate the cache related to it.
 * Alternatively, lastModified could be checked on each sourceAt call, but this still requires path resolution by Sling
 */
public class SlingTemplateLoader extends AbstractTemplateLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlingTemplateLoader.class);

    private static final String COMPONENTS_PATH_PREFIX = "/apps";
    private static final String SCRIPTS_SUFFEX = "." + HandlebarsScriptEngineFactory.HANDLEBARS_SCRIPT_EXTENSION;

    private ResourceResolverFactoryService resolverFactory;

    public SlingTemplateLoader(ResourceResolverFactoryService resolverFactory) {
        this.resolverFactory = resolverFactory;
        setPrefix(COMPONENTS_PATH_PREFIX);
        setSuffix(SCRIPTS_SUFFEX);
    }

    @Override
    public TemplateSource sourceAt(String location) throws IOException {

        TemplateSource templateSource = null;

        try( ResourceResolver resourceResolver =
                     resolverFactory.getDefaultResourceResolver()) {

            Resource scriptResource = resourceResolver.resolve( resolve(location) );
            InputStream is = scriptResource.adaptTo(InputStream.class);

            if(is != null) {
                BufferedReader templateReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                ValueMap scriptProperties = scriptResource.getChild(JcrConstants.JCR_CONTENT).getValueMap();
                long lastModified = scriptProperties.get(JcrConstants.JCR_LASTMODIFIED, 0l);

                templateSource = new ReaderTemplateSource(scriptResource.getPath(), lastModified, templateReader);

            } else {
                LOGGER.error("Cannot resolve Handlebars template at path {}", location);
                templateSource = new StringTemplateSource(location, "");
            }

        } catch (LoginException lex) {
            LOGGER.error("Cannot resolve Handlebars Template at {}", location, lex);
        }

        return templateSource;
    }

}
