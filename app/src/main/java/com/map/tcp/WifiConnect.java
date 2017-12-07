package com.map.tcp;

import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import static com.grasp.control.view.LayoutMainMAP.mWifiOldName;
import static com.grasp.control.view.LayoutMainMAP.mWifiOldNetId;

/**
 * Created by Administrator on 2017/3/23-023.
 */
public class WifiConnect {
    //    private static final String TAG = WifiConnect.class.getSimpleName();
    private static final String TAG = "Print-WifiConnect";
    private Context mContext;
    private ConnectWifiListener listener = null;
    /** 已经连接上的wifi的id号 */
    private int netID = -1;
    /** 最大优先权 */
    private int heighestPriority = -1;

    WifiManager wifiManager;
    private final static String BSSIDNone = "00:00:00:00:00:00"; // <none>

    // 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    // 构造函数
    public WifiConnect(WifiManager wifiManager, Context context) {
        this.wifiManager = wifiManager;
        this.mContext = context;
    }
    private int count = 0;
    // 提供一个外部接口，传入要连接的无线网
    public void connect(String ssid, String password, WifiCipherType type) {
        // 如果已连接其他wifi，则先关闭
        closeCurrentWifi();

        Log.i(TAG, "connect: 测试执行先后顺序，之前是否已检测过当前连接");
        count = 0;
        setIsStopConnect(false);
        Thread thread = new Thread(new ConnectRunnable(ssid, password, type));
        thread.start();

    }

    /** 如果已连接其他wifi，则先关闭 */
    private void closeCurrentWifi() {
//        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        Log.i(TAG, "Ssid wifiManager.getConnectionInfo().toString()=" + wifiManager.getConnectionInfo().toString());
//        return wifiManager.getConnectionInfo().getSSID().replace("\"", "").trim(); // 有时候带了双引号，有时候带了空格。
        List<WifiConfiguration> configuredNetworks = this.wifiManager.getConfiguredNetworks();

        WifiInfo connectionInfo = this.wifiManager.getConnectionInfo(); // 获取当前已连接的wifi

        Log.i(TAG, "closeCurrentWifi: " + ((connectionInfo == null) ? "当前没有已连接的wifi" : "当前已连接的是：" + connectionInfo.toString()));
        if (connectionInfo != null && connectionInfo.getNetworkId() != -1) {
            this.wifiManager.disableNetwork(connectionInfo.getNetworkId());
            Log.i(TAG, "closeCurrentWifi: 已关闭当前wifi：" + connectionInfo.toString());
        }

        if(configuredNetworks == null || configuredNetworks.size() <= 0){
            Log.i(TAG, "closeCurrentWifi: 已配置的wifi个数是：" + configuredNetworks.size());
            return;
        }
        for (WifiConfiguration configuredNetwork : configuredNetworks) {
            if (configuredNetwork != null) Log.i(TAG, "closeCurrentWifi: 打印wifi列表 configuredNetwork.SSID=" + configuredNetwork.SSID +",BSSID="+configuredNetwork.BSSID+", networkId="+configuredNetwork.networkId+", status="+configuredNetwork.status+", priority="+configuredNetwork.priority);
        }
    }

    public void setListener(ConnectWifiListener listener) {
        this.listener = listener;
    }

    /**
     * 获取最大优先权
     */
    private int getHeighestPriority(String goalSSID) {
        int priority = -1;
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();

        if(existingConfigs != null) {
            for (WifiConfiguration existingConfig : existingConfigs) {

                if (priority <= existingConfig.priority) {
                    priority = existingConfig.priority;
                    if (!existingConfig.SSID.equals(goalSSID)) {
                        priority++;
                    }
                }
            }
        }
        return priority;
    }

    // 查看以前是否也配置过这个网络、这个网络的密码
    private WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();

        if(existingConfigs != null) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                Log.d(TAG,SSID+ "    "+existingConfig.SSID+"  wifi");
                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {

                    Log.i(TAG, "isExsits: 已配置的wifi列表里存在 " + SSID + ", 密码：" + existingConfig.preSharedKey);
                    return existingConfig;
                }
            }
        }else Log.e(TAG, "isExsits: existingConfigs is null");
        return null;
    }

    private WifiConfiguration createWifiInfo(String SSID, String Password, WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        // nopass
        if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        // wep
        if (Type == WifiCipherType.WIFICIPHER_WEP) {
            if (!TextUtils.isEmpty(Password)) {
                if (isHexWepKey(Password)) {
                    config.wepKeys[0] = Password;
                } else {
                    config.wepKeys[0] = "\"" + Password + "\"";
                }
            }
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        // wpa
        if (Type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // 此处需要修改否则不能自动重联
// config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;
    }

    // 打开wifi功能
    private boolean openWifi() {
        boolean bRet = true;
        if (!wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    /** 检查是否已连接 */
    public String[] getConnectWifiSsid(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager != null){
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
//                Log.i(TAG, "Ssid wifiManager.getConnectionInfo().toString()=" + wifiManager.getConnectionInfo().toString() +"\n");
                return new String[]{wifiInfo.getSSID().replace("\"", "").trim(), wifiInfo.getBSSID(), wifiInfo.getSupplicantState().toString()}; // 有时候带了双引号，有时候带了空格。wifiManager.getConnectionInfo()有没有可能是null?
            }
        }
        Log.e(TAG, "getConnectWifiSsid: 网络获取失败");
        return null;
    }
    /** 获取网络连接状态 */
    public SupplicantState getState(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager != null){
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                Log.i(TAG, "getState: " + wifiInfo.getSupplicantState());
                return wifiInfo.getSupplicantState(); // 有时候带了双引号，有时候带了空格。wifiManager.getConnectionInfo()有没有可能是null?
            }
        }
        Log.e(TAG, "getState: 网络获取失败");
        return null;
    }
    /** 检查BSSID是否非none，是none就是没连接上。 */
    public boolean isAPBSSIDExisted(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        Log.i(TAG, "BSSID is null?"+(wifiManager.getConnectionInfo().getBSSID() == null ? "true" : wifiManager.getConnectionInfo().getBSSID().contains(BSSIDNone))+", wifiManager.getConnectionInfo().toString()=" + wifiManager.getConnectionInfo().toString());

        if(wifiManager.getConnectionInfo()!=null){
            if(wifiManager.getConnectionInfo().getBSSID() == null || wifiManager.getConnectionInfo().getBSSID().contains(BSSIDNone)) {
                return false;
            }
            else {
                return true;
            }
        }else{
            return false;
        }

    }
    /** 断开指定wifi */
    public boolean disconnectWifi(){
        Log.d(TAG,"---------------");
        setZt(false);

//        WifiConfiguration tempConfig = isExsits(app.substring(1,app.length()-1));
//        if (tempConfig != null){
//            wifiManager.enableNetwork(tempConfig.networkId, true);
//        }


        Log.i(TAG, "disconnectWifi: mWifiOldName="+ mWifiOldName+",mWifiOldNetId="+mWifiOldNetId);
        if (mWifiOldName != null && !mWifiOldName.equals("")) {
            WifiConfiguration tempConfig = isExsits(mWifiOldName);
            if (tempConfig != null){
                boolean enabled = wifiManager.enableNetwork(tempConfig.networkId, true);
                Log.i(TAG, "disconnectAP: idGet=" + tempConfig.networkId+",是否已连上原来的wifi?"+enabled);
                return enabled;
            }
        }

        return false;

//        return wifiManager.disableNetwork(this.netID);
    }

    public boolean isZt() {
        return zt;
    }

    public void setZt(boolean zt) {
        this.zt = zt;
    }

    public static String  app="";
    private boolean zt=true;

    /** 如果已连接，就不再run */
    private boolean isStopConnect = false;
    class ConnectRunnable implements Runnable {
        private String ssid;

        private String password;

        private WifiCipherType type;

        public ConnectRunnable(String ssid, String password, WifiCipherType type) {
            this.ssid = ssid;
            this.password = password;
            this.type = type;
        }

        @Override
        public void run() {
            Log.i(TAG, "run: check is execute， isStopConnect=" + isStopConnect); // 只检测一次
            while(!isStopConnect) {
                // 打开wifi
                openWifi();
                // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
                // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
                while (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
                    try {
                        Log.i(TAG, "run: 看是不是卡在这里了");
                        // 为了避免程序一直while循环，让它睡个100毫秒检测……
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
                WifiInfo info  = wifiManager.getConnectionInfo();
                if(info != null) {
//                    oldNetworkId = info.getNetworkId();
                    if(app == null || app.equals("")||app.equals("<unknown ssid>")){
                        app=info.getSSID();
                        Log.d(TAG,app);
                    }

                }



                WifiConfiguration wifiConfig = createWifiInfo(ssid, password, type);
                wifiConfig.priority = getHeighestPriority(ssid);


//                wifiConfig.priority = 10001;
                Log.i(TAG, "createWifiInfo: config.priority=" + wifiConfig.priority + ", ssid=" + ssid + ", password=" + password + ", type=" + type); // config.priority=982, ssid=ap, password=broad608, type=WIFICIPHER_WPA, 每连接一次，就增加一次，迟早会超出极限。
                wifiManager.updateNetwork(wifiConfig);
                wifiManager.saveConfiguration();
                //
                if (wifiConfig == null) {
                    Log.e(TAG, "wifiConfig is null!");
                    return;
                }


                Log.d(TAG,"+-+-+-+-+-+-+  "+zt);
                if(!zt){
                    return;
                }

                WifiConfiguration tempConfig = isExsits(ssid);
                boolean isRemoved = false;
                if (tempConfig != null) {
                    isRemoved = wifiManager.removeNetwork(tempConfig.networkId); // 如果移除失败，则无法连接新的网络,api6.0及以上无法改变现有网络，只能重连。如果密码改变，可能只有先手动连接了。
                    Log.i(TAG, "run: "+(isRemoved ? " 已移除: " : " 未移除：") + "tempConfig.networkId=" + tempConfig.networkId +", "+ tempConfig.SSID + ", "+tempConfig.BSSID);
                }else {
                    isRemoved = true;
                }

                int netID = -1;
                if(isRemoved) {
                    netID = wifiManager.addNetwork(wifiConfig); // netID=9, netID != -1，才是连接成功了。
                }else if(tempConfig != null) {
                    netID = tempConfig.networkId; // 如果用曾经的配置，可能哪天密码改了，则可能连接不上。
                }
                boolean enabled = wifiManager.enableNetwork(netID, true); // true
                Log.d(TAG, "enableNetwork netID=" + netID + " status enable=" + enabled); // 在小米手机上总是false
                boolean connected = wifiManager.reconnect();
                Log.d(TAG, "enableNetwork connected=" + connected);
                if (enabled && connected) { // 或许可以在这里检测scanning状态，等到connected以后再listener.success()
//                    isStopConnect = true;
                    WifiConnect.this.netID = netID;
                    Log.i(TAG, "run: wifiManager.getConnectionInfo().getSSID()=" + wifiManager.getConnectionInfo().getSSID() + ", wifiConfig.SSID=" + wifiConfig.SSID);
                    listener.conWifiSuccess(wifiManager.getConnectionInfo().getSSID());
                }
                else if(count >= 10){
                    Log.e(TAG, "run: 连接wifi失败，请手动配置wifi：" + ssid + ", 密码：" + password);
                    listener.conWifiFailed();
                }else if (count < 10) {
                    try {
                        Thread.sleep(1000);
                        count++;
                        Log.i(TAG, "run: count=" + count); // 打印10次
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }
    }





    private static boolean isHexWepKey(String wepKey) {
        final int len = wepKey.length();

// WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }

        return isHex(wepKey);
    }

    private static boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; i--) {
            final char c = key.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f')) {
                return false;
            }
        }

        return true;
    }

    /** 更改连接状态，可让run重连 */
    public void setIsStopConnect(boolean isStopConnect) {
        this.isStopConnect = isStopConnect;
    }
}
