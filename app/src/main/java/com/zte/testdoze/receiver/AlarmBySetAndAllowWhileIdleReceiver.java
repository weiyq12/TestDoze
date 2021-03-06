package com.zte.testdoze.receiver;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.zte.testdoze.utils.DozeUtil;
import com.zte.util.LogUtil;

/**
 * 以setAndAllowWhileIdle方式启动的定期闹钟
 * Created by weiyiqing on 16/11/6.
 */
public class AlarmBySetAndAllowWhileIdleReceiver extends BroadcastReceiver
{
    private static AlarmBySetAndAllowWhileIdleReceiver instance = new AlarmBySetAndAllowWhileIdleReceiver();

    private static boolean isAlreadyEvoked = false;

    private String tag = getClass().getSimpleName();

    private long intervalMillis = 60 * 1000;

    private int seq = 0;

    private AlarmBySetAndAllowWhileIdleReceiver()
    {
    }

    private static AlarmBySetAndAllowWhileIdleReceiver getInstance()
    {
        return instance;
    }

    /**
     * 是否已经启动Alarm
     * @return 结果
     * true-是
     * false-否
     */
    public static boolean isAlreadyEvoked()
    {
        return isAlreadyEvoked;
    }

    /**
     * 启动，默认周期间隔时间 60 * 1000 毫秒
     * @param context 应用上下文
     */
    public static void start(Context context)
    {
        getInstance().oper(context, 0, 60 * 1000);
    }

    /**
     * 启动
     * @param context 应用上下文
     * @param intervalMillis 重复间隔时间，单位毫秒
     */
    public static void start(Context context, long intervalMillis)
    {
        getInstance().oper(context, 0, intervalMillis);
    }

    /**
     * 停止
     * @param context 应用上下文
     */
    public static void stop(Context context)
    {
        getInstance().oper(context,1,  -1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private synchronized void oper(Context context, int type, long intervalMillis)
    {
        String action = getInstance().getClass().getName();

        LogUtil.i(tag, "oper context=" + context
                + ", type=" + type
                + ", intervalMillis=" + intervalMillis
                + ", action=" + action
                + ", isAlreadyEvoked=" + isAlreadyEvoked);

        if (0 == type)
        {
            if (intervalMillis <= 0)
            {
                LogUtil.e(tag, "param intervalMillis can not be less than 0.");
                return;
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            {
                LogUtil.e(tag, "setAndAllowWhileIdle need SDK_INT not less than " + Build.VERSION_CODES.M
                        + ", current SDK_INT: " + Build.VERSION.SDK_INT);
                return;
            }

            // 启动
            if (isAlreadyEvoked)
            {
                LogUtil.d(tag, "alarm already started, so return.");
                return;
            }
            isAlreadyEvoked = true;
            seq = 0;

            IntentFilter filter = new IntentFilter();
            filter.addAction(action);
            filter.setPriority(Integer.MAX_VALUE);
            context.registerReceiver(getInstance(), filter);

            AlarmManager localAlarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
            PendingIntent localPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(action), PendingIntent.FLAG_CANCEL_CURRENT);
            localAlarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalMillis, localPendingIntent);

            this.intervalMillis = intervalMillis;
        }
        else
        {
            // 关闭
            if (!isAlreadyEvoked)
            {
                LogUtil.d(tag, "alarm already stoped, so return.");
                return;
            }
            isAlreadyEvoked = false;

            context.unregisterReceiver(getInstance());

            AlarmManager localAlarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
            PendingIntent localPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(action), PendingIntent.FLAG_NO_CREATE);
            if (null != localAlarmManager && null != localPendingIntent)
            {
                localAlarmManager.cancel(localPendingIntent);
                localPendingIntent.cancel();
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        LogUtil.i(tag, "onReceive seq=" + (++seq)
                + ", isAlreadyEvoked=" + isAlreadyEvoked);

        if (null == context)
        {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            LogUtil.i(tag, "isIgnoringBatteryOptimizations=" + DozeUtil.isIgnoringBatteryOptimizations(context)
                    + ", isDeviceIdleMode=" + DozeUtil.isDeviceIdleMode(context));

            if (!isAlreadyEvoked)
            {
                return;
            }

            String action = getInstance().getClass().getName();
            AlarmManager localAlarmManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
            PendingIntent localPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(action), PendingIntent.FLAG_CANCEL_CURRENT);
            localAlarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalMillis, localPendingIntent);
        }
    }
}
