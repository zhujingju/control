package com.grasp.control.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.grasp.control.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhujingju on 2017/8/1.
 */

public class FragmetVideo1 extends Fragment {


    @BindView(R.id.viewPager)
    FrameLayout viewPager;
    Unbinder unbinder;
    private Context context;
    private String uid1="";

    private VideoListener videoListener;
    private int view_id=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragmet_video1, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        InitViewPager();

        return view;
    }

//    public void setUid(String u1,String u2){
//        uid1=u1;
//        uid2=u2;
//
//    }
    public void setUid(String u1){
        uid1=u1;
        if(video1!=null){
            video1.setUid(uid1);


        }

    }
    public void setUid2(String u1){
        uid1=u1;
        if(video1!=null){
            video1.setUid2(uid1);


        }

    }
//    public void setUid2(String u1){
//        uid1=u1;
//        if(video1!=null){
//            video1.setUid(uid1);
//        }
//
//    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

//    private void video_Start(){
//        video1.setSt();
//        video2.setSt();
//    }
//    private void video_dime(){
//        video1.setdime();
//        video2.setdime();
//    }

    public void video_Start(){
        if(video1==null){
            return;
        }
        video1.setStart();
    }
    public void video_Stop(){

        if(video1==null){
            return;
        }
        video1.setStop();
    }
    /**
     * 1
     * 初始化Viewpager页
     */
    private LayoutVideo video1;

    private void InitViewPager() {
        Log.d("qqq", "InitViewPager");
        video1 = new LayoutVideo();
        video1.setUid(uid1);
        initFragment1(video1);
    }



    //显示fragment
    private void initFragment1(Fragment f1){
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
//        if(f1 == null){
//            f1 = new MyFragment("消息");
        transaction.replace(R.id.viewPager, f1);
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


    public void Screenshot(){  //截图
            video1.Screenshot();
    }


    public void setToastListener(VideoListener toastListener) {
        this.videoListener = toastListener;
    }

    public interface VideoListener {

        void showToast(int posi);

    }

    int post=0;
    public int getPost(){  //获取当前选中那一个
        return post;
    }



    public boolean getRealPicture(){

           return    video1. getRealPicture();
    }

    public void getStopPicture(){

        video1. getStopPicture();
    }



    public void getJt(){
        if(video1!=null){
            video1.getJt();
        }

    }

    public boolean startVoiceTalk() {  //对讲
        if(video1!=null){

            return  video1.startVoiceTalk();
        }else{
            return false;
        }
    }

    public void stopVoiceTalk() {  //对讲
        if(video1!=null){
            video1.stopVoiceTalk();
        }
    }

    public void openSound() {  //
        if(video1!=null){
            video1.openSound();
        }
    }

    public void closeSound() {  //
        if(video1!=null){
            video1.closeSound();
        }
    }

    public void setMoshi(int i){
        if(video1!=null){
            video1.setMoshi(i);
        }
    }

    public void ydStop(){
        if(video1!=null){
            video1.stopPost();
        }
    }

    public void yd(){
        if(video1!=null){
            video1.loginByPost(2);
        }
    }

    public void setJt2(){
        if(video1!=null){
            video1.getJt2();
        }
    }

    public String getUid(){
        if(video1!=null){
            return  video1.getUid();

        }
        return "";
    }


    public void delPresetPoints(String post){
        if(video1!=null){
            video1.delPresetPoints( post);
        }
    }

    public void delPresetPoints2(){
        if(video1!=null){
            video1.del_listPresetPoints( );
        }
    }
    public void transferPresetPoints(String post){
        if(video1!=null){
            video1.transferPresetPoints( post);
        }
    }


    public void handleSetVedioModeSuccess(){
        if(video1!=null){
           video1.handleSetVedioModeSuccess();

        }
    }
}
