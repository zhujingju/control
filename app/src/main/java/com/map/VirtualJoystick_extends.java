package com.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.ros.android.view.VirtualJoystickView;
import org.ros.node.Node;

/**
 * 手柄控制控件，控制机器人移动的角速度、线速度。
 * Created by Administrator on 2016/9/19.
 * 继承手柄控制控件，修改速度值：将onTouchEvent方法里event.X和Y改变后，可使速度变慢。
 */
public class VirtualJoystick_extends extends VirtualJoystickView {
    private static final String TAG = "Print-Teleop";
    /** 控制是否可以发送数据到机器人，选中该控件则发送，不选中则不发送数据。 */
    private boolean isViewSelected = false;
    /** 用来获取控件的当前宽高 */
    private VirtualJoystick_extends thisView;
    /** 是否为半速，控制速度，半速与全速的区别：在离控件中心越近的位置，半速速度比全速小一半。*/
//    private boolean speedHalf = true;
    /** 半速 */
//    public static final int SPEED_HALF_1 = 0x100;
    /** 1/3速度 */
//    public static final int SPEED_ONE_THREE = 0x101;
    /** 速度值：全速？半速？ */
//    private int speedValue = SPEED_HALF_1;

    public VirtualJoystick_extends(Context context){
        super(context);
        thisView = this;
    }

    public VirtualJoystick_extends(Context context, AttributeSet attrs){
        super(context, attrs);
        thisView = this;
    }

    public VirtualJoystick_extends(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        thisView = this;
    }

    /**
     * author : sxg
     * Velocity commands are published when this is true. Not published otherwise.
     * This is to prevent spamming velocity commands.
     */
    public void setViewSelected(boolean isViewSelectedToPublishVelocity){
        isViewSelected = isViewSelectedToPublishVelocity;
    }

    /** 控制速度的增大幅度，越接近控件中心，速度越低，速度增长越慢 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isViewSelected) return true;
//        mainLayout 改宽高为原值的1/2
//        if(this.speedValue == SPEED_ONE_THREE) {


//            event.setLocation(event.getX() + (thisView.getWidth() / 2 - event.getX()) * 2 / 3, event
//                    .getY() + (thisView.getHeight() / 2 - event.getY()) * 2 / 3); // 可用，这个比1/2的速度小一些



//        }
//        else if(this.speedValue == SPEED_HALF_1) {
//            event.setLocation(event.getX() + (thisView.getWidth() / 2 - event.getX()) / 2, event
//                    .getY() + (thisView.getHeight() / 2 - event.getY()) / 2); // 可用，比1 / 3的速度大一些

//        event.setLocation(event.getX() + ((int)((thisView.getWidth() >> 1) - event.getX()) >> 1),
//                event
//                .getY() + ((int)((thisView.getHeight() >> 1) - event.getY()) >> 1));

//        }
//        System.out.println(event.getX() + " -- " + event.getY());
//        Log.i(TAG, "onTouchEvent: thisView.getWidth()=" + thisView.getWidth() + ", thisView.getHeight()=" + thisView.getHeight()); // 获得的就是代码控制的大小。
        super.onTouchEvent(event);
        return true;
    }

    /** 设置速度是否为半速，半速与全速的区别：在离控件中心越近的位置，半速速度比全速小一半。  */
//    public void setSpeedHalf(int speed) {
//        if(speed != SPEED_HALF_1 && speed != SPEED_ONE_THREE) return;
//        this.speedValue = speed;
//    }

    @Override
    public void onShutdown(Node node) {
//        this.thisView = null;
        super.onShutdown(node);
    }
}
