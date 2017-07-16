package com.omnigon.aem.handlebars.helpers.groupby;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by daniil.sheidak on 12.01.2017.
 */
public class Element {
    private Map<String, Object> map;

    public Element() {
        this.map = new LinkedHashMap<>();
    }

    public Element(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
