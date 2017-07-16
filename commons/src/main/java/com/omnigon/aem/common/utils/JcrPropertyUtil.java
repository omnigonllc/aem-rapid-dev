/*
 * Copyright (c) 2017 Omnigon Communications, LLC. All rights reserved.
 *
 * This software is the confidential and proprietary information of Omnigon Communications, LLC
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall
 * in accordance with the terms of the license agreement you entered into with Omnigon Communications, LLC, its
 * subsidiaries, affiliates or authorized licensee. Unless required by applicable law or agreed to in writing, this
 * Confidential Information is provided on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the license agreement for the specific language governing permissions and limitations.
 */
package com.omnigon.aem.common.utils;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.util.ArrayList;
import java.util.List;


/**
 * Util to update JCR node property
 */
public final class JcrPropertyUtil {

    private JcrPropertyUtil() {}

    /**
     *
     * @param node JCR node
     * @param propName property name
     * @param propValue property value
     * @throws RepositoryException -
     */
    public static void updateProperty(final Node node, final String propName, final String propValue) throws RepositoryException {
        boolean hasProperty = node.hasProperty(propName);

        if (!hasProperty && propValue != null) {
            updateStringProperty(node, propName, propValue);
        } else if (hasProperty && propValue == null) {
            updateStringProperty(node, propName, null);
        } else if (hasProperty && !StringUtils.equals(propValue, node.getProperty(propName).getString())) {
            updateStringProperty(node, propName, propValue);
        }
    }

    private static void updateStringProperty(final Node node, final String propName, final String propValue) throws RepositoryException {
        node.setProperty(propName, propValue);
    }

    /**
     *
     * @param node JCR node
     * @param propName property name
     * @param propValue property value
     * @throws RepositoryException -
     */
    public static void updateProperty(final Node node, final String propName, final Long propValue) throws RepositoryException {
        boolean hasProperty = node.hasProperty(propName);

        if (!hasProperty && propValue != null) {
            updateLongProperty(node, propName, propValue);
        } else if (hasProperty && propValue == null) {
            updateLongProperty(node, propName, null);
        } else if (hasProperty && !StringUtils.equals(propValue.toString(), node.getProperty(propName).getString())) {
            updateLongProperty(node, propName, propValue);
        }
    }

    private static void updateLongProperty(final Node node, final String propName, final Long propValue) throws RepositoryException {
        node.setProperty(propName, propValue);
    }

    /**
     *
     * @param node JCR node
     * @param newTags -
     * @throws RepositoryException -
     */
    public static void updateTagsProperty(final Node node, List<Tag> newTags) throws RepositoryException {
        List<String> tags = new ArrayList<String>(newTags.size());

        for (Tag tag : newTags) {
            tags.add(tag.getTagID());
        }

        if (node.hasProperty(TagConstants.PN_TAGS)) {
            Value[] values = node.getProperty(TagConstants.PN_TAGS).getValues();
            if (values != null) {
                List<String> existingTags = new ArrayList<String>(values.length);

                for (Value value : values) {
                    existingTags.add(value.getString());
                }

                if (!CollectionUtils.isEqualCollection(existingTags, tags)) {
                    updateTagProperty(node, tags);
                }
            } else {
                updateTagProperty(node, tags);
            }
        } else {
            updateTagProperty(node, tags);
        }
    }

    private static void updateTagProperty(final Node node, final List<String> tags) throws RepositoryException {
        node.setProperty(TagConstants.PN_TAGS, tags.toArray(new String[tags.size()]));
    }
}
