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
package com.omnigon.aem.handlebars.helpers.sortby;

import java.util.Map;

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
