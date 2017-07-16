package com.omnigon.aem.common.utils;

import org.apache.sling.settings.SlingSettingsService;

import javax.servlet.ServletRequest;

/**
 * Util to get run mode
 */
public final class RunMode {
    private static final String MODE_AUTHOR = "author";
    private static final String MODE_LOCAL = "local";

    private boolean author;
    private boolean local;

    private RunMode(SlingSettingsService settingsService) {
        this.author = settingsService.getRunModes().contains(MODE_AUTHOR);
        this.local = settingsService.getRunModes().contains(MODE_LOCAL);
    }

    public boolean isAuthor() {
        return author;
    }

    public boolean isLocal() {
        return local;
    }

    public boolean isAuthorOrLocal() {
        return isAuthor() || isLocal();
    }

    public static RunMode newInstance(ServletRequest request) {
        return newInstance(ServiceUtils.getService(SlingSettingsService.class, request));
    }

    public static RunMode newInstance(SlingSettingsService settingsService) {
        return new RunMode(settingsService);
    }

}