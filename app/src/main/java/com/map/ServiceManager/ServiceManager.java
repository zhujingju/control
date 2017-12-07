package com.map.ServiceManager;

import android.content.Context;
import android.util.Log;

import com.grasp.control.R;

import org.ros.exception.RemoteException;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.service.ServiceClient;
import org.ros.node.service.ServiceResponseListener;

import java.util.ArrayList;

import grobot_msgs.AddPosition;
import grobot_msgs.AddPositionRequest;
import grobot_msgs.AddPositionResponse;
import grobot_msgs.DeletePosition;
import grobot_msgs.DeletePositionRequest;
import grobot_msgs.DeletePositionResponse;
import grobot_msgs.GetAllPosition;
import grobot_msgs.GetAllPositionRequest;
import grobot_msgs.GetAllPositionResponse;
import grobot_msgs.ModifyPosition;
import grobot_msgs.ModifyPositionRequest;
import grobot_msgs.ModifyPositionResponse;
import grobot_msgs.ResetAllPosition;
import grobot_msgs.ResetAllPositionRequest;
import grobot_msgs.ResetAllPositionResponse;
import grobot_msgs.SavePosition;
import grobot_msgs.SavePositionRequest;
import grobot_msgs.SavePositionResponse;
import rocon_app_manager_msgs.GetRappList;
import rocon_app_manager_msgs.GetRappListRequest;
import rocon_app_manager_msgs.GetRappListResponse;
import rocon_app_manager_msgs.StartRapp;
import rocon_app_manager_msgs.StartRappRequest;
import rocon_app_manager_msgs.StartRappResponse;
import rocon_app_manager_msgs.StopRapp;
import rocon_app_manager_msgs.StopRappRequest;
import rocon_app_manager_msgs.StopRappResponse;
import rocon_std_msgs.KeyValue;
import rocon_std_msgs.Remapping;
import turtlebot_msgs.SetFollowStateRequest;
import turtlebot_msgs.SetFollowStateResponse;
import world_canvas_msgs.DeleteMap;
import world_canvas_msgs.DeleteMapRequest;
import world_canvas_msgs.DeleteMapResponse;
import world_canvas_msgs.ListMaps;
import world_canvas_msgs.ListMapsRequest;
import world_canvas_msgs.ListMapsResponse;
import world_canvas_msgs.PublishMap;
import world_canvas_msgs.PublishMapRequest;
import world_canvas_msgs.PublishMapResponse;
import world_canvas_msgs.ResetDatabaseResponse;
import world_canvas_msgs.SaveMap;
import world_canvas_msgs.SaveMapRequest;
import world_canvas_msgs.SaveMapResponse;

//import com.iot.remocon.R;

//import com.github.rosjava.android_remocons.a.R;

/**
 * newServiceClient()和call()分开放，可能是希望可以单独重复。
 * 不要盲目地把两个方法放到一起，可能不利于重复。
 */
public class ServiceManager extends AbstractNodeMain {
    private final String TAG = "Print-ServiceManager";
	private Context context;
	private ConnectedNode connectedNode;
//	private ServiceResponseListener<SaveMapResponse> saveServiceResponseListener;

	private String function = "";
	private String mapName;
    private String mapId;
    private String listSrvName;
    private String deleteSrvName;
    private String pubSrvName;
    /** 位置id */
	private String positionId = "";
    /** 位置名称 */
	private String positionName = "";
    private String saveSrvName;
    /** 发布位置，保存位置 */
    private String addPositionSrvName;
    /** 修改标记名称 */
    private String modifyPositionSrvName;
    /** 删除标记名称 */
    private String deletePositionSrvName;
    /** 告诉主机，可以保存位置了。不发布位置 */
    private String savePositionsSrvName;
    /** 清空所有坐标 */
    private String resetAllPositionsSrvName;
    /** 获取所有坐标 */
    private String getAllPositionsSrvName;
    /** 也许能清空激光雷达的地图数据 */
    private String resetDatabaseSrvName;

    /** rapp名称 */
    private String rappName;
    private String followerSrvName;
    private String listRappSrvName;
    private String startSrvName;
    private String stopSrvName;
    /** 跟随服务的状态 */
    private byte followerState;
    private boolean waitingFlag = false;
    /** 超时时间.
     * 删除标记、停止Rapp，10s易超时。 */
    private final int timeout = 15; //seconds
    /** 如果client返回null，则等待一段时间后再重新执行一次，希望等待之后网络会变好。只重复一次, seconds */
    private final int mWaitForAgain = 2;

    private StatusCall statusCall = null;

    private StatusCallbackSaveMap statusCallbackSaveMap;
    private StatusCallbackAddPosition statusCallbackAddPosition;
    private StatusCallbackModifyPosition statusCallbackModifyPosition;
    private StatusCallbackDeletePosition statusCallbackDeletePosition;
    private StatusCallbackSavePositions statusCallbackSavePositions;
    private StatusCallbackResetPositions statusCallbackResetPositions;
    private StatusCallbackGetPositions statusCallbackGetPositions;

    private StatusCallbackPublishMap statusCallbackPublishMap;
    private StatusCallbackListMaps statusCallbackListMaps;
    private StatusCallbackDeleteMaps statusCallbackDeleteMaps;
    private StatusCallbackResetDatabase statusCallbackResetDatabase;

    private StatusCallbackFollower statusCallbackFollower;
    private StatusCallbackGet statusCallbackGet;
    private StatusCallbackStart statusCallbackStart;
    private StatusCallbackStop statusCallbackStop;
    private abstract interface StatusCall{
        public void timeoutCallback();
        public void onFailureCallback(Exception e);
    }

    public interface StatusCallbackSaveMap  extends StatusCall{
        public void onSuccessCallback(SaveMapResponse arg0);
    }
    public interface StatusCallbackAddPosition extends StatusCall{
        public void onSuccessCallback(AddPositionResponse arg0);
    }
    public interface StatusCallbackModifyPosition extends StatusCall{
        public void onSuccessCallback(ModifyPositionResponse arg0);
    }
    public interface StatusCallbackDeletePosition extends StatusCall{
        public void onSuccessCallback(DeletePositionResponse arg0);
    }
    public interface StatusCallbackSavePositions extends StatusCall{
        public void onSuccessCallback(SavePositionResponse arg0);
    }
    public interface StatusCallbackResetPositions extends StatusCall{
        public void onSuccessCallback(ResetAllPositionResponse arg0);
    }
    public interface StatusCallbackGetPositions extends StatusCall{
        public void onSuccessCallback(GetAllPositionResponse arg0);
    }


    public interface StatusCallbackPublishMap extends StatusCall{
        public void onSuccessCallback(PublishMapResponse arg0);
    }
    public interface StatusCallbackListMaps  extends StatusCall{
        public void onSuccessCallback(ListMapsResponse arg0);
    }
    public interface StatusCallbackDeleteMaps  extends StatusCall{
        public void onSuccessCallback(DeleteMapResponse arg0);
    }
    public interface StatusCallbackResetDatabase extends StatusCall{
        public void onSuccessCallback(ResetDatabaseResponse arg0);
    }

    public abstract interface StatusCallbackFollower extends StatusCall{ // No implements clause allowed for interface
        public void onSuccessCallback(SetFollowStateResponse arg0);
    }

    public abstract interface StatusCallbackGet extends StatusCall {
        public void onSuccessCallback(GetRappListResponse arg0);
    }

    public interface StatusCallbackStart extends StatusCall {
        public void onSuccessCallback(StartRappResponse arg0);
    }

    public interface StatusCallbackStop extends StatusCall {
        public void onSuccessCallback(StopRappResponse arg0);
    }

    public ServiceManager(final Context context) {
        // Apply remappings
        this.context = context;
        saveSrvName = context.getString(R.string.save_map_srv);
        addPositionSrvName = context.getString(R.string.addPosition_srv);
        modifyPositionSrvName = context.getString(R.string.s_modifyPosition_srv);
        deletePositionSrvName = context.getString(R.string.s_deletePosition_srv);
        savePositionsSrvName = context.getString(R.string.savePositions_srv);
        resetAllPositionsSrvName = context.getString(R.string.resetPositions_srv);
        getAllPositionsSrvName = context.getString(R.string.getPositions_srv);
        resetDatabaseSrvName = context.getString(R.string.resetDatabase_srv);

        listSrvName = context.getString(R.string.list_maps_srv);
        deleteSrvName = context.getString(R.string.delete_maps_srv);
        pubSrvName = context.getString(R.string.publish_map_srv);

		mapName = "";
        followerSrvName = context.getString(R.string.s_followerSrv);
        listRappSrvName = context.getString(R.string.s_getRappSrv);
        startSrvName = context.getString(R.string.s_startRappSrv);
        stopSrvName = context.getString(R.string.s_stopRappSrv);
        rappName = "";
	}

	public void setMapName(String name) {
		mapName = name;
	}

    public void registerCallbackSaveMap(StatusCallbackSaveMap statusCallbackSaveMap) {
        this.statusCallbackSaveMap = statusCallbackSaveMap;
        this.statusCall = statusCallbackSaveMap;
    }

    public void registerCallbackSavePositions(StatusCallbackSavePositions statusCallbackSavePositions) {
        this.statusCallbackSavePositions = statusCallbackSavePositions;
        this.statusCall = statusCallbackSavePositions;
    }

    public void registerCallbackAddPosition(StatusCallbackAddPosition statusCallbackAddPosition) {
        this.statusCallbackAddPosition = statusCallbackAddPosition;
        this.statusCall = statusCallbackAddPosition;
    }

    public void registerCallbackModifyPosition(StatusCallbackModifyPosition statusCallbackModifyPosition) {
        this.statusCallbackModifyPosition = statusCallbackModifyPosition;
        this.statusCall = statusCallbackModifyPosition;
    }
    public void registerCallbackDeletePosition(StatusCallbackDeletePosition statusCallbackDeletePosition) {
        this.statusCallbackDeletePosition = statusCallbackDeletePosition;
        this.statusCall = statusCallbackDeletePosition;
    }

    public void registerCallbackResetPositions(StatusCallbackResetPositions statusCallbackResetPositions) {
        this.statusCallbackResetPositions = statusCallbackResetPositions;
        this.statusCall = statusCallbackResetPositions;
    }

    public void registerCallbackGetPositions(StatusCallbackGetPositions statusCallbackGetPositions) {
        this.statusCallbackGetPositions = statusCallbackGetPositions;
        this.statusCall = statusCallbackGetPositions;
    }

    public void registerCallbackListMaps(StatusCallbackListMaps statusCallbackListMaps) {
        this.statusCallbackListMaps = statusCallbackListMaps;
        this.statusCall = statusCallbackListMaps;
    }

    public void registerCallbackDeleteMaps(StatusCallbackDeleteMaps statusCallbackDeleteMaps) {
        this.statusCallbackDeleteMaps = statusCallbackDeleteMaps;
        this.statusCall = statusCallbackDeleteMaps;
    }

    public void registerCallbackPublishMap(StatusCallbackPublishMap statusCallbackPublishMap) {
        this.statusCallbackPublishMap = statusCallbackPublishMap;
        this.statusCall = statusCallbackPublishMap;
    }

    public void registerCallbackResetDatabase(StatusCallbackResetDatabase statusCallbackResetDatabase) {
        this.statusCallbackResetDatabase = statusCallbackResetDatabase;
        this.statusCall = statusCallbackResetDatabase;
    }



    public void registerCallbackFollower(StatusCallbackFollower statusCallbackFollower) {
        this.statusCallbackFollower = statusCallbackFollower;
        this.statusCall = statusCallbackFollower;
    }

    public void registerCallbackGet(StatusCallbackGet statusCallbackGet) {
        this.statusCallbackGet = statusCallbackGet;
        this.statusCall = statusCallbackGet;
    }
    public void registerCallbackStart(StatusCallbackStart statusCallbackStart) {
        this.statusCallbackStart = statusCallbackStart;
        this.statusCall = statusCallbackStart;
    }
    public void registerCallbackStop(StatusCallbackStop statusCallbackStop) {
        this.statusCallbackStop = statusCallbackStop;
        this.statusCall = statusCallbackStop;
    }
    public void setRappName(String name) {
        this.rappName = name;
    }
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }
    public void setFollowerState(byte followerState) {
        this.followerState = followerState;
    }
    private void clearWaitFor(){
        waitingFlag = false;
    }



    /** 超时检测 */
    private void timeoutCheck(final int timeout) {
        waitingFlag = true;
//        final AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                Log.i(TAG, "doInBackground: 开始超时计时");
//                while(waitingFlag){
//                    try { Thread.sleep(1000); }
//                    catch (InterruptedException e) {
//                        e.printStackTrace();
//                        if(statusCall == null) Log.e(TAG, "run: error, statusCall is null");
//                        else statusCall.onFailureCallback(e);
//                    }
//                }
//                return null;
//            }
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    asyncTask.get(timeout, TimeUnit.SECONDS); // 超时的时候会跳到TimeoutException。据说放到Thread里不会阻塞ui线程。
//                } catch (TimeoutException e) {
//                    e.printStackTrace();
//                    waitingFlag = false;
//                    if (statusCall == null) Log.e(TAG, "run: error, statusCall is null");
//                    else statusCall.timeoutCallback();
//                } catch (Exception e) {
//                    waitingFlag = false;
//                    e.printStackTrace();
//                    if(statusCall == null) Log.e(TAG, "run: error, statusCall is null");
//                    else statusCall.onFailureCallback(e);
//                }
                int timeoutTmp = timeout;

                while(waitingFlag && timeoutTmp > 0){
                    try {
                        Thread.sleep(1000);
                        timeoutTmp --;
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                        if(statusCall == null) Log.e(TAG, "run: error, statusCall is null");
                        else statusCall.onFailureCallback(e);
                    }
                }
            }
        }).start();

    }

    /** 保存地图 */
	public void saveMap(){
        Log.i(TAG, "saveMap()");
        ServiceClient<SaveMapRequest, SaveMapResponse> saveMapClient = null;
        if (connectedNode != null) {
            try{
//                if (nameResolverSet){
//                    saveSrvName = nameResolver.resolve(saveSrvName).toString();
//                }
                saveMapClient = connectedNode.newServiceClient(saveSrvName,	SaveMap._TYPE);
            }catch (Exception e) {
//                statusCallbackSaveMap.onFailureCallback(e);
                e.printStackTrace();

                Log.e(TAG, "saveMap: client exeption");
                if (isFirst) { // 允许重复执行一次。
                    isFirst = false;
                    try {
                        Thread.sleep(mWaitForAgain*1000);
                        Log.i(TAG, "saveMap: 重新调用saveMap()"); //
                        saveMap(); //
                        return; // 不能让后面的再执行了，否则混乱
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        statusCallbackSaveMap.onFailureCallback(e1); //
                    }
                }
                else statusCallbackSaveMap.onFailureCallback(e); //
                return; // 不要让之后再调用faile，以免重复处理
            }

            if (saveMapClient != null){
                final SaveMapRequest request = saveMapClient.newMessage();//world_canvas_msgs/SaveMap
                request.setMapName(mapName);
//                OccupancyGrid ocu;

                saveMapClient.call(request, new ServiceResponseListener<SaveMapResponse>() {
                    @Override
                    public void onSuccess(SaveMapResponse saveMapResponse) {
                        if (waitingFlag) {
                            clearWaitFor();
                            statusCallbackSaveMap.onSuccessCallback(saveMapResponse);
                        }
                    }

                    @Override
                    public void onFailure(RemoteException e) {
                        if (waitingFlag) {
                            clearWaitFor();
                            statusCallbackSaveMap.onFailureCallback(e);
                        }
                    }
                });
//                if(!waitFor(timeout)){
//                    statusCallbackSaveMap.timeoutCallback();
//                }
                timeoutCheck(timeout);
            }else{
                Log.e(TAG, "saveMap: client is null");
//                statusCallbackSaveMap.onFailureCallback(new NullPointerException("saveMap: client is null"));

                if (isFirst) { // 允许重复执行一次。
                    isFirst = false;
                    try {
                        Thread.sleep(mWaitForAgain*1000);
                        Log.i(TAG, "saveMap: 重新调用saveMap()"); //
                        saveMap(); //
                        return; // 不能让后面的再执行了，否则混乱
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        statusCallbackSaveMap.onFailureCallback(e1); //
                    }
                }
                else statusCallbackSaveMap.onFailureCallback(new NullPointerException("saveMap: client is null"));
            }

        }

	}

    /** 添加地图坐标，如门、卧室、客厅 */
    private void addPosition(){
//        Log.i(TAG, "addPosition()");

        if (connectedNode == null) {
            Log.i(TAG, "connectedNode == null");
            return;
        }

        ServiceClient<AddPositionRequest, AddPositionResponse> addPositionClient = null;
        try{
            addPositionClient = connectedNode.newServiceClient(addPositionSrvName, AddPosition
                    ._TYPE); // org.ros.exception.RosRuntimeException,未解决
        }catch (Exception e) {
//            statusCallbackAddPosition.onFailureCallback(e);

            Log.e(TAG, "addPosition: client exeption"); //
            e.printStackTrace();

            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "addPosition: 重新调用addPosition()"); //
                    addPosition(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackAddPosition.onFailureCallback(e1); //
                }
            }
            else statusCallbackAddPosition.onFailureCallback(e); //
            return; // 不要让之后再调用fail，以免重复处理
        }

        if(addPositionClient != null){
            final AddPositionRequest request = addPositionClient.newMessage();
            request.setPositionId(positionId);
//            Log.e(TAG, "addPosition: positionId=" + positionId);
            request.setPositionName(positionName);
            addPositionClient.call(request, new ServiceResponseListener<AddPositionResponse>() {
                @Override
                public void onSuccess(AddPositionResponse addPositionResponse) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackAddPosition.onSuccessCallback(addPositionResponse);
                    }
                }

                @Override
                public void onFailure(RemoteException e) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackAddPosition.onFailureCallback(e);
                    }
                }
            });
//            if(!waitFor(timeout)){
//                statusCallbackAddPosition.timeoutCallback();
//            }
            timeoutCheck(timeout);
        }else{
            Log.e(TAG, "addPosition: client is null");
//            statusCallbackAddPosition.onFailureCallback(new NullPointerException("addPosition: client is null"));

            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "addPosition: 重新调用addPosition()"); //
                    addPosition(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackAddPosition.onFailureCallback(e1); //
                }
            }
            else statusCallbackAddPosition.onFailureCallback(new NullPointerException("addPosition: client is null"));
        }
    }


    /**
     * 修改标记
     */
    public void modifyPosition() {

        if (connectedNode == null || positionId.equals("") || positionName.equals("")) {
            Log.i(TAG, "connectedNode is null, or positionId equals empty, or positionName equals empty.");
            return;
        }

        ServiceClient<ModifyPositionRequest, ModifyPositionResponse> modifyPositionClient = null;
        try{
            modifyPositionClient = connectedNode.newServiceClient(modifyPositionSrvName, ModifyPosition
                    ._TYPE); // org.ros.exception.RosRuntimeException,未解决
        }catch (Exception e) {
//            statusCallbackModifyPosition.onFailureCallback(e);

            e.printStackTrace();

            Log.e(TAG, "modifyPosition: client exeption"); //
            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "modifyPosition: 重新调用modifyPosition()"); //
                    modifyPosition(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackModifyPosition.onFailureCallback(e1); //
                }
            }
            else statusCallbackModifyPosition.onFailureCallback(e); //
            return; // 不要让之后再调用fail，以免重复处理
        }

        if(modifyPositionClient != null){
            final ModifyPositionRequest request = modifyPositionClient.newMessage();
            request.setPositionId(positionId);
//            Log.e(TAG, "addPosition: positionId=" + positionId);
//            Log.e(TAG, "modifyPosition: positionId=" + positionId);
            request.setPositionName(positionName);
            modifyPositionClient.call(request, new ServiceResponseListener<ModifyPositionResponse>() {
                @Override
                public void onSuccess(ModifyPositionResponse modifyPositionResponse) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackModifyPosition.onSuccessCallback(modifyPositionResponse);
                    }
                }

                @Override
                public void onFailure(RemoteException e) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackModifyPosition.onFailureCallback(e);
                    }
                }
            });
//            if(!waitFor(timeout)){
//                statusCallbackModifyPosition.timeoutCallback();
//            }
            timeoutCheck(timeout);
        }else{
            Log.e(TAG, "modifyPosition: client is null");
//            statusCallbackModifyPosition.onFailureCallback(new NullPointerException("modifyPosition: client is null"));
            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "modifyPosition: 重新调用modifyPosition()"); //
                    modifyPosition(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackModifyPosition.onFailureCallback(e1); //
                }
            }
            else statusCallbackModifyPosition.onFailureCallback(new NullPointerException("modifyPosition: client is null"));
        }
    }

    /**
     * 删除标记
     */
    private void deletePosition() {
        if (connectedNode == null || positionId.equals("")) {
            Log.i(TAG, "connectedNode is null, or positionId equals empty, or positionName equals empty.");
            return;
        }

        ServiceClient<DeletePositionRequest, DeletePositionResponse> deletePositionClient = null;
        try{
            deletePositionClient = connectedNode.newServiceClient(deletePositionSrvName, DeletePosition
                    ._TYPE); // org.ros.exception.RosRuntimeException,未解决
        }catch (Exception e) {
//            statusCallbackDeletePosition.onFailureCallback(e);

            e.printStackTrace();

            Log.e(TAG, "deletePosition: client exeption"); //
            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "deletePosition: 重新调用deletePosition()"); //
                    deletePosition(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackDeletePosition.onFailureCallback(e1); //
                }
            }
            else statusCallbackDeletePosition.onFailureCallback(e); //
            return; // 不要让之后再调用fail，以免重复处理
        }

        if(deletePositionClient != null){
            final DeletePositionRequest request = deletePositionClient.newMessage();
            request.setPositionId(positionId);
//            Log.e(TAG, "deletePosition: positionId=" + positionId);
            deletePositionClient.call(request, new ServiceResponseListener<DeletePositionResponse>() {
                @Override
                public void onSuccess(DeletePositionResponse deletePositionResponse) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackDeletePosition.onSuccessCallback(deletePositionResponse);
                    }
                }

                @Override
                public void onFailure(RemoteException e) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackDeletePosition.onFailureCallback(e);
                    }
                }
            });
//            if(!waitFor(timeout)){
//                statusCallbackDeletePosition.timeoutCallback();
//            }
            timeoutCheck(timeout);
        }else{
            Log.e(TAG, "deletePosition: client is null");
//            statusCallbackDeletePosition.onFailureCallback(new NullPointerException("deletePosition: client is null"));
            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "deletePosition: 重新调用deletePosition()"); //
                    deletePosition(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackDeletePosition.onFailureCallback(e1); //
                }
            }
            else statusCallbackDeletePosition.onFailureCallback(new NullPointerException("deletePosition: client is null"));
        }
    }

    /** 告诉主机可以保存坐标位置了 */
    private void savePositions(){
//        Log.i(TAG, "savePositions()");

        if (connectedNode == null) {
            Log.i(TAG, "connectedNode == null");
            return;
        }

        ServiceClient<SavePositionRequest, SavePositionResponse> savePositionsClient = null;
        try{
            savePositionsClient = connectedNode.newServiceClient(savePositionsSrvName, SavePosition
                    ._TYPE); //org.ros.exception.RosRuntimeException
        }catch (Exception e) {
//            statusCallbackSavePositions.onFailureCallback(e);

            e.printStackTrace();

            Log.e(TAG, "savePositions: client exeption"); //
            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "savePositions: 重新调用savePositions()"); //
                    savePositions(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackSavePositions.onFailureCallback(e1); //
                }
            }
            else statusCallbackSavePositions.onFailureCallback(e); //
            return; // 不要让之后再调用fail，以免重复处理
        }

        if(savePositionsClient != null){
            final SavePositionRequest request = savePositionsClient.newMessage();
            savePositionsClient.call(request, new ServiceResponseListener<SavePositionResponse>() {
                @Override
                public void onSuccess(SavePositionResponse savePositionResponse) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackSavePositions.onSuccessCallback(savePositionResponse);
                    }
                }

                @Override
                public void onFailure(RemoteException e) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackSavePositions.onFailureCallback(e);
                    }
                }
            });
//            if(!waitFor(timeout)){
//                statusCallbackSavePositions.timeoutCallback();
//            }
            timeoutCheck(timeout);
        }else{
            Log.e(TAG, "savePositions: client is null");
//            statusCallbackSavePositions.onFailureCallback(new NullPointerException("savePositions: client is null"));


            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "savePositions: 重新调用savePositions()"); //
                    savePositions(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackSavePositions.onFailureCallback(e1); //
                }
            }
            else statusCallbackSavePositions.onFailureCallback(new NullPointerException("savePositions: client is null")); //
        }
    }

    /** 清空上次的坐标位置 */
    private void resetAllPositions(){
//        Log.i(TAG, "resetAllPositions()");
        if (connectedNode == null) {
            Log.i(TAG, "connectedNode == null");
            return;
        }

        ServiceClient<ResetAllPositionRequest, ResetAllPositionResponse> resetPositionsClient = null;
        try {
            resetPositionsClient = connectedNode.newServiceClient(resetAllPositionsSrvName, ResetAllPosition._TYPE); // org.ros.exception.RosRuntimeException 导致程序崩溃
//        } catch (ServiceNotFoundException e) { // TimeoutException: No response after waiting for 10000 milliseconds.
        } catch (Exception e) {
//            statusCallbackResetPositions.onFailureCallback(e);
            Log.e(TAG, "resetAllPositions: client exeption"); //
            e.printStackTrace();

            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "resetAllPositions: 重新调用resetAllPositions()"); //
                    resetAllPositions(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackResetPositions.onFailureCallback(e1); //
                }
            }
            else statusCallbackResetPositions.onFailureCallback(e); //
            return; // 不要让之后再调用fail，以免重复处理
        }

        if(resetPositionsClient != null){
            final ResetAllPositionRequest request = resetPositionsClient.newMessage();
            resetPositionsClient.call(request, new ServiceResponseListener<ResetAllPositionResponse>() {
                @Override
                public void onSuccess(ResetAllPositionResponse resetResponse) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackResetPositions.onSuccessCallback(resetResponse);
                    }
                }

                @Override
                public void onFailure(RemoteException e) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackResetPositions.onFailureCallback(e);
                    }
                }
            });
//            if(!waitFor(timeout)){
//                statusCallbackResetPositions.timeoutCallback();
//            }
//            timeoutCheck(timeout);
            timeoutCheck(timeout);
        }else{
            Log.e(TAG, "resetAllPositions: client is null");
//            statusCallbackResetPositions.onFailureCallback(new NullPointerException("resetAllPositions: client is null"));

            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "resetAllPositions: 重新调用resetAllPositions()"); //
                    resetAllPositions(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackResetPositions.onFailureCallback(e1); //
                }
            }
            else statusCallbackResetPositions.onFailureCallback(new NullPointerException("resetAllPositions: client is null")); //
        }
    }


    /** 获取所有坐标位置 */
    private void getAllPositions(){
//        Log.i(TAG, "getAllPositions()");
        if (connectedNode == null) {
            Log.i(TAG, "connectedNode == null");
            return;
        }


        ServiceClient<GetAllPositionRequest, GetAllPositionResponse> getPositionsClient = null;
        try{
//            if(nameResolverSet){
//                getAllPositionsSrvName = nameResolver.resolve(getAllPositionsSrvName).toString();
//            }
            getPositionsClient = connectedNode.newServiceClient(getAllPositionsSrvName, GetAllPosition
                    ._TYPE);
        }catch (Exception e) {
//            statusCallbackGetPositions.onFailureCallback(e);

            Log.e(TAG, "getAllPositions: client exeption"); //
            e.printStackTrace();

            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "getAllPositions: 重新调用getAllPositions()"); //
                    getAllPositions(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackGetPositions.onFailureCallback(e1); //
                }
            }
            else statusCallbackGetPositions.onFailureCallback(e); //
            return; // 不要让之后再调用fail，以免重复处理
        }

        if(getPositionsClient != null){
            final GetAllPositionRequest request = getPositionsClient.newMessage();
            getPositionsClient.call(request, new ServiceResponseListener<GetAllPositionResponse>() {
                @Override
                public void onSuccess(GetAllPositionResponse getResponse) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackGetPositions.onSuccessCallback(getResponse);
                    }
                }

                @Override
                public void onFailure(RemoteException e) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackGetPositions.onFailureCallback(e);
                    }
                }
            });
//            if(!waitFor(timeout)){
//                statusCallbackGetPositions.timeoutCallback();
//            }
            timeoutCheck(timeout);
        }else{
            Log.e(TAG, "getAllPositions: client is null");
//            statusCallbackGetPositions.onFailureCallback(new NullPointerException("getAllPositions: client is null"));
            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "getAllPositions: 重新调用getAllPositions()"); //
                    getAllPositions(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackGetPositions.onFailureCallback(e1); //
                }
            }
            else statusCallbackGetPositions.onFailureCallback(new NullPointerException("getAllPositions: client is null")); //
        }
    }

    /** 发布地图 */
    public void publishMap() {
        ServiceClient<PublishMapRequest, PublishMapResponse> publishMapClient = null;

        if (connectedNode != null) {
            try {
                publishMapClient = connectedNode.newServiceClient(pubSrvName, PublishMap._TYPE);
            } catch (Exception e) {
//                statusCallbackPublishMap.onFailureCallback(e);
                Log.e(TAG, "publishMap: client exeption"); //
                e.printStackTrace();

                if (isFirst) { // 允许重复执行一次。
                    isFirst = false;
                    try {
                        Thread.sleep(mWaitForAgain*1000);
                        Log.i(TAG, "publishMap: 重新调用publishMap()"); //
                        publishMap(); //
                        return; // 不能让后面的再执行了，否则混乱
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        statusCallbackPublishMap.onFailureCallback(e1); //
                    }
                }
                else statusCallbackPublishMap.onFailureCallback(e); //
                return; // 不要让之后再调用fail，以免重复处理
            }

            if (publishMapClient != null) {
                final PublishMapRequest request = publishMapClient.newMessage();
                request.setMapId(mapId);
//                Log.i(TAG, "mapId=" + mapId);
                publishMapClient.call(request, new ServiceResponseListener<PublishMapResponse>(){
                    @Override
                    public void onSuccess(PublishMapResponse publishMapResponse) {
                        if (waitingFlag) {
                            clearWaitFor();
                            statusCallbackPublishMap.onSuccessCallback(publishMapResponse);
                        }
                    }

                    @Override
                    public void onFailure(RemoteException e) {
                        if (waitingFlag) {
                            clearWaitFor();
                            statusCallbackPublishMap.onFailureCallback(e);
                        }
                    }
                });
//                if(!waitFor(timeout)){
//                    statusCallbackPublishMap.timeoutCallback();
//                }
                timeoutCheck(timeout);
            }else{
                Log.e(TAG, "publishMap: client is null");
//                statusCallbackPublishMap.onFailureCallback(new NullPointerException("publishMap: client is null"));
                if (isFirst) { // 允许重复执行一次。
                    isFirst = false;
                    try {
                        Thread.sleep(mWaitForAgain*1000);
                        Log.i(TAG, "publishMap: 重新调用publishMap()"); //
                        publishMap(); //
                        return; // 不能让后面的再执行了，否则混乱
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        statusCallbackPublishMap.onFailureCallback(e1); //
                    }
                }
                else statusCallbackPublishMap.onFailureCallback(new NullPointerException("publishMap: client is null")); //
            }
        }
    }

    /** 获取地图列表 */
    public void listMaps() {
        ServiceClient<ListMapsRequest, ListMapsResponse> listMapsClient = null;
        if (connectedNode != null) {

            try {
                listMapsClient = connectedNode.newServiceClient(listSrvName, ListMaps._TYPE); // org.ros.exception.RosRuntimeException // org.ros.internal.node.DefaultNode

            }catch (Exception e) {
//                statusCallbackListMaps.onFailureCallback(e);
                Log.e(TAG, "listMaps: client exeption"); //
                e.printStackTrace();
                if (isFirst) { // 允许重复执行一次。
                    isFirst = false;
                    try {
                        Thread.sleep(2000L);
                        Log.i(TAG, "listMaps: 重新调用listMaps()");
                        listMaps();
                        return; // 不能让后面的再执行了，否则混乱
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        statusCallbackListMaps.onFailureCallback(e1);
                    }
                }
                else statusCallbackListMaps.onFailureCallback(e);
                return;
            }

            if (listMapsClient != null) {

                final ListMapsRequest request = listMapsClient.newMessage();
                listMapsClient.call(request, new ServiceResponseListener<ListMapsResponse>(){ // org.ros.internal.node.service.DefaultServiceClient
                    @Override
                    public void onSuccess(ListMapsResponse listMapsResponse) {
                        if (waitingFlag) {
                            clearWaitFor();
                            statusCallbackListMaps.onSuccessCallback(listMapsResponse);
                        }
                    }

                    @Override
                    public void onFailure(RemoteException e) {
                        if (waitingFlag) {
                            clearWaitFor();
                            statusCallbackListMaps.onFailureCallback(e);
                        }
                    }
                });
//                if(!waitFor(timeout)){
//                    statusCallbackListMaps.timeoutCallback();
//                }
                timeoutCheck(timeout); // 超时要执行好一会才执行下一个服务。
            }else{
                Log.e(TAG, "listMaps: client is null");
//                statusCallbackListMaps.onFailureCallback(new NullPointerException("listMaps: client is null"));

                if (isFirst) { // 允许重复执行一次。
                    isFirst = false;
                    try {
                        Thread.sleep(2000L);
                        Log.i(TAG, "listMaps: 重新调用listMaps()");
                        listMaps();
                        return; // 不能让后面的再执行了，否则混乱
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        statusCallbackListMaps.onFailureCallback(e1);
                    }
                }
                else statusCallbackListMaps.onFailureCallback(new NullPointerException("listMaps: client is null"));
        }
        }
    }

    /** 删除地图 */
    public void deleteMaps() {
//        Log.i(TAG, "deleteMaps()被执行");
        ServiceClient<DeleteMapRequest, DeleteMapResponse> deleteMapsClient = null;
        if (connectedNode != null) {
            try {
//                if (nameResolverSet) {
//                    deleteSrvName = nameResolver.resolve(deleteSrvName).toString();
//                }
                deleteMapsClient = connectedNode.newServiceClient(deleteSrvName, DeleteMap._TYPE);
            }catch (Exception e) {
//                statusCallbackDeleteMaps.onFailureCallback(e);

                Log.e(TAG, "deleteMaps: client exeption"); //
                e.printStackTrace();

                if (isFirst) { // 允许重复执行一次。
                    isFirst = false;
                    try {
                        Thread.sleep(mWaitForAgain*1000);
                        Log.i(TAG, "deleteMaps: 重新调用deleteMaps()"); //
                        deleteMaps(); //
                        return; // 不能让后面的再执行了，否则混乱
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        statusCallbackDeleteMaps.onFailureCallback(e1); //
                    }
                }
                else statusCallbackDeleteMaps.onFailureCallback(e); //
                return; // 不要让之后再调用fail，以免重复处理
            }
//		final ListMapsRequest request = deleteMapsClient.newMessage();

            if (deleteMapsClient != null) {
                final DeleteMapRequest request = deleteMapsClient.newMessage();
                request.setMapId(mapId);
                deleteMapsClient.call(request, new ServiceResponseListener<DeleteMapResponse>(){
                    @Override
                    public void onSuccess(DeleteMapResponse deleteMapResponse) {
                        if (waitingFlag) {
                            clearWaitFor();
                            statusCallbackDeleteMaps.onSuccessCallback(deleteMapResponse);
                        }
                    }

                    @Override
                    public void onFailure(RemoteException e) {
                        if (waitingFlag) {
                            clearWaitFor();
                            statusCallbackDeleteMaps.onFailureCallback(e);
                        }
                    }
                });
//                if(!waitFor(timeout)){
//                    statusCallbackDeleteMaps.timeoutCallback();
//                }
                timeoutCheck(timeout);
            }else{
                Log.e(TAG, "deleteMaps: client is null");
//                statusCallbackDeleteMaps.onFailureCallback(new NullPointerException("deleteMaps: client is null"));
                if (isFirst) { // 允许重复执行一次。
                    isFirst = false;
                    try {
                        Thread.sleep(mWaitForAgain*1000);
                        Log.i(TAG, "deleteMaps: 重新调用deleteMaps()"); //
                        deleteMaps(); //
                        return; // 不能让后面的再执行了，否则混乱
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        statusCallbackDeleteMaps.onFailureCallback(e1); //
                    }
                }
                else statusCallbackDeleteMaps.onFailureCallback(new NullPointerException("deleteMaps: client is null")); //
            }
        }
    }


    /** 跟随服务 */
    private void followerService(){

        if (connectedNode == null)
        {
            Log.e("FollowerActivity", "Still doesn't have a connected node");
            statusCallbackFollower.onFailureCallback(new NullPointerException("connectedNode == null"));
            return;
        }

        ServiceClient<SetFollowStateRequest, SetFollowStateResponse> serviceClient = null;
        try
        {
            serviceClient = connectedNode.newServiceClient(followerSrvName, turtlebot_msgs.SetFollowState._TYPE); //
        }catch (Exception e) {
//            statusCallbackFollower.onFailureCallback(e);

            Log.e(TAG, "followerService: client exeption"); //
            e.printStackTrace();

            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "followerService: 重新调用followerService()"); //
                    followerService(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackFollower.onFailureCallback(e1); //
                }
            }
            else statusCallbackFollower.onFailureCallback(e); //
            return; // 不要让之后再调用fail，以免重复处理
        }

        if(serviceClient != null) {
            final SetFollowStateRequest request = serviceClient.newMessage();
            request.setState(followerState);

            serviceClient.call(request, new ServiceResponseListener<SetFollowStateResponse>() {
                @Override
                public void onSuccess(SetFollowStateResponse response) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackFollower.onSuccessCallback(response);
                    }
                }

                @Override
                public void onFailure(RemoteException e) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackFollower.onFailureCallback(e);
                    }
                }
            });
//            if(!waitFor(timeout)){
//                statusCallbackFollower.timeoutCallback();
//            }
            timeoutCheck(timeout);
        }else{
            Log.e(TAG, "followerService: client is null");
//            statusCallbackFollower.onFailureCallback(new NullPointerException("followerService: client is null"));
            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "followerService: 重新调用followerService()"); //
                    followerService(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackFollower.onFailureCallback(e1); //
                }
            }
            else statusCallbackFollower.onFailureCallback(new NullPointerException("followerService: client is null")); //
        }
    }

    /** 查询已启动的Rapp */
    private void getRappList(){
        if (connectedNode == null) {
            Log.i(TAG, "connectedNode == null");
            return;
        }
        ServiceClient<GetRappListRequest, GetRappListResponse> getRappListClient = null;
        try {
            getRappListClient = connectedNode.newServiceClient(listRappSrvName, GetRappList._TYPE); // org.ros.exception.RosRuntimeException
        } catch (Exception e) {
//            statusCallbackGet.onFailureCallback(e);

            Log.e(TAG, "getRappList: client exeption"); //
            e.printStackTrace();

            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "getRappList: 重新调用getRappList()"); //
                    getRappList(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackGet.onFailureCallback(e1); //
                }
            }
            else statusCallbackGet.onFailureCallback(e); //
            return; // 不要让之后再调用fail，以免重复处理
        }

        if(getRappListClient != null){
            final GetRappListRequest request = getRappListClient.newMessage();
            getRappListClient.call(request, new ServiceResponseListener<GetRappListResponse>() {
                @Override
                public void onSuccess(GetRappListResponse getRappListResponse) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackGet.onSuccessCallback(getRappListResponse);
                    }
                }

                @Override
                public void onFailure(RemoteException e) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackGet.onFailureCallback(e);
                    }
                }
            });

//            if(!waitFor(timeout)){
//                statusCallbackGet.timeoutCallback();
//            }
            timeoutCheck(timeout);
        }else{
            Log.e(TAG, "getRappList: client is null");
//            statusCallbackGet.onFailureCallback(new NullPointerException("getRappList: client is null"));
            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "getRappList: 重新调用getRappList()"); //
                    getRappList(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackGet.onFailureCallback(e1); //
                }
            }
            else statusCallbackGet.onFailureCallback(new NullPointerException("getRappList: client is null")); //
        }
    }

    /** 启动rapp */
    private void startRapp(){
//        Log.i(TAG, "startRapp()");
        if (connectedNode == null) {
            Log.i(TAG, "connectedNode == null");
            return;
        }
        ServiceClient<StartRappRequest, StartRappResponse> startRappClient = null;
        try {
            startRappClient = connectedNode.newServiceClient(startSrvName, StartRapp._TYPE); //报错 md5Sums not match, org.ros.exception.RosRuntimeException
        } catch (Exception e) {
//            statusCallbackStart.onFailureCallback(e);
            Log.e(TAG, "startRapp: client exeption"); //
            e.printStackTrace();
            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "startRapp: 重新调用startRapp()");
                    startRapp();
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackStart.onFailureCallback(e1);
                }
            }
            else statusCallbackStart.onFailureCallback(e);
            return;
        }

        if(startRappClient != null){
            final StartRappRequest request = startRappClient.newMessage();
            request.setName(rappName);
            request.setRemappings(new ArrayList<Remapping>()); // 可以没有值
            request.setParameters(new ArrayList<KeyValue>());  // 可以没有值
            startRappClient.call(request, new ServiceResponseListener<StartRappResponse>() {
                @Override
                public void onSuccess(StartRappResponse startRappResponse) {
//                    Log.i(TAG, "startRapp onSuccess()");
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackStart.onSuccessCallback(startRappResponse);
                    }
                }

                @Override
                public void onFailure(RemoteException e) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackStart.onFailureCallback(e);
                    }
                }
            });

//            if(!waitFor(timeout)){
//                statusCallbackStart.timeoutCallback();
//            }
            timeoutCheck(timeout);
        }else{
            Log.e(TAG, "startRapp: client is null");
//            statusCallbackStart.onFailureCallback(new NullPointerException("startRapp: client is null"));

            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "startRapp: 重新调用startRapp()");
                    startRapp();
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackStart.onFailureCallback(e1);
                }
            }
            else statusCallbackStart.onFailureCallback(new NullPointerException("startRapp: client is null"));
        }
    }

    /** 重复只能是一次 */
    private boolean isFirst = true;
    /** 停止服务 */
    private void stopRapp(){
        Log.i(TAG, "stopRapp()");
        ServiceClient<StopRappRequest, StopRappResponse> stopRappClient = null;
        if (connectedNode == null) {
            Log.i(TAG, "connectedNode == null");
            statusCallbackStop.onFailureCallback(new NullPointerException("connectedNode == null"));
            return;
        }

        try {
            stopRappClient = connectedNode.newServiceClient(stopSrvName, StopRapp._TYPE); // org.ros.exception.RosRuntimeException +2
        } catch (Exception e) {
            Log.e(TAG, "stopRapp: client exeption"); //
            e.printStackTrace();
            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "stopRapp: 重新调用stopRapp()"); //
                    stopRapp(); //
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackStop.onFailureCallback(e1); //
                }
            }
            else statusCallbackStop.onFailureCallback(e); //
            return;
        }

        if(stopRappClient != null){
            Log.i(TAG, "stopRapp: call");
            final StopRappRequest request = stopRappClient.newMessage();
            stopRappClient.call(request, new ServiceResponseListener<StopRappResponse>() {
                @Override
                public void onSuccess(StopRappResponse stopRappResponse) {
                    Log.i(TAG, "stopRapp onSuccess()");
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackStop.onSuccessCallback(stopRappResponse);
                    }
                }

                @Override
                public void onFailure(RemoteException e) {
                    if (waitingFlag) {
                        clearWaitFor();
                        statusCallbackStop.onFailureCallback(e);
                    }
                }
            });

//            if(!waitFor(timeout * 3)){ // 停止Rapp需要的时间长
//                statusCallbackStop.timeoutCallback();
//            }

            timeoutCheck(timeout * 3);
        }
        else {
            Log.e(TAG, "stopRapp: client is null");
            if (isFirst) { // 允许重复执行一次。
                isFirst = false;
                try {
                    Thread.sleep(mWaitForAgain*1000);
                    Log.i(TAG, "stopRapp: 重新调用stopRapp()");
                    stopRapp();
                    return; // 不能让后面的再执行了，否则混乱
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                    statusCallbackStop.onFailureCallback(e1);
                }
            }
            else statusCallbackStop.onFailureCallback(new NullPointerException("stopRapp: client is null"));
        }
    }
//    /** 也许可以清空激光雷达的数据：不可以 */
//    private void resetDatabase() {
//        ServiceClient<ResetDatabaseRequest, ResetDatabaseResponse> resetDatabaseClient = null;
//        if (connectedNode != null) {
//            try {
////                if (nameResolverSet) {
////                    resetDatabaseSrvName = nameResolver.resolve(resetDatabaseSrvName).toString();
////                }
//                resetDatabaseClient = connectedNode.newServiceClient(resetDatabaseSrvName, ResetDatabase._TYPE);
//            } catch (ServiceNotFoundException e) {
//                try {
//                    Thread.sleep(1000L);
//                    resetDatabase();
//                    return;
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//
////                throw new RosRuntimeException(e);
//                statusCallbackResetDatabase.onFailureCallback(e);
//            } catch (RosRuntimeException e) {
//                statusCallbackResetDatabase.onFailureCallback(e);
//            } catch (Exception e) {
//                statusCallbackResetDatabase.onFailureCallback(e);
//            }
//        }
//
//        if (resetDatabaseClient != null) {
//            final ResetDatabaseRequest request = resetDatabaseClient.newMessage();
//            resetDatabaseClient.call(request, new ServiceResponseListener<ResetDatabaseResponse>(){
//                @Override
//                public void onSuccess(ResetDatabaseResponse resetDatabaseResponse) {
//                    if (waitingFlag) {
//                        clearWaitFor();
//                        statusCallbackResetDatabase.onSuccessCallback(resetDatabaseResponse);
//                    }
//                }
//
//                @Override
//                public void onFailure(RemoteException e) {
//                    if (waitingFlag) {
//                        clearWaitFor();
//                        statusCallbackResetDatabase.onFailureCallback(e);
//                    }
//                }
//            });
////            if(!waitFor(timeout)){
////                statusCallbackResetDatabase.timeoutCallback();
////            }
//            timeoutCheck(timeout);
//        }else{
//            Log.e(TAG, "resetDatabase: client is null");
//            statusCallbackResetDatabase.onFailureCallback(new NullPointerException("resetDatabase: client is null"));
//        }
//    }


    public ConnectedNode getConnectedNode() {
        return connectedNode;
    }

    @Override
	public GraphName getDefaultNodeName() {

		return null;
	}
    @Override
	public void onStart(final ConnectedNode connectedNode) {
        super.onStart(connectedNode);
        this.connectedNode = connectedNode;
        if(function.equals("")){
            Log.e(TAG, "function is empty.");
            return;
        }

        if (function.equals(context.getString(R.string.s_functionName_publish))) {
            publishMap();
        } else if (function.equals(context.getString(R.string.s_functionName_list))) {
            listMaps();
        } else if (function.equals(context.getString(R.string.s_functionName_delete))) {
            deleteMaps();
        }else if(function.equals(context.getString(R.string.s_functionName_SaveMap))){
            saveMap();
        }
        else if(function.equals(context.getString(R.string.s_functionName_AddPosition))){
            addPosition();
        }
        else if(function.equals(context.getString(R.string.s_functionName_ModifyPosition))) {
            modifyPosition();
        }
        else if(function.equals(context.getString(R.string.s_functionName_DeletePosition))) {
            deletePosition();
        }
        else if(function.equals(context.getString(R.string.s_functionName_SavePositions))){
            savePositions();
        }
        else if(function.equals(context.getString(R.string.s_functionName_ResetAllPositions))){
            resetAllPositions();
        }
        else if(function.equals(context.getString(R.string.s_functionName_GetAllPositions))){
            getAllPositions();
        }
//        else if(function.equals(context.getString(R.string.s_functionName_ResetDatabase))){
//            resetDatabase();
//        }
        else if(function.equals(context.getString(R.string.s_getRappList))){ //查询
            getRappList();
        }
        else if (function.equals(context.getString(R.string.s_startRapp))) { //启动
            startRapp();
        }
        else if (function.equals(context.getString(R.string.s_stopRapp))) { // 停止
            stopRapp();
        }
        else if(function.equals(context.getString(R.string.s_functionName_follower))){
            followerService();
        }
        else {
            Log.e(TAG, "onStart: 未知function="+function);
        }

    }
    private static boolean isCancelled = false;

}
