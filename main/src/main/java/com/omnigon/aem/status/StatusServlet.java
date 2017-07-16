package com.omnigon.aem.status;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Just a dummy health-check servlet to ensure that bundle is OK and services running.
 * TODO: May be removed in real project.
 */
@SlingServlet(label = "Status Servlet",
        methods = HttpConstants.METHOD_GET,
        paths = "/bin/status")
public class StatusServlet extends SlingSafeMethodsServlet {

    @Reference
    private StatusService statusService;

    StatusService getStatusService() {
        return statusService;
    }

    @Override
    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("Status: " + getStatusService().getStatus());
    }

}
