package com.grasp.control.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grasp.control.R;

/**
 * Created by zhujingju on 2017/8/28.
 */

public class MessageDialog extends Dialog {
    private static MessageDialog dialog = null;
    private TextView tvMsg;

    public static MessageDialog getInstance(Context context) {
        if (dialog == null) {
            synchronized (MessageDialog.class) {
                if (dialog == null) {
                    dialog = new MessageDialog(context, R.style.MessageDialogStyle);
                }
            }
        }
        return dialog;
    }

    private MessageDialog(Context context, int themeResId) {
        super(context, themeResId);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_message, null);
        tvMsg = (TextView) layout.findViewById(R.id.tv_msg);
        (layout.findViewById(R.id.bt_confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        this.addContentView(layout,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));//设置大小
    }

    public MessageDialog setMsg(String msg) {
        tvMsg.setText(msg);
        return this;
    }

}
