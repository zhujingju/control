package com.map.View;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/9-009.
 */

public class LogCustom {
    private static String TAG = "Print-LogCustom";
    private static String separator = "\n"; // +++++++++++++++++++++++++++++++++++++++++
//    private static DateFormat dataFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL); //2017年11月9日星期四 中国标准时间 下午4:01:29
//    private static SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //  2012-10-03 23:41:31
    /** 每个日志文件最大10M */
    private static long singleFileMaxLength = 100*1024; // 10M: 10*1024*1024; 5KB: 5*1024 //每个文件10M=10M*1024KB*1024B=10485760；1G * 1024M * 1024K * 1024B = 1073741824;
    /** 所有日志文件个数最多10个 */
    private static int filesMaxGeShu = 10;

//    public static final String Verbose = "Verbose";
//    public static final String Debug = "Debug";
//    public static final String Info = "Info";
//    public static final String Warn = "Warn";
//    public static final String Error = "Error";
//
//    private static void v(String TAG, String message){
//        if(message == null) return;
//        Log.v(TAG, message);
////        writeLog(TAG, message, Verbose);
//    }
//    private static void d(String TAG, String message){
//        if(message == null) return;
//        Log.d(TAG, message);
////        writeLog(TAG, message, Debug);
//    }
//    private static void i(String TAG, String message){
//        if(message == null) return;
//        Log.i(TAG, message);
////        writeLog(TAG, message, Info);
//    }
//    private static void w(String TAG, String message){
//        if(message == null) return;
//        Log.w(TAG, message);
////        writeLog(TAG, message, Warn);
//    }
//    private static void e(String TAG, String message){
//        if(message == null) return;
//        Log.e(TAG, message);
////        writeLog(TAG, message, Error);
//    }

     /** 把日志记录到手机本地txt文件里。Environment.getExternalStorageDirectory().getAbsolutePath()/resource/log_zjj/log_日期时分秒.txt */
    protected static void writeLog(String TAG, String message, String type) {
        File fileWrite = getFileToWrite();
        message = "\n" + separator + "\n"+TAG+"\t" +  getDate() + "\t" + type + "\n" + message;


        // 输出日志到txt文件里
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileWrite, true); // 频繁开通管道可能出错
            fos.write(message.getBytes(ConstantsForMap.CharsetName));//, (int)fileWrite.length(), message.length()); // 可能会出错
            fos.flush();
//            mHandler.obtainMessage(What_Message, "\n"+"保存位置成功").sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(fos != null) fos.close();
                fos = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** 输出到哪个目录下哪个文件，最多写10个文件，每个100kb */
    public static File getFileToWrite() {

        // 判断文件夹存在
        File file = new File(ConstantsForMap.Zjj_file);
        if(!file.exists()) file.mkdirs();
        file = new File(ConstantsForMap.log_file);
        if(!file.exists()) file.mkdirs();
        // 文件超过10个，删掉最旧的
        if(file.isDirectory() && file.listFiles().length > filesMaxGeShu){
            File[] files = file.listFiles();
            File fileAbort = files[0];
            for(int i = 1; i < files.length; i++) {
                if(fileAbort.lastModified() > files[i].lastModified()) fileAbort = files[i];
            }
            boolean isDeleted = fileAbort.delete();
            Log.i(TAG, "writeLog: 删除日志文件：isDeleted="+isDeleted+",文件名:"+fileAbort.getPath());
        }
        // 找出最新的文件，
        File fileWrite = null;
        if(file.isDirectory() && file.listFiles().length > 0){
            File[] files = file.listFiles();
            fileWrite = files[0];
            for(int i = 1; i < files.length; i++) {
                if(fileWrite.lastModified() < files[i].lastModified()) fileWrite = files[i];
            }

//            Log.i(TAG, "writeLog: 最新日志文件："+fileWrite.getPath());
        }

        String date = getDate(); // dataFormat.format(new Date()); //
        // 如果没有文件，或者最新的文件大于10M，则重新建一个文件：log_日期.txt
        if(fileWrite == null || fileWrite.length() > singleFileMaxLength) {
            if(fileWrite != null){
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(fileWrite);
                    int size = fis.available();
                    Log.i(TAG, "writeLog: 最新文件超过5kb？ fileWrite.length()="+fileWrite.length()+", size="+fis.available()+",fileName="+fileWrite.getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        if(fis != null) fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 添加日期+时分秒，日志级别
            Log.i(TAG, "writeLog: dataFormat="+date);
            fileWrite = new File(ConstantsForMap.log_file + ConstantsForMap.log_fileNamePrefix + date.replace(":", "_")+ConstantsForMap.log_fileSuffix);
        }
        return fileWrite;
    }

    public static String getDate() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = format1.format(new Date(System.currentTimeMillis()));
        return date1;// 2012-10-03 23:41:31
    }

    /** 打印  exception.printStackTrace();
     * 把异常记录到手机本地txt文件里。Environment.getExternalStorageDirectory().getAbsolutePath()/resource/log_zjj/log_日期时分秒.txt */
//    public static void writeException(String TAGParam, Exception exception, String type) {
//        exception.printStackTrace();
//
//        StackTraceElement [] messages=exception.getStackTrace();
//        int length=messages.length;
//        StringBuilder sb = new StringBuilder();
////        Log.i(TAG, "printException: +++++++++++++++++++++++++++++++++++");
////        System.out.println(exception.getLocalizedMessage());
////        System.out.println(exception.getMessage());
////        System.out.println(exception.toString());
//        sb.append(exception.toString());
//        for(int i=0;i<length;i++) {
////            System.out.println("ClassName:" + messages[i].getClassName());
////            System.out.println("getFileName:" + messages[i].getFileName());
////            System.out.println("getLineNumber:" + messages[i].getLineNumber());
////            System.out.println("getMethodName:" + messages[i].getMethodName());
//            System.out.println("toString:" + messages[i].toString());
//            sb.append("\n"+messages[i].toString());
//        }
//        sb.append("\n");
////        Log.i(TAG, "printException: +++++++++++++++++++++++++++++++++++");
//        writeLog(TAGParam, sb.toString(), type);
//    }
    /** 打印  exception.printStackTrace();
     * 把异常记录到手机本地txt文件里。Environment.getExternalStorageDirectory().getAbsolutePath()/resource/log_zjj/log_日期时分秒.txt */
//    public static void writeException(String TAGParam, Throwable throwable, String type) {
//
//        Throwable ex = throwable.getCause() == null ? throwable : throwable.getCause();
//        StackTraceElement[] stacks = ex.getStackTrace();
//
//        Writer writer = new StringWriter();
//        PrintWriter printWriter = new PrintWriter(writer);
//        ex.printStackTrace(printWriter);
//
//        StackTraceElement [] messages=ex.getStackTrace();
//        int length=messages.length;
//        StringBuilder sb = new StringBuilder();
////        Log.i(TAG, "printException: +++++++++++++++++++++++++++++++++++");
////        System.out.println(exception.getLocalizedMessage());
////        System.out.println(exception.getMessage());
////        System.out.println(exception.toString());
//        sb.append("可能没有内容需要打印："+throwable.toString());
//        for(int i=0;i<length;i++) {
////            System.out.println("ClassName:" + messages[i].getClassName());
////            System.out.println("getFileName:" + messages[i].getFileName());
////            System.out.println("getLineNumber:" + messages[i].getLineNumber());
////            System.out.println("getMethodName:" + messages[i].getMethodName());
//            System.out.println("toString:" + messages[i].toString());
//            sb.append("\n"+messages[i].toString());
//        }
//        sb.append("\n");
////        Log.i(TAG, "printException: +++++++++++++++++++++++++++++++++++");
//        writeLog(TAGParam, sb.toString(), type);
//        writeLog(TAGParam, sb.toString(), type);
//    }


}
