package com.grasp.control.view;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Player.Core.PlayerClient;
import com.Player.web.websocket.ClientCore;
import com.grasp.control.R;
import com.grasp.control.Umeye_sdk.Constants;
import com.grasp.control.fragmet.ImageMap;
import com.grasp.control.sqlite.AddSQLiteHelper;
import com.grasp.control.sqlite.Equipment;
import com.grasp.control.tool.MyApplication;
import com.grasp.control.tool.SharedPreferencesUtils;
import com.grasp.control.tool.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 11111111
 * Created by zhujingju on 2017/7/20.
 */

public class LayoutMain1 extends Fragment {


    @BindView(R.id.layout1_time)
    TextView time;
    @BindView(R.id.layout1_di)
    LinearLayout layout1Di;
    //    @BindView(R.id.player_ui2)
//    EZUIPlayer playerUi2;
//    @BindView(R.id.player_ui)
//    EZUIPlayer playerUi;
//    @BindView(R.id.layout1_bf)
//    ImageView layout1Bf;
//    @BindView(R.id.layout1_bf2)
//    ImageView layout1Bf2;
    @BindView(R.id.layout1_huan)
    ImageView layout1Huan;

    @BindView(R.id.layout1_dl)
    TextView layout1Dl;
    @BindView(R.id.layout1_sc)
    TextView layout1Sc;
    @BindView(R.id.layout1_mj)
    TextView layout1Mj;
    @BindView(R.id.layout1_robot)
    ImageView layout1Robot;
    @BindView(R.id.layout1_csdh)
    ImageView layout1Csdh;
    @BindView(R.id.layout1_bu1)
    ImageView layout1Bu1;
    @BindView(R.id.layout1_bu2)
    ImageView layout1Bu2;
    @BindView(R.id.layout1_bu3)
    ImageView layout1Bu3;
    @BindView(R.id.layout1_yddh)
    ImageView layout1Ydyh;

    @BindView(R.id.layout1_rel_yd)
    ControlView layout1RelYd;
    @BindView(R.id.layout1_rel_huan)
    RelativeLayout layout1RelHuan;
    @BindView(R.id.layout1_robot2)
    ImageView layout1Robot2;
    @BindView(R.id.layout1_move_up)
    Button layout1MoveUp;
    @BindView(R.id.layout1_move_down)
    Button layout1MoveDown;
    @BindView(R.id.layout1_rel_move)
    LinearLayout layout1RelMove;


    @BindView(R.id.layout1_hlistview)
    HorizontalListView listView;


    @BindView(R.id.layout_main1_frame1)
    FrameLayout Frame1;
    @BindView(R.id.layout_main1_frame2)
    FrameLayout Frame2;



    Unbinder unbinder;
    private Context context;
    private LayoutVideo video,video2;
    private ImageMap map,map2;

//    private EZUIPlayer mEZUIPlayer;

    private static final String TAG = "qqq";
//    /**
//     * onresume时是否恢复播放
//     */
//    private boolean isResumePlay = false;
//
//    private MyOrientationDetector mOrientationDetector;
//
//    /**
//     * 开发者申请的Appkey
//     */
//    private String appkey = "272a51a679e34ffba3542ed5b8c5c2de";
//    private String appSecret = "9780f03a715ff013f336109683e369ce";
//    /**
//     * 授权accesstoken
//     */
//    private String accesstoken = "";


    public String Uid = "";  //摄像头编号

    /**
     * 播放url：ezopen协议
     */
//    private String playUrl = "ezopen://open.ys7.com/" + Uid + "/1.hd.live";
//    private String getPlayUrl(String s){
//        return "ezopen://open.ys7.com/" + s + "/1.hd.live";
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_main1, container, false);
        context = getActivity();


        unbinder = ButterKnife.bind(this, view);

         video=new LayoutVideo();
         video2=new LayoutVideo();
         map=new ImageMap();
        map2= new ImageMap();

        video.setBF();
        video2.setBF();

        initFragment1(video);

        ha.sendEmptyMessageDelayed(1234,500);

        layout1RelYd.setControlListener(new ControlView.ControlListener() {
            @Override
            public void Control(final int posi) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ExcuteCommand(posi);
                    }
                }).start();
            }
        });


        alphaAnimation1 = new AlphaAnimation(1f, 0f);
        //设置动画时间
        alphaAnimation1.setDuration(2000);

        alphaAnimation2 = new AlphaAnimation(0f, 1.0f);
        //设置动画时间
        alphaAnimation2.setDuration(2000);

        alphaAnimation3 = new AlphaAnimation(1f, 0f);
        //设置动画时间
        alphaAnimation3.setDuration(2000);

        alphaAnimation4 = new AlphaAnimation(0f, 1.0f);
        //设置动画时间
        alphaAnimation4.setDuration(2000);

        initListview();
        onTo onto = new onTo();

        layout1MoveUp.setOnTouchListener(onto);
        layout1MoveDown.setOnTouchListener(onto);
        initPlayerClient();

//        ha.sendEmptyMessageDelayed(369,1000);
        return view;
    }


    //显示fragment
    private void initFragment1(Fragment f1) {
        if(Frame1==null){
            return;
        }
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
//        if(f1 == null){
//            f1 = new MyFragment("消息");
        transaction.replace(R.id.layout_main1_frame1, f1);
//        }
        //隐藏所有fragment
//        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(f1);

        //第二种方式(replace)，初始化fragment
//        if(f1 == null){
//            f1 = new MyFragment("消息");
//        }
//        transaction.replace(R.id.main_frame_layout, f1);

        //提交事务
        transaction.commit();
    }


    //显示fragment
    private void initFragment2(Fragment f1) {
        if(Frame1==null){
            return;
        }
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
//        if(f1 == null){
//            f1 = new MyFragment("消息");
        transaction.replace(R.id.layout_main1_frame2, f1);
//        }
        //隐藏所有fragment
//        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(f1);

        //第二种方式(replace)，初始化fragment
//        if(f1 == null){
//            f1 = new MyFragment("消息");
//        }
//        transaction.replace(R.id.main_frame_layout, f1);

        //提交事务
        transaction.commit();
    }

    private boolean huan_zt = true;//换 状态

    private boolean huan_qs = false;//清扫 状态

    private boolean huan_yd = false;//移动 状态

    private boolean huan_move = false;//杆移动 状态

    private boolean one_zt = true;


    @OnClick({R.id.layout1_robot2, R.id.layout1_huan, R.id.layout1_robot, R.id.layout1_bu1, R.id.layout1_bu2, R.id.layout1_bu3})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.layout1_huan:
//                LayoutVideo video=new LayoutVideo();
//                LayoutVideo video2=new LayoutVideo();
//                LayoutMainMAP map=new LayoutMainMAP();
//                LayoutMainMAP map2=new LayoutMainMAP();
//                video.setUid(Uid);
//                video.setBF();
//                video2.setUid(Uid);
//                video2.setBF();
                if (huan_zt) {
                    initFragment1(map);
                    initFragment2(video2);
                } else {
                    initFragment1(video);
                    initFragment2(map2);
                }

                huan_zt = !huan_zt;
                break;

            case R.id.layout1_robot:
                huan_move = false;

                if (huan_yd) {
                    layout1RelMove.setVisibility(View.GONE);
                    layout1RelHuan.setVisibility(View.VISIBLE);
                    layout1RelYd.setVisibility(View.GONE);
                    layout1Ydyh.setVisibility(View.GONE);
                    ha.removeMessages(160);
                    ha.removeMessages(161);



                } else {
                    layout1RelMove.setVisibility(View.GONE);
                    layout1RelHuan.setVisibility(View.GONE);
                    layout1RelYd.setVisibility(View.VISIBLE);
                    layout1Ydyh.setVisibility(View.VISIBLE);
                    layout1Ydyh.startAnimation(alphaAnimation2);
                    ha.sendEmptyMessageDelayed(160, 2000);


                }
                huan_yd = !huan_yd;

                if (huan_zt == false) {
                    initFragment1(video);
                    initFragment2(map2);
                    huan_zt = true;
                }


                break;

            case R.id.layout1_robot2:
                huan_yd = false;
                ha.removeMessages(160);
                ha.removeMessages(161);
                layout1Ydyh.setVisibility(View.GONE);
                if (huan_move) {
                    layout1RelHuan.setVisibility(View.VISIBLE);
                    layout1RelMove.setVisibility(View.GONE);
                    layout1RelYd.setVisibility(View.GONE);

                } else {
                    layout1RelHuan.setVisibility(View.GONE);
                    layout1RelMove.setVisibility(View.VISIBLE);
                    layout1RelYd.setVisibility(View.GONE);

                }
                huan_move = !huan_move;


                if (huan_zt == false) {
                    initFragment1(video);
                    initFragment2(map2);
                    huan_zt = true;
                }


                break;
            case R.id.layout1_bu1:
                break;
            case R.id.layout1_bu2:
                if (huan_qs) {
                    layout1Csdh.setVisibility(View.GONE);
                    ha.removeMessages(154);
                    ha.removeMessages(155);
                    setDevInfo2(32 + "");
                } else {
                    layout1Csdh.setVisibility(View.VISIBLE);
                    layout1Csdh.startAnimation(alphaAnimation2);
                    ha.sendEmptyMessageDelayed(154, 2000);
                    setDevInfo2(31 + "");
                }
                huan_qs = !huan_qs;
                break;
            case R.id.layout1_bu3:

                break;

        }
    }







    Handler ha = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 120:
                    if (Frame1 != null) {

                        if(huan_zt){
                            video.setUid2(Uid);
                            video2.setUid(Uid);
                        }else{
                            video.setUid(Uid);
                            video2.setUid2(Uid);
                        }

                        long sysTime = System.currentTimeMillis();
                        CharSequence sysTimeStr = DateFormat.format("hh:mm", sysTime);
                        time.setText(sysTimeStr); //更新时间
                        ha.sendEmptyMessageDelayed(123, 1000);
                    }


                    break;
                case 123:
                    long sysTime1 = System.currentTimeMillis();
                    CharSequence sysTimeStr1 = DateFormat.format("hh:mm", sysTime1);
                    if (time != null) {
                        time.setText(sysTimeStr1); //更新时间
                    }

                    ha.sendEmptyMessageDelayed(123, 1000);
                    break;

                case 154:   //清扫动画
                    if (layout1Csdh != null) {
                        layout1Csdh.setVisibility(View.GONE);
                        layout1Csdh.startAnimation(alphaAnimation1);
                        ha.sendEmptyMessageDelayed(155, 2000);
                    }

                    break;
                case 155:   //清扫动画
                    if (layout1Csdh != null) {
                        layout1Csdh.setVisibility(View.VISIBLE);
                        layout1Csdh.startAnimation(alphaAnimation2);
                        ha.sendEmptyMessageDelayed(154, 2000);
                    }

                    break;


                case 160:   //移动动画
                    if (layout1Ydyh != null) {
                        layout1Ydyh.setVisibility(View.GONE);
                        layout1Ydyh.startAnimation(alphaAnimation3);
                        ha.sendEmptyMessageDelayed(161, 2000);
                    }

                    break;
                case 161:   //移动动画
                    if (layout1Ydyh != null) {
                        layout1Ydyh.setVisibility(View.VISIBLE);
                        layout1Ydyh.startAnimation(alphaAnimation4);
                        ha.sendEmptyMessageDelayed(160, 2000);
                    }
                    break;

                case 999:

                    if (g_list.size() > 0) {
                        adapter.setList(g_list);
                        adapter.notifyDataSetChanged();
                        if (listView != null) {
                            LinearLayout.LayoutParams lin = (LinearLayout.LayoutParams) listView.getLayoutParams();
                            lin.width = g_list.size() * w;
                            listView.setLayoutParams(lin);
                        }


                        Uid = g_list.get(num_post - 1).getSid();
                        getDate((String) SharedPreferencesUtils.getParam(context, "UID" + g_list.get(num_post - 1).getUid(), ""));
                        setDevInfo3();
                    }

                    ha.sendEmptyMessage(120);
                    break;

                case 1999:
                    num_post = 1;
                    adapter.setList(g_list);
                    adapter.notifyDataSetChanged();
                    if (listView != null) {
                        LinearLayout.LayoutParams lin = (LinearLayout.LayoutParams) listView.getLayoutParams();
                        lin.width = g_list.size() * w;
                        listView.setLayoutParams(lin);
                    }

                    if (g_list.size() > 0) {
                        if (adapter != null) {
                            Uid = g_list.get(0).getSid();
                            if (!Uid.equals("")) {
                                Log.d("eee",Uid+"  "+huan_zt);
                                if(huan_zt){
                                    video.setUid2(Uid);
                                    video2.setUid(Uid);
                                }else{
                                    video.setUid(Uid);
                                    video2.setUid2(Uid);
                                }
                                    getDate((String) SharedPreferencesUtils.getParam(context, "UID" + g_list.get(num_post - 1).getUid(), ""));
                                    setDevInfo3();

                            }
                        }


                    } else {
                        Uid = "";
                        Log.d("eee",Uid+"  "+huan_zt);
                        if(huan_zt){
                            video.setUid2(Uid);
                            video2.setUid(Uid);
                        }else{
                            video.setUid(Uid);
                            video2.setUid2(Uid);
                        }
                    }
//                    ha.sendEmptyMessage(120);
                    break;

                case 369:
                    setDevInfo3();
                    break;
                case 1234:
                    if(huan_zt){
                        initFragment2(map2);
                }else{
                        initFragment1(map); // java.lang.IllegalStateException: Activity has been destroyed
                    }

                    break;
            }
        }
    };


    //Alpha动画 - 渐变透明度
    private Animation alphaAnimation1 = null;
    private Animation alphaAnimation2 = null;
    private Animation alphaAnimation3 = null;
    private Animation alphaAnimation4 = null;

    @Override
    public void onDestroyView() {

        Log.d("qqq", "onDestroyView");
        ha.removeCallbacks(null);
        han.removeCallbacks(null);
        super.onDestroyView();


        unbinder.unbind();

    }






    public void setZt() {
        huan_zt = true;
        one_zt = true;
    }



    private boolean isStopCloudCommand = false;
    public static final byte MD_STOP = 0; // 停止
    public static final byte MD_LEFT = 11; // 左
    public static final byte MD_RIGHT = 12; // 右
    public static final byte MD_UP = 9; // 上
    public static final byte MD_DOWN = 10; // 下

    private PlayerClient playClient;
    private MyApplication appMain;

    //    private  PlayerCore pc;
    private void initPlayerClient() {
        appMain = (MyApplication) ((Activity) context).getApplication();
        playClient = appMain.getPlayerclient();
//        pc = new PlayerCore(context);
//        startBestServer();
    }


    void setDevInfo2(final String s) {  //修改
        if (g_list == null) {
            return;
        }

        if (g_list.size() == 0) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {


                Constants.UMID = g_list.get(num_post - 1).getUid();
//                Constants.user = "admin";
//                Constants.password = ;

                JSONObject onj = null;
                try {
                    onj = new JSONObject();
                    onj.put("Operation", 3);
                    onj.put("Request_Type", 0);
                    onj.put("value", s);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestStr = onj.toString();
                Log.e("Test", requestStr);
                byte[] b_request = requestStr.getBytes();
                Log.e("Test", playClient + "  Constants.UMID =" + Constants.UMID + " Constants.user=" + Constants.user + " Constants.password=" + Constants.password + " b_request=" + b_request);
                byte[] result = playClient.CallCustomFunc(Constants.UMID,
                        Constants.user, Constants.password, 66052, b_request);
                Message msg = Message.obtain();
                msg.what = GET_FAIL;

                if (null != result) {
                    String reStr = new String(result);
                    // String reString = new String(Base64.decode(result,
                    // Base64.DEFAULT));// new
                    // String(result);
                    msg.what = GET_SUCCESS;
                    msg.obj = reStr;
                    Log.e("Test", result + "  good");
                } else {
                    Log.e("Test", result + " err");
                }
//                handler.sendMessage(msg);
            }
        }).start();
    }


    void setDevInfo3() {  //获得数据
        ha.removeMessages(369);
        if (g_list == null) {
            return;
        }

        if (g_list.size() == 0) {
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {


                Constants.UMID = g_list.get(num_post - 1).getUid();
//                Constants.user = "admin";
//                Constants.password = ;

                JSONObject onj = null;
                try {
                    onj = new JSONObject();
                    onj.put("Operation", 4);
                    onj.put("Request_Type", 0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestStr = onj.toString();
                Log.e("Test", "  Constants.UMID =" + Constants.UMID + " Constants.user=" + Constants.user + " Constants.password=" + Constants.password + " b_request=");

                Log.e("Test", Uid + "" + requestStr);
                byte[] b_request = requestStr.getBytes();
                byte[] result = playClient.CallCustomFunc(Constants.UMID,
                        Constants.user, Constants.password, 66052, b_request);
                Message msg = Message.obtain();
                msg.what = SET_FAIL;

                if (null != result) {
                    String reStr = new String(result);
                    // String reString = new String(Base64.decode(result,
                    // Base64.DEFAULT));// new
                    // String(result);
                    msg.what = SET_SUCCESS;
                    msg.obj = reStr;
                    Log.e("Test", result + "  good");
                } else {
                    Log.e("Test", result + " err");
                }
                han.sendMessage(msg);
            }
        }).start();
    }

    public static final int GET_SUCCESS = 1;

    public static final int GET_FAIL = 2;

    public static final int SET_SUCCESS = 3;

    public static final int SET_FAIL = 4;

    Handler han = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_SUCCESS:
                    Log.d("qqq", msg.obj.toString());
                    getDate(msg.obj.toString());
                    if (g_list == null) {
                        return;
                    }

                    if (g_list.size() == 0) {
                        return;
                    }
                    SharedPreferencesUtils.setParam(context, "UID" + g_list.get(num_post - 1).getUid(), msg.obj.toString());


                    break;
                case SET_FAIL:
                    ha.sendEmptyMessageDelayed(369, 60 * 3 * 1000);
                    break;
            }
        }
    };

    public void getDate(String s) {
        if(layout1Dl==null){
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject jo = jsonObject.getJSONObject("value");
            String electric = jo.getString("electric");
            String work_often = jo.getString("work_often");
            String floor_area = jo.getString("floor_area");
            layout1Dl.setText(electric);
            layout1Sc.setText(work_often);
            layout1Mj.setText(floor_area);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private ClientCore clientCore;

    /**
     * 新接口，获取最优P2P服务器，然后连接
     */
    void startBestServer() {
        clientCore = ClientCore.getInstance();
        int language = 1;
        clientCore.setupHost(context, Constants.server, 0, Utility.getImsi(context),
                language, Constants.CustomName, Utility.getVersionName(context),
                "");
//        clientCore.getCurrentBestServer(this, handler);
    }

    /**
     * 执行云台命令
     *
     * @param command 云台命令
     */
    private void ExcuteCommand(int command) {
        if (command == MD_DOWN || command == MD_UP || command == MD_LEFT
                || command == MD_RIGHT) {
            int length = 4;
            System.out.println("发送云台命令：" + command + ",云台步长：" + length);
//            pc.SetPtz(command, length);
            setDevInfo2(command + "");
//                if (pc.GetPlayerState() == SDKError.Statue_PLAYING)
        } else {
//                if (pc.GetPlayerState() == SDKError.Statue_PLAYING)
            setDevInfo2(command + "");
//            pc.SetPtz(command, 0);
            System.out.println("发送云台命令：" + command + ",云台步长：" + 0);
        }


    }

    private List<Equipment> g_list;
    private int num_post = 1;

    private void dataListview() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                g_list = new ArrayList<Equipment>();

                AddSQLiteHelper dbHelper = new AddSQLiteHelper(context, "add.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String s = "select * from AddEquipment ";
                Cursor cursor = db.rawQuery(s, null);
                int num = 1;
                while (cursor.moveToNext()) {

                    String uid = cursor.getString(1);//获取第2列的值
                    String sid = cursor.getString(2);//获取第3列的值
                    String index = cursor.getString(3);//获取第4列的值
                    Equipment g = new Equipment();
                    g.setUid(uid);
                    g.setSid(sid);
//                    g.setIndex(index);
                    g.setIndex(num + "");
                    g_list.add(g);
                    num++;
                }
                ha.sendEmptyMessage(999);
            }
        }).start();

    }

    public void dataListview2() {  //
        new Thread(new Runnable() {
            @Override
            public void run() {
                g_list = new ArrayList<Equipment>();

                AddSQLiteHelper dbHelper = new AddSQLiteHelper(context, "add.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String s = "select * from AddEquipment ";
                Cursor cursor = db.rawQuery(s, null);
                int num = 1;
                while (cursor.moveToNext()) {

                    String uid = cursor.getString(1);//获取第2列的值
                    String sid = cursor.getString(2);//获取第3列的值
                    String index = cursor.getString(3);//获取第4列的值
                    Equipment g = new Equipment();
                    g.setUid(uid);
                    g.setSid(sid);
//                    g.setIndex(index);
                    g.setIndex(num + "");
                    g_list.add(g);
                    num++;
                }
                ha.sendEmptyMessage(1999);
            }
        }).start();

    }


    private myListViewAdapter adapter;

    public void initListview() {
        adapter = new myListViewAdapter(context, g_list);
        dataListview();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                num_post = arg2 + 1;
                Uid = g_list.get(arg2).getSid();
                if(huan_zt){
                    video.setUid2(Uid);
                    video2.setUid(Uid);
                }else{
                    video.setUid(Uid);
                    video2.setUid2(Uid);
                }
                adapter.notifyDataSetChanged();
                getDate((String) SharedPreferencesUtils.getParam(context, "UID" + g_list.get(num_post - 1).getUid(), ""));
                setDevInfo3();
//                mEZUIPlayer.setUrl(playUrl);
            }
        });
    }


    class myListViewAdapter extends BaseAdapter {

        LayoutInflater inflater = null;
        private Context context;
        private List<Equipment> list = null;

        public myListViewAdapter(Context context, List<Equipment> list) {
            this.context = context;
            this.list = list;
            inflater = ((Activity) (context)).getLayoutInflater();
        }


        public List<Equipment> getList() {
            return list;
        }


        public void setList(List<Equipment> list) {
            this.list = list;
        }


        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        public Equipment getItem(int arg0) {
            if (list != null) {
                return list.get(arg0);
            }
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view = convertView;
            Equipment camera = getItem(position);
            ListViewTool too;
            if (convertView == null) {
                view = inflater.inflate(R.layout.item_layout1_tv, parent, false);
                too = new ListViewTool();
//
                too.name = (TextView) view.findViewById(R.id.item_layout_tv);
//                w=too.name.getWidth();


                view.setTag(too);
            } else {
                too = (ListViewTool) view.getTag();
            }
            if (camera == null) return null;
            too.name.setText(camera.getIndex());
            if (num_post == Integer.valueOf(camera.getIndex())) {
                too.name.setAlpha(1);
            } else {
                too.name.setAlpha(0.5f);
            }


            return view;
        }

        class ListViewTool {

            public TextView name;
        }
    }


    int w = 160;

    class onTo implements View.OnTouchListener {  //20 停止 21上 22下

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int eventaction = event.getAction();
            switch (v.getId()) {
                case R.id.layout1_move_down:
                    switch (eventaction) {
                        /**当初始进来的时候 ，向下移动的状态*/
                        case MotionEvent.ACTION_DOWN:
                            ExcuteCommand(22);
                            break;

                        case MotionEvent.ACTION_UP:
                            ExcuteCommand(20);
                            break;
                    }
                    break;
                case R.id.layout1_move_up:
                    switch (eventaction) {
                        /**当初始进来的时候 ，向下移动的状态*/
                        case MotionEvent.ACTION_DOWN:
                            ExcuteCommand(21);
                            break;

                        case MotionEvent.ACTION_UP:
                            ExcuteCommand(20);
                            break;
                    }
                    break;
            }


            return false;
        }
    }


}
