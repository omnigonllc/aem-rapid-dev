package com.omnigon.aem.sling.mvp.model;

import com.omnigon.aem.sling.mvp.context.ScriptContextAdapter;

import javax.script.Bindings;
import javax.script.ScriptContext;
import java.util.HashMap;
import java.util.Map;

/**
 * This class generates a Handlebars model by serializing available bindings and current resource properties into a Map.
 */
public class SimpleModelGenerator implements ModelGenerator {

    private static final String MODEL_KEY = "properties";

    @Override
    public Map<String, Object> createModel(ScriptContext scriptContext) {
        Map<String, Object> model = new HashMap<>();
        Bindings bindings = scriptContext.getBindings(ScriptContext.ENGINE_SCOPE);
        for (Object entryObj : bindings.entrySet()) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) entryObj;
            model.put((String) entry.getKey(), entry.getValue());
        }

        model.put(MODEL_KEY, new ScriptContextAdapter(scriptContext).getResource().getValueMap());
        return model;
    }

}
