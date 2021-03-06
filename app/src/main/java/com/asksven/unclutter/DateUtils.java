/*
 * Copyright (C) 2011 asksven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.asksven.unclutter;

/**
 * @author sven
 *
 */
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class DateUtils
{
    /**
     * Formats milliseconds to a friendly non abbreviated form (days, hrs, min, sec)
     * @param millis
     * @return the formated string
     */
    public static String formatDurationLong(long millis)
    {
        String ret = "";

        int seconds = (int) Math.floor(millis / 1000);

        int days = 0, hours = 0, minutes = 0;
        if (seconds > (60*60*24)) {
            days = seconds / (60*60*24);
            seconds -= days * (60*60*24);
        }
        if (seconds > (60 * 60)) {
            hours = seconds / (60 * 60);
            seconds -= hours * (60 * 60);
        }
        if (seconds > 60) {
            minutes = seconds / 60;
            seconds -= minutes * 60;
        }
        ret = "";
        if (days > 0)
        {
            if (days == 1)
            {
                ret += days + " day ";
            }
            else
            {
                ret += days + " days ";
            }
        }
        else
        {
            if (hours > 0)
            {
                if (hours <= 1)
                {
                    ret += hours + " hr ";
                } else
                {
                    ret += hours + " hrs ";
                }
            } else
            {
                if ((hours == 0) && (days == 0))
                    ret = "< 1 hr";
            }
        }
        return ret;
    }

}