
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

import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.api.scripting.SlingScriptHelper;

import javax.servlet.ServletRequest;

public final class ServiceUtils {

    private ServiceUtils() {
        super();
    }

    public static <T> T getService(final Class<T> serviceType, ServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request is null, Class = " + serviceType);
        } else {
            SlingBindings bindings = (SlingBindings) request.getAttribute(SlingBindings.class.getName());

            T service = null;

            if (bindings != null) {
                SlingScriptHelper sling = bindings.getSling();

                if (sling != null) {
                    service = sling.getService(serviceType);
                }
            }

            return service;
        }
    }
}
