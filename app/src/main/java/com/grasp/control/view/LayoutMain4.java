package com.grasp.control.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grasp.control.R;

/**
 * Created by zhujingju on 2017/7/20.
 */

public class LayoutMain4 extends Fragment {


    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_main4,container,false);
        context=getActivity();
        return view;
    }
}
