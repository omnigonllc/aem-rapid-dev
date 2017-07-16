package com.omnigon.aem.handlebars.helpers.groupby;

import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;
import com.omnigon.aem.handlebars.helpers.HandlebarsHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.json.JSONArray;

import java.io.IOException;
import java.util.*;


/**
 * Created by daniil.sheidak on 10.01.2017.
 */
@Service
@Component
public class GroupByHelper implements HandlebarsHelper {

    private static final String NAME = "groupBy";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public JSONArray apply(Object context, Options options) throws IOException {
        JSONArray array = new JSONArray();
        if (TagType.SUB_EXPRESSION == options.tagType && context instanceof List && ArrayUtils.isNotEmpty(options.params)) {
            List<Element> elements = new ArrayList<>();
            for (Map<String, Object> elementMap : (List<Map<String, Object>>) context) {
                elements.add(new Element(elementMap));
            }
            List<String> keys = new ArrayList(Arrays.asList(options.params));
            List<Group> groups = groupElements(elements, keys);
            List<Map<String, List<Element>>> result = new ArrayList<>();
            for (Group group : groups) {
                String formattedKeyValues = formatKeyValues(group.getKeysAndValues().values());
                Map<String, List<Element>> groupedMap = new HashMap<>();
                groupedMap.put(formattedKeyValues, group.getElements());
                result.add(groupedMap);
                group.getElements();
            }
            array.put(result);
        }
        return array;
    }

    private String formatKeyValues (Collection keyValues) {
        String formattedKeyValues = StringUtils.EMPTY;
        for (Object keyValue : keyValues) {
            formattedKeyValues += keyValue;
        }
        return formattedKeyValues;
    }

    private List<Group> groupElements(List<Element> elements, List<String> keys) {
        List<Group> groups = new ArrayList<>();
        for (Element element : elements) {
            if (groups.isEmpty()) {/*for the first iteration*/
                Group firstGroup = new Group();
                firstGroup.getElements().add(element);
                for (String key : keys) {
                    Object value = element.getMap().get(key);
                    firstGroup.getKeysAndValues().put(key, value);
                }
                groups.add(firstGroup);
                continue;
            }
            Boolean isComplies = null;
            for (Group group : groups) {
                isComplies = true;
                Map<String, Object> keysAndValues = group.getKeysAndValues();
                for (String key : keys) {
                    if (!element.getMap().get(key).equals(keysAndValues.get(key))) {
                        isComplies = false;
                        break;
                    }
                }
                if (isComplies) {
                    group.getElements().add(element);
                    break;
                }
            }
            if (isComplies == false) {/*element wasn't added to any group*/
                Group newGroup = new Group();
                newGroup.getElements().add(element);
                for (String key : keys) {
                    Object value = element.getMap().get(key);
                    newGroup.getKeysAndValues().put(key, value);
                }
                groups.add(newGroup);
            }
        }
        return groups;
    }
}
