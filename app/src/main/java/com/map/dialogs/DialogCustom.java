package com.map.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grasp.control.R;
import com.map.make_a_map.OccupancyGridLayerCustom;

import static com.map.dialogs.DialogCustom.ResultName.R_AddPosition;
import static com.map.dialogs.DialogCustom.ResultName.R_Cancel;
import static com.map.dialogs.DialogCustom.ResultName.R_DeletePosition;
import static com.map.dialogs.DialogCustom.ResultName.R_ModifyPosition;
import static com.map.dialogs.DialogCustom.ResultName.R_None;
import static com.map.dialogs.DialogCustom.ResultName.R_ReNewMap;
import static com.map.dialogs.DialogCustom.ResultName.R_ShowNotify;
import static com.map.dialogs.DialogCustom.ResultName.R_WifiNotSet;
import static com.map.dialogs.DialogCustom.ResultName.R_WifiSet;

/**
 * 显示各种对话框
 * Created by Administrator on 2017/8/28-028.
 */

public class DialogCustom {
    private final String TAG = "Print-Dia";
    /** 对话框返回的结果是什么 */
    public static enum ResultName {
        R_AddPosition, R_ModifyPosition, R_DeletePosition, R_ReNewMap, R_ShowNotify, R_WifiSet, R_WifiNotSet, R_None, R_Cancel
    }
    private static MyDialog dialogName;

    private static DialogCustom mDialogCustom;

    ///////////////////////////////////////////////////////////////
    /** 希望只创建一个对象 */
    public static DialogCustom getInstance() {
        if(mDialogCustom == null) mDialogCustom = new DialogCustom();
        return mDialogCustom;
    }

    /** 自定义位置名称 */
    public void showDialogCustomFlagName(final Context context, final OccupancyGridLayerCustom occupancyGridLayer, final Handler mHandler, final int handlerWhat){
        final DialogResult result = new DialogResult();
        View rootView = LayoutInflater.from(context).inflate(R.layout.map_dialog_addposition, null);
        final EditText id_dialogEt = (EditText)rootView.findViewById(R.id.id_dialogEt);
        Button id_dialogBtnOK = (Button)rootView.findViewById(R.id.id_dialogBtnOK);
        Button id_dialogBtnCancel = (Button)rootView.findViewById(R.id.id_dialogBtnCancel);

        // 适应屏幕
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidthPx = dm.widthPixels;
        int mScreenHeightPx = dm.heightPixels;
        int mModuleWidth = 1280;
        int mModuleHeight = 720;

        float rootWidth =  (context.getResources().getDimension(R.dimen.d_width_dialogRoot) / mModuleWidth * mScreenWidthPx); // 宽度
        float rootHeight = (context.getResources().getDimension(R.dimen.d_height_dialogRoot) / mModuleHeight * mScreenHeightPx); // 高度
        float etHeight2 = (context.getResources().getDimension(R.dimen.d_height_dialogEt) / mModuleHeight * mScreenHeightPx);


//        id_dialogEt.setLayoutParams(new AbsoluteLayout.LayoutParams((int) (getResources().getDimension(R.dimen.d_width_dialogEt) / mModuleWidth * mScreenWidthPx), (int) (getResources().getDimension(R.dimen.d_height_dialogEt) / mModuleHeight * mScreenHeightPx), (int) (getResources().getDimension(R.dimen.d_x_dialogEt) / mModuleWidth * mScreenWidthPx), (int) (getResources().getDimension(R.dimen.d_y_dialogEt) / mModuleHeight * mScreenHeightPx)));

        id_dialogEt.setLayoutParams(new AbsoluteLayout.LayoutParams((int) (context.getResources().getDimension(R.dimen.d_width_dialogEt) / mModuleWidth * mScreenWidthPx), (int) etHeight2, (int) (context.getResources().getDimension(R.dimen.d_x_dialogEt) / mModuleWidth * mScreenWidthPx), (int) (context.getResources().getDimension(R.dimen.d_y_dialogEt) / mModuleHeight * mScreenHeightPx)));
//        Log.i(TAG, "x_dialogEt=" + (context.getResources().getDimension(R.dimen.d_x_dialogEt) / mModuleWidth * mScreenWidthPx) +
//                ", y_dialogEt=" + context.getResources().getDimension(R.dimen.d_y_dialogEt) / mModuleHeight * mScreenHeightPx +
//                ", width_dialogEt="+context.getResources().getDimension(R.dimen.d_width_dialogEt) / mModuleWidth * mScreenWidthPx+
//                ", height_dialogEt="+context.getResources().getDimension(R.dimen.d_height_dialogEt) / mModuleHeight * mScreenHeightPx);
//
//        // 确定按钮左下边距必须相同
        float okMargin = context.getResources().getDimension(R.dimen.d_x_dialogBtnOK) / mModuleWidth * mScreenWidthPx;
        okMargin = Math.min(okMargin,
                context.getResources().getDimension(R.dimen.d_height_dialogRoot) / mModuleHeight * mScreenHeightPx -
                        context.getResources().getDimension(R.dimen.d_y_dialogBtnOK) / mModuleHeight * mScreenHeightPx -
                        context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx
        );
        float btnHeight2 = (context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx);

        id_dialogBtnOK.setLayoutParams(new AbsoluteLayout.LayoutParams((int) (context.getResources().getDimension(R.dimen.d_width_dialogBtnOK) / mModuleWidth * mScreenWidthPx), (int) (btnHeight2), (int) (okMargin),
                (int) (context.getResources().getDimension(R.dimen.d_height_dialogRoot) / mModuleHeight * mScreenHeightPx - context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx - okMargin)));

        id_dialogBtnCancel.setLayoutParams(new AbsoluteLayout.LayoutParams((int) (context.getResources().getDimension(R.dimen.d_width_dialogBtnCancel) / mModuleWidth * mScreenWidthPx), (int) (btnHeight2), (int) (context.getResources().getDimension(R.dimen.d_width_dialogRoot) / mModuleWidth * mScreenWidthPx -
                context.getResources().getDimension(R.dimen.d_width_dialogBtnCancel) / mModuleWidth * mScreenWidthPx -
                okMargin), (int) (context.getResources().getDimension(R.dimen.d_height_dialogRoot) / mModuleHeight * mScreenHeightPx - context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx - okMargin)));

        /** 界面字体大小 */
        float mTextSize;

        mTextSize = context.getResources().getDimension(R.dimen.d_textSize_dialogEt) / mModuleHeight *
                mScreenHeightPx;

        mTextSize = Math.min(mTextSize, (etHeight2 * context.getResources().getDimension(R.dimen.d_textSize_dialogEt)) / context.getResources().getDimension(R.dimen.d_height_dialogEt));

        id_dialogEt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        mTextSize = context.getResources().getDimension(R.dimen.d_textSize_dialogBtn) / mModuleHeight *
                mScreenHeightPx;
        mTextSize = Math.min(mTextSize, (btnHeight2 * context.getResources().getDimension(R.dimen.d_textSize_dialogBtn)) / context.getResources().getDimension(R.dimen.d_height_dialogBtnOK));
        id_dialogBtnOK.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        id_dialogBtnCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

//        final MyDialog dialogName = new MyDialog(RemoconAllActivity.this, 0, 0, rootView,
//                R.style.dialog); // cx, R.style.dialog
        dialogName = new MyDialog(context, 0, 0, rootView,
                R.style.dialog); // cx, R.style.dialog

        id_dialogBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id_dialogEt.getText().toString().trim().equals("")) {
//                    safeShowNotiDialogCustom(getString(R.string.s_customNameInputHint));
                    result.setResult(R_ShowNotify);
                    result.setMessage(context.getString(R.string.s_customNameInputHint));
                    mHandler.obtainMessage(handlerWhat, result).sendToTarget();
                }
                else if(!isChineseCharacter(id_dialogEt.getText().toString().trim())){
//                    safeShowNotiDialogCustom(getString(R.string.s_Error_PleaseInputChinese)); //请输入中文
                    result.setResult(R_ShowNotify);
                    result.setMessage(context.getString(R.string.s_Error_PleaseInputChinese));
                    mHandler.obtainMessage(handlerWhat, result).sendToTarget();
                } else if(id_dialogEt.getText().toString().trim().length() > 4){
//                    safeShowNotiDialogCustom(getString(R.string.s_Error_InputDontMoreThan4));
                    result.setResult(R_ShowNotify);
                    result.setMessage(context.getString(R.string.s_Error_InputDontMoreThan4));
                    mHandler.obtainMessage(handlerWhat, result).sendToTarget();
                }
                else if(!occupancyGridLayer.isNewPositionName(id_dialogEt.getText().toString())){
//                    safeShowNotiDialogCustom(getString(R.string.s_positionExists)); // 该位置已存在，不可重复标记
                    result.setResult(R_ShowNotify);
                    result.setMessage(context.getString(R.string.s_positionExists));
                    mHandler.obtainMessage(handlerWhat, result).sendToTarget();
                }
                else {
//                    addPosition(id_dialogEt.getText().toString());
                    result.setResult(R_AddPosition);
                    result.setMessage(id_dialogEt.getText().toString());
                    mHandler.obtainMessage(handlerWhat, result).sendToTarget();
                    dialogName.dismiss();
                    dialogName = null;
                }
            }
        });
        id_dialogBtnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                result.setResult(R_Cancel);
                mHandler.obtainMessage(handlerWhat, result).sendToTarget();
                dialogName.dismiss();
                dialogName = null;
            }
        });

        Window dialogWindow = dialogName.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int)rootWidth; // 宽度
        lp.height = (int)rootHeight; // 高度

        dialogWindow.setAttributes(lp);

        if(!((Activity)context).isFinishing())dialogName.show(); // E/WindowManager: android.view.WindowLeaked: Activity com.iot.remocon.RemoconAllActivity has leaked window com.android.internal.policy.PhoneWindow$DecorView{3c8ef37 V.E...... R.....ID 0,0-640,256} that was originally added here

        if (!dialogName.isShowing()) {
            dialogName.dismiss();
            dialogName = null;
        }
//        Log.i(TAG, "alert.isShowing():" + dialogName.isShowing() + "\t lp.width="+lp.width + "\t lp.height=" + lp.height);
//        return result;
    }

    /** 判断是不是中文 */
    private final boolean isChineseCharacter(String chineseStr) {
        Log.i(TAG, "isChineseCharacter()");
        char[] charArray = chineseStr.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if ((charArray[i] >= 0x4e00) && (charArray[i] <= 0x9fbb)) {
                return true;
            }
        }
        return false;
    }

    /** 修改标记名称 */
    public void showDialogModifyFlagName(final Context context, final String positionId, final String positionOldName, final OccupancyGridLayerCustom occupancyGridLayer, final Handler mHandler, final int handlerWhat){

        final DialogResult result = new DialogResult();
        result.setPositionId(positionId);
        result.setPositionOldName(positionOldName);
        View rootView = LayoutInflater.from(context).inflate(R.layout.map_dialog_modifyposition, null);
        final EditText id_dialogOldName = (EditText)rootView.findViewById(R.id.id_dialogOldName);
        final EditText id_dialogNewName = (EditText)rootView.findViewById(R.id.id_dialogNewName);
        Button id_dialogBtnDelete_modifyName = (Button)rootView.findViewById(R.id.id_dialogBtnDelete_modifyName);
        Button id_dialogBtnOK_modifyName = (Button)rootView.findViewById(R.id.id_dialogBtnOK_modifyName);
        Button id_dialogBtnCancel_modifyName = (Button)rootView.findViewById(R.id.id_dialogBtnCancel_modifyName);

        // 适应屏幕
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidthPx = dm.widthPixels;
        int mScreenHeightPx = dm.heightPixels;
        int mModuleWidth = 1280;
        int mModuleHeight = 720;

        float rootWidth =  (context.getResources().getDimension(R.dimen.d_width_dialogRoot_modifyName) / mModuleWidth * mScreenWidthPx); // 宽度
        float rootHeight = (context.getResources().getDimension(R.dimen.d_height_dialogRoot_modifyName) / mModuleHeight * mScreenHeightPx); // 高度
        float etHeight2 = (context.getResources().getDimension(R.dimen.d_height_dialogEt) / mModuleHeight * mScreenHeightPx);

        id_dialogOldName.setLayoutParams(new AbsoluteLayout.LayoutParams((int) (context.getResources().getDimension(R.dimen.d_width_dialogEt) / mModuleWidth * mScreenWidthPx), (int) etHeight2, (int) (context.getResources().getDimension(R.dimen.d_x_dialogOldName) / mModuleWidth * mScreenWidthPx), (int) (context.getResources().getDimension(R.dimen.d_y_dialogOldName) / mModuleHeight * mScreenHeightPx)));

        id_dialogNewName.setLayoutParams(new AbsoluteLayout.LayoutParams((int) (context.getResources().getDimension(R.dimen.d_width_dialogEt) / mModuleWidth * mScreenWidthPx), (int) etHeight2, (int) (context.getResources().getDimension(R.dimen.d_x_dialogNewName) / mModuleWidth * mScreenWidthPx), (int) (context.getResources().getDimension(R.dimen.d_y_dialogNewName) / mModuleHeight * mScreenHeightPx)));

//        // 确定按钮左下边距必须相同
        float okMargin = context.getResources().getDimension(R.dimen.d_x_dialogBtnOK_modifyName) / mModuleWidth * mScreenWidthPx;
        okMargin = Math.min(okMargin,
                context.getResources().getDimension(R.dimen.d_height_dialogRoot_modifyName) / mModuleHeight * mScreenHeightPx -
                        context.getResources().getDimension(R.dimen.d_y_dialogBtnOK_modifyName) / mModuleHeight * mScreenHeightPx -
                        context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx
        );
        float btnHeight2 = (context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx);

        float xGap = context.getResources().getDimension(R.dimen.d_width_dialogRoot_modifyName) / mModuleWidth * mScreenWidthPx -
                context.getResources().getDimension(R.dimen.d_width_dialogBtnDelete) / mModuleWidth * mScreenWidthPx -
                okMargin -
                okMargin -
                context.getResources().getDimension(R.dimen.d_width_dialogBtnOK) / mModuleWidth * mScreenWidthPx;

        id_dialogBtnOK_modifyName.setLayoutParams(new AbsoluteLayout.LayoutParams((int) (context.getResources().getDimension(R.dimen.d_width_dialogBtnOK) / mModuleWidth * mScreenWidthPx), (int) (btnHeight2), (int) (okMargin),
                (int) (context.getResources().getDimension(R.dimen.d_height_dialogRoot_modifyName) / mModuleHeight * mScreenHeightPx - (context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx) * 2 - okMargin - xGap)));


        id_dialogBtnDelete_modifyName.setLayoutParams(new AbsoluteLayout.LayoutParams((int) (context.getResources().getDimension(R.dimen.d_width_dialogBtnDelete) / mModuleWidth * mScreenWidthPx), (int) (btnHeight2), (int) (context.getResources().getDimension(R.dimen.d_width_dialogRoot_modifyName) / mModuleWidth * mScreenWidthPx -
                context.getResources().getDimension(R.dimen.d_width_dialogBtnDelete) / mModuleWidth * mScreenWidthPx -
                okMargin), (int) (context.getResources().getDimension(R.dimen.d_height_dialogRoot_modifyName) / mModuleHeight * mScreenHeightPx - (context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx) * 2 - okMargin - xGap)));


        id_dialogBtnCancel_modifyName.setLayoutParams(new AbsoluteLayout.LayoutParams((int) (context.getResources().getDimension(R.dimen.d_width_dialogBtnCancel_modifyName) / mModuleWidth * mScreenWidthPx), (int) (btnHeight2), (int) (okMargin), (int) (context.getResources().getDimension(R.dimen.d_height_dialogRoot_modifyName) / mModuleHeight * mScreenHeightPx - context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx - okMargin)));

        /** 界面字体大小 */
        float mTextSize;

        mTextSize = context.getResources().getDimension(R.dimen.d_textSize_dialogEt) / mModuleHeight *
                mScreenHeightPx;

        mTextSize = Math.min(mTextSize, (etHeight2 * context.getResources().getDimension(R.dimen.d_textSize_dialogEt)) / context.getResources().getDimension(R.dimen.d_height_dialogEt));

        id_dialogOldName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        id_dialogNewName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        mTextSize = context.getResources().getDimension(R.dimen.d_textSize_dialogBtn) / mModuleHeight *
                mScreenHeightPx;
        mTextSize = Math.min(mTextSize, (btnHeight2 * context.getResources().getDimension(R.dimen.d_textSize_dialogBtn)) / context.getResources().getDimension(R.dimen.d_height_dialogBtnOK));
        id_dialogBtnOK_modifyName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        id_dialogBtnDelete_modifyName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        id_dialogBtnCancel_modifyName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        id_dialogOldName.setText(positionOldName);
        dialogName = new MyDialog(context, 0, 0, rootView,
                R.style.dialog); // cx, R.style.dialog

        id_dialogBtnOK_modifyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id_dialogNewName.getText().toString().trim().equals("")) {
//                    safeShowNotiDialogCustom(getString(R.string.s_hintNewFlagName));
                    result.setResult(R_ShowNotify);
                    result.setMessage(context.getString(R.string.s_hintNewFlagName));
                    mHandler.obtainMessage(handlerWhat, result).sendToTarget();
                }
                else if(!isChineseCharacter(id_dialogNewName.getText().toString().trim())){
//                    safeShowNotiDialogCustom(getString(R.string.s_Error_PleaseInputChinese)); //请输入中文
                    result.setResult(R_ShowNotify);
                    result.setMessage(context.getString(R.string.s_Error_PleaseInputChinese));
                    mHandler.obtainMessage(handlerWhat, result).sendToTarget();
                } else if(id_dialogNewName.getText().toString().trim().length() > 4){
//                    safeShowNotiDialogCustom(getString(R.string.s_Error_InputDontMoreThan4));
                    result.setResult(R_ShowNotify);
                    result.setMessage(context.getString(R.string.s_Error_InputDontMoreThan4));
                    mHandler.obtainMessage(handlerWhat, result).sendToTarget();
                }
                else if(!occupancyGridLayer.isNewPositionName(id_dialogNewName.getText().toString())){
//                    safeShowNotiDialogCustom(getString(R.string.s_positionExists)); // 该位置已存在，不可重复标记
                    result.setResult(R_ShowNotify);
                    result.setMessage(context.getString(R.string.s_positionExists));
                    mHandler.obtainMessage(handlerWhat, result).sendToTarget();
                }
                else {
//                    modifyPosition(positionId, id_dialogNewName.getText().toString());
                    result.setResult(R_ModifyPosition);
                    result.setMessage(id_dialogNewName.getText().toString());
                    mHandler.obtainMessage(handlerWhat, result).sendToTarget();
                    dialogName.dismiss();
                    dialogName = null;
                }
            }
        });
        id_dialogBtnDelete_modifyName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                deletePosition(positionId);
                result.setResult(R_DeletePosition);
                mHandler.obtainMessage(handlerWhat, result).sendToTarget();
                dialogName.dismiss();
                dialogName = null;
            }
        });
        id_dialogBtnCancel_modifyName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                alert.dismiss();
                result.setResult(R_Cancel);
                mHandler.obtainMessage(handlerWhat, result).sendToTarget();
                dialogName.dismiss();
                dialogName = null;
            }
        });

        Window dialogWindow = dialogName.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int)rootWidth; // 宽度
        lp.height = (int)rootHeight; // 高度

        dialogWindow.setAttributes(lp);

        if(!((Activity)context).isFinishing())dialogName.show(); // E/WindowManager: android.view.WindowLeaked: Activity com.iot.remocon.RemoconAllActivity has leaked window com.android.internal.policy.PhoneWindow$DecorView{3c8ef37 V.E...... R.....ID 0,0-640,256} that was originally added here

        if (!dialogName.isShowing()) {
            dialogName.dismiss();
            dialogName = null;
        }

//        return result;
    }

    /** 是否删除已有地图 */
    public void showDialogIfNewMap(final Context context, final Handler mHandler, final int handlerWhat){
        final DialogResult result = new DialogResult();

        View rootView = LayoutInflater.from(context).inflate(R.layout.map_dialog_okorcancel, null);
        final TextView id_dialogEt = (TextView)rootView.findViewById(R.id.id_dialogEt);
        Button id_dialogBtnOK = (Button)rootView.findViewById(R.id.id_dialogBtnOK);
        Button id_dialogBtnCancel = (Button)rootView.findViewById(R.id.id_dialogBtnCancel);

        id_dialogEt.setText(R.string.s_isNewMap);
        // 适应屏幕
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidthPx = dm.widthPixels;
        int mScreenHeightPx = dm.heightPixels;
        int mModuleWidth = 1280;
        int mModuleHeight = 720;

        float rootWidth =  (context.getResources().getDimension(R.dimen.d_width_dialogRoot) / mModuleWidth * mScreenWidthPx); // 宽度
        float rootHeight = (context.getResources().getDimension(R.dimen.d_height_dialogRoot) / mModuleHeight * mScreenHeightPx); // 高度
        float etHeight2 = (context.getResources().getDimension(R.dimen.d_height_dialogEt) / mModuleHeight * mScreenHeightPx);

        id_dialogEt.setLayoutParams(new AbsoluteLayout.LayoutParams((int) (context.getResources().getDimension(R.dimen.d_width_dialogEt) / mModuleWidth * mScreenWidthPx), (int) etHeight2, (int) (context.getResources().getDimension(R.dimen.d_x_dialogEt) / mModuleWidth * mScreenWidthPx), (int) (context.getResources().getDimension(R.dimen.d_y_dialogEt) / mModuleHeight * mScreenHeightPx)));
//        // 确定按钮左下边距必须相同
        float okMargin = context.getResources().getDimension(R.dimen.d_x_dialogBtnOK) / mModuleWidth * mScreenWidthPx;
        okMargin = Math.min(okMargin,
                context.getResources().getDimension(R.dimen.d_height_dialogRoot) / mModuleHeight * mScreenHeightPx -
                        context.getResources().getDimension(R.dimen.d_y_dialogBtnOK) / mModuleHeight * mScreenHeightPx -
                        context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx
        );
        float btnHeight2 = (context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx);

        id_dialogBtnOK.setLayoutParams(new AbsoluteLayout.LayoutParams((int) (context.getResources().getDimension(R.dimen.d_width_dialogBtnOK) / mModuleWidth * mScreenWidthPx), (int) (btnHeight2), (int) (okMargin),
                (int) (context.getResources().getDimension(R.dimen.d_height_dialogRoot) / mModuleHeight * mScreenHeightPx - context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx - okMargin)));

        id_dialogBtnCancel.setLayoutParams(new AbsoluteLayout.LayoutParams((int) (context.getResources().getDimension(R.dimen.d_width_dialogBtnCancel) / mModuleWidth * mScreenWidthPx), (int) (btnHeight2), (int) (context.getResources().getDimension(R.dimen.d_width_dialogRoot) / mModuleWidth * mScreenWidthPx -
                context.getResources().getDimension(R.dimen.d_width_dialogBtnCancel) / mModuleWidth * mScreenWidthPx -
                okMargin), (int) (context.getResources().getDimension(R.dimen.d_height_dialogRoot) / mModuleHeight * mScreenHeightPx - context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx - okMargin)));

        /** 界面字体大小 */
        float mTextSize;

        mTextSize = context.getResources().getDimension(R.dimen.d_textSize_dialogEt) / mModuleHeight *
                mScreenHeightPx;

        mTextSize = Math.min(mTextSize, (etHeight2 * context.getResources().getDimension(R.dimen.d_textSize_dialogEt)) / context.getResources().getDimension(R.dimen.d_height_dialogEt));

        id_dialogEt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        mTextSize = context.getResources().getDimension(R.dimen.d_textSize_dialogBtn) / mModuleHeight *
                mScreenHeightPx;
        mTextSize = Math.min(mTextSize, (btnHeight2 * context.getResources().getDimension(R.dimen.d_textSize_dialogBtn)) / context.getResources().getDimension(R.dimen.d_height_dialogBtnOK));
        id_dialogBtnOK.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        id_dialogBtnCancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        final MyDialog dialog = new MyDialog(context, 0, 0, rootView,
                R.style.dialog); // cx, R.style.dialog
        id_dialogBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                isNewMap = true;
                Log.i(TAG, "onClick: 确定重启构建地图");
                dialog.dismiss();

//                status.setNextAction(status.Action_ReNewMap);
//                stopRapp(status.Action_ReNewMap);
                result.setResult(R_ReNewMap);
                mHandler.obtainMessage(handlerWhat, result).sendToTarget();
            }
        });
        id_dialogBtnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                alert.dismiss();
//                isNewMap = false;
                Log.i(TAG, "onClick: 不重启构建地图");
                result.setResult(R_Cancel);
                mHandler.obtainMessage(handlerWhat, result).sendToTarget();

                dialog.dismiss();
            }
        });

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int)rootWidth; // 宽度
        lp.height = (int)rootHeight; // 高度

        dialogWindow.setAttributes(lp);

        dialog.show();

        if (!dialog.isShowing()) {
            dialog.dismiss();
        }
        Log.i(TAG, "alert.isShowing():" + dialog.isShowing() + "\t lp.width="+lp.width + "\t lp.height=" + lp.height );
//        return result;
    }

    /**
     * 提示对话框，只显示消息和确定按钮，其实还不如用toast显示。
     */
    public Dialog getNotifyDialog(final Context context, final String message) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.map_dialog_notify, null);
        final TextView id_dialogMessage = (TextView)rootView.findViewById(R.id.id_dialogMessage);
        Button id_dialogBtnOK = (Button)rootView.findViewById(R.id.id_dialogBtnOK);

        id_dialogMessage.setVisibility(View.VISIBLE);

        // 适应屏幕
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidthPx = dm.widthPixels;
        int mScreenHeightPx = dm.heightPixels;
        int mModuleWidth = 1280;
        int mModuleHeight = 720;

        float rootWidth =  (context.getResources().getDimension(R.dimen.d_width_dialogRoot) / mModuleWidth * mScreenWidthPx); // 宽度
        float rootHeight = (context.getResources().getDimension(R.dimen.d_height_dialogRoot) / mModuleHeight * mScreenHeightPx); // 高度

        float tvHeight2 = context.getResources().getDimension(R.dimen.d_height_dialogEt) / mModuleHeight * mScreenHeightPx;

        LinearLayout.LayoutParams lpMsg = (LinearLayout.LayoutParams)id_dialogMessage.getLayoutParams();

        int marginLeft = (int) (context.getResources().getDimension(R.dimen.d_marginLeft_et) / mModuleWidth * mScreenWidthPx);
        int marginTop = (int) (context.getResources().getDimension(R.dimen.d_marginTop_et) / mModuleHeight * mScreenHeightPx);

        lpMsg.width = (int) (context.getResources().getDimension(R.dimen.d_width_dialogEt) / mModuleWidth * mScreenWidthPx);
        lpMsg.height = LinearLayout.LayoutParams.WRAP_CONTENT; // (int) (tvHeight2);
        lpMsg.setMargins(marginLeft, marginTop, marginLeft, marginTop);
        id_dialogMessage.setLayoutParams(lpMsg);

//        // 确定按钮左下边距必须相同
        float okMargin = context.getResources().getDimension(R.dimen.d_x_dialogBtnOK) / mModuleWidth * mScreenWidthPx;
        okMargin = Math.min(okMargin,
                context.getResources().getDimension(R.dimen.d_height_dialogRoot) / mModuleHeight * mScreenHeightPx -
                        context.getResources().getDimension(R.dimen.d_y_dialogBtnOK) / mModuleHeight * mScreenHeightPx -
                        context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx
        );
        float btnHeight2 = context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx;

        lpMsg = (LinearLayout.LayoutParams)id_dialogBtnOK.getLayoutParams();
        int marginBottom = (int) (context.getResources().getDimension(R.dimen.d_marginBottom_btn) / mModuleHeight * mScreenHeightPx);

        lpMsg.width = (int) (context.getResources().getDimension(R.dimen.d_width_dialogBtnOK) / mModuleWidth * mScreenWidthPx);
        lpMsg.height = (int) (context.getResources().getDimension(R.dimen.d_height_dialogBtnOK) / mModuleHeight * mScreenHeightPx); // (int) (tvHeight2);
        lpMsg.bottomMargin = marginBottom;
        lpMsg.gravity = Gravity.CENTER_HORIZONTAL;
        id_dialogBtnOK.setLayoutParams(lpMsg);


        /** 界面字体大小 */
        float mTextSize;

        mTextSize = context.getResources().getDimension(R.dimen.d_textSize_dialogEt) / mModuleHeight *
                mScreenHeightPx;
        mTextSize = Math.min(mTextSize, (tvHeight2 * context.getResources().getDimension(R.dimen.d_textSize_dialogEt)) / context.getResources().getDimension(R.dimen.d_height_dialogEt));
        id_dialogMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        mTextSize = context.getResources().getDimension(R.dimen.d_textSize_dialogBtn) / mModuleHeight *
                mScreenHeightPx;
        mTextSize = Math.min(mTextSize, (btnHeight2 * context.getResources().getDimension(R.dimen.d_textSize_dialogBtn)) / context.getResources().getDimension(R.dimen.d_height_dialogBtnOK));
        id_dialogBtnOK.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        id_dialogMessage.setText(message);
        final MyDialog dialog = new MyDialog(context, 0, 0, rootView,
                R.style.dialog); // cx, R.style.dialog
        id_dialogBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int)rootWidth; // 宽度
//        lp.height = (int)(rootHeight + extendsHeight); // 高度
        lp.height =  LinearLayout.LayoutParams.WRAP_CONTENT; // 高度

        dialogWindow.setAttributes(lp);

        return dialog;
    }

    /** 修改wifi */
    public void showDialogSetWifi(final Context context, final String mWifiName, final String mWifiPwd, final Handler mHandler, final int handlerWhat){ // 增加按钮：不设置wifi

        final DialogResult result = new DialogResult();

        View rootView = LayoutInflater.from(context).inflate(R.layout.map_dialog_setwifi, null);
        final EditText id_dialogWifi = (EditText)rootView.findViewById(R.id.id_dialogWifi);
        final EditText id_dialogPWD = (EditText)rootView.findViewById(R.id.id_dialogPWD);
        Button id_dialogBtnOK = (Button)rootView.findViewById(R.id.id_dialogBtnOK);
        Button id_dialogBtnCancel = (Button)rootView.findViewById(R.id.id_dialogBtnCancel);
        final Button id_dialogBtn00 = (Button)rootView.findViewById(R.id.id_dialogBtn00);
        final Button id_dialogBtn01 = (Button)rootView.findViewById(R.id.id_dialogBtn01);
        final Button id_dialogBtn02 = (Button)rootView.findViewById(R.id.id_dialogBtn02);
        final Button id_dialogBtn03 = (Button)rootView.findViewById(R.id.id_dialogBtn03);
        final Button id_dialogBtnNotSet = (Button)rootView.findViewById(R.id.id_dialogBtnNotSet);

        final Dialog dialogName = new MyDialog(context, 0, 0, rootView,
                R.style.dialog); // cx, R.style.dialog
        dialogName.setCancelable(false);

        id_dialogBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setResult(R_WifiSet);
                if (!id_dialogWifi.getText().toString().trim().equals("") && !id_dialogPWD.getText().toString().trim().equals("")) {
                    dialogName.dismiss();
//                    mWifiName = id_dialogWifi.getText().toString();
//                    mWifiPwd = id_dialogPWD.getText().toString();
//                    mHandler.sendEmptyMessage(mHandler_setWifi);
                    result.setMessage(id_dialogWifi.getText().toString());
                    result.setMessageSecond(id_dialogPWD.getText().toString());
                }
                else {
                    if(id_dialogWifi.getText().toString().trim().equals("")) result.setMessage(id_dialogWifi.getHint().toString().trim());// mWifiName = id_dialogWifi.getHint().toString().trim();
                    else result.setMessage(id_dialogWifi.getText().toString()); // mWifiName = id_dialogWifi.getText().toString();

                    if(id_dialogPWD.getText().toString().trim().equals("")) result.setMessageSecond(id_dialogPWD.getHint().toString().trim()); // mWifiPwd = id_dialogPWD.getHint().toString().trim();
                    else  result.setMessageSecond(id_dialogPWD.getText().toString()); // mWifiPwd = id_dialogPWD.getText().toString();

                    dialogName.dismiss();
//                    mHandler.sendEmptyMessage(mHandler_setWifi);
                }
                mHandler.obtainMessage(handlerWhat, result).sendToTarget();
            }
        });
        id_dialogBtnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialogName.dismiss();
                result.setResult(R_Cancel);
                mHandler.obtainMessage(handlerWhat, result).sendToTarget();
//                finish();
            }
        });

        id_dialogBtn00.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialogName.dismiss();
                result.setResult(R_WifiSet);
                result.setMessage(id_dialogBtn00.getText().toString().trim()); // mWifiName = id_dialogBtn00.getText().toString().trim();
                mHandler.obtainMessage(handlerWhat, result).sendToTarget();
//                mHandler.sendEmptyMessage(mHandler_setWifi);
            }
        });
        id_dialogBtn01.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialogName.dismiss();
                result.setResult(R_WifiSet);
                result.setMessage(id_dialogBtn01.getText().toString().trim()); // mWifiName = id_dialogBtn01.getText().toString().trim();
                mHandler.obtainMessage(handlerWhat, result).sendToTarget();
//                mHandler.sendEmptyMessage(mHandler_setWifi);
            }
        });
        id_dialogBtn02.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialogName.dismiss();
                result.setResult(R_WifiSet);
                result.setMessage(id_dialogBtn02.getText().toString().trim()); // mWifiName = id_dialogBtn02.getText().toString().trim();
                mHandler.obtainMessage(handlerWhat, result).sendToTarget();
//                mHandler.sendEmptyMessage(mHandler_setWifi);
            }
        });
        id_dialogBtn03.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialogName.dismiss();
                result.setResult(R_WifiSet);
                result.setMessage(id_dialogBtn03.getText().toString().trim()); // mWifiName = id_dialogBtn03.getText().toString().trim();
                mHandler.obtainMessage(handlerWhat, result).sendToTarget();
//                mHandler.sendEmptyMessage(mHandler_setWifi);
            }
        });
        id_dialogBtnNotSet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialogName.dismiss();
                result.setResult(R_WifiNotSet);
                mHandler.obtainMessage(handlerWhat, result).sendToTarget();
//                result.setMessage(id_dialogBtn03.getText().toString().trim()); // mWifiName = id_dialogBtn03.getText().toString().trim();
//                mHandler.sendEmptyMessage(mHandler_setWifi);
            }
        });

        Window dialogWindow = dialogName.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = 456; // 420 // 宽度
        lp.height = 702; // 588; // 276; // 210; // 高度

        dialogWindow.setAttributes(lp);

        if(!((Activity)context).isFinishing())dialogName.show(); // E/WindowManager: android.view.WindowLeaked: Activity com.iot.remocon.RemoconAllActivity has leaked window com.android.internal.policy.PhoneWindow$DecorView{3c8ef37 V.E...... R.....ID 0,0-640,256} that was originally added here

        if (!dialogName.isShowing()) {
            dialogName.dismiss();
        }
//        return result;
    }

    /** 对话框返回的结果 */
    public class DialogResult {
        private ResultName result = R_None;
        private String message = null;
        private String messageSecond = null;
        private String positionId = null;
        private String positionOldName = null;

        public ResultName getResult() {
            return result;
        }

        public void setResult(ResultName result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessageSecond() {
            return messageSecond;
        }

        public void setMessageSecond(String messageSecond) {
            this.messageSecond = messageSecond;
        }

        public String getPositionId() {
            return positionId;
        }

        public void setPositionId(String positionId) {
            this.positionId = positionId;
        }

        public String getPositionOldName() {
            return positionOldName;
        }

        public void setPositionOldName(String positionOldName) {
            this.positionOldName = positionOldName;
        }

        @Override
        public String toString() {
            return "DialogResult{" + "result=" + result + ", message='" + message + '\'' + ", messageSecond='" + messageSecond + '\'' + '}';
        }
    }
}
