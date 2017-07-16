package com.omnigon.aem.handlebars.helpers.sortby;

import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.json.JSONArray;
import com.omnigon.aem.handlebars.helpers.HandlebarsHelper;

import java.io.IOException;
import java.util.*;


/**
 * Created by daniil.sheidak on 10.01.2017.
 */
@Service
@Component
public class SortByHelper implements HandlebarsHelper {

    private static final String NAME = "sortBy";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public JSONArray apply(Object context, Options options) throws IOException {
        JSONArray array = new JSONArray();
        if (TagType.SUB_EXPRESSION == options.tagType && context instanceof List && ArrayUtils.isNotEmpty(options.params)) {
            Group mainGroup = new Group();

            String firstKey = options.param(0).toString();
            firstKeySorting(firstKey, (List<Map<String, Comparable>>) context, mainGroup);

            String secondKey = options.param(1).toString();
            secondKeySorting(firstKey, secondKey, mainGroup);

            List<Map<String, Comparable>> sortedElements = new ArrayList<>();
            for (Group subGroup : mainGroup.getGroups()) {
                for (Element element : subGroup.getElements()) {
                    sortedElements.add(element.getProperties());
                }
            }
            array.put(sortedElements);
        }
        return array;
    }

    private void firstKeySorting(String firstKey, List<Map<String, Comparable>> context, Group mainGroup) {
        List<Element> elements = new ArrayList<>();
        for (Map<String, Comparable> element : context) {
            elements.add(new Element(element, firstKey));
        }
        Collections.sort(elements);
        mainGroup.getElements().addAll(elements);
    }

    private void secondKeySorting(String firstKey, String secondKey, Group mainGroup) {
        Comparable sortedPropVal = null;
        List<Element> currGroupElems = new ArrayList<>();

        Iterator<Element> elementIterator = mainGroup.getElements().iterator();
        while (elementIterator.hasNext()) {
            Element element = elementIterator.next();
            Map<String, Comparable> currElemProps = element.getProperties();

            if (sortedPropVal == null) {/*for the first iteration*/
                sortedPropVal = currElemProps.get(firstKey);
            }

            if (currElemProps.get(firstKey).compareTo(sortedPropVal) != 0) {
                Collections.sort(currGroupElems);
                mainGroup.getGroups().add(new Group(currGroupElems));
                currGroupElems = new ArrayList<>();
                sortedPropVal = currElemProps.get(firstKey);
            }

            element.setSortingProperty(secondKey);
            currGroupElems.add(element);

            if (!elementIterator.hasNext()) {/*for the last iteration*/
                Collections.sort(currGroupElems);
                mainGroup.getGroups().add(new Group(currGroupElems));
            }
        }
    }

}
