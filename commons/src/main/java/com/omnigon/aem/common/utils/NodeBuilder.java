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

import org.apache.jackrabbit.value.DateValue;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import java.util.*;


/**
 * Created by brenn.hill on 12/2/2014.
 * Simplifies creation of nodes, abstracting away complexities of handling different property types.
 * Uses builder pattern/fluent interface or methods can be called without using internal Node object.
 */
public class NodeBuilder {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(NodeBuilder.class);

    private ResourceResolver resolver;
    private Session session;
    private Node node;

    public NodeBuilder(ResourceResolver resolver) {
        this.resolver = resolver;
        this.session = resolver.adaptTo(Session.class);
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setNode(Resource res) {
        this.node = res.adaptTo(Node.class);
    }

    public void setNode(String path) {
        setNode(resolver.getResource(path));
    }


    /**
     * Attempts to save the node and return finalized changes.
     *
     * @return saved node
     */
    public Node getNode() {
        try {
            session.save();
        } catch (Exception e) {
            LOG.error("Couldn't save updates to node!");
        }
        return node;
    }

    /**
     *
     * @return the node without attempting to save changes.  However, changes may have been saved to the node anyway.
     */
    public Node getRawNode() {
        return node;
    }

    /**
     * Writes properties on a node. Intelligently discovers types of Object in the propertyMap and builds the property set appropriately.
     * Automatically saves the session on success or if failOnError is false continues to write what it can, logging errors.
     * <p/>
     * Supported types in the propertyMap: String, Date, and Calendar.
     *
     * @param propertyMap -
     * @param failOnError tells to the method return instance of builder without saving to repository after exception in case of <b>false</b> and try to save node, in case of <b>true</b>
     * @return the node written to.
     * @throws RepositoryException -
     */
    public NodeBuilder writeNodeProperties(Map<String, Object> propertyMap, boolean failOnError) throws RepositoryException {
        return writeNodeProperties(this.node, propertyMap, failOnError);
    }

    /**
     * Writes properties on a node. Intelligently discovers types of Object in the propertyMap and builds the property set appropriately.
     * Automatically saves the session on success or if failOnError is false continues to write what it can, logging errors.
     * <p/>
     * Supported types in the propertyMap: String, Date, and Calendar.
     *
     * @param node -
     * @param propertyMap -
     * @param failOnError tells to the method return instance of builder without saving to repository after exception in case of <b>false</b> and try to save node, in case of <b>true</b>
     * @return the node written to.
     * @throws RepositoryException -
     */
    public NodeBuilder writeNodeProperties(Node node, Map<String, Object> propertyMap, boolean failOnError) throws RepositoryException {
        if (node == null) {
            LOG.error("Cannot add properties to null node!");
            throw new RepositoryException("Node is null");
        }

        //if session is dead, we can't save.
        if (!session.isLive()) {
            LOG.error("Dead session.");
            throw new RepositoryException("Session is dead");
        }

        Iterator<Map.Entry<String, Object>> it = propertyMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();

            /**
             * Tries to detect existing property, deleting it (with LOG) if it exists.
             * Then discovers the type of object in the property map.
             */
            try {
                //Test for existence of existing properties and remove them. Log what is deleted.
                if (node.hasProperty(entry.getKey())) {
                    LOG.error("Warning: node at:" + node.getPath() + " already has property: " + entry.getKey() + " and will be deleted.");
                    if (node.getProperty(entry.getKey()).isMultiple()) {
                        LOG.error("Warning: node property to be deleted is multivalued");
                    }
                    //Delete property if it already exists.  Shouldn't happen.
                    node.getProperty(entry.getKey()).remove();
                }

                if(entry.getValue() == null) {

                }

                if (entry.getValue() instanceof Date) {
                    setDateValue(node, entry.getKey(), (Date) entry.getValue());
                    continue;
                }
                if (entry.getValue() instanceof Calendar) {
                    setCalendarValue(node, entry.getKey(), (Calendar) entry.getValue());
                    continue;
                }
                if (entry.getValue() instanceof String) {
                    node.setProperty(entry.getKey(), (String) entry.getValue());
                }
                if (entry.getValue() instanceof Object[]) {
                    setMultiValuedProperty(node, entry.getKey(), (Object[]) entry.getValue());
                    continue;
                }
                //We shouldn't be here.
                LOG.error("Unknown type for key:" + entry.getKey());
            } catch (Exception e) {
                LOG.error("Cannot set property: " + entry.getKey() + ". REASON: " + e.toString());
                if (failOnError) {
                    return this; //saves nothing.
                }
            }
            try {
                session.save();
            } catch (Exception e) {
                LOG.error("Couldn't save changes: " + e.toString());
            }
        }
        return this;
    }

    public NodeBuilder setDateValue(String name, Date date) throws RepositoryException {
        return setDateValue(this.node, name, date);
    }

    private NodeBuilder setDateValue(Node node, String name, Date date) throws RepositoryException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Value value = new DateValue(cal);
        node.setProperty(name, value);
        return this;
    }

    /**
     *
     * @param name property name
     * @param cal -
     * @return node builder with set incoming date as {@link DateValue} value to property of node
     * @throws RepositoryException -
     */
    public NodeBuilder setCalendarValue(String name, Calendar cal) throws RepositoryException {
        return setCalendarValue(this.node, name, cal);
    }

    private NodeBuilder setCalendarValue(Node node, String name, Calendar cal) throws RepositoryException {
        Value value = new DateValue(cal);
        node.setProperty(name, value);
        return this;
    }

    /**
     *
     * @param name property name
     * @param values supported type of values: String[], Date[] and Calendar[]
     * @return node builder with set values to property of node
     * @throws RepositoryException -
     */
    public NodeBuilder setMultiValuedProperty(String name, Object[] values) throws RepositoryException {
        return setMultiValuedProperty(this.node, name, values);
    }

    /**
     *
     * @param node -
     * @param name property name
     * @param values supported type of values: String[], Date[] and Calendar[]
     * @return node builder with set values to property of node
     * @throws RepositoryException -
     */
    public NodeBuilder setMultiValuedProperty(Node node, String name, Object[] values) throws RepositoryException {

        try {
            //handle String values
            if (values instanceof String[]) {
                String[] vals = (String[]) values;
                node.setProperty(name, vals);
                session.save();
                return this;
            }

            //Handle date values
            if (values instanceof Date[]) {
                Date[] dvals = (Date[]) values;
                List<DateValue> dateValueArrayList = new ArrayList<DateValue>();
                for (Date val : dvals) {
                    if (val == null) {
                        continue;
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(val);
                    dateValueArrayList.add(new DateValue(cal));
                }
                DateValue[] dvalues = dateValueArrayList.toArray(new DateValue[dateValueArrayList.size()]);
                node.setProperty(name, dvalues);
                return this;
            }

            //handle calendar values
            if (values instanceof Calendar[]) {
                Calendar[] cvals = (Calendar[]) values;
                List<DateValue> dateValueArrayList = new ArrayList<DateValue>();
                for (Calendar cal : cvals) {
                    if (cal == null) {
                        continue;
                    }
                    dateValueArrayList.add(new DateValue(cal));
                }
                DateValue[] dvalues = dateValueArrayList.toArray(new DateValue[dateValueArrayList.size()]);
                node.setProperty(name, dvalues);
                return this;
            }
            LOG.error("Unknown type in multivalued property: " + name);
        } catch (Exception e) {
            LOG.error("Couldn't set values on node for property:" + name);
        }
        return this;
    }
}
