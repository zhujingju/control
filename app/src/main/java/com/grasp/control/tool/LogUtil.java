package com.grasp.control.tool;

import android.util.Log;

public class LogUtil {

	public static final int VERBOSE=1;
	public static final int DEBUG=2;
	public static final int INFO=3;
	public static final int WARN=4;
	public static final int ERROR=5;
	public static final int NOTHING=6;
	
	public static final int LEVEL=VERBOSE;  //LEVEL>6不打印
	
	public static void v(String tag,String msg ){
		if(LEVEL<=VERBOSE){
			Log.v(tag, msg);
		}
	} 
	
	public static void d(String tag,String msg ){
		if(LEVEL<=VERBOSE){
			Log.d(tag, msg);
		}
	} 
	public static void i(String tag,String msg ){
		if(LEVEL<=VERBOSE){
			Log.i(tag, msg);
		}
	} 
	
	public static void w(String tag,String msg ){
		if(LEVEL<=VERBOSE){
			Log.w(tag, msg);
		}
	} 
	
	public static void e(String tag,String msg ){
		if(LEVEL<=VERBOSE){
			Log.e(tag, msg);
		}
	} 
	
	
	
}
