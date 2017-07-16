package com.omnigon.aem.cloudinary.workflows;

import com.day.cq.dam.commons.process.AbstractAssetWorkflowProcess;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.omnigon.aem.cloudinary.CloudinaryService;
import org.apache.felix.scr.annotations.*;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Workflow process to remove images from cloudinary,
 * when they removed from DAM
 *
 * @author Yuri Kozub
 */

@Component(label = "Cloudinary Image Remover", metatype = true, immediate = true)
@Service
@Properties({
        @Property(name = Constants.SERVICE_DESCRIPTION, value = "Remove DAM images from Cloudinary."),
        @Property(name = Constants.SERVICE_VENDOR, value = "Omnigon"),
        @Property(name = "process.label", value = "Coudinary image remove Workflow")
})
public class CloudinaryRemover extends AbstractAssetWorkflowProcess {

    private final Logger LOGGER = LoggerFactory.getLogger(CloudinaryRemover.class);

    @Reference
    private CloudinaryService cloudinaryService;

    private static final Pattern IMG_PATTERN = Pattern.compile("\\/content\\/dam\\/([\\w-\\/]+)\\.(jpg|JPG|png|PNG|bmp|BMP|gif|GIF|tiff|TIFF)$");

    public CloudinaryService getCloudinaryService() {
        return cloudinaryService;
    }

    public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
        LOGGER.debug("Removing image from Cloudinary - STARTED");

        WorkflowData workflowData = item.getWorkflowData();
        if (workflowData != null && workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {
            String path = (String) workflowData.getPayload();
            processImage(path);
        }
        WorkflowUtils.advanceWorkflow(item, session);
    }

    private void processImage(final String path) throws WorkflowException {
        Matcher matcher = IMG_PATTERN.matcher(path);
        if (matcher.matches()) {
            String imageId = matcher.group(1);
            try {
                getCloudinaryService().remove(imageId, path);
            } catch (Exception e) {
                LOGGER.error("Cannot process a node", e);
                throw new WorkflowException(e);
            }
        }
    }

}