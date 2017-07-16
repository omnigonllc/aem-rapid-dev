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

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.Session;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class for node searching in JCR.
 */
public final class NodesSearcher {
    /**
     * Private constructor to prevent class instantiation.
     */
    private NodesSearcher() {
        super();
    }

    /**
     * Find nodes according to given parameters.
     *
     * @param resourceResolver - {@link ResourceResolver}
     * @param params           - params map for search
     * @return                 - result of a search
     */
    public static SearchResult findNodes(ResourceResolver resourceResolver, final Map<String, String> params) {
        return findNodes(resourceResolver, params, 0, 0);
    }

    /**
     * Find resources according to given parameters.
     *
     * @param resourceResolver - {@link ResourceResolver}
     * @param params           - params map for search
     * @return                 - result of a search
     */
    public static Iterator<Resource> getResources(ResourceResolver resourceResolver, final Map<String, String> params) {
        return findNodes(resourceResolver, params).getResources();
    }

    /**
     * Parametric search of al possible nodes, with the specified number of search result.
     *
     * @param resourceResolver - {@link ResourceResolver}
     * @param params           - params map for search
     * @param offset           - number of node from which to start search
     * @param max              - number of result per page
     * @return                 - result of a search
     */
    public static Iterator<Resource> getResources(ResourceResolver resourceResolver,
                                   final Map<String, String> params, int offset, int max) {
        return findNodes(resourceResolver, params, offset, max).getResources();
    }

    /**
     * Parametric search of al possible nodes, with the specified number of search result
     *
     * @param resourceResolver - {@link ResourceResolver}
     * @param params           - params map for search
     * @param offset           - number of node from which to start search
     * @param max              - number of result per page
     * @return                 - result of a search {@link SearchResult}
     */
    public static SearchResult findNodes(ResourceResolver resourceResolver,
                                          Map<String, String> params, int offset, int max) {
        Session session = resourceResolver.adaptTo(Session.class);
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
        Query query = builder.createQuery(PredicateGroup.create(params), session);
        query.setStart(offset);
        query.setHitsPerPage(max);
        return query.getResult();
    }
}
