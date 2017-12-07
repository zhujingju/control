package com.grasp.control;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Player.Core.PlayerClient;
import com.Player.web.websocket.ClientCore;
import com.grasp.control.MyInterface.AddListener;
import com.grasp.control.MyInterface.AddRquipmentListent;
import com.grasp.control.MyInterface.NotificationListener;
import com.grasp.control.Umeye_sdk.AcSearchDevice;
import com.grasp.control.Umeye_sdk.Constants;
import com.grasp.control.fragmet.AboutUs;
import com.grasp.control.fragmet.AddEquipment;
import com.grasp.control.fragmet.NotificationFragmet;
import com.grasp.control.fragmet.NotificationImage;
import com.grasp.control.fragmet.PlayBack;
import com.grasp.control.fragmet.SetUp;
import com.grasp.control.fragmet.Version;
import com.grasp.control.fragmet.help;
import com.grasp.control.fragmet.personal;
import com.grasp.control.tool.AndroidAdjustResizeBugFix;
import com.grasp.control.tool.AudioPlayUtil;
import com.grasp.control.tool.BaseFragmentActivity;
import com.grasp.control.tool.MyApplication;
import com.grasp.control.tool.SharedPreferencesUtils;
import com.grasp.control.tool.Utility;
import com.grasp.control.view.LayoutMain1;
import com.grasp.control.view.LayoutMain2;
import com.grasp.control.view.LayoutMain3;
import com.grasp.control.view.LayoutMain4;
import com.grasp.control.view.LayoutMain5;
import com.grasp.control.view.LayoutMain6;
import com.grasp.control.view.LayoutMainMAP;
import com.grasp.control.view.LookPhotoFragmet;
import com.grasp.control.view.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.openapi.EZOpenSDK;
import com.videogo.openapi.EZOpenSDKListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseFragmentActivity implements View.OnLayoutChangeListener {

    //    public static  String image_file=Environment.getExternalStorageDirectory()+"/MyFile_zjj/";
    public static String image_file = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/MyFile_ZWZD/";
    public static String image_file_im = image_file + "MyFile_im/";
    public static String image_file_im2 = image_file + "MyFile_ysd/";
    public static Activity activity;

    public static boolean Notification_zt=true;
    @BindView(R.id.main_bian1)
    Button mainBian1;
    @BindView(R.id.main_bian2)
    Button mainBian2;
    @BindView(R.id.main_bian3)
    Button mainBian3;
    @BindView(R.id.main_bian4)
    Button mainBian4;
    @BindView(R.id.main_bian5)
    Button mainBian5;
    @BindView(R.id.main_bian6)
    Button mainBian6;
    @BindView(R.id.main_bian7)
    Button mainBian7;
    @BindView(R.id.main_bian1_sz)
    LinearLayout mainBian1Sz;
    @BindView(R.id.main_bian1_sz2)
    RelativeLayout mainBian1Sz2;
    @BindView(R.id.main_frame)
    FrameLayout frame;
    @BindView(R.id.main_tx_name)
    TextView mainTxName;
    @BindView(R.id.main_tx)
    ImageView mainTx;
    @BindView(R.id.main_gl1)
    LinearLayout mainGl1;
    @BindView(R.id.main_gl2)
    LinearLayout mainGl2;
    @BindView(R.id.main_gl3)
    LinearLayout mainGl3;
    @BindView(R.id.main_gl4)
    LinearLayout mainGl4;
    @BindView(R.id.main_gl5)
    LinearLayout mainGl5;
    @BindView(R.id.main_gl6)
    LinearLayout mainGl6;
    @BindView(R.id.main_gl_tv1)
    TextView mainGlTv1;
    @BindView(R.id.main_gl_tv2)
    TextView mainGlTv2;
    @BindView(R.id.main_gl_tv3)
    TextView mainGlTv3;
    @BindView(R.id.main_gl_tv4)
    TextView mainGlTv4;
    @BindView(R.id.main_gl_tv5)
    TextView mainGlTv5;
    @BindView(R.id.main_gl_tv6)
    TextView mainGlTv6;
    @BindView(R.id.main_gl_frame)
    FrameLayout mainGlFrame;

    @BindView(R.id.main_rel)
    RelativeLayout loginRel;


    private LayoutMain1 layout1;
    private LayoutMain2 layout2;
    private LayoutMain3 layout3;
    private LayoutMain4 layout4;
    private LayoutMain5 layout5;
    private LayoutMain6 layout6;
    private LayoutMainMAP map;

//    private LookPhotoFragmet lookPhotoFragmet;

    private boolean bian1_zt = false;  //个人栏状态

    private boolean layout1_zt = false;
    private boolean layout2_zt = false;
    private boolean layout3_zt = false;
    private boolean layout4_zt = false;
    private boolean layout5_zt = false;
    private boolean layout6_zt = false;

    private AcSearchDevice acSearchDevice;
    private AddEquipment addEquipment;
    private NotificationImage notificationImage;
    @Override
    public int setLayoutId() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        ButterKnife.bind(this);
        AndroidAdjustResizeBugFix.assistActivity(this);
        Notification_zt= (boolean) SharedPreferencesUtils.getParam(getContext(),"mian_notification",true);
    }

    @Override
    public void initView() {
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        layout1 = new LayoutMain1();
        layout2 = new LayoutMain2();
        layout3 = new LayoutMain3();
        layout4 = new LayoutMain4();
        layout5 = new LayoutMain5();
        layout6 = new LayoutMain6();
        map = new LayoutMainMAP();
        notificationImage=new NotificationImage();
        acSearchDevice=new AcSearchDevice();
        addEquipment=new AddEquipment();
        acSearchDevice.setAddListener(new AddRquipmentListent() {
            @Override
            public void AddRquipmentListent(String s) {
                Log.d("qqq","--- s="+s);
                addEquipment.setName(s);
                initFragment2(addEquipment);
            }

            @Override
            public void SetRquipmentListent() {
                Log.d("qqq","SetRquipmentListent");
                layout1.dataListview2();
            }
        });

        addEquipment.setAddListener(new AddListener() {
            @Override
            public void addListener() {
                initFragment2(acSearchDevice);
            }
        });

        latout6Listener = new MyGridView.Latout6Listener() {
            @Override
            public void latout6Listener(int post, ArrayList<String> name, ArrayList<String> path, boolean zt) {
                LookPhotoFragmet lookPhotoFragmet = new LookPhotoFragmet();
                lookPhotoFragmet.setData(post, name, path, zt);
                initFragment1(lookPhotoFragmet);
            }
        };

        lookListener = new LookPhotoFragmet.LookListener() {
            @Override
            public void lookListener() {
                initFragment1(layout6);
            }
        };

        notificationListener=new NotificationListener() {
            @Override
            public void getNotFra() {
                mainBian1Sz2.setVisibility(View.VISIBLE);
                mainBian1.setBackgroundResource(R.drawable.tabbar_personalcenter_selected);
                anim2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_out);
//                    anim2 = new TranslateAnimation(-w, 0.0f, 0.0f, 0.0f);
//                    anim2.setDuration(500);
//                    anim2.setAnimationListener(animLis);
//
                mainBian1Sz2.layout(x + w, 0, x + 2 * w, mainBian1Sz2.getHeight());
//                    //开始动画
//                    mainBian1Sz.startAnimation(anim);

//                    anim2= AnimationUtils.loadAnimation(this, R.anim.alpha_out2);

                //开始动画
                mainBian1Sz2.startAnimation(anim2);

                bian1_zt=true;
                mainGl5.setBackgroundResource(R.drawable.zhu_kzbg_down);
                mainGlFrame.setVisibility(View.VISIBLE);
                if(!gl_zt1&&!gl_zt&&!gl_zt2&&!gl_zt3&&!gl_zt4&&!gl_zt5&&!gl_zt6)
                    mainGlFrame.startAnimation(anim_out);
                initFragment2(new NotificationFragmet());

            gl_zt5=!gl_zt5;
            gl_zt1=false;
            gl_zt2=false;
            gl_zt3=false;
            gl_zt4=false;
            gl_zt6=false;
                mainGl1.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl2.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl3.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl4.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl6.setBackgroundResource(R.drawable.zhu_kzbg_down1);
            }

            @Override
            public void open(String s) {
                notificationImage.setUrl(s);
                initFragment2(notificationImage);
            }

            @Override
            public void close() {
                initFragment2(new NotificationFragmet());
            }
        };

        initFragment1(layout1);
//        mainBian2.setBackgroundResource(R.drawable.tabbar_mapmode_selected);
//        new Thread(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        setpp();
//                    }
//                }
//        ).start();

    }

    @Override
    public void initObject() {

    }

    @Override
    public void initListener() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void init() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/ylyh.ttf");
        mainGlTv1.setTypeface(typeface);
        mainGlTv2.setTypeface(typeface);
        mainGlTv3.setTypeface(typeface);
        mainGlTv4.setTypeface(typeface);
        mainGlTv5.setTypeface(typeface);
        mainGlTv6.setTypeface(typeface);
        mainGlTv1.setLetterSpacing(0.2f);
        mainGlTv2.setLetterSpacing(0.2f);
        mainGlTv3.setLetterSpacing(0.2f);
        mainGlTv4.setLetterSpacing(0.2f);
        mainGlTv5.setLetterSpacing(0.2f);
        mainGlTv6.setLetterSpacing(0.2f);
//        mainGlTv1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//方法设置系统的字体样式
//        mainGlTv2.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//方法设置系统的字体样式
//        mainGlTv3.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));//方法设置系统的字体样式
//        mainGlTv4.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));//方法设置系统的字体样式
//        mainGlTv5.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));//方法设置系统的字体样式
//      ha.sendEmptyMessageDelayed(1234,1000);
        anim_lai = AnimationUtils.loadAnimation(this, R.anim.alpha_lai);
        anim_out = AnimationUtils.loadAnimation(this, R.anim.alpha_out);

        animLis_lai = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                //动画开始监听事件
                //do something...
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                mainBian1Sz.clearAnimation();
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                //动画结束监听事件
                mainGlFrame.setVisibility(View.GONE);

            }
        };
        anim_lai.setAnimationListener(animLis_lai);
    }

    private String str_pass;
    private int x, w;

    private Animation anim_lai,anim_out,anim2;
    private Animation.AnimationListener animLis_lai,animLis_out, animLis2;

    @OnClick({R.id.main_tx_name,R.id.main_tx, R.id.main_gl1, R.id.main_gl2, R.id.main_gl3, R.id.main_gl4, R.id.main_gl5, R.id.main_gl6, R.id.main_bian1, R.id.main_bian2, R.id.main_bian3, R.id.main_bian4, R.id.main_bian5, R.id.main_bian6, R.id.main_bian7})
    public void onViewClicked(View view) {
        if (view.getId() != R.id.main_bian1) {
            if (layout2_zt) {
                layout2.setDes();
            }

        }




        switch (view.getId()) {
            case R.id.main_bian1:

                if (bian1_zt) {
                    mainBian1.setBackgroundResource(R.drawable.button_1);

                    animLis2 = new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            //动画开始监听事件
                            //do something...
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            mainBian1Sz.clearAnimation();
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            //动画结束监听事件
                            mainBian1Sz2.setVisibility(View.GONE);
                            mainGlFrame.setVisibility(View.GONE);
                            fuyuan();
                        }
                    };
                    anim2 = AnimationUtils.loadAnimation(this, R.anim.alpha_lai2);
                    anim2.setAnimationListener(animLis2);
                    //开始动画
                    mainBian1Sz2.startAnimation(anim2);

                } else {
                    mainBian1Sz2.setVisibility(View.VISIBLE);
                    if (!bian1_zt) {
                        x = (int) mainBian1Sz2.getX();
                        w = mainBian1Sz2.getWidth();
                        Log.d("qqq", x + "  -- " + w + "  " + bian1_zt);
                    }
                    mainBian1.setBackgroundResource(R.drawable.tabbar_personalcenter_selected);
                    anim2 = AnimationUtils.loadAnimation(this, R.anim.alpha_out);
//                    anim2 = new TranslateAnimation(-w, 0.0f, 0.0f, 0.0f);
//                    anim2.setDuration(500);
//                    anim2.setAnimationListener(animLis);
//
                    mainBian1Sz2.layout(x + w, 0, x + 2 * w, mainBian1Sz2.getHeight());
//                    //开始动画
//                    mainBian1Sz.startAnimation(anim);

//                    anim2= AnimationUtils.loadAnimation(this, R.anim.alpha_out2);

                    //开始动画
                    mainBian1Sz2.startAnimation(anim2);
                }
                bian1_zt = !bian1_zt;
                break;
            case R.id.main_bian2:
                layout1.setZt();


                if (bian1_zt) {
                    animLis2 = new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            //动画开始监听事件
                            //do something...
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            mainBian1Sz.clearAnimation();
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            //动画结束监听事件
                            mainBian1Sz2.setVisibility(View.GONE);

                        }
                    };
                    anim2 = AnimationUtils.loadAnimation(this, R.anim.alpha_lai2);
                    anim2.setAnimationListener(animLis2);
                    //开始动画
                    mainBian1Sz2.startAnimation(anim2);
                }
                bian1_zt = false;


                mainBian1.setBackgroundResource(R.drawable.button_1);
                mainBian3.setBackgroundResource(R.drawable.bian3);
                mainBian4.setBackgroundResource(R.drawable.bian4);
                mainBian5.setBackgroundResource(R.drawable.bian5);
                mainBian6.setBackgroundResource(R.drawable.bian6);
                mainBian7.setBackgroundResource(R.drawable.bian7);

                if (layout1_zt) {
                    mainBian2.setBackgroundResource(R.drawable.bian2);
                    initFragment1(layout1);
                } else {
                    mainBian2.setBackgroundResource(R.drawable.tabbar_mapmode_selected);
                    initFragment1(map);
                }
                layout2_zt = false;
                layout3_zt = false;
                layout4_zt = false;
                layout5_zt = false;
                layout6_zt = false;
                layout1_zt = !layout1_zt;
                break;
            case R.id.main_bian3:
                layout1.setZt();
                if (bian1_zt) {
                    animLis2 = new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            //动画开始监听事件
                            //do something...
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            mainBian1Sz.clearAnimation();
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            //动画结束监听事件
                            mainBian1Sz2.setVisibility(View.GONE);

                        }
                    };
                    anim2 = AnimationUtils.loadAnimation(this, R.anim.alpha_lai2);
                    anim2.setAnimationListener(animLis2);
                    //开始动画
                    mainBian1Sz2.startAnimation(anim2);
                }
                bian1_zt = false;
                mainBian1.setBackgroundResource(R.drawable.button_1);
                mainBian2.setBackgroundResource(R.drawable.bian2);
//                mainBian3.setBackgroundResource(R.drawable.tabbar_monitormode_selected);
                mainBian4.setBackgroundResource(R.drawable.bian4);
                mainBian5.setBackgroundResource(R.drawable.bian5);
                mainBian6.setBackgroundResource(R.drawable.bian6);
                mainBian7.setBackgroundResource(R.drawable.bian7);
//                initFragment1(layout2);
                if (layout2_zt) {
                    mainBian3.setBackgroundResource(R.drawable.bian3);
                    initFragment1(layout1);
                } else {
                    mainBian3.setBackgroundResource(R.drawable.tabbar_monitormode_selected);
                    layout2.fuyan2();
                    initFragment1(layout2);
                }
                layout1_zt = false;
                layout3_zt = false;
                layout4_zt = false;
                layout5_zt = false;
                layout6_zt = false;
                layout2_zt = !layout2_zt;
                break;
            case R.id.main_bian4:
                layout1.setZt();
                if (bian1_zt) {
                    animLis2 = new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            //动画开始监听事件
                            //do something...
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            mainBian1Sz.clearAnimation();
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            //动画结束监听事件
                            mainBian1Sz2.setVisibility(View.GONE);

                        }
                    };
                    anim2 = AnimationUtils.loadAnimation(this, R.anim.alpha_lai2);
                    anim2.setAnimationListener(animLis2);
                    //开始动画
                    mainBian1Sz2.startAnimation(anim2);
                }
                bian1_zt = false;
                mainBian1.setBackgroundResource(R.drawable.button_1);
                mainBian2.setBackgroundResource(R.drawable.bian2);
                mainBian3.setBackgroundResource(R.drawable.bian3);
//                mainBian4.setBackgroundResource(R.drawable.tabbar_playback_selected);
                mainBian5.setBackgroundResource(R.drawable.bian5);
                mainBian6.setBackgroundResource(R.drawable.bian6);
                mainBian7.setBackgroundResource(R.drawable.bian7);
//                initFragment1(layout3);
                if (layout3_zt) {
                    mainBian4.setBackgroundResource(R.drawable.bian4);
                    initFragment1(layout1);
                } else {
                    mainBian4.setBackgroundResource(R.drawable.tabbar_playback_selected);
                    initFragment1(new PlayBack());
                }
                layout2_zt = false;
                layout1_zt = false;
                layout4_zt = false;
                layout5_zt = false;
                layout6_zt = false;
                layout3_zt = !layout3_zt;
                break;
            case R.id.main_bian5:
                layout1.setZt();
                if (bian1_zt) {
                    animLis2 = new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            //动画开始监听事件
                            //do something...
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            mainBian1Sz.clearAnimation();
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            //动画结束监听事件
                            mainBian1Sz2.setVisibility(View.GONE);

                        }
                    };
                    anim2 = AnimationUtils.loadAnimation(this, R.anim.alpha_lai2);
                    anim2.setAnimationListener(animLis2);
                    //开始动画
                    mainBian1Sz2.startAnimation(anim2);
                }
                bian1_zt = false;
                mainBian1.setBackgroundResource(R.drawable.button_1);
                mainBian2.setBackgroundResource(R.drawable.bian2);
                mainBian3.setBackgroundResource(R.drawable.bian3);
                mainBian4.setBackgroundResource(R.drawable.bian4);
//                mainBian5.setBackgroundResource(R.drawable.tabbar_equipment_selected);
                mainBian6.setBackgroundResource(R.drawable.bian6);
                mainBian7.setBackgroundResource(R.drawable.bian7);
//                initFragment1(layout4);
                if (layout4_zt) {
                    mainBian5.setBackgroundResource(R.drawable.bian5);
                    initFragment1(layout1);
                } else {
                    mainBian5.setBackgroundResource(R.drawable.tabbar_equipment_selected);
                    initFragment1(layout4);
                }
                layout2_zt = false;
                layout3_zt = false;
                layout1_zt = false;
                layout5_zt = false;
                layout6_zt = false;
                layout4_zt = !layout4_zt;
                break;
            case R.id.main_bian6:
                layout1.setZt();
                if (bian1_zt) {
                    animLis2 = new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            //动画开始监听事件
                            //do something...
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            mainBian1Sz.clearAnimation();
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            //动画结束监听事件
                            mainBian1Sz2.setVisibility(View.GONE);

                        }
                    };
                    anim2 = AnimationUtils.loadAnimation(this, R.anim.alpha_lai2);
                    anim2.setAnimationListener(animLis2);
                    //开始动画
                    mainBian1Sz2.startAnimation(anim2);
                }
                bian1_zt = false;
                mainBian1.setBackgroundResource(R.drawable.button_1);
                mainBian2.setBackgroundResource(R.drawable.bian2);
                mainBian3.setBackgroundResource(R.drawable.bian3);
                mainBian4.setBackgroundResource(R.drawable.bian4);
                mainBian5.setBackgroundResource(R.drawable.bian5);
//                mainBian6.setBackgroundResource(R.drawable.tabbar_alert_selected);
                mainBian7.setBackgroundResource(R.drawable.bian7);
//                initFragment1(layout5);
                if (layout5_zt) {
                    mainBian6.setBackgroundResource(R.drawable.bian6);
                    initFragment1(layout1);
                } else {
                    mainBian6.setBackgroundResource(R.drawable.tabbar_alert_selected);
                    initFragment1(layout5);
                }
                layout2_zt = false;
                layout3_zt = false;
                layout4_zt = false;
                layout1_zt = false;
                layout6_zt = false;
                layout5_zt = !layout5_zt;
                break;
            case R.id.main_bian7:
                layout1.setZt();
                if (bian1_zt) {
                    animLis2 = new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation arg0) {
                            //动画开始监听事件
                            //do something...
                        }

                        @Override
                        public void onAnimationRepeat(Animation arg0) {
                            mainBian1Sz.clearAnimation();
                        }

                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            //动画结束监听事件
                            mainBian1Sz2.setVisibility(View.GONE);

                        }
                    };
                    anim2 = AnimationUtils.loadAnimation(this, R.anim.alpha_lai2);
                    anim2.setAnimationListener(animLis2);
                    //开始动画
                    mainBian1Sz2.startAnimation(anim2);
                }
                bian1_zt = false;
                mainBian1.setBackgroundResource(R.drawable.button_1);
                mainBian2.setBackgroundResource(R.drawable.bian2);
                mainBian3.setBackgroundResource(R.drawable.bian3);
                mainBian4.setBackgroundResource(R.drawable.bian4);
                mainBian5.setBackgroundResource(R.drawable.bian5);
                mainBian6.setBackgroundResource(R.drawable.bian6);
//                mainBian7.setBackgroundResource(R.drawable.tabbar_image_selected);
//                initFragment1(layout6);
                if (layout6_zt) {
                    mainBian7.setBackgroundResource(R.drawable.bian7);
                    initFragment1(layout1);
                } else {
                    mainBian7.setBackgroundResource(R.drawable.tabbar_image_selected);
                    initFragment1(layout6);
                }
                layout2_zt = false;
                layout3_zt = false;
                layout4_zt = false;
                layout5_zt = false;
                layout1_zt = false;
                layout6_zt = !layout6_zt;
                break;

            case R.id.main_tx_name:
                if(gl_zt){
                    mainGlFrame.startAnimation(anim_lai);
                }else{
                    mainGlFrame.setVisibility(View.VISIBLE);
                    if(!gl_zt1&&!gl_zt&&!gl_zt2&&!gl_zt3&&!gl_zt4&&!gl_zt5&&!gl_zt6)
                        mainGlFrame.startAnimation(anim_out);
                    initFragment2(new personal());
                }
                gl_zt=!gl_zt;
                gl_zt1=false;
                gl_zt2=false;
                gl_zt3=false;
                gl_zt4=false;
                gl_zt5=false;
                gl_zt6=false;
                mainGl1.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl2.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl3.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl4.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl5.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl6.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                break;

            case R.id.main_tx:
                if(gl_zt){
                    mainGlFrame.startAnimation(anim_lai);
                }else{
                    mainGlFrame.setVisibility(View.VISIBLE);
                    if(!gl_zt1&&!gl_zt&&!gl_zt2&&!gl_zt3&&!gl_zt4&&!gl_zt5&&!gl_zt6)
                        mainGlFrame.startAnimation(anim_out);
                    initFragment2(new personal());
                }
                gl_zt=!gl_zt;
                gl_zt1=false;
                gl_zt2=false;
                gl_zt3=false;
                gl_zt4=false;
                gl_zt5=false;
                gl_zt6=false;
                mainGl1.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl2.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl3.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl4.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl5.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl6.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                break;
            case R.id.main_gl1:
                if(gl_zt1){
                    mainGl1.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                    mainGlFrame.startAnimation(anim_lai);
                }else{
                    mainGl1.setBackgroundResource(R.drawable.zhu_kzbg_down);
                    mainGlFrame.setVisibility(View.VISIBLE);
                    if(!gl_zt1&&!gl_zt&&!gl_zt2&&!gl_zt3&&!gl_zt4&&!gl_zt5&&!gl_zt6)
                            mainGlFrame.startAnimation(anim_out);
                    initFragment2(acSearchDevice);
                }
                gl_zt1=!gl_zt1;

                gl_zt2=false;
                gl_zt3=false;
                gl_zt4=false;
                gl_zt5=false;
                gl_zt6=false;
                mainGl2.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl3.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl4.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl5.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl6.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                break;
            case R.id.main_gl2:

                if(gl_zt2){
                    mainGl2.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                    mainGlFrame.startAnimation(anim_lai);

                    mainGlFrame.setVisibility(View.GONE);
                }else{
                    mainGl2.setBackgroundResource(R.drawable.zhu_kzbg_down);
                    mainGlFrame.setVisibility(View.VISIBLE);
                    if(!gl_zt1&&!gl_zt&&!gl_zt2&&!gl_zt3&&!gl_zt4&&!gl_zt5&&!gl_zt6){
                        mainGlFrame.startAnimation(anim_out);
                    }

                    initFragment2(new SetUp());
                }
                gl_zt2=!gl_zt2;
                gl_zt1=false;
                gl_zt3=false;
                gl_zt4=false;
                gl_zt5=false;
                gl_zt6=false;
                mainGl1.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl3.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl4.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl5.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl6.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                break;
            case R.id.main_gl3:
                if(gl_zt3){
                    mainGl3.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                    mainGlFrame.startAnimation(anim_lai);
                    mainGlFrame.setVisibility(View.GONE);
                }else{
                    mainGl3.setBackgroundResource(R.drawable.zhu_kzbg_down);
                    mainGlFrame.setVisibility(View.VISIBLE);
                    if(!gl_zt1&&!gl_zt&&!gl_zt2&&!gl_zt3&&!gl_zt4&&!gl_zt5&&!gl_zt6)
                        mainGlFrame.startAnimation(anim_out);
                    initFragment2(new AboutUs());
                }
                gl_zt3=!gl_zt3;
                gl_zt1=false;
                gl_zt2=false;
                gl_zt4=false;
                gl_zt5=false;
                gl_zt6=false;
                mainGl1.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl2.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl4.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl5.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl6.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                break;
            case R.id.main_gl4:
//                gl_zt1=false;

                if(gl_zt4){
                    mainGl4.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                    mainGlFrame.startAnimation(anim_lai);
                    mainGlFrame.setVisibility(View.GONE);
                }else{
                    mainGl4.setBackgroundResource(R.drawable.zhu_kzbg_down);
                    mainGlFrame.setVisibility(View.VISIBLE);
                    if(!gl_zt1&&!gl_zt&&!gl_zt2&&!gl_zt3&&!gl_zt4&&!gl_zt5&&!gl_zt6)
                        mainGlFrame.startAnimation(anim_out);
                    initFragment2(new Version());
                }
                gl_zt4=!gl_zt4;
                gl_zt2=false;
                gl_zt3=false;
                gl_zt5=false;
                gl_zt6=false;
                mainGl1.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl2.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl3.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl5.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl6.setBackgroundResource(R.drawable.zhu_kzbg_down1);
//                Toast.makeText(getContext(),"当前版本为："+getVersion(),Toast.LENGTH_SHORT).show();

                break;
            case R.id.main_gl5:

                if(gl_zt5){
                    mainGl5.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                    mainGlFrame.startAnimation(anim_lai);
                    mainGlFrame.setVisibility(View.GONE);
                }else{
                    mainGl5.setBackgroundResource(R.drawable.zhu_kzbg_down);
                    mainGlFrame.setVisibility(View.VISIBLE);
                    if(!gl_zt1&&!gl_zt&&!gl_zt2&&!gl_zt3&&!gl_zt4&&!gl_zt5&&!gl_zt6)
                        mainGlFrame.startAnimation(anim_out);
                    initFragment2(new NotificationFragmet());
                }
                gl_zt5=!gl_zt5;
                gl_zt1=false;
                gl_zt2=false;
                gl_zt3=false;
                gl_zt4=false;
                gl_zt6=false;
                mainGl1.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl2.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl3.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl4.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl6.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                break;
            case R.id.main_gl6:

                if(gl_zt6){
                    mainGl6.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                    mainGlFrame.startAnimation(anim_lai);
                    mainGlFrame.setVisibility(View.GONE);
                }else{
                    mainGl6.setBackgroundResource(R.drawable.zhu_kzbg_down);
                    mainGlFrame.setVisibility(View.VISIBLE);
                    if(!gl_zt1&&!gl_zt&&!gl_zt2&&!gl_zt3&&!gl_zt4&&!gl_zt5&&!gl_zt6)
                        mainGlFrame.startAnimation(anim_out);
                    initFragment2(new help());
                }
                gl_zt6=!gl_zt6;
                gl_zt1=false;
                gl_zt2=false;
                gl_zt3=false;
                gl_zt4=false;
                gl_zt5=false;
                mainGl1.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl2.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl3.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl4.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                mainGl5.setBackgroundResource(R.drawable.zhu_kzbg_down1);
                break;
        }
    }




    private boolean gl_zt,gl_zt1,gl_zt2,gl_zt3,gl_zt4,gl_zt5,gl_zt6=false;
    private void fuyuan(){
        gl_zt=false;
        gl_zt1=false;
        gl_zt2=false;
        gl_zt3=false;
        gl_zt4=false;
        gl_zt5=false;
        gl_zt6=false;
        mainGl1.setBackgroundResource(R.drawable.zhu_kzbg_down1);
        mainGl2.setBackgroundResource(R.drawable.zhu_kzbg_down1);
        mainGl3.setBackgroundResource(R.drawable.zhu_kzbg_down1);
        mainGl4.setBackgroundResource(R.drawable.zhu_kzbg_down1);
        mainGl5.setBackgroundResource(R.drawable.zhu_kzbg_down1);
        mainGl6.setBackgroundResource(R.drawable.zhu_kzbg_down1);

    }

    //显示fragment
    private void initFragment1(Fragment f1) {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
//        if(f1 == null){
//            f1 = new MyFragment("消息");
        transaction.replace(R.id.main_frame, f1);
//        }
        //隐藏所有fragment
//        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(f1);

        //第二种方式(replace)，初始化fragment
//        if(f1 == null){
//            f1 = new MyFragment("消息");
//        }
//        transaction.replace(R.id.main_frame_layout, f1);

        //提交事务
        transaction.commit();
    }


    //显示fragment
    private void initFragment2(Fragment f1) {
        //开启事务，fragment的控制是由事务来实现的
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //第一种方式（add），初始化fragment并添加到事务中，如果为null就new一个
//        if(f1 == null){
//            f1 = new MyFragment("消息");
        transaction.replace(R.id.main_gl_frame, f1);
//        }
        //隐藏所有fragment
//        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(f1);

        //第二种方式(replace)，初始化fragment
//        if(f1 == null){
//            f1 = new MyFragment("消息");
//        }
//        transaction.replace(R.id.main_frame_layout, f1);

        //提交事务
        transaction.commit();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d("qqq","onWindowFocusChanged");
//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
    }

    public void setViewQp(){
        if ( Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    public void setpp() {


        //<dimen name="dip_48">48dip</dimen>
        double a[] = new double[2000];

        for (int m = 0; m < 2000; m++) {
            Log.d("splash", m + "");
            a[m] = (m + 0.0) / 2;
//			Log.d("splash",""+a[i]+" ----  "+i);
        }


        String s = "";


        Log.d("splash", "i*0.5*2*8 /3*800/857************");
        s = "";
        for (int i = 0; i < a.length; i++) {
            //Log.d("qqq","<dimen name=\"dip_"+a[i]+"\">"+a[i]*0.5*9+"dip</dimen>");
            s += "<dimen name=\"dip_" + a[i] + "\">" + a[i] * 0.5 * 2 * 8 / 3 / 800 * 857 + "dip</dimen>" + "  ";
        }
        Log.d("splash", s);
//		Log.d("qqq","<dimen name=\"dip_"+i+"\">"+i*0.5+"dip</dimen>");
//		Log.d("qqq","<dimen name=\"dip_"+i+"\">"+i*0.5*1.5 +"dip</dimen>");
//		Log.d("qqq","<dimen name=\"dip_"+i+"\">"+i*0.5*2 +"dip</dimen>");
//		Log.d("qqq","<dimen name=\"dip_"+i+"\">"+i*0.5*3 +"dip</dimen>");
//		Log.d("qqq","<dimen name=\"dip_"+i+"\">"+i*0.5*3*1.2 +"dip</dimen>");
//		Log.d("qqq","<dimen name=\"dip_"+i+"\">"+i*0.5*9 +"dip</dimen>");

        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录,2.2的时候为:/mnt/sdcart  2.1的时候为：/sdcard，所以使用静态方法得到路径会好一点。
                File saveFile = new File(sdCardDir, "zjj.txt");
                FileOutputStream outStream = new FileOutputStream(saveFile);
                outStream.write(s.getBytes());
                outStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static MyGridView.Latout6Listener latout6Listener;
    public static LookPhotoFragmet.LookListener lookListener;
    public static NotificationListener notificationListener;








    @Override
    protected void onResume() {
        super.onResume();

        //添加layout大小发生改变监听器
        loginRel.addOnLayoutChangeListener(this);

        String userPic = "";
        userPic = (String) SharedPreferencesUtils.getParam(getContext(), MyApplication.NAME_TX, "");

        if (!TextUtils.isEmpty(userPic)) {
            Log.d("logo", "+++" + userPic);
            ImageLoader.getInstance().displayImage("file://" + userPic, mainTx, MyApplication.options2);
        } else {
        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    @Override
    protected void onDestroy() {
        if(loginRel!=null){
            loginRel.removeOnLayoutChangeListener(this);
        }
        ha.removeCallbacks(null);
        super.onDestroy();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > 0)) {

//            Toast.makeText(getContext(), "监听到软键盘弹起...", Toast.LENGTH_SHORT).show();
            Log.d("qqq","监听到软件盘弹起");
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom >0)) {
            Log.d("qqq","监听到软件盘关闭");
//            Toast.makeText(MainActivity.this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();
            ha.sendEmptyMessageDelayed(1234,100);
        }
    }

    Handler ha = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {


                case 1234:
                    View decorView = getWindow().getDecorView();
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    break;
                default:
                    break;
            }
        }

    };


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("qqq","onNewIntent");
        notificationListener.getNotFra();
    }
}
