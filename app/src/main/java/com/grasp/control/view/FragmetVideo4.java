package com.grasp.control.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grasp.control.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhujingju on 2017/8/1.
 */

public class FragmetVideo4 extends Fragment {


    @BindView(R.id.view16_viewvideo1)
    ViewVideo view16Viewvideo1;
    @BindView(R.id.view16_viewvideo2)
    ViewVideo view16Viewvideo2;
    @BindView(R.id.view16_viewvideo3)
    ViewVideo view16Viewvideo3;
    @BindView(R.id.view16_viewvideo4)
    ViewVideo view16Viewvideo4;
    @BindView(R.id.view16_viewvideo5)
    ViewVideo view16Viewvideo5;
    @BindView(R.id.view16_viewvideo6)
    ViewVideo view16Viewvideo6;
    @BindView(R.id.view16_viewvideo7)
    ViewVideo view16Viewvideo7;
    @BindView(R.id.view16_viewvideo8)
    ViewVideo view16Viewvideo8;
    @BindView(R.id.view16_viewvideo9)
    ViewVideo view16Viewvideo9;
    @BindView(R.id.view16_viewvideo10)
    ViewVideo view16Viewvideo10;
    @BindView(R.id.view16_viewvideo11)
    ViewVideo view16Viewvideo11;
    @BindView(R.id.view16_viewvideo12)
    ViewVideo view16Viewvideo12;
    @BindView(R.id.view16_viewvideo13)
    ViewVideo view16Viewvideo13;
    @BindView(R.id.view16_viewvideo14)
    ViewVideo view16Viewvideo14;
    @BindView(R.id.view16_viewvideo15)
    ViewVideo view16Viewvideo15;
    @BindView(R.id.view16_viewvideo16)
    ViewVideo view16Viewvideo16;


    Unbinder unbinder;
    private Context context;
    private String uid1, uid2, uid3, uid4, uid5, uid6, uid7, uid8, uid9, uid10, uid11, uid12, uid13, uid14, uid15, uid16;
    private int view_id = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragmet_video4, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        view16Viewvideo1.setUid(uid1);
        view16Viewvideo2.setUid(uid2);
        view16Viewvideo3.setUid(uid3);
        view16Viewvideo4.setUid(uid4);
        view16Viewvideo5.setUid(uid5);
        view16Viewvideo6.setUid(uid6);
        view16Viewvideo7.setUid(uid7);
        view16Viewvideo8.setUid(uid8);
        view16Viewvideo9.setUid(uid9);
        view16Viewvideo10.setUid(uid10);
        view16Viewvideo11.setUid(uid11);
        view16Viewvideo12.setUid(uid12);
        view16Viewvideo13.setUid(uid13);
        view16Viewvideo14.setUid(uid14);
        view16Viewvideo15.setUid(uid15);
        view16Viewvideo16.setUid(uid16);

        view16Viewvideo1.setPost(0);
        view16Viewvideo2.setPost(1);
        view16Viewvideo3.setPost(2);
        view16Viewvideo4.setPost(3);
        view16Viewvideo5.setPost(4);
        view16Viewvideo6.setPost(5);
        view16Viewvideo7.setPost(6);
        view16Viewvideo8.setPost(7);
        view16Viewvideo9.setPost(8);
        view16Viewvideo10.setPost(9);
        view16Viewvideo11.setPost(10);
        view16Viewvideo12.setPost(11);
        view16Viewvideo13.setPost(12);
        view16Viewvideo14.setPost(13);
        view16Viewvideo15.setPost(14);
        view16Viewvideo16.setPost(15);


        view16Viewvideo1.setMushi(16);
        view16Viewvideo2.setMushi(16);
        view16Viewvideo3.setMushi(16);
        view16Viewvideo4.setMushi(16);
        view16Viewvideo5.setMushi(16);
        view16Viewvideo6.setMushi(16);
        view16Viewvideo7.setMushi(16);
        view16Viewvideo8.setMushi(16);
        view16Viewvideo9.setMushi(16);
        view16Viewvideo10.setMushi(16);
        view16Viewvideo11.setMushi(16);
        view16Viewvideo12.setMushi(16);
        view16Viewvideo13.setMushi(16);
        view16Viewvideo14.setMushi(16);
        view16Viewvideo15.setMushi(16);
        view16Viewvideo16.setMushi(16);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        video4_Start();
    }

    @Override
    public void onStop() {
        super.onStop();
        //停止播放
        video4_dime();
    }


    public void setUid(String u1, String u2, String u3, String u4, String u5, String u6, String u7, String u8, String u9
            , String u10, String u11, String u12, String u13, String u14, String u15, String u16) {

        uid1 = u1;
        uid2 = u2;
        uid3 = u3;
        uid4 = u4;
        uid5 = u5;
        uid6 = u6;
        uid7 = u7;
        uid8 = u8;
        uid9 = u9;
        uid10 = u10;
        uid11 = u11;
        uid12 = u12;
        uid13 = u13;
        uid14 = u14;
        uid15 = u15;
        uid16 = u16;

        if(view16Viewvideo1!=null){
            view16Viewvideo1.setUid2(uid1);
            view16Viewvideo2.setUid2(uid2);
            view16Viewvideo3.setUid2(uid3);
            view16Viewvideo4.setUid2(uid4);
            view16Viewvideo5.setUid2(uid5);
            view16Viewvideo6.setUid2(uid6);
            view16Viewvideo7.setUid2(uid7);
            view16Viewvideo8.setUid2(uid8);
            view16Viewvideo9.setUid2(uid9);
            view16Viewvideo10.setUid2(uid10);
            view16Viewvideo11.setUid2(uid11);
            view16Viewvideo12.setUid2(uid12);
            view16Viewvideo13.setUid2(uid13);
            view16Viewvideo14.setUid2(uid14);
            view16Viewvideo15.setUid2(uid15);
            view16Viewvideo16.setUid2(uid16);
        }

    }

    private void video4_Start() {
        view16Viewvideo1.setStart();
        view16Viewvideo2.setStart();
        view16Viewvideo3.setStart();
        view16Viewvideo4.setStart();
        view16Viewvideo5.setStart();
        view16Viewvideo6.setStart();
        view16Viewvideo7.setStart();
        view16Viewvideo8.setStart();
        view16Viewvideo9.setStart();
        view16Viewvideo10.setStart();
        view16Viewvideo11.setStart();
        view16Viewvideo12.setStart();
        view16Viewvideo13.setStart();
        view16Viewvideo14.setStart();
        view16Viewvideo15.setStart();
        view16Viewvideo16.setStart();
    }

    private void video4_dime() {
        if(view16Viewvideo1==null){
            return;
        }
        view16Viewvideo1.setdime();
        view16Viewvideo2.setdime();
        view16Viewvideo3.setdime();
        view16Viewvideo4.setdime();
        view16Viewvideo5.setdime();
        view16Viewvideo6.setdime();
        view16Viewvideo7.setdime();
        view16Viewvideo8.setdime();
        view16Viewvideo9.setdime();
        view16Viewvideo10.setdime();
        view16Viewvideo11.setdime();
        view16Viewvideo12.setdime();
        view16Viewvideo13.setdime();
        view16Viewvideo14.setdime();
        view16Viewvideo15.setdime();
        view16Viewvideo16.setdime();
    }

    public void video16_Start_P() {
        if(view16Viewvideo1==null){
            return;
        }
        view16Viewvideo1.setStart_p();
        view16Viewvideo2.setStart_p();
        view16Viewvideo3.setStart_p();
        view16Viewvideo4.setStart_p();
        view16Viewvideo5.setStart_p();
        view16Viewvideo6.setStart_p();
        view16Viewvideo7.setStart_p();
        view16Viewvideo8.setStart_p();
        view16Viewvideo9.setStart_p();
        view16Viewvideo10.setStart_p();
        view16Viewvideo11.setStart_p();
        view16Viewvideo12.setStart_p();
        view16Viewvideo13.setStart_p();
        view16Viewvideo14.setStart_p();
        view16Viewvideo15.setStart_p();
        view16Viewvideo16.setStart_p();
    }

    public void video16_Stop() {
        if(view16Viewvideo1==null){
            return;
        }
        view16Viewvideo1.setStop();
        view16Viewvideo2.setStop();
        view16Viewvideo3.setStop();
        view16Viewvideo4.setStop();
        view16Viewvideo5.setStop();
        view16Viewvideo6.setStop();
        view16Viewvideo7.setStop();
        view16Viewvideo8.setStop();
        view16Viewvideo9.setStop();
        view16Viewvideo10.setStop();
        view16Viewvideo11.setStop();
        view16Viewvideo12.setStop();
        view16Viewvideo13.setStop();
        view16Viewvideo14.setStop();
        view16Viewvideo15.setStop();
        view16Viewvideo16.setStop();
    }

    @OnClick({R.id.view16_viewvideo1, R.id.view16_viewvideo2, R.id.view16_viewvideo3, R.id.view16_viewvideo4, R.id.view16_viewvideo5, R.id.view16_viewvideo6, R.id.view16_viewvideo7, R.id.view16_viewvideo8, R.id.view16_viewvideo9, R.id.view16_viewvideo10, R.id.view16_viewvideo11, R.id.view16_viewvideo12, R.id.view16_viewvideo13, R.id.view16_viewvideo14, R.id.view16_viewvideo15, R.id.view16_viewvideo16})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view16_viewvideo1:
                view_id=1;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);


                break;
            case R.id.view16_viewvideo2:
                view_id=2;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo3:
                view_id=3;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo4:
                view_id=4;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo5:
                view_id=5;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo6:
                view_id=6;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo7:

                view_id=7;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo8:
                view_id=8;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo9:
                view_id=9;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo10:
                view_id=10;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo11:
                view_id=11;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo12:
                view_id=12;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo13:
                view_id=13;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo14:
                view_id=14;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo15:
                view_id=15;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view16_viewvideo16:
                view_id=16;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang);
                break;
        }
    }

    public int getPost(){  //获取当前选中那一个
        return view_id;
    }


    public void setB(int post){

        if (view16Viewvideo1==null) {
            return;
        }
        view_id=post+1;
        switch (post) {
            case 0:
                view_id=1;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);


                break;
            case 1:
                view_id=2;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 2:
                view_id=3;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 3:
                view_id=4;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 4:
                view_id=5;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 5:
                view_id=6;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 6:

                view_id=7;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 7:
                view_id=8;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 8:
                view_id=9;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 9:
                view_id=10;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 10:
                view_id=11;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 11:
                view_id=12;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 12:
                view_id=13;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 13:
                view_id=14;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 14:
                view_id=15;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang2);
                break;
            case 15:
                view_id=16;
                view16Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo10.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo11.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo12.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo13.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo14.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo15.setBackgroundResource(R.drawable.kuang2);
                view16Viewvideo16.setBackgroundResource(R.drawable.kuang);
                break;
        }
    }
}
