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

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * User: Andrey Bardashevsky
 * Date/Time: 17.10.13 23:26
 */
public final class ResourceUtils {
  private static final Logger LOG = LoggerFactory.getLogger(ResourceUtils.class);

  private ResourceUtils() {
  }

  /**
   *
   * @param resourceResolver -
   * @param path to resource in JCR
   * @return resource as input stream
   */
  public static InputStream getInputStream(final ResourceResolver resourceResolver, final String path) {
    Resource resource = resourceResolver.getResource(path);

    if (resource != null) {
      Node node = resource.adaptTo(Node.class);

      try {
        if (node.isNodeType(DamConstants.NT_DAM_ASSET)) {
          return resource.adaptTo(Asset.class).getOriginal().getStream();
        } else {
          return resource.adaptTo(InputStream.class);
        }
      } catch (RepositoryException e) {
        LOG.error(e.getMessage(), e);
      }
    }

    return null;
  }

  public static void close(ResourceResolver resourceResolver) {
    if (resourceResolver != null && resourceResolver.isLive()) {
      resourceResolver.close();
    }
  }

  /**
   *
   * @param res -
   * @return resource as {@link ModifiableValueMap}
   */
  public static ModifiableValueMap getModifiableValueMap(final Resource res) {
    return (res != null) ? res.adaptTo(ModifiableValueMap.class) : null;
  }

  /**
   *
   * @param resource -
   * @return identifier based on resource path
   */
  public static String generateId(Resource resource) {
    if (resource != null) {
      return generateId(resource.getPath());
    } else {
      return null;
    }
  }

  /**
   *
   * @param path -
   * @return identifier based on path
   */
  public static String generateId(String path) {
    if (org.apache.commons.lang3.StringUtils.isNotBlank(path)) {
      return Hashing.murmur3_32().hashString(path, Charsets.UTF_8).toString();
    } else {
      return null;
    }
  }

  public static void orderChildren(Resource resource, String propertyName, Comparator comp, Boolean desc) {
    orderChildren(resource.adaptTo(Node.class), propertyName, comp, desc);
  }

  public static void orderChildren(Node node, String propertyName, Comparator comp, Boolean desc) {
    try {
      NodeIterator it = node.getNodes();
      List<Node> nodes = new ArrayList<Node>();
      while (it.hasNext()) {
        nodes.add(it.nextNode());
      }
    }catch(Exception e) {
      LOG.error(e.getMessage());
    }
  }

}
