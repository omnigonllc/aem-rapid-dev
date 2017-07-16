package com.omnigon.aem.common.utils;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class for mapping resource data.
 */
public final class ResourceMappingUtil {
    /**
     * Logger object.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ResourceMappingUtil.class);

    private ResourceMappingUtil() {}

    /**
     * Maps data provided by resource to the specified bean.
     *
     * @param resource - resource to map
     * @param clazz    - bean class
     * @return         - bean object
     */
    public static <T> T mapResourceToBean(final Resource resource, final Class<T> clazz) {
        if (null != resource && !ResourceUtil.isNonExistingResource(resource)) {
            T item = resource.adaptTo(clazz);
            if (null != item) {
                return item;
            } else {
                LOG.warn("Resource failed to adapt to class: " + clazz);
            }
        } else {
            LOG.warn("Resource for adapting to class is null or doesn't exist");
        }
        return null;
    }
    /**
     * Maps iterator of resources to list of specified beans.
     *
     * @param resources - iterator of resources to map
     * @param clazz     - bean class
     * @return          - list of bean objects
     */
    public static <T> List<T> mapResourcesToBeans(Iterator<Resource> resources, final Class<T> clazz) {
        List<T> items = new ArrayList<T>();
        while(resources.hasNext()) {
            T item = mapResourceToBean(resources.next(), clazz);
            if (null != item) {
                items.add(item);
            }
        }
        return items;
    }
}
