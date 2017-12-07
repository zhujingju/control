package com.grasp.control.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.grasp.control.MainActivity;
import com.grasp.control.R;
import com.grasp.control.tool.MediaFile;
import com.grasp.control.tool.MyApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhujingju on 2017/8/17.
 */

public class MyGridView extends RelativeLayout {


    MyGridViewView mygridview;
    private Context context;
    private myGriviewAdapter griview_adapter;
    private ArrayList<LayoutMain6.Goods_image> list;

    public MyGridView(Context context) {
        this(context, null, 0);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.mygridview, this);

        this.latout6Listener = MainActivity.latout6Listener;
        mygridview= (MyGridViewView) findViewById(R.id.mygridview);
        griview_adapter = new myGriviewAdapter(context, list);
        mygridview.setAdapter(griview_adapter);
        mygridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                if(LayoutMain6.bj_zt){
                    list.get(arg2).setDel_zt(!list.get(arg2).isDel_zt());
                    griview_adapter.notifyDataSetChanged();
                }else{  //进入看图模式
//                    if(MediaFile.isVideoFileType( list.get(arg2).getName())){ //视频
                        ArrayList<String> list_path=new ArrayList<String>();
                        ArrayList<String> list_name=new ArrayList<String>();
                        int num=0;
                        int n=0;
                        for(int i=0;i<LayoutMain6.list.size();i++){
                            for(int j=0;j<LayoutMain6.list.get(i).getG_list().size();j++){

//                                if(MediaFile.isVideoFileType( LayoutMain6.list.get(i).getG_list().get(j).getName())) { //视频
                                    list_path.add( LayoutMain6.list.get(i).getG_list().get(j).getPath());
                                    list_name.add( LayoutMain6.list.get(i).getG_list().get(j).getName());
                                    if(LayoutMain6.list.get(i).getG_list().get(j).getName().equals(list.get(arg2).getName())){
                                        num   =n;
                                    }

                                    n++;
//                                }


                            }
                        }
                        Log.d("qqq",num+"  ---");
                        latout6Listener.latout6Listener(num,list_name,list_path,true);
//                    }else{
//                        ArrayList<String> list_path=new ArrayList<String>();
//                        ArrayList<String> list_name=new ArrayList<String>();
//                        int num=0;
//                        int n=0;
//                        for(int i=0;i<LayoutMain6.list.size();i++){
//                            for(int j=0;j<LayoutMain6.list.get(i).getG_list().size();j++){
//
//                                if(!MediaFile.isVideoFileType( LayoutMain6.list.get(i).getG_list().get(j).getName())) { //视频
//                                    list_path.add( LayoutMain6.list.get(i).getG_list().get(j).getPath());
//                                    list_name.add( LayoutMain6.list.get(i).getG_list().get(j).getName());
//                                    if(LayoutMain6.list.get(i).getG_list().get(j).getName().equals(list.get(arg2).getName())){
//                                        num   =n;
//                                    }
//
//                                    n++;
//                                }else{
//
//
//                                }
//
//
//                            }
//                        }
//                        Log.d("qqq",num+"  ---");
//                        latout6Listener.latout6Listener(num,list_name,list_path,false);
//
//                    }

                }
            }
        });
    }



    public void setView(int post){
        list=LayoutMain6.list.get(post).getG_list();
        griview_adapter.setList(list);
        griview_adapter.notifyDataSetChanged();
    }

    private boolean lie_zt=false;

    public void setLie(boolean zt){
        if(zt){
            mygridview.setNumColumns(10);
        }else{
            mygridview.setNumColumns(5);
        }
        lie_zt=zt;
    }


    class myGriviewAdapter extends BaseAdapter {

        LayoutInflater inflater = null;
        private Context context;
        private List<LayoutMain6.Goods_image> list=null;

        public myGriviewAdapter(Context context,List<LayoutMain6.Goods_image> list) {
            this.context=context;
            this.list=list;
            inflater = ((Activity)(context)).getLayoutInflater();
        }


        public List<LayoutMain6.Goods_image> getList() {
            return list;
        }



        public void setList(List<LayoutMain6.Goods_image> list) {
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
        public LayoutMain6.Goods_image getItem(int arg0) {
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
            LayoutMain6.Goods_image camera= getItem(position);
            ListViewTool too;
            if(convertView==null){
                if(lie_zt){
                    view = inflater.inflate(R.layout.item_griview_image2, parent, false);
                }else{
                    view = inflater.inflate(R.layout.item_griview_image, parent, false);
                }

                too=new ListViewTool();
//
                too.im=(ImageView) view.findViewById(R.id.item_griview_image_im);
                too.del=(ImageView) view.findViewById(R.id.item_griview_image_del);
                too.sp=(ImageView) view.findViewById(R.id.item_griview_image_sp);

                view.setTag(too);
            }
            else {
                too = (ListViewTool) view.getTag();
            }
            if(camera == null) return null;
            ImageLoader.getInstance().displayImage("file://" + camera.getPath(), too.im, MyApplication.options);
            if(MediaFile.isVideoFileType( camera.getPath())){ //视频
                too.sp.setVisibility(VISIBLE);
            }else{
                too.sp.setVisibility(GONE);
            }

            if(camera.isDel_zt()){
                too.del.setVisibility(VISIBLE);
            }else{
                too.del.setVisibility(GONE);
            }
            return view;
        }




        class ListViewTool {
            public ImageView im ,del,sp;
        }
    }


    private Latout6Listener latout6Listener;


    public interface Latout6Listener {

        void latout6Listener(int post,ArrayList<String> name ,ArrayList<String> path,boolean zt);

    }

}
