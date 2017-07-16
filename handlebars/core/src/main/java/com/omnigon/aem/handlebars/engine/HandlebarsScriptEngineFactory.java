package com.omnigon.aem.handlebars.engine;

import com.omnigon.aem.sling.mvp.context.ContextGeneratorFactory;

import javax.script.ScriptEngineFactory;
import java.util.Collection;
import java.util.List;

/**
 * Created by daniil.sheidak on 28.10.2016.
 */
public interface HandlebarsScriptEngineFactory extends ScriptEngineFactory {

    String HANDLEBARS_SCRIPT_EXTENSION = "hbsx";
    String HANDLEBARS_MIME_TYPE = "text/x-handlebars";
    String HANDLEBARS_SHORT_NAME = "handlebars";

    List<ContextGeneratorFactory> getContextGeneratorFactories();

}
