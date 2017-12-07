package com.grasp.control.fragmet;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Player.Core.PlayerClient;
import com.grasp.control.MyInterface.AddListener;
import com.grasp.control.R;
import com.grasp.control.Umeye_sdk.Constants;
import com.grasp.control.Umeye_sdk.ShowProgress;
import com.grasp.control.sqlite.AddSQLiteHelper;
import com.grasp.control.sqlite.MySQLiteHelper;
import com.grasp.control.tool.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhujingju on 2017/8/30.
 */

public class AddEquipment extends Fragment {


    @BindView(R.id.r_a_qx)
    TextView rAQx;
    @BindView(R.id.r_a_qd)
    TextView rAQd;
    @BindView(R.id.r_a_zhong)
    TextView rAZhong;
//    @BindView(R.id.r_a_name)
    EditText rAName;
//    @BindView(R.id.r_a_pass)
    EditText rAPass;
    Unbinder unbinder;
    private Context context;
    private String name="";
    private String pass="";

    private PlayerClient client;
    private MyApplication appMain;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.addequipment, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        appMain = (MyApplication) ((Activity)context).getApplication();
        rAName= (EditText) view.findViewById(R.id.r_a_name);
        rAPass= (EditText) view.findViewById(R.id.r_a_pass);
        handler.sendEmptyMessage(1000);

        return view;
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacks(null);
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.r_a_qx, R.id.r_a_qd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.r_a_qx:
                if(name!=null){
                    rAPass.setText("");
                    rAName.setText("");
                }
                addListener.addListener();
                break;
            case R.id.r_a_qd:
                name=rAName.getText().toString();
                pass=rAPass.getText().toString();
                if(!name.equals("")&&!pass.equals("")){
                    getDevInfo();
                }else{
                    Toast.makeText(context,R.string.kong_err,Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }



    public ShowProgress pd;

    void getDevInfo() {  //获取参数
        if (pd == null) {
            pd = new ShowProgress(context);
            pd.setMessage(context.getResources().getString(
                    R.string.searching_device2));
            pd.setCanceledOnTouchOutside(true);
        }
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Constants.UMID = name;
//                Constants.user = Co;
                Constants.password = pass;
                Log.d("qqq",Constants.UMID+"  "+Constants.user+"  "  +Constants.password );
                client = appMain.getPlayerclient();// new
                String request = "{\"Operation\":1,\"Request_Type\":0}";
                Log.e("Test",request);
                byte[] b_request = request.getBytes();

                // byte[] result = client.CallCustomFunc(devId, 66052,
                // b_request);
                byte[] result = client.CallCustomFunc(Constants.UMID,
                        Constants.user, Constants.password, 66052, b_request);
                Log.e("Test", client+"  Constants.UMID ="+ Constants.UMID+" Constants.user="+Constants.user+" Constants.password="+Constants.password +" b_request="+b_request);
                Message msg = Message.obtain();
                msg.what = GET_FAIL;
                if (null != result) {
                    String reStr = new String(result);
                    // String reString = new String(Base64.decode(result,
                    // Base64.DEFAULT));// new
                    // String(result);
                    msg.what = GET_SUCCESS;
                    msg.obj = reStr;

                }
                handler.sendMessage(msg);
            }
        }).start();
    }



    public static final int GET_SUCCESS = 1;

    public static final int GET_FAIL = 2;

    public static final int SET_SUCCESS = 3;

    public static final int SET_FAIL = 4;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case GET_SUCCESS: //获取成功
                    Log.d("hhh","成功 "+msg.obj.toString());

                    try {
                        pd.dismiss();

                        JSONObject obj=new JSONObject(msg.obj.toString());
                        JSONObject array=obj.getJSONObject("value");
                        String sid=array.getString("id");


                        AddSQLiteHelper dbHelper = new AddSQLiteHelper(MyApplication.getContext(), "add.db", null, 1);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        String s="select * from AddEquipment where uid='"+name+"'";
//                        Log.d("qqq","s="+s);
                        Cursor cursor = db.rawQuery(s, null);
                        int num=0;
                        while (cursor.moveToNext()){
                            num++;
                        }
                        if (num==0) {
                            String s1="insert into AddEquipment(uid, sid,mindex) values('"+name+"','"+sid+"','-1')";
                            db.execSQL(s1);
                            if(name!=null){
                                rAPass.setText("");
                                rAName.setText("");
                            }
                            Toast.makeText(context,
                                    R.string.srmm2, Toast.LENGTH_SHORT).show();
                            addListener.addListener();
                        }else{
                            Toast.makeText(context,
                                    R.string.ybd, Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        pd.dismiss();
                        Toast.makeText(context,
                                R.string.srmm1, Toast.LENGTH_SHORT).show();
                    }


                    break;
                case GET_FAIL://
                    Log.d("hhh","err ");
                    pd.dismiss();
                    Toast.makeText(context,
                            R.string.srmm1, Toast.LENGTH_SHORT).show();
                    break;
//                case SET_SUCCESS://
//
//                    Toast.makeText(AcModifyDoorSetting.this,
//                            R.string.modify_success, Toast.LENGTH_SHORT).show();
//                    finish();
//                    break;
//                case SET_FAIL:
//                    Toast.makeText(AcModifyDoorSetting.this, R.string.set_fail,
//                            Toast.LENGTH_SHORT).show();
//                    break;
                case 1000:
                    rAName.setText(name);

                    break;


            }
            return false;
        }
    });

    private AddListener addListener;

    public void setAddListener(AddListener addListener) {
        this.addListener = addListener;
    }

    public void setName(String name) {
        this.name = name;
    }

}
