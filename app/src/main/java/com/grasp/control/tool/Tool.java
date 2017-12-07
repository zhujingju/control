package com.grasp.control.tool;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Tool {

	
	public static boolean isLianWang(final Context context) { // 判断是否联网 还要权限
		try {
			ConnectivityManager manger = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络通讯类
			return (manger.getActiveNetworkInfo().isAvailable());
		} catch (Exception e) {
			return false;
		}
	}

	public static void setWindowStatusBarColor(Activity activity, int colorResId) {
		try {


			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Window window = activity.getWindow();
				//取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				//黑色字
//				window.getDecorView().setSystemUiVisibility(
//						View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


			window.setStatusBarColor(activity.getResources().getColor(colorResId));
				//底部导航栏
				//window.setNavigationBarColor(activity.getResources().getColor(colorResId));
			}

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean hasSDCard() {   //判断有无sd卡
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}


	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}
}
