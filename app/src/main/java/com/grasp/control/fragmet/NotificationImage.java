package com.grasp.control.fragmet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grasp.control.MainActivity;
import com.grasp.control.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhujingju on 2017/9/19.
 */

public class NotificationImage extends Fragment {


    @BindView(R.id.notification_fragmet_listview)
    LinearLayout lin;

    WebView imageviewWebview;
    Unbinder unbinder;
    @BindView(R.id.r_a_qx)
    TextView rAQx;
    private Context context;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.notification_image, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        Log.d("qqq","onCreateView");
        imageviewWebview = new WebView(context);
        LinearLayout.LayoutParams la = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        la.gravity = Gravity.CENTER;
        imageviewWebview.setLayoutParams(la);
        lin.addView(imageviewWebview);


        StringBuffer sb = new StringBuffer();
        sb.append("<html>")
                .append("<head>")
                .append("<meta http-equiv='Content-Type' content='text/html'; charset='UTF-8'>")
                .append("<style type='text/css'>")
                .append(".response-img {max-width: 100%;}")
                .append("#box {width: 100%;height: 100%;display: table;text-align: center;background: #fff;}")
                .append("#box span {display: table-cell;vertical-align: middle;}")
                .append("</style>")
                .append("<title>")
                .append("</title>")
                .append("</head>")
                .append("<body style='text-align: center;' onClick='window.myInterfaceName.showToast(\"finish Activity\")'>")
                .append("<div id='box'>")
                .append("<span>")
                .append("<img src='" + "" + url + "' class='response-img' style='width: 100%'/>")
                .append("</span>")
                .append("</div>")
                .append("</body>")
                .append("</html>");
//        imageviewWebview.addJavascriptInterface(new JavascriptInterface(context),"myInterfaceName");

        imageviewWebview.loadUrl(url);
//        imageviewWebview.loadUrl("file://"+path);
        WebSettings settings = imageviewWebview.getSettings();
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);\
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        //支持屏幕缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @OnClick(R.id.r_a_qx)
    public void onViewClicked() {
        MainActivity.notificationListener.close();
    }
}
