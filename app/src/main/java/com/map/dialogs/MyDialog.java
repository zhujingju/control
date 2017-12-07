package com.map.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

/**
 * 自定义Dialog，不能继承AlertDialog，否则show()的时候出错。
 *
 * @author Administrator
 *
 */
public class MyDialog extends Dialog {

    private static int default_width = 400; // 默认宽度
    private static int default_height = 120;// 默认高度

    public MyDialog(Context context, View layout, int style) {
        this(context, default_width, default_height, layout, style);
    }

    public MyDialog(Context context, int width, int height, View layout,
                    int style) {
        super(context, style);
        setContentView(layout);
        // Window window = getWindow();
        // WindowManager.LayoutParams params = window.getAttributes();
        // params.gravity = Gravity.CENTER;
        // params.x = 10;
        // params.y = 10;
        // window.setAttributes(params);
    }
}
