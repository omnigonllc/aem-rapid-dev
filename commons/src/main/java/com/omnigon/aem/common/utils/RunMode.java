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