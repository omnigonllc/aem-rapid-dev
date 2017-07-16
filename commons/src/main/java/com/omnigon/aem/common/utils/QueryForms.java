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
package com.omnigon.aem.common.utils;

import com.day.cq.search.Predicate;
import com.day.cq.search.eval.JcrPropertyPredicateEvaluator;
import com.day.cq.search.eval.PathPredicateEvaluator;
import com.day.cq.search.eval.RangePropertyPredicateEvaluator;
import com.day.cq.wcm.api.NameConstants;
import org.apache.jackrabbit.spi.commons.query.QueryConstants;
import org.apache.sling.jcr.resource.JcrResourceConstants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: andrey.zelentsov
 * Date: 24.12.14
 * Time: 19:33
 */
public final class QueryForms {
    private static final String DATERANGE_PROPERTY_NAME = "daterange";
    /**
     * Date format for lower bound of search date range.
     */
    private static final String START_DATE_FILTERING_FORMAT = "yyyy-MM-dd'T'HH:mm:00.000";
    /**
     * Date format for upper bound of search date range.
     */
    private static final String END_DATE_FILTERING_FORMAT = "yyyy-MM-dd'T'HH:mm:'59.999'";

    private QueryForms() {}

    /**
     * Formats given date to String for searching with QueryBuilder {@link com.day.cq.search.QueryBuilder}.
     *
     * @param date    - date to format
     * @param isStart - if true date will have 00 seconds and 00 milliseconds, otherwise - 59 seconds
     *                and 999 milliseconds
     * @return - date as String
     */
    private static String toQueryDate(Date date, boolean isStart) {
        String pattern = isStart ? START_DATE_FILTERING_FORMAT : END_DATE_FILTERING_FORMAT;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        return formatter.format(date);
    }

    /**
     * Formats given date to query for searching events with given date as lower bound of search date range.
     *
     * @param date              - date to format
     * @param filteringProperty - property used for searching
     * @return - query map
     */
    public static Map<String, String> getStartDateQuery(Date date, String filteringProperty) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("2_" + DATERANGE_PROPERTY_NAME + "." + JcrPropertyPredicateEvaluator.PROPERTY, filteringProperty);
        map.put("2_" + DATERANGE_PROPERTY_NAME + "." + RangePropertyPredicateEvaluator.LOWER_OPERATION, QueryConstants.OP_NAME_GE_GENERAL);
        map.put("2_" + DATERANGE_PROPERTY_NAME + "." + RangePropertyPredicateEvaluator.LOWER_BOUND, toQueryDate(date, true));
        return map;
    }

    /**
     * Formats given date to query for searching events with given date as upper bound of search date range.
     *
     * @param date              - date to format
     * @param filteringProperty - property used for searching
     * @return - query map
     */
    public static Map<String, String> getEndDateQuery(Date date, String filteringProperty) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("3_" + DATERANGE_PROPERTY_NAME + "." + JcrPropertyPredicateEvaluator.PROPERTY, filteringProperty);
        map.put("3_" + DATERANGE_PROPERTY_NAME + "." + RangePropertyPredicateEvaluator.UPPER_OPERATION, QueryConstants.OP_NAME_LE_GENERAL);
        map.put("3_" + DATERANGE_PROPERTY_NAME + "." + RangePropertyPredicateEvaluator.UPPER_BOUND, toQueryDate(date, false));
        return map;
    }

    /**
     * Forms query for sorting search result by given property.
     *
     * @param filteringProperty - property to sort
     * @param desc              - if 'true' events are ordered descending - newest first,
     *                          else events are ordered ascending - older first
     * @return - query map
     */
    public static Map<String, String> getSortQuery(String filteringProperty, boolean desc) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put(Predicate.ORDER_BY, new StringBuilder("@").append(filteringProperty).toString());
        if (desc) {
            map.put(Predicate.ORDER_BY + "." + Predicate.PARAM_SORT, Predicate.SORT_DESCENDING);
        }
        return map;
    }

    /**
     * Forms query containing root path for search and resource type of liveFeed component.
     *
     * @param eventRoot - root path for events
     * @param resourceTypeValue -
     * @return - query map
     */
    public static Map<String, String> getGeneralPropertiesQuery(String eventRoot, String resourceTypeValue) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put(PathPredicateEvaluator.PATH, eventRoot);
        map.put("1_" + JcrPropertyPredicateEvaluator.PROPERTY, JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY);
        map.put("1_" + JcrPropertyPredicateEvaluator.PROPERTY + "." + JcrPropertyPredicateEvaluator.VALUE, resourceTypeValue);
        return map;
    }

    /**
     * Formats query for searching events with given year.
     *
     * @param year              - year of events to findFeedItems
     * @param filteringProperty - property used for searching
     * @return - query map
     */
    public static Map<String, String> getYearQuery(int year, String filteringProperty) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("3_" + DATERANGE_PROPERTY_NAME + "." + JcrPropertyPredicateEvaluator.PROPERTY, filteringProperty);
        map.put("3_" + DATERANGE_PROPERTY_NAME + "." + RangePropertyPredicateEvaluator.LOWER_OPERATION, QueryConstants.OP_NAME_GE_GENERAL);
        map.put("3_" + DATERANGE_PROPERTY_NAME + "." + RangePropertyPredicateEvaluator.LOWER_BOUND, year + "-01-01");
        map.put("3_" + DATERANGE_PROPERTY_NAME + "." + JcrPropertyPredicateEvaluator.PROPERTY, filteringProperty);
        map.put("3_" + DATERANGE_PROPERTY_NAME + "." + RangePropertyPredicateEvaluator.UPPER_BOUND, year + "-12-31");
        map.put("3_" + DATERANGE_PROPERTY_NAME + "." + RangePropertyPredicateEvaluator.UPPER_OPERATION, QueryConstants.OP_NAME_LE_GENERAL);
        return map;
    }

    /**
     * Formats query for searching events with given tags.
     *
     * @param tags              -
     * @return - query map
     */
    public static Map<String, String> getTagsQuery(List<String> tags) {
        if (null == tags) {
            return new HashMap<String, String>(0);
        }
        Map<String, String> tagQuery = new HashMap<String, String>();

        tagQuery.put("tagid." + JcrPropertyPredicateEvaluator.PROPERTY, NameConstants.PN_TAGS);
        for (int i = 0; i < tags.size(); i++) {
            String prefix = Integer.toString(i);
            tagQuery.put("tagid." + JcrPropertyPredicateEvaluator.PROPERTY + "." + prefix + "_" + JcrPropertyPredicateEvaluator.VALUE, tags.get(i));
        }
        return tagQuery;
    }
}
