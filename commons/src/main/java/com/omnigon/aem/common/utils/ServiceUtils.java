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
