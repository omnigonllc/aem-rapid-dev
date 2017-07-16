package com.omnigon.aem.handlebars.context.factories;

import org.apache.felix.scr.annotations.*;
import org.osgi.framework.Constants;
import com.omnigon.aem.handlebars.context.DefaultHandlebarsContextGenerator;
import com.omnigon.aem.sling.mvp.context.ContextGenerator;
import com.omnigon.aem.sling.mvp.context.ContextGeneratorFactory;
import com.omnigon.aem.sling.mvp.model.presenter.PresenterModelGenerator;

@Component
@Service(value = {ContextGeneratorFactory.class})
@Properties(value = {
        @Property(name = "service.description", value = "Handlebars: Presenter Context Generator Factory"),
        @Property(name = Constants.SERVICE_RANKING, intValue = -100)
    }
)
public class HandlebarsPresenterContextGeneratorFactory implements ContextGeneratorFactory {

    @Reference
    protected PresenterModelGenerator modelGenerator;

    @Override
    public ContextGenerator getContextGenerator() {
        ContextGenerator contextGenerator =
                new DefaultHandlebarsContextGenerator(modelGenerator);
        return contextGenerator;
    }

}
