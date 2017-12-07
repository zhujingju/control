package com.map;

import android.util.Log;

import com.map.ServiceManager.ServiceManager;

import org.ros.node.ConnectedNode;

/**
 * 远程连接的状态
 * Created by Administrator on 2017/3/20-020.
 */
public class RemoconStatus {
    private final String TAG = "Print-RStatus";
    /** 是否在运行Rapp */
    private boolean isRunningRapp;
    /** 正在运行的Rapp的名称 */
    private String runningRappName;
    /** 地图控件是否显示了地图 */
    private boolean isReceivedMap;
    /** 视频控件是否显示了视频 */
    private boolean isReceivedCamera;
    /** 跟随状态 */
    private boolean isFollow = false;
    /** 将要打开的rappName */
    private String willRunRappName;
    /** 下一个操作 */
    private String nextAction;
    /** BackButton按钮退出事件 */
    public static final String Action_Exit_BackBtn = "Action_Exit_BackBtn";
    /** BackKey退出事件 */
    public static final String Action_Exit_BackKey = "Action_Exit_BackKey";
    /** 保存地图成功后，启动地图导航：保存地图-保存位置-停止构建地图-获取地图-发布地图-打开地图导航 */
    public static final String Action_SavedMap_StartNav = "Action_SavedMap_StartNav";
    /** 非人为，触发了onPause退出事件 */
    public static final String Action_Exit_OnPause = "Action_Exit_OnPause";
    /** 异常退出 */
    public static final String Action_Exit_Exception = "Action_Exit_Exception";

    /** 删除地图和位置坐标，打开构建地图 */
    public static final String Action_LongClick_StartMapMake = "Action_LongClick_StartMapMake";
    /** 启动地图导航，获取地图-发布地图-启动地图导航 */
    public static final String Action_LongClick_StartMapNav = "Action_LongClick_StartMapNav";
    /** 只是清理坐标 */
    public static final String Action_ResetPositionsOnly = "Action_ResetPositionsOnly";
    /** 重新新建地图 */
    public static final String Action_ReNewMap = "Action_ReNewMap";
    /** 刚进入Remocon时，上次已启动了导航，则发布地图，发布以后什么也不做，发布失败，就停止导航 */
    public static final String Action_InitRemocon_RunningMapNav_PublishMap = "Action_InitRemocon_RunningMapNav_PublishMap";
    /** 刚进入remocon */
    public static final String Action_InitRemocon = "InitRemocon";
    /** 只是停止rapp */
    public static final String Action_stopRappOnly = "Action_stopRappOnly";
    /** 保存标记 */
    public static final String Action_savePositions = "Action_savePositions";
    /** 刷新后，注入nodemain，并打开地图导航 */
    public static final String Action_refresh_Nav_inject = "Action_refresh_Nav_inject";
    /** 测试 */
    public static final String Action_Test = "Action_Test";

    /** 错误代码 */
    /** 停止rapp时报错 */
    public static final String ErrorMsg_StopRappWithNoRunning = "but no rapp found running"; //Tried to stop a rapp, but no rapp found running.

    /** 启动rapp用的的RappManager，为了下次启动的时候好注入数据 */
    private ServiceManager serviceManager;

    /** 启动rapp的ConnectedNode，为了下次启动的时候好注入数据 */
    private ConnectedNode connectedNode;

    public RemoconStatus(){
       initData();
    }

    /** 初始化数据 */
    public void initData(){
        isRunningRapp = false;
        runningRappName = "";
        isReceivedMap = false;
        isReceivedCamera = false;
        willRunRappName = "";
        nextAction = "";
    }

    /** 是否在运行某个Rapp，true-正在运行，false-未运行 */
    public boolean isRunning(String rappName){
        if(!this.isRunningRapp) return false;
        else return (this.runningRappName.equals(rappName));
    }

    public boolean isRunningRapp() {
        Log.i(TAG, "isRunningRapp: isRunningRapp="+isRunningRapp+",runningRappName="+runningRappName);
        if (isRunningRapp && runningRappName != null && !runningRappName.equals("")) { // 有正在运行的rapp
        }
        else if(!isRunningRapp && runningRappName.equals("")){} // 没有正在运行的Rapp
        else {
            Log.e(TAG, "isRunningRapp: 出错了");
        }
        return isRunningRapp;
    }

    public void setIsRunningRapp(boolean isRunningRapp) {
        this.isRunningRapp = isRunningRapp;
    }

    public String getRunningRappName() {
        return runningRappName;
    }

    public void setRunningRappName(String runningRappName) {
        this.runningRappName = runningRappName;
        if(runningRappName != null && !runningRappName.equals("")) isRunningRapp = true;
        else isRunningRapp = false;
    }

    public boolean isReceivedMap() {
        return isReceivedMap;
    }

    public void setIsReceivedMap(boolean isReceivedMap) {
        this.isReceivedMap = isReceivedMap;
    }

    public boolean isReceivedCamera() {
        return isReceivedCamera;
    }

    public void setIsReceivedCamera(boolean isReceivedCamera) {
        this.isReceivedCamera = isReceivedCamera;
    }

    public String getWillRunRappName() {
        return willRunRappName;
    }

    public void setWillRunRappName(String willRunRappName) {
        this.willRunRappName = willRunRappName;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
        if(nextAction.equals("")) Log.e(TAG, "setNextAction: "+nextAction);
        else Log.i(TAG, "setNextAction: "+nextAction);
    }
    /** 下一个是不是某个操作 */
    public boolean isNextAction(String nextAction){
        return (this.nextAction.equals(nextAction));
    }

    public ConnectedNode getConnectedNode() {
        return connectedNode;
    }

    public void setConnectedNode(ConnectedNode connectedNode) {
        this.connectedNode = connectedNode;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }
}
