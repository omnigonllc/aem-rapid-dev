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
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

/**
 * This Handlebars' Helper class prints out a time passed since the given moment:
 *
 * Input format is {{timeago &lt;ISO8601 DATETIME&gt;}}, e.g.  {{timeago '2016-10-17T17:11:12Z'}}
 *
 * Output:
 *
 * < 5m -> now
 * > 5m < 1h -> n×m ago
 * > 1h < 24h -> n×h ago
 * > 24h -> n×d ago
 *
 * where n stands for number of time units passed since the given moment.
 * Unit of measure is lower-cased, if necessary use CSS to uppercase.
 *
 * Moment shall be encoded with ISO8601 format or its subset.
 *
 * TODO: write a unit test, ZonedDateTime.now needs to be mocked properly
 */
@Service
@Component
public class TimeAgoHelper implements HandlebarsHelper {

    private static final Logger logger = LoggerFactory.getLogger(TimeAgoHelper.class);

    private static final String AGO = " ago";
    private static final String NOW = " now";

    private static final int HOUR_IN_MINUTES = 60;
    private static final int DAY_IN_MINUTES = 24 * HOUR_IN_MINUTES;

    public static final String NAME = "timeago";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public CharSequence apply(Object context, Options options) throws IOException {

        ZonedDateTime since, now;
        since = now = ZonedDateTime.now(ZoneOffset.UTC);

        // context contains the given moment
        if(context instanceof String) {
            try {
                since = ZonedDateTime.parse((String)context);
            } catch(DateTimeParseException ex) {
                logger.error("Cannot parse datetime: {}", context);
            }
        } else {
            logger.error("Empty context: {}", context); // it is a HashMap instance that we cannot make use of
        }

        final Duration duration = Duration.between(since, now);
        final long minutes =  duration.toMinutes();

        // NB: no rounding is used
        String ago;
        if( minutes < 5 ) { // duration < 5 minutes
            ago = NOW;
        } else if ( minutes < HOUR_IN_MINUTES ) { // 5 min <= duration < 1 hour
            ago = minutes + "m" + AGO;
        } else if ( minutes < DAY_IN_MINUTES) { // 1 hour <= duration < 1 day
            ago = duration.toHours() + "h" + AGO;
        } else { // duration >= 1 day
            ago = duration.toDays() + "d" + AGO;
        }

        return ago;
    }

}
