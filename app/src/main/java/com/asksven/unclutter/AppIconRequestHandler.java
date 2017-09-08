package com.asksven.unclutter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;

/**
 * Created by sven on 07.09.2017.
 */

public class AppIconRequestHandler extends RequestHandler
{

    public static final String SCHEME_PNAME = "package";

    private final PackageManager pm;
    private final int dpi;
    private Bitmap defaultAppIcon;

    public AppIconRequestHandler(Context context)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        dpi = am.getLauncherLargeIconDensity();
        pm = context.getPackageManager();
    }

    @Override
    public boolean canHandleRequest(Request data)
    {
        return data.uri != null && TextUtils.equals(data.uri.getScheme(), SCHEME_PNAME);
    }

    @Override
    public Result load(Request request, int networkPolicy) throws IOException
    {
        try
        {
            return new Result(getFullResIcon(request.uri.toString().split(":")[1]), Picasso.LoadedFrom.DISK);
        } catch (PackageManager.NameNotFoundException e)
        {
            return null;
        }
    }

    private Bitmap getFullResIcon(String packageName) throws PackageManager.NameNotFoundException
    {
        return getFullResIcon(pm.getApplicationInfo(packageName, 0));
    }

    private Bitmap getFullResIcon(ApplicationInfo info)
    {
        try
        {
            Resources resources = pm.getResourcesForApplication(info.packageName);
            if (resources != null)
            {
                int iconId = info.icon;
                if (iconId != 0)
                {
                    return getFullResIcon(resources, iconId);
                }
            }
        } catch (PackageManager.NameNotFoundException ignored)
        {
        }
        return getFullResDefaultActivityIcon();
    }

    private Bitmap getFullResIcon(Resources resources, int iconId)
    {
        final Drawable drawable;
        try
        {
            drawable = resources.getDrawableForDensity(iconId, dpi, null);
        } catch (Resources.NotFoundException e)
        {
            return getFullResDefaultActivityIcon();
        }
        return drawableToBitmap(drawable);
    }

    private Bitmap getFullResDefaultActivityIcon()
    {
        if (defaultAppIcon == null)
        {
            Drawable drawable;
            drawable = Resources.getSystem().getDrawableForDensity(
                    android.R.mipmap.sym_def_app_icon, dpi);
            defaultAppIcon = drawableToBitmap(drawable);
        }
        return defaultAppIcon;
    }

    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable)
        {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null)
            {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0)
        {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else
        {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}