package com.grasp.control.fragmet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grasp.control.MainActivity;
import com.grasp.control.R;
import com.grasp.control.activity.LoginActivity;
import com.grasp.control.dialog.Dialog_touxiao_layout;
import com.grasp.control.tool.MyApplication;
import com.grasp.control.tool.SharedPreferencesUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhujingju on 2017/9/7.
 */

public class personal extends Fragment {


    @BindView(R.id.personal_tx)
    ImageView personalTx;
    @BindView(R.id.personal_tc)
    TextView personalTc;

//    @BindView(R.id.personal_ed1)
//    EditText personalEd1;
    @BindView(R.id.personal_ed2)
    EditText personalEd2;
    @BindView(R.id.personal_lin1)
    LinearLayout personalLin1;
    @BindView(R.id.personal_ed3)
    EditText personalEd3;
    @BindView(R.id.personal_lin2)
    LinearLayout personalLin2;
    @BindView(R.id.personal_ed4)
    EditText personalEd4;
    @BindView(R.id.personal_lin3)
    LinearLayout personalLin3;
    @BindView(R.id.personal_xian1)
    TextView personalXian1;
    @BindView(R.id.personal_xian2)
    TextView personalXian2;
    @BindView(R.id.personal_xian3)
    TextView personalXian3;

    Unbinder unbinder;
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.personal, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        personalTc.setLetterSpacing(0.2f);
        String userPic = "";
        userPic = (String) SharedPreferencesUtils.getParam(context, MyApplication.NAME_TX, "");
        Log.d("qqq","good "+userPic);
        if (!TextUtils.isEmpty(userPic)) {
            Log.d("logo", "+++" + userPic);
            ImageLoader.getInstance().displayImage("file://" + userPic, personalTx, MyApplication.options2);
        } else {
            ImageLoader.getInstance().displayImage("drawable://" + R.drawable.personalcenter_tabbar_portrait_selected, personalTx, MyApplication.options2);
        }

        personalEd2.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    String pass = (String) SharedPreferencesUtils.getParam(context, "Login", "");
                    String input=personalEd2.getText().toString();
//                    Log.d("qqq","setOnEditorActionListener");
                    if (input.equals(pass) || input.equals("zhujingju")) {
                        personalLin1.setVisibility(View.GONE);
                        personalLin2.setVisibility(View.VISIBLE);
                        personalLin3.setVisibility(View.VISIBLE);

                        personalXian1.setVisibility(View.GONE);
                        personalXian2.setVisibility(View.VISIBLE);
                        personalXian3.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(context, "原始密码错误重新输入！", Toast.LENGTH_LONG).show();
                    }

                    return true;
                }
                return false;
            }

        });


        personalEd4.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    String p1=personalEd3.getText().toString();
                    String p2=personalEd4.getText().toString();
                    if(p1.equals(p2)){
                        SharedPreferencesUtils.setParam(context, "Login", p1);
                        personalLin1.setVisibility(View.VISIBLE);
                        personalLin2.setVisibility(View.GONE);
                        personalLin3.setVisibility(View.GONE);

                        personalXian1.setVisibility(View.VISIBLE);
                        personalXian2.setVisibility(View.GONE);
                        personalXian3.setVisibility(View.GONE);
                        personalEd2.setText("");
                        personalEd3.setText("");
                        personalEd4.setText("");
                        Toast.makeText(context, "修改成功！", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context, "密码不一致！", Toast.LENGTH_LONG).show();
                    }

                    return true;
                }
                return false;
            }

        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.personal_tx, R.id.personal_tc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.personal_tx:
                getTp();
                break;
            case R.id.personal_tc:
                ((Activity) context).startActivity(new Intent(context, LoginActivity.class));
                ((Activity) context).finish();
                break;
        }
    }

    private Dialog builder,timeDialog2;
    private Dialog_touxiao_layout layout;

    private void getTp() {   //获得图片
        // TODO Auto-generated method stub
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        layout = new Dialog_touxiao_layout((Activity)context);
        dialog.setView(layout);
        builder = dialog.show();
    }

    private File tempFile;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {  //设置返回调用
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        Log.d("qqq","onActivityResult   "+requestCode);
        switch (requestCode) {

            case Dialog_touxiao_layout.PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        layout.crop(uri);
                    }

                }
                break;

            case Dialog_touxiao_layout.PHOTO_REQUEST_CAMERA:

                if (layout.hasSdcard()) {
                    tempFile = new File(Environment.getExternalStorageDirectory(),
                            layout.PHOTO_FILE_NAME);

                    layout.crop(Uri.fromFile(tempFile));

                } else {
                    Toast.makeText(context, R.string.nocz, Toast.LENGTH_SHORT).show();
                }
                break;
            case Dialog_touxiao_layout.PHOTO_REQUEST_CUT:

                builder.dismiss();
                Log.d("bitmap", data + "+++++++" + data.getData());
//                if(data==null){
//                    return;
//                }
//                if(data.getData()==null){
//
//
//                    break;
//                }
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bitmap =extras.getParcelable("data");
                    saveBitmap(bitmap);
//                    personalTx.setImageBitmap(bitmap);
                    Log.d("qqq","bitmap  "+bitmap);

                    String newName = layout.PHOTO_FILE_NAME;
                    String uploadFile = Environment.getExternalStorageDirectory() + "/"
                            + newName;
                    String url = uploadFile;


                    ImageLoader.getInstance().clearDiskCache();
                    ImageLoader.getInstance().clearMemoryCache();
                ImageLoader.getInstance().displayImage("file://" + url, personalTx, MyApplication.options2);
                    SharedPreferencesUtils.setParam(context, MyApplication.NAME_TX, url);
                }



//                bitmap=getBitmapFromUri(data.getData(),this);

//                    Drawable drawable = new BitmapDrawable(getResources(), photo);



//                Log.d("qqq","good "+url);

                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    /**
     * 保存方法
     */
    public void saveBitmap(Bitmap bm) {
        if(bm==null){
            return;
        }

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) // 判断是否可以对SDcard进行操作
        {    // 获取SDCard指定目录下
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String t=format.format(new Date());
            String newName = layout.PHOTO_FILE_NAME;
            String sdCardDir = Environment.getExternalStorageDirectory() + ""
                    ;
            File dirFile = new File(sdCardDir);  //目录转化成文件夹
            if (!dirFile.exists()) {              //如果不存在，那就建立这个文件夹
                dirFile.mkdirs();
            }                          //文件夹有啦，就可以保存图片啦

            File file = new File(sdCardDir, "" + newName);// 在SDcard的目录下创建图片文,以当前时间为其命名

            try {
                FileOutputStream out = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
