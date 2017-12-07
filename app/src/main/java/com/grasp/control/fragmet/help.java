package com.grasp.control.fragmet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grasp.control.R;

/**
 * Created by zhujingju on 2017/9/22.
 */

public class help extends Fragment {


    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.help, container, false);
        context = getActivity();
        return view;
    }
}
