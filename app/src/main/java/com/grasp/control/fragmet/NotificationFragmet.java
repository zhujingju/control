package com.grasp.control.fragmet;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grasp.control.MainActivity;
import com.grasp.control.R;
import com.grasp.control.Umeye_sdk.AcSearchDevice;
import com.grasp.control.Umeye_sdk.ShowProgress;
import com.grasp.control.dialog.SpotsDialog;
import com.grasp.control.sqlite.AddSQLiteHelper;
import com.grasp.control.tool.MyApplication;
import com.grasp.control.tool.Tool;
import com.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhujingju on 2017/9/18.
 */

public class NotificationFragmet extends Fragment {


    @BindView(R.id.notification_fragmet_listview)
    SwipeMenuListView listview;
    Unbinder unbinder;
    private Context context;
    private myListViewAdapter adapter;
    private List<Goods> list ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.notification_fragmet, container, false);
        context = getActivity();

        unbinder = ButterKnife.bind(this, view);
        progressDialog = new ShowProgress(context);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        initListview();
        return view;
    }

    @Override
    public void onDestroyView() {
        ha.removeCallbacks(null);
        super.onDestroyView();
        unbinder.unbind();
    }


    private void initListview() {
        list=new ArrayList<Goods>() ;
        listview.setCacheColorHint(0);
        adapter=new myListViewAdapter(context, list);
        dataListview();
        listview.setAdapter(adapter);
        listview.setonRefreshListener(new SwipeMenuListView.OnRefreshListener() { //刷新

            @Override
            public void onRefresh() {
                dataListview();

            }
        });



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Log.d("qqq",list.get(arg2).getIm_url());
                MainActivity.notificationListener.open(list.get(arg2).getIm_url());
            }
        });

    }



    private ShowProgress progressDialog;
    private List<Goods> toulist;

    public void dataListview() {  //获取list数据
        toulist=new ArrayList<>();
        new Thread(){
            @Override
            public void run() {

                AddSQLiteHelper dbHelper = new AddSQLiteHelper(context, "add.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String s="select * from AddEquipment ";
                Cursor cursor = db.rawQuery(s, null);
                while (cursor.moveToNext()) {

                    String uid = cursor.getString(1);//获取第2列的值
                    String sid = cursor.getString(2);//获取第3列的值
                    String index = cursor.getString(3);//获取第4列的值
                    Goods g=new Goods();
                    g.setName(uid);
                    g.setSid(sid);
                    toulist.add(g);

                }
                if( toulist.size()>0){

                    if(accesstoken.equals("")){
                        loginByPost();
                    }else{
                        Calendar calendar = Calendar.getInstance();
                        Calendar calendar3 = Calendar.getInstance();
                        calendar3.set(Calendar.DATE, calendar3.get(Calendar.DATE) -3);
                        getAlarmList(calendar3.getTime().getTime()+"",calendar.getTime().getTime()+"");
                    }

                }else{
                    ha.sendEmptyMessageDelayed(3004,0);
                }


            }
        }.start();
    }


    /**
     * 开发者申请的Appkey
     */
    private String appkey = "272a51a679e34ffba3542ed5b8c5c2de";
    private String appSecret = "9780f03a715ff013f336109683e369ce";
    private String accesstoken = "";

    public void getAlarmList(String startTime,String endTime ) {//第一次为true
        if (!Tool.isLianWang(context)) {
//            Toast.makeText(context, R.string.lianwang, Toast.LENGTH_SHORT).show();
            ha.sendEmptyMessageDelayed(3003,0);
        }

        if (accesstoken.equals("")) {
//            Toast.makeText(context, R.string.lianwang, Toast.LENGTH_SHORT).show();
            ha.sendEmptyMessageDelayed(3002,0);
        }
        String path = "https://open.ys7.com/api/lapp/alarm/list";
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            //数据准备
            String data="accessToken="+accesstoken+"&startTime="+startTime+"&endTime="+endTime+"&alarmType=-1&status=2&pageStart=0&pageSize=50";
            Log.d("qqq", "sssss " + data);
            //至少要设置的两个请求头
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", data.length() + "");

            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());

            //获得结果码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                //请求成功
                InputStream is = connection.getInputStream();
//                return IOSUtil.inputStream2String(is);
                Message m = new Message();
                String s = convertStreamToString(is);
                m.obj = s;
                m.what=3001;
                Log.d("qqq", "wwwwwwwwwwww " + s);
                if (s.equals("")) {
                    m.what=3002;
                }
                ha.sendMessage(m);
//                Log.d("qqq", m.obj.toString());
            } else {
                //请求失败
                Log.d("qqq", "xxxxx----------");
                ha.sendEmptyMessageDelayed(3002,0);
//                Toast.makeText(context, "请求失败错误码：" + responseCode, Toast.LENGTH_SHORT).show();
            }
        } catch (MalformedURLException e) {
            ha.sendEmptyMessageDelayed(3002,0);
            e.printStackTrace();
        } catch (ProtocolException e) {
            ha.sendEmptyMessageDelayed(3002,0);
            e.printStackTrace();
        } catch (IOException e) {
            ha.sendEmptyMessageDelayed(3002,0);
            e.printStackTrace();
        }
        Log.d("qqq", "xxxxx----------");
//        return null;
    }


    /**
     * post的方式请求
     *
     * @return 返回null 登录异常
     */
    public String loginByPost() {//第一次为true
        if (!Tool.isLianWang(context)) {
//            Toast.makeText(context, R.string.lianwang, Toast.LENGTH_SHORT).show();
            ha.sendEmptyMessage(3003);
            return null;
        }


        String path = "https://open.ys7.com/api/lapp/token/get";
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");

            //数据准备
            String data = "appKey=" + appkey + "&appSecret=" + appSecret;
            Log.d("qqq", "sssss " + data);
            //至少要设置的两个请求头
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", data.length() + "");

            //post的方式提交实际上是留的方式提交给服务器
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(data.getBytes());

            //获得结果码
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                //请求成功
                InputStream is = connection.getInputStream();
//                return IOSUtil.inputStream2String(is);
                Message m = new Message();
                    m.what = 100;
                String s = convertStreamToString(is);
                m.obj = s;
                Log.d("qqq", "wwwwwwwwwwww " + s);
                if (s.equals("")) {
                    ha.sendEmptyMessage(3002);
                    return null;
                }
                ha.sendMessage(m);
//                Log.d("qqq", m.obj.toString());
                return null;
            } else {
                //请求失败
                ha.sendEmptyMessage(3002);
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("qqq", "xxxxx----------");
        return null;
    }

    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();


        String line = null;

        try {

            while ((line = reader.readLine()) != null) {

                sb.append(line + "/n");

            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                is.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }


        return sb.toString();

    }


    Handler ha=new Handler() {
        String xx;

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {

                case 100:

                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        JSONObject json = jsonObject.getJSONObject("data");
                        accesstoken = json.getString("accessToken");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Calendar calendar = Calendar.getInstance();
                                Calendar calendar3 = Calendar.getInstance();
                                calendar3.set(Calendar.DATE, calendar3.get(Calendar.DATE) -3);
                                getAlarmList(calendar3.getTime().getTime()+"",calendar.getTime().getTime()+"");
                            }
                        }).start();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;


                case 3001:
                    Log.d("EZAlarmInfo","---++++  sj"+msg.obj.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        JSONArray json = jsonObject.getJSONArray("data");
                        if(json.length()>0){
                            for(int i=0;i<json.length();i++){
                                JSONObject jo=json.getJSONObject(i);
                                String alarmId=jo.getString("alarmId");
                                String alarmName=jo.getString("alarmName");
                                String alarmTime=jo.getString("alarmTime");
                                String alarmPicUrl=jo.getString("alarmPicUrl");
                                String deviceSerial=jo.getString("deviceSerial");
                                for(int j=0;j<toulist.size();j++){
                                    if(toulist.get(j).getSid().equals(deviceSerial)){
                                        Goods g=new Goods();
                                        g.setSid(deviceSerial);
                                        g.setIm_url(alarmPicUrl);
                                        Calendar ca=Calendar.getInstance();
                                        ca.setTimeInMillis(Long.valueOf(alarmTime));
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                                        String t=format.format(ca.getTime());
                                        g.setName("设备："+toulist.get(j).getName()+"  "+t);
                                        list.add(g);
                                    }
                                }
                            }

                        }else{

                        }
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        listview.onRefreshComplete();
                        //设置授权accesstoken


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;

                case 3002:
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(context,"获取数据失败",Toast.LENGTH_SHORT).show();
                    break;
                case 3003:
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(context,"请联网后重试",Toast.LENGTH_SHORT).show();
                    break;
                case 3004:
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    break;
            }
        }
    };

    class myListViewAdapter extends BaseAdapter {

        LayoutInflater inflater = null;
        private Context context;
        private List<Goods> list=null;

        public myListViewAdapter(Context context,List<Goods> list) {
            this.context=context;
            this.list=list;
            inflater = ((Activity)(context)).getLayoutInflater();
        }


        public List<Goods> getList() {
            return list;
        }



        public void setList(List<Goods> list) {
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
        public Goods getItem(int arg0) {
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
            Goods camera= getItem(position);
            ListViewTool too;
            if(convertView==null){
                view = inflater.inflate(R.layout.sw_item, parent, false);
                too=new ListViewTool();
//
                too.name=(TextView) view.findViewById(R.id.ybd_tv);
                too.last = (LinearLayout) view.findViewById(R.id.sw_item_last);


                view.setTag(too);
            }
            else {
                too = (ListViewTool) view.getTag();
            }
            if (camera == null) return null;
            too.name.setText(camera.getName());
            too.last.setBackgroundResource(R.drawable.d1);
            if (list.size() > 1) {
                if (position == list.size() - 1) {
                    too.last.setBackgroundResource(R.drawable.d2);
                } else {

                }
            } else {

                too.last.setBackgroundResource(R.drawable.d2);

            }

            return view;
        }

        class ListViewTool {

            public TextView name ;
            public LinearLayout last;
        }
    }


    class Goods {
        private String name;
        private String im_url;
        private String sid;

        public String getIm_url() {
            return im_url;
        }

        public void setIm_url(String im_url) {
            this.im_url = im_url;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}

