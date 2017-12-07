package com.grasp.control.fragmet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.grasp.control.R;
import com.map.View.ConstantsForMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by iotbroad on 2017/11/22.
 */

public class ImageMap extends Fragment {


    @BindView(R.id.image_map_im)
    ImageView imageMapIm;
    Unbinder unbinder;
    private Context context;
    private String name = ConstantsForMap.File_MapPng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.image_map, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);


        if(fileIsExists(name)){
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(name);
                Bitmap bitmap  = BitmapFactory.decodeStream(fis);

                if(bitmap != null)imageMapIm.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return view;
    }

    //判断文件是否存在
    public boolean fileIsExists(String strFile) {
        try {

            File folder = new File(ConstantsForMap.FileFolder);
            if(!folder.exists()) folder.mkdirs();

            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
