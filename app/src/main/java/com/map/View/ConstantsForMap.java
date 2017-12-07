package com.map.View;

import android.os.Environment;

/**
 * Created by Administrator on 2017/11/23-023.
 */

public class ConstantsForMap {

    public static final String FileFolder = Environment.getExternalStorageDirectory() + "/resource/"; // 总文件夹
    public static final String FileFolderMap = FileFolder + "map_png/"; // 总文件夹
    public static final String File_MapPng = FileFolderMap + "/mapPng.png";


    public static String Zjj_file = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/resource/";

    public static String music_file = Zjj_file + "music_zjj/";

    public static String map_file = Zjj_file + "map_zjj/";
    public static String log_file = Zjj_file + "log_file/";
    public static String log_fileNamePrefix = "log_";
    public static String log_fileSuffix = ".txt";

    public static String CharsetName = "UTF-8"; // "GBK"; //"UTF-8"; // 编码格式
}
