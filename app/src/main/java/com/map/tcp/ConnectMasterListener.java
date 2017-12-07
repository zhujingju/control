package com.map.tcp;

import com.github.rosjava.android_remocons.common_tools.master.RoconDescription;

/**
 * Created by Administrator on 2017/3/23-023.
 */
public interface ConnectMasterListener {
    String ERROR_WIFI = "failed to connect wifi";
    void success(RoconDescription roconDescription, String SSID);
    void failed(String message);
    void failed(ConnectMaster.ConnResult result);
}
