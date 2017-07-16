package com.omnigon.aem.handlebars.helpers.sortby;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniil.sheidak on 10.01.2017.
 */
public class Group {
    private List<Group> groups;

    private List<Element> elements;

    public Group() {
        this.groups = new ArrayList<>();
        this.elements = new ArrayList<>();
    }

    public Group(List<Element> elements) {
        this.groups = new ArrayList<>();
        this.elements = elements;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Element> getElements() {
        return elements;
    }
}
