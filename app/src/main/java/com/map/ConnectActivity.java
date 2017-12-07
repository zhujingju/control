package com.map;

import android.app.Activity;

/**
 * 连接wifi和ros
 * Created by Administrator on 2017/3/29-029.
 */
public class ConnectActivity extends Activity {
//    private final String TAG = "Print-ConnActi";
//
//    private TextView id_tvLoading;
//20005    6shang
//    private ImageView id_progressBg;
//    private ProgressBar id_progress;
//    private ImageView id_progressTop;
//
//    private TextView id_tvTips;
//
//    private String dWifiname;   //当前连接wifi名
//    /**
//     * 保存原来的wifi，断开后继续连接，以后不可改变。必须在第一次连接之前就获取，之后不可再获取。
//     */
//    public static String mWifiOldName;
//    public static int mWifiOldNetId;
//    /** wifi start */
//    private static  String mWifiName = ""; //"Xiaomi_iotbroad"; //"zw2017060803"; // "app"; // "PKUCY"; // "app";
//    private static  String mWifiPwd = ""; //"broad608"; // "88888888"; //"bbroad608";
//    private SharedPreferences sp;
//
//    private ProgressDialogWrapper progressDialog;
//    private boolean isConnected = false;
//    private int inetAddress;
//    /** 连接wifi和主机 */
//    private ConnectMaster connectMaster = null;
//
//
//    private final int handler_error_exit = 0x200;
//    private final int handler_setTipsText = 0x202;
//    /** 手动填写wifi */
//    private final int handler_setWifi = 0x204;
//
//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case handler_error_exit:
//                    String errorMsg = (String)msg.obj;
//                    if (errorMsg != null && !errorMsg.equals("")) {
//                        id_tvTips.setText(errorMsg);
//                    }
//                    exit();
//                        break;
//
//                case handler_setTipsText:
//                    String msgStr = (String)msg.obj;
//                    if (msgStr != null && !msgStr.equals("")) {
//                        id_tvTips.setText(msgStr);
//                    }
//                    break;
//
//                case handler_setWifi:
//                    connWifiRos();
////                    initGPS();
//                    break;
//
//                default:
//                    break;
//            }
//        }
//    };
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) { // 1
//        super.onCreate(savedInstanceState);
//        Log.i(TAG, "onCreate()");
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.connect_wifi_ros);
//        sp= PreferenceManager.getDefaultSharedPreferences(ConnectActivity.this);
//        wifi= ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE));
////        mWifiName=sp.getString(DemoApplication.UID, "");
////        mWifiPwd=sp.getString(mWifiName, "");
//
//
//        isConnected = false;
//        initUI();
//        resetUILocation();
//
//        // 先保存原来的wifi，之前可以不曾连接，则wifi是null或""
//        setOldWifi();
//        // 弹对话框，可手动输入wifi和密码，也可“取消”用默认的登录
//        showDialogSetWifi();
//        // 用默认的账号密码登录
////        connWifiRos();
//        initGPS();
//        Log.i(TAG, "onCreate: id_progressBg.toString()=" + id_progressBg.toString() + ", id_progressTop.toString()=" + id_progressTop.toString());
//        //
//    }
//
//    @Override
//    protected void onStart() { // 2
//        super.onStart();
//    }
//
//    @Override
//    protected void onResume() { // 3
//        super.onResume();
//
//    }
//
//    /**
//     * 初始化ui界面
//     */
//    private void initUI() {
//        id_tvLoading = (TextView) findViewById(R.id.id_tvLoading);
////        ProgressBar id_progress = (ProgressBar) findViewById(R.id.id_progress);
//        id_progressBg = (ImageView) findViewById(R.id.id_progressBg);
////        id_progressMove_1 = (ImageView) findViewById(R.id.id_progressMove_1);
////        id_progressMove_2 = (ImageView) findViewById(R.id.id_progressMove_2);
//        id_progress = (ProgressBar) findViewById(R.id.id_progress);
//        id_progressTop = (ImageView) findViewById(R.id.id_progressTop);
//        id_tvTips = (TextView) findViewById(R.id.id_tvTips);
//
////        id_progress.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.progress_conn_progressdrawable));
//
//
//        // 判断sdk
//        if (android.os.Build.VERSION.SDK_INT > 22) {
//            Log.i(TAG, "initUI: 换图片资源 android.os.Build.VERSION.SDK_INT=" + android.os.Build.VERSION.SDK_INT); // 红米note3：android.os.Build.VERSION.SDK_INT=23
//            final Drawable drawable = getApplicationContext().getResources().getDrawable(R.drawable.progress_conn_indeterminate_api22);
//            id_progress.setIndeterminateDrawable(drawable);
//        }
//    }
//
//
//    /** 适配UI控件位置 */
//    private void resetUILocation(){
//        Log.i(TAG, "resetUILocation()");
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int mScreenWidthPx = dm.widthPixels;
//        int mScreenHeightPx = dm.heightPixels;
//        Log.i(TAG, "resetUILocation: mScreenWidthPx=" + mScreenWidthPx + ", mScreenHeightPx=" + mScreenHeightPx);
//
//        /** 模板宽高，横屏，原始尺寸 1280*720, 1280/720=1.777777777777778,  2048/1536=1.333333333333333 */
//        int mModuleWidth = 1280;
//        int mModuleHeight = 720;
//        // width / 1280 = x / screenWidth, x = width * screenWidth / 1280;
//
//        float textSize = 0;
//        AbsoluteLayout.LayoutParams lpTvLoading = (AbsoluteLayout.LayoutParams)id_tvLoading.getLayoutParams();
//
//        lpTvLoading.x = 0;
//        lpTvLoading.y = (int) (getResources().getDimension(R.dimen.d_y_progLoading) / mModuleHeight * mScreenHeightPx);
//        lpTvLoading.width = AbsoluteLayout.LayoutParams.MATCH_PARENT;
//        lpTvLoading.height = AbsoluteLayout.LayoutParams.WRAP_CONTENT;
//        id_tvLoading.setLayoutParams(lpTvLoading);
//        textSize = getResources().getDimension(R.dimen.d_textSize_progLoading) / mModuleHeight * mScreenHeightPx;
//        id_tvLoading.setTextSize(textSize);
//
//        AbsoluteLayout.LayoutParams lpProBg = (AbsoluteLayout.LayoutParams)id_progressBg.getLayoutParams();
//        lpProBg.width = (int) (getResources().getDimension(R.dimen.d_width_progBG) / mModuleWidth * mScreenWidthPx);
//        lpProBg.height = (int) (getResources().getDimension(R.dimen.d_height_progBG) / mModuleHeight * mScreenHeightPx);
////        layoutParams.x = (int) (getResources().getDimension(R.dimen.d_x_progBG) / mModuleWidth * mScreenWidthPx);
//        lpProBg.x = mScreenWidthPx / 2 - lpProBg.width / 2;
//        lpProBg.y = (int) (getResources().getDimension(R.dimen.d_y_progBG) / mModuleHeight * mScreenHeightPx);
//        id_progressBg.setLayoutParams(lpProBg);
//
//
////        lpPro = (AbsoluteLayout.LayoutParams)id_progressMove_1.getLayoutParams();
//        // 用int好些，后面的用的都是int，数据差可以几乎消除。
//        int margin = (int)Math.max((getResources().getDimension(R.dimen.d_marginTop_progMove) / mModuleHeight * mScreenHeightPx),
//                (getResources().getDimension(R.dimen.d_marginLeft_progMove) / mModuleWidth * mScreenWidthPx));
//
//
////        lpPro.width = (int) (lpProBg.width - (getResources().getDimension(R.dimen.d_marginLeft_progMove) / mModuleWidth * mScreenWidthPx) * 2);
////        lpPro.height = (int) (lpProBg.height - (getResources().getDimension(R.dimen.d_marginTop_progMove) / mModuleHeight * mScreenHeightPx) * 2);
////        lpPro.x = (int)(lpProBg.x + (getResources().getDimension(R.dimen.d_marginLeft_progMove) / mModuleWidth * mScreenWidthPx));
////        lpPro.y = (int)(lpProBg.y + (getResources().getDimension(R.dimen.d_marginTop_progMove) / mModuleHeight * mScreenHeightPx));
//
//        AbsoluteLayout.LayoutParams lpPro = (AbsoluteLayout.LayoutParams)id_progress.getLayoutParams();
//        lpPro.width = (int) (lpProBg.width - margin * 2);
//        lpPro.height = (int) (lpProBg.height - margin * 2);
//        lpPro.x = (int)(lpProBg.x + margin);
//        lpPro.y = (int)(lpProBg.y + margin);
//
//        Log.i(TAG, "resetUILocation: margin=" + margin + ", (lpProBg.x - lpPro.x)=" + (lpProBg.x - lpPro.x) + ", (lpProBg.y - lpPro.y)=" + (lpProBg.y - lpPro.y)); // 3个值是一样的
////        int widthMargin = (lpPro.x - lpProBg.x);
////        int heightMargin = (lpPro.y - lpProBg.y);
////        margin = Math.max(widthMargin, heightMargin);
////        lpPro.width = lpProBg.width - (int)margin * 2;
////        lpPro.height = lpProBg.height - (int)margin * 2;
////        lpPro.x = lpProBg.x + (int)margin;
////        lpPro.y = lpProBg.y + (int)margin;
//
////        // 用大的值做边距
////        if(widthMargin > heightMargin){
////            lpPro.height = lpPro.height - (widthMargin - heightMargin) * 2;
////            lpPro.y = lpPro.y + (widthMargin - heightMargin);
////        }
////        else if(heightMargin > widthMargin){
////            lpPro.width = lpPro.width - (heightMargin - widthMargin) * 2;
////            lpPro.x = lpPro.x + (heightMargin - widthMargin);
////        }
//
////        id_progressMove_1.setLayoutParams(lpPro);
//
//
////        AbsoluteLayout.LayoutParams lpProMove2 = (AbsoluteLayout.LayoutParams)id_progressMove_2.getLayoutParams();
////        lpProMove2.width = (int) (getResources().getDimension(R.dimen.d_width_progMove) / mModuleWidth * mScreenWidthPx);
////        lpProMove2.height = (int) (getResources().getDimension(R.dimen.d_height_progMove) / mModuleHeight * mScreenHeightPx);
////        lpProMove2.x = (int)((float)mScreenWidthPx / 2 - (float)lpProMove2.width / 2);
////        lpProMove2.y = (int)(BGY + margin);
////        lpProMove2 = lpPro;
////        lpProMove2.x = lpPro.x - lpProMove2.width;
////        id_progressMove_2.setLayoutParams(lpPro);
//
////        AbsoluteLayout.LayoutParams lpProgress = (AbsoluteLayout.LayoutParams)id_progress.getLayoutParams();
////        lpProgress = lpPro;
//
//
//        id_progress.setLayoutParams(lpPro);
//        id_progress.setMinimumWidth(lpPro.width);
//        id_progress.setMinimumHeight(lpPro.height);
//        //id_progress.requestLayout();
//
//
//        Log.i(TAG, "resetUILocation: lpPro.x=" + lpPro.x + ", lpPro.y=" + lpPro.y + ", lpPro.width=" + lpPro.width + ", lpPro.height=" + lpPro.height);
//
//        id_progressTop.setLayoutParams(lpPro);
//
//        lpTvLoading = (AbsoluteLayout.LayoutParams)id_tvTips.getLayoutParams();
//        lpTvLoading.x = 0;
//        lpTvLoading.y = (int) (getResources().getDimension(R.dimen.d_y_progTips) / mModuleHeight * mScreenHeightPx);
//        lpTvLoading.width = AbsoluteLayout.LayoutParams.MATCH_PARENT;;
//        lpTvLoading.height = AbsoluteLayout.LayoutParams.WRAP_CONTENT;;
//        id_tvTips.setLayoutParams(lpTvLoading);
//        textSize = getResources().getDimension(R.dimen.d_textSize_progTips) / mModuleHeight * mScreenHeightPx;
//        id_tvTips.setTextSize(textSize);
//
////        ProgressDialog progressDialog = ProgressDialog.show(this, "测试是否转动", "测试动画是否转动", true); // 可以转动
//
//    }
//
//
//    private boolean zt=false;
//
//    private void initGPS() {
//        LocationManager locationManager = (LocationManager) this
//                .getSystemService(Context.LOCATION_SERVICE);
//        // 判断GPS模块是否开启，如果没有则开启
//        if (!locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(ConnectActivity.this, "请打开GPS",
//                    Toast.LENGTH_SHORT).show();
//            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//            dialog.setMessage("请打开GPS");
//            dialog.setPositiveButton("确定",
//                    new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//
//                            arg0.dismiss();
//                            // 转到手机设置界面，用户设置GPS
//                            Intent intent = new Intent(
//                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
//                        }
//                    });
//            dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface arg0, int arg1) {
//                    arg0.dismiss();
//                }
//            } );
//            dialog.show(); // android.view.WindowLeaked: Activity com.iot.remocon.ConnectActivity has leaked window com.android.internal.policy.impl.PhoneWindow$DecorView{c5d0a8f V.E..... R....... 0,0-698,281} that was originally added here
//
//        } else {
//            // 弹出Toast
////          Toast.makeText(TrainDetailsActivity.this, "GPS is ready",
////                  Toast.LENGTH_LONG).show();
////          // 弹出对话框
////          new AlertDialog.Builder(this).setMessage("GPS is ready")
////                  .setPositiveButton("OK", null).show();
//        }
//    }
//    /**
//     * This is an override which diverts the usual startup once a node is
//     * connected. Typically this would go to the master chooser, however
//     * here we are sometimes returning from one of its child apps (in which
//     * case it doesn't have to go choosing a concert). In that case, send
//     * it directly to the concert validation and initialisation steps.
//     */
//    public void connWifiRos() { // execute 4 希望直接连接固定主机
//        Log.i(TAG, "connWifiRos:"); // 先
//
//        if (!isConnected) {
//            connectMaster = new ConnectMaster(ConnectActivity.this, new ConnectMasterListener(){
//                @Override
//                public void success(RoconDescription roconDescription, String SSID){
//                    if(fi_unknown){
//                        return;
//                    }
//
//
//                    Log.i(TAG, "connWifiRos() success:");
//                    if (roconDescription != null) {
//                        Log.i(TAG, "成功拿到了roconDescription, SSID=" + SSID);
//                        isConnected = true;
//
////                        init(roconDescription);
//                        Intent intent = new Intent(ConnectActivity.this, RemoconAllActivity.class);
//                        intent.putExtra(RoconDescription.UNIQUE_KEY, roconDescription);
//                        intent.putExtra("mWifiName", mWifiName);
//                        intent.putExtra("mWifiPwd", mWifiPwd);
//                        startActivity(intent);
//                        zt=true;
//
//                        Message msg = mHandler.obtainMessage();
//                        msg.obj = getString(R.string.s_connMasterSuccess);
//                        msg.what = handler_setTipsText;
//                        mHandler.sendMessage(msg);
//                        finish();
//                    } else {
//                        if(isConnected) {
//                            Log.i(TAG, "success: 连接之后收到错误信息，不理会");
//                            return;
//                        }
//                        showToast(getString(R.string.s_Error_masterParams));
//                        Log.i(TAG, "connWifiRos() success:, roconDescription拿到的是空的，这里执行了shutDown()");
//                        Message msg = mHandler.obtainMessage();
//                        msg.what = handler_error_exit;
//                        msg.obj = getString(R.string.s_connMasterFailed);
//                        mHandler.sendMessage(msg);
//                    }
//                }
//                @Override
//                public void failed(String message){
//                    //if (isBackPressed) {
//                      //  Log.e(TAG, "failed: 已经手动退出此界面，这里不再处理");
//                       // return;
//                    //}
//                    if(isConnected) {
//                        Log.i(TAG, "success: 连接之后收到错误信息，不理会");
//                        return;
//                    }
//                    Log.e(TAG, "连接主机失败：" + message);
//                    Message msg = mHandler.obtainMessage();
//                    msg.what = handler_error_exit;
//                    msg.obj = getString(R.string.s_connMasterFailed); // "连接主机失败：" + message;
//                    mHandler.sendMessage(msg);
//                }
//            });
//            if(fi_unknown){
//            }else{
//                Log.i(TAG, "connWifiRos: mWifiName="+mWifiName+", mWifiPwd="+mWifiPwd);
//                connectMaster.connectWifiRos(ConnectActivity.this, mWifiName, mWifiPwd);
//            }
//
//        }
//    }
//
//    /**
//     * 获取旧的wifi
//     */
//    private void setOldWifi() {
//        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
////        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//
////        Log.i(TAG, "Ssid wifiManager.getConnectionInfo().toString()=" + wifiManager.getConnectionInfo().toString());
//        if (wifiManager.getConnectionInfo() != null) {
//            mWifiOldName = wifiManager.getConnectionInfo().getSSID().replace("\"", "").trim(); // 有时候带了双引号，有时候带了空格。wifiManager.getConnectionInfo()有没有可能是null?
//            mWifiOldNetId = wifiManager.getConnectionInfo().getNetworkId();
//        }
//        Log.i(TAG, "setOldWifi: mWifiOldName="+mWifiOldName+",mWifiOldNetId="+mWifiOldNetId);
//    }
//
//    /** 修改wifi */
//    private void showDialogSetWifi(){
//
//        View rootView = LayoutInflater.from(ConnectActivity.this).inflate(R.layout.dialog_remo_setwifi, null);
//        final EditText id_dialogWifi = (EditText)rootView.findViewById(R.id.id_dialogWifi);
//        final EditText id_dialogPWD = (EditText)rootView.findViewById(R.id.id_dialogPWD);
//        Button id_dialogBtnOK = (Button)rootView.findViewById(R.id.id_dialogBtnOK);
//        Button id_dialogBtnCancel = (Button)rootView.findViewById(R.id.id_dialogBtnCancel);
//        final Button id_dialogBtn00 = (Button)rootView.findViewById(R.id.id_dialogBtn00);
//        final Button id_dialogBtn01 = (Button)rootView.findViewById(R.id.id_dialogBtn01);
//        final Button id_dialogBtn02 = (Button)rootView.findViewById(R.id.id_dialogBtn02);
//        final Button id_dialogBtn03 = (Button)rootView.findViewById(R.id.id_dialogBtn03);
//
////        id_dialogWifi.setHint(mWifiName);
////        id_dialogPWD.setHint(mWifiPwd);
//
//        final Dialog dialogName = new MyDialog(ConnectActivity.this, 0, 0, rootView,
//                R.style.dialog); // cx, R.style.dialog
//        dialogName.setCancelable(false);
//
//        id_dialogBtnOK.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!id_dialogWifi.getText().toString().trim().equals("") && !id_dialogPWD.getText().toString().trim().equals("")) {
////                    showToast("wifi和密码不能为空");
//                    dialogName.dismiss();
//                    mWifiName = id_dialogWifi.getText().toString();
//                    mWifiPwd = id_dialogPWD.getText().toString();
//                    mHandler.sendEmptyMessage(handler_setWifi);
//                }
//                else {
//                    if(id_dialogWifi.getText().toString().trim().equals("")) mWifiName = id_dialogWifi.getHint().toString().trim();
//                    else mWifiName = id_dialogWifi.getText().toString();
//
//                    if(id_dialogPWD.getText().toString().trim().equals("")) mWifiPwd = id_dialogPWD.getHint().toString().trim();
//                    else  mWifiPwd = id_dialogPWD.getText().toString();
//
//                    dialogName.dismiss();
//                    mHandler.sendEmptyMessage(handler_setWifi);
//                }
//            }
//        });
//        id_dialogBtnCancel.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
////                mHandler.sendEmptyMessage(handler_setWifi);
//                dialogName.dismiss();
//                finish();
//            }
//        });
//
//        id_dialogBtn00.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                dialogName.dismiss();
//                mWifiName = id_dialogBtn00.getText().toString().trim();
//                mHandler.sendEmptyMessage(handler_setWifi);
//            }
//        });
//        id_dialogBtn01.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                dialogName.dismiss();
//                mWifiName = id_dialogBtn01.getText().toString().trim();
//                mHandler.sendEmptyMessage(handler_setWifi);
//            }
//        });
//        id_dialogBtn02.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                dialogName.dismiss();
//                mWifiName = id_dialogBtn02.getText().toString().trim();
//                mHandler.sendEmptyMessage(handler_setWifi);
//            }
//        });
//        id_dialogBtn03.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                dialogName.dismiss();
//                mWifiName = id_dialogBtn03.getText().toString().trim();
//                mHandler.sendEmptyMessage(handler_setWifi);
//            }
//        });
//
//        Window dialogWindow = dialogName.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = 456; // 420 // 宽度
//        lp.height = 588; // 276; // 210; // 高度
//
//        dialogWindow.setAttributes(lp);
//
//        if(!isFinishing())dialogName.show(); // E/WindowManager: android.view.WindowLeaked: Activity com.iot.remocon.RemoconAllActivity has leaked window com.android.internal.policy.PhoneWindow$DecorView{3c8ef37 V.E...... R.....ID 0,0-640,256} that was originally added here
//
//        if (!dialogName.isShowing()) {
//            dialogName.dismiss();
//        }
////        Log.i(TAG, "alert.isShowing():" + dialogName.isShowing() + "\t lp.width="+lp.width + "\t lp.height=" + lp.height);
//    }
//
//
//
//
//    /** 关闭wifi ap */
//    private void disconnectAP(){
//        boolean isWifiStopped = true; // 有时候ap没连接上。
//        if(connectMaster != null ) {
//            isWifiStopped = connectMaster.disConnectAP(); // 这里关闭成功了。
//            Log.i(TAG, "disconnectAP: wifi 是否已关闭：" + isWifiStopped);
//            connectMaster.setBo();
//        }else{
////            WifiConfiguration tempConfig = isExsits(WifiConnect.app.substring(1, WifiConnect.app.length()-1));
//            Log.i(TAG, "disconnectAP: mWifiOldName="+mWifiOldName+",mWifiOldNetId="+mWifiOldNetId);
//            if (mWifiOldName != null && !mWifiOldName.equals("")) {
//                WifiConfiguration tempConfig = isExsits(mWifiOldName);
//                if (tempConfig != null){
//                    boolean enabled = wifi.enableNetwork(tempConfig.networkId, true);
//                    Log.i(TAG, "disconnectAP: idGet=" + tempConfig.networkId+",是否已连上原来的wifi?"+enabled);
//                }
//            }
//        }
//    }
//    /** 显示Toast */
//    private void showToast(int strId){
//        Toast.makeText(ConnectActivity.this, strId, Toast.LENGTH_LONG).show();
//    }
//    /** 显示Toast */
//    private void showToast(String str){
//        Toast.makeText(ConnectActivity.this, str, Toast.LENGTH_LONG).show();
//    }
//
//    /**
//     * 退出
//     */
//    private void exit() {
//        Log.i(TAG, "exit: 退出");
//        if(!zt){
//            disconnectAP();
//        }
//
//        finish();
//    }
//
//    WifiManager wifi;
//    boolean fi_unknown =false;
//    @Override
//    protected void onDestroy() {
//        Log.d(TAG,zt+"  qqq");
//        fi_unknown =true;
//        if(zt==false){
//            disconnectAP();
//
//        }
//        super.onDestroy();
//    }
//    // 查看以前是否也配置过这个网络、这个网络的密码
//    private WifiConfiguration isExsits(String SSID) {
//        Log.i(TAG, "isExsits: SSID=" + SSID);
//        if (SSID == null || SSID.equals("")) {
//            return null;
//        }
//        List<WifiConfiguration> existingConfigs = wifi.getConfiguredNetworks();
//
//        if(existingConfigs != null) {
//            for (WifiConfiguration existingConfig : existingConfigs) {
//                Log.d(TAG,SSID+ "    "+existingConfig.SSID +"  conn");
//                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
//
//                    Log.i(TAG, "isExsits: 已配置的wifi列表里存在 " + SSID + ", 密码：" + existingConfig.preSharedKey);
//                    return existingConfig;
//                }
////                if (existingConfig.SSID.equals( SSID  )) {
////
////                    Log.i(TAG, "isExsits: 已配置的wifi列表里存在 " + SSID + ", 密码：" + existingConfig.preSharedKey);
////                    return existingConfig;
////                }
//            }
//        }else Log.e(TAG, "isExsits: existingConfigs is null");
//        return null;
//    }
}
