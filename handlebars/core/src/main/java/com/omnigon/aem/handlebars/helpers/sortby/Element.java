package com.omnigon.aem.handlebars.helpers.sortby;

import java.util.Map;

/**
 * Created by daniil.sheidak on 10.01.2017.
 */
public class Element implements Comparable<Element> {

    private String sortingProperty;
    private Map<String, Comparable> properties;

    public Element(Map<String, Comparable> property) {
        this.properties = property;
    }

    public Element(Map<String, Comparable> property, String sortingProperty) {
        this.sortingProperty = sortingProperty;
        this.properties = property;
    }

    public String getSortingProperty() {
        return sortingProperty;
    }

    public void setSortingProperty(String sortingProperty) {
        this.sortingProperty = sortingProperty;
    }

    public Map<String, Comparable> getProperties() {
        return properties;
    }

    @Override
    public int compareTo(Element element) {
        Comparable var1 = this.properties.get(sortingProperty);
        Comparable var2 = element.properties.get(sortingProperty);
        return var1.compareTo(var2);
    }
}
