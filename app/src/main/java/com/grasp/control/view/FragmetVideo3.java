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

public class FragmetVideo3 extends Fragment {


    @BindView(R.id.view9_viewvideo1)
    ViewVideo view9Viewvideo1;
    @BindView(R.id.view9_viewvideo2)
    ViewVideo view9Viewvideo2;
    @BindView(R.id.view9_viewvideo3)
    ViewVideo view9Viewvideo3;
    @BindView(R.id.view9_viewvideo4)
    ViewVideo view9Viewvideo4;
    @BindView(R.id.view9_viewvideo5)
    ViewVideo view9Viewvideo5;
    @BindView(R.id.view9_viewvideo6)
    ViewVideo view9Viewvideo6;
    @BindView(R.id.view9_viewvideo7)
    ViewVideo view9Viewvideo7;
    @BindView(R.id.view9_viewvideo8)
    ViewVideo view9Viewvideo8;
    @BindView(R.id.view9_viewvideo9)
    ViewVideo view9Viewvideo9;
    Unbinder unbinder;
    private Context context;
    private String uid1, uid2, uid3, uid4, uid5, uid6, uid7, uid8, uid9;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragmet_video3, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        view9Viewvideo1.setUid(uid1);
        view9Viewvideo2.setUid(uid2);
        view9Viewvideo3.setUid(uid3);
        view9Viewvideo4.setUid(uid4);
        view9Viewvideo5.setUid(uid5);
        view9Viewvideo6.setUid(uid6);
        view9Viewvideo7.setUid(uid7);
        view9Viewvideo8.setUid(uid8);
        view9Viewvideo9.setUid(uid9);

        view9Viewvideo1.setPost(0);
        view9Viewvideo2.setPost(1);
        view9Viewvideo3.setPost(2);
        view9Viewvideo4.setPost(3);
        view9Viewvideo5.setPost(4);
        view9Viewvideo6.setPost(5);
        view9Viewvideo7.setPost(6);
        view9Viewvideo8.setPost(7);
        view9Viewvideo9.setPost(8);

        view9Viewvideo1.setMushi(9);
        view9Viewvideo2.setMushi(9);
        view9Viewvideo3.setMushi(9);
        view9Viewvideo4.setMushi(9);
        view9Viewvideo5.setMushi(9);
        view9Viewvideo6.setMushi(9);
        view9Viewvideo7.setMushi(9);
        view9Viewvideo8.setMushi(9);
        view9Viewvideo9.setMushi(9);
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


    public void setUid(String u1, String u2, String u3, String u4, String u5, String u6, String u7, String u8, String u9) {

        uid1 = u1;
        uid2 = u2;
        uid3 = u3;
        uid4 = u4;
        uid5 = u5;
        uid6 = u6;
        uid7 = u7;
        uid8 = u8;
        uid9 = u9;

        if(view9Viewvideo1!=null){
            view9Viewvideo1.setUid2(uid1);
            view9Viewvideo2.setUid2(uid2);
            view9Viewvideo3.setUid2(uid3);
            view9Viewvideo4.setUid2(uid4);
            view9Viewvideo5.setUid2(uid5);
            view9Viewvideo6.setUid2(uid6);
            view9Viewvideo7.setUid2(uid7);
            view9Viewvideo8.setUid2(uid8);
            view9Viewvideo9.setUid2(uid9);
        }


    }

    private void video4_Start() {
        if(view9Viewvideo1==null){
            return;
        }
        view9Viewvideo1.setStart();
        view9Viewvideo2.setStart();
        view9Viewvideo3.setStart();
        view9Viewvideo4.setStart();
        view9Viewvideo5.setStart();
        view9Viewvideo6.setStart();
        view9Viewvideo7.setStart();
        view9Viewvideo8.setStart();
        view9Viewvideo9.setStart();
    }

    private void video4_dime() {
        if(view9Viewvideo1==null){
            return;
        }
        view9Viewvideo1.setdime();
        view9Viewvideo2.setdime();
        view9Viewvideo3.setdime();
        view9Viewvideo4.setdime();
        view9Viewvideo5.setdime();
        view9Viewvideo6.setdime();
        view9Viewvideo7.setdime();
        view9Viewvideo8.setdime();
        view9Viewvideo9.setdime();
    }

    public void video9_Start_P() {
        if(view9Viewvideo1==null){
            return;
        }
        view9Viewvideo1.setStart_p();
        view9Viewvideo2.setStart_p();
        view9Viewvideo3.setStart_p();
        view9Viewvideo4.setStart_p();
        view9Viewvideo5.setStart_p();
        view9Viewvideo6.setStart_p();
        view9Viewvideo7.setStart_p();
        view9Viewvideo8.setStart_p();
        view9Viewvideo9.setStart_p();
    }

    public void video9_Stop() {
        if(view9Viewvideo1==null){
            return;
        }
        view9Viewvideo1.setStop();
        view9Viewvideo2.setStop();
        view9Viewvideo3.setStop();
        view9Viewvideo4.setStop();
        view9Viewvideo5.setStop();
        view9Viewvideo6.setStop();
        view9Viewvideo7.setStop();
        view9Viewvideo8.setStop();
        view9Viewvideo9.setStop();
    }

    private int view_id=1;
    @OnClick({R.id.view9_viewvideo1, R.id.view9_viewvideo2, R.id.view9_viewvideo3, R.id.view9_viewvideo4, R.id.view9_viewvideo5, R.id.view9_viewvideo6, R.id.view9_viewvideo7, R.id.view9_viewvideo8, R.id.view9_viewvideo9})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view9_viewvideo1:
                view_id=1;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view9_viewvideo2:
                view_id=2;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view9_viewvideo3:
                view_id=3;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view9_viewvideo4:
                view_id=4;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view9_viewvideo5:
                view_id=5;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view9_viewvideo6:
                view_id=6;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view9_viewvideo7:
                view_id=7;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view9_viewvideo8:
                view_id=8;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view9_viewvideo9:
                view_id=9;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang);
                break;
        }
    }

    public int getPost(){  //获取当前选中那一个
        return view_id;
    }

    public void setB(int post){

        if (view9Viewvideo1==null) {
            return;
        }
        view_id=post+1;
        switch (post) {
            case 0:
                view_id=1;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case 1:
                view_id=2;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case 2:
                view_id=3;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case 3:
                view_id=4;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case 4:
                view_id=5;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case 5:
                view_id=6;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case 6:
                view_id=7;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case 7:
                view_id=8;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang2);
                break;
            case 8:
                view_id=9;
                view9Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo5.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo6.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo7.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo8.setBackgroundResource(R.drawable.kuang2);
                view9Viewvideo9.setBackgroundResource(R.drawable.kuang);
                break;
        }
    }
}