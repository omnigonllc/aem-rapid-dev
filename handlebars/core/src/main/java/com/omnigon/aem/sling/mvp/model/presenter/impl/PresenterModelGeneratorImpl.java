package com.omnigon.aem.sling.mvp.model.presenter.impl;

import aQute.bnd.annotation.component.Deactivate;
import com.omnigon.aem.sling.mvp.context.ScriptContextAdapter;
import com.omnigon.aem.sling.mvp.model.presenter.BeanToMapSerializer;
import com.omnigon.aem.sling.mvp.model.presenter.PresenterBundleListener;
import com.omnigon.aem.sling.mvp.model.presenter.PresenterModelGenerator;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.ComponentContext;


import javax.script.ScriptContext;
import java.util.HashMap;
import java.util.Map;

@Component
@Service
@Properties(value = {
        @Property(name = "service.description", value = "Handlebars: Presenter Model Generator")}
)
public class PresenterModelGeneratorImpl implements PresenterModelGenerator {

    private PresenterBundleListener presenterBundleListener;

    @Reference
    private BeanToMapSerializer beanToMapSerializer;

    @Override
    public Map<String, Object> createModel(ScriptContext scriptContext) {

        ScriptContextAdapter scriptContextAdapter = new ScriptContextAdapter(scriptContext);
        Resource resource = scriptContextAdapter.getResource();

        Map<String, Object> model;
        Class<?> presenterType = getPresenterType(resource);
        if (presenterType != null) {
            Object presenter = resource.adaptTo(presenterType);
            // TODO: if we put model under key (e.g. 'data') we may never need serialization at all
            model = beanToMapSerializer.convertToMap(presenter);
        } else {
            model = new HashMap<>();
        }
        return model;
    }

    private Class<?> getPresenterType(Resource resource) {
        String resourceType = resource.getResourceType();
        return presenterBundleListener.getPresenters().get(resourceType);
    }

    @Activate
    private void activate(ComponentContext componentContext) {
        presenterBundleListener = new PresenterBundleListener(componentContext.getBundleContext());

    }

    @Deactivate
    private void deactivate() {
        presenterBundleListener.unregisterAll();
    }

}
