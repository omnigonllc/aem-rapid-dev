package com.omnigon.aem.cloudinary.workflows;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.Route;
import com.day.cq.workflow.exec.WorkItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.List;

public final class WorkflowUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowUtils.class);
    private static final String XMPRIGHTS_PROPERTY = "xmpRights:WebStatement";

    /**
     * Deny outside instantiation.
     */
    private WorkflowUtils() {
    }
    
    /**
     * 
     * @param node -
     * @param session -
     */
    public static void removeProperty(final Node node, final Session session) {
        try {
            // check again if it has the property
            if (node.hasProperty(XMPRIGHTS_PROPERTY)) {
                LOGGER.debug("Removing from: {}", node.getPath());
                javax.jcr.Property prop = node.getProperty(XMPRIGHTS_PROPERTY);
                prop.remove();
            }
            // save
            session.save();

        } catch (final Exception e) {
            LOGGER.error("Error while deleting XMPRIGHTS_PROPERTY: ", e);
        }
    }
    
    /**
     * 
     * @param workItem -
     * @param workflowSession -
     * @throws WorkflowException -
     */
    public static void advanceWorkflow(final WorkItem workItem, final WorkflowSession workflowSession)
        throws WorkflowException {
        final List<Route> routes = workflowSession.getRoutes(workItem);

        boolean advanced = false;

        for (final Route route : routes) {
            if (route.hasDefault()) {
                workflowSession.complete(workItem, route);
                advanced = true;
            }
        }

        if (!advanced) {
            workflowSession.complete(workItem, routes.get(0));
        }
    }
}
