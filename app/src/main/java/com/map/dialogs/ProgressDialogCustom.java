package com.map.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.grasp.control.R;


//import com.iot.remocon.R;

//import com.github.rosjava.android_remocons.a.R;

/**
 * Created by Administrator on 2017/3/30-030.
 */
public class ProgressDialogCustom  extends Dialog {
    public static final float TextSizeNone = -1f;
    public ProgressDialogCustom(Context context) {
        super(context);
    }

    public ProgressDialogCustom(Context context, int theme) {
        super(context, theme);
    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.id_progressDialogView);
        // 获取ImageView上的动画背景
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        // 开始动画
        spinner.start();
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.id_progressDialogMsg).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.id_progressDialogMsg);
            txt.setText(message);
            txt.invalidate();
        }
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context
     *            上下文
     * @param message
     *            提示
     * @param textSize 字体大小
     * @param cancelable
     *            是否按返回键取消
     * @param cancelListener
     *            按下返回键监听
     * @return
     */
    public static ProgressDialogCustom show(Context context, CharSequence message, float textSize, boolean cancelable, OnCancelListener cancelListener) {
        ProgressDialogCustom dialog = new ProgressDialogCustom(context, R.style.st_progressDialog);
        dialog.setTitle("");
        dialog.setContentView(R.layout.map_dialog_progress);
        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.id_progressDialogMsg).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.id_progressDialogMsg);
            txt.setText(message);
            if(textSize != TextSizeNone){
                txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
        }
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        // 监听返回键处理
        dialog.setOnCancelListener(cancelListener);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }

    /**
     * 弹出自定义ProgressDialog
     *
     * @param context
     *            上下文
     * @param cancelable
     *            是否按返回键取消
     * @param cancelListener
     *            按下返回键监听
     * @return
     */
    public static ProgressDialogCustom show(Context context, boolean cancelable, OnCancelListener cancelListener) {
        ProgressDialogCustom dialog = new ProgressDialogCustom(context, R.style.st_progressDialog);
        dialog.setTitle("");
        dialog.setContentView(R.layout.map_dialog_progress_notext);
//        if (message == null || message.length() == 0) {
//            dialog.findViewById(R.id.id_progressDialogMsg).setVisibility(View.GONE);
//        } else {
//            TextView txt = (TextView) dialog.findViewById(R.id.id_progressDialogMsg);
//            txt.setText(message);
//            if(textSize != TextSizeNone){
//                txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
//            }
//        }
        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        // 监听返回键处理
        dialog.setOnCancelListener(cancelListener);
        // 设置居中
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        // 设置背景层透明度
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        // dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }
}