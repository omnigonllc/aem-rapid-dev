package com.omnigon.aem.cloudinary.impl;

import com.day.cq.dam.api.DamConstants;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.omnigon.aem.cloudinary.CloudinaryConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.commons.osgi.PropertiesUtil;

import java.util.Arrays;
import java.util.Map;

/**
 * @author e.yubin: Marcus
 *         Date: 9/23/13
 *         Time: 4:29 PM
 * @version 1.0
 */
@Component(configurationFactory = true, label = "Cloudinary Configuration Service",
        description = "Cloudinary Configuration Service", metatype = true, policy = ConfigurationPolicy.REQUIRE)
@Service
public class CloudinaryConfigurationServiceImpl implements CloudinaryConfigurationService {

    private static final String CLOUD_NAME = "cloud_name";
    private static final String API_KEY = "api_key";
    private static final String API_SECRET = "api_secret";
    private static final String CNAME = "cname";

    @Property(label = "Static Cloud Name", description = "Static Cloud Name")
    private static final String STATIC_CLOUD_NAME = "staticCloudName";
    @Property(label = "Static Api Key", description = "Static Api Key")
    private static final String STATIC_API_KEY = "staticApiKey";
    @Property(label = "Static Api Secret", description = "Static Api Secret")
    private static final String STATIC_API_SECRET = "staticApiSecret";
    @Property(label = "Static Custom Domain", description = "Static Custom Domain")
    private static final String STATIC_CNAME = "staticCname";
    @Property(label = "Mapped DAM Folder", description = "Mapped DAM Folder")
    private static final String DAM_FOLDER = "damFolder";
    @Property(boolValue = false, label = "Synchronize DAM Folder", description = "Synchronize DAM Folder")
    private static final String DAM_SYNC = "damSync";

    private String cloudName;
    private String damFolder;
    private boolean damSynchronized;

    private ImmutableMap<String, Object> configuration;

    @Activate
    @Modified
    private void activate(Map<String, Object> properties) {
        ImmutableMap.Builder<String, Object> configBuilder = ImmutableMap.builder();
        configBuilder
                .put(CLOUD_NAME, PropertiesUtil.toString(properties.get(STATIC_CLOUD_NAME), StringUtils.EMPTY))
                .put(API_KEY, PropertiesUtil.toString(properties.get(STATIC_API_KEY), StringUtils.EMPTY))
                .put(API_SECRET, PropertiesUtil.toString(properties.get(STATIC_API_SECRET), StringUtils.EMPTY));

        String staticCName = PropertiesUtil.toString(properties.get(STATIC_CNAME), StringUtils.EMPTY);

        if (StringUtils.isNotBlank(staticCName)) {
            configBuilder.put(CNAME, staticCName).put("private_cdn", true);
        }

        this.configuration = configBuilder.build();

        cloudName = (String) configuration.get(CLOUD_NAME);

        // Initialize DAM Folder property
        damFolder = PropertiesUtil.toString(properties.get(DAM_FOLDER), cloudName);
        if(!damFolder.startsWith(DamConstants.MOUNTPOINT_ASSETS)) {
            if(!damFolder.startsWith("/")) {
                damFolder = "/" + damFolder;
            }
            damFolder = DamConstants.MOUNTPOINT_ASSETS + damFolder;
        }

        damSynchronized = PropertiesUtil.toBoolean(properties.get(DAM_SYNC), false);
    }

    @Override
    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    @Override
    public Map<String, Object> getClientConfiguration() {
        return createClientSideMap(configuration);
    }

    @Override
    public String getCloudName() {
        return cloudName;
    }

    @Override
    public String getDamFolder() {
        return damFolder;
    }

    @Override
    public boolean getDamSynchronized() {
        return damSynchronized;
    }

    private Map<String, Object> createClientSideMap(final Map<String, Object> map) {
        return ImmutableMap.copyOf(
                Maps.filterKeys(map, Predicates.not(Predicates.in(Arrays.asList(API_KEY, API_SECRET))))
        );
    }

}