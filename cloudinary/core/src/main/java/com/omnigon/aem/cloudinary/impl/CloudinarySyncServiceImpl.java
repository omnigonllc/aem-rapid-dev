package com.omnigon.aem.cloudinary.impl;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.eval.PathPredicateEvaluator;
import com.day.cq.search.eval.TypePredicateEvaluator;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.omnigon.aem.cloudinary.CloudinaryConfigurationService;
import com.omnigon.aem.cloudinary.CloudinaryService;
import com.omnigon.aem.common.sling.ResourceResolverFactoryService;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.day.cq.search.eval.JcrPropertyPredicateEvaluator.*;
import static com.omnigon.aem.cloudinary.CloudinaryConstants.*;
import static com.day.cq.commons.jcr. JcrConstants.*;

/**
 * user: Brenn Hill
 * Implementation of cloudinary service.  Creates a common use cloudinary implementation based on system configurations.
 */
@Component(name = "Cloudinary DAM sync service", description = "Looks for images not uploaded to cloudinary and uploads them." /*, metatype = true */)
/*
Currently disabled.  Uncomment this and sync service will start auto-polling for missing images and uploading them.
TODO: make class smarter to keep from overloading plan limits
@Service(value = Runnable.class)
@Property( name = "scheduler.expression", value = "0 0 * * * ?")
*/
public class CloudinarySyncServiceImpl implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudinarySyncServiceImpl.class);

    private final static int SEARCH_LIMIT = 20;

    @Reference
    private CloudinaryService cloudinaryService;

    @Reference(target = "(factory.label=cloudinary)")
    private ResourceResolverFactoryService resolverFactoryService;

    @Activate
    @Modified
    private void activate(ComponentContext context) {
    }

    /**
     * Do a search for all assets under the dam root path without onCloudinary = true.  Iterate through them and push to cloudinary.
     * Run according to service interval.
     */
    public void run() {

        try (ResourceResolver resolver = resolverFactoryService.getDefaultResourceResolver()) {

            for(CloudinaryConfigurationService configurationService
                    : cloudinaryService.getCloudinaryConfigurationServices()) {

                String folder = configurationService.getDamFolder();
                LOGGER.info("Running DAM to Cloudinary sync for folder {}", folder);

                try {
                    Map<String, String> queryParams = buildQueryParams(folder);
                    SearchResult result = findNodes(resolver, queryParams, 0, SEARCH_LIMIT);
                    if(result != null) {
                        List<Hit> hits = result.getHits();
                        for (Hit hit : hits) {
                            try {
                                Asset asset = hit.getResource().adaptTo(Asset.class);
                                if (null != asset.getMetadata().get(ON_CLOUDINARY_KEY)) {
                                    cloudinaryService.upload(asset);
                                }
                            } catch (Exception e) {
                                LOGGER.error(e.getMessage(), e);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Cannot sync folder {}", folder, e);
                }

            }

        } catch (LoginException e) {
            LOGGER.error(e.toString());
        }

    }

    final static String METADATA = JCR_CONTENT + "/metadata/";
    final static String _1_PROPERTY = "1_" + PROPERTY;
    final static String _2_PROPERTY = "2_" + PROPERTY;
    /**
     * Gets a query params map to search for all items under the the given folder dam to upload to Cloudinary.
     *
     * @return query params
     */
    private Map<String, String> buildQueryParams(String damFolder) {

        /* look for assets under our configured root */
        Map<String, String> searchmap = new HashMap<String, String>();
        searchmap.put(TypePredicateEvaluator.TYPE, DamConstants.NT_DAM_ASSET);
        searchmap.put(PathPredicateEvaluator.PATH, damFolder);

        searchmap.put(_1_PROPERTY, METADATA + ON_CLOUDINARY_KEY);
        /* Operation to perform on the value of the prop, in this case existence check */
        searchmap.put(_1_PROPERTY + "." + OPERATION, OP_NOT);
        searchmap.put(_1_PROPERTY + "." + VALUE, String.valueOf(Boolean.TRUE));
        searchmap.put(_2_PROPERTY, METADATA + CLOUDINARY_STATUS);
        searchmap.put(_2_PROPERTY, "failed");
        searchmap.put(_2_PROPERTY + "." + OPERATION, OP_NOT);

        return searchmap;
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
    private static SearchResult findNodes(ResourceResolver resourceResolver,
                                         Map<String, String> params, int offset, int max) {
        Session session = resourceResolver.adaptTo(Session.class);
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
        Query query = builder.createQuery(PredicateGroup.create(params), session);
        query.setStart(offset);
        query.setHitsPerPage(max);
        return query.getResult();
    }

}
