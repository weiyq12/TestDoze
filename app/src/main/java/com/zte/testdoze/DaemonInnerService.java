package com.zte.testdoze;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.zte.util.LogUtil;

public class DaemonInnerService extends Service
{
    private final String tag = getClass().getSimpleName();

    private static final int GRAY_SERVICE_ID = 100;

    private void grayGuard()
    {
        if (Build.VERSION.SDK_INT < 18)
        {
            //API < 18 ，此方法能有效隐藏Notification上的图标
            startForeground(GRAY_SERVICE_ID, new Notification());
        }
        else
        {
            Intent innerIntent = new Intent(this, DaemonInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }
    }

    @Override
    public void onCreate()
    {
        LogUtil.i(tag, "InnerService -> onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        LogUtil.i(tag, "InnerService -> onStartCommand");
        startForeground(GRAY_SERVICE_ID, new Notification());
        //stopForeground(true);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy()
    {
        LogUtil.i(tag, "InnerService -> onDestroy");
        super.onDestroy();
    }
}
