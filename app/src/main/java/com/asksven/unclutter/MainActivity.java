package com.asksven.unclutter;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.Tracking;
import net.hockeyapp.android.UpdateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<AppInfo> apps = new ArrayList<AppInfo>();
    private List<PackageInfo> packages = null;
    private PackageManager pm = null;
    private UsageStatsManager um = null;
    Map<String, UsageStats> usageStats = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Picasso.setSingletonInstance(new Picasso.Builder(this)
                .addRequestHandler(new AppIconRequestHandler(this))
                .build());


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.INVISIBLE);

        progressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
        progressBar.setVisibility(View.VISIBLE);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        apps.clear();
        mAdapter = new AppListAdapter(apps);
        recyclerView.setAdapter(mAdapter);

        pm = getPackageManager();
        long now = System.currentTimeMillis();
        um = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        usageStats = um.queryAndAggregateUsageStats(now - (365 * 24 * 60 * 60 * 1000), now);

        new AppInfoTask().execute("foo", "bar");
    }

    /* Request updates at startup */
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i(TAG, "OnResume called");

        // we need to make sure that hockeyapp actions never crash the app
        try
        {
            CrashManager.register(this);
            Tracking.startUsage(this);

            PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            if (pinfo.packageName.endsWith("_hockeyappedition"))
            {
                UpdateManager.register(this);
            }
        } catch (Exception e)
        {
        }

        // Analytics opt-in
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        boolean wasPresentedOptOutAnalytics = prefs.getBoolean("analytics_opt_out_offered", false);

        if (!wasPresentedOptOutAnalytics)
        {
            Log.i(TAG, "Application was launched for the first time with analytics");

            Snackbar bar = Snackbar.make(findViewById(android.R.id.content), R.string.pref_app_analytics_summary, Snackbar.LENGTH_LONG)
                    .setAction(R.string.label_button_no, new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("analytics", false);
                            editor.commit();
                        }
                    });

            bar.show();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("analytics_opt_out_offered", true);
            editor.commit();
        }




    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause()
    {
        super.onPause();
        // Hockeyapp
        UpdateManager.unregister();
        Tracking.stopUsage(this);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        // Hockeyapp
        UpdateManager.unregister();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // For former Androif versions see: https://developer.android.com/reference/android/app/ActivityManager.html#getRecentTasks(int, int)
    @Override
    @TargetApi(21)
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        if (id == R.id.action_test)
        {
            final PackageManager pm = getPackageManager();
            final UsageStatsManager um = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);

            //get a list of installed apps.
            List<ApplicationInfo> applications = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_META_DATA);
            long now = System.currentTimeMillis();

            Map<String, UsageStats> usageStats = um.queryAndAggregateUsageStats(now - (365 * 24 * 60 * 60 * 1000), now);

            for (PackageInfo packageInfo : packages)
            {
                // Filter out system apps
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                {
                    Log.d(TAG, "Installed package :" + packageInfo.packageName);
//                    Log.d(TAG, "Installed first :" + DateUtils.formatDurationLong(now - packageInfo.firstInstallTime));
                    Log.d(TAG, "Last updated :" + DateUtils.formatDurationLong(now - packageInfo.lastUpdateTime));
                    UsageStats us = (UsageStats) usageStats.get(packageInfo.packageName);
                    if (us != null)
                    {
                        Log.d(TAG, "Last used :" + DateUtils.formatDurationLong(now - us.getLastTimeUsed()));
                        Log.d(TAG, "Time spend in foregroud :" + DateUtils.formatDurationLong(us.getTotalTimeInForeground()));
                    } else
                    {
                        Log.d(TAG, "No usage stats found for " + packageInfo.packageName);
                    }
//                    Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
//                    Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private class AppInfoTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            // perform long running operation operation

            for (PackageInfo packageInfo : packages)
            {
                // Filter out system apps
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                {
                    ApplicationInfo ai = packageInfo.applicationInfo;
                    String appLabel = "unknown";
                    if (ai != null)
                    {
                        appLabel = (String) pm.getApplicationLabel(packageInfo.applicationInfo);
                    }

                    AppInfo app = new AppInfo(appLabel, packageInfo.packageName, packageInfo.firstInstallTime, packageInfo.lastUpdateTime, -1, -1);
                    UsageStats us = (UsageStats) usageStats.get(packageInfo.packageName);
                    if (us != null)
                    {
                        app.setLastUsed(us.getLastTimeUsed());
                        app.setTimeInForeground(us.getTotalTimeInForeground());
                    }

                    apps.add(app);
                }

                Collections.sort(apps, new LastUsedComparator());

            }

            return "done";
        }

        @Override
        protected void onPostExecute(String result)
        {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            progressBar.setIndeterminate(false);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute()
        {

            progressBar.setIndeterminate(true);
            packages = pm.getInstalledPackages(PackageManager.GET_META_DATA);
            apps.clear();

        }

    }

    ;
}
