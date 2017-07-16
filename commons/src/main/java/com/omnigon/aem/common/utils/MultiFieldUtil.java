package com.omnigon.aem.common.utils;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.*;

/**
 * ACS AEM Commons MultiField parser based on JSONObject mapping.
 *
 * Created with IntelliJ IDEA.
 * User: andrey.zelentsov
 * Date: 04.03.15
 * Time: 15:29
 */
public final class MultiFieldUtil {
    private MultiFieldUtil() {}

    /**
     * Get properties of given ACS AEM Commons MultiField value as list of maps
     *
     * @param multiFieldItems raw MultiField value.
     * @param propertyNames list of property names of current MultiField.
     * @return list of maps with MultiField values.
     */
    public static List<Map<String,String>> getMultiFieldProperties(String[] multiFieldItems, List<String> propertyNames) {

        return getMultiFieldPropertiesFromList(multiFieldItems, propertyNames);
    }

    /**
     * Get properties of given ACS AEM Commons MultiField value as list of maps
     *
     * @param multiFieldItems raw MultiField value.
     * @param clazz generic type object which extends {@link Enum} (contains list of names of current MultiField).
     * @param <E> Enum
     * @return list of maps with MultiField values.
     */
    public static <E extends Enum<E>> List<Map<String,String>> getMultiFieldProperties(String[] multiFieldItems, Class<E> clazz) {
        List<String> listOfPropertyNames = getPropertyNamesFromEnum(clazz);

        return getMultiFieldPropertiesFromList(multiFieldItems, listOfPropertyNames);
    }

    /**
     * Get property names from given {@link Enum} class
     *
     * @param clazz generic type object which extends {@link Enum} (contains list of names of current MultiField).
     * @param <E> Enum
     * @return list of names from {@link Enum} object.
     */
    public static <E extends Enum<E>> List<String> getPropertyNamesFromEnum(Class<E> clazz) {

        List<String> listOfPropertyNames = new ArrayList<String>();
        for (E en : EnumSet.allOf(clazz)) {
            listOfPropertyNames.add(en.name());
        }

        return listOfPropertyNames;
    }

    /**
     * Get properties of given ACS AEM Commons MultiField value as list of maps
     *
     * @param multiFieldItems list of property names of current MultiField.
     * @param propertyNames list of property names of current MultiField.
     * @return list of maps with MultiField values.
     */
    private static List<Map<String,String>> getMultiFieldPropertiesFromList(String[] multiFieldItems, List<String> propertyNames) {

        List<Map<String,String>> mapList = new ArrayList<Map<String, String>>();
        if (multiFieldItems != null) {
            for (String item: multiFieldItems) {
                Object obj = JSONValue.parse(item);
                JSONObject jsonObject = (JSONObject) obj;

                Map<String, String> map = new HashMap<String, String>();
                for (String property : propertyNames) {
                    map.put(property, String.valueOf(jsonObject.get(property)));
                }
                mapList.add(map);
            }
        }

        return mapList;
    }

}
