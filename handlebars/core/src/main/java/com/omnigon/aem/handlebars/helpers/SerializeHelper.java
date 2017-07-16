package com.omnigon.aem.handlebars.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Options;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Brenn Hill on 10.21.2016.
 *
 * This helper serializes a java object to JSON representation.  This is used to pass data to FE helpers and services.
 */
@Service
@Component
public class SerializeHelper implements HandlebarsHelper {

    private static final Logger LOG = LoggerFactory.getLogger(SerializeHelper.class);

    private static final String NAME = "serialize";

    private ObjectMapper objectMapper = new ObjectMapper();

    public SerializeHelper() {
       objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Serializes data to JSON
     * @param context the object to be serialized
     * @param options part of the API but not used as part of serialization at this time.
     * @return A valid JSON object representing the object passed as context
     * @throws IOException
     */
    @Override
    public CharSequence apply(final Object context, final Options options) throws IOException {

        Object ctx = !isEmptyMap(context) ? context : options.context.model(); // change related to https://jira.omnigon.com/browse/USOD-937

        if(ctx==null) return "{}";

        String json = objectMapper.writeValueAsString(ctx); //   Gson gson = new Gson(); String json = gson.toJson(ctx);

        // we force Handlebars not to escape JSON content
        Handlebars.SafeString safeString = new Handlebars.SafeString(json);
        return safeString;
    }

    // Used to detect serialize call inside a loop
    private static boolean isEmptyMap(final Object context) {
        if(context instanceof Map) {
           return ((Map)context).isEmpty();
        }
        return false;
    }


}
