package com.grasp.control.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.grasp.control.R;



/**
 * Created by zhujingju on 2017/8/11.
 */

public class ControlView extends LinearLayout {

    ImageView layout1You;
    ImageView layout1Up;
    ImageView layout1Down;
    ImageView layout1Zou;
    private Context context;

    public ControlView(Context context) {
        this(context, null, 0);
    }

    public ControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ControlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.kongzhi, this);
        layout1You= (ImageView) findViewById(R.id.layout1_you);
        layout1Zou= (ImageView) findViewById(R.id.layout1_zou);
        layout1Up= (ImageView) findViewById(R.id.layout1_up);
        layout1Down= (ImageView) findViewById(R.id.layout1_down);
    }

    int w, h,X,Y;
    boolean zt=false;
    Handler ha=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 999:
                    zt=false;
                    break;
            }
        }
    };
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

         X = (int) event.getX();
         Y = (int) event.getY();
        w = getWidth();
        h = getHeight()-10;

        switch (eventaction) {
            /**当初始进来的时候 ，向下移动的状态*/
            case MotionEvent.ACTION_DOWN:

                Log.d("qqq", "ACTION_DOWN" + X + "y=" + Y + "w= " + w + "h=" + h);
                if (Y >= 50 && Y <= 100 && X >= 90 && X < 140) {

                    return true;
                }
                if (Y >= 50 && Y <= 100 && X >= 90 && X < 140) {

                    return true;
                }
                if ((X - w / 2) * (X - w / 2) + (Y - h / 2) * (Y - h / 2) > ((h - 10) / 2) * ((h - 10) / 2)) {
                    return true;
                }
                zt=true;
                ha.sendEmptyMessageDelayed(999,2000);

                if (Y - (h / 2) >= (-1 * X) + (w / 2) && Y - (h / 2) >= X - (w / 2)) {

                    layout1Down.setBackgroundResource(R.drawable.robot_icon_direction_down);
                    controlListener.Control(MD_DOWN);
                } else if (Y - (h / 2) < (-1 * X) + (w / 2) && Y - (h / 2) < X - (w / 2)) {
                    layout1Up.setBackgroundResource(R.drawable.robot_icon_direction_up);
                    controlListener.Control(MD_UP);
                } else if (Y - (h / 2) < (-1 * X) + (w / 2) && Y - (h / 2) > X - (w / 2)) {
                    layout1Zou.setBackgroundResource(R.drawable.robot_icon_direction_left);
                    controlListener.Control(MD_LEFT);
                } else if (Y - (h / 2) > (-1 * X) + (w / 2) && Y - (h / 2) < X - (w / 2)) {
                    layout1You.setBackgroundResource(R.drawable.robot_icon_direction_right);
                    controlListener.Control(MD_RIGHT);
                }
                break;
            /**balId>0,小球的移动状态*/
            case MotionEvent.ACTION_MOVE:

//                Log.d("qqq", "ACTION_MOVE" + X + "y=" + Y);
//
//                if (Y >= 50 && Y <= 100 && X >= 90 && X < 140) {
//
//                    return true;
//                }
//                if (Y >= 50 && Y <= 100 && X >= 90 && X < 140) {
//
//                    return true;
//                }
//                if ((X - w / 2) * (X - w / 2) + (Y - h / 2) * (Y - h / 2) > ((h - 10) / 2) * ((h - 10) / 2)) {
//                    return true;
//                }
//                if(zt){
//                    ha.sendEmptyMessageDelayed(999,2000);
//                    return true;
//                }else{
//                    zt=true;
//                }
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (Y - (h / 2) >= (-1 * X) + (w / 2) && Y - (h / 2) >= X - (w / 2)) {
//
//                            controlListener.Control(MD_DOWN);
//                        } else if (Y - (h / 2) < (-1 * X) + (w / 2) && Y - (h / 2) < X - (w / 2)) {
//                            controlListener.Control(MD_UP);
//                        } else if (Y - (h / 2) < (-1 * X) + (w / 2) && Y - (h / 2) > X - (w / 2)) {
//                            controlListener.Control(MD_LEFT);
//                        } else if (Y - (h / 2) > (-1 * X) + (w / 2) && Y - (h / 2) < X - (w / 2)) {
//                            controlListener.Control(MD_RIGHT);
//                        }
//                    }
//                }).start();

                break;
            case MotionEvent.ACTION_UP:
                Log.d("qqq", "ACTION_UP" + X + "y=" + Y);
                if (layout1Up != null) {
                    controlListener.Control(MD_STOP);
                    layout1Up.setBackgroundResource(R.drawable.robot_icon_direction_up_normal);
                    layout1Down.setBackgroundResource(R.drawable.robot_icon_direction_down_normal);
                    layout1Zou.setBackgroundResource(R.drawable.robot_icon_direction_left_normal);
                    layout1You.setBackgroundResource(R.drawable.robot_icon_direction_right_normal);
                }


                break;
        }
        return true;
    }

    public static final byte MD_STOP = 8; // 停止
    public static final byte MD_LEFT = 11; // 左
    public static final byte MD_RIGHT = 12; // 右
    public static final byte MD_UP = 9; // 上
    public static final byte MD_DOWN = 10; // 下
    private ControlListener controlListener;
    public void setControlListener(ControlListener controlListener) {
        this.controlListener = controlListener;
    }

    public interface ControlListener {

        void Control(int posi);

    }
}
