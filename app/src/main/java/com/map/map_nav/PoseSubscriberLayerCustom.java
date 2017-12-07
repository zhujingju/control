package com.map.map_nav;

import android.util.Log;

import org.ros.android.view.visualization.VisualizationView;
import org.ros.android.view.visualization.layer.SubscriberLayer;
import org.ros.android.view.visualization.layer.TfLayer;
import org.ros.android.view.visualization.shape.GoalShape;
import org.ros.android.view.visualization.shape.Shape;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.rosjava_geometry.FrameTransform;
import org.ros.rosjava_geometry.Transform;

import javax.microedition.khronos.opengles.GL10;

import geometry_msgs.PoseStamped;

/**
 * Created by Administrator on 2017/1/18-018.
 */
public class PoseSubscriberLayerCustom extends SubscriberLayer<PoseStamped> implements TfLayer {
    private final String TAG = "Print-PoseSubs";
    private final GraphName targetFrame;
    private Shape shape;
    private boolean ready;

    public PoseSubscriberLayerCustom(String topic) {
        this(GraphName.of(topic));
    }

    public PoseSubscriberLayerCustom(GraphName topic) {
        super(topic, PoseStamped._TYPE); // "geometry_msgs/PoseStamped");
        this.targetFrame = GraphName.of("map");
        this.ready = false;
    }

//    0,7:0.25
//    last:0.06853891945200942
    public void draw(VisualizationView view, GL10 gl) {
        if(this.ready) {
            this.shape.draw(view, gl); // 长按事件后的青箭头。
        }

    }
    @Override
    public void onStart(final VisualizationView view, ConnectedNode connectedNode) {
        super.onStart(view, connectedNode);
        if(this.shape == null) {
            this.shape = new GoalShape();
            shape.getTransform().toMatrix();
        }
        this.getSubscriber().addMessageListener(new MessageListener<PoseStamped>() {
            public void onNewMessage(PoseStamped pose) {
                Log.i(TAG, "onNewMessage"); // 只打印了一次
                GraphName source = GraphName.of(pose.getHeader().getFrameId());
                FrameTransform frameTransform = view.getFrameTransformTree().transform(source, PoseSubscriberLayerCustom.this.targetFrame);
                if (frameTransform != null) {
                    Transform poseTransform = Transform.fromPoseMessage(pose.getPose());
                    PoseSubscriberLayerCustom.this.shape.setTransform(frameTransform.getTransform().multiply(poseTransform));
//                    Log.i(TAG, "onNewMessage: 长按图标变换了" + shape.getTransform().getRotationAndScale().getMagnitude()); // 只打印了一次：长按图标变换了1.0。不论是图片还是箭头，都打印了。
                    PoseSubscriberLayerCustom.this.ready = true;
                    Log.i(TAG, "onNewMessage: poseTransform.toString()="+poseTransform.toString()+",poseTransform.getTranslation().toString()="+poseTransform.getTranslation().toString()+",poseTransform.getRotationAndScale().toString()="+poseTransform.getRotationAndScale().toString());
                }

            }
        });
    }

    public GraphName getFrame() {
        return this.targetFrame;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        this.shape.getTransform().toMatrix();
    }

    public void setReady(boolean ready) {
        this.ready = ready;
        Log.i(TAG, "setReady: ready="+ready);

    }
}
