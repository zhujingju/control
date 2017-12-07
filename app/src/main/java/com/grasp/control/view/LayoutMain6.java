package com.grasp.control.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grasp.control.MainActivity;
import com.grasp.control.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhujingju on 2017/7/20.
 */

public class LayoutMain6 extends Fragment {


    @BindView(R.id.layout6_qx)
    ImageView layout6Qx;
    @BindView(R.id.layout6_meu)
    ImageView layout6Meu;
    @BindView(R.id.layout6_del)
    ImageView layout6Del;
    Unbinder unbinder;
    MyListview layout6Listview;
    private Context context;
    public static List<Goods> list;
    private myListViewAdapter adapter;
    public static boolean bj_zt=false;
    private boolean lie_zt=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_main6, container, false);
        layout6Listview= (MyListview) view.findViewById(R.id.layout6_listview);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        layout6Qx.setBackgroundResource(R.drawable.image_icon_edit);
        bj_zt=false;
        list=null;

        if(lie_zt){
            layout6Meu.setBackgroundResource(R.drawable.image_icon_patterna);
//                    adapter.notifyDataSetChanged();
        }else{

            layout6Meu.setBackgroundResource(R.drawable.image_icon_patternb);
//                    adapter.notifyDataSetChanged();
        }
        initListview();
//        ImageLoader.getInstance().clearDiskCache();
//        ImageLoader.getInstance().clearMemoryCache();
        return view;
    }


    private void initListview() {
        layout6Listview.setCacheColorHint(0);
        adapter = new myListViewAdapter(context, list);
        dataListview();
        layout6Listview.setAdapter(adapter);
        layout6Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub

            }
        });

    }

    public void dataListview() {  //获取list数据

        new Thread() {
            @Override
            public void run() {
                getFileDir_sd(MainActivity.image_file_im);


            }
        }.start();
    }

    Handler ha = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    adapter.notifyDataSetChanged();
                    break;
            }

        }
    };

    public void getFileDir_sd(String filePath) {
        try {
            list = new ArrayList<>();
            File f = new File(filePath);

            List<File> fi = new ArrayList();
            File[] files= f.listFiles();// 列出所有文件
            // 将所有文件存入list中
            if (files != null) {
                int count = files.length;// 文件个数
                for (int i = 0; i < count; i++) {
                    File file = files[i];
                    fi.add(file);
                }
            }

            Collections.sort(fi, new Comparator< File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    return o1.getName().compareTo(o2.getName());
                }
            });

            if (files != null) {
                int count = files.length;// 文件个数
                for (int i = 0; i < fi.size(); i++) {
                    File file =fi.get(i);
                    Goods goods = new Goods();
                    goods.setName(file.getName());

                    goods.setPath(file.getPath());
                    Log.d("qqq",file.getName()+"  "+file.getPath());
                    goods.setG_list(getFileDir_sd2(file.getPath()));
                    list.add(goods);
                }

            }
            adapter.setList(list);
            ha.sendEmptyMessageDelayed(100, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Goods_image> getFileDir_sd2(String filePath) {
        try {
            ArrayList<Goods_image> g_list = new ArrayList<>();
            File f = new File(filePath);
            File[] files = f.listFiles();// 列出所有文件
            // 将所有文件存入list中
            if (files != null) {
                int count = files.length;// 文件个数
                for (int i = 0; i < count; i++) {
                    File file = files[i];
                    Goods_image goods = new Goods_image();
                    goods.setName(file.getName());
                    goods.setPath(file.getPath());
                    g_list.add(goods);
                }
            }

            return g_list;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        ha.removeCallbacks(null);
    }

    @OnClick({R.id.layout6_qx, R.id.layout6_meu, R.id.layout6_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout6_qx:
                if(bj_zt){
                    layout6Qx.setBackgroundResource(R.drawable.image_icon_edit);
                    layout6Del.setVisibility(View.GONE);
                    for(int i=0;i<list.size();i++){
                        for(int j=0;j<list.get(i).getG_list().size();j++){
                            list.get(i).getG_list().get(j).setDel_zt(false);
                        }
                    }
                    adapter.notifyDataSetChanged();

                }else{
                    layout6Qx.setBackgroundResource(R.drawable.image_icon_cancel);
                    layout6Del.setVisibility(View.VISIBLE);
                }
                bj_zt=!bj_zt;
                break;
            case R.id.layout6_meu:
                if(lie_zt){
                    layout6Meu.setBackgroundResource(R.drawable.image_icon_patterna);

                }else{
                    layout6Meu.setBackgroundResource(R.drawable.image_icon_patternb);
                }
                layout6Listview.setAdapter(null);
                layout6Listview.setAdapter(adapter);
                lie_zt=!lie_zt;
                break;
            case R.id.layout6_del:

                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(context);
                normalDialog.setTitle("删除");
                normalDialog.setMessage("是否删除");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for(int i=0;i<list.size();i++){
                                            for(int j=0;j<list.get(i).getG_list().size();j++){
                                                if(list.get(i).getG_list().get(j).isDel_zt()){

                                                    deleteFile(new File(list.get(i).getG_list().get(j).getPath()));
                                                }
                                            }
                                        }
                                        getFileDir_sd(MainActivity.image_file_im);
                                    }
                                }).start();
                            }
                        });
                normalDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //...To-do
                            }
                        });
                // 显示
                normalDialog.show();





                break;
        }
    }





    private static String sdState = Environment.getExternalStorageState();
    public static void  deleteFile(File file)
    {
        if(sdState.equals(Environment.MEDIA_MOUNTED))
        {
            if (file.exists())
            {
                if (file.isFile())
                {
                    file.delete();
                }
                // 如果它是一个目录
                else if (file.isDirectory())
                {
                    // 声明目录下所有的文件 files[];
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++)
                    { // 遍历目录下所有的文件
                        deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                    }
                }
                file.delete();
            }
        }
    }

    class myListViewAdapter extends BaseAdapter {

        LayoutInflater inflater = null;
        private Context context;
        private List<Goods> list = null;


        public myListViewAdapter(Context context, List<Goods> list) {
            this.context = context;
            this.list = list;
            inflater = ((Activity) (context)).getLayoutInflater();
        }


        public List<Goods> getList() {
            return list;
        }


        public void setList(List<Goods> list) {
            this.list = list;
        }


        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        public Goods getItem(int arg0) {
            if (list != null) {
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
            View view = convertView;
//            Goods camera = getItem(position);
            Goods camera =null;
            if (LayoutMain6.list != null) {
                camera = LayoutMain6.list.get(position);
            }
            ListViewTool too;
            if (convertView == null) {
                view = inflater.inflate(R.layout.item_image, parent, false);
                too = new ListViewTool();
//
                too.name = (TextView) view.findViewById(R.id.item_image_time);
                too.gridview = (MyGridView) view.findViewById(R.id.item_image_Gridview);


                view.setTag(too);
            } else {
                too = (ListViewTool) view.getTag();
            }
            if (camera == null) return null;
            too.name.setText(camera.getName());
//            griview_adapter.setList(camera.getG_list());
            too.gridview.setView(position);
            too.gridview.setLie(lie_zt);

            return view;
        }

        class ListViewTool {
            public MyGridView gridview;
            public TextView name;
        }
    }


    class Goods {
        private String name;
        private String path;
        private ArrayList<Goods_image> g_list;

        public ArrayList<Goods_image> getG_list() {
            return g_list;
        }

        public void setG_list(ArrayList<Goods_image> g_list) {
            this.g_list = g_list;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    class Goods_image {
        private String name;
        private String path;
        private boolean del_zt=false;

        public boolean isDel_zt() {
            return del_zt;
        }

        public void setDel_zt(boolean del_zt) {
            this.del_zt = del_zt;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
