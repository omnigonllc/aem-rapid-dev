package com.omnigon.aem.sling.mvp.model;

import javax.script.ScriptContext;
import java.util.Map;

public interface ModelGenerator {

    Map<String, Object> createModel(ScriptContext scriptContext);

}
