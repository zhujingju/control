package com.grasp.control.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezvizuikit.open.EZUIPlayer;
import com.grasp.control.MainActivity;
import com.grasp.control.MyInterface.AddListener;
import com.grasp.control.MyInterface.PopwinAddListener;
import com.grasp.control.R;
import com.grasp.control.Umeye_sdk.ShowProgress;
import com.grasp.control.dialog.MessageDialog;
import com.grasp.control.sqlite.AddSQLiteHelper;
import com.grasp.control.sqlite.Equipment;
import com.grasp.control.sqlite.MySQLiteHelper;
import com.grasp.control.tool.AudioPlayUtil;
import com.grasp.control.tool.BaseFragmentActivity;
import com.grasp.control.tool.MyApplication;
import com.grasp.control.tool.SharedPreferencesUtils;
import com.grasp.control.tool.Tool;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.videogo.exception.BaseException;
import com.videogo.util.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhujingju on 2017/7/20.
 */

public class LayoutMain2 extends Fragment
//        implements WindowSizeChangeNotifier.OnWindowSizeChangedListener
{


    @BindView(R.id.layout2_xz1)
    ImageView layout2Xz1;
    @BindView(R.id.layout2_xz2)
    ImageView layout2Xz2;
    @BindView(R.id.layout2_1_xz)
    TextView layout21Xz;
    @BindView(R.id.layout2_xz3)
    ImageView layout2Xz3;
    @BindView(R.id.layout2_xz4)
    ImageView layout2Xz4;
    @BindView(R.id.layout2_bian1)
    ImageView layout2Bian1;
    @BindView(R.id.layout2_bian2)
    ImageView layout2Bian2;
    @BindView(R.id.layout2_bian3)
    ImageView layout2Bian3;
    @BindView(R.id.layout2_bian4)
    ImageView layout2Bian4;
    @BindView(R.id.layout2_bian5)
    ImageView layout2Bian5;
    @BindView(R.id.layout2_bian6)
    ImageView layout2Bian6;
    @BindView(R.id.layout2_bian7)
    ImageView layout2Bian7;
    @BindView(R.id.layout2_bian8)
    ImageView layout2Bian8;
    @BindView(R.id.layout2_bian9)
    ImageView layout2Bian9;
    @BindView(R.id.layout2_dian_1)
    ImageView dian1;
    @BindView(R.id.layout2_dian_2)
    ImageView dian2;
    @BindView(R.id.layout2_frame)
    FrameLayout layout2_frame;
    @BindView(R.id.rec_dian)
    TextView recDian;
    @BindView(R.id.rec_time)
    TextView recTime;
    @BindView(R.id.rec_lin)
    LinearLayout recLin;
    @BindView(R.id.duijiang)
    TextView duijiang;
    @BindView(R.id.horizontal_listview)
    HorizontalListView listview;

    Unbinder unbinder;


    private Context context;


    private static final String TAG = "qqq";
    /**
     * onresume时是否恢复播放
     */
    private boolean isResumePlay = false;



    private ArrayList<String> Uid_list;


    private int moashi = 1;  //1  4  9  16

    private FragmetVideo1 fvideo1;
    private FragmetVideo2 fvideo2;
    private FragmetVideo3 fvideo3;
    private FragmetVideo4 fvideo4;

    private boolean zt9, zt6, zt7, zt8, zt4 = false;
    public static boolean zt3, zt5, zt2 = false;

    public static EZUIPlayer.LayoutViewListener layoutViewListener;
    public static AddListener addListener;
    public static PopwinAddListener popwinAddListener;

    private int max, current;
    private int time_num = 0;

    private AudioPlayUtil mAudioPlayUtil = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_main2, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        Uid_list = new ArrayList<>();
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");
        Uid_list.add("");

        if (pd == null) {
            pd = new ShowProgress(context);
            pd.setMessage("");
            pd.setCanceledOnTouchOutside(true);
        }

        recLin.setVisibility(View.GONE);
        initListview();

        time_num = 0;
        mAudioPlayUtil = AudioPlayUtil.getInstance(((Activity) context).getApplication());
        layout2Xz1.setImageResource(R.drawable.toolbar_monitormode_one_selected);
        layout2Xz2.setImageResource(R.drawable.toolbar_monitormode_for_normal);
        layout2Xz3.setImageResource(R.drawable.toolbar_monitormode_nin_normal);
        layout2Xz4.setImageResource(R.drawable.toolbar_monitormode_ths_normal);


        popwinAddListener=new PopwinAddListener() {
            @Override
            public void PopwinAddListener(int i,int post) {

                datainitw(i);
                fvideo2.setB(post);
                fvideo3.setB(post);
                fvideo4.setB(post);
            }
        };

        fvideo1 = new FragmetVideo1();
        fvideo2 = new FragmetVideo2();
        fvideo3 = new FragmetVideo3();
        fvideo4 = new FragmetVideo4();

//        fvideo1.setToastListener(new FragmetVideo1.VideoListener() {
//            @Override
//            public void showToast(int posi) {
//                if (posi == 0) {
//                    dian2.setBackgroundResource(R.drawable.toolbar_monitormode_circle_normal);
//                    dian1.setBackgroundResource(R.drawable.toolbar_monitormode_circle_selected);
//                } else {
//                    dian1.setBackgroundResource(R.drawable.toolbar_monitormode_circle_normal);
//                    dian2.setBackgroundResource(R.drawable.toolbar_monitormode_circle_selected);
//                }
//            }
//        });

        addListener=new AddListener() {
            @Override
            public void addListener() {
                dataListview();
            }
        };

        layoutViewListener = new EZUIPlayer.LayoutViewListener() {

            @Override
            public void layoutViewListener() {
                layout2Bian2.setImageResource(R.drawable.toolbar_monitormode_videotape_normal);
                zt2 = false;
                recLin.setVisibility(View.GONE);
                ha.removeMessages(2000);
                ha.removeMessages(2001);
                Log.d("qqq", "fffff");
            }

            @Override
            public void layoutViewListener_good() {
                layout2Bian2.setImageResource(R.drawable.toolbar_monitormode_videotape_selected);
                zt2 = true;
                recLin.setVisibility(View.VISIBLE);
                ha.sendEmptyMessageDelayed(2000, 0);
                ha.sendEmptyMessageDelayed(2001, 0);

                Log.d("qqq", "good");
            }

            @Override
            public void VoiceTalkListener_close_err() {


                ha.sendEmptyMessageDelayed(1666, 500);
                duijiang.setVisibility(View.VISIBLE);
                layout2Bian7.setImageResource(R.drawable.toolbar_monitormode_mic_selected);
                layout2Bian6.setImageResource(R.drawable.toolbar_monitormode_voice_normal);
                zt7 = true;
                zt6 = true;
            }

            @Override
            public void VoiceTalkListener_open_err() {
                ha.removeMessages(1666);
                duijiang.setVisibility(View.GONE);
                layout2Bian7.setImageResource(R.drawable.toolbar_monitormode_mic_normal);
                layout2Bian6.setImageResource(R.drawable.toolbar_monitormode_voice_selected);
                zt7 = false;
                zt6 = true;
            }
        };

        datainitw(0);

        fuyan();
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d("qqq", "max : " + max + " current : " + current);
        if (current != 0) {
            layout2Bian6.setImageResource(R.drawable.toolbar_monitormode_voice_selected);
            zt6 = true;
        } else {
            layout2Bian6.setImageResource(R.drawable.toolbar_monitormode_voice_normal);
        }


        return view;
    }

    private List<Equipment> g_list;
    public void datainitw(final int post) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                g_list = new ArrayList<Equipment>();

                AddSQLiteHelper dbHelper = new AddSQLiteHelper(context, "add.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String s = "select * from AddEquipment ";
                Cursor cursor = db.rawQuery(s, null);
                int num=1;
                for(int i=0;i<Uid_list.size();i++){
                    Uid_list.set(i,"");
                }
                while (cursor.moveToNext()) {

                    String uid = cursor.getString(1);//获取第2列的值
                    String sid = cursor.getString(2);//获取第3列的值
                    String index = cursor.getString(3);//获取第4列的值
                    Equipment g = new Equipment();
                    g.setUid(uid);
                    g.setSid(sid);
//                    g.setIndex(index);
                    g.setIndex(num+"");
                    g_list.add(g);
                    if(Integer.valueOf(index)>=0&&Integer.valueOf(index)<16){
                        Uid_list.set(Integer.valueOf(index),sid);
                    }


                    num++;
                }
                if(post==1){
                    ha.sendEmptyMessage(1999);
                }else if(post==4){
                    ha.sendEmptyMessage(2999);
                }else if(post==9){
                    ha.sendEmptyMessage(3999);
                }else if(post==16){
                    ha.sendEmptyMessage(4999);
                }else{
                    ha.sendEmptyMessage(1988);
                }

            }
        }).start();

    }

    //显示fragment
    private void initFragment1(Fragment f1) {
        if(f1==null){
            return;
        }
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
//        if(f1 == null){
//            f1 = new MyFragment("消息");
        transaction.replace(R.id.layout2_frame, f1);
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
    private void initFragment2(Fragment f1) {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
//        if(f1 == null){
//            f1 = new MyFragment("消息");
        transaction.replace(R.id.layout2_frame, f1);
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

//    @Override
//    public void onWindowSizeChanged(int w, int h, int oldW, int oldH) {
//        Log.d("qqq","onWindowSizeChanged");
//        if (mEZUIPlayer != null) {
//            setSurfaceSize();
//        }
//    }


    public void setDes() {
        fvideo1.stopVoiceTalk();
        ha.removeCallbacks(null);
    }

    @Override
    public void onDestroyView() {

        Log.d("qqq", "onDestroyView");
        ha.removeCallbacks(null);
        super.onDestroyView();


        unbinder.unbind();
    }


    @Override
    public void onResume() {
        super.onResume();
//        if (mOrientationDetector == null) {
//            return;
//        }
//        mOrientationDetector.enable();
        //界面stop时，如果在播放，那isResumePlay标志位置为true，resume时恢复播放
    }

    @Override
    public void onStop() {
        super.onStop();
        //停止播放
    }


    @OnClick({R.id.layout2_xz1, R.id.layout2_xz2, R.id.layout2_xz3, R.id.layout2_xz4, R.id.layout2_bian1, R.id.layout2_bian2, R.id.layout2_bian3, R.id.layout2_bian4, R.id.layout2_bian5, R.id.layout2_bian6, R.id.layout2_bian7, R.id.layout2_bian8, R.id.layout2_bian9})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout2_xz1:

                moashi = 1;
                layout2Xz1.setImageResource(R.drawable.toolbar_monitormode_one_selected);
                layout2Xz2.setImageResource(R.drawable.toolbar_monitormode_for_normal);
                layout2Xz3.setImageResource(R.drawable.toolbar_monitormode_nin_normal);
                layout2Xz4.setImageResource(R.drawable.toolbar_monitormode_ths_normal);
//                dian1.setVisibility(View.VISIBLE);
//                dian2.setVisibility(View.VISIBLE);
//                layout21Xz.setVisibility(View.GONE);
//                dian1.setVisibility(View.GONE);
//                dian2.setVisibility(View.GONE);
//                layout21Xz.setVisibility(View.VISIBLE);
                initFragment1(fvideo1);
//                fuyan();


                break;
            case R.id.layout2_xz2:
                ha.removeMessages(1666);

                layout2Bian7.setImageResource(R.drawable.toolbar_monitormode_mic_normal);
                layout2Bian6.setImageResource(R.drawable.toolbar_monitormode_voice_selected);
                zt7 = false;
                initFragment1(fvideo2);
                moashi = 4;
                layout2Xz1.setImageResource(R.drawable.toolbar_monitormode_one_normal);
                layout2Xz2.setImageResource(R.drawable.toolbar_monitormode_for_selected);
                layout2Xz3.setImageResource(R.drawable.toolbar_monitormode_nin_normal);
                layout2Xz4.setImageResource(R.drawable.toolbar_monitormode_ths_normal);
//                dian1.setVisibility(View.GONE);
//                dian2.setVisibility(View.GONE);
//                layout21Xz.setVisibility(View.VISIBLE);
                fuyan();
                break;
            case R.id.layout2_xz3:
                ha.removeMessages(1666);
                layout2Bian7.setImageResource(R.drawable.toolbar_monitormode_mic_normal);
                layout2Bian6.setImageResource(R.drawable.toolbar_monitormode_voice_selected);
                zt7 = false;
                initFragment1(fvideo3);
                moashi = 9;
                layout2Xz1.setImageResource(R.drawable.toolbar_monitormode_one_normal);
                layout2Xz2.setImageResource(R.drawable.toolbar_monitormode_for_normal);
                layout2Xz3.setImageResource(R.drawable.toolbar_monitormode_nin_selected);
                layout2Xz4.setImageResource(R.drawable.toolbar_monitormode_ths_normal);
//                dian1.setVisibility(View.GONE);
//                dian2.setVisibility(View.GONE);
//                layout21Xz.setVisibility(View.VISIBLE);
                fuyan();
                break;
            case R.id.layout2_xz4:
                ha.removeMessages(1666);
                layout2Bian7.setImageResource(R.drawable.toolbar_monitormode_mic_normal);
                layout2Bian6.setImageResource(R.drawable.toolbar_monitormode_voice_selected);
                zt7 = false;
                initFragment1(fvideo4);
                moashi = 16;
                layout2Xz1.setImageResource(R.drawable.toolbar_monitormode_one_normal);
                layout2Xz2.setImageResource(R.drawable.toolbar_monitormode_for_normal);
                layout2Xz3.setImageResource(R.drawable.toolbar_monitormode_nin_normal);
                layout2Xz4.setImageResource(R.drawable.toolbar_monitormode_ths_selected);
//                dian1.setVisibility(View.GONE);
//                dian2.setVisibility(View.GONE);
//                layout21Xz.setVisibility(View.VISIBLE);
                fuyan();
                break;
            case R.id.layout2_bian1:
                if (zt9) {
                    Toast.makeText(context, "请开启视频后重试", Toast.LENGTH_SHORT).show();
                    return;
                }

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        sreenShot();
//                    }
//                }).start();


                if (moashi == 1) {
                    if( fvideo1.getUid().equals("")){
                        Toast.makeText(context, "请播放视频后再试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAudioPlayUtil.playAudioFile(AudioPlayUtil.CAPTURE_SOUND);
                    fvideo1.getJt();
                } else {
                    if(setView1()){
                        Toast.makeText(context, "已切换到单频模式，请重试", Toast.LENGTH_SHORT).show();
                    }
                }


                break;
            case R.id.layout2_bian2:
                if (zt9) {
                    Toast.makeText(context, "请开启视频后重试", Toast.LENGTH_SHORT).show();
                    return;
                }
//                layout2Bian2.setImageResource(R.drawable.toolbar_monitormode_videotape_selected);

//                Toast.makeText(context,"暂无此功能",Toast.LENGTH_SHORT).show();
                if (moashi == 1) {
                    if( fvideo1.getUid().equals("")){
                        Toast.makeText(context, "请播放视频后再试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.d("qqq",zt2+"   -----------");
                    mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
                    if (zt2) {
                        recLin.setVisibility(View.GONE);
                        ha.removeMessages(2000);
                        ha.removeMessages(2001);
                        time_num = 0;
                        recTime.setText("00:00");
                        layout2Bian2.setImageResource(R.drawable.video);
                        fvideo1.getStopPicture();
//                        zt2 = !zt2;
                    } else {
                        if(fvideo1.getRealPicture()){
                            layout2Bian2.setImageResource(R.drawable.toolbar_monitormode_videotape_selected);
//                            zt2 = !zt2;
                        }



                    }
                    Log.d("qqq",zt2+"   +++-----------");
                } else {
                    if(setView1()){
                        Toast.makeText(context, "已切换到单频模式，请重试", Toast.LENGTH_SHORT).show();
                    }

                }


                break;
            case R.id.layout2_bian3:
                if (zt9) {
                    Toast.makeText(context, "请开启视频后重试", Toast.LENGTH_SHORT).show();
                    return;
                }
//                zt5 = false;
//                layout2Bian5.setImageResource(R.drawable.toolbar_monitormode_resolving_normal);



                if (moashi == 1) {
                    if( fvideo1.getUid().equals("")){
                        Toast.makeText(context, "请播放视频后再试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setView1();
                    mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
                    if (zt3) {
                        layout2Bian3.setImageResource(R.drawable.kongzhi);
                    } else {
                        layout2Bian3.setImageResource(R.drawable.toolbar_monitormode_control_selected);
                    }
                    zt3 = !zt3;
                } else {
                    if(setView1()){
                        Toast.makeText(context, "已切换到单频模式，请重试", Toast.LENGTH_SHORT).show();
                    }
                }


                break;
            case R.id.layout2_bian4:
                if (zt9) {
                    Toast.makeText(context, "请开启视频后重试", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (moashi == 1) {
                    if( fvideo1.getUid().equals("")){
                        Toast.makeText(context, "请播放视频后再试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
                    if (zt4) {
                        zt4 = !zt4;
                        listview.setVisibility(View.GONE);
                        layout2Bian4.setImageResource(R.drawable.toolbar_monitormode_point_normal);
                    } else {
                        if(!fvideo1.getUid().equals("")){
                            String s = (String) SharedPreferencesUtils.getParam(context, "zjj"+fvideo1.getUid(), "");
                            if(s.equals("")){
                                fvideo1.delPresetPoints2();
                            }else{
                                zt4 = !zt4;
                                listview.setVisibility(View.VISIBLE);
                                layout2Bian4.setImageResource(R.drawable.toolbar_monitormode_point_selected);
                                dataListview();
                            }
                        }

                    }
                } else {
                    if(setView1()){
                        Toast.makeText(context, "已切换到单频模式，请重试", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.layout2_bian5:
                if (zt9) {
                    Toast.makeText(context, "请开启视频后重试", Toast.LENGTH_SHORT).show();
                    return;
                }
//                layout2Bian3.setImageResource(R.drawable.kongzhi);
//                zt3 = false;
//
//
//                if (zt5) {
//                    layout2Bian5.setImageResource(R.drawable.toolbar_monitormode_resolving_normal);
//                } else {
//                    layout2Bian5.setImageResource(R.drawable.toolbar_monitormode_resolving_selected);
//                }
//                zt5 = !zt5;
//                fvideo1.setMoshi(1);




                if (moashi == 1) {
                    if( fvideo1.getUid().equals("")){
                        Toast.makeText(context, "请播放视频后再试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
                    openQualityPopupWindow(layout2Bian5);
                } else {
                    if(setView1()){
                        Toast.makeText(context, "已切换到单频模式，请重试", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.layout2_bian6:
                if (zt9) {
                    Toast.makeText(context, "请开启视频后重试", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (moashi == 1) {
                    if( fvideo1.getUid().equals("")){
                        Toast.makeText(context, "请播放视频后再试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
                    if (zt6) {
                        layout2Bian6.setImageResource(R.drawable.toolbar_monitormode_voice_normal);
                        fvideo1.closeSound();
                    } else {
                        layout2Bian6.setImageResource(R.drawable.toolbar_monitormode_voice_selected);
                        fvideo1.openSound();
                    }
                    zt6 = !zt6;
                } else {
                    if(setView1()){
                        Toast.makeText(context, "已切换到单频模式，请重试", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.layout2_bian7:
                if (zt9) {
                    Toast.makeText(context, "请开启视频后重试", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (moashi == 1) {
                    if( fvideo1.getUid().equals("")){
                        Toast.makeText(context, "请播放视频后再试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
                    if (zt7) {
                        ha.removeMessages(1666);
                        duijiang.setVisibility(View.GONE);
                        layout2Bian7.setImageResource(R.drawable.toolbar_monitormode_mic_normal);
                        layout2Bian6.setImageResource(R.drawable.toolbar_monitormode_voice_selected);
                        zt7 = !zt7;
                        zt6 = true;
                        fvideo1.stopVoiceTalk();
                    } else {

                        if(fvideo1.startVoiceTalk()){
                            ha.sendEmptyMessageDelayed(1666, 500);
                            duijiang.setVisibility(View.VISIBLE);
                            layout2Bian7.setImageResource(R.drawable.toolbar_monitormode_mic_selected);
                            layout2Bian6.setImageResource(R.drawable.toolbar_monitormode_voice_normal);
                            zt7 = !zt7;
                            zt6 = false;
                        }


                    }
                } else {
                    if(setView1()){
                        Toast.makeText(context, "已切换到单频模式，请重试", Toast.LENGTH_SHORT).show();
                    }
                }


                break;
            case R.id.layout2_bian8:
                if (zt9) {
                    Toast.makeText(context, "请开启视频后重试", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (moashi == 1) {
                    if( fvideo1.getUid().equals("")){
                        Toast.makeText(context, "请播放视频后再试", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
                    if (zt8) {
                        layout2Bian8.setImageResource(R.drawable.toolbar_monitormode_call_normal);
                        zt8 = !zt8;
                        fvideo1.ydStop();
                    } else {
                        layout2Bian8.setImageResource(R.drawable.toolbar_monitormode_call_selected);
                        zt8 = !zt8;
                        fvideo1.yd();

                    }
                } else {
                    if(setView1()){
                        Toast.makeText(context, "已切换到单频模式，请重试", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.layout2_bian9:
                boolean zt = true;
                mAudioPlayUtil.playAudioFile(AudioPlayUtil.RECORD_SOUND);
                if (zt9) {
                    layout2Bian9.setImageResource(R.drawable.toolbar_monitormode_onoff_selected);
                    zt9 = false;
                    if (moashi == 1) {
                        fvideo1.video_Start();
                    } else if (moashi == 4) {
                        fvideo2.video4_Start_P();
                    } else if (moashi == 9) {
                        fvideo3.video9_Start_P();
                    } else if (moashi == 16) {
                        fvideo4.video16_Start_P();
                    }
                } else {

                    fuyan();
                    zt9 = true;
                    layout2Bian9.setImageResource(R.drawable.toolbar_monitormode_onoff_normal);
                    if (moashi == 1) {
                        fvideo1.video_Stop();
                    } else if (moashi == 4) {
                        fvideo2.video4_Stop();
                    } else if (moashi == 9) {
                        fvideo3.video9_Stop();
                    } else if (moashi == 16) {
                        fvideo4.video16_Stop();
                    }
                }
                break;
        }
    }


    private boolean setView1() {
        int post = 0;
        if (moashi == 1) {
            post = fvideo1.getPost();
            Log.d("qqq", "bian3 " + Uid_list.get(post) + "  " + post);
            if (Uid_list.get(post).equals("")) {
                Toast.makeText(context, "请选择正在播放的设备重试", Toast.LENGTH_SHORT).show();
                return false;
            }
            fvideo1.setUid(Uid_list.get(post));
            moashi = 1;
        } else if (moashi == 4) {
            post = fvideo2.getPost() - 1;
            Log.d("qqq", "bian3 " + Uid_list.get(post) + "  " + post);
            if (Uid_list.get(post).equals("")) {
                Toast.makeText(context, "请选择正在播放的设备重试", Toast.LENGTH_SHORT).show();
                return false;
            }
            fvideo1.setUid(Uid_list.get(post));
            moashi = 1;
            initFragment1(fvideo1);
        } else if (moashi == 9) {
            post = fvideo3.getPost() - 1;
            Log.d("qqq", "bian3 " + Uid_list.get(post) + "  " + post);
            if (Uid_list.get(post).equals("")) {
                Toast.makeText(context, "请选择正在播放的设备重试", Toast.LENGTH_SHORT).show();
                return false;
            }
            fvideo1.setUid(Uid_list.get(post));
            moashi = 1;
            initFragment1(fvideo1);
        } else if (moashi == 16) {
            post = fvideo4.getPost() - 1;
            Log.d("qqq", "bian3 " + Uid_list.get(post) + "  " + post);
            if (Uid_list.get(post).equals("")) {
                Toast.makeText(context, "请选择正在播放的设备重试", Toast.LENGTH_SHORT).show();
                return false;
            }
            fvideo1.setUid(Uid_list.get(post));
            moashi = 1;
            initFragment1(fvideo1);
        }
        layout2Xz1.setImageResource(R.drawable.toolbar_monitormode_one_selected);
        layout2Xz2.setImageResource(R.drawable.toolbar_monitormode_for_normal);
        layout2Xz3.setImageResource(R.drawable.toolbar_monitormode_nin_normal);
        layout2Xz4.setImageResource(R.drawable.toolbar_monitormode_ths_normal);
        return true;
    }

    public void fuyan2(){
        zt9 = false;
        zt2 = false;
        zt3 = false;
        zt7 = false;
        zt8 = false;
        zt4 = false;
        zt6 = true;
        time_num = 0;
    }
    private void fuyan() {
        dian1.setVisibility(View.GONE);
        dian2.setVisibility(View.GONE);
        layout21Xz.setVisibility(View.VISIBLE);
        layout2Bian9.setImageResource(R.drawable.toolbar_monitormode_onoff_selected);
        zt9 = false;

        layout2Bian3.setImageResource(R.drawable.kongzhi);
        layout2Bian2.setImageResource(R.drawable.video);
        zt2 = false;
        zt3 = false;
        zt7 = false;
        zt8 = false;
        zt4 = false;
        zt6 = true;
        listview.setVisibility(View.GONE);
        layout2Bian6.setImageResource(R.drawable.toolbar_monitormode_voice_selected);
        layout2Bian7.setImageResource(R.drawable.toolbar_monitormode_mic_normal);
        layout2Bian8.setImageResource(R.drawable.toolbar_monitormode_call_normal);
        layout2Bian4.setImageResource(R.drawable.toolbar_monitormode_point_normal);
        fvideo1.ydStop();
//        fvideo1.getStopPicture();
        fvideo1.stopVoiceTalk();
        duijiang.setVisibility(View.GONE);

        recLin.setVisibility(View.GONE);
        ha.removeMessages(2000);
        ha.removeMessages(2001);
        time_num = 0;
        recTime.setText("00:00");
    }


    Handler ha = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 333:
                    LogUtil.infoLog(TAG, "KEYCODE_BACK DOWN   vvv");
                    pd.dismiss();
                    View decorView = ((BaseFragmentActivity)context).getWindow().getDecorView();
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    break;
                case 1666:
                    if (duijiang != null) {
                        if (duijiang.getText().equals("对讲中..")) {
                            duijiang.setText("对讲中...");
                        } else {
                            duijiang.setText("对讲中..");
                        }
                        ha.sendEmptyMessageDelayed(1666, 1000);
                    }

                    break;
                case 1111:

                    Toast.makeText(context, R.string.jt_good, Toast.LENGTH_SHORT).show();
                    break;
                case 1112:
                    Toast.makeText(context, R.string.jt_err2, Toast.LENGTH_SHORT).show();
                    break;

                case 2000:
                    Log.d("time", "time:" + time_num);
                    if (recTime != null) {
                        recTime.setText(Tool.secToTime(time_num));
                        time_num++;
                        ha.sendEmptyMessageDelayed(2000, 1000);
                    }

                    break;

                case 2001:
                    if (recDian != null) {
                        if (recDian.getVisibility() == View.VISIBLE) {
                            recDian.setVisibility(View.INVISIBLE);
                        } else {
                            recDian.setVisibility(View.VISIBLE);
                        }
                        ha.sendEmptyMessageDelayed(2001, 1000);
                    }

                    break;

                case 1988:
                    fvideo1.setUid(Uid_list.get(0));
                    fvideo2.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3));
                    fvideo3.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3), Uid_list.get(4),
                            Uid_list.get(5), Uid_list.get(6), Uid_list.get(7), Uid_list.get(8));
                    fvideo4.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3), Uid_list.get(4),
                            Uid_list.get(5), Uid_list.get(6), Uid_list.get(7), Uid_list.get(8), Uid_list.get(9), Uid_list.get(10)
                            , Uid_list.get(11), Uid_list.get(12), Uid_list.get(13), Uid_list.get(14), Uid_list.get(15));
                    initFragment1(fvideo1);
                    break;
                case 1999:
                    fvideo1.setUid2(Uid_list.get(0));
                    fvideo2.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3));
                    fvideo3.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3), Uid_list.get(4),
                            Uid_list.get(5), Uid_list.get(6), Uid_list.get(7), Uid_list.get(8));
                    fvideo4.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3), Uid_list.get(4),
                            Uid_list.get(5), Uid_list.get(6), Uid_list.get(7), Uid_list.get(8), Uid_list.get(9), Uid_list.get(10)
                            , Uid_list.get(11), Uid_list.get(12), Uid_list.get(13), Uid_list.get(14), Uid_list.get(15));
                    initFragment1(fvideo1);
                    break;

                case 2999:
                    Log.d("qqq","PopwinAddListener="+2999);
                    fvideo1.setUid2(Uid_list.get(0));
                    fvideo2.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3));
                    fvideo3.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3), Uid_list.get(4),
                            Uid_list.get(5), Uid_list.get(6), Uid_list.get(7), Uid_list.get(8));
                    fvideo4.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3), Uid_list.get(4),
                            Uid_list.get(5), Uid_list.get(6), Uid_list.get(7), Uid_list.get(8), Uid_list.get(9), Uid_list.get(10)
                            , Uid_list.get(11), Uid_list.get(12), Uid_list.get(13), Uid_list.get(14), Uid_list.get(15));
                    initFragment2(fvideo2);
                    break;

                case 3999:
                    fvideo1.setUid2(Uid_list.get(0));
                    fvideo2.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3));
                    fvideo3.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3), Uid_list.get(4),
                            Uid_list.get(5), Uid_list.get(6), Uid_list.get(7), Uid_list.get(8));
                    fvideo4.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3), Uid_list.get(4),
                            Uid_list.get(5), Uid_list.get(6), Uid_list.get(7), Uid_list.get(8), Uid_list.get(9), Uid_list.get(10)
                            , Uid_list.get(11), Uid_list.get(12), Uid_list.get(13), Uid_list.get(14), Uid_list.get(15));
                    initFragment2(fvideo3);
                    break;

                case 4999:
                    fvideo1.setUid2(Uid_list.get(0));
                    fvideo2.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3));
                    fvideo3.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3), Uid_list.get(4),
                            Uid_list.get(5), Uid_list.get(6), Uid_list.get(7), Uid_list.get(8));
                    fvideo4.setUid(Uid_list.get(0), Uid_list.get(1), Uid_list.get(2), Uid_list.get(3), Uid_list.get(4),
                            Uid_list.get(5), Uid_list.get(6), Uid_list.get(7), Uid_list.get(8), Uid_list.get(9), Uid_list.get(10)
                            , Uid_list.get(11), Uid_list.get(12), Uid_list.get(13), Uid_list.get(14), Uid_list.get(15));
                    initFragment2(fvideo4);
                    break;
            }
        }
    };


    public void sreenShot() {  //截图

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
        {
            String sdCardDir = MainActivity.image_file;
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
                Log.d("qqq", "buzai  " + sdCardDir);
            } else {
                Log.d("qqq", "zai  " + sdCardDir);
            }
        }

        String path = MainActivity.image_file + "zjj_" + System.currentTimeMillis() + ".png";
        String cmd = "screencap -p " + path;
        try {
            Process process = Runtime.getRuntime().exec("su");//不同的设备权限不一样
            PrintWriter pw = new PrintWriter(process.getOutputStream());
            pw.println(cmd);
            pw.flush();
            pw.println("exit");
            pw.flush();
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            pw.close();
            process.destroy();
            saveBitmap(ImageCrop(convertToBitmap(path, 1920, 1280)));


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            ha.sendEmptyMessage(1112);
        }


    }


    public Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int) scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        if (weak.get() == null) {
            return null;
        }
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    /**
     * 裁切图片
     */
    public Bitmap ImageCrop(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Log.d("qqq", "ImageCrop" + bitmap.getWidth() + "  " + bitmap.getHeight());
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();

        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长

        int retX = w > h ? (w - h) / 2 : 0;//基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;

        //下面这句是关键
        return Bitmap.createBitmap(bitmap, 120, 0, 1920 - 120 - 120, 1280 - 80, null, false);
    }


    /**
     * 保存方法
     */
    public void saveBitmap(Bitmap bm) {
        if (bm == null) {
            ha.sendEmptyMessage(1112);
            return;
        }

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
        {    // 获取SDCard指定目录下
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String t = format.format(new Date());
            String sdCardDir = MainActivity.image_file_im + "/" + t;
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }                          //文件夹有啦，就可以保存图片啦

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String t1 = format1.format(new Date());
            File file = new File(sdCardDir, "" + t1 + ".png");// 在SDcard的目录下创建图片文,以当前时间为其命名

            try {
                FileOutputStream out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                ha.sendEmptyMessage(1111);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private PopupWindow mQualityPopupWindow = null;
    ShowProgress pd;

    private void openQualityPopupWindow(View anchor) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup layoutView = (ViewGroup) layoutInflater.inflate(R.layout.realplay_quality_items, null, true);

        Button qualityHdBtn = (Button) layoutView.findViewById(R.id.quality_hd_btn);
        qualityHdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fvideo1.setMoshi(1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MyApplication.getOpenSDK().setVideoLevel(fvideo1.getUid(),1,2);
                            fvideo1.handleSetVedioModeSuccess();
                        } catch (BaseException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                dismissPopWindow(mQualityPopupWindow);
            }
        });
        Button qualityBalancedBtn = (Button) layoutView.findViewById(R.id.quality_balanced_btn);
        qualityBalancedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MyApplication.getOpenSDK().setVideoLevel(fvideo1.getUid(),1,0);
                            fvideo1.handleSetVedioModeSuccess();
                        } catch (BaseException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                dismissPopWindow(mQualityPopupWindow);
            }
        });

        Button jh = (Button) layoutView.findViewById(R.id.quality_jh_btn);
        jh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MyApplication.getOpenSDK().setVideoLevel(fvideo1.getUid(),1,1);
                            fvideo1.handleSetVedioModeSuccess();
                        } catch (BaseException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                dismissPopWindow(mQualityPopupWindow);
            }
        });

        mQualityPopupWindow = new PopupWindow(layoutView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        mQualityPopupWindow.setBackgroundDrawable(new BitmapDrawable());
//        mQualityPopupWindow.
        mQualityPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mQualityPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                LogUtil.infoLog(TAG, "KEYCODE_BACK DOWN");
                mQualityPopupWindow = null;
                if (mQualityPopupWindow != null) {
                    dismissPopWindow(mQualityPopupWindow);
                    mQualityPopupWindow = null;

                }
               if(pd!=null){
                       pd.show();
               }

                ha.sendEmptyMessageDelayed(333,500);
            }
        });
        try {
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            mQualityPopupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, location[0] + (45), location[1] - 365);


        } catch (Exception e) {
            e.printStackTrace();
            if (mQualityPopupWindow != null) {
                dismissPopWindow(mQualityPopupWindow);
                mQualityPopupWindow = null;
            }
        }
    }

    private void dismissPopWindow(PopupWindow popupWindow) {
        if (popupWindow != null && !((Activity) (context)).isFinishing()) {
            try {
                popupWindow.dismiss();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }


    private List<Goods> list ;
    private myListViewAdapter adapter;

    private void dataListview(){

        list=new ArrayList<>();
        if(fvideo1==null){
            return;
        }
        MySQLiteHelper dbHelper = new MySQLiteHelper(context, "demo.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String s="select * from TabPlay where uid='"+fvideo1.getUid()+"'";
        Log.d("qqq","s="+s);
        Cursor cursor = db.rawQuery(s, null);
        while (cursor.moveToNext()) {


            String title = cursor.getString(1);//获取第2列的值
            String index = cursor.getString(2);//获取第3列的值
            String uid = cursor.getString(3);//获取第4列的值
            String path = cursor.getString(4);//获取第5列的值
            Goods goods=new Goods();
            goods.setName(title);
            goods.setIm_path(path);
            goods.setIndex(index);
            list.add(goods);
        }




        Goods goods=new Goods();
        goods.setAdd_zt(true);


        list.add(goods);
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    };

    private void initListview(){

        adapter=new myListViewAdapter(context, list);
//        dataListview();



//        View v=LayoutInflater.from(context).inflate(R.layout.item_h_add,null);
//
//
//        listview.addView(v);

//        Log.d("qqq",adapter.getList().size()+" ---------");
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
            }
        });
    }



    class myListViewAdapter extends BaseAdapter {

        LayoutInflater inflater = null;
        private Context context;
        private List<Goods> list=null;

        public myListViewAdapter(Context context,List<Goods> list) {
            this.context=context;
            this.list=list;

            inflater = ((Activity)(context)).getLayoutInflater();
        }


        public List<Goods> getList() {
            return list;
        }



        public void setList(List<Goods> list) {
            this.list = list;
        }



        @Override
        public int getCount() {
            if(list!=null){
                return list.size();
            }
            return 0;
        }

        @Override
        public Goods getItem(int arg0) {
            if(list!=null){
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
            View view=convertView;
            Goods camera= getItem(position);
            click click=new click(position);
            ListViewTool too;
            if(convertView==null){
                view = inflater.inflate(R.layout.item_h_listview, parent, false);
                too=new ListViewTool();
                too.name=(TextView) view.findViewById(R.id.item_h_tv);
                too.del=(ImageView) view.findViewById(R.id.item_h_del);
                too.im=(ImageView) view.findViewById(R.id.item_h_im);
                too.add=(ImageView) view.findViewById(R.id.item_h_add);
                too.layout=(RelativeLayout) view.findViewById(R.id.item_h_layout);
                too.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fvideo1.setJt2();
                    }
                });
//                too.add.setOnTouchListener(click);
                too.im.setOnTouchListener(click);
//                too.del.setOnTouchListener(click);

                view.setTag(too);
            }
            else {
                too = (ListViewTool) view.getTag();
            }
            if(camera == null) return null;

            click.setPost(position);


            too.name.setText(camera.getName());
            if(camera.isDel_zt()){
                too.del.setVisibility(View.VISIBLE);
            }else{
                too.del.setVisibility(View.GONE);
            }

            if(camera.isAdd_zt()){
                if(list.size()>=13){
                    too.add.setVisibility(View.GONE);
                    too.layout.setVisibility(View.GONE);
                }else{
                    too.add.setVisibility(View.VISIBLE);
                    too.layout.setVisibility(View.GONE);
                }

            }else{
                too.add.setVisibility(View.GONE);
                too.layout.setVisibility(View.VISIBLE);
            }
            if(!camera.getIm_path().equals("")){
                ImageLoader.getInstance().displayImage("file://" + camera.getIm_path(), too.im, MyApplication.options);

            }
            if(!camera.getName().equals("")){
                too.name.setText(camera.getName());
            }
            return view;
        }

        class ListViewTool {

            public TextView name ;
            public ImageView del,im,add;
            public RelativeLayout layout;
        }

        float xDown,yDown,xUp;
        boolean dialog_zt=false;
        int mosi=0;
        class click implements View.OnTouchListener ,View.OnClickListener{
            private int post;
            public click(int post){
                this.post=post;
            }

            public void setPost(int post) {
                this.post = post;
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        xDown= event.getX();
                        yDown = event.getY();
                        mosi=1;
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("qqq",mosi+" ssss ");
                        if(v.getId()==R.id.item_h_im) {
                            if(mosi==1){
                                Log.d("qqq",mosi+"  ");
                                fvideo1.transferPresetPoints(list.get(post).getIndex());
                            }

                        }
                        break;
                    case MotionEvent.ACTION_MOVE:  //长按
                        if(isLongPressed(xDown, yDown, event.getX(),
                                event.getY(),event.getDownTime() ,event.getEventTime(),300)){
                            if(v.getId()==R.id.item_h_im){
                                mosi=2;
//                                list.get(post).setDel_zt(true);
//                                adapter.setList(list);
//                                adapter.notifyDataSetChanged();
                                if(!dialog_zt){
                                    dialog_zt=true;
                                    final AlertDialog.Builder normalDialog =
                                            new AlertDialog.Builder(context);
                                    normalDialog.setTitle("删除");
                                    normalDialog.setMessage("是否删除\""+list.get(post).getName()+"\"");
                                    normalDialog.setPositiveButton("确定",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog_zt=false;
                                                    fvideo1.delPresetPoints(list.get(post).getIndex());

                                                }
                                            });
                                    normalDialog.setNegativeButton("取消",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //...To-do
                                                    dialog_zt=false;
                                                }
                                            });
                                    // 显示
                                    normalDialog.setCancelable(false);
                                    normalDialog.show();
                                }

                            }
                        }
                        break;
                }

                return false;
            }

            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.item_h_add){
//                    fvideo1.setJt2();
                }
            }
        }

    }


    class Goods {
        private String name="" ,im_path="",index="";
        private boolean del_zt,add_zt;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public boolean isAdd_zt() {
            return add_zt;
        }

        public void setAdd_zt(boolean add_zt) {
            this.add_zt = add_zt;
        }

        public String getIm_path() {
            return im_path;
        }

        public void setIm_path(String im_path) {
            this.im_path = im_path;
        }

        public boolean isDel_zt() {
            return del_zt;
        }

        public void setDel_zt(boolean del_zt) {
            this.del_zt = del_zt;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }




    /* 判断是否有长按动作发生
* @param lastX 按下时X坐标
* @param lastY 按下时Y坐标
* @param thisX 移动时X坐标
* @param thisY 移动时Y坐标
* @param lastDownTime 按下时间
* @param thisEventTime 移动时间
* @param longPressTime 判断长按时间的阀值
*/
    private boolean isLongPressed(float lastX,float lastY,
                                  float thisX,float thisY,
                                  long lastDownTime,long thisEventTime,
                                  long longPressTime){
        float offsetX = Math.abs(thisX - lastX);
        float offsetY = Math.abs(thisY - lastY);
        long intervalTime = thisEventTime - lastDownTime;
        if(offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime){
            return true;
        }
        return false;
    }
}
