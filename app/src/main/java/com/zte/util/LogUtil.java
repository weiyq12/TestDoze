package com.zte.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/***
 * 自定义日志输出工具类
 * 2016.11.6
 * @author weiyiqing
 */
public class LogUtil
{
    private static LogUtil instance = new LogUtil();

    private String tag = getClass().getSimpleName();

    @SuppressLint("SdCardPath")
    private String logDirPath = "/sdcard/";
    private String logFileName = "test.log";
    private String pid = "";

    private static final int LOG_SIZE_LIMIT = 10 * 1024 * 1024; // 日志大小限制10兆
    private static final String LINE_SEPERATOR = System.getProperty("line.separator");
    private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.CANADA);

    private File logFile = new File(logDirPath + logFileName);
    private FileOutputStream fos;

    private LogUtil()
    {
    }

    private static LogUtil getInstance()
    {
        return instance;
    }

    /**
     * 初始化日志
     * @param filePath 日志文件目录
     * @param fileName 日志文件名称
     * @param pid 应用进程号
     */
    public static void init(String filePath, String fileName, String pid)
    {
        getInstance().initLog(filePath, fileName, pid);
    }

    public static void v(String tag, String content)
    {
        Log.v(tag, content);
        getInstance().log("V", tag, content);
    }

    public static void d(String tag, String content)
    {
        Log.d(tag, content);
        getInstance().log("D", tag, content);
    }

    public static void i(String tag, String content)
    {
        Log.i(tag, content);
        getInstance().log("I", tag, content);
    }

    public static void w(String tag, String content)
    {
        Log.w(tag, content);
        getInstance().log("W", tag, content);
    }

    public static void e(String tag, String content)
    {
        Log.e(tag, content);
        getInstance().log("E", tag, content);
    }

    private synchronized void log(String level, String tag, String content)
    {
        try
        {
            String record = sdf.format(new Date()) + " "
                    + level + "/" + tag + "(" + pid + "): "
                    + content + LINE_SEPERATOR;
            if (null == fos || null == logFile || !logFile.isFile())
            {
                Log.e(tag, "fos=" + fos
                        + ", logFile=" + logFileName
                        + ", isFile=" + (null != logFile && logFile.isFile()));
                init(null, null, null);
            }

            if (null != fos)
            {
                fos.write(record.getBytes());
            }
        }
        catch (IOException e)
        {
            Log.e(tag, "write log occurred exception: " + e.getMessage() + ", reset fos to null");
            fos = null;
            e.printStackTrace();
        }
    }

    private synchronized  void initLog(String filePath, String fileName, String pid)
    {
        Log.i(tag, "initLog filePath=" + filePath + ", fileName=" + fileName + ", pid=" + pid);

        if (null == filePath || "".equals(filePath))
        {
            Log.w(tag, "filePath is empty, use default: " + logDirPath);
            filePath = logDirPath;
        }

        if (null == fileName || "".equals(fileName))
        {
            Log.w(tag, "fileName is empty, use default: " + logFileName);
            fileName = logFileName;
        }

        filePath = filePath.replace("\\", "/");
        if (!filePath.endsWith("/"))
        {
            filePath += "/";
        }

        if (!fileName.endsWith(".log"))
        {
            fileName += ".log";
        }

        if (null != pid)
        {
            this.pid = pid;
        }
        if (null == logFile)
        {
            logFile = new File(filePath + fileName);
        }

        if (filePath.equals(logDirPath)
                && fileName.equals(logFileName)
                && null != fos
                && logFile.isFile()
                && logFile.length() < LOG_SIZE_LIMIT)
        {
            Log.i(tag, "log init already ok, so return.");
            return;
        }

        logDirPath = filePath;
        logFileName = fileName;
        logFile = new File(filePath + fileName);

        boolean isdirOk = true;
        File dirFile = new File(filePath);
        if (!dirFile.isDirectory())
        {
            isdirOk = dirFile.mkdirs();
        }
        Log.i(tag, "filePath=" + filePath + ", isdirOk=" + isdirOk);
        if (!isdirOk)
        {
            return;
        }

        String fileFullPath = filePath + fileName;
        boolean isFileOk = true;
        File logFile = new File(fileFullPath);
        if (logFile.isFile())
        {
            if (logFile.length() >= LOG_SIZE_LIMIT)
            {
                String bakFilePath = fileFullPath + ".bak";
                File bakFile = new File(bakFilePath);
                boolean isDelBackLogOk = true;
                if (bakFile.exists())
                {
                    isDelBackLogOk = bakFile.delete();
                }
                Log.i(tag, "bakFilePath=" + bakFilePath + ", isDelBackLogOk=" + isDelBackLogOk);

                boolean isRenameOk = logFile.renameTo(bakFile);
                Log.i(tag, "rename log " + fileFullPath + " to " + bakFilePath + ", isRenameOk=" + isRenameOk);

                try
                {
                    isFileOk = logFile.createNewFile();
                }
                catch (IOException e)
                {
                    isFileOk = false;
                    e.printStackTrace();
                }
            }
        }
        else
        {
            try
            {
                isFileOk = logFile.createNewFile();
            }
            catch (IOException e)
            {
                isFileOk = false;
                e.printStackTrace();
            }
        }
        Log.i(tag, "fileFullPath=" + fileFullPath + ", isFileOk=" + isFileOk);
        if (!isFileOk)
        {
            return;
        }

        if (null != fos)
        {
            try
            {
                fos.close();
            }
            catch (IOException e)
            {
                Log.e(tag, "close old fos=" + fos + " occurred exception: " + e.getMessage());
                e.printStackTrace();
            }
        }

        try
        {
            fos = new FileOutputStream(fileFullPath, true);
            Log.e(tag, "create new fos=" + fos);
        }
        catch (FileNotFoundException e)
        {
            Log.e(tag, "create new fos occurred exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
