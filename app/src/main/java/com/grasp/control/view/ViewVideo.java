package com.grasp.control.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.ezuikit.open.WindowSizeChangeNotifier;
import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIKit;
import com.ezvizuikit.open.EZUIPlayer;
import com.grasp.control.MainActivity;
import com.grasp.control.R;
import com.grasp.control.Receiver;
import com.grasp.control.Umeye_sdk.AcSearchDevice;
import com.grasp.control.Umeye_sdk.ShowProgress;
import com.grasp.control.sqlite.AddSQLiteHelper;
import com.grasp.control.sqlite.Equipment;
import com.grasp.control.tool.Tool;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.util.LogUtil;

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
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhujingju on 2017/7/31.
 */

public class ViewVideo extends RelativeLayout implements WindowSizeChangeNotifier.OnWindowSizeChangedListener {


    EZUIPlayer mEZUIPlayer;
    ImageView videoLayoutBf,videoLayoutadd;
    private Context context;
    private String Uid = "";
    private boolean zt;

    private static final String TAG = "qqq";
    /**
     * onresume时是否恢复播放
     */
    private boolean isResumePlay = false;


    /**
     * 开发者申请的Appkey
     */
    private String appkey = "272a51a679e34ffba3542ed5b8c5c2de";
    private String appSecret = "9780f03a715ff013f336109683e369ce";
    /**
     * 授权accesstoken
     */
    private String accesstoken = "";

    private String getPlayUrl() {

        return "ezopen://open.ys7.com/" + Uid + "/1.hd.live";

    }
    private boolean one_zt=true;

    public ViewVideo(Context context) {
        this(context, null, 0);
    }

    public ViewVideo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewVideo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_video, this);
         mEZUIPlayer= (EZUIPlayer) findViewById(R.id.video_player_p);

         videoLayoutBf= (ImageView) findViewById(R.id.video_layout_bf);
        videoLayoutadd= (ImageView) findViewById(R.id.video_layout_add);
        videoLayoutBf.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                videoLayoutBf.setVisibility(View.GONE);
                if(one_zt){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            loginByPost(true);
                        }
                    }).start();
                }else{
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
            }
        });

        videoLayoutadd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                videoLayoutBf.setVisibility(View.GONE);
                Log.d("qqq", "addd");
//                pop_dataListview();
                openListview();
            }
        });

        Log.d("qqq", "video");

        mEZUIPlayer.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                del();
                return false;
            }
        });

        startPushService();
    }

    public void setUid(String sUid) {
        Uid = sUid;
        ha.sendEmptyMessage(120);

    }
    public void setUid2(String sUid) {
        Uid = sUid;

        if(Uid.equals("")){
            if (mEZUIPlayer != null) {
                mEZUIPlayer.stopPlay();
                mEZUIPlayer.resumePlay();
                mEZUIPlayer.setUrl("");


            }
            videoLayoutadd.setVisibility(View.VISIBLE);
            zt=false;
            return;
        }else{
            preparePlay();
        }


    }

    public void setStart() {

        if (accesstoken.equals("")) {

            mEZUIPlayer.setLoadingView(initProgressBar());
            setSurfaceSize();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loginByPost(true);
                }
            }).start();
        } else {
            preparePlay();
        }
    }
    public void setdime(){
        del_zt=true;
        one_zt=true;
        ha.removeCallbacks(null);
        if (mEZUIPlayer != null) {
            mEZUIPlayer.stopPlay();
            mEZUIPlayer.releasePlayer();
//            mEZUIPlayer.resumePlay();
        }
    }

//    public void setSt(){

//        if (mEZUIPlayer != null) {
//            preparePlay();
//        }
//    }
    public void setStop() {
        if(!zt){
            return;
        }
    if (mEZUIPlayer != null) {
        mEZUIPlayer.stopPlay();
        videoLayoutBf.setVisibility(View.GONE);
        videoLayoutadd.setVisibility(View.GONE);
    }
}

    public void setStart_p() {
        if(!zt){
            return;
        }
        if (mEZUIPlayer != null) {
            mEZUIPlayer.startPlay();
            videoLayoutBf.setVisibility(View.GONE);
            videoLayoutadd.setVisibility(View.GONE);
        }
    }


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


//    @Override
//    private void onDestroy() {
//
//        Log.d("qqq", "+++++++++++onDestroyView");
//        super.onDestroy();
//
//        unbinder.unbind();
//        ha.removeMessages(100);
//        ha.removeMessages(101);
//        ha.removeMessages(120);
//        if (mEZUIPlayer != null) {
//            mEZUIPlayer.releasePlayer();
//        }
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
////        if (mOrientationDetector == null) {
////            return;
////        }
////        mOrientationDetector.enable();
//        Log.d(TAG, "onResume");
//        //界面stop时，如果在播放，那isResumePlay标志位置为true，resume时恢复播放
//        if (isResumePlay) {
//            isResumePlay = false;
//            if (mEZUIPlayer != null) {
//                mEZUIPlayer.startPlay();
//            }
//
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (mOrientationDetector == null) {
//            return;
//        }
//        mOrientationDetector.disable();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mEZUIPlayer == null) {
//            return;
//        }
//        Log.d(TAG, "onStop + " + mEZUIPlayer.getStatus());
//        //界面stop时，如果在播放，那isResumePlay标志位置为true，以便resume时恢复播放
//        if (mEZUIPlayer.getStatus() != EZUIPlayer.STATUS_STOP) {
//            isResumePlay = true;
//        }
//        //停止播放
//        mEZUIPlayer.stopPlay();
//    }


    Handler ha = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:

                    try {
                        Log.d("qqq","qqqqqqqqq   "+msg.obj.toString());
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        JSONObject json = jsonObject.getJSONObject("data");
                        accesstoken = json.getString("accessToken");
                        //设置授权accesstoken
                        preparePlay();

                        one_zt=false;
                        //设置加载需要显示的view


//                        mAccessTokenEditText.setText(accessToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 101:
                    try {
                        Log.d("qqq","qqqqqqqqq   "+msg.obj.toString());
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
//                    mOrientationDetector = new MyOrientationDetector(context);
//                    new WindowSizeChangeNotifier((Activity) context, ViewVideo.this);


                    break;
                case 400:
                    if (videoLayoutBf != null) {
                        if(!Uid.equals("")){
                            videoLayoutBf.setVisibility(View.VISIBLE);
                        }else{
                            videoLayoutadd.setVisibility(View.VISIBLE);
                        }
                    }
                    break;

                case 999:
                    if(pop_adapter!=null){
                        pop_adapter.setList(toulist);
                        pop_adapter.notifyDataSetChanged();
                    }


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
        noti.flags = Notification.FLAG_AUTO_CANCEL;
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
                (context, 0, new Intent(context, MainActivity.class), 0);
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
        ProgressBar mProgressBar = new ProgressBar(context);
        LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
        mProgressBar.setLayoutParams(lp);
        return mProgressBar;
    }

    /**
     * 准备播放资源参数
     */
    private void preparePlay() {
        //设置debug模式，输出log信息
        if(Uid.equals("")){
            videoLayoutadd.setVisibility(View.VISIBLE);
            zt=false;
            return;
        }
        if(videoLayoutadd!=null){
            videoLayoutadd.setVisibility(View.GONE);
        }
        zt=true;
        EZUIKit.setDebug(true);
        //appkey初始化
        EZUIKit.initWithAppKey(((Activity) context).getApplication(), appkey);
        //设置授权accesstoken
        EZUIKit.setAccessToken(accesstoken);
        //设置播放资源参数
        MyCallBack call = new MyCallBack();
        if (mEZUIPlayer != null) {
            mEZUIPlayer.setCallBack(call);

            if(Uid!=null){
                mEZUIPlayer.setUrl("ezopen://open.ys7.com/" + Uid + "/1.hd.live");
            }

        }

    }

    private void setSurfaceSize() {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
//        boolean isWideScrren = mOrientationDetector.isWideScrren();
////        //竖屏
//        if (!isWideScrren) {
//            //竖屏调整播放区域大小，宽全屏，高根据视频分辨率自适应
//            mEZUIPlayer.setSurfaceSize(dm.widthPixels, 0);
//        } else {
            //横屏屏调整播放区域大小，宽、高均全屏，播放区域根据视频分辨率自适应
            mEZUIPlayer.setSurfaceSize(dm.widthPixels, dm.heightPixels);

//            mEZUIPlayer.setSurfaceSize(mEZUIPlayer.getWidth() + 100, mEZUIPlayer.getHeight());
//        }
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
            Log.d("qqq","sssss "+data);
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
                String s=convertStreamToString(is);
                m.obj = s;
                Log.d("qqq","wwwwwwwwwwww "+s);
                if( s.equals("")){
                    ha.sendEmptyMessage(400);
                    return null;
                }
//                Log.d("qqq","qqqqqqqqq"+m.obj);
                ha.sendMessage(m);
//                Log.d("qqq", m.obj.toString());
                return null;
            } else {
                //请求失败
                Log.d("qqq", "xxxxx----------");
                Toast.makeText(context, "请求失败错误码：" + responseCode, Toast.LENGTH_SHORT).show();
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


    @Override
    public void onWindowSizeChanged(int w, int h, int oldW, int oldH) {
        Log.d("qqq", "onWindowSizeChanged");
        if (mEZUIPlayer != null) {
            setSurfaceSize();
        }
    }



    private PopupWindow mQualityPopupWindow = null;
    private ListView toulistView;
    private List<Equipment> toulist;
    private myPopWimAdapter pop_adapter;

    private int post=0;
    private int mushi=0;

    public int getMushi() {
        return mushi;
    }

    public void setMushi(int mushi) {
        this.mushi = mushi;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

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
                String s="update  AddEquipment  set mindex='"+getPost()+"' where uid='"+toulist.get(arg2).getUid()+"'";
                db.execSQL(s);
                dismissPopWindow(mQualityPopupWindow);
                LayoutMain2.popwinAddListener.PopwinAddListener(getMushi(),getPost());
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
                        db.execSQL(s);
                        LayoutMain2.popwinAddListener.PopwinAddListener(getMushi(),getPost());
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
