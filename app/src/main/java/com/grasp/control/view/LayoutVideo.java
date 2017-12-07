package com.grasp.control.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIKit;
import com.ezvizuikit.open.EZUIPlayer;
import com.grasp.control.MainActivity;
import com.grasp.control.R;
import com.grasp.control.Umeye_sdk.ShowProgress;
import com.grasp.control.dialog.SpotsDialog;
import com.grasp.control.sqlite.AddSQLiteHelper;
import com.grasp.control.sqlite.Equipment;
import com.grasp.control.sqlite.MySQLiteHelper;
import com.grasp.control.tool.MyApplication;
import com.grasp.control.tool.SharedPreferencesUtils;
import com.grasp.control.tool.Tool;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhujingju on 2017/7/31.
 */


public class LayoutVideo extends Fragment implements View.OnTouchListener, GestureDetector.OnGestureListener
//        , SurfaceHolder.Callback , Handler.Callback
{


    @BindView(R.id.video_player_Rel)
    RelativeLayout rel;
    @BindView(R.id.video_player_ui)
    EZUIPlayer mEZUIPlayer;
    @BindView(R.id.video_layout_bf)
    ImageView videoLayoutBf;
    Unbinder unbinder;
    @BindView(R.id.video_layout_add)
    ImageView videoLayoutAdd;
    @BindView(R.id.layout2_up)
    public ImageView layout2Up;
    @BindView(R.id.layout2_down)
    ImageView layout2Down;
    @BindView(R.id.layout2_left)
    ImageView layout2Left;
    @BindView(R.id.layout2_right)
    ImageView layout2Right;
    @BindView(R.id.layout2_rel_kz)
    RelativeLayout layout2RelKz;
//    @BindView(R.id.realplay_sv)
//    SurfaceView realplaySv;
    private Context context;
    private String Uid = "";
    private boolean zt;

//    EZPlayer mEZPlayer;

    private static final String TAG = "qqq";
    /**
     * onresume时是否恢复播放
     */
    private boolean isResumePlay = false;

//    private LayoutMain1.MyOrientationDetector mOrientationDetector;

    /**
     * 开发者申请的Appkey
     */
    private String appkey = "272a51a679e34ffba3542ed5b8c5c2de";
    private String appSecret = "9780f03a715ff013f336109683e369ce";
    /**
     * 授权accesstoken
     */
    private String accesstoken = "";

    private String getPlayUrl(int ms) { //1高清 2流畅 3回放
        if(ms==1){
            return "ezopen://open.ys7.com/" + Uid + "/1.hd.live";
        }else if(ms==2){
            return "ezopen://open.ys7.com/" + Uid + "/1.live";
        }else{
            return "ezopen://open.ys7.com/" + Uid + "/1.rec";
        }

    }

    public LayoutVideo() {
        super();
        Log.d("qqq", "LayoutVideo");
    }

    private SurfaceHolder mRealPlaySh = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_video, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
//        PicOnLongClick pic = new PicOnLongClick();
//        mEZUIPlayer.setOnTouchListener(pic);


        //设置Touch监听
        mEZUIPlayer.setOnTouchListener(this);
        //允许长按
        mEZUIPlayer.setLongClickable(true);

        mEZUIPlayer.setLayoutViewListener(LayoutMain2.layoutViewListener);

        mEZUIPlayer.setEZUIPlayerJt(new EZUIPlayer.EZUIPlayerJt() {
            @Override
            public void onJt(String s) {
                openQualityPopupWindow(s);
            }
        });

        startPushService();
        if( BF_zt){
            videoLayoutAdd.setVisibility(View.GONE);
        }
        ha.sendEmptyMessage(120);

//        mRealPlaySh = realplaySv.getHolder();
//        mRealPlaySh.addCallback(this);

//        mEZPlayer.setHandler(mHandler);


//        if (!Uid.equals("")) {
//            Log.d("qqq",Uid+"  ssssss");
////            mEZPlayer = MyApplication.getOpenSDK().createPlayer(Uid, 1);
//            mEZPlayer.setSurfaceHold(mRealPlaySh);
//            mEZPlayer.startRealPlay();
//        }
//        mEZPlayer.startRealPlay();

//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                if (realplaySv != null) {
//                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(realplaySv.getWindowToken(), 0);
//                }
//            }
//        }, 200);

        mEZUIPlayer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!BF_zt){
                    del();
                }

                return false;
            }
        });

        return view;
    }

    private boolean BF_zt=false;
    public void setBF(){
        BF_zt=true;
    }

    public void setUid(String sUid) {
        Uid = sUid;
//        if (!Uid.equals("")) {
//            Log.d("qqq",Uid+"  111111");
//            mEZPlayer = MyApplication.getOpenSDK().createPlayer(Uid, 1);
//        }


    }

    public void setUid2(String sUid) {
        Uid = sUid;
//        if (!Uid.equals("")) {
//            Log.d("qqq",Uid+"  111111");
//            mEZPlayer = MyApplication.getOpenSDK().createPlayer(Uid, 1);
//        }
        if(Uid.equals("")){
            if (mEZUIPlayer != null) {
                mEZUIPlayer.stopPlay();
                mEZUIPlayer.resumePlay();
                mEZUIPlayer.setUrl("");
                videoLayoutAdd.setVisibility(View.VISIBLE);
                if( BF_zt){
                    videoLayoutAdd.setVisibility(View.GONE);
                }

            }

            zt=false;
            return;
        }else{
            preparePlay();
        }

    }
    private boolean one_zt = true;

    @OnClick({R.id.video_layout_bf, R.id.video_layout_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.video_layout_bf:
                videoLayoutBf.setVisibility(View.GONE);
                if (one_zt) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            loginByPost(true);
                        }
                    }).start();
                } else {
                    if (accesstoken.equals("")) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                loginByPost(false);
                            }
                        }).start();
                    } else {
//                    preparePlay();
                        mEZUIPlayer.startPlay();
                    }
                }
                break;
            case R.id.video_layout_add:
                openListview();
                break;
        }
    }

//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        if (mEZPlayer != null) {
//            mEZPlayer.setSurfaceHold(holder);
//        }
//        mRealPlaySh = holder;
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        if (mEZPlayer != null) {
//            mEZPlayer.setSurfaceHold(null);
//        }
//        mRealPlaySh = null;
//    }



    class MyCallBack implements EZUIPlayer.EZUIPlayerCallBack {

        @Override
        public void onPlaySuccess() {
            Log.d(TAG, "onPlaySuccess");
//            if(layout1Bf!=null){
//                layout1Bf.setVisibility(View.GONE);
//                layout1Bf2.setVisibility(View.GONE);
//            }

            // TODO: 2017/2/7 播放成功处理
//        mBtnPlay.setText("暂停");
        }

        @Override
        public void onPlayFail(EZUIError error) {
            Log.d(TAG, "onPlayFail");
//            if(layout1Bf!=null){
//                if(huan_zt){
//                    layout1Bf.setVisibility(View.GONE);
//                    layout1Bf2.setVisibility(View.VISIBLE);
//                }else{
//                    layout1Bf.setVisibility(View.VISIBLE);
//                    layout1Bf2.setVisibility(View.GONE);
//                }
//            }


            // TODO: 2017/2/21 播放失败处理
            if (error.getErrorString().equals(EZUIError.UE_ERROR_INNER_VERIFYCODE_ERROR)) {

            } else if (error.getErrorString().equalsIgnoreCase(EZUIError.UE_ERROR_NOT_FOUND_RECORD_FILES)) {
                // TODO: 2017/5/12
                //未发现录像文件
//            Toast.makeText(this,getString(R.string.string_not_found_recordfile),Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onVideoSizeChange(int width, int height) {
            // TODO: 2017/2/16 播放视频分辨率回调
            Log.d(TAG, "onVideoSizeChange  width = " + width + "   height = " + height);
        }

        @Override
        public void onPrepared() {
            Log.d(TAG, "onPrepared");
            //播放

            mEZUIPlayer.startPlay();

        }

        @Override
        public void onPlayTime(Calendar calendar) {
//        Log.d(TAG, "onPlayTime");
            if (calendar != null) {
                // TODO: 2017/2/16 当前播放时间
//            Log.d(TAG, "onPlayTime calendar = " + calendar.getTime().toString());
            }
        }

        @Override
        public void onPlayFinish() {
            // TODO: 2017/2/16 播放结束
            Log.d(TAG, "onPlayFinish");
        }
    }


    @Override
    public void onDestroyView() {
        ha.removeCallbacks(null);
        super.onDestroyView();
        del_zt=true;
        unbinder.unbind();
        stopVoiceTalk();
        one_zt = true;

        if (mEZUIPlayer != null) {
            mEZUIPlayer.releasePlayer();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        if (mOrientationDetector == null) {
//            return;
//        }
//        mOrientationDetector.enable();
        Log.d(TAG, "onResume");
        //界面stop时，如果在播放，那isResumePlay标志位置为true，resume时恢复播放
        if (isResumePlay) {
            isResumePlay = false;
            if (mEZUIPlayer != null) {
                videoLayoutAdd.setVisibility(View.GONE);
                mEZUIPlayer.startPlay();
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (mOrientationDetector == null) {
//            return;
//        }
//        mOrientationDetector.disable();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mEZUIPlayer == null) {
            videoLayoutAdd.setVisibility(View.GONE);
            return;
        }
        videoLayoutAdd.setVisibility(View.VISIBLE);
        if( BF_zt){
            videoLayoutAdd.setVisibility(View.GONE);
        }
        Log.d(TAG, "onStop + " + mEZUIPlayer.getStatus());
        //界面stop时，如果在播放，那isResumePlay标志位置为true，以便resume时恢复播放
        if (mEZUIPlayer.getStatus() != EZUIPlayer.STATUS_STOP) {
            isResumePlay = true;
        }
        //停止播放
        mEZUIPlayer.stopPlay();
    }

    Handler ha = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            MySQLiteHelper dbHelper = new MySQLiteHelper(context, "demo.db", null, 1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            super.handleMessage(msg);
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            if(mEZUIPlayer==null){
                return;
            }
            switch (msg.what) {
                case 100:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        JSONObject json = jsonObject.getJSONObject("data");
                        accesstoken = json.getString("accessToken");
                        //设置授权accesstoken
                        preparePlay();
                        one_zt = false;

                        //设置加载需要显示的view


//                        mAccessTokenEditText.setText(accessToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 101:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        JSONObject json = jsonObject.getJSONObject("data");
                        accesstoken = json.getString("accessToken");
                        //设置授权accesstoken
                        mEZUIPlayer.startPlay();

                        //设置加载需要显示的view


//                        mAccessTokenEditText.setText(accessToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 120:

                    mEZUIPlayer.setLoadingView(initProgressBar());
                    setSurfaceSize();

                    if (accesstoken.equals("")) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                loginByPost(true);
                            }
                        }).start();
                    } else {
                        preparePlay();
                    }


                    break;
                case 200:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        JSONObject json = jsonObject.getJSONObject("data");
                        String url = json.getString("picUrl");
                        //设置授权accesstoken
                        Log.d("qqq", "url=" + url);

                        //设置加载需要显示的view


//                        mAccessTokenEditText.setText(accessToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case 400:
                    if (videoLayoutBf != null) {
                        if (!Uid.equals("")) {
                            videoLayoutBf.setVisibility(View.VISIBLE);
                        } else {
                            videoLayoutAdd.setVisibility(View.VISIBLE);
                            if( BF_zt){
                                videoLayoutAdd.setVisibility(View.GONE);
                            }
                        }

                    }
                    break;
                case 600:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        JSONObject json = jsonObject.getJSONObject("data");
                        int index = json.getInt("index");
                        //设置授权accesstoken

                        Toast.makeText(context,R.string.AddPresetpoints_good,Toast.LENGTH_SHORT).show();

                        String s="insert into TabPlay(title, mindex,uid,path) values('"+name+"','"+index+"','"+Uid+"','"+path+"')";
                        Log.d("qqq","s="+s);
                        db.execSQL(s);
                        LayoutMain2.addListener.addListener();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case 601:
                    Toast.makeText(context,R.string.AddPresetpoints_err,Toast.LENGTH_SHORT).show();
                    break;
                case 700:
                    Toast.makeText(context,R.string.DelPresetpoints_good,Toast.LENGTH_SHORT).show();
                    String s="delete from  TabPlay  where  mindex='"+msg.obj.toString()+"' and uid='"+Uid+"'";
                    Log.d("qqq","s="+s);
                    db.execSQL(s);
                    LayoutMain2.addListener.addListener();
                    break;
                case 701:
                    Toast.makeText(context,R.string.DelPresetpoints_err,Toast.LENGTH_SHORT).show();
                    break;
                case 800:
                    Toast.makeText(context,R.string.transferPresetpoints_good,Toast.LENGTH_SHORT).show();
                    break;
                case 801:
                    Toast.makeText(context,R.string.transferPresetpoints_err,Toast.LENGTH_SHORT).show();
                    break;
                case 900:
                    Toast.makeText(context,"初始化结束",Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtils.setParam(context, "zjj"+Uid,Uid);
                    break;
                case 901:
                    Toast.makeText(context,"初始化失败请重试",Toast.LENGTH_SHORT).show();
                    break;
                case 999:
                    if(pop_adapter!=null){
                        pop_adapter.setList(toulist);
                        pop_adapter.notifyDataSetChanged();
                    }
                    break;
                case 2333:
                    mEZUIPlayer.stopPlay();
                    ha.sendEmptyMessageDelayed(2334,500);
                    break;
                case 2334:
                    mEZUIPlayer.startPlay();
                    break;
                case 3000:
                    if(!MainActivity.Notification_zt){
                        return;
                    }
                    if(mEZUIPlayer!=null&&!Uid.equals("")){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Calendar ca1=Calendar.getInstance();
                                getAlarmList(caTime.getTime().getTime()+"",ca1.getTime().getTime()+"");
                                caTime=ca1;
                            }
                        }).start();
                    }else{
                        if(!del_zt){
                            ha.sendEmptyMessageDelayed(3000,30*1000);
                        }
                    }




                    break;
                case 3001:
                    if(!MainActivity.Notification_zt){
                        return;
                    }
                    Log.d("EZAlarmInfo","---++++  sj"+msg.obj.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        JSONArray json = jsonObject.getJSONArray("data");
                        if(json.length()>0){
                            JSONObject jo=json.getJSONObject(0);
                            String alarmId=jo.getString("alarmId");
                            String alarmName=jo.getString("alarmName");
                            String alarmTime=jo.getString("alarmTime");
                            String alarmPicUrl=jo.getString("alarmPicUrl");
                            n_time=alarmTime;
                            Log.d("EZAlarmInfo","  sj"+alarmTime);
                            setNotification(alarmPicUrl);


                        }else{

                        }
                        //设置授权accesstoken


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(!del_zt){
                        ha.sendEmptyMessageDelayed(3000,30*1000);
                    }

                    break;

                case 3002:
                    if(!MainActivity.Notification_zt){
                        return;
                    }
                    if(!del_zt){
                        ha.sendEmptyMessageDelayed(3000,30*1000);
                    }
                    break;



            }
        }
    };



    public void setNotification(String urlStr){
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(6000);//设置超时
                    conn.setDoInput(true);
                    conn.setUseCaches(false);//不缓存
                    conn.connect();
                    int code = conn.getResponseCode();
                    Bitmap bitmap = null;
                    if(code==200) {
                        InputStream is = conn.getInputStream();//获得图片的数据流
                        bitmap = BitmapFactory.decodeStream(is);
                    }
                    return bitmap;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                if (result != null) {
                    showNotification(result);
                }else{
                }
            }
        }.execute(urlStr);
    }

    private void showNotification(Bitmap bitmap){
        if(del_zt){
            return;
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti = new Notification();
        noti.defaults=Notification.DEFAULT_ALL;
        noti.flags  |= Notification.FLAG_AUTO_CANCEL;

        noti.icon = R.drawable.icon_m;
        // 1、创建一个自定义的消息布局 notification.xml
        // 2、在程序代码中使用RemoteViews的方法来定义image和text。然后把RemoteViews对象传到contentView字段
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.cus_noti);
        remoteView.setImageViewResource(R.id.image,
                R.drawable.icon_m);
        remoteView.setImageViewBitmap(R.id.image, bitmap);
        Calendar ca=Calendar.getInstance();
        ca.setTimeInMillis(Long.valueOf(n_time));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String t=format.format(ca.getTime());
        remoteView.setTextViewText(R.id.text,
                "报警时间："+t);

        noti.contentView = remoteView;
        PendingIntent contentIntent = PendingIntent.getActivity
                (context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        noti.contentIntent = contentIntent;
        notificationManager.notify(1, noti);
    }


    private String n_time="";
    private boolean del_zt=false;
    /**
     * 创建加载view
     *
     * @return
     */
    private ProgressBar initProgressBar() {
        if(rel==null){
            return null ;
        }
        ProgressBar mProgressBar = new ProgressBar(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
        mProgressBar.setLayoutParams(lp);
        return mProgressBar;
    }

    /**
     * 准备播放资源参数
     */
    private void preparePlay() {
        if (Uid.equals("")) {
            zt = false;
            videoLayoutAdd.setVisibility(View.VISIBLE);
            if( BF_zt){
                videoLayoutAdd.setVisibility(View.GONE);
            }
            return;
        }
        if (videoLayoutAdd != null) {
            videoLayoutAdd.setVisibility(View.GONE);
        }

        zt = true;
        //设置debug模式，输出log信息
        EZUIKit.setDebug(true);
        //appkey初始化
        EZUIKit.initWithAppKey(MyApplication.application, appkey);
        //设置授权accesstoken
        EZUIKit.setAccessToken(accesstoken);
        //设置播放资源参数
        MyCallBack call = new MyCallBack();
        if (mEZUIPlayer != null) {
            mEZUIPlayer.setCallBack(call);
            if (getPlayUrl(moshi) != null) {
                Log.e("qqq", getPlayUrl(moshi));
                mEZUIPlayer.setUrl(getPlayUrl(moshi));
            }

        }

    }

    public int moshi=1;

    public void setMoshi(int moshi){
        this.moshi=moshi;
        mEZUIPlayer.setUrl(getPlayUrl(moshi));
    }

    private void setSurfaceSize() {
        if(mEZUIPlayer==null){
            return;
        }
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        mEZUIPlayer.setSurfaceSize(dm.widthPixels+100 , dm.heightPixels);
    }

    /**
     * post的方式请求
     *
     * @return 返回null 登录异常
     */
    public String loginByPost(boolean zt) {//第一次为true
        Log.d(TAG, "loginByPost");
        if (!Tool.isLianWang(context)) {
//            Toast.makeText(context, R.string.lianwang, Toast.LENGTH_SHORT).show();
            ha.sendEmptyMessage(400);
            return null;
        }


        String path = "https://open.ys7.com/api/lapp/token/get";
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            //数据准备
            String data = "appKey=" + appkey + "&appSecret=" + appSecret;
            Log.d("qqq", "sssss " + data);
            //至少要设置的两个请求头
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", data.length() + "");

            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());

            //获得结果码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                //请求成功
                InputStream is = connection.getInputStream();
//                return IOSUtil.inputStream2String(is);
                Message m = new Message();
                if (zt) {//第一次
                    m.what = 100;
                } else {
                    m.what = 101;
                }
                String s = convertStreamToString(is);
                m.obj = s;
                Log.d("qqq", "wwwwwwwwwwww " + s);
                if (s.equals("")) {
                    ha.sendEmptyMessage(400);
                    return null;
                }
                ha.sendMessage(m);
//                Log.d("qqq", m.obj.toString());
                return null;
            } else {
                //请求失败
                Log.d("qqq", "xxxxx----------");
//                Toast.makeText(context, "请求失败错误码：" + responseCode, Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("qqq", "xxxxx----------");
        return null;
    }

    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();


        String line = null;

        try {

            while ((line = reader.readLine()) != null) {

                sb.append(line + "/n");

            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                is.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }


        return sb.toString();

    }

    public void setdime() {
        if (mEZUIPlayer != null) {
//            mEZUIPlayer.stopPlay();
            mEZUIPlayer.releasePlayer();
//            mEZUIPlayer.resumePlay();
        }
    }

    public void setSt() {
        if (mEZUIPlayer != null) {
            preparePlay();
        }
    }

    public void setStop() {
        if (!zt) {
            return;
        }
        if (mEZUIPlayer != null) {
            mEZUIPlayer.stopPlay();
            videoLayoutBf.setVisibility(View.GONE);
            videoLayoutAdd.setVisibility(View.GONE);
        }
    }

    public void setStart() {
        if (!zt) {
            return;
        }
        if (mEZUIPlayer != null) {
            mEZUIPlayer.startPlay();
            videoLayoutBf.setVisibility(View.GONE);
            videoLayoutAdd.setVisibility(View.GONE);
        }
    }

    public void Screenshot() {
        if (mEZUIPlayer != null) {
//            mEZUIPlayer.
            if (mEZUIPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
//                sreenShot();
//                jietu();
//                mEZUIPlayer.setJt();
//                 GlobalScreenShot screenshot = new GlobalScreenShot(context);
//                screenshot.takeScreenshot(((Activity)context).getWindow().getDecorView(), new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, true, true);
            } else {
                Toast.makeText(context, R.string.jt_err1, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(context, R.string.jt_err1, Toast.LENGTH_SHORT).show();
        }

    }

    private float mPosX, mPosY, mCurPosX, mCurPosY;
    private static final int FLING_MIN_DISTANCE = 20;// 移动最小距离
    private static final int FLING_MIN_VELOCITY = 200;// 移动最大速度
    //构建手势探测器
    GestureDetector mygesture = new GestureDetector(this);
    private boolean fd_zt = false;
    float lasth = -1;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub

        if (Uid.equals("")) {
            return mygesture.onTouchEvent(event);
        }
        if (!LayoutMain2.zt3) {
        } else {
            switch (event.getAction()) {

//
                case MotionEvent.ACTION_UP:
                    layout2Up.setVisibility(View.GONE);
                    layout2Down.setVisibility(View.GONE);
                    layout2Left.setVisibility(View.GONE);
                    layout2Right.setVisibility(View.GONE);
                    stopPost();
                    Log.d("qqq", mPosX + " ACTION_UP " + mPosY);
                    break;
            }
        }


//        if(!LayoutMain2.zt5){ //缩放
//        }else{
//            switch (event.getAction()) { //触摸类型
//                case MotionEvent.ACTION_DOWN:  //按下
//                    break;
//                case MotionEvent.ACTION_MOVE:  //触摸滑动
//                    Log.d("qqq", "ACTION_MOVE"+"x:"+event.getX()+"y:"+event.getY());
////            	   Log.d("qqq", "触摸点数："+event.getPointerCount()+
////            			   "\n第一个触摸点x"+event.getX(0)+"第一个触摸点y"+event.getY(0)+
////            			   "\n第二个触摸点x"+event.getX(1)+"第二个触摸点y"+event.getY(1) );//只有两个或两个触摸点以上才不报错
//            	   if(event.getPointerCount()>=2){  //缩放
//            		   float x= event.getX(0)-event.getX(1);
//            		   float y=event.getY(0)-event.getY(1);
//            		   float h=(float) Math.sqrt(x*x+y*y);  //获得距离
//            		   if(lasth<0){
//            			   lasth=h;
//            		   }else{
//            			   if(lasth-h>10){//缩
//                               RelativeLayout.LayoutParams f=(RelativeLayout.LayoutParams) mEZUIPlayer.getLayoutParams();
//            				   f.width=(int) (mEZUIPlayer.getWidth()*0.9f);
//            				   f.height=(int) (mEZUIPlayer.getHeight()*0.9f);
//                               mEZUIPlayer.setLayoutParams(f);
//            				   lasth=h;
//            				   Log.d("qqq", "缩小");
//            			   }else if(lasth-h<-10){//放大
//
//                               RelativeLayout.LayoutParams f=(RelativeLayout.LayoutParams) mEZUIPlayer.getLayoutParams();
//                               Log.d("qqq", "放大"+f.width+"  "+f.height);
//            				   f.width=(int) (mEZUIPlayer.getWidth()*1.1f);
//            				   f.height=(int) (mEZUIPlayer.getHeight()*1.1f);
//                               mEZUIPlayer.setLayoutParams(f);
//            				   lasth=h;
//            				   Log.d("qqq", "放大"+f.width+"  "+f.height);
//            			   }
//            		   }
//            	   }

//                    break;
//                case MotionEvent.ACTION_UP:  //松开
//                    break;
//
//            }
//        }

        return mygesture.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        Log.d("qqq", "onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub
        Log.d("qqq", "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        Log.d("qqq", "onSingleTapUp");

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub

        if (Uid.equals("")) {
            return false;
        }
        if (!LayoutMain2.zt3) {
        } else {
            Log.d("qqq", e1.getX() + " onScroll " + e2.getX() + "  " + Math.abs(distanceX));
            float minMove = 50;         //最小滑动距离
            float minMove2 = 50;         //最小滑动距离
            float minVelocity = 0;      //最小滑动速度
            float beginX = e1.getX();
            float endX = e2.getX();
            float beginY = e1.getY();
            float endY = e2.getY();

            if (beginX - endX > minMove2 && Math.abs(distanceX) > minVelocity) {   //左滑
//            Toast.makeText(this,velocityX+"左滑",Toast.LENGTH_SHORT).show();
                Log.d("qqq", "左");
                loginByPost(2);
                layout2Left.setVisibility(View.VISIBLE);
            } else if (endX - beginX > minMove2 && Math.abs(distanceX) > minVelocity) {   //右滑
//            Toast.makeText(this,velocityX+"右滑",Toast.LENGTH_SHORT).show();
                Log.d("qqq", "右");
                loginByPost(3);
                layout2Right.setVisibility(View.VISIBLE);
            } else if (beginY - endY > minMove && Math.abs(distanceY) > minVelocity) {   //上滑
//            Toast.makeText(this,velocityX+"上滑",Toast.LENGTH_SHORT).show();
                loginByPost(0);
                layout2Up.setVisibility(View.VISIBLE);
            } else if (endY - beginY > minMove && Math.abs(distanceY) > minVelocity) {   //下滑
//            Toast.makeText(this,velocityX+"下滑",Toast.LENGTH_SHORT).show();
                loginByPost(1);
                layout2Down.setVisibility(View.VISIBLE);
            }
        }


        if (!LayoutMain2.zt5) { //缩放
        } else {

        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub
        Log.d("qqq", "onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        Log.d("qqq", "onFling");

        return false;
    }


    int dong_post = 0;

    public void loginByPost(final int dong) {  //云台控制  0-上，1-下，2-左，3-右，4-左上，5-左下，6-右上，7-右下，8-放大，9-缩小，10-近焦距，11-远焦距
        Log.d("qqq", "loginByPost----------" + dong);
        if (dong == dong_post) {

            return;
        }


        Log.d("qqq", "loginByPost----------  1111111111");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = "https://open.ys7.com/api/lapp/device/ptz/start";
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("POST");
                    dong_post = dong;
                    //数据准备

                    String data = "accessToken=" + accesstoken + "&deviceSerial=" + Uid + "&channelNo=1&direction=" + dong + "&speed=1";
                    //至少要设置的两个请求头
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length", data.length() + "");

                    //post的方式提交实际上是留的方式提交给服务器
                    connection.setDoOutput(true);
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(data.getBytes());

                    //获得结果码
                    int responseCode = connection.getResponseCode();
                    Log.d("qqq", "loginByPost----------  1111111111" + responseCode);
                    if (responseCode == 200) {
                        //请求成功
                        InputStream is = connection.getInputStream();

                    } else {
                        //请求失败
                        Log.d("qqq", "xxxxx----------");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    public void stopPost() {  //停止云台
        Log.d("qqq", "stopPost----------");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = "https://open.ys7.com/api/lapp/device/ptz/stop";
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("POST");
                    dong_post = -1;
                    //数据准备

                    String data = "accessToken=" + accesstoken + "&deviceSerial=" + Uid + "&channelNo=1";
                    //至少要设置的两个请求头
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length", data.length() + "");

                    //post的方式提交实际上是留的方式提交给服务器
                    connection.setDoOutput(true);
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(data.getBytes());

                    //获得结果码
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        //请求成功
                        InputStream is = connection.getInputStream();
                    } else {
                        //请求失败
                        Log.d("qqq", "xxxxx----------");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public boolean getRealPicture() {
        if( mEZUIPlayer!=null){
            if (mEZUIPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
                mEZUIPlayer.getRealPicture();
                return true;
            } else {
                Toast.makeText(context, R.string.jt_err22, Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return false;
    }

    public void getStopPicture() {

        if (mEZUIPlayer == null) {
            return;
        }
        mEZUIPlayer.getStopPicture();
    }


//    /**
//     * 开始录像成功
//     *
//     * @param recordFilePath
//     * @see
//     * @since V2.0
//     */
//    private void handleRecordSuccess(String recordFilePath) {
//
//    }
//
//    private void handleRecordFail() {
//        Toast.makeText(context, "录像失败", Toast.LENGTH_SHORT).show();
//        layoutViewListener.layoutViewListener();
//    }
//
//
//    private LayoutViewListener layoutViewListener;
//
//    public void setLayoutViewListener(LayoutViewListener layoutViewListener) {
//        this.layoutViewListener = layoutViewListener;
//    }
//
//    public interface LayoutViewListener {
//
//        void layoutViewListener();
//
//    }

    public void getJt(){  //截图
        if(mEZUIPlayer!=null){
            if (mEZUIPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
                mEZUIPlayer.getJt();
            } else {
                Toast.makeText(context, R.string.jt_err1, Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void getJt2(){  // 预设点截图
        if(mEZUIPlayer!=null){
            if (mEZUIPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
                mEZUIPlayer.getJt2();
            } else {
                Toast.makeText(context, R.string.jt_err66, Toast.LENGTH_SHORT).show();
            }

        }

    }

    public boolean startVoiceTalk() {  //对讲
        if(mEZUIPlayer!=null){
            if (mEZUIPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
                mEZUIPlayer.startVoiceTalk();
                return true;
            } else {
                Toast.makeText(context, R.string.jt_err33, Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return false;
    }

    public void stopVoiceTalk() {  //关闭对讲
        if(mEZUIPlayer!=null){
            if (mEZUIPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
                mEZUIPlayer.stopVoiceTalk();
            } else {
            }

        }
    }


    public void openSound(){
        if(mEZUIPlayer!=null){
            if (mEZUIPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
                mEZUIPlayer.openSound();
            } else {
            }

        }
    }

    public void closeSound(){
        if(mEZUIPlayer!=null){
            if (mEZUIPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
                mEZUIPlayer.closeSound();
            } else {
            }

        }
    }
    private SpotsDialog progressDialog;
    private String name="";
    private String path="";


    public void addPresetPoints() {  //添加预置点
        Log.d("qqq", "addPresetPoints----------");
        if(mEZUIPlayer!=null){
            if (mEZUIPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {

                progressDialog = new SpotsDialog(context);
                progressDialog.setCancelable(true);
                progressDialog.show();
                progressDialog.setStrText(getString(R.string.add_ysd));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String path = "https://open.ys7.com/api/lapp/device/preset/add";
                        try {
                            URL url = new URL(path);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setRequestMethod("POST");
                            //数据准备

                            String data = "accessToken=" + accesstoken + "&deviceSerial=" + Uid + "&channelNo=1";
                            //至少要设置的两个请求头
                            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            connection.setRequestProperty("Content-Length", data.length() + "");

                            //post的方式提交实际上是留的方式提交给服务器
                            connection.setDoOutput(true);
                            OutputStream outputStream = connection.getOutputStream();
                            outputStream.write(data.getBytes());

                            //获得结果码
                            int responseCode = connection.getResponseCode();
                            Log.d("qqq", "wwwwwwwwwwww responseCode=" + responseCode);
                            if (responseCode == 200) {
                                //请求成功
                                InputStream is = connection.getInputStream();

                                Message m = new Message();
                                m.what = 600;
                                String s = convertStreamToString(is);
                                m.obj = s;
                                Log.d("qqq", "wwwwwwwwwwww " + s);
                                if (s.equals("")) { //失败
                                    ha.sendEmptyMessage(601);
                                }
                                ha.sendMessage(m);
                            } else {
                                //请求失败
                                Log.d("qqq", "xxxxx----------");
                                ha.sendEmptyMessage(601);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                Toast.makeText(context, R.string.jt_err66, Toast.LENGTH_SHORT).show();
            }
        }




    }

    public void delPresetPoints(final String index) {  //删除预置点
        Log.d("qqq", "delPresetPoints----------");

        if(mEZUIPlayer!=null){
            if (mEZUIPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
                progressDialog = new SpotsDialog(context);
                progressDialog.setCancelable(true);
                progressDialog.show();
                progressDialog.setStrText(getString(R.string.del_ysd));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String path = "https://open.ys7.com/api/lapp/device/preset/clear";
                        try {
                            URL url = new URL(path);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setRequestMethod("POST");
                            //数据准备

                            String data = "accessToken=" + accesstoken + "&deviceSerial=" + Uid + "&channelNo=1&index="+index;
                            //至少要设置的两个请求头
                            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            connection.setRequestProperty("Content-Length", data.length() + "");

                            //post的方式提交实际上是留的方式提交给服务器
                            connection.setDoOutput(true);
                            OutputStream outputStream = connection.getOutputStream();
                            outputStream.write(data.getBytes());

                            //获得结果码
                            int responseCode = connection.getResponseCode();
                            Log.d("qqq", "xxxxx----------"+responseCode);
                            if (responseCode == 200) {
                                //请求成功
                                InputStream is = connection.getInputStream();

                        Message m = new Message();
                        m.what = 700;
                        String s = index;
                        m.obj = s;
                        Log.d("qqq", "wwwwwwwwwwww " + s);
                        if (s.equals("")) { //失败
                            ha.sendEmptyMessage(701);
                        }
                        ha.sendMessage(m);
                            }
//                            else if(){
//                                //请求失败
//
////                        ha.sendEmptyMessage(701);
//                            }
                            else{
                                Log.d("qqq", "xxxxx----------");
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                Toast.makeText(context, R.string.jt_err55, Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void del_listPresetPoints() {  //删除预置点
        if(mEZUIPlayer!=null) {
            if (mEZUIPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
                progressDialog = new SpotsDialog(context);
                progressDialog.setCancelable(false);
                progressDialog.show();
                progressDialog.setStrText("正在初始化...");
                delPresetPoints2();
            }
        }
    }
    private int del_index=1;
    private int del_index_err=0;
    public void delPresetPoints2() {  //删除预置点
        Log.d("qqq", "delPresetPoints----------");

        if(mEZUIPlayer!=null){
            if (mEZUIPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String path = "https://open.ys7.com/api/lapp/device/preset/clear";
                        try {
                            URL url = new URL(path);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setRequestMethod("POST");
                            //数据准备

                            String data = "accessToken=" + accesstoken + "&deviceSerial=" + Uid + "&channelNo=1&index="+del_index;
                            //至少要设置的两个请求头
                            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            connection.setRequestProperty("Content-Length", data.length() + "");

                            //post的方式提交实际上是留的方式提交给服务器
                            connection.setDoOutput(true);
                            OutputStream outputStream = connection.getOutputStream();
                            outputStream.write(data.getBytes());

                            //获得结果码
                            int responseCode = connection.getResponseCode();
                            Log.d("qqq", "xxxxx----------"+responseCode);
                            if (responseCode == 200) {
                                if(del_index<12){
                                    del_index++;
                                    delPresetPoints2();
                                }else{
                                    ha.sendEmptyMessage(900);
                                }

                                //请求成功
//                                InputStream is = connection.getInputStream();
//
//                                Message m = new Message();
//                                m.what = 700;
//                                String s = index;
//                                m.obj = s;
//                                Log.d("qqq", "wwwwwwwwwwww " + s);
//                                if (s.equals("")) { //失败
//                                    ha.sendEmptyMessage(701);
//                                }
//                                ha.sendMessage(m);
                            }
//                            else if(){
//                                //请求失败
//
////                        ha.sendEmptyMessage(701);
//                            }
                            else{
                                if(del_index_err<=10){
                                    del_index_err++;
                                    delPresetPoints2();
                                }else{
                                    ha.sendEmptyMessage(901);
                                }


                                Log.d("qqq", "xxxxx----------");
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                Toast.makeText(context, R.string.jt_err55, Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void transferPresetPoints(final String index) {  //调用预置点
        Log.d("qqq", "transferPresetPoints----------");
        if(mEZUIPlayer!=null){
            if (mEZUIPlayer.getStatus() == EZUIPlayer.STATUS_PLAY) {
                progressDialog = new SpotsDialog(context);
                progressDialog.show();
                progressDialog.setStrText(getString(R.string.dy_ysd));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String path = "https://open.ys7.com/api/lapp/device/preset/move";
                        try {
                            URL url = new URL(path);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setRequestMethod("POST");
                            //数据准备

                            String data = "accessToken=" + accesstoken + "&deviceSerial=" + Uid + "&channelNo=1&index="+index;
                            //至少要设置的两个请求头
                            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            connection.setRequestProperty("Content-Length", data.length() + "");

                            //post的方式提交实际上是留的方式提交给服务器
                            connection.setDoOutput(true);
                            OutputStream outputStream = connection.getOutputStream();
                            outputStream.write(data.getBytes());

                            //获得结果码
                            int responseCode = connection.getResponseCode();
                            if (responseCode == 200) {
                                //请求成功
                                InputStream is = connection.getInputStream();

                                ha.sendEmptyMessage(800);
                            } else {
                                //请求失败
                                Log.d("qqq", "xxxxx----------");
                                ha.sendEmptyMessage(801);
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else {
                Toast.makeText(context, R.string.jt_err44, Toast.LENGTH_SHORT).show();
            }
        }



    }


    private PopupWindow mQualityPopupWindow = null;
     private   EditText edit;
    private void openQualityPopupWindow(String path) {
        this.path=path;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layoutView = (ViewGroup) layoutInflater.inflate(R.layout.dialog_save_ysd, null, true);

        ImageView bj = (ImageView) layoutView.findViewById(R.id.dialog_save_ysd_bj);
        bj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopWindow(mQualityPopupWindow);
            }
        });

        Button qualityHdBtn = (Button) layoutView.findViewById(R.id.dialog_save_ysd_save);
        qualityHdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=edit.getText().toString();
                if(!s.isEmpty()){
                    name=s;
                    addPresetPoints();
                    dismissPopWindow(mQualityPopupWindow);
                }else{
                    Toast.makeText(context,R.string.name_err,Toast.LENGTH_SHORT).show();
                }

            }
        });
        ImageView im = (ImageView) layoutView.findViewById(R.id.dialog_save_ysd_im);
        Log.d("qqq","path="+path);
        ImageLoader.getInstance().displayImage("file://" + path, im, MyApplication.options);
         edit = (EditText) layoutView.findViewById(R.id.dialog_save_ysd_edit);

        mQualityPopupWindow = new PopupWindow(layoutView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        mQualityPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mQualityPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                mQualityPopupWindow = null;
                if (mQualityPopupWindow != null) {
                    dismissPopWindow(mQualityPopupWindow);
                    mQualityPopupWindow = null;
                }
            }
        });
        try {
            mQualityPopupWindow.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.CENTER, 0,0);
        } catch (Exception e) {
            e.printStackTrace();
            if (mQualityPopupWindow != null) {
                dismissPopWindow(mQualityPopupWindow);
                mQualityPopupWindow = null;
            }
        }
    }


    public String getUid(){
        return  Uid;
    }


    private ListView toulistView;
    private List<Equipment> toulist;
    private myPopWimAdapter pop_adapter;

    private void openListview() {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layoutView = (ViewGroup) layoutInflater.inflate(R.layout.dialog_openlistview, null, true);
//        pop_dataListview();
        ImageView bj = (ImageView) layoutView.findViewById(R.id.dialog_openlistview_bj);
        bj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopWindow(mQualityPopupWindow);
            }
        });

        toulistView = (ListView) layoutView.findViewById(R.id.dialog_openlistview_listview);
        toulistView.setCacheColorHint(0);
        pop_adapter=new myPopWimAdapter(context, toulist);
        pop_dataListview();
        toulistView.setAdapter(pop_adapter);
        toulistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                AddSQLiteHelper dbHelper = new AddSQLiteHelper(context, "add.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String s="update  AddEquipment  set mindex='"+0+"' where uid='"+toulist.get(arg2).getUid()+"'";
                db.execSQL(s);
                dismissPopWindow(mQualityPopupWindow);
                LayoutMain2.popwinAddListener.PopwinAddListener(1,0);
            }
        });

        mQualityPopupWindow = new PopupWindow(layoutView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
        mQualityPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mQualityPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                mQualityPopupWindow = null;
                if (mQualityPopupWindow != null) {
                    dismissPopWindow(mQualityPopupWindow);
                    mQualityPopupWindow = null;
                }
            }
        });
        try {
            mQualityPopupWindow.showAtLocation(((Activity)context).getWindow().getDecorView(), Gravity.CENTER, 0,0);
        } catch (Exception e) {
            e.printStackTrace();
            if (mQualityPopupWindow != null) {
                dismissPopWindow(mQualityPopupWindow);
                mQualityPopupWindow = null;
            }
        }
    }
    private ShowProgress pd;
    private void dismissPopWindow(PopupWindow popupWindow) {
        if (popupWindow != null && !((Activity) (context)).isFinishing()) {
            try {
                popupWindow.dismiss();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        if (pd == null) {
            pd = new ShowProgress(context);
            pd.setMessage("");
            pd.setCanceledOnTouchOutside(true);
        }
        if(pd!=null){
            pd.show();
        }
        if(pd!=null) {
            pd.dismiss();
        }
    }

    private void pop_dataListview(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                toulist=new ArrayList<>();

                AddSQLiteHelper dbHelper = new AddSQLiteHelper(context, "add.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String s="select * from AddEquipment ";
                Cursor cursor = db.rawQuery(s, null);
                while (cursor.moveToNext()) {

                    String uid = cursor.getString(1);//获取第2列的值
                    String sid = cursor.getString(2);//获取第3列的值
                    String index = cursor.getString(3);//获取第4列的值
                    Equipment g=new Equipment();
                    g.setUid(uid);
                    g.setSid(sid);
                    g.setIndex(index);
                    Log.e("qqq",uid+"   ");
                    toulist.add(g);

                }
                ha.sendEmptyMessage(999);
            }
        }).start();



    }

    class myPopWimAdapter extends BaseAdapter {

        LayoutInflater inflater = null;
        private Context context;
        private List<Equipment> list = null;

        public myPopWimAdapter(Context context, List<Equipment> list) {
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
        public View getView( int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view = convertView;
            Equipment camera = getItem(position);
            ListViewTool too;
            if (convertView == null) {
                view = inflater.inflate(R.layout.sw_item2, parent, false);
                too = new ListViewTool();

                too.name = (TextView) view.findViewById(R.id.ybd_tv);
                too.last = (LinearLayout) view.findViewById(R.id.sw_item_last);


                view.setTag(too);
            } else {
                too = (ListViewTool) view.getTag();
            }
            if (camera == null) return null;
            too.name.setText(camera.getUid());
            too.last.setBackgroundResource(R.drawable.d1);
            if (list.size() > 1) {
                if (position == list.size() - 1) {
                    too.last.setBackgroundResource(R.drawable.d2);
                } else {

                }
            } else {

                too.last.setBackgroundResource(R.drawable.d2);

            }

//			if(camera.getName().equals(Uid)){
//				too.ss.setState(true);
//			}


            return view;
        }

        class ListViewTool {

            public LinearLayout last;
            public TextView name;
        }
    }


    private void del(){
        if(Uid.equals("")){
            return;
        }
        if(LayoutMain2.zt3){
            return;
        }
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle("删除");
        normalDialog.setMessage("是否删除");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddSQLiteHelper dbHelper = new AddSQLiteHelper(context, "add.db", null, 1);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String s="update  AddEquipment  set mindex='-1' where sid='"+Uid+"'";
                        Log.e("qqq","SQLiteDatabase="+s);
                        db.execSQL(s);
                        LayoutMain2.popwinAddListener.PopwinAddListener(0,0);
                        mEZUIPlayer.stopPlay();
                        mEZUIPlayer.resumePlay();
                        Uid="";
                        setUid2("");
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.setCancelable(false);
        normalDialog.show();
    }

    public void handleSetVedioModeSuccess(){
        ha.sendEmptyMessage(2333);
    }



    Calendar caTime=null;
    private String AlarmId="";
  private void startPushService(){
      if(!MainActivity.Notification_zt){
          return;
      }
      Calendar ca=Calendar.getInstance();
      caTime=ca;
//      Log.d("EZAlarmInfo","---  sj  cat  ca"+caTime.getTime() +"    "+ca.getTime());
      ha.sendEmptyMessageDelayed(3002,0);
  }

    public void getAlarmList(String startTime,String endTime ) {//第一次为true
        Log.d(TAG, "loginByPost");
        if (!Tool.isLianWang(context)) {
//            Toast.makeText(context, R.string.lianwang, Toast.LENGTH_SHORT).show();
            ha.sendEmptyMessageDelayed(3000,30*1000);
        }

        if (accesstoken.equals("")) {
//            Toast.makeText(context, R.string.lianwang, Toast.LENGTH_SHORT).show();
            ha.sendEmptyMessageDelayed(3000,30*1000);
        }
        String path = "https://open.ys7.com/api/lapp/alarm/device/list";
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            //数据准备
            String data="accessToken="+accesstoken+"&deviceSerial="+Uid+"&startTime="+startTime+"&endTime="+endTime+"&alarmType=-1&status=2&pageStart=0&pageSize=5";
            Log.d("qqq", "sssss " + data);
            //至少要设置的两个请求头
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", data.length() + "");

            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());

            //获得结果码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                //请求成功
                InputStream is = connection.getInputStream();
//                return IOSUtil.inputStream2String(is);
                Message m = new Message();
                String s = convertStreamToString(is);
                m.obj = s;
                m.what=3001;
                Log.d("qqq", "wwwwwwwwwwww " + s);
                if (s.equals("")) {
                    m.what=3002;
                }
                ha.sendMessage(m);
//                Log.d("qqq", m.obj.toString());
            } else {
                //请求失败
                Log.d("qqq", "xxxxx----------");
                ha.sendEmptyMessageDelayed(3002,0);
//                Toast.makeText(context, "请求失败错误码：" + responseCode, Toast.LENGTH_SHORT).show();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("qqq", "xxxxx----------");
//        return null;
    }

}
