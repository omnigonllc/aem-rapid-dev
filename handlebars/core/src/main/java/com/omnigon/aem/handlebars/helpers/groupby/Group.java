package com.omnigon.aem.handlebars.helpers.groupby;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniil.sheidak on 12.01.2017.
 */
public class Group {
    private List<Element> elements;
    private Map<String, Object> keyAndValues;

    public Group() {
        this.elements = new ArrayList<>();
        this.keyAndValues = new LinkedHashMap<>();
    }

    public Group(List<Element> elements, Map<String, Object> keyAndValues) {
        this.elements = elements;
        this.keyAndValues = keyAndValues;
    }

    public List<Element> getElements() {
        return elements;
    }

    public Map<String, Object> getKeysAndValues() {
        return keyAndValues;
    }
}
