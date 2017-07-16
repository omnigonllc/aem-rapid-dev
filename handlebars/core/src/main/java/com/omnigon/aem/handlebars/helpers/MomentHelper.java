/*
 * Copyright (c) 2016 Omnigon Communications, LLC. All rights reserved.
 *
 * This software is the confidential and proprietary information of Omnigon Communications, LLC
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall access and use it only
 * in accordance with the terms of the license agreement you entered into with Omnigon Communications, LLC, its
 * subsidiaries, affiliates or authorized licensee. Unless required by applicable law or agreed to in writing, this
 * Confidential Information is provided on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the license agreement for the specific language governing permissions and limitations.
 */

package com.omnigon.aem.handlebars.helpers;

import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This Handlebars' Helper class formats the given datetime moment with provided format:
 * <p>
 * Examples:
 * <p>
 * {{moment '2016-10-17T17:11:12Z'}}, expected 2016-10-17
 * {{moment '2016-10-17T17:11:12Z' 'DD MMM'}} expected 17 Oct
 * {{moment '2016-10-17T17:11:12Z' format='DD MMM YYYY'}}, expected 17 Oct 2016
 * {{moment format='DD MMM YYYY HH:mm:ss'}}, expected [Current date and time], e.g. 17 Oct 2016 23:02:45
 * <p>
 * Moment shall be encoded with ISO8601 format or its subset.
 * <p>
 * TODO: write a unit test, ZonedDateTime.now needs to be mocked properly
 */
@Service
@Component
public class MomentHelper implements HandlebarsHelper {

    private static Logger logger = LoggerFactory.getLogger(MomentHelper.class);
    public static final String NAME = "moment";

    private static final String DEFAULT_FORMAT = "YYYY-MM-dd";
    private static final String FORMAT_PARAM = "format";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public CharSequence apply(Object context, Options options) throws IOException {

        Date date = null;
        if (context instanceof Date) {
            date = (Date) context;
        } else {
            try {
                date = new Date((Long) context); //date gets converted to Long for handlebars, need to handle.
            } catch(Exception e) {
               logger.error("Invalid object passed as date, cannot cast to string or string is not an integer");
                return StringUtils.EMPTY;
            }

        }

        String format = DEFAULT_FORMAT; // default parameter
        if (options.params.length > 0) {
            format = options.params[0].toString();
        } else if (options.hash.containsKey(FORMAT_PARAM)) {
            format = options.hash.get(FORMAT_PARAM).toString();
        }

        /* Moment.js and Java Datetime format patterns differ in some details.
           Moment.js:
            D DD 1..31 Day of month
            DDD DDDD 1..365 Day of year
            ddd dddd Mon...Sunday Day name in locale set by moment.locale()

           Java:
            D DDD Day in year Number 189
            d dd Day in month Number 10

            So we need to convert D to d and DD to dd.
        */
        if (!format.contains("DDD") // true for DDDD as well
                && format.contains("D")) { // true for DD as well
            format = format.replace("D", "d");
        }

        String formattedMoment = format(format, date);

        return formattedMoment;
    }

    public static String format(String pattern, Date date) {
        if (StringUtils.isBlank(pattern) || date == null) {
            return StringUtils.EMPTY;
        }
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

}
