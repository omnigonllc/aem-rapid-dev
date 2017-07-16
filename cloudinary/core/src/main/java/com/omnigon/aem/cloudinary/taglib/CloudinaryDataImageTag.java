package com.omnigon.aem.cloudinary.taglib;

import com.day.cq.commons.Externalizer;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.designer.Designer;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.omnigon.aem.cloudinary.CloudinaryFunctions;
import com.omnigon.aem.common.utils.ServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TagSupport;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * User: Andrey Bardashevsky
 * Date/Time: 03.10.13 17:06
 */
public class CloudinaryDataImageTag extends TagSupport implements DynamicAttributes {
    private static final Logger LOG = LoggerFactory.getLogger(CloudinaryDataImageTag.class);

    private static final String DEFAULT_IMAGE = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNgYAAAAAMAASsJTYQAAAAASUVORK5CYII=";
    private static final String SRC_TAG = "src";

    private String src;
    private String classes = "";
    private String alt = "";
    private String styles = "";
    private String dataRadius = "";
    private String dataCrop = "";
    private boolean absoluteUrl = false;
    private boolean noCDN = false;
    private Map<String, Object> attributes = new LinkedHashMap<String, Object>();

    /**
     * <p>Constant to identify <code>Resource Resolver</code> in the properties.</p>
     */
    private static final String RESOURCE_RESOLVER = "resourceResolver";

    @Override
    public int doStartTag() throws JspException {

        setAttributes();

        String attrs = Joiner.on(" ").withKeyValueSeparator("=").join(
                Maps.transformValues(attributes, new Function<Object, Object>() {
                    @Override
                    public Object apply(final Object input) {
                        return "\"" + Objects.toString(input) + "\"";
                    }
                })
        );

        JspWriter out = pageContext.getOut();

        try {
            jspWriterAppender(out, true, "<img ", attrs);
            jspWriterAppender(out, StringUtils.isNotBlank(alt), "alt=\"", alt, "\" ");
            jspWriterAppender(out, true, "class=\"", classes, "\"", " style=\"", styles, "\"");
            jspWriterAppender(out, StringUtils.isNotBlank(dataRadius), "data-radius=\"", dataRadius, "\"");
            jspWriterAppender(out, StringUtils.isNotBlank(dataCrop), "data-crop=\"", dataCrop, "\"");
            jspWriterAppender(out, true, ">");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return EVAL_BODY_INCLUDE;
    }

    private void jspWriterAppender(JspWriter out, boolean condition, String... items) throws Exception{
        if (condition) {
            for (String item : items) {
                out.append(item);
            }
        }
    }

    private void setAttributes() {
        /*
        if it's a manually entered link, print it verbatim, skip cdn and everything else.
         */
        if (URI.create(src).isAbsolute()) {
            attributes.put(SRC_TAG, src);
        }
        /*
        If no CDN, just handle the absolute part
         */
        else if (noCDN) {
            if (absoluteUrl) {
                attributes.put(SRC_TAG, getAbsoluteLink(src));
            } else {
                attributes.put(SRC_TAG, src);
            }
        }
        /*
        otherwise setup for cloudinary front end code.
         */
        else {
            if (StringUtils.contains(src, DamConstants.MOUNTPOINT_ASSETS)) {
                attributes.put("data-src", CloudinaryFunctions.truncateDamFolder(src));
            } else if (StringUtils.contains(src, Designer.DESIGN_HOME)) {
                attributes.put(SRC_TAG, src);
            } else {
                attributes.put("data-src", src);
            }
            if (absoluteUrl) {
                attributes.put(SRC_TAG, DEFAULT_IMAGE);
            } else if (!attributes.containsKey(SRC_TAG)) {
                attributes.put(SRC_TAG, DEFAULT_IMAGE);
            }
        }
    }

    /**
     * Gets the absolute link for a path.  In this case, the default img.
     *
     * @param path
     * @return
     */
    private String getAbsoluteLink(String path) {
        try {
            ResourceResolver rr = (ResourceResolver) pageContext.findAttribute(RESOURCE_RESOLVER);
            Externalizer externalizer = rr.adaptTo(Externalizer.class);
            SlingSettingsService configService = ServiceUtils.getService(SlingSettingsService.class, pageContext.getRequest());
            if (configService != null) {
                Set<String> modes = configService.getRunModes();
                for (String mode : modes) {
                    if ("author".equals(mode)) {
                        return externalizer.authorLink(rr, path);
                    }
                    if ("publish".equals(mode)) {
                        return externalizer.publishLink(rr, path);
                    }
                }
            }

            return externalizer.publishLink(rr, path);
        } catch (Exception e) {
            LOG.error("Couldn't externalize default image: " + e.toString());
        }
        return path;
    }

    @Override
    public int doEndTag() throws JspException {
        attributes.clear();
        src = null;
        absoluteUrl = false;

        return super.doEndTag();
    }

    @Override
    public void setDynamicAttribute(final String uri, final String localName, final Object value) throws JspException {
        attributes.put(localName, value);
    }

    public void setAbsolute(final boolean absoluteUrl) {
        this.absoluteUrl = absoluteUrl;
    }

    public void setClasses(final String classes) {
        this.classes = classes;
    }

    public void setAlt(final String alttext) {
        this.alt = alttext;
    }

    public void setStyle(final String stylz) {
        this.styles = stylz;
    }

    public void setSrc(final String src) {
        try {
            this.src = URI.create(src).toString(); // TODO: ensure correct encoding
        } catch (Exception e) {
            this.src = src;
        }
    }

    public void setDataRadius(final String dataRadius) {
        this.dataRadius = dataRadius;
    }

    public void setDataCrop(String dataCrop) {
        this.dataCrop = dataCrop;
    }

    public void setNoCdn(final boolean cdn) {
        this.noCDN = cdn;
    }
}