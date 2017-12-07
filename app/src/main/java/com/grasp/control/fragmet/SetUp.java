package com.grasp.control.fragmet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.grasp.control.MainActivity;
import com.grasp.control.R;
import com.grasp.control.tool.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhujingju on 2017/9/20.
 */

public class SetUp extends Fragment {


    @BindView(R.id.ybd_tv)
    TextView ybdTv;
    @BindView(R.id.set_up_xx)
    Button setUpXx;
    Unbinder unbinder;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.set_up, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        if(MainActivity.Notification_zt){
            setUpXx.setBackgroundResource(R.drawable.autologin_on);
        }else{
            setUpXx.setBackgroundResource(R.drawable.autologin_off);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.set_up_xx)
    public void onViewClicked() {
        if(MainActivity.Notification_zt){
            setUpXx.setBackgroundResource(R.drawable.autologin_off);
        }else{
            setUpXx.setBackgroundResource(R.drawable.autologin_on);
        }
        MainActivity.Notification_zt=!MainActivity.Notification_zt;
        SharedPreferencesUtils.setParam(getContext(),"mian_notification",MainActivity.Notification_zt);
    }
}
