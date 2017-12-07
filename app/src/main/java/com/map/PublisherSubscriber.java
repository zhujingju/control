package com.map;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import com.grasp.control.R;

import org.ros.android.MessageCallable;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;

import geometry_msgs.PoseStamped;

//
//import com.iot.coffee.MainActivity;
//import com.iot.coffee.R;

/**
 * Created by Administrator on 2017/1/13-013.
 */
public class PublisherSubscriber<T> implements NodeMain {
    private final String TAG = "Print-pb";

    private ConnectedNode connectedNode;
//    /** 发布消息 start */
//    private String topicPubCancel;
//    private String messageTypePubCancel;
    /** 监听
     private MessageCallable<String, T> callable;
     /** 发布器 */
//    private Publisher<std_msgs.String> pubCancel;
//    private Publisher<std_msgs.Float32MultiArray> pubCancel;
    /** set Handler to send sending result to MainActivity */
    private Handler mHandler = null;
    /** 被发布的类 */
//    private std_msgs.String contentToSend;
    /** 发布消息 end */

    /** 接收消息 start */
    private String topicSubGoal;
    private String messageTypeSubGoal;
    /** 监听*/
    private MessageCallable<String, T> callable;
//     /** 发布器 */
//    private Publisher<std_msgs.String> pubCancel;
//    /** 收数据 */
//    private Subscriber<PoseStamped> mSubGoal;
//    private Subscriber<std_msgs.Float32MultiArray> subscriberCancel;

    /**
     * 接收消息 end
     */

//    /** 异步线程 */
//    private AsyncRun mAsyncRun;
    public PublisherSubscriber(Context context) {
//        topicPubCancel = context.getResources().getString(R.string.s_topicName_grobotgoal);
////        topicPubCancel = "_stop_navi_";
//        messageTypePubCancel = Float32MultiArray._TYPE;

        topicSubGoal = context.getResources().getString(R.string.simple_goal_topic);
        messageTypeSubGoal = PoseStamped._TYPE;
    }
    /** set the topic name for this view. */
//    public void setTopicPubCancel(String topicPubCancel) {
//        this.topicPubCancel = topicPubCancel;
//    }

//    /** set the message type for this view. */
//    public void setMessageTypePubCancel(String messageTypePubCancel) {
//        this.messageTypePubCancel = messageTypePubCancel;
//    }



    /** set the topic name for this view. */
    public void setTopicSubGoal(String topicSubGoal) {
        this.topicSubGoal = topicSubGoal;
    }

    /** set the message type for this view. */
    public void setMessageTypeSubGoal(String messageTypeSubGoal) {
        this.messageTypeSubGoal = messageTypeSubGoal;
    }
    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    /** set the message callable to this view. */
    public void setMessageToStringCallable(MessageCallable<String, T> callable) {
        this.callable = callable;
    }

    /** get default node name by name. */
    @Override
    public GraphName getDefaultNodeName() {
//        return GraphName.of("android_gingerbread/ros_text_view");
        return GraphName.of("iot/ros_publish");
    }

    /**
     * this method will launch in the first time when app is launching.
     * @param connectedNode the node to publish and subscribe message.
     */
    @Override
    public void onStart(ConnectedNode connectedNode) {
        this.connectedNode = connectedNode;
        // init the pubCancel.
//        pubCancel = connectedNode.newPublisher(topicPubCancel, messageTypePubCancel); //初始化
//        pubCancel.getNumberOfSubscribers();
//        Log.i(TAG, "onStart-publisherNum=" + pubCancel.getNumberOfSubscribers()); // 0
//        System.out.println(TAG + " - onStart 被执行了");
//        mAsyncRun = new AsyncRun();
        initSubscriber(connectedNode);


    }

    /** 初始化订阅变量 */
    private void initSubscriber(ConnectedNode connectedNode){ // 不接收数据，据说不需要。
        // init the mSubGoal.
//        mSubGoal = connectedNode.newSubscriber(topicSubGoal, messageTypeSubGoal);
//
//        // listen message published by rosbot.
//        mSubGoal.addMessageListener(new MessageListener<T>() {
//            @Override
//            public void onNewMessage(final T message) {
////                System.out.println(TAG + " - 订阅数据 onNewMessage 被执行了");
//                if (callable != null) {
//                    new AsyncRun().execute(callable.call(message)); //异步线程只能执行一次
//                } else {
//                    new AsyncRun().execute(message.toString());
//                }
//            }
//        });

//        subscriberCancel = connectedNode.newSubscriber(topicPubCancel, messageTypePubCancel); //初始化
//        subscriberCancel.addMessageListener(new MessageListener<Float32MultiArray>() {
//            @Override
//            public void onNewMessage(Float32MultiArray float32MultiArray) {
//                Log.i(TAG, "onNewMessage: 取消得到返回:" + float32MultiArray.getData() + "=========================================");
//                for (float value : float32MultiArray.getData()) {
//                    Log.i(TAG, "onNewMessage: 取消得到返回:" + value);
//                }
//                Log.i(TAG, "onNewMessage: 取消得到返回:" + "=========================================");
//            }
//        });

//        mSubGoal = connectedNode.newSubscriber(topicSubGoal, messageTypeSubGoal); //初始化
//        mSubGoal.addMessageListener(new MessageListener<PoseStamped>() {
//            @Override
//            public void onNewMessage(PoseStamped poseStamped) {
//                Log.i(TAG, "onNewMessage: 测试，看长按事件不去目的地时是否会收到消息，poseStamped.x,y,z=(" + poseStamped.getPose().getPosition().getX() + ", " + poseStamped.getPose().getPosition().getY() + ", " + poseStamped.getPose().getPosition().getZ() + ")"); // 有消息，每次长按都收到，poseStamped.x,y,z=(1.2393670722755552, 0.8987239169601664, 1.5252291157347213E-17)，每次手指长按去目的地后，会收到一次，此后将不收了。
//            }
//        });
    }

//    /** 发布消息 */
//    public void send(Context context){
//        // sxg 已测试或运算是短路运算，第二个条件不会报空指针。
//        if(pubCancel == null || pubCancel.newMessage() == null){
//            Toast.makeText(context, "停止指令发送失败", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        /** 被发布的类 */
//        std_msgs.Float32MultiArray contentToSend = pubCancel.newMessage();
//        // convert message type from string to std_msgs.String
//        contentToSend.setData(new float[]{0, 0, 0, 0});
//        // publish the message.
//        pubCancel.publish(contentToSend);
//        Log.i(TAG, "send: 停止指令发送成功");
//    }


    /**
     * shut down
     * @param node
     */
    @Override
    public void onShutdown(Node node) {
    }

    @Override
    public void onShutdownComplete(Node node) {
    }

    @Override
    public void onError(Node node, Throwable throwable) {
    }

    private class AsyncRun extends AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String...args){
//            java.lang.String message = args[0];
            if (mHandler != null) {
//                Message msg = mHandler.obtainMessage();
////                msg.obj = callable.call(message);
//                msg.obj = args[0]; // 到了Activity里要判断文字是否空
//                msg.what = MainActivity.mHandler_Subscribe;
//                mHandler.sendMessage(msg);
            } else {
                System.out.println(TAG + " - mHandler is null!");
            }

//            Log.i(TAG, "callable is null ? " + (callable == null));
//            Log.i(TAG, "message = " + message);
            return null;
        }
    }
    /** 获取connectedNode，为了重连ros主机使得app换数据，旧的connectedNode需要先清理掉 */
    public ConnectedNode getConnectedNode() {
        return connectedNode;
    }
}
