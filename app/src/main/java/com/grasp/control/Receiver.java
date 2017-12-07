package com.grasp.control;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.grasp.control.tool.BaseActivity;

/**
 * Created by zhujingju on 2017/9/18.
 */

public class Receiver extends BaseActivity {



    @Override
    public int setLayoutId() {
        return R.layout.receiver;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        Log.d("EZAlarmInfo","Receiver");
        MainActivity.notificationListener.getNotFra();
        finish();
    }

    @Override
    public void initObject() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void init() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("qqq","onWindowFocusChanged");
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
    }

}
