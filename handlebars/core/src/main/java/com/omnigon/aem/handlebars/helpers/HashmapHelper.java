package com.omnigon.aem.handlebars.helpers;

import com.github.jknack.handlebars.Options;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniil.sheidak on 20.10.2016.
 */
@Service
@Component
public class HashmapHelper implements HandlebarsHelper {

    private static final String NAME = "hashMap";

    /**
     * @return Returns helper's name
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     *
     * @param context Contains resource properties
     * @param options Contains context variables.
     * @return Returns map that consists listed pairs.
     * If no pairs specified the map will be empty
     * @throws IOException
     */
    @Override
    public Map<String, Object> apply(final Object context, final Options options) throws IOException {
        if (options == null) {
            return new HashMap<String, Object>();
        }
        return options.hash;
    }
}
