package com.omnigon.aem.sling.mvp.context;

import javax.script.ScriptContext;

public interface ContextGenerator<T> {

    T createContext(ScriptContext scriptContext);

}
