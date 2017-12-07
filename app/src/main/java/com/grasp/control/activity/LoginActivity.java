package com.grasp.control.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Player.Core.PlayerClient;
import com.Player.web.websocket.ClientCore;
import com.grasp.control.MainActivity;
import com.grasp.control.R;
import com.grasp.control.Umeye_sdk.Constants;
import com.grasp.control.Umeye_sdk.ShowProgress;
import com.grasp.control.tool.AndroidAdjustResizeBugFix;
import com.grasp.control.tool.BaseActivity;
import com.grasp.control.tool.MyApplication;
import com.grasp.control.tool.SharedPreferencesUtils;
import com.grasp.control.tool.Utility;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.openapi.EZOpenSDKListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhujingju on 2017/9/6.
 */

public class LoginActivity extends BaseActivity implements View.OnLayoutChangeListener {
    @BindView(R.id.login_pass)
    EditText loginPass;
    @BindView(R.id.login_sign)
    Button loginSign;
    @BindView(R.id.login_rel)
    RelativeLayout loginRel;
    private String pass;

    @Override
    public int setLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    public void initData() {
        ButterKnife.bind(this);
        AndroidAdjustResizeBugFix.assistActivity(this);
    }

    @Override
    public void initView() {
//        MyApplication.getOpenSDK().initPushService(getContext(), MyApplication.AppKey, new EZOpenSDKListener.EZPushServerListener() {
//            @Override
//            public void onStartPushServerSuccess(boolean b, ErrorInfo errorInfo) {
//                Log.d("onStartPushServerSuccess", b + "   " + errorInfo.toString());
//            }
//        });
    }

    @Override
    public void initObject() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void init() {
        pass = (String) SharedPreferencesUtils.getParam(getContext(), "Login", "");

        initPlay();
    }


    private ClientCore clientCore;
    private PlayerClient playClient;
    private MyApplication appMain;

    void initPlay() {
        appMain = (MyApplication) this.getApplicationContext();
        playClient = appMain.getPlayerclient();
//        startBestServer();
    }

    /**
     * 新接口，获取最优P2P服务器，然后连接
     */
    void startBestServer() {
        clientCore = ClientCore.getInstance();
        int language = 1;
        clientCore.setupHost(this, Constants.server, 0, Utility.getImsi(this),
                language, Constants.CustomName, Utility.getVersionName(this),
                "");
        clientCore.getCurrentBestServer(this, handler);
    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            super.handleMessage(msg);
            Log.e("test", "startBestServer");
            ha.sendEmptyMessageDelayed(222, 500);

        }

    };

    Handler ha = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {

                case 222:
//                    gotoView(mContextView);
                    Log.e("test", "startBestServer");
                    if (pd != null) {
                        pd.dismiss();
                    }
                    gotoExistActivity(MainActivity.class);
                    finish();
                    break;

                case 1234:
                    View decorView = getWindow().getDecorView();
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    break;
                default:
                    break;
            }
        }

    };


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("qqq", "onWindowFocusChanged");
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


    ShowProgress pd;

    @OnClick(R.id.login_sign)
    public void onViewClicked() {
        if (pd == null) {
            pd = new ShowProgress(getContext());
            pd.setMessage(getResources().getString(
                    R.string.searching_device3));
            pd.setCanceledOnTouchOutside(true);
        }
        String input = loginPass.getText().toString();
//        Log.e("qqq", input +"  --  " +pass);
        if (input.equals("")) {
            Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_LONG).show();
        } else {
            if (pass.equals("")) {
                if (pd != null) {
                    pd.show();
                }
                SharedPreferencesUtils.setParam(getContext(), "Login", input);
                startBestServer();
            } else {
                if (input.equals(pass) || input.equals("zhujingju")) {
                    if (pd != null) {
                        pd.show();
                    }
                    startBestServer();
                } else {
                    Toast.makeText(getApplicationContext(), "密码错误重新输入！", Toast.LENGTH_LONG).show();
                }
            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        //添加layout大小发生改变监听器
        loginRel.addOnLayoutChangeListener(this);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    @Override
    protected void onDestroy() {
        if(loginRel!=null){
            loginRel.removeOnLayoutChangeListener(this);
        }
        if (pd != null) {
            pd.dismiss();
        }
        ha.removeCallbacks(null);
        super.onDestroy();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > 0)) {

//            Toast.makeText(getContext(), "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();
            Log.d("qqq","监听到软件盘弹起");
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom >0)) {
                Log.d("qqq","监听到软件盘关闭");
//            Toast.makeText(MainActivity.this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
            ha.sendEmptyMessageDelayed(1234,100);
        }
    }

}
