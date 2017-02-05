package com.zte.testdoze.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.zte.util.LogUtil;

/**
 * Created by weiyiqing on 16/11/6.
 */
public class IgnoredBatteryOptiActivity extends Activity
{
    private static final int REQUEST_CODE = 100;

    private String tag = getClass().getSimpleName();

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onStart()
    {
        LogUtil.i(tag, "onStart()");

        super.onStart();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            Toast.makeText(this, "SDK_INT不小于" + Build.VERSION_CODES.M
                    + "的Android版本才有忽略电池优化功能，您当前的SDK_INT为："
                    + Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();
            LogUtil.i(tag, "SDK_INT不小于" + Build.VERSION_CODES.M
                    + "的Android版本才有忽略电池优化功能，您当前的SDK_INT为："
                    + Build.VERSION.SDK_INT);

            finish();
            return;
        }

        try
        {
            // 打开指定应用的设置界面
            Intent intent = new Intent();
            // intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "打开忽略电池优化界面失败，Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            LogUtil.e(tag, "打开忽略电池优化界面失败，Exception: " + e.getMessage());
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onStop()
    {
        LogUtil.i(tag, "onStop()");

        super.onStop();

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        LogUtil.i(tag, "onActivityResult requestCode=" + requestCode
                + ", resultCode=" + resultCode
                + ", data=" + data);

        if (REQUEST_CODE == requestCode)
        {
            if (RESULT_OK == resultCode)
            {
                Toast.makeText(this, "忽略电池优化的设置成功！", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "忽略电池优化的设置失败(" + resultCode + ")！", Toast.LENGTH_SHORT).show();
            }

            finish();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
