package com.omnigon.aem.models;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.omnigon.aem.sling.mvp.model.presenter.JacksonSerializable;
import com.omnigon.aem.sling.mvp.model.presenter.Presenter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import com.citytechinc.cq.component.annotations.Component;

import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL )
@Presenter(resourceTypes = {"omnigon/components/example"})
@Component(value = "Example Component", name="example",
        actions={"text: Example", "-", "edit", "insert", "copymove", "delete"}, disableTargeting = true)
public class Example implements JacksonSerializable {

    @Inject
    private String title;

    @DialogField
    @TextField
    public String getTitle() {
        return title;
    }

}
