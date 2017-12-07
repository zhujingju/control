package com.map.tcp;

/** 用来监听是否已连接Master */
public interface ConnectWifiListener {
    void conWifiSuccess(String SSID);
    void conWifiFailed();
}

