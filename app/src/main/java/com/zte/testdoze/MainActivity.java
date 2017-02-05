package com.zte.testdoze;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zte.testdoze.activity.IgnoredBatteryOptiActivity;
import com.zte.testdoze.receiver.AlarmBySetAndAllowWhileIdleReceiver;
import com.zte.testdoze.receiver.AlarmBySetExactAndAllowWhileIdleReceiver;
import com.zte.testdoze.receiver.AlarmBySetExactReceiver;
import com.zte.testdoze.receiver.AlarmBySetInexactRepeatingReceiver;
import com.zte.testdoze.receiver.AlarmBySetRepeatingReceiver;
import com.zte.testdoze.service.MyJobService;
import com.zte.testdoze.utils.DozeUtil;
import com.zte.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements View.OnClickListener
{
    private Button btn_check_battery;
    private Button btn_pop_ignore_battery;
    private EditText et_interval_seconds;
    private Button btn_start_setRepeating;
    private Button btn_stop_setRepeating;
    private Button btn_start_setInexactRepeating;
    private Button btn_stop_setInexactRepeating;
    private Button btn_start_setExact;
    private Button btn_stop_setExact;
    private Button btn_start_jobScheduler;
    private Button btn_stop_jobScheduler;
    private Button btn_start_setAndAllowWhileIdle;
    private Button btn_stop_setAndAllowWhileIdle;
    private Button btn_start_setExactAndAllowWhileIdle;
    private Button btn_stop_setExactAndAllowWhileIdle;
    private Button btn_start_timer;
    private Button btn_stop_timer;
    private Button btn_start_thread;
    private Button btn_stop_thread;

    private String tag = getClass().getSimpleName();

    private static final int JOB_TASK_ID = 1;
    private JobScheduler jobScheduler;
    private MyTimerTask myTimerTask;
    private MyThread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化组件
        initWidget();
        // 初始化数据
        initData();
    }

    private void initWidget()
    {
        btn_check_battery = (Button) findViewById(R.id.btn_check_battery);
        btn_pop_ignore_battery = (Button) findViewById(R.id.btn_pop_ignore_battery);
        et_interval_seconds = (EditText) findViewById(R.id.et_interval_seconds);
        btn_start_setRepeating = (Button) findViewById(R.id.btn_start_setRepeating);
        btn_stop_setRepeating = (Button) findViewById(R.id.btn_stop_setRepeating);
        btn_start_setInexactRepeating = (Button) findViewById(R.id.btn_start_setInexactRepeating);
        btn_stop_setInexactRepeating = (Button) findViewById(R.id.btn_stop_setInexactRepeating);
        btn_start_setExact = (Button) findViewById(R.id.btn_start_setExact);
        btn_stop_setExact = (Button) findViewById(R.id.btn_stop_setExact);
        btn_start_jobScheduler = (Button) findViewById(R.id.btn_start_jobScheduler);
        btn_stop_jobScheduler = (Button) findViewById(R.id.btn_stop_jobScheduler);
        btn_start_setAndAllowWhileIdle = (Button) findViewById(R.id.btn_start_setAndAllowWhileIdle);
        btn_stop_setAndAllowWhileIdle = (Button) findViewById(R.id.btn_stop_setAndAllowWhileIdle);
        btn_start_setExactAndAllowWhileIdle = (Button) findViewById(R.id.btn_start_setExactAndAllowWhileIdle);
        btn_stop_setExactAndAllowWhileIdle = (Button) findViewById(R.id.btn_stop_setExactAndAllowWhileIdle);
        btn_start_timer = (Button) findViewById(R.id.btn_start_timer);
        btn_stop_timer = (Button) findViewById(R.id.btn_stop_timer);
        btn_start_thread = (Button) findViewById(R.id.btn_start_thread);
        btn_stop_thread = (Button) findViewById(R.id.btn_stop_thread);

        btn_check_battery.setOnClickListener(this);
        btn_pop_ignore_battery.setOnClickListener(this);
        btn_start_setRepeating.setOnClickListener(this);
        btn_stop_setRepeating.setOnClickListener(this);
        btn_start_setInexactRepeating.setOnClickListener(this);
        btn_stop_setInexactRepeating.setOnClickListener(this);
        btn_start_setExact.setOnClickListener(this);
        btn_stop_setExact.setOnClickListener(this);
        btn_start_jobScheduler.setOnClickListener(this);
        btn_stop_jobScheduler.setOnClickListener(this);
        btn_start_setAndAllowWhileIdle.setOnClickListener(this);
        btn_stop_setAndAllowWhileIdle.setOnClickListener(this);
        btn_start_setExactAndAllowWhileIdle.setOnClickListener(this);
        btn_stop_setExactAndAllowWhileIdle.setOnClickListener(this);
        btn_start_timer.setOnClickListener(this);
        btn_stop_timer.setOnClickListener(this);
        btn_start_thread.setOnClickListener(this);
        btn_stop_thread.setOnClickListener(this);
    }

    private void initData()
    {
        et_interval_seconds.setSelection(et_interval_seconds.getText().toString().length());
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btn_check_battery:
            {
                checkBattery();
                break;
            }
            case R.id.btn_pop_ignore_battery:
            {
                popIgnoreBattery();
                break;
            }
            case R.id.btn_start_setRepeating:
            {
                startSetRepeating();
                break;
            }
            case R.id.btn_stop_setRepeating:
            {
                stopSetRepeating();
                break;
            }
            case R.id.btn_start_setInexactRepeating:
            {
                startSetInexactRepeating();
                break;
            }
            case R.id.btn_stop_setInexactRepeating:
            {
                stopSetInexactRepeating();
                break;
            }
            case R.id.btn_start_setExact:
            {
                startSetExact();
                break;
            }
            case R.id.btn_stop_setExact:
            {
                stopSetExact();
                break;
            }
            case R.id.btn_start_jobScheduler:
            {
                startJobSchedler();
                break;
            }
            case R.id.btn_stop_jobScheduler:
            {
                stopJobScheduler();
                break;
            }
            case R.id.btn_start_setAndAllowWhileIdle:
            {
                startSetAndAllowWhileIdle();
                break;
            }
            case R.id.btn_stop_setAndAllowWhileIdle:
            {
                stopSetAndAllowWhileIdle();
                break;
            }
            case R.id.btn_start_setExactAndAllowWhileIdle:
            {
                startSetExactAndAllowWhileIdle();
                break;
            }
            case R.id.btn_stop_setExactAndAllowWhileIdle:
            {
                stopSetExactAndAllowWhileIdle();
                break;
            }
            case R.id.btn_start_timer:
            {
                startTimer();
                break;
            }
            case R.id.btn_stop_timer:
            {
                stopTimer();
                break;
            }
            case R.id.btn_start_thread:
            {
                startThread();
                break;
            }
            case R.id.btn_stop_thread:
            {
                stopThread();
                break;
            }
        }
    }

    private void checkBattery()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            Toast.makeText(this, "SDK_INT不小于" + Build.VERSION_CODES.M
                    + "的Android版本才有忽略电池优化功能，您当前的SDK_INT为："
                    + Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();
            return;
        }

        boolean isIgnored = DozeUtil.isIgnoringBatteryOptimizations(this);
        if (isIgnored)
        {
            Toast.makeText(this, "已经忽略省电优化", Toast.LENGTH_LONG).show();
            LogUtil.i(tag, "已经忽略省电优化");
        }
        else
        {
            Toast.makeText(this, "还没有忽略省电优化", Toast.LENGTH_LONG).show();
            LogUtil.i(tag, "还没有忽略省电优化");
        }
    }

    private void popIgnoreBattery()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            Toast.makeText(this, "SDK_INT " + Build.VERSION_CODES.M
                    + "以上版本才有忽略电池优化功能，您当前的SDK_INT为："
                    + Build.VERSION.SDK_INT, Toast.LENGTH_LONG).show();
            LogUtil.i(tag, "SDK_INT " + Build.VERSION_CODES.M
                    + "以上版本才有忽略电池优化功能，您当前的SDK_INT为："
                    + Build.VERSION.SDK_INT);
            return;
        }

        startActivity(new Intent(this, IgnoredBatteryOptiActivity.class));
        LogUtil.i(tag, "弹出忽略电池优化界面");
    }

    private void startSetRepeating()
    {
        int interval = Integer.parseInt(et_interval_seconds.getText().toString().trim());
        if (interval <= 0)
        {
            Toast.makeText(this, "闹钟Alarm间隔时长必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        et_interval_seconds.setText(String.valueOf(interval));
        et_interval_seconds.setSelection(et_interval_seconds.getText().toString().length());

        if (AlarmBySetRepeatingReceiver.isAlreadyEvoked())
        {
            Toast.makeText(this, "setRepearting已经启动，若要重启请先点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmBySetRepeatingReceiver.start(getApplicationContext(), interval * 1000);
        Toast.makeText(this, "启动setRepearting", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "启动setRepearting");
    }

    private void stopSetRepeating()
    {
        if (!AlarmBySetRepeatingReceiver.isAlreadyEvoked())
        {
            Toast.makeText(this, "setRepearting已经停止，不要重复点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmBySetRepeatingReceiver.stop(getApplicationContext());
        Toast.makeText(this, "停止setRepearting", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "停止setRepearting");
    }

    private void startSetInexactRepeating()
    {
        int interval = Integer.parseInt(et_interval_seconds.getText().toString().trim());
        if (interval <= 0)
        {
            Toast.makeText(this, "闹钟Alarm间隔时长必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        et_interval_seconds.setText(String.valueOf(interval));
        et_interval_seconds.setSelection(et_interval_seconds.getText().toString().length());

        if (AlarmBySetInexactRepeatingReceiver.isAlreadyEvoked())
        {
            Toast.makeText(this, "setInexactRepeating已经启动，若要重启请先点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmBySetInexactRepeatingReceiver.start(getApplicationContext(), interval * 1000);
        Toast.makeText(this, "启动setInexactRepeating", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "启动setInexactRepeating");
    }

    private void stopSetInexactRepeating()
    {
        if (!AlarmBySetInexactRepeatingReceiver.isAlreadyEvoked())
        {
            Toast.makeText(this, "setetInexactRepeating已经停止，不要重复点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmBySetInexactRepeatingReceiver.stop(getApplicationContext());
        Toast.makeText(this, "停止setInexactRepeating", Toast.LENGTH_SHORT).show();
    }

    private void startSetExact()
    {
        int interval = Integer.parseInt(et_interval_seconds.getText().toString().trim());
        if (interval <= 0)
        {
            Toast.makeText(this, "闹钟Alarm间隔时长必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        et_interval_seconds.setText(String.valueOf(interval));
        et_interval_seconds.setSelection(et_interval_seconds.getText().toString().length());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
        {
            Toast.makeText(this, "setExact need SDK_INT not less than " + Build.VERSION_CODES.KITKAT
                    + ", current SDK_INT: " + Build.VERSION.SDK_INT, Toast.LENGTH_SHORT).show();
            return;
        }

        if (AlarmBySetExactReceiver.isAlreadyEvoked())
        {
            Toast.makeText(this, "setSetExact已经启动，若要重启请先点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmBySetExactReceiver.start(getApplicationContext(), interval * 1000);
        Toast.makeText(this, "启动setSetExact", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "启动setSetExact");
    }

    private void stopSetExact()
    {
        if (!AlarmBySetExactReceiver.isAlreadyEvoked())
        {
            Toast.makeText(this, "SetExact已经停止，不要重复点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmBySetExactReceiver.stop(getApplicationContext());
        Toast.makeText(this, "停止setSetExact", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "停止setSetExact");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private synchronized void startJobSchedler()
    {
        int interval = Integer.parseInt(et_interval_seconds.getText().toString().trim());
        if (interval <= 0)
        {
            Toast.makeText(this, "闹钟Alarm间隔时长必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        et_interval_seconds.setText(String.valueOf(interval));
        et_interval_seconds.setSelection(et_interval_seconds.getText().toString().length());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            Toast.makeText(this, "JobScheduler need SDK_INT not less than " + Build.VERSION_CODES.LOLLIPOP
                    + ", current SDK_INT: " + Build.VERSION.SDK_INT, Toast.LENGTH_SHORT).show();
            return;
        }

        if (null != jobScheduler)
        {
            Toast.makeText(this, "jobScheduler已经启动，若要重启请先点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        JobInfo.Builder jobBuilder = new JobInfo.Builder(JOB_TASK_ID, new ComponentName( getPackageName(), MyJobService.class.getName()));
        jobBuilder.setPeriodic(interval * 1000);

        MyJobService.resetReq();
        JobScheduler jobScheduler = (JobScheduler) getSystemService( Context.JOB_SCHEDULER_SERVICE );
        this.jobScheduler = jobScheduler; // 使用局部变量解决迸发时停止会将成员亦是置null
        int errorCode = jobScheduler.schedule(jobBuilder.build());
        if(errorCode <= 0 )
        {
            Toast.makeText(this, "启动jobScheduler失败(errorCode=" + errorCode + ")", Toast.LENGTH_SHORT).show();
            LogUtil.e(tag, "jobScheduler schedule failed, errorCode=" + errorCode);
            return;
        }

        Toast.makeText(this, "启动jobScheduler", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "启动jobScheduler");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private synchronized void stopJobScheduler()
    {
        if (null == jobScheduler)
        {
            Toast.makeText(this, "jobScheduler已经停止，不要重复点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        jobScheduler.cancel(JOB_TASK_ID);
        jobScheduler = null;

        Toast.makeText(this, "停止jobScheduler", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "停止jobScheduler");
    }

    private void startSetAndAllowWhileIdle()
    {
        int interval = Integer.parseInt(et_interval_seconds.getText().toString().trim());
        if (interval <= 0)
        {
            Toast.makeText(this, "闹钟Alarm间隔时长必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        et_interval_seconds.setText(String.valueOf(interval));
        et_interval_seconds.setSelection(et_interval_seconds.getText().toString().length());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            Toast.makeText(this, "setAndAllowWhileIdle need SDK_INT not less than " + Build.VERSION_CODES.M
                    + ", current SDK_INT: " + Build.VERSION.SDK_INT, Toast.LENGTH_SHORT).show();
            return;
        }

        if (AlarmBySetAndAllowWhileIdleReceiver.isAlreadyEvoked())
        {
            Toast.makeText(this, "setAndAllowWhileIdle已经启动，若要重启请先点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmBySetAndAllowWhileIdleReceiver.start(getApplicationContext(), interval * 1000);
        Toast.makeText(this, "启动setAndAllowWhileIdle", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "启动setAndAllowWhileIdle");
    }

    private void stopSetAndAllowWhileIdle()
    {
        if (!AlarmBySetAndAllowWhileIdleReceiver.isAlreadyEvoked())
        {
            Toast.makeText(this, "setAndAllowWhileIdle已经停止，不要重复点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmBySetAndAllowWhileIdleReceiver.stop(getApplicationContext());
        Toast.makeText(this, "停止setAndAllowWhileIdle", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "停止setAndAllowWhileIdle");
    }

    private void startSetExactAndAllowWhileIdle()
    {
        int interval = Integer.parseInt(et_interval_seconds.getText().toString().trim());
        if (interval <= 0)
        {
            Toast.makeText(this, "闹钟Alarm间隔时长必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        et_interval_seconds.setText(String.valueOf(interval));
        et_interval_seconds.setSelection(et_interval_seconds.getText().toString().length());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            Toast.makeText(this, "setExactAndAllowWhileIdle need SDK_INT not less than " + Build.VERSION_CODES.M
                    + ", current SDK_INT: " + Build.VERSION.SDK_INT, Toast.LENGTH_SHORT).show();
            return;
        }

        if (AlarmBySetExactAndAllowWhileIdleReceiver.isAlreadyEvoked())
        {
            Toast.makeText(this, "setExactAndAllowWhileIdle已经启动，若要重启请先点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmBySetExactAndAllowWhileIdleReceiver.start(getApplicationContext(), interval * 1000);
        Toast.makeText(this, "启动setExactAndAllowWhileIdle", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "启动setExactAndAllowWhileIdle");
    }

    private void stopSetExactAndAllowWhileIdle()
    {
        if (!AlarmBySetExactAndAllowWhileIdleReceiver.isAlreadyEvoked())
        {
            Toast.makeText(this, "setExactAndAllowWhileIdle已经停止，不要重复点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmBySetExactAndAllowWhileIdleReceiver.stop(getApplicationContext());
        Toast.makeText(this, "停止setExactAndAllowWhileIdle", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "停止setExactAndAllowWhileIdle");
    }

    private void startTimer()
    {
        int interval = Integer.parseInt(et_interval_seconds.getText().toString().trim());
        if (interval <= 0)
        {
            Toast.makeText(this, "闹钟Alarm间隔时长必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        et_interval_seconds.setText(String.valueOf(interval));
        et_interval_seconds.setSelection(et_interval_seconds.getText().toString().length());

        if (null != myTimerTask && !myTimerTask.isCanceled)
        {
            Toast.makeText(this, "timer已经启动，若要重启请先点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        myTimerTask = new MyTimerTask(interval * 1000, false, 0);
        Timer timer = new Timer();
        timer.schedule(myTimerTask, interval * 1000);
        Toast.makeText(this, "启动timer", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "启动timer");
    }

    private void stopTimer()
    {
        if (null == myTimerTask || myTimerTask.isCanceled)
        {
            Toast.makeText(this, "timer已经停止，不要重复点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        myTimerTask.cancelTimerTask();
        Toast.makeText(this, "停止timer", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "停止timer");
    }

    private synchronized void startThread()
    {
        int interval = Integer.parseInt(et_interval_seconds.getText().toString().trim());
        if (interval <= 0)
        {
            Toast.makeText(this, "闹钟Alarm间隔时长必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        et_interval_seconds.setText(String.valueOf(interval));
        et_interval_seconds.setSelection(et_interval_seconds.getText().toString().length());

        if (null != myThread && !myThread.isCanceled)
        {
            Toast.makeText(this, "thread已经启动，若要重启请先点击停止", Toast.LENGTH_SHORT).show();
            return;
        }

        myThread = new MyThread(interval * 1000);
        myThread.start();
        Toast.makeText(this, "启动thread", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "启动thread");
    }

    private synchronized void stopThread()
    {
        if (null == myThread || myThread.isCanceled)
        {
            Toast.makeText(this, "thread已经停止，不要重复点击停止\"", Toast.LENGTH_SHORT).show();
            return;
        }

        myThread.cancelThread();
        Toast.makeText(this, "停止tread", Toast.LENGTH_SHORT).show();
        LogUtil.i(tag, "停止tread");
    }

    private class MyThread extends Thread
    {
        private final String tag = getClass().getSimpleName();

        private long intervalMillis;
        private boolean isCanceled;

        private int seq = 0;

        public MyThread(long intervalMillis)
        {
            this.intervalMillis = intervalMillis;
        }

        public void cancelThread()
        {
            isCanceled = true;
        }

        public boolean isCanceled()
        {
            return isCanceled;
        }

        @Override
        public void run()
        {
            while (true)
            {
                if (isCanceled)
                {
                    break;
                }

                try
                {
                    Thread.sleep(intervalMillis);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                LogUtil.i(tag, "thread run seq=" + (++seq));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    LogUtil.i(tag, "isIgnoringBatteryOptimizations="
                            + DozeUtil.isIgnoringBatteryOptimizations(getApplicationContext())
                            + ", isDeviceIdleMode=" + DozeUtil.isDeviceIdleMode(getApplicationContext()));
                }
            }
        }
    }

    private class MyTimerTask extends TimerTask
    {
        private final String tag = getClass().getSimpleName();

        private long intervalMillis;
        private boolean isCanceled;

        private int seq = 0;

        public void cancelTimerTask()
        {
            isCanceled = true;
        }

        public MyTimerTask(long intervalMillis, boolean isCanceled, int seq)
        {
            this.intervalMillis = intervalMillis;
            this.isCanceled = isCanceled;
            this.seq = seq;
        }

        @Override
        public void run()
        {
            if (isCanceled)
            {
                return;
            }

            // 为解决线程并发总是，增加isCanceled字段为构造函数参数，
            // 刚判断了isCanceled为false，但执行到此cancelTimerTask()又将isCanceled置为true
            myTimerTask = new MyTimerTask(intervalMillis, isCanceled, ++seq);
            Timer timer = new Timer();
            timer.schedule(myTimerTask, intervalMillis);

            LogUtil.i(tag, "timer run seq=" + (seq));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                LogUtil.i(tag, "isIgnoringBatteryOptimizations="
                        + DozeUtil.isIgnoringBatteryOptimizations(getApplicationContext())
                        + ", isDeviceIdleMode=" + DozeUtil.isDeviceIdleMode(getApplicationContext()));
            }
        }
    }
}
