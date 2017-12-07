package com.map.make_a_map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.grasp.control.R;
import com.map.View.BitmapShape;
import com.map.View.ConstantsForMap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.android.view.visualization.TextureBitmap;
import org.ros.android.view.visualization.VisualizationView;
import org.ros.android.view.visualization.layer.SubscriberLayer;
import org.ros.android.view.visualization.layer.TfLayer;
import org.ros.android.view.visualization.shape.Shape;
import org.ros.android.view.visualization.shape.TextShapeFactory;
import org.ros.internal.message.MessageBuffers;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.rosjava_geometry.Quaternion;
import org.ros.rosjava_geometry.Transform;
import org.ros.rosjava_geometry.Vector3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import nav_msgs.OccupancyGrid;
//import std_msgs.String;

/**
 * 几个方法的执行顺序： onNewMessage() --> update() --> draw() --> onSurfaceCreated() --> onSurfaceChanged()
 * Created by Administrator on 2017/1/9-009.
 * aaaaaa
 做地图背景色

 6b6b6b做地图颜色
 */
public class OccupancyGridLayerCustom extends SubscriberLayer<OccupancyGrid> implements TfLayer {
    private final String TAG = "Print-Occu";
//    private static final int COLOR_OCCUPIED = -15658735;
    private static final int COLOR_OCCUPIED = 0xffff5913; // 障碍物
//    private static final int COLOR_FREE = -1;
    private static final int COLOR_FREE = 0xffffffff; // 0xff6b6b6b; //0xffffffff; 地图颜色
    //    private static final int COLOR_UNKNOWN = -2236963;
//    private static final int COLOR_UNKNOWN = 0xffdddddd;//灰
    private static final int COLOR_UNKNOWN = 0xff6b6b6b; // 0xffaaaaaa; //0xff00ff00; // 0xff6b6b6b; // 0xff00ff00;
    //COLOR_UNKNOWN背景色，要同XYOrthographicRenderer的BACKGROUND_COLOR一样, 0xfff8c806,0xff6b6b6b
    private final List<Tile> tiles;
    private boolean ready;
    private boolean readyText;
    private GraphName frame;
    private GL10 previousGl;
    /**
     * 是否接收数据
     */
    private boolean isSubscribe = false;
    private ReceiveMapListener receiveMapListener;
    /** 是否存在地图，如果不存在，则需要在收到第一张地图的时候给Activity传递信息，好刷新地图。 */
    private boolean isExistsMap = false;
    /** 如果允许长按编辑标记 */
    private boolean isEditFlag = false;
    /** 正在发布地图 */
    private boolean isPublishingMap = false;
    /** 测试接收地图 */
    private boolean isTestReceiveMap = false;
    //    OccupancyGridLayerCustom mThis;
    private final int mHandler_UpdateMap = 0x200;
    private List<FlagBean> flagBeanList = null;
    private List<FlagBean> flagTempList = null;
    private VisualizationView view;
    private GL10 gl10;
    private TextShapeFactory textShapeFactory = null;
    private boolean isFirstDraw = true;
//    private boolean isFirstSurfaceChanged = true;
//    private boolean isFirstSurfaceCreated = true;
//    private int count = 0;
    /** 屏幕尺寸：px */
    private int mScreenWidthPx = 0;
    private int mScreenHeightPx = 0;

    private Object mutex = null;
    /**
     * 当前map的id，想用来清理地图色块
     */
    private OccupancyGrid occupancyGrid = null;

    /** 标记的半径 */
    private float mFlagRadius;

    private Context context;

    /** 屏幕物理尺寸：英寸 */
    private double mScreenInches;
    private List<String> strList = null;
    /** 长按、点击事件 */
    private GestureDetector gestureDetector;
    /** 长按事件传送消息回RemoconAllActivity */
    private Handler mHandler = null;
    private int whatModify = -1;
    private int whatCancel = -1;

    /** 当前app的状态是地图构建、地图导航、没有启动rapp */
    public enum rapp{
        make_a_map, map_nav, none
    }
    private rapp mStatusRapp = rapp.none;
    private rapp mSendedRapp = rapp.none; // 刚发出去就收到地图了，还来不及回调success方法，设置不了mStatusRapp

    private long mSendRappTime = 0;
    /** 发rapp到收地图之间的时间不能超过20s */
    private final long TimeGap = 20000;
    public OccupancyGridLayerCustom( Context context, String topic, ReceiveMapListener receiveMapListenerParam) {
        this(GraphName.of(topic));
        this.receiveMapListener = receiveMapListenerParam;
        isExistsMap = false;
        this.flagBeanList = new ArrayList<FlagBean>();
//        mThis = new OccupancyGridLayerCustom(topic);
        this.context = context;
        mFlagRadius = 80f;
        this.mutex = new Object();

        this.readyText = false;
        this.isEditFlag = false;
    }

    public OccupancyGridLayerCustom(GraphName topic) {
//        super(topic, "nav_msgs/OccupancyGrid");
        super(topic, OccupancyGrid._TYPE);
        this.tiles = Lists.newCopyOnWriteArrayList();
        this.ready = false;
    }

    @Override
    public void onSurfaceCreated(VisualizationView view, GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(view, gl, config);
        if (this.gl10 == null) {
            this.gl10 = gl;
        }
        if (this.view == null) {
            this.view = view;
        }
    }

    /** 是否删除旧的纹理 */
    private boolean isDeleteOld = false;
    public void draw(VisualizationView view, GL10 gl) {
        if (isFirstDraw) {
            Log.i(TAG, "draw()"); // 刷屏
            isFirstDraw = false;
        }



        Iterator iterator;
        OccupancyGridLayerCustom.Tile tile;
        if (this.previousGl != gl) {

            iterator = this.tiles.iterator();

            while (iterator.hasNext()) {
                Log.i(TAG, "draw: this.previousGl != gl， 将清空纹理");
                tile = (OccupancyGridLayerCustom.Tile) iterator.next();
                tile.clearHandle();
            }

            this.previousGl = gl;
        }

        if (this.ready) {
            iterator = this.tiles.iterator();

            while (iterator.hasNext()) {
                tile = (OccupancyGridLayerCustom.Tile) iterator.next();
                tile.draw(view, gl);
            }
        }

        // 文字必须在地图之后画出来，不然出错。
        if (this.readyText) {
            if (textShapeFactory == null ) {
                initTextShapeFactory(strList); // 没有地图的时候也会执行，onStart一执行，这里就开始初始化了，而strList还是null，必须放
                clearStrList();
                initFlagBeanList();
                clearFlagTempList();

                Log.i(TAG, "draw: textShapeFactory is null, 请先初始化textShapeFactory");
            } else {
                for (int i = 0; i < flagBeanList.size(); i++) {
                    FlagBean flagBean = flagBeanList.get(i);
                    flagBean.getShape().draw(view, gl);
                }
            }
        }

    }

    /** 初始化TextShapeFactory，设置字体、字大小、间隔xy */
    private void initTextShapeFactory(List<String> strListParam) {
//        if(strList.size() <= 0){
//            strList.add("*");
//        }
        if (textShapeFactory != null) {
            return;
        }
        Log.i(TAG, "initTextShapeFactory: 初始化textShapeFactory");

//        textShapeFactory = new TextShapeFactory(view, this.gl10, this.context, DemoApplication.typeface_Arial, 50, 50, 50, this.view.getCamera().getZoom(), strListParam);

        //字体设置

//        if(DemoApplication.typeface_Arial != null) {
//
//            textShapeFactory = new TextShapeFactory(view, this.gl10, this.context, DemoApplication.typeface_Arial, 50, 50, 50, this.view.getCamera().getZoom(), strListParam);
//        }
//        else {
            String fontType = "宋体";
            Typeface typeface = Typeface.create(fontType, Typeface.BOLD);
            textShapeFactory = new TextShapeFactory(view, this.gl10, this.context, typeface, 50, 50, 50, this.view.getCamera().getZoom(), strListParam);
//        }

//        if(strListParam != null) {
//            textShapeFactory.getGlText().setStrList(strListParam);
//            Log.i(TAG, "initTextShapeFactory: 设置了strList");
//        }

        textShapeFactory.getGlText().loadModule();
        isDeleteOld = false;

        // 设置字边距、单个字宽高
        float[] fontStartXY = textShapeFactory.getGlText().getFontStartXY();
        Vector3 vector3_margin = this.view.getCamera().toCameraFrame((int)fontStartXY[0], (int)fontStartXY[1]);
        Vector3 vector3_zero = this.view.getCamera().toCameraFrame(0, 0);
        textShapeFactory.getGlText().setmCameraMarginWidthHeight(new double[]{Math.abs(vector3_margin.getX() - vector3_zero.getX()),
                Math.abs(vector3_margin.getY() - vector3_zero.getY())});

        Log.i(TAG, "draw: view.getCamera().getZoom()=" + view.getCamera().getZoom());
    }

    private void initFlagBeanList() {
        Log.i(TAG, "initFlagBeanList: flagTempList is null?" + (flagTempList == null));
        if (flagTempList != null && flagTempList.size() > 0) {
            for (int i = 0; i < flagTempList.size(); i++) {
                FlagBean flag = flagTempList.get(i);
                addFlagBean((float)flag.getX(), (float)flag.getY(), flag.getId(), flag.getName());
            }
        }
    }

    public GraphName getFrame() {
        return this.frame;
    }

    public void onStart(final VisualizationView view, ConnectedNode connectedNode) {
        super.onStart(view, connectedNode);
        this.previousGl = null;
        this.getSubscriber().addMessageListener(new MessageListener<OccupancyGrid>() {
            public void onNewMessage(OccupancyGrid message) {
//                mAsyncRun.execute(message);
                // 原始地图的message.getHeader().getSeq() == 0，如果有异，立即改。发布的地图，地图固定，message.getHeader().getSeq()一直变动不固定。
                Log.i(TAG, "onNewMessage(),mStatusRapp=" + mStatusRapp +", mSendedRapp="+mSendedRapp+ "\t isExistsMap=" + isExistsMap+ "\t isPublishingMap=" + isPublishingMap + "\t header.stamp=" + message.getHeader().getStamp() + "\t header.FrameId=" + message.getHeader().getFrameId() + "\t header.Seq=" + message.getHeader().getSeq() + "\t info.MapLoadTime=" + message.getInfo().getMapLoadTime() + "\t info.Origin=" + message.getInfo().getOrigin());

                occupancyGrid = message;

                // 从前写法
//                if (message.getHeader().getSeq() == 0 && isExistsMap) { //如果是第一张地图，就必须收，否则后续无法刷新。，如果是构建地图，也必须接受第一张地图。
//                    Log.i(TAG, "收到原始地图，不处理");
//                    return;
//                } else if (isSubscribe) {
//                    Log.i(TAG, "onNewMessage: 显示地图 header.FrameId=" + message.getHeader().getFrameId());
//                    OccupancyGridLayerCustom.this.update(message); // 此处不更新时，内存50M
//                    if (!isExistsMap) {
//                        isExistsMap = true;
//                        Log.i(TAG, "onNewMessage: 发送消息"); // 解决不了地图与机器人不重合问题。如果每次来地图都刷新，且地图刷新后的位置不可见，则每次刷新后都要重新调整位置。
//                        receiveMapListener.onSuccess();
//                    }
//                }

                // 新写法
                /** 对于0地图，如果是地图导航或无rapp，就不显示了，如果是地图构建就显示；如果是x地图，none不显示，构建地图和地图导航显示。默认不显示地图。
                 * 0-none-SendMake 显，0-none-SendNotMake 不显, x-none 不显
                 * 0-make 显，x-make 显
                 * 0-nav 不显，x-nav-isPublishingMapTrue 显
                 *
                 */
                if ((message.getHeader().getSeq() == 0 && mStatusRapp == rapp.none && mSendedRapp != rapp.make_a_map)
                || (message.getHeader().getSeq() == 0 && mStatusRapp == rapp.map_nav) ||
                        (message.getHeader().getSeq() != 0 && mStatusRapp == rapp.none)) {
                    Log.i(TAG, "Seq="+message.getHeader().getSeq() +", mStatusRapp="+mStatusRapp+", mSendedRapp="+mSendedRapp+"\t 不显示地图");
//                    return; // 注释return可能会出错
                }
                else if ((mStatusRapp == rapp.make_a_map) ||
                        (message.getHeader().getSeq() != 0 && mStatusRapp == rapp.map_nav && isPublishingMap) ||
                        (mStatusRapp == rapp.none && mSendedRapp == rapp.make_a_map && message.getHeader().getSeq() == 0)) {
                    Log.i(TAG, "onNewMessage: 显示地图 header.FrameId=" + message.getHeader().getFrameId());
                    OccupancyGridLayerCustom.this.update(message); // 此处不更新时，内存50M
                    if(mSendedRapp != rapp.none) {mSendedRapp = rapp.none;}
                    if(isPublishingMap) setIsPublishingMap(false);
                    if (!isExistsMap) {
                        isExistsMap = true;
                        Log.i(TAG, "onNewMessage: 发送消息"); // 解决不了地图与机器人不重合问题。如果每次来地图都刷新，且地图刷新后的位置不可见，则每次刷新后都要重新调整位置。
                        receiveMapListener.onSuccess();
                    }
                } else if (isTestReceiveMap) {
                    Log.i(TAG, "onNewMessage: isTestReceiveMap="+isTestReceiveMap);
                    update(message);
//                    isTestReceiveMap = false;
                } else {
                    Log.e(TAG, "onNewMessage: 注意，未显示地图，mStatusRapp=" + mStatusRapp + ",mSendedRapp=" + mSendedRapp + ",Seq()=" + message.getHeader().getSeq());
                }

            }
        });

        this.view = view;


        // 添加点击、长按事件

        view.post(new Runnable() {
            @Override
            public void run() {
                gestureDetector = new GestureDetector(view.getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent event) {
                        Log.i(TAG, "onLongPress: ");
                        if(isEditFlag && flagBeanList != null && flagBeanList.size() > 0) {
                            FlagBean flagClick = getFlagClick(view, event);
                            Log.i(TAG, "onLongPress: flagClick is null?" + (flagClick == null) + ", mHandler is null?" + (mHandler == null));
                            if (flagClick != null && mHandler != null && whatModify != -1) {
                                Message msg = mHandler.obtainMessage();
                                msg.what = whatModify;
                                msg.obj = flagClick;
                                mHandler.sendMessage(msg);
                            }
                        }
                        else{
                            Log.e(TAG, "onLongPress: It's not edit mode, could'nt modify flag.");
                        }
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent event) {

                        FlagBean flagClick = getFlagClick(view, event);
                        Log.i(TAG, "onSingleTapUp: flagClick="+flagClick+",mHandler="+mHandler+",whatCancel="+whatCancel); // flagClick=null,mHandler=null,whatCancel=-1
                        if (flagClick != null ) { // 不点击在标记上，才可以停止，否则点击一次标记就停止了，不好。
                            Toast.makeText(OccupancyGridLayerCustom.this.context, flagClick.getName(), Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "onSingleTapUp: click：" + flagClick.getName() +", "+ flagClick.toString()); // getFlagClick: view=org.ros.android.view.visualization.VisualizationView{55359c3 V.E...... ........ 0,0-2048,1536 #7f0e025e app:id/id_map},flagBeanList=[FlagBean.toString: com.iot.remocon.make_a_map.FlagBean@460b4db, name: 客厅, id: 10001, x: 0.0, y: 0.0, shape: com.iot.remocon.View.BitmapShape@a9eb78, FlagBean.toString: com.iot.remocon.make_a_map.FlagBean@4e80251, name: 客厅, id: 10001, x: 0.0, y: 0.0, shape: org.ros.android.view.visualization.shape.TextShape@323dab6]
                        }
                        else{
                           if (mHandler != null && whatCancel != -1) {
                                mHandler.sendEmptyMessage(whatCancel);
                                Log.i(TAG, "onSingleTapUp: go to stop.");
                            }
                        }

//                        return super.onSingleTapUp(event);
                        return false;
                    }
                });
            }
        });
    }

    private void update(OccupancyGrid message) {
//        Log.i(TAG, "update()");
       clearTiles();
//        System.out.println("想知道刷新频率 - update"); // 只刷一次。
        float resolution = message.getInfo().getResolution();
        int width = message.getInfo().getWidth();
        int height = message.getInfo().getHeight();
        int numTilesWide = (int) Math.ceil((double) ((float) width / TextureBitmap.STRIDE));//TextureBitmap.STRIDE
        int numTilesHigh = (int) Math.ceil((double) ((float) height / TextureBitmap.STRIDE));//TextureBitmap.STRIDE
        int numTiles = numTilesWide * numTilesHigh;
        Log.i(TAG, "update() resolution=" + resolution + ", width=" + width + ", height=" + height + ", " +
                "numTilesWide=" + numTilesWide + ", " +
                "numTilesHigh=" + numTilesHigh + ", numTiles=" + numTiles);
        // resolution=0.01, width=3072, height=4544, numTilesWide=3, numTilesHigh=5, numTiles=15
        Transform origin = Transform.fromPoseMessage(message.getInfo().getOrigin());

        while (this.tiles.size() < numTiles) {
            this.tiles.add(new OccupancyGridLayerCustom.Tile(resolution));
        }

        int x;
        int y;
        for (x = 0; x < numTilesHigh; ++x) {
            for (y = 0; y < numTilesWide; ++y) {
                int buffer = x * numTilesWide + y; //x=1, 1 * 3 = 3
//                Log.i(TAG, "update: x=" + x + ", y=" + y + ", resolution=" + resolution); // x=0, y=0, resolution=0.05
//                (this.tiles.get(buffer)).setOrigin(origin.multiply(new Transform(new Vector3((double) ((float) y * resolution * 1024.0F), (double) ((float) x * resolution * 1024.0F), 0.0D), Quaternion.identity())));
                (this.tiles.get(buffer)).setOrigin( origin.multiply( new Transform(
                        new Vector3((double) ((float) y * resolution * 1024.0F), (double) ((float) x * resolution * 1024.0F), 0.0D),
                        Quaternion.identity() ) ) );

                Log.i(TAG, "update: origin: " + (this.tiles.get(buffer)).getOrigin().toString()
                + ", Scale=" + (this.tiles.get(buffer)).getOrigin().getScale()); // Quaternion.toString()=Quaternion<x: 0.0000, y: 0.0000, z: 0.0000, w: 1.0000>, Vector3.toString()=Vector3<x: -1.0000, y: -12.2000, z: 0.0000>, Scale=1.0

                if (y < numTilesWide - 1) {
                    (this.tiles.get(buffer)).setStride(1024);
                } else {
                    Log.e(TAG, "update: y >= numTilesWide - 1, width="+width+", y="+y); // y < numTilesWide - 1, width=384 每次处理地图都打印,当前地图resolution=0.05, width=384, height=320, numTilesWide=1, numTilesHigh=1, numTiles=1；384 % 1024 = 384
                    (this.tiles.get(buffer)).setStride(width % 1024); // 3072/1024=3, 3072%1024=0
// 此值会导致尺寸大的地图显示不全，例如width=3072, height=4544，size=13959226bytes的地图，会被整齐的切掉一部分。
                }

//                Log.i(TAG, "解析地图 y = " + y);
//                (this.tiles.get(buffer)).setStride(1024); //此处会导致地图虚、重复？此写法显示大地图会完整。
            }
        }

        x = 0;
        y = 0;
        ChannelBuffer var14 = message.getData();

        while (var14.readable()) {
            Preconditions.checkState(y < height);
            int i$ = y / 1024 * numTilesWide + x / 1024;
            byte tile = var14.readByte();
//            Log.i(TAG, "update: i$取值重复？i$="+i$+", x="+x+", y="+y+", tile="+tile); // i$取值重复？i$=0, x=294, y=563, tile=-1 ... i$取值重复？i$=0, x=856, y=575, tile=-1 ， x自增1 严重刷屏，影响app上地图生成速度。
            if (this.tiles.size() <= i$) {
                Log.e(TAG, "update: this.tiles.size() <= i$, tiles.size()="+tiles.size()+",i$="+i$);
            }
            else if (tile == COLOR_FREE) {
                (this.tiles.get(i$)).writeInt(COLOR_UNKNOWN); // 2017/7/5-周三 java.lang.ArrayIndexOutOfBoundsException: length=0; index=0
            } else if (tile < 50) {
                (this.tiles.get(i$)).writeInt(COLOR_FREE);
            } else {
                (this.tiles.get(i$)).writeInt(COLOR_OCCUPIED);
            }

            ++x;
            if (x == width) {
                x = 0;
                ++y;
            }
        }
        Log.i(TAG, "update: while(var14.readable()) x,y=("+x+", "+y+")"); // while(var14.readable()) x,y=(0, 320) 这是起始位置,多次前进后退刷地图，都是这个值。 x,y=(0, 576)
        Iterator var15 = this.tiles.iterator();

        while (var15.hasNext()) {
            OccupancyGridLayerCustom.Tile var16 = (OccupancyGridLayerCustom.Tile) var15.next();
            var16.update();
        }

        this.frame = GraphName.of(message.getHeader().getFrameId());
        Log.i(TAG, "update: frame="+frame); // frame=map 只打印一次
        this.ready = true;
    }

    private class Tile {
        private final ChannelBuffer pixelBuffer = MessageBuffers.dynamicBuffer();
        private final TextureBitmap textureBitmap = new TextureBitmap();
        private final float resolution;
        private Transform origin;
        private int stride;
        private boolean ready;

        public Tile(float resolution) {
            this.resolution = resolution;
            this.ready = false;
        }

        public void draw(VisualizationView view, GL10 gl) {
            if (this.ready) {
                this.textureBitmap.draw(view, gl);
//                new AsyncRun().execute(textureBitmap, view, gl);
//                Log.i(TAG, "Tile.draw()");

            }


        }

        public void clearHandle() {
            this.textureBitmap.clearHandle();
        }

        public void writeInt(int value) {
            this.pixelBuffer.writeInt(value); // ArrayIndexOutOfBoundsException
        }

        public void update() {
//            Log.i(TAG, "Tile.update()");
            Preconditions.checkNotNull(this.origin);    //NullPointerException，因为y=1开始
            Preconditions.checkNotNull(Integer.valueOf(this.stride));
            this.textureBitmap.updateFromPixelBuffer(this.pixelBuffer, this.stride, this.resolution, this.origin, COLOR_UNKNOWN);
            this.pixelBuffer.clear();
            this.ready = true;
//            Log.i(TAG, "Tile - update");
        }

        public void setOrigin(Transform origin) {
            this.origin = origin;
        }

        public Transform getOrigin() {
            return this.origin;
        }

        public void setStride(int stride) {
            this.stride = stride;
        }
        public Bitmap getBitmap(){
            return this.textureBitmap.getBitmap();
        }
        /**
         * 回收tile的Bitmap
         */
        public void recycleTextureBitmap() {
            this.textureBitmap.recycleBitmap();
        }
    }

    /**
     * 不接收ROS主机端的消息了
     */
    public void setSubscribe(boolean isSubscribeParam) {
        this.isSubscribe = isSubscribeParam;
    }

    /** 是否已存在地图。 */
    public void setIsExistsMap(boolean isFirstReceived) {
        this.isExistsMap = isFirstReceived;
    }

    public boolean isExistsMap() {
        return isExistsMap;
    }

    public OccupancyGrid getOccupancyGrid() {
        return occupancyGrid;
    }

    //    private Shape shapeGlobal = null;
//    private ArrayList<View> listViews = new ArrayList<View>();
//    private boolean isReloadFont = false;
    /**
     * 无法加入列表的时候，先暂存
     */
    private FlagBean flag;

    /**
     * 添加一个标记
     */
    public void addFlagBean( float x, float y, String id, String name) {//GraphName graphName
        Log.i(TAG, "addFlagBean(), x=" + x + ", y=" + y + ", id=" + id ); //+

        Transform transform = Transform.translation(x, y, 0.01);
        Log.i(TAG, "addFlagBean: transform.translate.x,y=(" + transform.getTranslation().getX()+","+transform.getTranslation().getY()+")");

//        Vector3 vector3 = this.view.getCamera().getScreenTransform(GraphName.of("map")).getTranslation();
//        Log.i(TAG, "addFlagBean: 屏幕坐标？=(" + vector3.getX() + ", " + vector3.getY() + ")"); // (0.0, 0.0) 一直不变

//        if (textShapeFactory == null) {
//            Log.i(TAG, "addFlagBean: textShapeFactory is null, 将初始化textShapeFactory");
//            initTextShapeFactory(null);
//        }

        Shape shape;
        if (transform != null && textShapeFactory != null) {

            List<String> newStringList = textShapeFactory.getGlText().getNewStringList(name);

            if (newStringList != null && newStringList.size() > 0) {
                Log.i(TAG, "addFlagBean: newStringList is null?" + (newStringList == null) + ", newStringList.size()=" + (newStringList == null ? "" : newStringList.size()));
                shape = new BitmapShape(this.context, R.mipmap.btn_map_zdy, this.view.getCamera().getPixelsPerMeter()); // 标记自定义坐标，用图片标记。
            }
            else {
                // 完全没有新的字，才可以画中文。
                Vector3 vector3_zero = this.view.getCamera().toCameraFrame(0, 0);
                float[] fontWidthHeight = textShapeFactory.getGlText().getFontsWidthHeight(name);
                Vector3 vector3_WidthHeight = this.view.getCamera().toCameraFrame((int)fontWidthHeight[0], (int)fontWidthHeight[1]);
                textShapeFactory.getGlText().setmCameraFontWidthHeight(new double[]{Math.abs(vector3_WidthHeight.getX() - vector3_zero.getX()),
                        Math.abs(vector3_WidthHeight.getY() - vector3_zero.getY())});
                shape = textShapeFactory.newTextShape(name, this.view.getCamera().getPixelsPerMeter()); // 字和图形不能共存？

//                Log.i(TAG, "addFlagBean: 字体缩放=" + textShapeFactory.getGlText().getScaleX() );
//                PixcelFlagShape shapeCircle = new PixcelFlagShape(this.view.getCamera().getPixelsPerMeter(), this.mFlagRadius); // 橘色，颜色正常显示，原因？
            }
            Shape shapeCircle = new BitmapShape(this.context, R.mipmap.btn_map_oo, this.view.getCamera().getPixelsPerMeter()); // 标记自定义坐标，用图片标记。灰色，原因？
            shapeCircle.setTransform(transform);
            flagBeanList.add(new FlagBean(shapeCircle, id, name));

            shape.setTransform(transform);
            shape.setColor(org.ros.android.view.visualization.Color.fromHexAndAlpha("ff5913", 0.5f)); // 文字的颜色

            flagBeanList.add(new FlagBean(shape, id, name)); // 最后一个是什么半径，就是什么半径。
            textShapeFactory.getGlText().getLocation(name);

//            Log.i(TAG, "addFlagBean: zoom=" + this.view.getCamera().getZoom() + ", this.view.getCamera().getPixelsPerMeter()=" + this.view.getCamera().getPixelsPerMeter());

        }
        else{
            Log.e(TAG, "addFlagBean: transform is null?" + (transform == null) + ", textShapeFactory is null?" + (textShapeFactory == null));
        }

    }

//    /**
//     * 再次启动地图导航时，要把所有的字都画出来
//     */
//    public void addFlagBeanAllStr(List<String> strListParam) {
//                // 这只在测试时用，正式环境里去掉 ----------start
//        Log.i(TAG, "addFlagBeanAllStr:初始化textShapeFactory， textShapeFactory is null?" + (textShapeFactory == null));
//        if (textShapeFactory == null) {
//            Log.i(TAG, "addFlagBeanAllStr: textShapeFactory is null, 将初始化textShapeFactory" + ", strListParam.size()=" + strListParam.size());
//            initTextShapeFactory(strListParam);
//        }
//        // 这只在测试时用，正式环境里去掉 ----------end+
//
//    }

    /** 获取标记列表，用来匹配是不是新的位置 */
    public List<FlagBean> getFlagBeanList() {
        return flagBeanList;
    }

    public void deleteFlagBean(int index) {
        Log.i(TAG, "deleteFlagBean 1: index="+index+",flagBeanList.size()="+flagBeanList.size());
        if (index < 0 || index >= flagBeanList.size() || flagBeanList.size() <= 0) {
            return;
        }
        synchronized (mutex) {
            this.flagBeanList.remove(index);
            Log.i(TAG, "deleteFlagBean 2: 已删掉第"+index+"个元素，flagBeanList.size()="+flagBeanList.size());
        }
    }

    public void setFlagBeanList(List<FlagBean> flagBeanList) {
        Log.i(TAG, "setFlagBeanList 1: flagBeanList.size()="+flagBeanList.size());
        synchronized (mutex) {
            this.flagBeanList = flagBeanList;
            Log.i(TAG, "setFlagBeanList 2: flagBeanList.size()="+flagBeanList.size());
        }
    }


//    public void setFlagBeanList(List<FlagBean> flagBeanListParam) {
    public void setFlagBeanList(int index, FlagBean flagBeanParam) {
        if (index <= 0 || flagBeanParam == null) {
            Log.e(TAG, "setFlagBeanList: index="+index+", flagBeanParam is null?"+(flagBeanParam == null));
            return;
        }
        synchronized (mutex) {
            this.flagBeanList.set(index, flagBeanParam);
            Log.i(TAG, "setFlagBeanList: ");
        }
    }

    /**
     * 设置是否正在发布地图
     */
    public void setIsPublishingMap(boolean isPublishing) {
        this.isPublishingMap = isPublishing;
    }
    /**
     * 设置rapp状态
     */
    public void setStatusRapp(rapp currRapp) {
        this.mStatusRapp = currRapp;
    }

    /** 设置发送的rappName，防止在success方法回调之前收到地图而处理误认为没有启动rapp */
    public void setmSendedRapp(rapp mSendedRapp) {
        this.mSendedRapp = mSendedRapp;
        Log.i(TAG, "setmSendedRapp: mSendedRapp="+mSendedRapp);
//        this.mSendRappTime = System.currentTimeMillis();
    }

    public rapp getmSendedRapp() {
        return mSendedRapp;
    }

    /** 清空坐标列表 */
    public void clearFlagBeanList() {
        if(flagBeanList != null && flagBeanList.size() > 0) {
            flagBeanList.clear();
            Log.i(TAG, "clearFlagBeanList: 已清理所有坐标");
        }

    }

    public boolean isNewPositionName(String name) {
        for (FlagBean flagBeanTemp : flagBeanList) {
            if (name.equals(flagBeanTemp.getName())) {
                return false;
            }
        }
        return true;
    }


//    /**
//     * 所有字符集
//     */
//    private List<String> strList = new ArrayList<String>();

//    /**
//     * 获取增加的字符数组
//     */
//    public List<String> resetNewStringList(String strsModule) {
//        Log.i(TAG, "getNewStringList: ");
//        List<String> strsParam = new ArrayList<String>();
//        String strTemp = "";
//        for (int i = 0; i < strsModule.length(); i++) {
//            // 取单个字
//            if (strsModule.length() > 1) strTemp = strsModule.substring(i, i + 1);
//            else strTemp = strsModule.substring(i);
//            boolean isExist = false;
//            // 如果已存在就不添加
//            for (String strExisted : strList) {
//                if (strExisted.equals(strTemp)) {
//                    isExist = true;
//                    Log.i(TAG, "getNewStringList: 已存在字则不添加字模板：strExisted=" + strExisted + ", strTemp=" + strTemp);
//                    break;
//                }
//            }
//            if (!isExist) {
//                strsParam.add(strTemp);
//                strList.add(strTemp);  // 把字符加到字符列表里去。
//                Log.i(TAG, "getNewStringList: strTemp=" + strTemp);
//            }
//        }
//        if (strsParam.size() <= 0) {
//            Log.i(TAG, "getNewStringList: 没有需要添加的字模板，不添加");
//        } else {
////            charWidthList.clear();
////            charRgnList.clear();
//            Log.i(TAG, "getNewStringList: 需要添加的字模板strsParam.size()=" + strsParam.size() + ", strsParam.toString()=" + strsParam.toString()); // 需要添加的字模板strsParam.size()=1, strsParam.toString()=[客厅]
//        }
//        return strList;
//    }




    public void clearTiles() {
        if (this.tiles != null && this.tiles.size() > 0) {
            this.tiles.clear();
            isExistsMap = false;
            if(isPublishingMap) isPublishingMap = false;
            Log.i(TAG, "已清理地图, this.tiles.size()=" + this.tiles.size() + ", isExistsMap=" + isExistsMap);
        }
    }

    public void updateTiles() {
        Iterator var15 = this.tiles.iterator();

        while (var15.hasNext()) {
            OccupancyGridLayerCustom.Tile var16 = (OccupancyGridLayerCustom.Tile) var15.next();
            var16.update();
        }
    }

    /**
     * 回收所有tile的Bitmap
     */
    public void recycleTile() {
        for (Tile tile : tiles) {
            tile.recycleTextureBitmap();
            tile.clearHandle();
            tile = null;
            Log.i(TAG, "recycleTile()执行了");
        }
    }

    /** 手指点击半径 */
    private final double clickRadius = 0.5;
    @Override
    public boolean onTouchEvent(VisualizationView view, MotionEvent event) {
//        if (flagBeanList == null || flagBeanList.size() <= 0) {
//            return false;
//        }
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_UP:
////                FlagBean flagClick = getFlagClick(view, event);
////                if (flagClick != null) {
////                    Toast.makeText(this.context, flagClick.getName(), Toast.LENGTH_SHORT).show();
////                    Log.i(TAG, "onTouchEvent: click：" + flagClick.getName());
////                }
//
////                if (!clickName.equals("")) {
////                    Toast.makeText(this.context, clickName, Toast.LENGTH_SHORT).show();
//////                return false; // 不可true，否则后续手指按下事件无法处理。
////                }
//                break;
//            default:
//                break;
//        }

        if(gestureDetector != null){
            gestureDetector.onTouchEvent(event);
        }
        return  false;
    }

    public FlagBean getFlagClick(VisualizationView view, MotionEvent event){
        Log.i(TAG, "getFlagClick: view="+view+",flagBeanList="+flagBeanList);
        if (view == null || flagBeanList == null || flagBeanList.size() <= 0) {
            return null;
        }
        FlagBean resultFlag = null;
//        if (mScreenInches <= 0 || mScreenWidthPx <= 0 || mScreenHeightPx <= 0) {
//            Vector3 vec1 = view.getCamera().toCameraFrame(0, 0);
//            Vector3 vec2 = view.getCamera().toCameraFrame((int)(this.mFlagRadius), (int)(this.mFlagRadius));
//            clickRadius = Math.sqrt( Math.pow(vec2.getX() - vec1.getX(), 2) + Math.pow(vec2.getY() - vec1.getY(), 2));
//        }
//        else{
//            clickRadius = ( Math.sqrt(Math.pow(mScreenWidthPx, 2) + Math.pow(mScreenHeightPx, 2)) / mScreenInches) * 0.2; // 手指点击范围直径1cm，即raduis=0.5cm。0.0
//        }
        double disFlagFinger = clickRadius;   // 手指到附近点的距离，离谁最近就说点击了谁。
        String clickName = "";
        double disTemp = 0;
//                Transform fingerTrans = Transform.translation(view.getCamera().toCameraFrame((int) event.getX(), (int) event.getY()));
        Vector3 fingerVec = view.getCamera().toCameraFrame((int) event.getX(), (int) event.getY());
        for (FlagBean position : flagBeanList) {
            disTemp = Math.sqrt( Math.pow(position.getPose().getTranslation().getX() - fingerVec.getX(), 2) +
                    Math.pow(position.getPose().getTranslation().getY() - fingerVec.getY(), 2) );

//                    Log.i(TAG, "onTouchEvent:event.x,y=(" + event.getX() + ", " + event.getY() + ")" + ", eventToRos.x,y=(" + fingerVec.getX() + ", " + fingerVec.getY() + ", " + fingerVec.getZ() + ")"+", ros.clickRadius=" + clickRadius + ", " + position.getName() + ": position.pose.x,y=(" + position.getPose().getTranslation().getX() + ", " + position.getPose().getTranslation().getY() + "), position.x,y=(" +position.getX() + ", " + position.getY() +  "), disTemp=" + disTemp);
            if(disTemp <= clickRadius){
                if (disTemp <= disFlagFinger) {
                    disFlagFinger = disTemp;
                    clickName = position.getName();
                    resultFlag = position;
                }
            }
        }
//        if (!clickName.equals("")) {
//            Toast.makeText(this.context, clickName, Toast.LENGTH_SHORT).show();
//            Log.i(TAG, "onTouchEvent: click：" + clickName + ", disTemp=" + disTemp);
////                return false; // 不可true，否则后续手指按下事件无法处理。
//        }

        return resultFlag;
    }

    public void setmScreenWidthPx(int mScreenWidthPx) {
        this.mScreenWidthPx = mScreenWidthPx;
    }

    public void setmScreenHeightPx(int mScreenHeightPx) {
        this.mScreenHeightPx = mScreenHeightPx;
    }

    public void setmScreenInches(double mScreenInches) {
        this.mScreenInches = mScreenInches;
    }

    public TextShapeFactory getTextShapeFactory() {
        return textShapeFactory;
    }

    public void setStrList(List<String> strList) {
        this.strList = strList;
    }

    public boolean isReadyText() {
        return readyText;
    }

    public void setReadyText(boolean readyText) {
        this.readyText = readyText;
    }

    //    public void setFlagBeanList(List<FlagBean> flagBeanList) {
//        this.flagBeanList = flagBeanList;
//    }


    public void initHandler(Handler handler, int whatModify, int whatCancel) {
        this.mHandler = handler;
        this.whatModify = whatModify;
        this.whatCancel = whatCancel;
    }
    public void setFlagTempList(List<FlagBean> flagTempList) {
        this.flagTempList = flagTempList;
    }

    public void addFlagTempList(double x, double y, String id, String name) {
        if(flagTempList == null) flagTempList = new ArrayList<>();

        flagTempList.add(new FlagBean(id, name, (float) x, (float) y));
        Log.i(TAG, "addFlagTempList: flagTempList.size()=" + flagTempList.size());
    }

    public void saveMapPng() {
        Log.i(TAG, "saveMapPng: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Iterator tileIterator = OccupancyGridLayerCustom.this.tiles.iterator();
                int i = 0;
                while (tileIterator.hasNext()) {
                    OccupancyGridLayerCustom.Tile var16 = (OccupancyGridLayerCustom.Tile) tileIterator.next();

                    Bitmap drawingCache = var16.getBitmap(); //getDrawingCache是空的
                    if(drawingCache == null) continue;
//        String filename = "testMap"+"_" +".png";
//        File sd = Environment.getExternalStorageDirectory();
                    File folder = new File(ConstantsForMap.FileFolder);
                    if(!folder.exists()) folder.mkdirs();
                    folder = new File(ConstantsForMap.FileFolderMap);
                    if(!folder.exists()) folder.mkdirs();
                    if(folder.isDirectory() && folder.listFiles().length > 0){
                        // 删掉从前的地图文件
                        File[] files = folder.listFiles();
                        for (int j = files.length - 1; j >= 0; j--) {
                            if(files[j].getAbsolutePath().startsWith(ConstantsForMap.File_MapPng)) {
                                Log.i(TAG, "saveMapPng: 删掉地图png文件：" + files[j].getAbsolutePath()); // /storage/emulated/0/resource/map_png/.thumbnail
                                files[j].delete();
                            }
                        }
                    }
                    File dest = new File(ConstantsForMap.File_MapPng); // 这里只会有一张图片 new File(sd, filename);  +(i++)+ConstantsForMap.File_MapPng_Suffix
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(dest);
                        drawingCache.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance // W/System.err
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    Log.i(TAG, "saveMap: fileName="+dest.getAbsolutePath());
                }
            }
        }).start();

//        return filePic.getAbsolutePath();
//        return sd.getAbsolutePath();
    }
    public void resetFlagBeanList() {
        if (textShapeFactory != null && flagTempList != null && flagTempList.size() > 0) {
            initFlagBeanList();
            clearFlagTempList();
        }
    }



    public void clearStrList() {
        if (strList != null) {
            strList.clear();
            strList = null;
        }
        Log.i(TAG, "clearStrList: 已清理strList");
    }

    public void clearFlagTempList() {
        if (flagTempList != null) {
            flagTempList.clear();
            flagTempList = null;
        }
        Log.i(TAG, "clearFlagTempList: 已清理flagTempList");
    }

    /** 是否允许长按修改标记 */
    public void setEditFlag(boolean editFlag) {
        isEditFlag = editFlag;
    }

    //    /** 销毁线程 */
//    public void cancelAsyncRun() {
//        if(mAsyncRun != null){
//            mAsyncRun.cancel(true);
//            mAsyncRun = null;
//        }
//    }
//    class AsyncRun extends AsyncTask<OccupancyGrid, Integer, Integer> {
//        @Override
//        protected Integer doInBackground(OccupancyGrid...args){
////            OccupancyGrid message = args[0];
//            if(args[0] != null) OccupancyGridLayerCustom.this.update(args[0]);
////            TextureBitmap textureBitmap = (TextureBitmap)args[0];
////            VisualizationView view = (VisualizationView)args[1];
////            GL10 gl = (GL10)args[2];
////            if(textureBitmap == null || view == null || gl == null){
////                Log.i(TAG, "tile == null || view == null || gl == null");
////                return null;
////            }
////                textureBitmap.draw(view, gl);
////            this.cancel(true);
//            return null;
//        }
//    }


}
