package com.map.tcp;

import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.github.rosjava.android_remocons.common_tools.master.ConcertChecker;
import com.github.rosjava.android_remocons.common_tools.master.MasterId;
import com.github.rosjava.android_remocons.common_tools.master.RoconDescription;
import com.github.rosjava.android_remocons.common_tools.system.WifiChecker;
import com.grasp.control.R;

import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

import static com.map.tcp.ConnectMaster.ConnResult.R_CannotContactConcert;
import static com.map.tcp.ConnectMaster.ConnResult.R_ErrorMasterUri;
import static com.map.tcp.ConnectMaster.ConnResult.R_MasterUnavailable;
import static com.map.tcp.ConnectMaster.ConnResult.R_MasterUriNull;
import static com.map.tcp.ConnectMaster.ConnResult.R_MasterWifiConnFailed;
import static com.map.tcp.ConnectMaster.ConnResult.R_NoWifiConnected;

/**
 * 连接固定ip的master
 * Created by Administrator on 2017/3/2-002.
 */
public class ConnectMaster {
    private final String TAG = "Print-ConnectM";
    private Context mContext;
    private RoconDescription roconDescription = null;
    private ConnectMasterListener mListener;
    private static ConnectMaster connectMaster;
    /** 是否已连接上wifi */
    private boolean isWifiConnected = false;
    private Yaml yaml = new Yaml();
    /** wifi start */
    private static String mWifiName = "";
    private static String mWifiPwd = "";
    private static String WIFI_PWD_DEFAULT = "broad608";
//    private static final String mRosUri = "http://10.42.0.1";
    /** 当前已连接的SSID */
    private String currentSSID = "";
    private final int handler_initMaster = 0x200;
    /** 连接wifi或ros的返回结果 */
    public enum ConnResult{
        /** 当前没有wifi连接 */
        R_NoWifiConnected,
        /** 机器人主机地址错误 */
        R_ErrorMasterUri,
        /** 机器人地址为空 */
        R_MasterUriNull,
        /** 连接主机ROS失败 */
        R_CannotContactConcert,
        /** 主机地址不可连接 */
        R_MasterUnavailable,
        /** 机器人wifi连接失败 */
        R_MasterWifiConnFailed
    };
    /**************************************************************************************/
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case handler_initMaster:
                    initMasterDescription();
                    break;
                default:
                    break;
            }
        }
    };

    public ConnectMaster(Context context, ConnectMasterListener listener){
        Log.i(TAG, "ConnectMaster()");
        this.mContext = context;
        this.roconDescription = null;
        this.mListener = listener;
    }

    public static ConnectMaster newInstance(Context context, ConnectMasterListener listener){
        if(connectMaster == null) {
            connectMaster = new ConnectMaster(context, listener);
        }
        return connectMaster;
    }

    /** 初始化Master相关数据 */
    public void initMasterDescription(){
        Log.i(TAG, "initMasterDescription:");
        String newMasterUri = mContext.getString(R.string.s_uri);
//        String newWifiName = mWifiName; //mContext.getString(R.string.s_wifiName); // 这里必须置空，remocon自带的连接wifi不可用
//        String newWifiPassword = mWifiPwd;//mContext.getString(R.string.s_wifiPwd); // 这里必须置空，remocon自带的连接wifi不可用

        if (newMasterUri != null && newMasterUri.length() > 0) {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("URL", newMasterUri);
            if (mWifiName != null && mWifiName.length() > 0) {
                data.put("WIFI", mWifiName);
            }
            if (mWifiPwd != null && mWifiPwd.length() > 0) {
                data.put("WIFIPW", mWifiPwd);
            }
            try {

                Log.i(TAG, "initMasterDescription: ros的ip地址：newMasterUri=" + newMasterUri+", mWifiName="+mWifiName+", mWifiPwd="+mWifiPwd);
                addMaster(new MasterId(data));
            } catch (Exception e) {
//                Toast.makeText(mContext, "Invalid Parameters.", Toast.LENGTH_SHORT).show();
//                mListener.failed("Error!" + " Invalid Parameters. " + e.toString());
//                mListener.failed(mContext.getString(R.string.s_Error_masterUri));
                mListener.failed(R_ErrorMasterUri);
                e.printStackTrace();
            }
        } else {
//            Toast.makeText(mContext, "Must specify Master URI.",
//                    Toast.LENGTH_SHORT).show();
//            mListener.failed("Error!" + " Must specify Master URI.");
//            mListener.failed(mContext.getString(R.string.s_Error_masterUriNull));
            mListener.failed(R_MasterUriNull);
        }
    }

    private void addMaster(MasterId masterId) {
        addMaster(masterId, false);
    }

    private void addMaster(MasterId masterId, boolean connectToDuplicates) {
        Log.i(TAG, "addMaster: masterId=" + masterId.toString());
        if (masterId == null || masterId.getMasterUri() == null) {
            Log.e(TAG, "addMaster: 连接ros失败，masterId == null || masterId.getMasterUri() == null");
            mListener.failed(R_MasterUriNull);
        } else {
            if(roconDescription != null) roconDescription = null;
            roconDescription = RoconDescription.createUnknown(masterId); // wifi和ros不能在一次逻辑里都连上，可能是这里出问题了。
            Log.i(TAG, "addMaster: roconDescription.getConnectionStatus()=" + roconDescription.getConnectionStatus()); // true
            onMastersChanged(masterId);
        }
    }

    int countMaster = 0;
    boolean isOK = false;

    /**
     * wifiManager.getConnectionInfo().toString()=SSID: ap, BSSID: 00:21:5c:c9:12:b5 此状态下连接上了ap，后续可能“isOK=false, count=10”也可以连上ros，这可能是误报错误，其实ok=true
     * @param masterId
     */
    private void onMastersChanged(final MasterId masterId) {
//        writeMasterList();
        Log.i(TAG, "onMastersChanged: ");
        countMaster = 0;
        new Thread()
        {
            public void run()
            {
                while (!isOK) {
                    try {

                        if (countMaster >= 5) {
                            Log.i(TAG, "onMastersChanged: isOK=" + isOK + ", countMaster=" + countMaster);
                            updateListView();
                            break;
                        } else if (roconDescription == null || roconDescription.getConnectionStatus() == null || roconDescription.getConnectionStatus().equals(RoconDescription.ERROR) || roconDescription.getConnectionStatus().equals(RoconDescription.UNAVAILABLE)) {
                            if (countMaster % 2 == 0) { // 可能在连接的时候，wifi早就不是ap了。如果SSID是ap，BSSID是null，表示正在连接ap，要等待

                                if(isWifiConnected()){
                                    roconDescription = RoconDescription.createUnknown(masterId);
                                    Log.i(TAG, "run: wifi已连接，重新createUnknown(masterId), roconDescription.getConnectionStatus()=" + roconDescription.getConnectionStatus() ); // 大概重连9次以后连接上了。必须增加连接的可能性。
                                }
                                else if(isWifiConnecting()){// && !wifi.isAPBSSIDExisted(mContext)){
                                    Log.e(TAG, "run: 正在连接wifi...");
                                }
                                else {
                                    Log.e(TAG, "run: 将连接wifi");
                                    connectWifiRos(mContext, mWifiName, mWifiPwd);
                                    break;
                                }
                            }
                        } else { // 如果状态是ok，就可以连接上了
                            isOK = true;

                            Log.i(TAG, "onMastersChanged: isOK=" + isOK + ", countMaster=" + countMaster);
                            updateListView();
                            break;
                        }


                        Thread.sleep(1000);
                        countMaster++;
                    } catch (InterruptedException e) {
                        Log.i(TAG, "onMastersChanged: isOK=" + isOK + ", countMaster=" + countMaster);
                        updateListView();
                        e.printStackTrace();
                    }
                }
            }
        }.start();



//        Log.i(TAG, "onMastersChanged: isOK=" + isOK + ", countMaster=" + countMaster);
//
//        updateListView();

    }

    public void refresh() {
//        readMasterList();
        Log.i(TAG, "refresh: ");
        isWifiConnected = isWifiConnected();
        if (isWifiConnected){// && wifi.isAPBSSIDExisted(mContext)) {
            Log.i(TAG, "refresh: wifi已连接，将连接ros");
            updateListView();
        } else {
            Log.i(TAG, "refresh: wifi未连接，将连接wifi");
            connectWifiRos(mContext, mWifiName, mWifiPwd);
        }
    }
//    private void readMasterList() {//2
//        String str = null;
//        Cursor c = mContext.getContentResolver().query(
//                Database.CONTENT_URI, null, null, null, null);
//        if (c == null) {
////            masters = new ArrayList<RoconDescription>();
//            roconDescription = new RoconDescription();
//            Log.e(TAG, "获取数据库游标失败!!!");
//            return;
//        }
//        if (c.getCount() > 0) {
//            c.moveToFirst();
//            str = c.getString(c.getColumnIndex(Database.TABLE_COLUMN));
//            Log.i(TAG, "数据库找到一个MasterUri: " + str);
//        }
//        if (str != null) {
////            masters = (List<RoconDescription>) yaml.load(str);
//            roconDescription = (RoconDescription) yaml.load(str);
//        } else {
//            roconDescription = new RoconDescription();
//        }
//    }
//
//    /** 写入记录Master？先不用List<Master>,用单个的RoconDescription，看可行否 */
//    public void writeMasterList() {
//        Log.i(TAG, "master chooser saving rocon master details...");
//
//        String str = null;
//        final RoconDescription tmp = roconDescription;
//        if (tmp != null) {
//            str = yaml.dump(tmp);
//        }
//        ContentValues cv = new ContentValues();
//        cv.put(Database.TABLE_COLUMN, str);
//        Uri newEmp = mContext.getContentResolver().insert(Database.CONTENT_URI, cv);
//        if (newEmp != Database.CONTENT_URI) {
//            Log.e(TAG, "master chooser could not save roconDescription, non-equal URI's");
//        }
//    }

//    /** 超时时间 */
//    private int timeCiShu = 10;
    private int countUpdate = 0;
    /** 重新连接 */
    private void updateListView() {
        if (roconDescription == null) { // 有时候会空。
            Log.e(TAG, "updateListView: roconDescription is null");
            return;
        }
        countUpdate = 0;
        roconDescription = setRoconDescription(roconDescription); // 内部失败了自己连接，不要在这里反复。
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isReceived) {
                    try {
//                        roconDescription = setRoconDescription(roconDescription);
                        countUpdate++;
                        if (countUpdate >= 3) {
                            break;
                        }else {
                            Thread.sleep(3000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
        Log.i(TAG, "updateListView: countUpdate=" + countUpdate + ", isReceived=" + isReceived);
    }
    /**
     * Called when the user clicks on one of the listed masters in master chooser
     * view. Should probably check the connection status before
     * proceeding here, but perhaps we can just rely on the user clicking
     * refresh so this process stays without any lag delay.
     *
     * @param
     */
    private void choose(RoconDescription roconDescription) {
        Log.i(TAG, "choose()");
//        RoconDescription roconDescription = masters.get(position);
        if (roconDescription == null || roconDescription.getConnectionStatus() == null
                || roconDescription.getConnectionStatus().equals(RoconDescription.ERROR)) {
//            mListener.failed(mContext.getString(R.string.s_Error_CannotContactConcert));
            mListener.failed(R_CannotContactConcert);
        } else if ( roconDescription.getConnectionStatus().equals(RoconDescription.UNAVAILABLE) ) {
//            mListener.failed(mContext.getString(R.string.s_Error_MasterUnavailable));
            mListener.failed(R_MasterUnavailable);
        } else {
            Log.i(TAG, "choose: roconDescription.getConnectionStatus() is null?" + (roconDescription.getConnectionStatus() == null));
            mListener.success(roconDescription, ConnectMaster.this.currentSSID);
        }
    }

    int countConcert = 0;
    boolean isReceived = false;
    String errorRoconReason = "";
    public RoconDescription setRoconDescription(final RoconDescription roconDescription) {
//        this.parentMca = parentMca;
        final RoconDescription description = roconDescription;
        isReceived = false;
        ConcertChecker checker;
        description.setConnectionStatus(RoconDescription.CONNECTING);
        if (WifiChecker.wifiValid(description.getMasterId(), (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE))) {
//            checker = new ConcertChecker(this, this);
            checker = new ConcertChecker(new ConcertChecker.ConcertDescriptionReceiver(){
                @Override
                public void receive(RoconDescription roconDescription) {
                    Log.i(TAG, "receive: 拿到ok的description，将choose()");
                    description.copyFrom(roconDescription);
                    checkState(description);
                    isReceived = true;
                    choose(roconDescription); // 这里不能直接choose，将无法成功。

//                    Log.i(TAG, "收到roconDescription, description.getConnectionStatus()=" + description.getConnectionStatus());
                }
            },

            new ConcertChecker.FailureHandler(){ // 断链主机minimal后很快检测到了。
                @Override
                public void handleFailure(String reason) {
                    errorRoconReason = reason;
                    Log.e(TAG, "handleFailure: errorRoconReason=" + errorRoconReason + ", countConcert=" + countConcert);
                    description.setConnectionStatus(RoconDescription.ERROR);
//                    mListener.failed(reason);
                    checkState(description);
                    choose(null);

//                    if (!isReceived && countConcert < 3) {
//                        try {
//                            setRoconDescription(description);
//                            Thread.sleep(1000);
//                            countConcert++;
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }else {
//                        checkState(description);
//                        choose(null);
//                    }
                }
            });
            checker.beginChecking(description.getMasterId());
            
        } else {
            errorRoconReason = "Wrong WiFi Network";
            Log.i(TAG, "setRoconDescription: errorRoconReason=" + errorRoconReason);
            description.setConnectionStatus(RoconDescription.WIFI);
//            mListener.failed(errorRoconReason);
            checkState(description);
            choose(null);


//            if (!isReceived && countConcert < 3) {
//                try {
//                    setRoconDescription(description);
//                    Thread.sleep(1000);
//                    countConcert++;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }else {
//                checkState(description);
//                choose(null);
//            }
        }
        Log.e(TAG, "errorRoconReason=" + errorRoconReason);
        return description;
    }

    private void checkState(RoconDescription description) {
//        connectState();
        Log.i(TAG, "connectionRoconStatus = " + description.getConnectionStatus()); // 断开主机的minimal，将exception。
        boolean isOk = description.getConnectionStatus().equals(RoconDescription.OK);
        boolean isUnavailable = description.getConnectionStatus().equals(RoconDescription.UNAVAILABLE);
        boolean isWifi = description.getConnectionStatus().equals(RoconDescription.WIFI);
        boolean isError = description.getConnectionStatus().equals(RoconDescription.ERROR);
        boolean isConnecting = description.getConnectionStatus().equals(RoconDescription.CONNECTING);

        Log.e(TAG, "connectionRoconStatus: isOK=" + isOk);
        Log.e(TAG, "connectionRoconStatus: isConnecting=" + isConnecting);
        Log.e(TAG, "connectionRoconStatus: isUnavailable=" + isUnavailable);
        Log.e(TAG, "connectionRoconStatus: isWifi=" + isWifi);
        Log.e(TAG, "connectionRoconStatus: isError=" + isError);
//        writeMasterList();
        
    }
    /** 连接wifi用的 */
    private WifiConnect wifi = null;

    /** 连接wifi */
    public void connectWifiRos(final Context context, final String SSIDParam, final String pwd){
        Log.i(TAG, "connectWifiRos: SSIDParam="+SSIDParam+", pwd="+pwd);

        mWifiName = SSIDParam;
        mWifiPwd = pwd;

        wifi = new WifiConnect((WifiManager) context
                .getSystemService(Context.WIFI_SERVICE), context);


        wifi.setIsStopConnect(false);
        wifi.setListener(new ConnectWifiListener() {
            @Override
            public void conWifiSuccess(String SSID) {
                // 这里不能用Async异步，否则解决不了进度条卡住，且导致重复连接而ros服务对象重复。

                Log.i(TAG, "conWifiSuccess: 测试是否假连接");
                int count = 0;
                boolean isConnected = isWifiConnected();
                while (!isConnected) { // 确保已经连接上了，否则会造成假连接，例如ap不可用。
                    try {
                        if(count > 5) break;
                        Thread.sleep(1000);
                        isConnected = isWifiConnected();
                        count++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                wifi.setIsStopConnect(true);

                if (isConnected) {
                    Log.i(TAG, "连上" + mWifiName + "了，将连接主机"); // 连上"iotbroad_mi"了，将连接主机，竟然也可以链接ros //  "连上" + wifi.getConnectWifiSsid(mContext) + "了，将连接主机"
                    isWifiConnected = true;
                    mHandler.sendEmptyMessage(handler_initMaster);
                }else{
                    Log.e(TAG, "conWifiSuccess: 假连接ap,wifi不可用，");
//                    mListener.failed(mListener.ERROR_WIFI);
//                    mListener.failed(mContext.getString(R.string.s_Error_masterWifi));
                    mListener.failed(R_MasterWifiConnFailed);
                }
            }

            @Override
            public void conWifiFailed() { // 无法检测wifi断连
                Log.i(TAG, "没连上wifi");
                isWifiConnected = false;
//                mListener.failed(mListener.ERROR_WIFI);
//                mListener.failed(mContext.getString(R.string.s_Error_masterWifi));
                mListener.failed(R_MasterWifiConnFailed);
                wifi.setIsStopConnect(true);
            }

        });
        wifi.connect(mWifiName, mWifiPwd, WifiConnect.WifiCipherType.WIFICIPHER_WPA); // 没有密码就连不上

    }

    /** 直接连接ros，用系统当前wifi
     * @param context
     */
    public void connectRos(Context context) {
        Log.i(TAG, "connectRos: 不去连接wifi，直接连接ros");
        wifi = new WifiConnect((WifiManager) context
                .getSystemService(Context.WIFI_SERVICE), context);
        String[] strings = wifi.getConnectWifiSsid(context);
        Log.i(TAG, "connectRos: 当前系统的wifi连接状态，wifiName="+strings[0]+", BSSID="+strings[1]+", state="+strings[2]);
        if(strings[0] != null && !strings[0].equals("") && strings[2].equals(SupplicantState.COMPLETED.toString())){
            mWifiName = strings[0];
            mWifiPwd = WIFI_PWD_DEFAULT;
            mHandler.sendEmptyMessage(handler_initMaster);
        }
        else {
            Log.e(TAG, "connectRos: 当前没有连接wifi，无法连接ros");
            mListener.failed(R_NoWifiConnected);
        }

    }

    /** 连接指定的wifi */
    public boolean disConnectAP(){
        Log.d(TAG,"disConnectAP");
        Log.i(TAG, "disConnectAP: wifi is null?" +(wifi == null));
        if(wifi != null) {
            return wifi.disconnectWifi();
        }
        return false;
    }
    public void setBo(){
        wifi.setZt(false);
    }
    /** wifi连接状态 */
    public boolean isWifiConnected() {
        isWifiConnected = false;
        String[] wifiInfo = wifi.getConnectWifiSsid(mContext);
//        Log.i(TAG, "isWifiConnected: mWifiName=" + mWifiName + ", wifi.getConnectWifiSsid(mContext)=" + wifi.getConnectWifiSsid(mContext));
        if(!mWifiName.equals("") && wifiInfo[0].equals(mWifiName) && wifiInfo[2].equals(SupplicantState.COMPLETED.toString()) && wifiInfo[1] != null){ //  && wifi.isAPBSSIDExisted(mContext)
            isWifiConnected = true;
        }
        return isWifiConnected;
    }

    public boolean isWifiConnecting() {
        String[] wifiInfo = wifi.getConnectWifiSsid(mContext);
        if(!mWifiName.equals("") && wifiInfo[0].equals(mWifiName) && wifiInfo[2].equals(SupplicantState.SCANNING.toString())){ //  && wifi.isAPBSSIDExisted(mContext)
            return true;
        }
        return false;
    }
}

