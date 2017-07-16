package com.omnigon.aem.handlebars.context.factories;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import com.omnigon.aem.handlebars.context.DefaultHandlebarsContextGenerator;
import com.omnigon.aem.sling.mvp.context.ContextGenerator;
import com.omnigon.aem.sling.mvp.context.ContextGeneratorFactory;
import com.omnigon.aem.sling.mvp.model.SimpleModelGenerator;

@Component
@Service(value = {ContextGeneratorFactory.class})
@Properties(value = {
        @Property(name = "service.description", value = "Handlebars: Simple Context Generator Factory")}
)
public class HandlebarsSimpleContextGeneratorFactory implements ContextGeneratorFactory {
    @Override
    public ContextGenerator getContextGenerator() {
        ContextGenerator result =
                new DefaultHandlebarsContextGenerator(new SimpleModelGenerator());
        return result;
    }
}
