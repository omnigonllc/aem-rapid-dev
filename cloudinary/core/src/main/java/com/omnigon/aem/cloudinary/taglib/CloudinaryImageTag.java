package com.omnigon.aem.cloudinary.taglib;

import com.day.cq.dam.api.DamConstants;
import com.omnigon.aem.cloudinary.CloudinaryFunctions;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.scripting.jsp.util.TagUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Backing class to the cloudinary JSP taglibs.
 *
 */
public class CloudinaryImageTag extends TagSupport implements DynamicAttributes {
    private static final Logger LOG = LoggerFactory.getLogger(CloudinaryImageTag.class);

    private String src;
    private String classes = "";
    private String styles = "";
    private String alt = "";
    private boolean absoluteUrl = false;
    private String transformation;

    private Map<String, Object> attributes = new LinkedHashMap<String, Object>();

    @Override
    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();

            out.append("<img");

            SlingHttpServletRequest request = TagUtil.getRequest(pageContext);

            if (absoluteUrl) {
                src = CloudinaryFunctions.getAbsoluteImageUrl(request, transformation, src);
            } else if (StringUtils.contains(src, DamConstants.MOUNTPOINT_ASSETS)) {
                src = CloudinaryFunctions.getImageUrl(request, src, transformation);
            }

            attributes.put("src", src);
            attributes.put("class", classes);
            attributes.put("style", styles);
            attributes.put("alt", alt);

            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                String value = Objects.toString(entry.getValue());
                String name = entry.getKey();

                if (StringUtils.isNotBlank(value)) {
                    out.append(" ").append(name).append("=\"").append(value).append("\"");
                }
            }


            out.append(">");

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        attributes.clear();
        src = null;
        transformation = null;
        absoluteUrl = false;

        return super.doEndTag();
    }

    @Override
    public void setDynamicAttribute(final String uri, final String localName, final Object value) throws JspException {
        attributes.put(localName, value);
    }

    public void setAbsoluteUrl(final boolean absoluteUrl) {
        this.absoluteUrl = absoluteUrl;
    }

    public void setSrc(final String src) {
        this.src = src;
    }

    public void setAlt(final String alttext) {this.alt=alttext;}
    public void setClasses(final String classz) {this.classes=classz;}

    public void setStyle(final String style){this.styles=style;}

    public void setTransformation(final String transformation) {
        this.transformation = transformation;
    }

}
