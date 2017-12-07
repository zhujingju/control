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

public class FragmetVideo2 extends Fragment {


    @BindView(R.id.view4_viewvideo1)
    ViewVideo view4Viewvideo1;
    @BindView(R.id.view4_viewvideo2)
    ViewVideo view4Viewvideo2;
    @BindView(R.id.view4_viewvideo3)
    ViewVideo view4Viewvideo3;
    @BindView(R.id.view4_viewvideo4)
    ViewVideo view4Viewvideo4;
    Unbinder unbinder;
    private Context context;
    private String uid1, uid2, uid3, uid4;
    private int view_id=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragmet_video2, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        view4Viewvideo1.setUid(uid1);
        view4Viewvideo2.setUid(uid2);
        view4Viewvideo3.setUid(uid3);
        view4Viewvideo4.setUid(uid4);
        view4Viewvideo1.setPost(0);
        view4Viewvideo2.setPost(1);
        view4Viewvideo3.setPost(2);
        view4Viewvideo4.setPost(3);
        view4Viewvideo1.setMushi(4);
        view4Viewvideo2.setMushi(4);
        view4Viewvideo3.setMushi(4);
        view4Viewvideo4.setMushi(4);
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


    public void setUid(String u1, String u2, String u3, String u4) {
        uid1 = u1;
        uid2 = u2;
        uid3 = u3;
        uid4 = u4;

        if(view4Viewvideo1!=null){
            view4Viewvideo1.setUid2(uid1);
            view4Viewvideo2.setUid2(uid2);
            view4Viewvideo3.setUid2(uid3);
            view4Viewvideo4.setUid2(uid4);
        }


    }

    private void video4_Start() {
        if(view4Viewvideo1==null){
            return;
        }
        view4Viewvideo1.setStart();
        view4Viewvideo2.setStart();
        view4Viewvideo3.setStart();
        view4Viewvideo4.setStart();
    }

    private void video4_dime() {
        if(view4Viewvideo1==null){
            return;
        }
        view4Viewvideo1.setdime();
        view4Viewvideo2.setdime();
        view4Viewvideo3.setdime();
        view4Viewvideo4.setdime();
    }


    public void video4_Start_P() {
        if(view4Viewvideo1==null){
            return;
        }
        view4Viewvideo1.setStart_p();
        view4Viewvideo2.setStart_p();
        view4Viewvideo3.setStart_p();
        view4Viewvideo4.setStart_p();
    }

    public void video4_Stop() {
        if(view4Viewvideo1==null){
            return;
        }
        view4Viewvideo1.setStop();
        view4Viewvideo2.setStop();
        view4Viewvideo3.setStop();
        view4Viewvideo4.setStop();
    }

    @OnClick({R.id.view4_viewvideo1, R.id.view4_viewvideo2, R.id.view4_viewvideo3, R.id.view4_viewvideo4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view4_viewvideo1:
                view_id=1;
                view4Viewvideo1.setBackgroundResource(R.drawable.kuang);
                view4Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view4_viewvideo2:
                view_id=2;
                view4Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo2.setBackgroundResource(R.drawable.kuang);
                view4Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view4_viewvideo3:
                view_id=3;
                view4Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo3.setBackgroundResource(R.drawable.kuang);
                view4Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                break;
            case R.id.view4_viewvideo4:
                view_id=4;
                view4Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo4.setBackgroundResource(R.drawable.kuang);
                break;
        }
    }

    public int getPost(){  //获取当前选中那一个
        return view_id;
    }

    public void setB(int post){

        if (view4Viewvideo1==null) {
            return;
        }
        view_id=post+1;
        switch (post) {
            case 0:
                view4Viewvideo1.setBackgroundResource(R.drawable.kuang);
                view4Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                break;
            case 1:
                view4Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo2.setBackgroundResource(R.drawable.kuang);
                view4Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                break;
            case 2:
                view4Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo3.setBackgroundResource(R.drawable.kuang);
                view4Viewvideo4.setBackgroundResource(R.drawable.kuang2);
                break;
            case 3:
                view4Viewvideo1.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo2.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo3.setBackgroundResource(R.drawable.kuang2);
                view4Viewvideo4.setBackgroundResource(R.drawable.kuang);
                break;
        }
    }
}