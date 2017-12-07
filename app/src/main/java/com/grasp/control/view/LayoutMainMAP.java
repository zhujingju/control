package com.grasp.control.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rosjava.android_remocons.common_tools.master.RoconDescription;
import com.google.common.collect.Lists;
import com.grasp.control.R;
import com.map.PoseCustom;
import com.map.PublisherSubscriber;
import com.map.RemoconStatus;
import com.map.ServiceManager.ServiceManager;
import com.map.View.BitmapShape;
import com.map.VirtualJoystick_extends;
import com.map.dialogs.DialogCustom;
import com.map.dialogs.MyDialog;
import com.map.make_a_map.FlagBean;
import com.map.make_a_map.OccupancyGridLayerCustom;
import com.map.make_a_map.ReceiveMapListener;
import com.map.map_nav.MapPosePublisherLayer;
import com.map.map_nav.PoseSubscriberLayerCustom;
import com.map.tcp.ConnectMaster;
import com.map.tcp.ConnectMasterListener;

import org.ros.android.NodeMainExecutorService;
import org.ros.android.RosFragment;
import org.ros.android.view.VirtualJoystickView;
import org.ros.android.view.visualization.VisualizationView;
import org.ros.android.view.visualization.layer.CameraControlLayer;
import org.ros.android.view.visualization.layer.LaserScanLayer;
import org.ros.android.view.visualization.layer.Layer;
import org.ros.android.view.visualization.layer.RobotLayer;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeListener;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;

import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import grobot_msgs.AddPositionResponse;
import grobot_msgs.DeletePositionResponse;
import grobot_msgs.GetAllPositionResponse;
import grobot_msgs.ModifyPositionResponse;
import grobot_msgs.ResetAllPositionResponse;
import grobot_msgs.SavePositionResponse;
import grobot_msgs.VoiceLocalsEntry;
import rocon_app_manager_msgs.GetRappListResponse;
import rocon_app_manager_msgs.Rapp;
import rocon_app_manager_msgs.StartRappResponse;
import rocon_app_manager_msgs.StopRappResponse;
import turtlebot_msgs.SetFollowStateRequest;
import turtlebot_msgs.SetFollowStateResponse;
import world_canvas_msgs.DeleteMapResponse;
import world_canvas_msgs.ListMapsResponse;
import world_canvas_msgs.MapListEntry;
import world_canvas_msgs.PublishMapResponse;
import world_canvas_msgs.SaveMapResponse;

import static com.grasp.control.view.LayoutMainMAP.ConnResult.timeout;
import static com.map.RemoconStatus.Action_InitRemocon;
import static com.map.RemoconStatus.Action_InitRemocon_RunningMapNav_PublishMap;
import static com.map.RemoconStatus.Action_LongClick_StartMapNav;
import static com.map.RemoconStatus.Action_SavedMap_StartNav;
import static com.map.RemoconStatus.Action_refresh_Nav_inject;
import static com.map.dialogs.DialogCustom.ResultName.R_Cancel;

/**
 * Created by zhujingju on 2017/7/20.
 * setHideMapControl(boolean) 是否隐藏地图操作按钮，给Activity用的
 */

public class LayoutMainMAP extends RosFragment {
    private final String TAG = "Print-MAP";
    @BindView(R.id.id_joystick)
    VirtualJoystick_extends id_joystick;
    @BindView(R.id.id_newMap)
    ImageView id_newMap;
    @BindView(R.id.id_saveMap)
    ImageView id_saveMap;
    @BindView(R.id.id_menTing)
    ImageView id_menTing;
    @BindView(R.id.id_qianTing)
    ImageView id_qianTing;
    @BindView(R.id.id_huiYiTing)
    ImageView id_huiYiTing;
    @BindView(R.id.id_banGongQu)
    ImageView id_banGongQu;
    @BindView(R.id.id_customName)
    ImageView id_customName;
    @BindView(R.id.id_saveFlag)
    ImageView id_saveFlag;
    @BindView(R.id.id_mapAddRoute)
    ImageView id_mapAddRoute;
    @BindView(R.id.id_map)
    VisualizationView id_map;
    @BindView(R.id.id_mapEstablish)
    ImageView id_mapEstablish;
    @BindView(R.id.id_mapNavigation)
    ImageView id_mapNavigation;
    @BindView(R.id.id_mapSign)
    ImageView id_mapSign;
    @BindView(R.id.id_mapFollow)
    ImageView id_mapFollow;
    @BindView(R.id.id_mapStartingpoint)
    ImageView id_mapStartingpoint;
    @BindView(R.id.id_mapArrivepoint)
    ImageView id_mapArrivepoint;
    @BindView(R.id.id_mapRoute)
    ImageView id_mapRoute;
    @BindView(R.id.id_mapRefresh)
    ImageView id_mapRefresh;
    @BindView(R.id.id_mapTeleop)
    ImageView id_mapTeleop;
    @BindView(R.id.id_mapLayoutBottomBar)
    LinearLayout id_mapLayoutBottomBar;
    @BindView(R.id.id_mapConnectError)
    TextView id_mapConnectError;
    @BindView(R.id.id_progressDialogView)
    ProgressBar id_progressDialogView;
    @BindView(R.id.rl_progressBar)
    RelativeLayout rl_progressBar;

    @BindView(R.id.id_layout_map_route)
    LinearLayout id_layout_map_route;

    @BindView(R.id.id_layout_map_sign)
    LinearLayout id_layout_map_sign;

    @BindView(R.id.id_layout_map_makemap)
    LinearLayout id_layout_map_makemap;
    private Context context;

    Unbinder unbinder;
    /** 手柄 */
//    @BindView(R.id.id_joystick)
//     VirtualJoystick_extends id_joystick;
//    @BindView(R.id.id_map)
//    /** 地图 */
//     VisualizationView id_map;
//
//
//    @BindView(R.id.id_mapEstablish)
//    /** 地图构建唤醒 */  ImageView id_mapEstablish;
//    @BindView(R.id.id_mapNavigation)
//    /** 地图导航唤醒 */
//     ImageView id_mapNavigation;
//    @BindView(R.id.id_mapFollow)
//    /** 跟随唤醒 */
//     ImageView id_mapFollow;
//    @BindView(R.id.id_mapRefresh)
//    /** 刷新 */
//     ImageView id_mapRefresh;
//    @BindView(R.id.id_menTing)
//    /** 门 */
//     ImageView id_menTing;
//    @BindView(R.id.id_qianTing)
//    /** 客厅 */
//     ImageView id_qianTing;
//    @BindView(R.id.id_huiYiTing)
//    /** 卧室 */
//     ImageView id_huiYiTing;
//    @BindView(R.id.id_banGongQu)
//    /** 厨房 */
//     ImageView id_banGongQu;
////    @BindView(R.id.id_toilet)
////    /** 卫生间 */
////     ImageView id_toilet;
//    @BindView(R.id.id_customName)
//    /** 自定义标记名称 */
//     ImageView id_customName;
//    @BindView(R.id.id_mapSign)
//    /** 编辑标记 */
//     ImageView id_mapSign;
//    @BindView(R.id.id_saveFlag)
//    /** 保存标记 */
//     ImageView id_saveFlag;
//    @BindView(R.id.id_saveMap)
//    /** 保存地图 */
//     ImageView id_saveMap;
//    @BindView(R.id.id_newMap)
//    /** 构建新地图 */
//     ImageView id_newMap;
//    @BindView(R.id.id_mapStartingpoint)
//    /** 地图导航-初始定位 */
//     ImageView id_mapStartingpoint;
//    @BindView(R.id.id_mapArrivepoint)
//    /** 地图导航-目标定位 */
//     ImageView id_mapArrivepoint;
//    @BindView(R.id.id_mapRoute)
//    /** 路径 */
//     ImageView id_mapRoute;
//    @BindView(R.id.id_mapTeleop)
//    /** 手柄显示、隐藏 */
//     ImageView id_mapTeleop;
//    @BindView(R.id.id_mapAddRoute)
//    /** 添加路径 */
//     ImageView id_mapAddRoute;
//    @BindView(R.id.id_layout_map_makemap)
//    /** 构建地图弹出框 */
//    LinearLayout id_layout_map_makemap;
//    @BindView(R.id.id_layout_map_sign)
//    /** 标记弹出框 */
//    LinearLayout id_layout_map_sign;
//    @BindView(R.id.id_layout_map_route)
//    /** 路径弹出框  */
//    LinearLayout id_layout_map_route;
//
//    @BindView(R.id.id_mapConnectError)
//    /** 提示：无法连接到wifi？无法连接到ros？ */
//    TextView id_mapConnectError;
//    /** 转圈圈 */
//    @BindView(R.id.rl_progressBar)
//    RelativeLayout rl_progressBar;
//    @BindView(R.id.id_progressDialogView)
//    /** 转圈圈 */
//    ProgressBar id_progressDialogView;
//    @BindView(R.id.id_mapLayoutBottomBar)
//    /** 底部按钮条 */
//    LinearLayout id_mapLayoutBottomBar;
    /////////////////////////////////////////////////

    /**
     * 白地图层
     */
    private OccupancyGridLayerCustom occupancyGridLayer;
    /**
     * 发布消息、订阅消息
     */
    private PublisherSubscriber mPublisherSubscriber;
//    /** 保存从ROS主机获取到的rapp */
//    private ArrayList<Interaction> availableAppsCache;
    /**
     * 地图信息
     */
    private List<MapListEntry> mListMaps = null;
    /**
     * 地图导航，发布初始定位和目标定位
     */
    private MapPosePublisherLayer mapPosePublisherLayer;
    /**
     * 收消息：初始位置、目标位置
     */
    private PoseSubscriberLayerCustom poseSubscriberLayerCustom;

    private final int positionIdFixed_ = 10000; //
    private final int positionIdCustom_ = 10999;
    /**
     * 固定地点 从10001开始，10000是原点。
     */
    private int positionIdFixed = positionIdFixed_; //
    /**
     * 自定义地点从11000开始
     */
    private int positionIdCustom = positionIdCustom_;

    /**
     * 屏幕尺寸：px
     */
    private int mScreenWidthPx = 0;
    private int mScreenHeightPx = 0;

    private NodeConfiguration nodeConfiguration;
    private NodeMainExecutor nodeMainExecutor;
    /**
     * Rapp运行状态
     */
    private RemoconStatus status = null;

    /**
     * 最后一个坐标的值
     */
    private PoseCustom lastPose;

    /**
     * 保存原来的wifi，断开后继续连接，以后不可改变。必须在第一次连接之前就获取，之后不可再获取。
     */
    public static String mWifiOldName;
    public static int mWifiOldNetId;
    private static String mWifiName = "iotbroad_123";
    private static String mWifiPwd = "broad608";
    // wifi end

    /**
     * 连接结果：成功、失败、异常、超时
     */
    protected enum ConnResult {
        success, failed, exception, timeout, NoFollowService
    }

    /**
     * execute服务的nodeName
     */
    private enum NodeName {
        camera_view, virtual_joystick, get_rapp_list, list_maps, publish_map, follower, delete_map, get_all_positions, reset_all_positions, save_map, add_position, modify_position, delete_position, save_positions, start_rapp, stop_rapp, map_view, PublisherSubscriber
    }

    /**
     * 启动Rapp的名字
     */
    private final String RappNameMakeAMap = "grobot_rapps/make_a_map";
    private final String RappNameMapNav = "grobot_rapps/map_nav";

    /**
     * 存放要发布的mapId，在地图导航开启之前先检测有没有地图，有才能开启。
     */
    private String mapId = null;
    /**
     * 是否在充电桩，在充电桩才可以启动构建地图
     */
    private boolean isAtChargingPile = true; //先写在充电桩
    /**
     * 网络超时
     */
    private boolean waitingFlag = false;
    /**
     * Concert是否初始化成功
     */
    private boolean validatedConcert;
    /**
     * 从连接界面来的
     */
    private boolean fromConnectActivity = false;
    /**
     * 位置id是固定的还是自定义的
     */
    private boolean isPositionIdFixed = false;

    /**
     * 是否继续等待地图
     */
    private boolean isWaitforMap = false;
    /**
     * 等待地图的标志，可增加
     */
    private static int mWaitMapFlag = 0;
    /**
     * 是否已执行完获取标记
     */
    private boolean isExecutedGetPositions = false;
    /**
     * 是否要隐藏不需要的控件
     */
    private boolean mIsHideMapControl = false;

//    private PosePublisherLayerCustom mPosePublisherLayerCustom;

    private AlertDialog notiDialog;
    private Dialog notiDialogCustom;
    /**
     * 显示自定义对话框
     */
    private MyDialog dialogName = null;

    private RoconDescription roconDescription;

    /**
     * handler的what值
     */
    private final int Handler_Toast = 0x200;
    private final int Handler_Dialog = 0x202;
    private final int Handler_Dialog_Timeout = 0x203;
    private final int mHandler_showDialogDelete = 0x204;
    private final int mHandler_connectRappFailure = 0x206;
    /**
     * 设置背景
     */
    private final int mHandler_MapMakeBackground = 0x208;
    private final int mHandler_MapNavBackground = 0x210;
    /**
     * 设置长按事件对应的背景色
     */
    private final int mHandler_LongClickBg = 0x211;
    /**
     * 设置控件隐藏、显示
     */
    private final int mHandler_MapMakeViewGone = 0x212;
    private final int mHandler_MapNavViewGone = 0x214;
//    /** 刷新主机连接 */
//    private final int mHandler_refreshMaster = 0x216;
    /**
     * 设置跟随按钮的背景图
     */
    private final int mHandler_btnFollowerBG = 0x218;
    /**
     * 清理摄像头显示
     */
    private final int mHandler_clearCameraView = 0x220;
    /**
     * 显示摄像头
     */
    private final int mHandler_displayCameraView = 0x222;
    /**
     * 修改标记
     */
    private final int mHandler_modifyPosition = 0x224;
    /**
     * 取消去目的地
     */
    private final int mHandler_cancelGoal = 0x226;
    /**
     * 取消目的地成功
     */
    private final int mHandler_cancelGoalOK = 0x228;
    /**
     * 刷新
     */
    private final int mHandler_refresh = 0x230;
    /**
     * 长按：构建地图
     */
    private final int mHandler_longClick_makeAMap = 0x232;
    /**
     * 长按：地图导航
     */
    private final int mHandler_longClick_mapNavigation = 0x234;
    /**
     * 等待地图超时
     */
    private final int mHandler_TimeoutForWaitMap = 0x236;
    /**
     * 给手柄传handler数据用的
     */
    private final int mHandler_Teleop = 0x238;
    // 来自ConnectActivity的handler what
    private final int mHandler_error_exit = 0x240;
    private final int mHandler_setTipsText = 0x242;
    /**
     * 手动填写wifi
     */
    private final int mHandler_setWifi = 0x244;
    /**
     * 链接ros失败时，显示TextView
     */
    private final int mHandler_mapConnectError = 0x246;
    /**
     * 自定义对话框返回结果处理
     */
    private final int mHandler_DialogResultCustomFlagName = 0x248;
    /**
     * 修改标记名称对话框返回结果处理
     */
    private final int mHandler_DialogResultModifyFlagName = 0x250;
    /**
     * 是否重新构建地图对话框返回结果处理
     */
    private final int mHandler_DialogResultIfNewMap = 0x252;
    /**
     * 设置wifi名称和密码 对话框返回结果处理
     */
    private final int mHandler_DialogResultSetWifi = 0x254;


    /**
     * 清理数据用的超时clearExecute
     */
    private final int mTimeout = 45;
    /**
     * 等待地图超时时间,秒
     */
    private final int mTimeoutWaitMap = 60;
    /**
     * 跟随服务目标状态
     */
    private byte mFollowerNewState;

    private Handler mHandler = null;
    /**
     * 暂时存储一些String数据
     */
    private StringBuilder sbTemp = null;

    private WifiManager wifi;
    /** 状态 */
    private boolean zt_zhuJingJu = false;

    /**
     * 是否已初始化
     */
    private boolean isInitNodeMain = false;
    private SharedPreferences sp;

    private boolean isConnected = false;
    private int inetAddress;
    /**
     * 连接wifi和主机
     */
    private ConnectMaster connectMaster = null;
    ////////////////////////////////////////////////////////////////////////////
    private static LayoutMainMAP mLayoutMainMAP;

    /************************************************ 父类方法区 start *******************************************************************/
    /**
     * 要调用这个方法
     */
    public static LayoutMainMAP getInstance() {
        if (mLayoutMainMAP == null) mLayoutMainMAP = new LayoutMainMAP();
        return mLayoutMainMAP;
    }

    /**
     * 不要调用这个方法
     */
    public LayoutMainMAP() {
        super("远程控制", "远程控制");
        Log.i(TAG, "LayoutMainMAP()");
        status = new RemoconStatus();


//        isBackKeyClicked = false;
//        isExit = false;
//        isSavedMap = false;

        mListMaps = new ArrayList<MapListEntry>();

        sbTemp = new StringBuilder();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.e(TAG, "onAttach: ");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_main_map, container, false);
        context = getActivity();
        super.setmContext(context);

        unbinder = ButterKnife.bind(this, view);

        wifi = ((WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE));
//        id_mapLayoutRight.setVisibility(mIsHideMapControl ? View.GONE : View.VISIBLE);
        id_mapLayoutBottomBar.setVisibility(mIsHideMapControl ? View.GONE : View.VISIBLE);
        id_joystick.setVisibility(mIsHideMapControl ? View.GONE : View.VISIBLE);

        initUI(view);
        initHandler();
        initListener();
//        if (context.getIntent() != null) { // 从连接界面来的。 ----------不写连接界面了
//            if (getIntent().hasExtra(RoconDescription.UNIQUE_KEY)) {
//                fromConnectActivity = true;
//            }
//            if (getIntent().hasExtra("mWifiName")) {
//                mWifiName = getIntent().getStringExtra("mWifiName");
//                Log.i(TAG, "onCreate: 从intent获取mWifiName的值：" + mWifiName);
//            }
//            if (getIntent().hasExtra("mWifiPwd")) {
//                mWifiPwd = getIntent().getStringExtra("mWifiPwd");
//                Log.i(TAG, "onCreate: 从intent获取mWifiPwd的值：" + mWifiPwd);
//            }
//        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
        zt_zhuJingJu = false;
    }
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser){ // 此方法没有执行过
//        Log.i(TAG, "setUserVisibleHint: isVisibleToUser="+isVisibleToUser);
//        if (isVisibleToUser) {
//            setWifi();
//        }
//    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        Log.i(TAG, "init(NodeMainExecutor)");
        isInitNodeMain = false;
//        if(this.nodeMainExecutor != null && nodeMainExecutor != null){ // 新旧nodeMainExecutor是一个对象，即使重连，原因未知。
//            Log.i(TAG, "init: this.nodeMainExecutor.toString()=" + this.nodeMainExecutor.toString() + ", nodeMainExecutor.toString()=" + nodeMainExecutor.toString());
//        }
        this.nodeMainExecutor = nodeMainExecutor;
        try {
            Socket socket = new Socket(getMasterUri().getHost(), getMasterUri().getPort()); // 用这种方式获取数据，可捕获异常，捕获到了异常就可以保证界面不崩溃。
            NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(socket.getLocalAddress().getHostAddress(), getMasterUri());
            Log.i(TAG, "init: socket.getLocalAddress().getHostAddress()=" + socket.getLocalAddress().getHostAddress() + ",nodeConfiguration=" + nodeConfiguration);
//            Log.i(TAG, "init: this.nodeConfiguration.toString()=" + (this.nodeConfiguration == null ? "null" : this.nodeConfiguration.toString()) + ", nodeConfiguration.toString()=" + nodeConfiguration.toString()); // nodeConfiguration，如果主机minimal不重连，每次都不是同一个对象
            this.nodeConfiguration = nodeConfiguration;

            if (id_joystick == null || id_map == null) {
                Log.e(TAG, "init: 界面未初始化, id_joystick is null or id_map is null");
                safeDismissWaitingDialog();
                return;
            }
            boolean isShouldRappList = (id_joystick.getConnectedNode() == null || id_map.getConnectedNode() == null || mPublisherSubscriber == null); // 任何一个为空，都表示重启了，应该重新获取ros状态。
            try {
                if (id_joystick.getConnectedNode() == null)
                    nodeMainExecutor.execute(id_joystick, nodeConfiguration.setNodeName(NodeName.virtual_joystick.toString()), getNodeList(NodeName.virtual_joystick, status.getNextAction(), id_joystick)); // execute 只是传递数据，而不是处理。// "android/virtual_joystick"
                else Log.e(TAG, "init: id_joystick.getConnectedNode() != null，这里没有再次初始化");

                if (id_map.getConnectedNode() == null) {
                    id_map.init(nodeMainExecutor);
                    nodeMainExecutor.execute(id_map, nodeConfiguration.setNodeName(NodeName.map_view.toString()), getNodeList(NodeName.map_view, status.getNextAction(), id_map)); // getStringLocal(R.string.map_nodeName)
                } else Log.e(TAG, "init: id_map.getConnectedNode() != null，这里没有再次初始化");
//                if(mPublisherCheck == null){
//                    mPublisherCheck = PublisherCheck.getInstance();
//                }+
//                nodeMainExecutor.execute(mPublisherCheck, nodeConfiguration.setNodeName(mPublisherCheck.getDefaultNodeName()));
//                mPublisherCheck.setRun(true);
//                Log.i(TAG, "init: execute(mPublisherCheck");

                if (mPublisherSubscriber == null) {
                    mPublisherSubscriber = new PublisherSubscriber(context);
                }
                if (mPublisherSubscriber.getConnectedNode() == null)
                    nodeMainExecutor.execute(mPublisherSubscriber, nodeConfiguration.setNodeName(NodeName.PublisherSubscriber.toString()), getNodeList(NodeName.PublisherSubscriber, status.getNextAction(), mPublisherSubscriber)); //getStringLocal(R.string.map_nodeNamePubSub)
                else Log.e(TAG, "init: mPublisherSubscriber.getConnectedNode() != null，这里没有再次初始化");

            } catch (IllegalArgumentException e) { // 此法捕获不了这个异常。
                Log.e(TAG, "init: 控件已存在 IllegalArgumentException");
                safeDismissWaitingDialog();
                e.printStackTrace();
            }


            // 如果不是刷新过来的，删掉已存在的make a map的Rapp.如果minimal正常退出，则会关掉rapp，那时就没有rapp存在了。
            status.setNextAction(Action_InitRemocon);
//            getRappList(getStringLocal(R.string.s_actionInitRemocon));
            if (isShouldRappList) {
                getRappList(); // status.getNextAction());
            } else {
                safeDismissWaitingDialog();
                Log.e(TAG, "init: 不需要获取ros状态");
            }
//
//            if(mIsHideMapControl && !status.isRunning(RappNameMapNav)){ // 如果是和朱镜居混合的界面，则看有没有地图，有则显示地图，无则显示“尚未构建地图”
//                getMapList();
//            }

        } catch (Exception e) {
            safeDismissWaitingDialog();
            e.printStackTrace();
            Log.e(TAG, "init: Exception:" + e.toString());
        }
    }

    /**
     * This is an override which diverts the usual startup once a node is
     * connected. Typically this would go to the master chooser, however
     * here we are sometimes returning from one of its child apps (in which
     * case it doesn't have to go choosing a concert). In that case, send
     * it directly to the concert validation and initialisation steps.
     */
    @Override
    protected void startMasterChooser() {
        Log.i(TAG, "startMasterChooer()"); // 先
        if (!isConnected || connectMaster == null) { // 未连接或，未初始化connectMaster
//            safeShowWaitingDialog(getStringLocal(R.string.s_Waiting), getStringLocal(R.string.s_connectingMaster));
            safeShowWaitingDialog("", getStringLocal(R.string.s_connectingMaster), false); // 一旦开启，将不能打断。
            connectMaster = new ConnectMaster(context, new ConnectMasterListener() {
                @Override
                public void success(RoconDescription roconDescription, String SSID) {
//                    safeDismissWaitingDialog();

                    Log.i(TAG, "startMasterChooser() success:");
                    if (roconDescription != null) {
                        Log.i(TAG, "成功拿到了roconDescription, SSID=" + SSID);
                        isConnected = true;
                        LayoutMainMAP.this.roconDescription = roconDescription;

                        init(roconDescription); // 这里直到rapp拿到状态后才停止圈圈，中途出错页停止圈圈。
                    } else {
                        safeDismissWaitingDialog();
                        safeShowNotiDialogCustom(getStringLocal(R.string.s_ErrorConnToRosFailed));
                        Log.i(TAG, "startMasterChooser() success:, roconDescription拿到的是空的，这里执行了shutDown()");
                        // todo 启动Master失败，处理
                    }
                }

                @Override
                public void failed(String message) {
                    safeDismissWaitingDialog();
                    safeShowNotiDialogCustom(getStringLocal(R.string.s_ErrorConnToRosFailed));
                    Log.e(TAG, "连接主机失败：" + message);
                }

                @Override
                public void failed(ConnectMaster.ConnResult result) {
                    if (result.equals(ConnectMaster.ConnResult.R_NoWifiConnected)) {
                        mHandler.obtainMessage(mHandler_mapConnectError, getStringLocal(R.string.s_Error_NoWifiConnected)).sendToTarget();
                        Log.i(TAG, "failed: 当前没有连接wifi，请设置wifi");
                    } else if (result.equals(ConnectMaster.ConnResult.R_ErrorMasterUri)) {
                        mHandler.obtainMessage(mHandler_mapConnectError, getStringLocal(R.string.s_Error_masterUri)).sendToTarget();
                        Log.i(TAG, "failed: 机器人地址错误");
                    } else if (result.equals(ConnectMaster.ConnResult.R_MasterUriNull)) {
                        mHandler.obtainMessage(mHandler_mapConnectError, getStringLocal(R.string.s_Error_masterUriNull)).sendToTarget();
                        Log.i(TAG, "failed:机器人地址为空，请先设置机器人ip地址 ");
                    } else if (result.equals(ConnectMaster.ConnResult.R_CannotContactConcert)) {
                        mHandler.obtainMessage(mHandler_mapConnectError, getStringLocal(R.string.s_Error_CannotContactConcert)).sendToTarget();
                        Log.i(TAG, "failed: 连接主机ROS失败");
                    } else if (result.equals(ConnectMaster.ConnResult.R_MasterUnavailable)) {
                        mHandler.obtainMessage(mHandler_mapConnectError, getStringLocal(R.string.s_Error_MasterUnavailable)).sendToTarget();
                        Log.i(TAG, "failed: 主机地址不可连接");
                    } else if (result.equals(ConnectMaster.ConnResult.R_MasterWifiConnFailed)) {
                        mHandler.obtainMessage(mHandler_mapConnectError, getStringLocal(R.string.s_Error_MasterWifiConnFailed)).sendToTarget();
                        Log.i(TAG, "failed: 连接机器人wifi失败");
                    } else {
                        mHandler.obtainMessage(mHandler_mapConnectError, "failed: 未知错误，未处理， result=" + result).sendToTarget();
                        Log.e(TAG, "failed: 未知错误，未处理， result=" + result);
                    }
                    safeDismissWaitingDialog();
                }
            });
            Log.i(TAG, "startMasterChooser: zt_zhuJingJu=" + zt_zhuJingJu);
            if (zt_zhuJingJu) {
            } else {
                if (!mWifiName.equals("") && !mWifiPwd.equals("")) {
                    Log.i(TAG, "startMasterChooser: 将连接wifi和ros");
                    connectMaster.connectWifiRos(context, mWifiName, mWifiPwd);
                } else {
                    Log.i(TAG, "startMasterChooser: 将连接ros");
                    connectMaster.connectRos(context);
                }
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: ");

        // 暂停前先关闭对话框，否则可能报错WindowLeaked
        if (dialogName != null && dialogName.isShowing()) {
            dialogName.dismiss();
            dialogName = null;
            Log.i(TAG, "onPause: 关闭了dialogName");
        }


        if (!status.isRunningRapp()) { // 没有运行的Rapp，或没有在界面激活哪个rapp，直接退出，不用发送停止键
            Log.i(TAG, "onPause：没有在运行的Rapp");
        } else if (status.isNextAction(status.Action_Exit_BackBtn) || status.isNextAction(status.Action_Exit_BackKey) || status.isNextAction(Action_SavedMap_StartNav)) { // 有退出按钮被点击了，或保存地图了，退出程序
            Log.i(TAG, "有退出按钮被点击了，或保存地图了，退出程序，" + status.getNextAction());
        } else if (status.isRunning(RappNameMapNav)) { // 如果正在运行地图导航，则不退出地图导航，但关闭Remocon
            leaveRemoconServiceForceShutdown(null);
            Log.i(TAG, "正在运行地图导航，则不退出地图导航，但关闭Remocon");
        } else if (status.isRunning(RappNameMakeAMap)) { // 如果异常退出，或onPause退出，已启动构建地图，就关闭构建地图，且退出Remocon
            status.setNextAction(status.Action_Exit_OnPause);
            stopRapp(status.getNextAction());
            Log.i(TAG, "如果异常退出，或onPause退出，已启动构建地图，就关闭构建地图，且退出Remocon");
        } else {
            Log.i(TAG, "没有启动程序，则forceshutdown");
            leaveRemoconServiceForceShutdown(null);
        }
    }

    @Override
    protected void serviceShutdown(NodeMainExecutorService nodeMainExecutorService) {
        // todo 服务停止了，应该把这个界面停止不让使用。
        Log.e(TAG, "serviceShutdown: 未处理");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        Log.e(TAG, "onDestroyView");
        mHandler.removeCallbacks(null); // 让Handler在程序停止的时候，不要执行任务。
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        zt_zhuJingJu = true;
        disconnectAP();

//        WifiConfiguration tempConfig = isExsits(WifiConnect.app.substring(1, WifiConnect.app.length()-1));
//        if (tempConfig != null){
//            wifi.enableNetwork(tempConfig.networkId, true);
//        }
        if (waitingFlag) waitingFlag = false;
        if (this.nodeMainExecutor != null) {
            boolean isShutDown = this.nodeMainExecutor.getScheduledExecutorService().isShutdown(); // false "this.nodeMainExecutor"=6个

            Log.i(TAG, "onDestroy: isShutDown=" + isShutDown + ", isTerminated=" + this.nodeMainExecutor.getScheduledExecutorService().isTerminated());
            if (!isShutDown) {
                Log.e(TAG, "onDestroy: nodeMainExecutor未关闭，尚未处理");
//                // 以下不能删，得找到安全的写法。
////                List<Runnable> listRunnable = this.nodeMainExecutor.getScheduledExecutorService().shutdownNow();
////                Log.i(TAG, "onDestroy: listRunnable.size()="+listRunnable.size()); // 0
//                this.nodeMainExecutor.getScheduledExecutorService().shutdown();
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        new AsyncTask<Void, Void, Void>(){
//                            @Override
//                            protected void onPreExecute() {
//                                super.onPreExecute();
//                                Log.i(TAG, "onPreExecute: 将执行 nodeMainExecutor..shutdown();"); // 打印
//                            }
//
//                            @Override
//                            protected void onPostExecute(Void aVoid) {
//                                super.onPostExecute(aVoid);
//                                Log.i(TAG, "onPostExecute: 已执行完 nodeMainExecutor.shutdown(); isShutdown="+nodeMainExecutor.getScheduledExecutorService().isShutdown()+",isTerminated="+nodeMainExecutor.getScheduledExecutorService().isTerminated()); // 不打印
//                            }
//
//                            @Override
//                            protected Void doInBackground(Void... params) {
//                                Log.i(TAG, "doInBackground: 执行 nodeMainExecutor.shutdown();");// nodeMainExecutor.getScheduledExecutorService().shutdown();"); // 打印
//                                try {
//                                    nodeMainExecutor.shutdown(); // 这句必须放AsyncTask里才会执行，执行以后内消除内存，但报错：java.util.concurrent.RejectedExecutionException
//                                } catch (Exception e) {
//                                    Log.e(TAG, "doInBackground: nodeMainExecutor.shutdown() 执行后的错误");
//                                    e.printStackTrace();
//
//                                }
////                                nodeMainExecutor.getScheduledExecutorService().shutdown(); // 这和上句效果相同，两句都可单独使用也可放一起。
//                                return null;
//                            }
//                        }.execute();
//                    }
//                }).start();
                if (this.nodeMainExecutor.getScheduledExecutorService().isShutdown()) {
                    Log.i(TAG, "onDestroy: nodeMainExecutor.getScheduledExecutorService() 线程关闭了"); // 打印
                } else
                    Log.e(TAG, "onDestroy: nodeMainExecutor.getScheduledExecutorService() 线程还没有关闭");
            }
        } else Log.e(TAG, "onDestroy: this.nodeMainExecutor is null");
        Log.i(TAG, "onDestroy: Thread.activeCount()=" + Thread.activeCount()); // 95
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "onDetach: ");
    }

    private String getStringLocal(int strId) {
        if (getActivity() != null) {
            return getActivity().getString(strId);
        } else return "Activity null, could not get string!";
    }

    /************************************************ 父类方法区 end *******************************************************************/

    /**
     * 是否隐藏地图操作按钮，给Activity用的
     *
     * @param isHideMapControl true-显示，false-隐藏
     */
    public void setHideMapControl(boolean isHideMapControl) {
        this.mIsHideMapControl = isHideMapControl;
    }

    /**
     * 设置wifi名称和密码
     */
    private void setWifi() {
        Log.i(TAG, "setWifi: ");
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        wifi = ((WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE));
//        mWifiName=sp.getStringLocal(DemoApplication.UID, "");
//        mWifiPwd=sp.getStringLocal(mWifiName, "");

        isConnected = false;

        // 先保存原来的wifi，之前可以不曾连接，则wifi是null或""
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.getConnectionInfo() != null) {
            mWifiOldName = wifiManager.getConnectionInfo().getSSID().replace("\"", "").trim(); // 有时候带了双引号，有时候带了空格。wifiManager.getConnectionInfo()有没有可能是null?
            mWifiOldNetId = wifiManager.getConnectionInfo().getNetworkId();
        }
        Log.i(TAG, "setWifi: mWifiOldName=" + mWifiOldName + ",mWifiOldNetId=" + mWifiOldNetId);
        // 弹对话框，可手动输入wifi和密码，也可“取消”用默认的登录
//        showDialogSetWifi();
        DialogCustom.getInstance().showDialogSetWifi(context, mWifiName, mWifiPwd, mHandler, mHandler_DialogResultSetWifi);


        // 用默认的账号密码登录
//        connWifiRos();
        initGPS();
        //
    }

    /**
     * 打开GPS
     */
    private void initGPS() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "请打开GPS", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage("请打开GPS");
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    arg0.dismiss();
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                }
            });
            dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show(); // android.view.WindowLeaked: Activity com.iot.remocon.ConnectActivity has leaked window com.android.internal.policy.impl.PhoneWindow$DecorView{c5d0a8f V.E..... R....... 0,0-698,281} that was originally added here

        } else {
        }
    }

    /**
     * 初始化控件，找控件
     */
    private void initUI(View view) {
        Log.i(TAG, "initUI()");

        id_mapConnectError.setVisibility(View.GONE);

        // 地图导航用的
//        id_map = (VisualizationView) view.findViewById(R.id.id_map);
        id_map.onCreate(Lists.<Layer>newArrayList()); //必须在onCreate里写mapView.onCreate()
        id_map.getCamera().jumpToFrame(getStringLocal(R.string.map_frame));

        mapPosePublisherLayer = new MapPosePublisherLayer(context);
        mapPosePublisherLayer.initHandler(mHandler, mHandler_cancelGoalOK);

        occupancyGridLayer = new OccupancyGridLayerCustom(context, "map", new ReceiveMapListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "onSuccess: occupancyGridLayer, status.getRunningRappName()=" + status.getRunningRappName() + ",status.isRunningRapp()=" + status.isRunningRapp() + ",isInitNodeMain=" + isInitNodeMain + ", NextAction=" + status.getNextAction()); // isInitNodeMain=true
                if (!isInitNodeMain || (isInitNodeMain && status.isRunningRapp())) {
                    // 接收到地图了。
                    Log.i(TAG, "onSuccess: 接收到地图，刷新一下，希望robot在屏幕正中间"); //有用，可以保证robot在屏幕正中间。
                    refreshMapView();
                    //                        isReceivedMap = true;
                    status.setIsReceivedMap(true);
                    if ((status.isReceivedCamera() && status.isReceivedMap()) || status.isNextAction(status.Action_ReNewMap) || status.isNextAction(status.Action_LongClick_StartMapMake) ||

                            status.isNextAction(Action_LongClick_StartMapNav) || status.isNextAction(Action_InitRemocon_RunningMapNav_PublishMap) || status.isNextAction(Action_SavedMap_StartNav) || status.isNextAction(Action_refresh_Nav_inject)) {

                        safeDismissWaitingDialog();
                        isWaitforMap = false;
                        if (status.isNextAction(status.Action_ReNewMap) || status.isNextAction(status.Action_LongClick_StartMapMake)) {
                            status.setNextAction("");
                        } // 地图接收不在程序流程里，不应该在此置空任务，接收地图之后，此任务不一定已完成。
                        else if (status.isNextAction(Action_LongClick_StartMapNav) || status.isNextAction(Action_InitRemocon_RunningMapNav_PublishMap) || status.isNextAction(Action_SavedMap_StartNav) || status.isNextAction(Action_refresh_Nav_inject)) {
                            if (isExecutedGetPositions) {
                                status.setNextAction("");
                            } else Log.e(TAG, "onSuccess: 尚未执行获取标记服务，不可置空action");

                        } else {
                            Log.e(TAG, "onSuccess: 未知任务，未处理 " + status.getNextAction());
                        }
                    }
                } else {
                    Log.e(TAG, "onSuccess: 收到不需要的地图，将清理地图");
                    occupancyGridLayer.clearTiles();
                }
            }
        });

        occupancyGridLayer.setSubscribe(true);
        occupancyGridLayer.setmScreenHeightPx(mScreenHeightPx);
        occupancyGridLayer.setmScreenWidthPx(mScreenWidthPx);
        occupancyGridLayer.setmScreenInches(getScreenSizeOfDevice2());
        occupancyGridLayer.initHandler(mHandler, mHandler_modifyPosition, mHandler_cancelGoal);

        RobotLayer robotLayer = new RobotLayer(getStringLocal(R.string.s_mapRobotLayer));
        robotLayer.setShape(new BitmapShape(context, R.mipmap.btn_map_robot, id_map.getCamera().getZoom()));

        poseSubscriberLayerCustom = new PoseSubscriberLayerCustom(getStringLocal(R.string.s_mapPoseSubscriberLayer));
        poseSubscriberLayerCustom.setShape(new BitmapShape(context, R.mipmap.btn_map_robot_dian, id_map.getCamera().getZoom()));

        id_map.addLayer(new CameraControlLayer()); // 缩放
        id_map.addLayer(occupancyGridLayer);
        id_map.addLayer(robotLayer);
        id_map.addLayer(new LaserScanLayer(getStringLocal(R.string.s_mapLaserScanLayer)));
        id_map.addLayer(poseSubscriberLayerCustom);
        id_map.addLayer(mapPosePublisherLayer); // 长按

        id_joystick.setViewSelected(true);
        id_joystick.setHandler(mHandler, mHandler_Teleop);


        id_joystick.setTopicName(getStringLocal(R.string.joystick_topic));


    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(id_mapEstablish==null){
                    Log.e(TAG, "handleMessage: id_mapEstablish is null");
                    return;
                }
                switch (msg.what) {


                    case Handler_Toast:
                        String str = (String) msg.obj;
                        if (str != null && !str.equals("")) {
                            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "handleMessage: 参数错误，无法显示toast");
                        }
                        break;

                    case Handler_Dialog:
                        break;

                    case Handler_Dialog_Timeout:
                        break;

                    case mHandler_showDialogDelete:
                        // 是否删除地图
                        showDialogIfDeleteMaps();
                        break;


                    case mHandler_connectRappFailure: // 连接rapp失败
                        break;

                    case mHandler_MapMakeBackground: //  构建地图按钮设置背景
                        Integer bgIdMake = (Integer) msg.obj;
                        if (bgIdMake != null && bgIdMake != 0) {
//                            id_mapEstablish.setBackgroundResource(bgIdMake);
                            id_mapEstablish.setImageResource(bgIdMake);
                            Log.i(TAG, "构建地图按钮设置背景设置成功");
                        } else {
                            Log.e(TAG, "构建地图按钮设置背景设置失败，参数错误");
                        }
                        break;

                    case mHandler_MapNavBackground: // 地图导航按钮设置背景
                        Integer bgIdNav = (Integer) msg.obj;
                        if (bgIdNav != null && bgIdNav != 0) {
//                            id_mapNavigation.setBackgroundResource(bgIdNav);
                            if (id_mapNavigation != null)
                                id_mapNavigation.setImageResource(bgIdNav);
                            Log.i(TAG, "地图导航按钮设置背景成功");
                        } else {
                            Log.e(TAG, "地图导航按钮设置背景失败，参数错误");
                        }
                        break;

                    case mHandler_MapMakeViewGone: // 隐藏构建地图控制按钮
                        if (id_map != null) {
                            mapMakeControlerView(false, false);
                        } else {
                            mHandler.sendEmptyMessageDelayed(mHandler_MapMakeViewGone, 1000);
                            Log.i(TAG, "已 隐藏构建地图控制按钮");
                        }


                        break;


                    case mHandler_MapNavViewGone: // 隐藏地图导航控制按钮

                        if (id_map != null) {
                            mapNavControlerView(false, false);
                        } else {
                            mHandler.sendEmptyMessageDelayed(mHandler_MapNavViewGone, 1000);
                            Log.i(TAG, "已 隐藏地图导航控制按钮");
                        }

                        break;

                    case mHandler_LongClickBg: // 设置长按事件对应的背景图片
                        boolean[] clicks = (boolean[]) (msg.obj);
                        if (clicks != null && clicks.length == 4) {
                            setLongClickBg(clicks[0], clicks[1], clicks[2]);
                        } else {
                            Log.e(TAG, "handleMessage: 参数错误， clicks is null or clicks.length != 4");
                        }
                        break;

                    case mHandler_btnFollowerBG: // 设置跟随按钮的背景图
                        Integer bgIdFollwer = (Integer) msg.obj;
                        if (bgIdFollwer != null && bgIdFollwer != 0) {
//                            id_mapFollow.setBackgroundResource(bgIdFollwer);
                            id_mapFollow.setImageResource(bgIdFollwer);
                            Log.i(TAG, "跟随按钮设置背景设置成功");
                        } else {
                            Log.e(TAG, "跟随按钮设置背景设置失败，参数错误");
                        }
                        break;

//                    case mHandler_clearCameraView: // 清理摄像头背景
////                    cameraView.setImageResource(R.drawable.shape_null);
//                        cameraView.setVisibility(View.GONE);
//                        Log.i(TAG, "handleMessage: 已隐藏cameraView");
//                        break;
//                    case mHandler_displayCameraView:
//                        cameraView.setVisibility(View.VISIBLE);
//                        Log.i(TAG, "handleMessage: 已显示cameraView");
//                        break;

                    case mHandler_modifyPosition: // 修改标记
                        Log.i(TAG, "handleMessage: mHandler_modifyPosition");
                        FlagBean flagClick = (FlagBean) (msg.obj);
                        if (flagClick != null) {

                            showDialogModifyFlagName(flagClick.getId(), flagClick.getName());
                        }
                        break;

                    case mHandler_cancelGoal: // 取消去目的地，手指点击取消来这里
//                    mPublisherSubscriber.send(RemoconAllActivity.this);
                        Log.i(TAG, "handleMessage: mapPosePublisherLayer.isGoingToGoal()=" + mapPosePublisherLayer.isGoingToGoal());
                        mapPosePublisherLayer.send(context);
                        break;

                    case mHandler_cancelGoalOK: // 取消去目的地成功
                        Log.i(TAG, "handleMessage: mapPosePublisherLayer.isGoingToGoal()=" + mapPosePublisherLayer.isGoingToGoal());
                        if (mapPosePublisherLayer.isGoingToGoal()) {
                            showToast(R.string.s_canceled);
                        }
                        mapPosePublisherLayer.setGoingToGoal(false);
                        poseSubscriberLayerCustom.setReady(false);
                        break;

                    case mHandler_refresh: // 刷新按钮点击后，来这里。
                        Log.i(TAG, "handleMessage: 刷新");
                        // 测试连接主机，停止后也收不住的逻辑，且连接重合将出错
//                    if(connectMaster != null){
//                        safeShowWaitingDialog(getStringLocal(R.string.s_Waiting), getStringLocal(R.string.s_connectingMaster), false);
////                        connectMaster.refresh();
////                        connectMaster.connectWifiRos(RemoconAllActivity.this, mWifiName, mWifiPwd);
//                        // 如果已经初始化过了，则先清理掉
//                        clearExecuted();
//                        connectMaster.refresh();
//                    }
//                    else if(connectMaster == null && getIntent() != null){ // 从连接界面过来的
//                        Log.i(TAG, "onClick: 从连接界面连上的，将要刷新，先清理所有数据，再启动StartMasterChooser()");
//                        clearExecuted();
//                        startMasterChooser();
//                    }
                        if (id_mapConnectError != null && id_mapConnectError.isShown()) {
                            id_mapConnectError.setVisibility(View.GONE);
                        }
                        if (connectMaster != null) {
                            AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected void onPreExecute() {
                                    safeShowWaitingDialog("", getStringLocal(R.string.s_clearingExecute), false);
                                }

                                @Override
                                protected Void doInBackground(Void... params) {
                                    Log.i(TAG, "doInBackground: 刷新");
                                    clearExecuted();
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aBoolean) {
                                    Log.i(TAG, "onPostExecute: 将执行connectMaster.refresh()");
                                    connectMaster.refresh();
                                }

                                @Override
                                protected void onCancelled(Void aVoid) {
                                    super.onCancelled(aVoid);
                                    Log.i(TAG, "onCancelled: " + "," + aVoid);
                                }
                            };
                            asyncTask.execute();


                            timeoutCheck(mTimeout, asyncTask);

                        } else {
                            safeShowWaitingDialog("", getStringLocal(R.string.s_connectingRosAgain), false);
                            isConnected = false;
                            startMasterChooser();
                        }

                        break;

                    case mHandler_longClick_makeAMap: // 长按：构建地图

                        if (status.isRunning(RappNameMakeAMap)) {
                            Log.i(TAG, "长按事件，停止make a map");
//                        mapMakeControlerView(false, false);
//                        stopRapp(getStringLocal(R.string.s_actionStopRappOnly));
                            status.setNextAction(status.Action_stopRappOnly);
                            stopRapp(status.Action_stopRappOnly);
                        } else if (status.isRunning(RappNameMapNav)) {
                            showToast(R.string.s_Error_PleaseGotoStopMapNavBefore);
                        } else {
                            Log.i(TAG, "直接启动构建地图");
                            // 启动地图构建成功，或失败，都清除动作
                            status.setNextAction(status.Action_LongClick_StartMapMake);
                            getMapList();

                        }
                        break;

                    case mHandler_longClick_mapNavigation: // 长按：地图导航
                        if (status.isRunning(RappNameMapNav)) {
                            Log.i(TAG, "长按事件，停止map nav");
//                        mapNavControlerView(false, false);
//                        stopRapp(getStringLocal(R.string.s_actionStopRappOnly));
                            status.setNextAction(status.Action_stopRappOnly);
                            stopRapp(status.Action_stopRappOnly);
                        } else if (status.isRunning(RappNameMakeAMap)) {
                            showToast(R.string.s_Error_PleaseGotoStopMapMakeBefore);
                        } else {
                            // 数据库里有地图才可以打开地图导航
                            Log.i(TAG, "直接打开地图导航：获取地图-发布地图-启动地图导航");
                            status.setNextAction(Action_LongClick_StartMapNav);
                            isExecutedGetPositions = false;
                            getMapList();
                        }
                        break;

                    case mHandler_TimeoutForWaitMap: // 等待地图超时
                        int obj = (int) (msg.obj);
                        if (isWaiting() && isWaitforMap && (obj == mWaitMapFlag)) { // 只处理最近一次的超时
                            safeDismissWaitingDialog();
                            showToast(R.string.s_TimoutToWaitforMap);
                            Log.e(TAG, "handleMessage: 等待地图超时，不等了");
                        } else {
                            Log.i(TAG, "handleMessage: TimeoutForWaitMap:isWaiting()=" + isWaiting() + ",isWaitforMap=" + isWaitforMap + ",obj=" + obj + ",mWaitMapFlag=" + mWaitMapFlag + ",mTimeoutWaitMap=" + mTimeoutWaitMap); // 此句可能频繁打印，以后最好能去掉。
                        }
                        break;

                    case mHandler_Teleop: // 从手柄控件传来的数据
                        Log.i(TAG, "handleMessage: teleop");
                        if (msg.obj == VirtualJoystickView.enum_whatcode.unInit) {
                            showToast(R.string.s_TeleopUninit);
                        }
                        break;
                    case mHandler_error_exit:
                        Log.e(TAG, "handleMessage: 未处理，mHandler_error_exit");
//                        String errorMsg = (String)msg.obj;
//                        if (errorMsg != null && !errorMsg.equals("")) {
//                            id_tvTips.setText(errorMsg);
//                        }
//                        exit();
                        break;

                    case mHandler_setTipsText:
                        Log.e(TAG, "handleMessage: 未处理，mHandler_setTipsText");
//                        String msgStr = (String)msg.obj;
//                        if (msgStr != null && !msgStr.equals("")) {
//                            id_tvTips.setText(msgStr);
//                        }
                        break;

                    case mHandler_setWifi:
                        Log.e(TAG, "handleMessage: 未处理，mHandler_setWifi");
//                        connWifiRos();
////                    initGPS();
                        break;

                    case mHandler_mapConnectError: // 连接ros失败时，显示Text View
                        String strErr = (String) msg.obj;
                        if (id_mapConnectError != null) {
                            id_mapConnectError.setText(strErr);
                            id_mapConnectError.setVisibility(View.VISIBLE);
                        } else Log.i(TAG, "handleMessage: id_mapConnectError 空");
                        break;
                    case mHandler_DialogResultCustomFlagName:/** 自定义对话框返回结果处理 */
                        DialogCustom.DialogResult resultCustomFlag = (DialogCustom.DialogResult) msg.obj;
                        Log.i(TAG, "mHandler_DialogResultCustomFlagName: resultCustomFlag=" + resultCustomFlag.toString());
                        if (resultCustomFlag.getResult().equals(DialogCustom.ResultName.R_AddPosition) && resultCustomFlag.getMessage() != null && !resultCustomFlag.getMessage().equals("")) {
                            Log.i(TAG, "mHandler_DialogResultCustomFlagName: 添加标记，name=" + resultCustomFlag.getMessage());
                            addPosition(resultCustomFlag.getMessage());
                        } else if (resultCustomFlag.getResult().equals(DialogCustom.ResultName.R_ShowNotify) && resultCustomFlag.getMessage() != null && !resultCustomFlag.getMessage().equals("")) {
                            Log.i(TAG, "mHandler_DialogResultCustomFlagName: 显示消息：" + resultCustomFlag.getMessage());
                            safeShowNotiDialogCustom(resultCustomFlag.getMessage());
                        } else if (resultCustomFlag.getResult().equals(R_Cancel)) {
                            Log.i(TAG, "mHandler_DialogResultCustomFlagName: 取消");
                        } else {
                            Log.e(TAG, "mHandler_DialogResultCustomFlagName:未知错误，未处理，result=null ? " + (resultCustomFlag == null) + ",message=" + (resultCustomFlag.getMessage() == null ? "null" : resultCustomFlag.getMessage()));
                        }
                        break;
                    case mHandler_DialogResultModifyFlagName:/** 修改标记名称对话框返回结果处理 */
                        DialogCustom.DialogResult resultModifyFlag = (DialogCustom.DialogResult) msg.obj;
                        Log.i(TAG, "mHandler_DialogResultModifyFlagName: resultModifyFlag=" + resultModifyFlag.toString());
                        if (resultModifyFlag.getResult().equals(DialogCustom.ResultName.R_ModifyPosition) && resultModifyFlag.getMessage() != null && !resultModifyFlag.getMessage().equals("")) {
                            Log.i(TAG, "mHandler_DialogResultModifyFlagName: 修改标记，原标记:" + resultModifyFlag.getPositionOldName() + ", 新标记：" + resultModifyFlag.getMessage());
                            modifyPosition(resultModifyFlag.getPositionId(), resultModifyFlag.getMessage());
                        } else if (resultModifyFlag.getResult().equals(DialogCustom.ResultName.R_DeletePosition) && resultModifyFlag.getMessage() != null && !resultModifyFlag.getMessage().equals("")) {
                            Log.i(TAG, "mHandler_DialogResultModifyFlagName: 删除标记：" + resultModifyFlag.getMessage());
                            deletePosition(resultModifyFlag.getPositionId());
                        } else if (resultModifyFlag.getResult().equals(DialogCustom.ResultName.R_ShowNotify) && resultModifyFlag.getMessage() != null && !resultModifyFlag.getMessage().equals("")) {
                            Log.i(TAG, "mHandler_DialogResultModifyFlagName: 显示消息：" + resultModifyFlag.getMessage());
                            safeShowNotiDialogCustom(resultModifyFlag.getMessage());
                        } else if (resultModifyFlag.getResult().equals(R_Cancel)) {
                            Log.i(TAG, "mHandler_DialogResultModifyFlagName: 取消");
                        } else {
                            Log.e(TAG, "mHandler_DialogResultModifyFlagName:未知错误，处理，result=null ? " + (resultModifyFlag == null) + ",message=" + (resultModifyFlag.getMessage() == null ? "null" : resultModifyFlag.getMessage()));
                        }
                        break;
                    case mHandler_DialogResultIfNewMap: /** 是否重新构建地图对话框返回结果处理 */
                        DialogCustom.DialogResult resultIfNewMap = (DialogCustom.DialogResult) msg.obj;
                        Log.i(TAG, "mHandler_DialogResultIfNewMap: resultIfNewMap=" + resultIfNewMap.toString());
                        if (resultIfNewMap.getResult().equals(DialogCustom.ResultName.R_ReNewMap)) {
                            Log.i(TAG, "mHandler_DialogResultIfNewMap: 重新构建地图");
                            status.setNextAction(status.Action_ReNewMap);
                            stopRapp(status.Action_ReNewMap);
                        } else if (resultIfNewMap.getResult().equals(R_Cancel)) {
                            Log.i(TAG, "mHandler_DialogResultIfNewMap: 取消");
                        } else {
                            Log.e(TAG, "mHandler_DialogResultIfNewMap:result=null ? " + (resultIfNewMap == null) + ",message=" + (resultIfNewMap.getMessage() == null ? "null" : resultIfNewMap.getMessage()));
                        }
                        break;
                    case mHandler_DialogResultSetWifi:/** 设置wifi名称和密码 对话框返回结果处理 */
                        DialogCustom.DialogResult resultSetWifi = (DialogCustom.DialogResult) msg.obj;
                        Log.i(TAG, "mHandler_DialogResultSetWifi: result=" + resultSetWifi.toString());
                        if (resultSetWifi.getResult().equals(DialogCustom.ResultName.R_WifiSet)) {
                            Log.i(TAG, "mHandler_DialogResultSetWifi: 设置wifi");
                            if (resultSetWifi.getMessage() != null && !resultSetWifi.getMessage().equals(""))
                                mWifiName = resultSetWifi.getMessage();
                            if (resultSetWifi.getMessageSecond() != null && !resultSetWifi.getMessageSecond().equals(""))
                                mWifiPwd = resultSetWifi.getMessageSecond();
//            mHandler.sendEmptyMessage(mHandler_setWifi);
                            startMasterChooser();
                        } else if (resultSetWifi.getResult().equals(DialogCustom.ResultName.R_WifiNotSet)) {
                            Log.i(TAG, "mHandler_DialogResultSetWifi: 不设置wifi，用系统当前的");
                            mWifiName = "";
                            mWifiPwd = "";
//            mHandler.sendEmptyMessage(mHandler_setWifi);
                            startMasterChooser();
                        } else if (resultSetWifi.getResult().equals(DialogCustom.ResultName.R_Cancel)) {
                            Log.i(TAG, "mHandler_DialogResultSetWifi: 取消");
                        } else {
                            Log.e(TAG, "mHandler_DialogResultSetWifi:未知错误，处理");
                        }
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    /**
     * 监听UI事件、其他监听
     */
    private void initListener() {
        Log.i(TAG, "initListener()");

        id_mapEstablish.setOnLongClickListener(clickLongListener);
        id_mapNavigation.setOnLongClickListener(clickLongListener);

        id_mapEstablish.setOnClickListener(clickListener);
        id_mapNavigation.setOnClickListener(clickListener);

        id_mapRefresh.setOnClickListener(clickListener);
        id_menTing.setOnClickListener(clickListener);
        id_qianTing.setOnClickListener(clickListener);
        id_huiYiTing.setOnClickListener(clickListener);
        id_banGongQu.setOnClickListener(clickListener);
//        id_toilet.setOnClickListener(clickListener);
        id_customName.setOnClickListener(clickListener);
        id_mapSign.setOnClickListener(clickListener);
        id_saveFlag.setOnClickListener(clickListener);

        id_saveMap.setOnClickListener(clickListener);
        id_newMap.setOnClickListener(clickListener);

        id_mapStartingpoint.setOnClickListener(clickListener);
        id_mapArrivepoint.setOnClickListener(clickListener);
        id_mapFollow.setOnClickListener(clickListener);
        id_mapRoute.setOnClickListener(clickListener);
        id_mapTeleop.setOnClickListener(clickListener);

    }

    /**
     * 点击事件监听
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

//                case R.id.id_back: /** 返回 */
//                    // 返回
//                    Log.i(TAG, "onClick: 返回");
//                    isExit = true; // 在onPause里用
//                    status.setNextAction(status.Action_Exit_BackBtn);
//                    if(status.isRunning(RappNameMakeAMap)){ // 如果正在构建地图，就退出程序，不启动地图导航
//                        stopRapp(getStringLocal(R.string.s_actionForceShutDown));
//                        Log.i(TAG, "正在构建地图，就退出程序Rapp，不启动地图导航");
//                    }
//                    else if(status.isRunning(RappNameMapNav)){ // 如果启动了地图导航，直接forceshutdown
//                        leaveRemoconServiceForceShutdown(null);
//                        Log.i(TAG, "启动了地图导航，直接forceshutdown");
//                    }
//                    else { // 如果没有启动程序，则forceshutdown
//                        Log.i(TAG, "没有启动程序，则forceshutdown");
//                        leaveRemoconServiceForceShutdown(null);
//                    }
//
//                    break;

                case R.id.id_mapEstablish: // 地图构建按钮点击事件:置反横排控件的显示
                    if (status.isRunning(RappNameMakeAMap)) {
                        mapMakeControlerView(true, false);
                    } else {
                        showToast(R.string.s_invalidMapMake);
                    }
                    break;

                case R.id.id_mapNavigation:// 地图导航按钮点击事件:置反横排控件的显示
                    if (status.isRunning(RappNameMapNav)) {
                        mapNavControlerView(true, false);
                    } else {
                        showToast(R.string.s_invalidMapNav);
                    }
                    break;

                case R.id.id_mapSign: /** 编辑标记 */
                    Log.i(TAG, "onClick: id_mapSign");
                    if (status.isRunning(RappNameMakeAMap) || status.isRunning(RappNameMapNav)) {
                        if (!mapPosePublisherLayer.isEditMode()) {
                            setLongClickBg(true, false, false);
                            mapPosePublisherLayer.setEditMode();
                            mapMakeControlerView(false, false);
                            setRouteLayoutShow(false);
//                            id_layout_map_sign.setVisibility(View.VISIBLE);
//                            id_mapSign.setImageResource(R.mipmap.toolbar_mapmode_sign_selected);
                        } else {
                            id_layout_map_sign.setVisibility(View.GONE);
                            setLongClickBg(false, false, false);
                            mapPosePublisherLayer.setNoneMode();
                            id_mapSign.setImageResource(R.mipmap.toolbar_mapmode_sign_normal);
                        }

                        Log.i(TAG, "onClick: id_mapSign.getY()=" + id_mapSign.getY() + ", id_mapSign.getHeight()=" + id_mapSign.getHeight() + ", id_joystick.getBottom()=" + id_joystick.getBottom() + ", id_joystick.getY()=" + id_joystick.getY() + ", id_joystick.getHeight()=" + id_joystick.getHeight()); // id_mapSign.getY()=0.0, id_mapSign.getHeight()=72, id_joystick.getBottom()=956, id_joystick.getY()=556.0, id_joystick.getHeight()=400
                    } else {
                        showToast(R.string.s_startRappBefore);
                    }
                    break;


                case R.id.id_mapStartingpoint:
                    Log.i(TAG, "onClick: id_mapStartingpoint");
                    if (status.isRunning(RappNameMapNav)) {
                        if (!mapPosePublisherLayer.isPoseMode()) {
                            setLongClickBg(false, true, false);
                            mapPosePublisherLayer.setPoseMode();
                            id_mapStartingpoint.setImageResource(R.mipmap.toolbar_mapmode_startingpoint_selected);
                        } else {
                            setLongClickBg(false, false, false);
                            mapPosePublisherLayer.setNoneMode();
                            id_mapStartingpoint.setImageResource(R.mipmap.toolbar_mapmode_startingpoint_normal);
                        }
                    } else {
                        showToast(R.string.s_startMapNavBefore);
                    }

                    break;

                case R.id.id_mapArrivepoint:
                    Log.i(TAG, "onClick: id_mapArrivepoint");
                    if (status.isRunning(RappNameMapNav) || status.isRunning(RappNameMakeAMap)) {
                        if (!mapPosePublisherLayer.isGoalMode()) {
                            setLongClickBg(false, false, true);
                            mapPosePublisherLayer.setGoalMode();
                            id_mapArrivepoint.setImageResource(R.mipmap.toolbar_mapmode_arrivalpoint_selected);
                        } else {
                            setLongClickBg(false, false, false);
                            mapPosePublisherLayer.setNoneMode();
                            id_mapArrivepoint.setImageResource(R.mipmap.toolbar_mapmode_arrivalpoint_normal);
                        }
                    } else {
                        showToast(R.string.s_startRappBefore);
                    }
                    break;

                case R.id.id_mapRoute: // 打开路径弹出框
                    if(occupancyGridLayer != null) occupancyGridLayer.saveMapPng();//测试

                    if (status.isRunning(RappNameMapNav)) {
                        setRouteLayoutShow(!id_layout_map_route.isShown());
                    } else {
                        showToast(R.string.s_startMapNavBefore);
                    }

//                    if (id_layout_map_route.isShown()) {
//                        id_layout_map_route.setVisibility(View.GONE);
//                        id_mapRoute.setImageResource(R.mipmap.toolbar_mapmode_route_normal);
//                    }
//                    else {
//                        id_layout_map_route.setVisibility(View.VISIBLE);
//                        id_mapRoute.setImageResource(R.mipmap.toolbar_mapmode_route_selected);
//                    }
                    break;
                case R.id.id_mapRefresh:/** 刷新 */
                    Log.i(TAG, "onClick: 刷新");
                    mHandler.sendEmptyMessage(mHandler_refresh);

                    break;

                case R.id.id_mapTeleop: // 打开、隐藏手柄
                    if (id_joystick.isShown()) {
                        id_joystick.setVisibility(View.GONE);
                        id_mapTeleop.setImageResource(R.mipmap.toolbar_mapmode_nothing_normal);
                    } else {
                        id_joystick.setVisibility(View.VISIBLE);
                        id_mapTeleop.setImageResource(R.mipmap.toolbar_mapmode_nothing_selected);
                    }
                    break;
                case R.id.id_mapFollow: // 地图唤醒功能
                    if (status.isRunningRapp()) {
                        if (!status.isFollow()) {
                            followerService(SetFollowStateRequest.FOLLOW);
                        } else {
                            followerService(SetFollowStateRequest.STOPPED);
                        }
                    } else {
                        showToast(R.string.s_openMakeMapOrMapNav);
                    }
                    break;

                case R.id.id_menTing:/** 门 */
                    // 门
//                    if(id_mapEstablish.isChecked() && selectedInteraction.getName().contains(appName.make_a_map.toString())) {
//                    if(status.isRunning(RappNameMakeAMap)) {
                    if (status.isRunning(RappNameMakeAMap) || status.isRunning(RappNameMapNav)) {
                        if (occupancyGridLayer.getTextShapeFactory() == null) {
                            showToast(R.string.s_initialingNotAddPosition);
                        } else if (isNewPositionName(getStringLocal(R.string.s_menTing))) {
                            isPositionIdFixed = true;
                            addPosition(getStringLocal(R.string.s_menTing));
                        } else showToast(R.string.s_positionExists);
                    } else {
//                        showToast(R.string.s_invalidMapMake);
                        showToast(R.string.s_startRappBefore);
                    }

//                    // 打开make a map rapp
//                    for(Interaction inter : availableAppsCache){
//                        if(inter.getName().contains(appName.make_a_map.toString())){
//                            selectedInteraction = inter;
//                            connectRapp(selectedInteraction.getName());
//                        }
//                    }

                    break;

                case R.id.id_qianTing:/** 客厅 */
                    // 客厅
//                    if(id_mapEstablish.isChecked() && selectedInteraction.getName().contains(appName.make_a_map.toString())) {
//                    if(status.isRunning(RappNameMakeAMap)) {
                    if (status.isRunning(RappNameMakeAMap) || status.isRunning(RappNameMapNav)) {
                        if (occupancyGridLayer.getTextShapeFactory() == null) {
                            showToast(R.string.s_initialingNotAddPosition);
                        } else if (isNewPositionName(getStringLocal(R.string.s_qianTing))) {
                            isPositionIdFixed = true;
                            addPosition(getStringLocal(R.string.s_qianTing));
                        } else showToast(R.string.s_positionExists);
                    } else {
//                        showToast(R.string.s_invalidMapMake);
                        showToast(R.string.s_startRappBefore);
                    }

//                    // 打开地图导航
//                    // 打开make a map rapp
//                    for(Interaction inter : availableAppsCache){
//                        if(inter.getName().contains(appName.map_nav.toString())){
//                            selectedInteraction = inter;
//                            connectRapp(selectedInteraction.getName());
//                        }
//                    }
                    break;

                case R.id.id_huiYiTing:/** 卧室 */
//                    if(id_mapEstablish.isChecked() && selectedInteraction.getName().contains(appName.make_a_map.toString())) {
//                    if(status.isRunning(RappNameMakeAMap)) {
                    if (status.isRunning(RappNameMakeAMap) || status.isRunning(RappNameMapNav)) {
                        if (occupancyGridLayer.getTextShapeFactory() == null) {
                            showToast(R.string.s_initialingNotAddPosition);
                        } else if (isNewPositionName(getStringLocal(R.string.s_huiYiTing))) {
                            isPositionIdFixed = true;
                            addPosition(getStringLocal(R.string.s_huiYiTing));
                        } else showToast(R.string.s_positionExists);


                    } else {
//                        showToast(R.string.s_invalidMapMake);
                        showToast(R.string.s_startRappBefore);
                    }
                    break;

                case R.id.id_banGongQu:/** 厨房 */
//                    if(id_mapEstablish.isChecked() && selectedInteraction.getName().contains(appName.make_a_map.toString())) {
//                    if(status.isRunning(RappNameMakeAMap)) {
                    if (status.isRunning(RappNameMakeAMap) || status.isRunning(RappNameMapNav)) {
                        if (occupancyGridLayer.getTextShapeFactory() == null) {
                            showToast(R.string.s_initialingNotAddPosition);
                        } else if (isNewPositionName(getStringLocal(R.string.s_banGongQu))) {
                            isPositionIdFixed = true;
                            addPosition(getStringLocal(R.string.s_banGongQu));
                        } else showToast(R.string.s_positionExists);
                    } else {
//                        showToast(R.string.s_invalidMapMake);
                        showToast(R.string.s_startRappBefore);
                    }
                    break;

//                case R.id.id_toilet: /** 卫生间 */
////                    if(id_mapEstablish.isChecked() && selectedInteraction.getName().contains(appName.make_a_map.toString())) {
////                    if(status.isRunning(RappNameMakeAMap)) {
//                    if (status.isRunning(RappNameMakeAMap) || status.isRunning(RappNameMapNav)) {
//                        if (occupancyGridLayer.getTextShapeFactory() == null) {
//                            showToast(R.string.s_initialingNotAddPosition);
//                        }
//                        else if(isNewPositionName(getStringLocal(R.string.s_toilet))) {
//                            isPositionIdFixed = true;
//                            addPosition(getStringLocal(R.string.s_toilet));
//                        }else showToast(R.string.s_positionExists);
//                    }else{
////                        showToast(R.string.s_invalidMapMake);
//                        showToast(R.string.s_startRappBefore);
//                    }
//
//                    break;

                case R.id.id_customName: /** 自定义标记名称 */
//                    if(id_mapEstablish.isChecked() && selectedInteraction.getName().contains(appName.make_a_map.toString())) {
//                    if(status.isRunning(RappNameMakeAMap)) {
                    if (status.isRunning(RappNameMakeAMap) || status.isRunning(RappNameMapNav)) {

                        if (occupancyGridLayer.getTextShapeFactory() == null) {
                            showToast(R.string.s_initialingNotAddPosition);
                        } else {
                            isPositionIdFixed = false;
                            showDialogCustomFlagName();
                        }
                    } else {
//                        showToast(R.string.s_invalidMapMake);
                        showToast(R.string.s_startRappBefore);
                    }

                    break;
                case R.id.id_saveFlag: /** 保存标记 */
                    Log.i(TAG, "onClick: id_saveFlag");
                    if (status.isRunning(RappNameMakeAMap) || status.isRunning(RappNameMapNav)) {
                        if (occupancyGridLayer.getFlagBeanList().size() > 0) {
                            status.setNextAction(status.Action_savePositions);
                            savePositions();
                        } else {
                            showToast(R.string.s_addPositionBeforeSave);
                        }
                    } else {
                        showToast(R.string.s_startRappBefore);
                    }
                    break;

                case R.id.id_saveMap: /** 保存地图 */
//                    // 是否已存在地图，已存在则删除，不存在则直接进入
//                    if(id_mapEstablish.isChecked() && selectedInteraction.getName().contains(appName.make_a_map.toString())) {
                    if (status.isRunning(RappNameMakeAMap)) {
//                        dealWithOldMap();
                        status.setNextAction(Action_SavedMap_StartNav);
                        isExecutedGetPositions = false;
                        // 直接保存
//                        saveMap();
                        saveMap();
                        occupancyGridLayer.saveMapPng();
                    } else {
                        showToast(R.string.s_invalidMapMake);
                    }

                    break;

                case R.id.id_newMap:/** 构建新地图 */
//                    if(id_mapEstablish.isChecked() && selectedInteraction.getName().contains(appName.make_a_map.toString())) {
                    if (status.isRunning(RappNameMakeAMap) && isAtChargingPile) {
                        // 停止构建地图、开启构建地图
                        Log.i(TAG, "构建新地图：希望重启构建地图");

                        showDialogIfNewMap();

                    } else if (!status.isRunning(RappNameMakeAMap)) {
                        showToast(R.string.s_invalidMapMake);
                    } else if (!isAtChargingPile) { // 回到充电桩后才可以重新构建地图。
                        showToast(R.string.s_backChargePile);
                    }
                    break;


                case R.id.id_mapAddRoute: // 添加路径
                    showToast(R.string.s_waitForAddFunction);
                    Log.i(TAG, "onClick: id_mapAddRoute被点击了");
                    break;

                default:
                    break;
            }
        }
    };


    /**
     * 按钮长按事件
     */
    private View.OnLongClickListener clickLongListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            switch (view.getId()) {
                case R.id.id_mapEstablish: // 地图构建，打开或停止rapp
                    Log.i(TAG, "onLongClick: make a map");
                    if (nodeConfiguration != null)
                        mHandler.sendEmptyMessage(mHandler_longClick_makeAMap);
                    else showToast(R.string.s_RosUninit);
                    break;

                case R.id.id_mapNavigation: // 地图导航，打开或停止rapp
                    Log.i(TAG, "onLongClick: map navigation");
//                    mHandler.sendEmptyMessage(mHandler_longClick_mapNavigation);
                    if (nodeConfiguration != null)
                        mHandler.sendEmptyMessage(mHandler_longClick_mapNavigation);
                    else showToast(R.string.s_RosUninit);
                    break;

                default:
                    return true;
            }

            return true;
        }
    };

    /**
     * todo 用layout代替单个控件
     * 设置make a map功能的按钮是否显示
     *
     * @param isReverse 是否置反
     * @param isShown   isReverse=false时，用此变量
     */
    private void mapMakeControlerView(boolean isReverse, boolean isShown) {
        Log.i(TAG, "mapMakeControlerView: ");
//        Log.i(TAG, "isReverse=" + isReverse + ", isShown=" + isShown);

        if (id_layout_map_makemap == null) {
            return;
        }
        Log.i(TAG, "mapMakeControlerView: id_layout_map_makemap.isShown()=" + id_layout_map_makemap.isShown() + ", isReverse=" + isReverse + ", isShown=" + isShown);
        if (isReverse) {
            id_layout_map_makemap.setVisibility(id_layout_map_makemap.isShown() ? View.GONE : View.VISIBLE);
        } else {
            id_layout_map_makemap.setVisibility(isShown ? View.VISIBLE : View.GONE);
        }

        if (id_layout_map_makemap.isShown()) { // 其他布局都消失
            if (id_layout_map_sign.isShown()) { // 标记子按钮都消失，标记按钮变成未选择状态。
                id_layout_map_sign.setVisibility(View.GONE);
                id_mapSign.setImageResource(R.mipmap.toolbar_mapmode_sign_normal);
                occupancyGridLayer.setEditFlag(false);
                mapPosePublisherLayer.setNoneMode();
            }
            if (id_layout_map_route.isShown()) { //路径子按钮都消失，路径按钮变成未选择状态
                id_layout_map_route.setVisibility(View.GONE);
                id_mapRoute.setImageResource(R.mipmap.toolbar_mapmode_route_normal);
            }
        }
        Log.i(TAG, "mapMakeControlerView: 地图控制按钮 id_layout_map_makemap.isShown()=" + id_layout_map_makemap.isShown());
    }

    /**
     * 设置map nav功能的按钮是否显示
     *
     * @param isReverse 是否置反
     * @param isShown   isReverse=false时，用此变量
     */
    private void mapNavControlerView(boolean isReverse, boolean isShown) {
        Log.i(TAG, "mapNavControlerView()");
    }

    /**
     * 设置长按事件几个按钮对应的背景图片
     *
     * @param flagMode    是否弹出标记按钮
     * @param initialMode 长按时，是不是设置初始位置
     * @param goalMode    长按时，是不是去目的地
     */
    private void setLongClickBg(boolean flagMode, boolean initialMode, boolean goalMode) {

        Log.i(TAG, "" + "" + "9: editMode=" + flagMode + ", initialNavMode=" + initialMode + ",goalNavMode=" + goalMode);

        if (flagMode) {
            occupancyGridLayer.setEditFlag(true);
            occupancyGridLayer.initHandler(mHandler, mHandler_modifyPosition, mHandler_cancelGoal);
        } else {
            occupancyGridLayer.setEditFlag(false);
        }
        // 标记
        id_mapSign.setImageResource(flagMode ? R.mipmap.toolbar_mapmode_sign_selected : R.mipmap.toolbar_mapmode_sign_normal);
        id_layout_map_sign.setVisibility(flagMode ? View.VISIBLE : View.GONE);
        // 标记当前位置
        id_mapStartingpoint.setImageResource(initialMode ? R.mipmap.toolbar_mapmode_startingpoint_selected : R.mipmap.toolbar_mapmode_startingpoint_normal);
        // 去目的地
        id_mapArrivepoint.setImageResource(goalMode ? R.mipmap.toolbar_mapmode_arrivalpoint_selected : R.mipmap.toolbar_mapmode_arrivalpoint_normal);

        if (flagMode) showToast(R.string.s_setEdit);
        if (goalMode) showToast(R.string.s_setGoalPose);
        if (initialMode) showToast(R.string.s_setInitialPose);
    }

//    /**
//     * 控制按钮是否显示
//     * @param isShowEstablishMapLayout 地图构建
//     * @param isShowFlagLayout 标记
//     * @param isShowRouteLayout 路径
//     */
//    private void setChildLayoutShow(boolean isShowEstablishMapLayout, boolean isShowFlagLayout, boolean isShowRouteLayout) {
//
//        // 按钮选中与否、布局是否弹出显示，每个boolean变量各管各的，不要管别的
//        id_layout_map_makemap.setVisibility(isShowEstablishMapLayout ? View.VISIBLE : View.GONE);
//
//        // 标记
//        if(isShowFlagLayout){
//            occupancyGridLayer.setEditFlag(true);
//            occupancyGridLayer.initHandler(mHandler, mHandler_modifyPosition, mHandler_cancelGoal);
//        }else {
//            occupancyGridLayer.setEditFlag(false);
//        }
//        id_mapSign.setImageResource(isShowFlagLayout ? R.mipmap.toolbar_mapmode_sign_selected : R.mipmap.toolbar_mapmode_sign_normal);
//        id_layout_map_sign.setVisibility(isShowFlagLayout ? View.VISIBLE : View.GONE);
//        // 路径
//        id_layout_map_route.setVisibility(isShowRouteLayout ? View.VISIBLE : View.GONE);
//        id_mapRoute.setImageResource(isShowRouteLayout ? R.mipmap.toolbar_mapmode_route_selected : R.mipmap.toolbar_mapmode_route_normal);
//    }

    /**
     * 路径按钮被单击时，界面控件的隐藏和显示
     */
    private void setRouteLayoutShow(boolean isRootLayoutShow) {
        id_mapRoute.setImageResource(isRootLayoutShow ? R.mipmap.toolbar_mapmode_route_selected : R.mipmap.toolbar_mapmode_route_normal);
        id_layout_map_route.setVisibility(isRootLayoutShow ? View.VISIBLE : View.GONE);

        if (isRootLayoutShow) {
            if (id_layout_map_makemap.isShown()) id_layout_map_makemap.setVisibility(View.GONE);
            if (id_layout_map_sign.isShown()) {
                id_layout_map_sign.setVisibility(View.GONE);
                id_mapSign.setImageResource(R.mipmap.toolbar_mapmode_sign_normal);
                occupancyGridLayer.setEditFlag(false);
                mapPosePublisherLayer.setNoneMode();
            }
        }
    }


    /**
     * 用RoconDescription做参数
     */
    private void init(RoconDescription mRoconDescriptionParam) {
        Log.i(TAG, "init(RoconDescription)");
        if (mRoconDescriptionParam == null) {
            safeDismissWaitingDialog();
            Log.e(TAG, "init(RoconDescription), mRoconDescriptionParam 是空的");
            return;
        }
        URI uri = null;
        try {
//            this.roconDescription = (RoconDescription) intent.getSerializableExtra(RoconDescription.UNIQUE_KEY);
//            this.roconDescription = mRoconDescriptionParam;
//            roconDescription = mRoconDescriptionParam;
            validatedConcert = false;
//            validateConcert(roconDescription.getMasterId()); // 拿到需要的roconDescription后，不能再连接，否则和前面一个重合，将返回failure，导致整个remocon无法连接。

            uri = new URI(roconDescription.getMasterId().getMasterUri());
            Log.i(TAG, "主机uri：" + uri.toString());
        } catch (ClassCastException e) {
//            Log.e(TAG, "Cannot get concert description from intent. " + e.getMessage());
            safeDismissWaitingDialog();
            e.printStackTrace();
            Log.e(TAG, "无法获取concert. " + e.toString());
//            throw new RosRuntimeException(e); // 任何情况，都不允许界面崩溃
        } catch (URISyntaxException e) {
            safeDismissWaitingDialog();
            e.printStackTrace();
//            throw new RosRuntimeException(e);
        }
        if (uri == null) {
            Log.e(TAG, "init: uri is null, 无法初始化init(nodeMainExecutorService)");
            safeDismissWaitingDialog();
            return;
        }
        nodeMainExecutorService.setMasterUri(uri); // 这是ConcertChecker的目的，连接ros并拿到已连接的uri。
        // Run init() in a new thread as a convenience since it often
        // requires network access. This would be more robust if it
        // had a failure handler for uncontactable errors (override
        // onPostExecute) that occurred when calling init. In reality
        // this shouldn't happen often - only when the connection
        // is unavailable inbetween validating and init'ing.
//        if (roconDescription.getCurrentRole() == null) {
//            chooseRole();
//        }
//        else {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
//                    while (!validatedConcert) {
//                        // should use a sleep here to avoid burnout
//                        try { Thread.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
//                    }
//                    Log.i(TAG, "init(Intent) passing control back to init(nodeMainExecutorService)");
                Log.i(TAG, "已连上concert，开始初始化init(nodeMainExecutorService)");
                init(nodeMainExecutorService);
                return null;
            }
        }.execute();
//        }
    }


    /**
     * 获取rapp信息，数据发出去后不可控制，所有不允许手动消失进度条
     */
    private void getRappList() { // final String nextAction){
        Log.i(TAG, "getRappList()");
        safeShowWaitingDialog("", getStringLocal(R.string.s_gettingRapp), false);
        try {
            final ServiceManager serviceManager = new ServiceManager(context);
            serviceManager.setFunction(getStringLocal(R.string.s_getRappList));
            serviceManager.registerCallbackGet(new ServiceManager.StatusCallbackGet() {
                @Override
                public void timeoutCallback() {
                    getRappListFailure(timeout);
                }

                @Override
                public void onSuccessCallback(GetRappListResponse arg0) {
//                    safeDismissWaitingDialog();
                    boolean isGone = getRappListSuccess(arg0); // , status.getNextAction());
                    if (isGone) {
                        safeDismissWaitingDialog();
                    }

                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();
                    getRappListFailure(ConnResult.failed);
                }
            });

            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.get_rapp_list.toString()), getNodeList(NodeName.get_rapp_list, status.getNextAction(), serviceManager));// "android/get_rapp_list"));
        } catch (Exception e) {
            e.printStackTrace();

            getRappListFailure(ConnResult.exception);
        }


    }

    /**
     * 获取rapp状态成功
     *
     * @return 是否隐藏进度条，如果有后续显示进度条的动作，则不隐藏
     */
    private boolean getRappListSuccess(GetRappListResponse arg0) { // , final String nextAction){
        Log.i(TAG, "getRappListSuccess: ");
        boolean isDismissPro = true;
        List<Rapp> availableRapps = arg0.getAvailableRapps();
        List<Rapp> runningRapps = arg0.getRunningRapps();
        Log.i(TAG, "可用rapp个数availableRapps:" + availableRapps.size() + ", 运行rapp的个数是runningRapps.size()=" + runningRapps.size()); // start

        if (status.isNextAction(Action_InitRemocon)) {
            Log.i(TAG, "getRappListSuccess: status.isRunningRapp()=" + status.isRunningRapp() + ",status.getServiceManager()=" + status.getServiceManager() + ",status.getConnectedNode()=" + status.getConnectedNode());
            // 刷新之前已启动rapp，不用更新界面，直接注入数据
            if (status.isRunningRapp() && status.getServiceManager() != null && status.getConnectedNode() != null) {
                Log.i(TAG, "getRappListSuccess: 原来已开启rapp的情况下刷新，原rapp=" + status.getRunningRappName());

                if (runningRapps.size() > 0) {
                    Log.i(TAG, "onSuccessCallback: 刷新之前已存在rapp,则直接注入数据，不改界面");
                    nodeMainExecutor.execute(status.getServiceManager(), nodeConfiguration.setNodeName(NodeName.start_rapp.toString()), getNodeList(NodeName.start_rapp, status.getNextAction(), status.getServiceManager())); // 导航，NodeMain.getDefaultNodeName()' on a null object reference // getStringLocal(R.string.map_nodeNameStartRapp)
                    status.setNextAction(Action_refresh_Nav_inject);
                    isExecutedGetPositions = false;

                    getAllPositions();
                } else {
                    Log.i(TAG, "onSuccessCallback: 刷新之前已存在rapp，但ros主机端已关闭了rapp。");
//                    Message msgUncheck = mHandler.obtainMessage();

                    if (status.isRunning(RappNameMakeAMap)) {
//                        mHandler.sendEmptyMessage(mHandler_MapMakeViewGone);
//
//                        msgUncheck.what = mHandler_MapMakeBackground;
//                        msgUncheck.obj = R.drawable.uncheck_mapmakecall;
//                        startRapp(status.getRunningRappName());
                    } else if (status.isRunning(RappNameMapNav)) {
//                        mHandler.sendEmptyMessage(mHandler_MapNavViewGone);
//
//                        msgUncheck.what = mHandler_MapNavBackground;
//                        msgUncheck.obj = R.mipmap.toolbar_mapmode_navigation_normal;
                    }
                    startRapp(status.getRunningRappName());
//                    mHandler.sendMessage(msgUncheck);

//                    startRapp(status.getRunningRappName());
                    isDismissPro = false;
                    Log.i(TAG, "getRappListSuccess: 刷新之前已连接：" + status.getRunningRappName() + "，主机那边已断掉，app应当重新连上此rapp");

                    status.setIsRunningRapp(false);
                    status.setRunningRappName("");
                    occupancyGridLayer.setStatusRapp(OccupancyGridLayerCustom.rapp.none);
                    status.setServiceManager(null);
                    status.setConnectedNode(null);
                    occupancyGridLayer.setSubscribe(false);
                }
//                return isDismissPro;
            } else if (runningRapps.size() > 0) { // 关构建地图，不关地图导航
                Log.i(TAG, "初次打开Remocon");
                status.setIsRunningRapp(true);
                status.setRunningRappName(runningRapps.get(0).getName());

                Log.i(TAG, "getRappListSuccess: runningRapps.get(0).getName()=" + runningRapps.get(0).getName());

                if (runningRapps.size() == 1 && runningRapps.get(0).getName().equals(RappNameMakeAMap)) {
                    // 已开启地图构建，且不是刷新按钮过来的，关闭
                    Log.i(TAG, "启动Remocon时，已启动了构建地图，将要停止它");
                    occupancyGridLayer.setStatusRapp(OccupancyGridLayerCustom.rapp.make_a_map);
                    stopRapp(Action_InitRemocon);
                    isDismissPro = false;
                } else if (runningRapps.size() == 1 && runningRapps.get(0).getName().equals(RappNameMapNav)) {
                    // 已开启地图导航
                    Log.i(TAG, "getRappListSuccess: 已开启地图导航，设置地图控件不收0地图");
                    occupancyGridLayer.setStatusRapp(OccupancyGridLayerCustom.rapp.map_nav);
//                    Log.e(TAG, "已开启地图导航，ROS没有传地图给app，需要发布地图。"); // 无地图，需要自己发布
                    isExecutedGetPositions = false;
                    status.setNextAction(Action_InitRemocon_RunningMapNav_PublishMap);
                    isExecutedGetPositions = false;
                    getMapList();
                    isDismissPro = false;

                    Message msgChecked = mHandler.obtainMessage();
                    Message msgUncheck = mHandler.obtainMessage();

                    // 设置背景，初始化运行状态。
                    msgChecked.what = mHandler_MapNavBackground;
                    msgChecked.obj = R.mipmap.toolbar_mapmode_navigation_selected;

                    msgUncheck.what = mHandler_MapMakeBackground;
                    msgUncheck.obj = R.mipmap.toolbar_mapmode_establish_normal;

                    Log.i(TAG, "设置控件相应状态");

                    mHandler.sendMessage(msgChecked);
                    mHandler.sendMessage(msgUncheck);

                    mapPosePublisherLayer.setNoneMode();
                } else if (runningRapps.size() > 1) {
                    // todo 报错，处理，如果有则记录每一次情况
                    StringBuilder sb = new StringBuilder();
                    sb.append("启动Remocon时，已有" + runningRapps.size() + "个Rapp在运行，待处理");
                    for (Rapp rapp : runningRapps) {
                        sb.append(rapp.getName() + "\t");
                    }
                    showToast(sb.toString());
                    Log.e(TAG, sb.toString());
                    isDismissPro = true;
                }
            } else if (runningRapps.size() <= 0 && !status.isRunningRapp()) {
                Log.i(TAG, "getRappListSuccess: 主机端没有正在运行的rapp，且当前程序没有开启rapp，可以启动任何一个Rapp!");
//                Log.i(TAG, "初次打开Remocon，主机端没有正在运行的rapp，且当前程序没有开启rapp，可以启动任何一个Rapp!");

                Log.i(TAG, "getRappListSuccess: 没有启动的rapp，将检查并清理地图");
                status.setIsRunningRapp(false);
                status.setRunningRappName("");
                occupancyGridLayer.setStatusRapp(OccupancyGridLayerCustom.rapp.none);
                occupancyGridLayer.clearTiles();
                isDismissPro = true;
            }
        }

        Log.i(TAG, "getRappListSuccess: status.getRunningRappName()=" + status.getRunningRappName() + ",status.getNextAction()=" + status.getNextAction());
        isInitNodeMain = true;
        return isDismissPro;
    }

    /**
     * 获取rapp list失败
     */
    private void getRappListFailure(ConnResult result) {
        isInitNodeMain = true;
        safeDismissWaitingDialog();

        Log.e(TAG, getStringLocal(R.string.s_Error_GettingRappListInfo) + "\t" + result + "\t" + status.getNextAction() + "\t runningRapp=" + status.getRunningRappName());
        if (result == timeout) {
            showToast(getStringLocal(R.string.s_ErrorConnToRosTimeout));
        } else if (result == ConnResult.failed) {
            showToast(getStringLocal(R.string.s_ErrorConnToRosFailed));
        } else if (result == ConnResult.exception) {
            showToast(getStringLocal(R.string.s_ErrorConnToRosException));
        }
    }

    /**
     * 获取地图列表
     */
    private List<MapListEntry> getMapList() {
        Log.i(TAG, "getMapList()");
        safeShowWaitingDialog("", getStringLocal(R.string.s_WaitingForMap), false);
        try {
            final ServiceManager serviceManager = new ServiceManager(context);
            serviceManager.setFunction(getStringLocal(R.string.s_functionName_list));

            serviceManager.registerCallbackListMaps(new ServiceManager.StatusCallbackListMaps() {
                @Override
                public void timeoutCallback() {
//                serviceManager.setTaskCancel();
                    getMapListFailed(getStringLocal(R.string.s_TimeoutToGetMapList), timeout);
                }

                @Override
                public void onSuccessCallback(ListMapsResponse message) {
                    boolean isGone = getMapListSuccess(message);
//                safeDismissWaitingDialog();

                    if (isGone) {
                        safeDismissWaitingDialog();
                    }

                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();
//                serviceManager.setTaskCancel();
                    getMapListFailed(e.toString(), ConnResult.failed);
                }

            });

            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.list_maps.toString()), getNodeList(NodeName.list_maps, status.getNextAction(), serviceManager)); // NodeConfiguration.setNodeName(java.lang.String)' on a null object reference // "android/list_maps"
        } catch (Exception e) {
            e.printStackTrace();
            getMapListFailed(e.toString(), ConnResult.exception);
        }

        return mListMaps;
    }

    /**
     * 获取地图成功
     */
    private boolean getMapListSuccess(ListMapsResponse message) {
        boolean isGone = true;
        mListMaps = message.getMapList();

        Log.i(TAG, "getMapList() Success, mListMaps is null?" + (mListMaps == null));
        if (mListMaps != null) Log.i(TAG, "mListMaps.size()=" + mListMaps.size());

        if (status.isNextAction(Action_InitRemocon_RunningMapNav_PublishMap) && status.isRunning(RappNameMapNav)) {
            if (mListMaps != null && mListMaps.size() > 0) {
//                        LoadMap(message.getMapList());
                publishMap(mListMaps.get(0).getMapId());

                Message msgChecked = mHandler.obtainMessage();
                Message msgUncheck = mHandler.obtainMessage();

                // 设置背景，初始化运行状态。
                msgChecked.what = mHandler_MapNavBackground;
                msgChecked.obj = R.mipmap.toolbar_mapmode_navigation_selected;

                msgUncheck.what = mHandler_MapMakeBackground;
                msgUncheck.obj = R.mipmap.toolbar_mapmode_establish_normal;

                Log.i(TAG, "设置控件相应状态");

                mHandler.sendMessage(msgChecked);
                mHandler.sendMessage(msgUncheck);

                isGone = false;
                Log.i(TAG, "打开Remoocon，已启动地图导航，有地图，将发布地图");
            } else if (mListMaps != null && mListMaps.size() <= 0) {
                Log.e(TAG, "打开Remoocon，已启动地图导航，没有地图可发布，停止地图导航");
//                        stopRapp(getStringLocal(R.string.s_actionStopRappOnly));
//                        stopRapp(Action_InitRemocon_RunningMapNav_PublishMap);
                stopRapp(status.getNextAction());
                isGone = false;
            }
        } else if (status.isNextAction(Action_SavedMap_StartNav) || status.isNextAction(Action_LongClick_StartMapNav)) {
            if (mListMaps != null && mListMaps.size() > 0) {
//                        publishMap(mListMaps.get(0).getMapId());
                mapId = mListMaps.get(0).getMapId();
                Log.i(TAG, "id=" + mapId + ",将开启地图导航");
                startRapp(RappNameMapNav);
                isGone = false;
            } else if (mListMaps != null && mListMaps.size() <= 0) {
//                        showToast(R.string.s_Error_StartMapNavWithoutMap);
//                        stopRapp(status.getNextAction());
                status.setNextAction("");
                showToast(R.string.s_noMap);

                Log.e(TAG, "没有地图，无法启动地图导航，请先构建地图。");
            }
        }
        // 构建新地图、重新构建地图
        else if (status.isNextAction(status.Action_LongClick_StartMapMake) || status.isNextAction(status.Action_ReNewMap)) {
            // 如果有地图，就清理，没地图，就直接清理坐标
            if (mListMaps != null && mListMaps.size() > 0) {
                deleteMaps();
                isGone = false;
                Log.i(TAG, "有地图，将删除地图");
            } else if (mListMaps != null && mListMaps.size() <= 0) {
                // 不获取坐标，直接清理
//                        getAllPositions();
                isGone = false;
                Log.i(TAG, "无地图，将获取所有位置坐标");
                resetAllPositions();

            }
        } else if (status.isNextAction(status.Action_Test)) {
            if (mListMaps != null && mListMaps.size() > 0) {
                Log.i(TAG, "getMapListSuccess: 有地图，将发布地图");
                publishMap(mListMaps.get(0).getMapId());
            } else if (mListMaps != null && mListMaps.size() <= 0) {
                Log.e(TAG, "getMapListSuccess: 无地图");
                isGone = true;
            }
        } else {
            Log.e(TAG, "getMapListSuccess: 未知目的获取地图成功,mapSize=" + mListMaps.size());
        }
        return isGone;
    }

    /**
     * 获取地图失败处理
     */
    private void getMapListFailed(String errMsg, ConnResult result) {
        Log.e(TAG, "getMapListFailed: " + result + "\t" + status.getNextAction());
//        if(sbTemp.length() > 0) sbTemp.delete(0, sbTemp.length());

        if (status.isNextAction(Action_InitRemocon_RunningMapNav_PublishMap) && status.isRunning(RappNameMapNav)) {
//            stopRapp(getStringLocal(R.string.s_actionStopRappOnly));
            Log.e(TAG, "将停止地图导航");
            stopRapp(status.Action_stopRappOnly);
//            sbTemp.append("\n" + getStringLocal(R.string.s_Error_PublishMapStopMapNav));
            return; // 直接停止，不toast提示了。
        } else if (status.isNextAction(status.Action_LongClick_StartMapMake)) {
            status.setNextAction("");
            Log.i(TAG, "获取地图失败，不启动构建地图");
            safeDismissWaitingDialog();
//            sbTemp.append("\n" + getStringLocal(R.string.s_Error_PublishMapDontStartMapMak));
        } else if (status.isNextAction(Action_LongClick_StartMapNav)) {
            status.setNextAction("");
            Log.i(TAG, "获取地图失败，不启动地图导航");
            safeDismissWaitingDialog();
//            sbTemp.append("\n" + getStringLocal(R.string.s_Error_PublishMapDontStartMapNav));
        } else if (status.isNextAction(status.Action_ReNewMap)) {
            status.setNextAction("");
            Log.i(TAG, "获取地图失败，不知是否可以重新构建地图");
            safeDismissWaitingDialog();
//            sbTemp.append(getStringLocal(R.string.s_ErrorToNewMap));
        } else if (status.isNextAction(Action_SavedMap_StartNav)) {
            status.setNextAction("");
            Log.i(TAG, "获取地图失败，无法启动地图导航");
            safeDismissWaitingDialog();

        } else {
            Log.e(TAG, "getMapListFailed: 未知目的，未处理");
        }

//        sbTemp.append("\n" + errMsg);
//        safeShowNotiDialogCustom(getStringLocal(R.string.s_ErrorFailedToRos));
//        showToast(getStringLocal(R.string.s_ErrorFailedToRos));
        if (result.equals(ConnResult.exception))
            showToast(getStringLocal(R.string.s_ErrorConnToRosException));
        else if (result.equals(ConnResult.failed))
            showToast(getStringLocal(R.string.s_ErrorConnToRosFailed));
        else if (result.equals(timeout))
            showToast(getStringLocal(R.string.s_ErrorConnToRosTimeout));
        else Log.e(TAG, "getMapListFailed: 判断出错，未处理：" + result);
//        Log.e(TAG, sbTemp.toString());

    }


    /**
     * 发布地图是否成功
     */
    private boolean isPublishMapSuccess = false;

    /**
     * 加载地图到界面
     */
    private boolean publishMap(final String mapId) {
        Log.i(TAG, "publishMap(), mapId=" + mapId);
        if (mapId == null || mapId.equals("")) {
            Log.e(TAG, "publishMap: 地图id空，无法发布地图");
            return false;
        }
        isPublishMapSuccess = false;
        occupancyGridLayer.setIsPublishingMap(true);
        safeShowWaitingDialog("", getStringLocal(R.string.s_WaitingForPublishMap), false);

        try {
            ServiceManager serviceManager = new ServiceManager(context);
            serviceManager.setFunction(getStringLocal(R.string.s_functionName_publish));
            serviceManager.setMapId(mapId);

            serviceManager.registerCallbackPublishMap(new ServiceManager.StatusCallbackPublishMap() {
                @Override
                public void timeoutCallback() {
                    publishMapFailed(getStringLocal(R.string.s_TimeoutToPublishMap), timeout);
                }

                @Override
                public void onSuccessCallback(PublishMapResponse message) {
                    Log.i(TAG, "发布地图成功," + status.getNextAction());
                    boolean isGone = true;
//                    safeDismissWaitingDialog();
                    isPublishMapSuccess = true;

                    if (status.isNextAction(Action_InitRemocon_RunningMapNav_PublishMap)) {
                        Log.i(TAG, "进入Remocon-已启动导航-发布地图成功");
//                        status.setNextAction("");

                        isGone = false; // 等收到地图&&收到坐标后才停止转圈圈
                    } else if (status.isNextAction(Action_LongClick_StartMapNav) || status.isNextAction(Action_SavedMap_StartNav)) { // 发布地图后，启动地图导航
//                        Log.i(TAG, "将启动地图导航");
//                        startRapp(RappNameMapNav);
                        isGone = false;
                        LayoutMainMAP.this.mapId = null;
                        if (status.isNextAction(Action_SavedMap_StartNav)) { // 如果是从保存地图过来的，就发送初始位置，其他情况不发。
                            if (lastPose != null) {
                                mapPosePublisherLayer.publishLastPose(lastPose.getX(), lastPose.getY(), lastPose.getZ(), lastPose.getW());
                                Log.i(TAG, "startRappSuccess: 已发布初始坐标=(" + lastPose.getX() + ", " + lastPose.getY() + ", " + lastPose.getZ() + ", " + lastPose.getW() + ")");
                            } else {
                                Log.e(TAG, "想发布最后的坐标，但发布失败，lastPose is null");
                            }
//                            Log.i(TAG, "保存地图成功-且启动地图导航成功，不退出Remocon");
                        }
//                        refreshMapView(); // 收到地图的时候才刷新，不然白刷了
                        // 获取标记，把标记画在地图导航的地图上
                    } else if (status.isNextAction(status.Action_Test)) {
                    }
                    Log.i(TAG, "onSuccessCallback: 将获取标记");
                    getAllPositions();

                    if (isGone) {
                        safeDismissWaitingDialog();
                    }
                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();
                    publishMapFailed(e.toString(), ConnResult.failed);
                }
            });
            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.publish_map.toString()), getNodeList(NodeName.publish_map, status.getNextAction(), serviceManager)); // "android/publish_map"
        } catch (Throwable ex) {
            ex.printStackTrace();
            publishMapFailed(ex.toString(), ConnResult.exception);
        }

        return isPublishMapSuccess;
    }

    /**
     * 发布地图失败处理
     */
    private void publishMapFailed(String errMsg, ConnResult result) {
//        Log.i(TAG, "publishMapFailed()");
        isPublishMapSuccess = false;

        Log.e(TAG, "publishMapFailed: " + result + "\t" + status.getNextAction());
//        if(sbTemp.length() > 0) sbTemp.delete(0, sbTemp.length());
//        sbTemp.append(getStringLocal(R.string.s_ErrorToPublishMap));

        if (status.isNextAction(Action_InitRemocon_RunningMapNav_PublishMap)) {
//            status.setNextAction("");

            Log.i(TAG, "进入Remocon-已启动导航-发布地图失败-将停止地图导航。");
//            sbTemp.append("\n" + getStringLocal(R.string.s_ErrorPublishMapWillStopMapNav));
            stopRapp(Action_InitRemocon_RunningMapNav_PublishMap);
        } else if (status.isNextAction(Action_SavedMap_StartNav) || status.isNextAction(Action_LongClick_StartMapNav)) {
            status.setNextAction("");
            safeDismissWaitingDialog();
            mapId = null;
//            sbTemp.append("\n" + getStringLocal(R.string.s_ErrorPublishMap) + "\n" + getStringLocal(R.string.s_ErrorStartMapNav));
        } else {
            Log.e(TAG, "publishMapFailed: 未知目的，未处理");
        }

//        sbTemp.append("\n" + errMsg);

//        safeShowNotiDialogCustom(getStringLocal(R.string.s_ErrorFailedToRos));
//        showToast(getStringLocal(R.string.s_ErrorFailedToRos));
        if (result.equals(ConnResult.exception))
            showToast(getStringLocal(R.string.s_ErrorConnToRosException));
        else if (result.equals(ConnResult.failed))
            showToast(getStringLocal(R.string.s_ErrorConnToRosFailed));
        else if (result.equals(timeout))
            showToast(getStringLocal(R.string.s_ErrorConnToRosTimeout));
        else Log.e(TAG, "publishMapFailed: 判断出错，未处理：" + result);
//        Log.e(TAG, "发布地图失败,"+result + "\t" + sbTemp.toString());
    }

    /**
     * 开始或停止follower服务
     */
    private void followerService(final byte newState) {
        Log.i(TAG, "followerService(), newState=" + newState);
        safeShowWaitingDialog("", getStringLocal(R.string.s_gettingFollowerService), false);
        try {
            ServiceManager serviceManager = new ServiceManager(context);

            serviceManager.setFunction(getStringLocal(R.string.s_functionName_follower));
            serviceManager.setFollowerState(newState);
            // serviceManager.setNameResolver(getMasterNameSpace());
            serviceManager.registerCallbackFollower(new ServiceManager.StatusCallbackFollower() {
                @Override
                public void timeoutCallback() {
                    followerServiceFailed(newState, timeout);
                }

                @Override
                public void onSuccessCallback(SetFollowStateResponse arg0) {
                    safeDismissWaitingDialog();

                    // 设置背景图
                    Message msg = mHandler.obtainMessage();
                    msg.what = mHandler_btnFollowerBG;

                    if (arg0.getResult() == SetFollowStateResponse.OK && newState == SetFollowStateRequest.STOPPED) {
                        showToast(R.string.s_successFollowerServiceStop);

                        msg.obj = R.mipmap.toolbar_mapmode_follow_normal;
                        mHandler.sendMessage(msg);

                        status.setFollow(false);
                    } else if (arg0.getResult() == SetFollowStateResponse.OK && newState == SetFollowStateRequest.FOLLOW) {
                        showToast(R.string.s_successFollowerServiceFollower);

                        msg.obj = R.mipmap.toolbar_mapmode_follow_selected;
                        mHandler.sendMessage(msg);

                        status.setFollow(true);
                    } else if (arg0.getResult() == SetFollowStateResponse.ERROR) {
                        showToast(R.string.s_failureToFollower);

                        Log.i(TAG, "onSuccessCallback: msg.obj=" + msg.obj + ", " + getStringLocal(R.string.s_failureToFollower));
                    } else {
                        Log.e(TAG, "onSuccessCallback: 未知错误=" + arg0.getResult());
                    }
                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();

                    if (e.toString().contains("No such service"))
                        followerServiceFailed(newState, ConnResult.NoFollowService);
                    else followerServiceFailed(newState, ConnResult.failed);
                }
            });
            mFollowerNewState = newState;
            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.follower.toString()), getNodeList(NodeName.follower, status.getNextAction(), serviceManager)); // "android/follower"
        } catch (Exception e) {
            e.printStackTrace();


            followerServiceFailed(newState, ConnResult.exception);
        }
    }

    /**
     * 跟随服务失败
     */
    private void followerServiceFailed(final byte newState, ConnResult result) {
        String stat = (newState == SetFollowStateRequest.STOPPED ? "停止跟随" : "开始跟随");
        Log.e(TAG, "followerServiceFailed: " + result + "\t" + status.getNextAction() + (newState == SetFollowStateRequest.STOPPED ? getStringLocal(R.string.s_Error_StopFollowerService) : getStringLocal(R.string.s_Error_StartFollowerService)));
        safeDismissWaitingDialog();
        if (result == timeout) {
            showToast(stat + ": " + getStringLocal(R.string.s_ErrorConnToRosTimeout));
        } else if (result == ConnResult.NoFollowService) {
            showToast(stat + ": " + getStringLocal(R.string.s_ErrorNoFollowService));
        } else if (result == ConnResult.failed) {
            showToast(stat + ": " + getStringLocal(R.string.s_ErrorConnToRosFailed));
        } else if (result == ConnResult.exception) {
            showToast(stat + ": " + getStringLocal(R.string.s_ErrorConnToRosException));
        }
    }

    /**
     * 刷新地图显示
     */
    private void refreshMapView() {
        Log.i(TAG, "refreshMapView()");
//        showToast(R.string.s_RefreshingMap);
//        id_map.getCamera().jumpToFrame(getStringLocal(R.string.s_mapRobotLayer));
//        id_map.getCamera().jumpToFrame(getStringLocal(R.string.s_mapLaserScanLayer));
//        id_map.getCamera().jumpToFrame("map");
    }

    /**
     * 删除地图成功，如果出错了就不删除了
     */
    private boolean isDeleteMapSuccess = false;

    /**
     * 删除地图了
     */
    private boolean deleteMaps() { //mListMaps = new

        Log.i(TAG, "deleteMaps()");
        if (mListMaps.size() <= 0) {
            Log.e(TAG, "deleteMaps: 没有地图，无需删除，mListMaps.size() <= 0");
            if (!status.getNextAction().equals("")) {
                status.setNextAction("");
            }
            return false;
        }
        safeShowWaitingDialog(getStringLocal(R.string.s_Waiting), getStringLocal(R.string.s_WaitingForDeleteMap), false);

        try {
            ServiceManager serviceManager = new ServiceManager(context);
            serviceManager.setFunction(getStringLocal(R.string.s_functionName_delete));
            serviceManager.setMapId(mListMaps.get(0).getMapId());

            Log.i(TAG, "mListMaps.get(0).getOccupancyGrid()=" + mListMaps.get(0).getMapId());

            serviceManager.registerCallbackDeleteMaps(new ServiceManager.StatusCallbackDeleteMaps() {
                @Override
                public void timeoutCallback() {
                    deleteMapFailed(getStringLocal(R.string.s_TimeoutToDeleteMap), timeout);
                }

                @Override
                public void onSuccessCallback(DeleteMapResponse message) {
                    Log.i(TAG, "删除地图成功");
                    mListMaps.remove(0);
                    isDeleteMapSuccess = true;
                    if (status.isNextAction(status.Action_LongClick_StartMapMake) || status.isNextAction(status.Action_ReNewMap)) {
                        // 不获取坐标，直接清理
                        resetAllPositions();
                    } else {
                        safeDismissWaitingDialog();
                    }
                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();

                    deleteMapFailed(e.toString(), ConnResult.failed);
                }

            });

            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.delete_map.toString()), getNodeList(NodeName.delete_map, status.getNextAction(), serviceManager)); // "android/delete_map"

        } catch (Exception e) {
            e.printStackTrace();
            deleteMapFailed(e.toString(), ConnResult.exception);

        }
        return isDeleteMapSuccess;
    }

    /**
     * 删除地图失败
     */
    private void deleteMapFailed(String errMsg, ConnResult result) {
        Log.e(TAG, "deleteMapFailed: " + result + "\t" + status.getNextAction());
        safeDismissWaitingDialog();
        isDeleteMapSuccess = false;

//        if(sbTemp.length() > 0) sbTemp.delete(0, sbTemp.length());

        if (status.isNextAction(status.Action_LongClick_StartMapMake)) {
            status.setNextAction("");
//            sbTemp.append(getStringLocal(R.string.s_ErrorToDeleteMapFailedToStartMapMake));
        } else if (status.isNextAction(status.Action_ReNewMap)) {
            status.setNextAction("");
//            sbTemp.append(getStringLocal(R.string.s_ErrorToNewMap));
        } else {
            Log.e(TAG, "deleteMapFailed: 未知目的，未处理");
        }

//        sbTemp.append("\n" + errMsg);
//        safeShowNotiDialogCustom(getStringLocal(R.string.s_ErrorToDeleteMap)+"! "+sbTemp.toString());
//        showToast(getStringLocal(R.string.s_ErrorFailedToRos));
        if (result.equals(ConnResult.exception))
            showToast(getStringLocal(R.string.s_ErrorConnToRosException));
        else if (result.equals(ConnResult.failed))
            showToast(getStringLocal(R.string.s_ErrorConnToRosFailed));
        else if (result.equals(timeout))
            showToast(getStringLocal(R.string.s_ErrorConnToRosTimeout));
        else Log.e(TAG, "deleteMapFailed: 判断出错，未处理：" + result);

    }

    /**
     * 获取所有坐标位置
     */
    private void getAllPositions() { //mListMaps = new
        Log.i(TAG, "getAllPositions(), 获取坐标前应先清空坐标列表");
        safeShowWaitingDialog(getStringLocal(R.string.s_Waiting), getStringLocal(R.string.s_WaitingForGetAllPositions), false);
        occupancyGridLayer.clearFlagBeanList();
        resetFlagId();
        isExecutedGetPositions = false;
        try {
            final ServiceManager serviceManager = new ServiceManager(context);
            serviceManager.setFunction(getStringLocal(R.string.s_functionName_GetAllPositions));

            serviceManager.registerCallbackGetPositions(new ServiceManager.StatusCallbackGetPositions() {
                @Override
                public void timeoutCallback() {
                    getAllPositionFailed(getStringLocal(R.string.s_TimeoutToGetAllPositions), timeout);
                }

                @Override
                public void onSuccessCallback(GetAllPositionResponse message) {
                    isExecutedGetPositions = true;
                    boolean isGone = getAllPositionSuccess(message);
                    if (isGone) {
                        safeDismissWaitingDialog();
                    }
                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();

                    getAllPositionFailed(e.toString(), ConnResult.failed);
                }

            });

            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.get_all_positions.toString()), getNodeList(NodeName.get_all_positions, status.getNextAction(), serviceManager));  // "android/get_all_positions"

        } catch (Exception e) {
            e.printStackTrace();
            getAllPositionFailed(e.toString(), ConnResult.exception);
        }
    }

    /**
     * 获取坐标成功
     */
    private boolean getAllPositionSuccess(GetAllPositionResponse message) {
        boolean isGone = true;

        List<VoiceLocalsEntry> voicePositions = message.getVoicePositions();
        Log.i(TAG, "onSuccessCallback: 获取位置坐标成功，坐标个数：" + voicePositions.size() + "," + status.getNextAction());
//        StringBuilder sb = new StringBuilder();
//        for (VoiceLocalsEntry position : voicePositions) {
//            sb.append(position.getPositionId() + ", " + position.getPositionName() + ", [" + position.getLocalX() + ", " + position.getLocalY() + ", " + position.getLocalZ() + ", " + position.getLocalW() + "] \n");
//        }
//        if (sb.length() > 0) Log.i(TAG, "onSuccessCallback: 位置详情：id,名字,x,y,z,w = " + sb.toString());

        if ((status.isNextAction(status.Action_LongClick_StartMapMake) || status.isNextAction(status.Action_ReNewMap)) && message.getVoicePositions().size() <= 0) {
            Log.i(TAG, "onSuccessCallback: 没有坐标，将启动构建地图");
            startRapp(RappNameMakeAMap);
            isGone = false;
        } else if (status.isNextAction(Action_LongClick_StartMapNav) || status.isNextAction(Action_InitRemocon_RunningMapNav_PublishMap) || status.isNextAction(Action_SavedMap_StartNav) || status.isNextAction(Action_refresh_Nav_inject)) { // nextAction = ""
            occupancyGridLayer.clearFlagBeanList();

            // 如果还没有初始化，则可以输入文字。
            if (occupancyGridLayer.getTextShapeFactory() == null) {
                List<String> strListTemp = new ArrayList<String>();
                for (int i = 0; i < voicePositions.size(); i++) {
                    String str = voicePositions.get(i).getPositionName();
                    String strSingleTemp = ""; //str.substring(j, j + 1);
                    for (int j = 0; j < str.length(); j++) {
                        // 取单个字
                        if (str.length() > 1) strSingleTemp = str.substring(j, j + 1);
                        else strSingleTemp = str.substring(j);
                        Log.i(TAG, "onSuccessCallback: strListTemp.contains(strSingleTemp)=" + strListTemp.contains(strSingleTemp));
                        if (!strListTemp.contains(strSingleTemp)) {
                            strListTemp.add(strSingleTemp);
                            Log.i(TAG, "onSuccessCallback: 添加字：" + strSingleTemp);
                        }
                    }
                }
                occupancyGridLayer.setStrList(strListTemp);
            }


            for (int i = 0; i < voicePositions.size(); i++) {
                occupancyGridLayer.addFlagTempList(voicePositions.get(i).getLocalX(), voicePositions.get(i).getLocalY(), String.valueOf(voicePositions.get(i).getPositionId()), voicePositions.get(i).getPositionName());
                // 更新id号：固定标记
                if (voicePositions.get(i).getPositionId() > positionIdFixed_ && voicePositions.get(i).getPositionId() < positionIdCustom_ && voicePositions.get(i).getPositionId() > positionIdFixed) {
                    positionIdFixed = voicePositions.get(i).getPositionId();
                }
                // 更新id号：自定义标记
                else if (voicePositions.get(i).getPositionId() > positionIdCustom) {
                    positionIdCustom = voicePositions.get(i).getPositionId();
                }
                Log.i(TAG, "onSuccessCallback: 获得标记：" + voicePositions.get(i).getPositionName() + ",voicePositions.get(i).getPositionId()=" + voicePositions.get(i).getPositionId() + ",positionIdFixed=" + positionIdFixed);
            }

            occupancyGridLayer.setReadyText(true);

            if (occupancyGridLayer.getTextShapeFactory() != null) {
                occupancyGridLayer.resetFlagBeanList();
            }

            if (occupancyGridLayer.isExistsMap()) {
                isGone = true;
                status.setNextAction("");
            } else {
                Log.i(TAG, "getAllPositionSuccess: 将等待地图，action=" + status.getNextAction());
                isGone = false;
                isWaitforMap = true;
                mWaitMapFlag++;
                mHandler.sendMessageDelayed(mHandler.obtainMessage(mHandler_TimeoutForWaitMap, mWaitMapFlag), mTimeoutWaitMap * 1000);
            }
        } else if (status.isNextAction(status.Action_Test)) {
        } else {
            Log.e(TAG, "getAllPositionSuccess: 未知目的，未处理");
        }


        return isGone;
    }

    /**
     * 获取坐标失败
     */
    private void getAllPositionFailed(String errMsg, ConnResult result) {
        Log.i(TAG, "getAllPositionFailed: " + result + "\t" + status.getNextAction());

        safeDismissWaitingDialog();
//        if(sbTemp.length() > 0) sbTemp.delete(0, sbTemp.length());

        if (status.isNextAction(status.Action_LongClick_StartMapMake)) {
//            status.setNextAction("");
//            sbTemp.append(getStringLocal(R.string.s_ErrorToStartMapMake));
        } else if (status.isNextAction(status.Action_ResetPositionsOnly)) { // 只是清理位置坐标
//            status.setNextAction("");;
//            sbTemp.append(getStringLocal(R.string.s_ErrorToResetPositiosnOnly));
        } else if (status.isNextAction(status.Action_LongClick_StartMapMake)) {
//            status.setNextAction("");
//            sbTemp.append(getStringLocal(R.string.s_ErrorToNewMap));
        } else if (status.isNextAction(Action_SavedMap_StartNav) || status.isNextAction(Action_LongClick_StartMapNav) || status.isNextAction(Action_InitRemocon_RunningMapNav_PublishMap)) {
//            status.setNextAction("");
            Log.i(TAG, "getAllPositionFailed: 启动地图导航后，获取坐标失败");
        } else {
            Log.e(TAG, "getAllPositionFailed: 目的未知，未处理");
        }
        status.setNextAction("");

//        sbTemp.append("\n" + errMsg);

//        safeShowNotiDialogCustom(getStringLocal(R.string.s_ErrorFailedToRos));
//        showToast(getStringLocal(R.string.s_ErrorFailedToRos));
        if (result.equals(ConnResult.exception))
            showToast(getStringLocal(R.string.s_ErrorConnToRosException));
        else if (result.equals(ConnResult.failed))
            showToast(getStringLocal(R.string.s_ErrorConnToRosFailed));
        else if (result.equals(timeout))
            showToast(getStringLocal(R.string.s_ErrorConnToRosTimeout));
        else Log.e(TAG, "getAllPositionFailed: 判断出错，未处理：" + result);
//        Log.e(TAG, "获取坐标失败，"+result+"\t" + sbTemp.toString());
    }

    private boolean isResetAllPositions = false;

    /**
     * 清空位置坐标
     */
    private boolean resetAllPositions() { //mListMaps = new
        Log.i(TAG, "resetAllPositions()");
        safeShowWaitingDialog("", getStringLocal(R.string.s_WaitingForResetAllPositions), false);

        try {
            final ServiceManager serviceManager = new ServiceManager(context);
            serviceManager.setFunction(getStringLocal(R.string.s_functionName_ResetAllPositions));

            serviceManager.registerCallbackResetPositions(new ServiceManager.StatusCallbackResetPositions() {
                @Override
                public void timeoutCallback() {
                    resetAllPositionFailed(getStringLocal(R.string.s_TimeoutToResetAllPositions), timeout);
                }

                @Override
                public void onSuccessCallback(ResetAllPositionResponse message) {
                    boolean isGone = true;
                    Log.i(TAG, "onSuccessCallback: message.getMessage()=" + message.getMessage() + ",message.toString()=" + message.toString() + ",message.getResult()=" + message.getResult());

                    Log.i(TAG, "清空位置坐标成功");
                    isResetAllPositions = true;
                    resetFlagId();

                    // 清理坐标成功后，如果要启动构建地图，就启动
                    if (status.isNextAction(status.Action_LongClick_StartMapMake) || status.isNextAction(status.Action_ReNewMap)) {
                        startRapp(RappNameMakeAMap);
                        isGone = false;
                    } else if (status.isNextAction(status.Action_ResetPositionsOnly)) { // 只是清理位置坐标
                    } else {
                        Log.e(TAG, "onSuccessCallback: 目的未知，未处理" + "\t" + status.getNextAction());
                    }

                    if (isGone) {
                        safeDismissWaitingDialog();
                    }
                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();
                    safeDismissWaitingDialog();

                    resetAllPositionFailed(e.toString(), ConnResult.failed);
                }
            });

            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.reset_all_positions.toString()), getNodeList(NodeName.reset_all_positions, status.getNextAction(), serviceManager)); // "android/reset_all_positions"

        } catch (Exception e) {
            safeDismissWaitingDialog();
            e.printStackTrace();
            isResetAllPositions = false;

            resetAllPositionFailed(e.toString(), ConnResult.exception);

        }
        return isResetAllPositions;
    }

    /**
     * 清理坐标失败
     */
    private void resetAllPositionFailed(String errMsg, ConnResult result) {
        Log.i(TAG, "resetAllPositionFailed: " + result + "\t" + status.getNextAction());
        safeDismissWaitingDialog();
        isResetAllPositions = false;

//        if(sbTemp.length() > 0) sbTemp.delete(0, sbTemp.length());

        if (status.isNextAction(status.Action_LongClick_StartMapMake)) {
//            status.setNextAction("");
//            sbTemp.append(getStringLocal(R.string.s_ErrorToStartMapMake));
        } else if (status.isNextAction(status.Action_ResetPositionsOnly)) { // 只是清理位置坐标
//            status.setNextAction("");;
//            sbTemp.append(getStringLocal(R.string.s_ErrorToResetPositiosnOnly));
        } else if (status.isNextAction(status.Action_ReNewMap)) { // 只是清理位置坐标
//            status.setNextAction("");;
//            sbTemp.append(getStringLocal(R.string.s_ErrorToNewMap));
        } else {
            Log.e(TAG, "resetAllPositionFailed: 目的未知，未处理");
        }
        status.setNextAction("");
        ;

//        sbTemp.append("\n" + errMsg);

//        safeShowNotiDialogCustom(getStringLocal(R.string.s_ErrorToResetAllPositions)+"! "+sbTemp.toString());
//        safeShowNotiDialogCustom(getStringLocal(R.string.s_ErrorFailedToRos));
//        showToast(getStringLocal(R.string.s_ErrorFailedToRos));
        if (result.equals(ConnResult.exception))
            showToast(getStringLocal(R.string.s_ErrorConnToRosException));
        else if (result.equals(ConnResult.failed))
            showToast(getStringLocal(R.string.s_ErrorConnToRosFailed));
        else if (result.equals(timeout))
            showToast(getStringLocal(R.string.s_ErrorConnToRosTimeout));
        else Log.e(TAG, "resetAllPositionFailed: 判断出错，未处理：" + result);
//        Log.e(TAG, "清理坐标失败，" + sbTemp.toString());
    }


    /**
     * 保存地图，名字固定
     */
    private void saveMap() {
        Log.i(TAG, "saveMap()");
//        Log.i(TAG, "开始保存地图");
        safeShowWaitingDialog("", getStringLocal(R.string.s_SavingMap), false);
        try {
            final ServiceManager serviceManager = new ServiceManager(context);


            serviceManager.setMapName(getStringLocal(R.string.s_croom));
            serviceManager.setFunction(getStringLocal(R.string.s_functionName_SaveMap));
            serviceManager.registerCallbackSaveMap(new ServiceManager.StatusCallbackSaveMap() {
                @Override
                public void timeoutCallback() {
                    saveMapFailed(timeout);
                }

                @Override
                public void onSuccessCallback(SaveMapResponse arg0) {
//                    safeDismissWaitingDialog();
                    showToast(R.string.s_MapSavingSuccess);
                    Log.i(TAG, getStringLocal(R.string.s_MapSavingSuccess));

                    savePositions();
                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();
                    saveMapFailed(ConnResult.failed);
                }
            });

            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.save_map.toString()), getNodeList(NodeName.save_map, status.getNextAction(), serviceManager)); // "android/save_map"

        } catch (Exception e) {
            e.printStackTrace();
            saveMapFailed(ConnResult.exception);
        }
    }

    /**
     * 保存地图失败
     */
    private void saveMapFailed(ConnResult result) {
        safeDismissWaitingDialog();
        status.setNextAction("");

        Log.e(TAG, "saveMapFailed: " + result + "\t" + status.getNextAction());
        if (result == timeout) {
            showToast(getStringLocal(R.string.s_ErrorConnToRosTimeout));
        } else if (result == ConnResult.failed) {
            showToast(getStringLocal(R.string.s_ErrorConnToRosFailed));
        } else if (result == ConnResult.exception) {
            showToast(getStringLocal(R.string.s_ErrorConnToRosException));
        }
    }

    /**
     * 添加坐标位置
     */
    private void addPosition(final String positionName) {

        Log.i(TAG, "addPosition()");
        try {
            final ServiceManager serviceManager = new ServiceManager(context);

            serviceManager.setPositionId(String.valueOf(isPositionIdFixed ? positionIdFixed + 1 : positionIdCustom + 1)); //
            serviceManager.setPositionName(positionName);
            serviceManager.setFunction(getStringLocal(R.string.s_functionName_AddPosition));
            serviceManager.registerCallbackAddPosition(new ServiceManager.StatusCallbackAddPosition() {
                @Override
                public void timeoutCallback() {
                    addPositionFailed(timeout);
                }

                @Override
                public void onSuccessCallback(AddPositionResponse arg0) { //自动加1，下次好继续用。
                    if (arg0.getResult()) {
                        Log.i(TAG, "添加位置成功");
                        occupancyGridLayer.addFlagBean(arg0.getX(), arg0.getY(), String.valueOf(isPositionIdFixed ? ++positionIdFixed : ++positionIdCustom), positionName);
                        showToast(getStringLocal(R.string.s_addPositionSuccess) + ":" + positionName + getStringLocal(R.string.s_customPositionDisplayNext));
                    } else {
                        Log.i(TAG, "标记已存在，两标记之间应大于5厘米！");
                        safeShowNotiDialogCustom(getStringLocal(R.string.s_ErrorToAddPositionRepeat));
                        Log.e(TAG, getStringLocal(R.string.s_ErrorToAddPositionRepeat));
                    }

                    serviceManager.setPositionName("");
                    serviceManager.setFunction("");
                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();
                    addPositionFailed(ConnResult.failed);
                }
            });

            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.add_position.toString()), getNodeList(NodeName.add_position, status.getNextAction(), serviceManager)); // "android/add_position"
        } catch (Exception e) {
            e.printStackTrace();
            addPositionFailed(ConnResult.exception);
        }
    }

    /**
     * 添加标记失败
     */
    private void addPositionFailed(ConnResult result) {
        Log.e(TAG, "addPositionFailed: " + result + "\t" + status.getNextAction());

        if (result == timeout) {
            showToast(getStringLocal(R.string.s_ErrorConnToRosTimeout));
        } else if (result == ConnResult.failed) {
            showToast(getStringLocal(R.string.s_ErrorConnToRosFailed));
        } else if (result == ConnResult.exception) {
            showToast(getStringLocal(R.string.s_ErrorConnToRosException));
        } else {
            Log.e(TAG, "addPositionFailed: 未知错误：" + result);
        }
    }

    /**
     * 修改标记
     */
    private void modifyPosition(final String positionId, final String positionName) {
        Log.i(TAG, "modifyPosition: ");
        try {
            final ServiceManager serviceManager = new ServiceManager(context);

            serviceManager.setPositionId(positionId); //
            serviceManager.setPositionName(positionName);
            serviceManager.setFunction(getStringLocal(R.string.s_functionName_ModifyPosition));
            serviceManager.registerCallbackModifyPosition(new ServiceManager.StatusCallbackModifyPosition() {
                @Override
                public void timeoutCallback() {
                    modifyPositionFailed(ConnResult.exception);
                }

                @Override
                public void onSuccessCallback(ModifyPositionResponse arg0) { //自动加1，下次好继续用。
                    List<FlagBean> flagBeanList = occupancyGridLayer.getFlagBeanList();
                    double x = 0, y = 0;
                    for (int i = flagBeanList.size() - 1; i >= 0; i--) {
                        if (positionId.equals(flagBeanList.get(i).getId())) {
                            x = flagBeanList.get(i).getShape().getTransform().getTranslation().getX();
                            y = flagBeanList.get(i).getShape().getTransform().getTranslation().getY();
                            Log.i(TAG, "onSuccessCallback: 已修改标记，id=" + positionId + ", name=" + positionName + ", flag.shape.transform.x,y=(" + x + "," + y + "), flag.x,y=(" + flagBeanList.get(i).getX() + ", " + flagBeanList.get(i).getY() + ")");//flag.shape.transform.x,y=(1.121999979019165,0.00800000037997961), flag.x,y=(0.0, 0.0)
                            flagBeanList.remove(i);
                        }
                    }
                    occupancyGridLayer.setFlagBeanList(flagBeanList);
                    occupancyGridLayer.addFlagBean((float) x, (float) y, positionId, positionName);

                    showToast(getStringLocal(R.string.s_modifyPositionSuccess) + ":" + positionName + getStringLocal(R.string.s_customPositionDisplayNext));
                    status.setNextAction("");
                    serviceManager.setPositionId("");
                    serviceManager.setPositionName("");
                    serviceManager.setFunction("");
                    Log.i(TAG, "onSuccessCallback: 置空action");
                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();

                    modifyPositionFailed(ConnResult.exception);
                }
            });

            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.modify_position.toString()), getNodeList(NodeName.modify_position, status.getNextAction(), serviceManager)); // "android/modify_position"
        } catch (Exception e) {
            e.printStackTrace();
            modifyPositionFailed(ConnResult.exception);
        }
    }

    /**
     * 修改标记失败
     */
    private void modifyPositionFailed(ConnResult result) {
        Log.e(TAG, "modifyPositionFailed: " + result + "\t" + status.getNextAction());

        if (result == timeout) {
            showToast(getStringLocal(R.string.s_ErrorFailedToModifyPositions) + ": " + getStringLocal(R.string.s_ErrorConnToRosTimeout));
        } else if (result == ConnResult.failed) {
            showToast(getStringLocal(R.string.s_ErrorFailedToModifyPositions) + ": " + getStringLocal(R.string.s_ErrorConnToRosFailed));
        } else if (result == ConnResult.exception) {
            showToast(getStringLocal(R.string.s_ErrorFailedToModifyPositions) + ": " + getStringLocal(R.string.s_ErrorConnToRosException));
        } else {
            Log.e(TAG, "modifyPositionFailed: 未知错误：" + result);
        }
    }

    /**
     * 删除标记
     */
    private void deletePosition(final String positionId) {
        Log.i(TAG, "deletePosition: positionId=" + positionId);
        try {
            final ServiceManager serviceManager = new ServiceManager(context);

            serviceManager.setPositionId(positionId); //
            serviceManager.setFunction(getStringLocal(R.string.s_functionName_DeletePosition));
            serviceManager.registerCallbackDeletePosition(new ServiceManager.StatusCallbackDeletePosition() {
                @Override
                public void timeoutCallback() {
                    deletePositionFailed(timeout);
                }

                @Override
                public void onSuccessCallback(DeletePositionResponse arg0) { //自动加1，下次好继续用。

                    List<FlagBean> flagBeanList = occupancyGridLayer.getFlagBeanList();
                    String positionName = "";
                    for (int i = flagBeanList.size() - 1; i >= 0; i--) {
                        if (positionId.equals(flagBeanList.get(i).getId())) {
                            positionName = flagBeanList.get(i).getName();
                            flagBeanList.remove(i);
                        }
                    }
                    occupancyGridLayer.setFlagBeanList(flagBeanList);

                    showToast(getStringLocal(R.string.s_deletePositionSuccess) + ":" + positionName);
                    status.setNextAction("");
                    serviceManager.setPositionId(""); //
                    serviceManager.setFunction("");
                    Log.i(TAG, "onSuccessCallback: " + ",name=" + positionName + ",id=" + positionId + "已置空action");
                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();
                    deletePositionFailed(ConnResult.failed);
                }
            });

            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.delete_position.toString()), getNodeList(NodeName.delete_position, status.getNextAction(), serviceManager)); // "android/delete_position"
        } catch (Exception e) {
            e.printStackTrace();
            deletePositionFailed(ConnResult.exception);
        }
    }

    /**
     * 删除标记失败
     */
    private void deletePositionFailed(ConnResult result) {

        Log.e(TAG, "deletePositionFailed: " + result + "\t" + status.getNextAction());
        if (result == timeout) {
            showToast(getStringLocal(R.string.s_ErrorFailedToDeletePositions) + ": " + getStringLocal(R.string.s_ErrorConnToRosTimeout));
        } else if (result == ConnResult.failed) {
            showToast(getStringLocal(R.string.s_ErrorFailedToDeletePositions) + ": " + getStringLocal(R.string.s_ErrorConnToRosFailed));
        } else if (result == ConnResult.exception) {
            showToast(getStringLocal(R.string.s_ErrorFailedToDeletePositions) + ": " + getStringLocal(R.string.s_ErrorConnToRosException));
        } else {
            Log.e(TAG, "deletePositionFailed: 未知错误：" + result);
        }
    }

    /**
     * 保存坐标位置，告诉主机，可以保存了
     */
    private void savePositions() {
        Log.i(TAG, "savePositions()");
        try {
            final ServiceManager serviceManager = new ServiceManager(context);

            serviceManager.setFunction(getStringLocal(R.string.s_functionName_SavePositions));
            serviceManager.registerCallbackSavePositions(new ServiceManager.StatusCallbackSavePositions() {
                @Override
                public void timeoutCallback() {

                    savePositionsFailed(timeout);
                }

                @Override
                public void onSuccessCallback(SavePositionResponse arg0) {
                    if (!arg0.getResult()) {
                        Log.e(TAG, "保存位置:success-false");
                    } else if (occupancyGridLayer.getFlagBeanList().size() > 0) {
                        showToast(R.string.s_PositionSavingSuccess);
                    }
//                    safeDismissWaitingDialog();
                    if (status.isNextAction(status.Action_ReNewMap)) { // 重新构建地图
                        Log.i(TAG, "onSuccessCallback: 保存位置成功，将删除地图");
                        getMapList();
                    } else if (status.isNextAction(status.Action_savePositions)) {
                        Log.i(TAG, "onSuccessCallback: 保存位置成功，将清除action: " + status.getNextAction());
                        status.setNextAction("");
                    } else if (status.isNextAction(Action_SavedMap_StartNav)) {
                        lastPose = new PoseCustom(arg0.getLocalX(), arg0.getLocalY(), arg0.getLocalZ(), arg0.getLocalW());
                        Log.i(TAG, "保存位置成功，设置最后位置的值成功，lastPose is null?" + (lastPose == null));
//                        status.setNextAction(status.Action_SavedMap_StartNav);
                        stopRapp(Action_SavedMap_StartNav);
                    } else {
                        Log.e(TAG, "onSuccessCallback: 未知目的保存坐标成功" + status.getNextAction());
                    }
                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();
                    savePositionsFailed(ConnResult.failed);
                }
            });

            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.save_positions.toString()), getNodeList(NodeName.save_positions, status.getNextAction(), serviceManager)); // "android/save_positions"
        } catch (Exception e) {
            e.printStackTrace();

            savePositionsFailed(ConnResult.exception);
        }
    }

    /**
     * 保存坐标失败
     */
    private void savePositionsFailed(ConnResult result) {
        Log.e(TAG, "savePositionsFailed: " + result + "\t" + status.getNextAction());

        if (status.isNextAction(status.Action_ReNewMap) || status.isNextAction(status.Action_savePositions)) {
            status.setNextAction("");

        } else if (status.isNextAction(Action_SavedMap_StartNav)) {
            Log.e(TAG, "timeoutCallback: 保存坐标超时，将获取地图、删除地图");
            getMapList();
        }

        if (result == timeout) {
            showToast(getStringLocal(R.string.s_ErrorConnToRosTimeout));
        } else if (result == ConnResult.failed) {
            showToast(getStringLocal(R.string.s_ErrorConnToRosFailed));
        } else if (result == ConnResult.exception) {
            showToast(getStringLocal(R.string.s_ErrorConnToRosException));
        } else {
            Log.e(TAG, "savePositionsFailed: 未知错误：" + result);
        }
    }


    /**
     * 启动rapp，用服务启动
     */
    private void startRapp(final String rappName) {
        Log.i(TAG, "startRapp, rappName=" + rappName);
//        if (roconDescription == null) {
//            // todo  这些变量可能不需要检测了，可能用不上。
//            Log.e(TAG, "startRapp() 想要连接rapp，请先初始化数据 roconDescription is null?" + (roconDescription == null)); // getMasterUri() != null时打印了这句，roconDescription为空时，startRapp服务可执行。
//            return;
//        }
        // 数据初始化 start
        status.setIsReceivedCamera(false);
        status.setIsReceivedMap(false);

        occupancyGridLayer.setSubscribe(true);
        occupancyGridLayer.clearTiles();
        // 数据初始化 end

        if (sbTemp.length() > 0) sbTemp.delete(0, sbTemp.length());
        sbTemp.append(getStringLocal(R.string.s_RequestingMasterRapp)).append(": ");
        if (rappName.equals(RappNameMakeAMap))
            sbTemp.append(getStringLocal(R.string.s_CNMake_A_Map));
        else if (rappName.equals(RappNameMapNav))
            sbTemp.append(getStringLocal(R.string.s_CNMap_Navigation));

        safeShowWaitingDialog("", sbTemp.toString(), false);

        if (rappName.equals(RappNameMakeAMap))
            occupancyGridLayer.setmSendedRapp(OccupancyGridLayerCustom.rapp.make_a_map);
        else if (rappName.equals(RappNameMapNav))
            occupancyGridLayer.setmSendedRapp(OccupancyGridLayerCustom.rapp.map_nav);
        else Log.e(TAG, "startRapp: 错误rappName=" + rappName);

        try {
            final ServiceManager serviceManager = new ServiceManager(context);

            serviceManager.setFunction(getStringLocal(R.string.s_startRapp));
            serviceManager.setRappName(rappName);
            serviceManager.registerCallbackStart(new ServiceManager.StatusCallbackStart() {
                @Override
                public void timeoutCallback() {
                    startRappFailed(getStringLocal(R.string.s_TimeoutStartRapp), timeout);
                }

                @Override
                public void onSuccessCallback(StartRappResponse arg0) {
                    if (!arg0.getStarted()) { // 启动失败
                        Log.i(TAG, "onSuccessCallback: startRapp-success-false");
//                        safeDismissWaitingDialog();
//                        startRappFailed(arg0.getMessage());
//                        return;
                    }

                    boolean isDismissDialog = startRappSuccess(rappName, serviceManager);
                    if (isDismissDialog) {
                        safeDismissWaitingDialog();
                    }

                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();

                    startRappFailed(e.toString(), ConnResult.failed);
                }
            });
            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.start_rapp.toString()), getNodeList(NodeName.start_rapp, status.getNextAction(), serviceManager)); // getStringLocal(R.string.map_nodeNameStartRapp)

        } catch (Exception e) {
            e.printStackTrace();

            startRappFailed(e.toString(), ConnResult.exception);

        }

    }

    /**
     * 启动Rapp成功
     */
    private boolean startRappSuccess(final String rappName, final ServiceManager serviceManager) {
        boolean isDismissDialog = true;

        status.setIsRunningRapp(true);
        status.setRunningRappName(rappName);
        status.setServiceManager(serviceManager);
        status.setConnectedNode(serviceManager.getConnectedNode());

        Log.i(TAG, "startRappSuccess: 已设置status.setRunningRappName");
        occupancyGridLayer.setmSendedRapp(OccupancyGridLayerCustom.rapp.none);
        // 显示摄像头
        mHandler.sendEmptyMessage(mHandler_displayCameraView);
//        resetPositionIdFixed();
//        resetPositionIdCustom();
        resetFlagId();

        // 启动成功
        if (rappName.equals(RappNameMakeAMap) || rappName.equals(RappNameMapNav)) {
            Message msgChecked = mHandler.obtainMessage();
            Message msgUncheck = mHandler.obtainMessage();
            Message msgLongClick = mHandler.obtainMessage();

            boolean[] longClickBg = new boolean[3];

            if (rappName.equals(RappNameMakeAMap)) {
                occupancyGridLayer.setStatusRapp(OccupancyGridLayerCustom.rapp.make_a_map);
                // 设置背景，初始化运行状态。
//                            id_mapEstablish.setBackgroundResource(R.mipmap.toolbar_mapmode_establish_selected);
                msgChecked.what = mHandler_MapMakeBackground;
                msgChecked.obj = R.mipmap.toolbar_mapmode_establish_selected;

                msgUncheck.what = mHandler_MapNavBackground;
                msgUncheck.obj = R.mipmap.toolbar_mapmode_navigation_normal;

//                setGoal(); // 默认长按事件不处理
                longClickBg[0] = false;
                longClickBg[1] = false;
                longClickBg[2] = false;
//                occupancyGridLayer.setIsExistsMap(false); // 启动构建地图后，过几秒才会收到原始地图，必须收原始地图，否则地图出错。
                // 启动成功，置空动作
//                if (status.isNextAction(status.Action_LongClick_StartMapMake) ) {
//                    status.setNextAction("");
//                }
                // 清空位置记录
//                if(mListPositionNames.size() > 0) mListPositionNames.clear();
                occupancyGridLayer.clearFlagBeanList();
                occupancyGridLayer.setReadyText(true);
            } else if (rappName.equals(RappNameMapNav)) {
                occupancyGridLayer.setStatusRapp(OccupancyGridLayerCustom.rapp.map_nav);
                // 设置背景，初始化运行状态。
//                            id_mapNavigation.setBackgroundResource(R.mipmap.toolbar_mapmode_navigation_selected);
                msgChecked.what = mHandler_MapNavBackground;
                msgChecked.obj = R.mipmap.toolbar_mapmode_navigation_selected;

                msgUncheck.what = mHandler_MapMakeBackground;
                msgUncheck.obj = R.mipmap.toolbar_mapmode_establish_normal;

//                setPose(); // 默认长按事件不处理
                longClickBg[0] = false;
                longClickBg[1] = false;
                longClickBg[2] = false;

                // 启动地图导航之前，发布地图
//                            publishMap(mListMaps.get(0)); // IndexOutOfBoundsException
//                            getMapList(getStringLocal(R.string.s_actionLoadMap));
//                            Log.i(TAG, "publish map after started map nav");
//                if (status.isNextAction(Action_LongClick_StartMapNav)) {
////                    status.setNextAction("");
//
//
//                    isDismissDialog = false;
////                    getMapList(Action_LongClick_StartMapNav);
//                    publishMap(mapId);
//                }
            } else {
                Log.e(TAG, "startRappSuccess: 未知rapp，rappName=" + rappName);
            }
            mHandler.sendMessage(msgChecked);
            mHandler.sendMessage(msgUncheck);
            msgLongClick.what = mHandler_LongClickBg;
            msgLongClick.obj = longClickBg;
            mHandler.sendMessage(msgLongClick);
            mapPosePublisherLayer.setNoneMode();
        }

//        status.setIsRunningRapp(true);
//        status.setRunningRappName(rappName);
//        status.setServiceManager(serviceManager);
//        status.setConnectedNode(serviceManager.getConnectedNode());


        Log.i(TAG, "启动rapp成功:" + rappName + ", status.getNextAction()=" + status.getNextAction() + ", status.getServiceManager()=" + status.getServiceManager() + ", status.getConnectedNode()=" + status.getConnectedNode());
        if (rappName.equals(RappNameMapNav) && (status.isNextAction(Action_SavedMap_StartNav) || status.isNextAction(Action_LongClick_StartMapNav))) { //如果是保存地图成功，且启动地图导航成功，则退出Remocon
//            if (lastPose != null) {
//                mapPosePublisherLayer.publishLastPose(lastPose.getX(), lastPose.getY(), lastPose.getZ(), lastPose.getW());
//                Log.i(TAG, "startRappSuccess: 已发布初始坐标=(" + lastPose.getX() + ", " + lastPose.getY() + ", " + lastPose.getZ() + ", " + lastPose.getW() + ")");
//            } else {
//                Log.e(TAG, "想发布最后的坐标，但发布失败，lastPose is null");
//            }
//            Log.i(TAG, "保存地图成功-且启动地图导航成功，不退出Remocon");
//            status.setNextAction("");
            isDismissDialog = false; //
            occupancyGridLayer.setSubscribe(true);
//            getMapList(Action_SavedMap_StartNav);
            Log.i(TAG, "startRappSuccess: 将发布地图");
            publishMap(mapId);
        } else if (status.isNextAction(status.Action_ReNewMap) || status.isNextAction(status.Action_LongClick_StartMapMake)) {
//            if (status.isNextAction(status.Action_ReNewMap))  refreshMapView(); // 如果没收到地图也就不用刷新了

            // 如果已收到地图，就不转圈圈了，如果未收到，则等待。
            Log.i(TAG, "startRappSuccess: isExistsMap=" + occupancyGridLayer.isExistsMap());
            if (occupancyGridLayer.isExistsMap()) status.setNextAction("");
            else {
                Log.i(TAG, "startRappSuccess: 将等待地图，action=" + status.getNextAction());
                isDismissDialog = false;
                isWaitforMap = true;
                mWaitMapFlag++;
                mHandler.sendMessageDelayed(mHandler.obtainMessage(mHandler_TimeoutForWaitMap, mWaitMapFlag), mTimeoutWaitMap * 1000);
            }

            Log.i(TAG, "onSuccessCallback: 启动构建地图成功");
        }

//        // 如果启动了rapp，就显示跟随按钮可按
//        id_mapFollow.setBackgroundResource(R.drawable.btn_followstop);

        return isDismissDialog;
    }

    /**
     * 启动rapp失败的处理、打印信息
     */
    private void startRappFailed(String errMsg, ConnResult result) {
        Log.i(TAG, "startRappFailed: " + result + "\t" + status.getNextAction());

        safeDismissWaitingDialog();
        occupancyGridLayer.setmSendedRapp(OccupancyGridLayerCustom.rapp.none);

        if (!status.getRunningRappName().equals("") && errMsg.contains(status.getRunningRappName())) {
            Log.i(TAG, "startRappFailed: 已经在运行rapp：" + status.getRunningRappName());
            return;
        }

//        if(sbTemp.length() > 0) sbTemp.delete(0, sbTemp.length());
//        sbTemp.append(getStringLocal(R.string.s_ErrorStartRapp));

        if (status.isNextAction(status.Action_LongClick_StartMapMake)) {
//            status.setNextAction("");
//            sbTemp.append("\n" + getStringLocal(R.string.s_ErrorStartMapMake));
        } else if (status.isNextAction(status.Action_ReNewMap)) {
//            status.setNextAction("");
//            sbTemp.append("\n" + getStringLocal(R.string.s_ErrorToNewMap));
        } else if (status.isNextAction(Action_LongClick_StartMapNav) || status.isNextAction(Action_SavedMap_StartNav)) {
//            status.setNextAction("");
            mapId = null;
//            sbTemp.append("\n" + getStringLocal(R.string.s_ErrorStartMapNav));
        } else {
            Log.e(TAG, "startRappFailed: 未知目的，未处理");
        }
        status.setNextAction("");

//        sbTemp.append("\n" + errMsg);

//        safeShowNotiDialogCustom(getStringLocal(R.string.s_ErrorStartRapp)+"! "+sbTemp.toString());
//        safeShowNotiDialogCustom(sbTemp.toString());
//        showToast(getStringLocal(R.string.s_ErrorFailedToRos));
        if (result.equals(ConnResult.exception))
            showToast(getStringLocal(R.string.s_ErrorConnToRosException));
        else if (result.equals(ConnResult.failed))
            showToast(getStringLocal(R.string.s_ErrorConnToRosFailed));
        else if (result.equals(timeout))
            showToast(getStringLocal(R.string.s_ErrorConnToRosTimeout));
        else Log.e(TAG, "startRappFailed: 判断出错，未处理：" + result);
//        Log.e(TAG, sbTemp.toString());
    }


    private void stopRapp(final String stopRappNextAction) {
        Log.i(TAG, "stopRapp: ");
        // 不管停的是哪个，都应该让跟随停止
        if (status.isFollow()) {
            followerService(SetFollowStateRequest.STOPPED);
        }
        stopRappByService(stopRappNextAction);
    }

    /**
     * 退出rapp
     *
     * @param stopRappNextAction 退出rapp后的下一个动作
     *                           主机端警告：Rapp Manager: tried to stopRapp a rapp, but no rapp found running.
     */
    private void stopRappByService(final String stopRappNextAction) {
        Log.i(TAG, "stopRappByService: ");
        if (sbTemp.length() > 0) sbTemp.delete(0, sbTemp.length());
        sbTemp.append(getStringLocal(R.string.s_stoppingRapp)).append(": ");

        if (status.isRunning(RappNameMakeAMap))
            sbTemp.append(getStringLocal(R.string.s_CNMake_A_Map));
        else if (status.isRunning(RappNameMapNav))
            sbTemp.append(getStringLocal(R.string.s_CNMap_Navigation));

        safeShowWaitingDialog("", sbTemp.toString(), false);

        try {
            final ServiceManager serviceManager = new ServiceManager(context);

            serviceManager.setFunction(getStringLocal(R.string.s_stopRapp));
            serviceManager.registerCallbackStop(new ServiceManager.StatusCallbackStop() {
                @Override
                public void timeoutCallback() {
                    stopRappFailed(getStringLocal(R.string.s_TimeoutStopRapp), stopRappNextAction, timeout);
                }

                @Override
                public void onSuccessCallback(StopRappResponse arg0) {
//                    safeDismissWaitingDialog();
                    if (!arg0.getStopped()) {
//                        safeDismissWaitingDialog();
                        Log.i(TAG, "onSuccessCallback: 停止地图-success-false, arg0.getMessage()=" + arg0.getMessage());
//                        stopRappFailed(arg0.getMessage(), stopRappNextAction);
//                        return;
                    }
                    Log.i(TAG, "停止rapp成功");
                    boolean isGone = stopRappSuccess(stopRappNextAction);
                    if (isGone) {
                        safeDismissWaitingDialog();
                    }
                }

                @Override
                public void onFailureCallback(Exception e) {
                    e.printStackTrace();
                    stopRappFailed(e.toString(), stopRappNextAction, ConnResult.failed);
                }
            });


            nodeMainExecutor.execute(serviceManager, nodeConfiguration.setNodeName(NodeName.stop_rapp.toString()), getNodeList(NodeName.stop_rapp, stopRappNextAction, serviceManager)); // "android/stop_rapp"

        } catch (Exception e) {
            e.printStackTrace();
            stopRappFailed(e.toString(), stopRappNextAction, ConnResult.exception);
        }
    }

    /**
     * 停止失败处理
     */
    private void stopRappFailed(String errMsg, final String stopRappNextAction, ConnResult result) {
        Log.e(TAG, "stopRappFailed: " + result + "\t" + status.getNextAction());

//        if(sbTemp.length() > 0) sbTemp.delete(0, sbTemp.length());

        if (errMsg.contains(status.ErrorMsg_StopRappWithNoRunning)) { // 停止时发现没有运行的rapp，当作已停止成功处理。
            stopRappSuccess(stopRappNextAction);
            Log.e(TAG, "停止时发现没有运行的rapp，当作已停止成功处理");
            return;
        }
//        else if (stopRappNextAction.equals(Action_SavedMap_StartNav)) {
        else if (status.isNextAction(Action_SavedMap_StartNav)) {
//            status.setNextAction("");
//            sbTemp.append(getStringLocal(R.string.s_Error_StopMapMakeFailedAfterSavedMap)); // 保存地图后，停止构建地图失败
        } else if (status.isNextAction(status.Action_Exit_OnPause)) {
//            status.setNextAction("");
//            sbTemp.append(getStringLocal(R.string.s_Error_OnPauseStopRappFailed));// 程序暂停时退出Rapp失败
        } else if (status.isNextAction(Action_InitRemocon_RunningMapNav_PublishMap)) {
//            status.setNextAction("");
//            sbTemp.append(getStringLocal(R.string.s_Error_InitRemoconImplementsOldMapNav));
        } else if (status.isNextAction(status.Action_ReNewMap)) {
//            status.setNextAction("");
//            sbTemp.append(getStringLocal(R.string.s_ErrorToNewMap));
        } else if (status.isNextAction(status.Action_stopRappOnly)) {
//            status.setNextAction("");
//            sbTemp.append(getStringLocal(R.string.s_ErrorStopRapp));
        } else {
            Log.e(TAG, "stopRappFailed: 未知目的，未处理");
        }
        status.setNextAction("");
        safeDismissWaitingDialog();
//        sbTemp.append("\n" + errMsg);

//        safeShowNotiDialogCustom(getStringLocal(R.string.s_ErrorStopRapp)+"! "+sbTemp.toString());
//        safeShowNotiDialogCustom(sbTemp.toString());
//        safeShowNotiDialogCustom(getStringLocal(R.string.s_ErrorFailedToRos));
//        showToast(getStringLocal(R.string.s_ErrorFailedToRos));
        if (result.equals(ConnResult.exception))
            showToast(getStringLocal(R.string.s_ErrorConnToRosException));
        else if (result.equals(ConnResult.failed))
            showToast(getStringLocal(R.string.s_ErrorConnToRosFailed));
        else if (result.equals(timeout))
            showToast(getStringLocal(R.string.s_ErrorConnToRosTimeout));
        else Log.e(TAG, "stopRappFailed: 判断出错，未处理：" + result);
//        Log.e(TAG, "停止Rapp失败" + sbTemp.toString());
    }

    /**
     * 停止Rapp成功处理
     *
     * @return 是否隐藏进度条，有下一个操作且需要继续显示进度条，则不隐藏。
     */
    private boolean stopRappSuccess(final String stopRappNextAction) {
        boolean isGone = true;

        // 清理地图
        occupancyGridLayer.clearTiles();
//        occupancyGridLayer.setIsExistsMap(false); // 清理完地图以后，也要设置当前无地图，否则nav->makeMap将没有初始地图。
//        resetPositionIdFixed();
//        resetPositionIdCustom();
        resetFlagId();

//        mPublisherSubscriber.send(RemoconAllActivity.this); // 无论是否在走，都取消，不影响性能。
        mapPosePublisherLayer.send(context); // 无论是否在走，都取消，不影响性能。停止Rapp后取消来这里。
        // 清理摄像头
        mHandler.sendEmptyMessage(mHandler_clearCameraView);
        if (getActivity() != null && stopRappNextAction.equals(getStringLocal(R.string.s_actionForceShutDown))) { // 关闭全部remocon // getActivity() == null空指针
            Log.i(TAG, "stopRappSuccess()-success-forceShutdown");


//            disconnectAP();
            leaveRemoconServiceForceShutdown(null);
//            nodeMainExecutorService.forceShutdown();
//            finish();
        } else if (stopRappNextAction.equals(getStringLocal(R.string.s_actionBackToRemocon))) { // 退回remocon
            Log.e(TAG, "stopRappSuccess: 未处理：想要退回remocon");
//            finish();
        }
//        else if (stopRappNextAction.equals(getStringLocal(R.string.s_actionInitRemocon))) { //删掉所有已配对的Rapp
        else if (status.isNextAction(Action_InitRemocon)) { //删掉所有已配对的Rapp
            Log.i(TAG, stopRappNextAction + "\t" + "已删除一个Rapp，再次检查");
            getRappList(); // 继续检查是否还有正在运行的Rapp // getStringLocal(R.string.s_actionInitRemocon)
            isGone = false;
        } else if (status.isNextAction(Action_SavedMap_StartNav)) { // 如果是保存地图成功，则停止地图构建，打开地图导航，退出Remocon
            // 发布地图
//            getMapList(status.Action_SavedMap_StartNav);

            if (status.isRunning(RappNameMakeAMap)) {
                Log.i(TAG, "保存地图-已停止地图构建，将启动地图导航");
                getMapList();
                isGone = false;
            } else if (status.isRunning(RappNameMapNav)) {
                Log.i(TAG, "stopRappSuccess: 停止地图导航成功。");

            } else {
                Log.e(TAG, "stopRappSuccess: 出错了，当前在运行的rapp是：" + status.getRunningRappName());
            }

        } else if (status.isNextAction(status.Action_ReNewMap)) { // 重新启动构建地图
//            connectRapp(RappNameMakeAMap);
            // 不获取坐标，直接清理
//            getAllPositions();
            resetAllPositions();
            Log.i(TAG, "保存地图-已停止地图构建，将启动构建地图");
            isGone = false;
        } else if (status.isNextAction(status.Action_Exit_OnPause)) {
//            disconnectAP();
            leaveRemoconServiceForceShutdown(null);
//            nodeMainExecutorService.forceShutdown();
//            finish();
            Log.i(TAG, "退出程序，" + status.Action_Exit_OnPause);
        }
//        else if (stopRappNextAction.equals(getStringLocal(R.string.s_actionStopRappOnly))) { // 仅仅停止rapp而已
        else if (status.isNextAction(status.Action_stopRappOnly)) { // 仅仅停止rapp而已
            status.setNextAction("");
        } else if (status.isNextAction(Action_InitRemocon_RunningMapNav_PublishMap)) {
            status.setNextAction("");
            Log.i(TAG, "初始启动地图导航，没有发布地图，停止地图导航成功");
        } else {
            Log.e(TAG, "未知目的停止Rapp，待处理。");
        }

        if (status.isRunning(RappNameMakeAMap) || status.isRunning(RappNameMapNav)) {
            Message msgUncheck = mHandler.obtainMessage();

            Message msgLongClick = mHandler.obtainMessage();
            boolean[] longClickBg = new boolean[]{false, false, false};
            msgLongClick.what = mHandler_LongClickBg;
            msgLongClick.obj = longClickBg;
            mHandler.sendMessage(msgLongClick);
            mapPosePublisherLayer.setNoneMode();

            if (status.isRunning(RappNameMakeAMap)) {
                mHandler.sendEmptyMessage(mHandler_MapMakeViewGone);

                msgUncheck.what = mHandler_MapMakeBackground;
                msgUncheck.obj = R.mipmap.toolbar_mapmode_establish_normal;

//                // 如果停止的是构建地图，则清理地图后也要清理标记
//                occupancyGridLayer.clearFlagBeanList();
            } else if (status.isRunning(RappNameMapNav)) {
                mHandler.sendEmptyMessage(mHandler_MapNavViewGone);

                msgUncheck.what = mHandler_MapNavBackground;
                msgUncheck.obj = R.mipmap.toolbar_mapmode_navigation_normal;
            } else {
//                safeShowNotiDialogCustom(getStringLocal(R.string.s_ErrorToRos));
                showToast(getStringLocal(R.string.s_ErrorToRos));
                Log.e(TAG, "停止出错, 未知Rapp已停止，未处理.");
            }
            mHandler.sendMessage(msgUncheck);

            // 如果停止的是构建地图，则清理地图后也要清理标记
            occupancyGridLayer.clearFlagBeanList();
        }

        status.setIsRunningRapp(false);
        status.setRunningRappName("");
        occupancyGridLayer.setStatusRapp(OccupancyGridLayerCustom.rapp.none);
        status.setServiceManager(null);
        status.setConnectedNode(null);
        if (!status.isNextAction(status.Action_ReNewMap)) { // 如果是重新构建地图，则需要第一张地图。
            occupancyGridLayer.setSubscribe(false);
        }

        Log.i(TAG, "停止rapp后," + stopRappNextAction + ",已更新status运行状态");
        return isGone;
    }

    private List<NodeListener> getNodeList(final NodeName nodeName, final String nextAction, final NodeMain nodeMain) {
        List<NodeListener> listNodeListener = new ArrayList<NodeListener>();
        listNodeListener.add(new NodeListener() {
            @Override
            public void onStart(ConnectedNode connectedNode) {
                Log.i(TAG, "onStart: " + nodeName + " NodeListener,connectedNode=" + connectedNode.toString());
            }

            @Override
            public void onShutdown(Node node) { // 常打印 1.
                Log.e(TAG, "onShutdown: " + nodeName + "  NodeListener, node.log=" + node.getLog().toString() + ",debug=" + node.getLog().isDebugEnabled() + ",error=" + node.getLog().isErrorEnabled() + ",fatal=" + node.getLog().isFatalEnabled() + ",info=" + node.getLog().isInfoEnabled() + ",trace=" + node.getLog().isTraceEnabled() + ",warn=" + node.getLog().isWarnEnabled()); // org.ros.internal.node.DefaultNode@d36c5ba
            }

            @Override
            public void onShutdownComplete(Node node) { // 常打印 2.
                Log.e(TAG, "onShutdownComplete: " + nodeName + "  NodeListener, node=" + node.toString()); // org.ros.internal.node.DefaultNode@d36c5ba
//                node.removeListeners(); // 可能会让第二次调用报错
//                node.shutdown(); // 可能会让第二次调用报错
            }

            @Override
            public void onError(Node node, Throwable throwable) {
                Log.e(TAG, "onError: " + nodeName + "  NodeListener, node=" + node.toString() + "\t" + nodeName);
                throwable.printStackTrace();
                nodeMainExecutor.shutdownNodeMain(nodeMain);
                safeDismissWaitingDialog();
                if (nodeName.equals(NodeName.stop_rapp))
                    stopRappFailed(throwable.toString(), nextAction, ConnResult.exception);
                else if (nodeName.equals(NodeName.start_rapp))
                    startRappFailed(throwable.toString(), ConnResult.exception);
                else if (nodeName.equals(NodeName.get_rapp_list))
                    getRappListFailure(ConnResult.exception);
                else if (nodeName.equals(NodeName.list_maps))
                    getMapListFailed(throwable.toString(), ConnResult.exception);
                else if (nodeName.equals(NodeName.publish_map))
                    publishMapFailed(throwable.toString(), ConnResult.exception);
                else if (nodeName.equals(NodeName.follower))
                    followerServiceFailed(mFollowerNewState, ConnResult.exception);
                else if (nodeName.equals(NodeName.delete_map))
                    deleteMapFailed(throwable.toString(), ConnResult.exception);
                else if (nodeName.equals(NodeName.get_all_positions))
                    getAllPositionFailed(throwable.toString(), ConnResult.exception);
                else if (nodeName.equals(NodeName.reset_all_positions))
                    resetAllPositionFailed(throwable.toString(), ConnResult.exception);
                else if (nodeName.equals(NodeName.save_map)) saveMapFailed(ConnResult.exception);
                else if (nodeName.equals(NodeName.add_position))
                    addPositionFailed(ConnResult.exception);
                else if (nodeName.equals(NodeName.modify_position))
                    modifyPositionFailed(ConnResult.exception);
                else if (nodeName.equals(NodeName.delete_position))
                    deleteMapFailed(throwable.toString(), ConnResult.exception);
                else if (nodeName.equals(NodeName.save_positions))
                    savePositionsFailed(ConnResult.exception);
                else if (nodeName.equals(NodeName.camera_view)) ;
                else if (nodeName.equals(NodeName.virtual_joystick)) ;
                else if (nodeName.equals(NodeName.map_view)) ;
                else if (nodeName.equals(NodeName.PublisherSubscriber)) ;

                else
                    Log.e(TAG, "onError: 未知节点名，未处理 nodeName=" + nodeName); // camera_view virtual_joystick  map_view PublisherSubscriber

                /** camera_view, virtual_joystick, get_rapp_list, list_maps, publish_map, follower, delete_map, get_all_positions, reset_all_positions, save_map, add_position, modify_position, delete_position, save_positions, start_rapp, stop_rapp, map_view, PublisherSubscriber
                 *
                 */
            }
        });
        return listNodeListener;
    }

    /**
     * 判断是不是中文
     */
    private final boolean isChineseCharacter(String chineseStr) {
        Log.i(TAG, "isChineseCharacter()");
        char[] charArray = chineseStr.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {
                return true;
            }
        }
        return false;
    }


    int count = 0;
    boolean isOK = false;

    /**
     * This returns the activity to the concert master chooser
     * activity. It will get triggered via either a backpress
     * or the button provided in the Remocon activity.
     */
    public void leaveRemoconServiceForceShutdown(View view) { // xml 里的onClick方法，只可View做参数？
        Log.i(TAG, "leaveRemoconServiceForceShutdown()");

        AsyncTask asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPostExecute(Void aBoolean) {
//                                super.onPostExecute(aBoolean);
                Log.i(TAG, "onPostExecute: 将关闭ros-wifi");
//                new Thread()
//                {
//                    public void run()
//                    {
//                        while (!isOK) {
//                            try {
//                                if (count >= 10) {
//
//                                    disconnectAP(); // 导致退出后还在调用ros的线程，但调用不了，于是卡死了退不出。
//                                    finish();
//                                    break;
//                                }
//
//                                Thread.sleep(80);
//                                count++;
//                            } catch (InterruptedException e) {
//                                disconnectAP(); // 导致退出后还在调用ros的线程，但调用不了，于是卡死了退不出。
//                                finish();
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }.start();


                disconnectAP(); // 导致退出后还在调用ros的线程，但调用不了，于是卡死了退不出。
//                finish(); // 系统稳定前不要删
                Log.e(TAG, "onPostExecute: 原：finish()");
                safeDismissWaitingDialog();
            }

            @Override
            protected Void doInBackground(Void... params) {
                Log.i(TAG, "doInBackground: 将清理nodemain、关闭nodeMainExecutorService");
                safeShowWaitingDialog("", getStringLocal(R.string.s_clearingExecute), false);
                clearExecuted(); // 这可能导致org.ros.internal.node.xmlrpc.XmlRpcTimeoutException: org.apache.xmlrpc.client.TimingOutCallback$TimeoutException: No response after waiting for 10000 milliseconds.

                nodeMainExecutorService.forceShutdown(); // 这句是必须的，否则onDestroy里的shutdown关闭不了nodeMainExecutor // 只要有网络，没有启动rapp时，374ms左右可以退出。如果开启了rapp则1157ms左右退出。其实只要留一点点时间退出，之后再关闭ap，就不会报错。待多次检测。
                return null;
            }

            @Override
            protected void onCancelled(Void aVoid) {
                super.onCancelled(aVoid);
                Log.i(TAG, "onCancelled: 取消清理数据");
            }
        }.execute();

        timeoutCheck(mTimeout, asyncTask);
    }


    /**
     * 超时检测
     */
    private void timeoutCheck(final int timeout, final AsyncTask taskParam) {
        waitingFlag = true;
        final AsyncTask<Void, Void, Boolean> asyncTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                while (waitingFlag) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        return false;
                    }
                }
                return true;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    asyncTask.get(timeout, TimeUnit.SECONDS); // 超时的时候会跳到TimeoutException。据说放到Thread里不会阻塞ui线程。
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    waitingFlag = false;
                    taskParam.cancel(true);
                    showToast(R.string.s_TimoutToCloseRos);
                } catch (Exception e) {
                    e.printStackTrace();
                    waitingFlag = false;
                    taskParam.cancel(true);
                }
            }
        }).start();
    }

    /**
     * 清理一些node方面的数据，避免重刷数据报错。
     * 清理前，必须已经初始化，否则不需要清理。
     */
    private void clearExecuted() {
        Log.i(TAG, "clearExecuted: 清空node");
        if (nodeMainExecutor == null) {
            Log.e(TAG, "clearExecuted: nodeMainExecutor is null");
            return;
        }

        if (id_joystick != null && id_joystick.getConnectedNode() != null)
            id_joystick.onShutdown(id_joystick.getConnectedNode());
        nodeMainExecutor.shutdownNodeMain(id_joystick);

        if (id_map != null && id_map.getConnectedNode() != null)
            id_map.onShutdown(id_map.getConnectedNode());
        nodeMainExecutor.shutdownNodeMain(id_map);

        if (status != null && status.isRunningRapp() && status.getServiceManager() != null && status.getConnectedNode() != null) {
            Log.i(TAG, "clearExecuted: rapp的node在此shutdown");
            status.getServiceManager().onShutdown(status.getConnectedNode());
            nodeMainExecutor.shutdownNodeMain(status.getServiceManager());
        }

        if (mPublisherSubscriber != null && mPublisherSubscriber.getConnectedNode() != null) {
            nodeMainExecutor.shutdownNodeMain(mPublisherSubscriber);
        }

    }


    /**
     * 判断位置是否已标记
     */
    private boolean isNewPositionName(String name) {
//        for (String existsName : mListPositionNames) {
//            if (name.equals(existsName)) {
//                return false;
//            }
//        }
//        return true;

        return occupancyGridLayer.isNewPositionName(name);
    }


    private double getScreenSizeOfDevice2() {
        return 10.1; //朱镜居用的白平板
//        Point point = new Point();
//        ((Activity)context).getWindowManager().getDefaultDisplay().getRealSize(point); // 需要api 17
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        double x = Math.pow(point.x/ dm.xdpi, 2);
//        double y = Math.pow(point.y / dm.ydpi, 2);
//        double screenInches = Math.sqrt(x + y);
//        Log.d(TAG, "Screen inches : " + screenInches);
//        return screenInches;
    }

    /**
     * 重置标记id
     */
    private void resetFlagId() {

        /** 固定地点 从10001开始，10000是原点。 */
        positionIdFixed = positionIdFixed_; //
        /** 自定义地点从11000开始 */
        positionIdCustom = positionIdCustom_;
        Log.i(TAG, "resetFlagId: positionIdFixed=" + positionIdFixed + ",positionIdCustom=" + positionIdCustom);
    }


    /**
     * 关闭wifi ap
     */
    private void disconnectAP() {
        Log.d(TAG, "disconnectAP");

        boolean isWifiStopped = true; // 有时候ap没连接上。
        if (connectMaster != null && connectMaster.isWifiConnected()) {
            isWifiStopped = connectMaster.disConnectAP(); // 这里关闭成功了。
            Log.i(TAG, "disconnectAP: wifi 是否已关闭：" + isWifiStopped);
            connectMaster.setBo();
        } else {

            Log.i(TAG, "disconnectAP: mWifiOldName=" + mWifiOldName + ",mWifiOldNetId=" + mWifiOldNetId);
            if (mWifiOldName != null && !mWifiOldName.equals("")) {
                WifiConfiguration tempConfig = isExsits(mWifiOldName);
                if (tempConfig != null) {
                    boolean enabled = wifi.enableNetwork(tempConfig.networkId, true);
                    Log.i(TAG, "disconnectAP: idGet=" + tempConfig.networkId + ",是否已连上原来的wifi?" + enabled);
                }
            }
        }
    }

    /**
     * todo 需要activity给消息过来。
     * 退出。如果在转圈圈，就取消，没有转圈圈，就退出。
     */
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed()");

        if (isWaiting()) {
            status.setNextAction("");
            safeDismissWaitingDialog();
            if (occupancyGridLayer.getmSendedRapp() != OccupancyGridLayerCustom.rapp.none) {
                occupancyGridLayer.setmSendedRapp(OccupancyGridLayerCustom.rapp.none);
            }
            if (isWaitforMap) isWaitforMap = false;
        } else {
            status.setNextAction(status.Action_Exit_BackKey);

            if (status.isRunning(RappNameMakeAMap)) { // 如果正在构建地图，就退出程序，不启动地图导航
                stopRapp(getStringLocal(R.string.s_actionForceShutDown));
                Log.i(TAG, "正在构建地图，就退出程序Rapp，不启动地图导航");
            } else if (status.isRunning(RappNameMapNav)) { // 如果启动了地图导航，直接forceshutdown
//                leaveRemoconServiceForceShutdown(null);
                Log.i(TAG, "启动了地图导航，直接forceshutdown");
                new LeaveRemoconTask().execute();
            } else { // 如果没有启动程序，则forceshutdown
                Log.i(TAG, "没有启动程序，则forceshutdown");
//                leaveRemoconServiceForceShutdown(null);
                new LeaveRemoconTask().execute();
            }

        }
    }

    /**
     * 退出remocon的异步线程
     */
    class LeaveRemoconTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isWaiting()) {
//                rl_progressBar.setVisibility(View.VISIBLE);
                safeShowWaitingDialog("", "", true);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            leaveRemoconServiceForceShutdown(null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (isWaiting()) {
                safeDismissWaitingDialog();
            }
        }
    }

    /**
     * 显示Toast
     */
    private void showToast(int strId) {
//        Toast.makeText(RemoconAllActivity.this, strId, Toast.LENGTH_LONG).show();
        showToast(getStringLocal(strId));
    }
//    /** 做一个list让相邻近的显示在一个Toast里面，然后隔1s才向toast发出 */
//    private String mToastString = "";
//    private long mLastToastTime = -1;
//    /** Toast显示时长：2000ms，Toast.LENGTH_LONG（3.5秒）和Toast.LENGTH_SHORT（2秒） */
//    private long mToastShowShiChang = 2000; //
//    private boolean isWaitForToast = false;

    /**
     * 显示Toast
     */
    private void showToast(String str) {
//        if(!mToastString.equals("")) mToastString += " ## ";
//        mToastString += str;
//        if(isWaitForToast) return;
//        long tmp = System.currentTimeMillis() - mLastToastTime;
//        if(tmp > mToastShowShiChang){
//        }
//        else{
//            try {
//                Log.i(TAG, "showToast: 等待"+tmp+"ms");
//                isWaitForToast = true;
//                Thread.sleep(tmp);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        Message msg = mHandler.obtainMessage();
        msg.what = Handler_Toast;
        msg.obj = str;
//        msg.obj = mToastString;
        mHandler.sendMessage(msg);
//        mToastString = "";
//        mLastToastTime = System.currentTimeMillis();
//        isWaitForToast = false;

    }

    /**
     * 自定义位置名称
     */
    private void showDialogCustomFlagName() {
        DialogCustom.getInstance().showDialogCustomFlagName(context, occupancyGridLayer, mHandler, mHandler_DialogResultCustomFlagName);
    }

    /**
     * 修改标记名称
     */
    private void showDialogModifyFlagName(final String positionId, final String positionOldName) {
        DialogCustom.getInstance().showDialogModifyFlagName(context, positionId, positionOldName, occupancyGridLayer, mHandler, mHandler_DialogResultModifyFlagName);

    }


    /**
     * 是否删除已有地图
     */
    private boolean isDeletedMap = false;

    /**
     * 是否删除已有地图
     */
    private boolean showDialogIfDeleteMaps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.s_isDelete);
        builder.setPositiveButton(getStringLocal(R.string.s_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selectedRole) {
//                deleteMaps();
                dialog.dismiss();
                isDeletedMap = true;
//									Log.i(TAG, "确定删除地图成功");
            }
        });
        builder.setNegativeButton(getStringLocal(R.string.s_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selectedRole) {
//									finish();
//									return;
                dialog.dismiss();
                isDeletedMap = false;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Log.i(TAG, "alert.isShowing():" + alert.isShowing());
        return isDeletedMap;
    }

    /**
     * 是否删除已有地图
     */
    private void showDialogIfNewMap() {

        DialogCustom.getInstance().showDialogIfNewMap(context, mHandler, mHandler_DialogResultIfNewMap);
    }

    /**
     * 提示对话框，只显示消息和确定按钮，其实还不如用toast显示。
     */
    private Dialog getNotifyDialog(String message) {
        return DialogCustom.getInstance().getNotifyDialog(context, message);
    }

    /**
     * 显示提示对话框，自定义
     */
    private void safeShowNotiDialogCustom(final CharSequence messageStr) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (notiDialogCustom != null) {
                    if (notiDialogCustom.isShowing()) {
                        notiDialogCustom.dismiss();
                    }
                    notiDialogCustom = null;
                }
                notiDialogCustom = getNotifyDialog(messageStr.toString());
                notiDialogCustom.show(); //error RemoconAllActivity has leaked window com.android.internal.policy.impl.PhoneWindow$DecorView{960d33 V.E..... R.....ID 0,0-640,257} that was originally added here
            }
        });
    }


    /**
     * 对话框，给代码中调用的
     *
     * @param cancelable 是否可取消
     */
    protected void safeShowWaitingDialog(final String title, final CharSequence message, final boolean cancelable) {
        Log.i(TAG, "safeShowWaitingDialog: ");

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rl_progressBar != null && !rl_progressBar.isShown()) {
                    rl_progressBar.setVisibility(View.VISIBLE);
                    Log.i(TAG, "safeShowWaitingDialog:已经显示了进度圈");

                    rl_progressBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (cancelable) {
                                safeDismissWaitingDialog();
                            }
                        }
                    });
                }

            }
        });


    }

    protected void safeDismissWaitingDialog() {
        Log.i(TAG, "safeDismissWaitingDialog: ");
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rl_progressBar != null && rl_progressBar.isShown()) {
                    rl_progressBar.setVisibility(View.INVISIBLE);
                    Log.i(TAG, "safeDismissWaitingDialog:已经隐藏了进度圈 rl_progressBar");
                }
            }
        });
    }

    /**
     * 进度圈是不是在显示
     */
    private boolean isWaiting() {
//        if(waitingProgBar != null) return waitingProgBar.isShown();
        if (rl_progressBar != null) return rl_progressBar.isShown();
        else return false;
    }

    //    private ProgressDialog waitingDialog;
    private ProgressDialog progressDialog;

    // 查看以前是否也配置过这个网络、这个网络的密码
    private WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = wifi.getConfiguredNetworks();

        if (existingConfigs != null) {
            for (WifiConfiguration existingConfig : existingConfigs) {
                Log.d(TAG, SSID + "    " + existingConfig.SSID + "  rem");
                if (existingConfig.SSID.equals("\"" + SSID + "\"")) {

                    Log.i(TAG, "isExsits: 已配置的wifi列表里存在 " + SSID + ", 密码：" + existingConfig.preSharedKey);
                    return existingConfig;
                }
//                if (existingConfig.SSID.equals( SSID  )) {
//
//                    Log.i(TAG, "isExsits: 已配置的wifi列表里存在 " + SSID + ", 密码：" + existingConfig.preSharedKey);
//                    return existingConfig;
//                }
            }
        } else Log.e(TAG, "isExsits: existingConfigs is null");
        return null;
    }

}
