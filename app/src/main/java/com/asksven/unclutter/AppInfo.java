package com.asksven.unclutter;

/**
 * Created by sven on 04.09.2017.
 */

/**
 * Value holder for app info stuff
 */
public class AppInfo
{
    private String appName;
    private String packageName;
    private long installed;
    private long lastUsed;
    private long lastUpdated;
    private long timeInForeground;

    AppInfo(String appName, String packageName, long installed, long updated, long lastUsed, long timeInForeground)
    {
        this.appName            = appName;
        this.packageName        = packageName;
        this.installed          = installed;
        this.lastUpdated        = updated;
        this.lastUsed           = lastUsed;
        this.timeInForeground   = timeInForeground;
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public long getInstalled()
    {
        return installed;
    }

    public void setInstalled(long installed)
    {
        this.installed = installed;
    }

    public long getLastUsed()
    {
        return lastUsed;
    }

    public void setLastUsed(long lastUsed)
    {
        this.lastUsed = lastUsed;
    }

    public long getTimeInForeground()
    {
        return timeInForeground;
    }

    public void setTimeInForeground(long timeInForeground)
    {
        this.timeInForeground = timeInForeground;
    }
}
