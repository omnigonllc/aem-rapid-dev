package com.omnigon.aem.common.utils;

import org.apache.jackrabbit.spi.commons.name.NameConstants;
import org.apache.sling.api.resource.Resource;

import javax.jcr.Item;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by brenn.hill on 12/2/2014.
 */
public final class NodeUtil {
    //private constructor,
    private NodeUtil() {
    }

    /**
     * Checks to see if a node has all properties in a list.
     *
     * @param node        -
     * @param propertyMap
     * @return -
     * @throws RepositoryException -
     */
    public static boolean hasAllProperties(Node node, Map<String, Object> propertyMap) throws RepositoryException {
        List<String> propertyList = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
            propertyList.add(entry.getKey());
        }

        return hasAllProperties(node, propertyList);
    }

    /**
     * Checks to see if the node has all properties in a list
     *
     * @param node       -
     * @param properties -
     * @return -
     * @throws RepositoryException -
     */
    public static boolean hasAllProperties(Node node, List<String> properties) throws RepositoryException {
        for (String prop : properties) {
            if (!node.hasProperty(prop)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks to see if the node represented by a resource has all properties in a list
     *
     * @param resource   -
     * @param properties -
     * @return -
     * @throws RepositoryException -
     */
    public static boolean hasAllProperties(Resource resource, List<String> properties) throws RepositoryException {
        Node node = resource.adaptTo(Node.class);
        if (node == null) {
            return false;
        }
        for (String prop : properties) {
            if (!node.hasProperty(prop)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if passed node is root node.
     * IMPORTANT! NullPointerException unsafe
     *
     * @param node node to be checked
     * @return -
     * @throws RepositoryException -
     */
    public static boolean isRoot(Node node) throws RepositoryException {
        return node.isNodeType("rep:root");
    }

    /**
     * Retrieves primary child node if exists
     *
     * @param node -
     * @return primary child node
     * @throws RepositoryException -
     */
    public static Node getPrimaryNode(Node node) throws ItemNotFoundException, RepositoryException {
        Item primaryItem = node.getPrimaryItem();
        if (primaryItem.isNode()) {
            return (Node) primaryItem;
        }
        throw new ItemNotFoundException("There is no primary node for node: " + node);
    }
}
