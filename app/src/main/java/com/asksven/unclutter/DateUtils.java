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
    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_SHORT = "HH:mm:ss";
    private static final Calendar m_cal = Calendar.getInstance();

    /**
     * Returns the current date in the default format.
     * @return the current formatted date/time
     */
    public static String now()
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        long now = System.currentTimeMillis();
        return sdf.format(new Date(now));
    }

    /**
     * Returns the current date in a given format.
     * DateUtils.now("dd MMMMM yyyy")
     * DateUtils.now("yyyyMMdd")
     * DateUtils.now("dd.MM.yy")
     * DateUtils.now("MM/dd/yy")
     * @param dateFormat a date format (See examples)
     * @return the current formatted date/time
     */
    public static String now(String dateFormat)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        long now = System.currentTimeMillis();
        return sdf.format(new Date(now));
    }

    public static String format(String dateFormat, Date time)
    {

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(time);
    }

    public static String format(String dateFormat, Long time)
    {

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(time);
    }

    public static String format(Date time)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(time);
    }

    public static String format(long timeMs)
    {
        return format(timeMs, DATE_FORMAT_NOW);
    }

    public static String formatShort(long timeMs)
    {
        return format(timeMs, DATE_FORMAT_SHORT);
    }

    public static String format(long timeMs, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(timeMs);
    }




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