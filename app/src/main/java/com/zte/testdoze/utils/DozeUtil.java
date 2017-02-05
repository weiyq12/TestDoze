package com.zte.testdoze.utils;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.PowerManager;

public class DozeUtil
{
    private static String versionName;

    /**
     * 是否忽略了省电优化
     * @param context 上下文对象，不能为空
     * @return 结果：
     * true-已忽略
     * false-未忽略
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isIgnoringBatteryOptimizations(Context context)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return true;
        }

        String packageName = context.getPackageName();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm.isIgnoringBatteryOptimizations(packageName);
    }

    /**
     * 是否进入doze状态了
     * @param context 上下文对象
     * @return 结果
     * true-是
     * false-否
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isDeviceIdleMode(Context context)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return false;
        }

        PowerManager pm = (PowerManager) context.getSystemService(Service.POWER_SERVICE);
        return pm.isDeviceIdleMode();
    }

    /**
     * 获取AndroidManifest.xml中的versionName
     * @param context 上下文对象，不能为空
     * @return 版本号
     */
    public static String getVersionName(Context context)
    {
        if (null == versionName)
        {
            try
            {
                PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                versionName = info.versionName;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return versionName;
    }

    /**
     * 手机屏幕是否亮着
     * @param context 上下文对象，不能为空
     * @return 结果：
     * true-是
     * false-否
     */
    @SuppressWarnings("deprecation")
    public static boolean isScreenOn(Context context)
    {
        boolean isScreenOn = false;
        
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= 20)
        {
            isScreenOn = pm.isInteractive();
        }
        else if (Build.VERSION.SDK_INT >= 7)
        {
            isScreenOn = pm.isScreenOn();
        }
        
        return isScreenOn;
    }
}
