package com.zte.testdoze.service;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.zte.util.LogUtil;

/**
 * Created by Torick on 16/11/13.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService
{
    private String tag = getClass().getSimpleName();

    private static final int MSG_JOG_ARRIVE = 1001;

    private static int staticSeq = 0;

    private int seq = 0;

    private Handler myJobHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage( Message msg )
        {
            // service是运行在主线程，能Toast出来
            // Toast.makeText( getApplicationContext(), "handleMessage seq=" + seq
            // + ", MyJobService task is running", Toast.LENGTH_SHORT ).show();

            /*
            try
            {
                // 耗时操作应该启动子线程处理，在此虽不会报ANR，但日志会显示如下提示信息
                // I/Choreographer: Skipped 1200 frames!  The application may be doing too much work on its main thread.
                Thread.sleep(20 * 1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            */

            jobFinished( (JobParameters) msg.obj, false );
            LogUtil.i(tag, "jobFinished seq=" + seq);

            return true;
        }
    } );

    @Override
    public void onCreate()
    {
        super.onCreate();

        // 运行在主线程，此处能够Toast出来
        // Toast.makeText(getApplicationContext(), "MyJobService onCreate", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters)
    {
        // service是运行在主线程，此处能够Toast出来
        // Toast.makeText(getApplicationContext(), "onStartJob", Toast.LENGTH_SHORT).show();

        seq = ++staticSeq;

        LogUtil.i(tag, "onStartJob seq=" + (seq) + ", jobParameters=" + jobParameters);

        myJobHandler.sendMessage( Message.obtain(myJobHandler, MSG_JOG_ARRIVE, jobParameters));

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters)
    {
        LogUtil.i(tag, "onStopJob seq=" + seq + ", jobParameters=" + jobParameters);

        myJobHandler.removeMessages(MSG_JOG_ARRIVE);

        return false;
    }

    public static void resetReq()
    {
        staticSeq = 0;
    }
}
