package com.omnigon.aem.cloudinary.workflows;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.process.AbstractAssetWorkflowProcess;
import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.omnigon.aem.cloudinary.CloudinaryService;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.*;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * workflow process that uploads newly added DAM images to the Cloudinary servers.
 */
@Component(label = "Cloudinary Image Uploader", metatype = true, immediate = true)
@Service
@Properties({
        @Property(name = Constants.SERVICE_DESCRIPTION, value = "Upload DAM images to Cloudinary."),
        @Property(name = Constants.SERVICE_VENDOR, value = "Omnigon"),
        @Property(name = "process.label", value = "Coudinary image upload Workflow")
})
public class CloudinaryUploader extends AbstractAssetWorkflowProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudinaryUploader.class);

    @Reference
    private CloudinaryService cloudinaryService;

    public CloudinaryService getCloudinaryService() {
        return cloudinaryService;
    }

    public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {
        LOGGER.debug("Uploading of image in DAM to Cloudinary - STARTED");

        WorkflowData workflowData = item.getWorkflowData();
        if (workflowData != null && workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {
            try {
                final Asset asset = getAssetFromPayload(item, session.getSession());
                /* filter to image type only */
                if (asset != null && StringUtils.contains(asset.getMimeType(), "image")) {
                    processAsset(asset);
                }
            } catch (Exception e) {
                LOGGER.error("CloudinaryUploader cannot process a node", e);
                throw new WorkflowException(e);
            }
        }
        WorkflowUtils.advanceWorkflow(item, session);
    }

    /**
     * @param asset
     */
    private void processAsset(final Asset asset){
        String path = asset.getPath();
        getCloudinaryService().upload(asset);
    }
}