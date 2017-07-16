package com.omnigon.aem.handlebars.helpers.include;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;

import static com.omnigon.aem.handlebars.context.DefaultHandlebarsContextGenerator.*;

public class OptionsAdapter {
    private Options options;
    private Resource resource;
    private String absolutePath;

    public OptionsAdapter(Options options) {
        this.options = options;
    }


    public String getAddSelectors() {
        return (String) options.hash.get(IncludeHelper.ADD_SELECTORS);
    }

    public String getForceResourceType() {
        return (String) options.hash.get(IncludeHelper.RESOURCE_TYPE);
    }

    public String getReplaceSelectors() {
        return (String) options.hash.get(IncludeHelper.REPLACE_SELECTORS);
    }

    public String getReplaceSuffix() {
        return (String) options.hash.get(IncludeHelper.REPLACE_SUFFIX);
    }

    public SlingHttpServletRequest getRequest() {
        return options.get(REQUEST_KEY);
    }

    public ResourceResolver getResolver() {
        return options.get(RESOLVER_KEY);
    }

    public String getResourceType() {
        return getResource().getResourceType();
    }

    public SlingHttpServletResponse getResponse() {
        return options.get(RESPONSE_KEY);
    }

    public String getSelector() {
        return (String) options.hash.get(IncludeHelper.SELECTOR);
    }

    public Resource getResource() {
        if (resource == null) {

            resource = getResolver().resolve(getAbsolutePath());

            if (resource instanceof NonExistingResource) {
                String resourceType = StringUtils.isNotBlank(getForceResourceType()) ? getForceResourceType()
                        : getRequest().getResource().getResourceType();
                resource = new SyntheticResource(getResolver(), getAbsolutePath(), resourceType);
            }
        }

        return resource;
    }

    public String getAbsolutePath() {
        if (absolutePath == null) {
            String path = (String) options.hash.get(IncludeHelper.PATH);
            if ((path != null) && path.startsWith("/")) {
                absolutePath = path;
            } else {
                absolutePath = getRequest().getResource().getPath();

                if (StringUtils.isNotBlank(path)) {
                    absolutePath += "/" + path;
                }
            }

            absolutePath = ResourceUtil.normalize(absolutePath);
        }
        return absolutePath;
    }

}
