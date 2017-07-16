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
package com.omnigon.aem.common.utils.url;

class NullUrlBuilder extends SlingUrlBuilder {
    @Override
    public SlingUrlBuilder setSuffix(final String suffix) {
        return this;
    }

    @Override
    public SlingUrlBuilder addSelector(final String selector) {
        return this;
    }

    @Override
    public SlingUrlBuilder setExtension(final String extension) {
        return this;
    }

    @Override
    public String build() {
        return null;
    }
}
