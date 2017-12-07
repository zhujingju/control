package com.map.map_nav;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.common.base.Preconditions;
import com.grasp.control.R;

import org.ros.android.view.visualization.VisualizationView;
import org.ros.android.view.visualization.layer.DefaultLayer;
import org.ros.android.view.visualization.shape.PixelSpacePoseShape;
import org.ros.android.view.visualization.shape.Shape;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.ros.rosjava_geometry.Quaternion;
import org.ros.rosjava_geometry.Transform;
import org.ros.rosjava_geometry.Vector3;

import javax.microedition.khronos.opengles.GL10;

import geometry_msgs.Pose;
import geometry_msgs.PoseStamped;
import geometry_msgs.PoseWithCovarianceStamped;
import move_base_msgs.MoveBaseActionGoal;
import std_msgs.Float32MultiArray;


public class MapPosePublisherLayer extends DefaultLayer {
	private static final String TAG = "Print-MapPoseP";

	private Shape shape;
	private Publisher<PoseWithCovarianceStamped> pubInitialpose;
	private Publisher<PoseStamped> pubMoveBaseSimpleGoal;
	private Publisher<MoveBaseActionGoal> pubMoveBaseGoal;
	private Publisher<PoseWithCovarianceStamped> publishLastPose;
	private Publisher<Float32MultiArray> pubCancel;

	private Subscriber<MoveBaseActionGoal> subGoal;
	    private Subscriber<Float32MultiArray> subscriberCancel;
	private boolean visible;
	private GestureDetector gestureDetector;
	private Transform pose;
	private Transform fixedPose;
	private ConnectedNode connectedNode;

	/** 用来发布最后的坐标 */
	private VisualizationView view;

	private int mode;
	private static final int NONE_MODE = 0;
	private static final int POSE_MODE = 1;
	private static final int GOAL_MODE = 2;
	private static final int EDIT_MODE = 3;

    private String mapFrame;
    private String robotFrame;
    private String initialPoseTopic;
    private String simpleGoalTopic;
    private String moveBaseGoalTopic;
    /** 正在去往目的地 */
	private boolean isGoingToGoal = false;
	/** 发布消息 start */
	private String topicPubCancel;
	private String messageTypePubCancel;
	/** 接收消息 start */
	private String topicSubGoal;
	private String messageTypeSubGoal;

	private Handler mHandler = null;
	private int whatCancelOK = -1;

	private int[] mGestureXY;


	public MapPosePublisherLayer(final Context context) {
		visible = false;

		this.mapFrame = context.getString(R.string.map_frame);
		this.robotFrame = context.getString(R.string.robot_frame);

		this.initialPoseTopic = context.getString(R.string.initial_pose_topic);
		//长按事件，发布position(x,y,z)和orientation(z, w)出去。
		this.simpleGoalTopic = context.getString(R.string.simple_goal_topic);
		this.moveBaseGoalTopic = context.getString(R.string.move_base_goal_topic);


		this.topicPubCancel = context.getResources().getString(R.string.s_topicName_grobotgoal);
		this.messageTypePubCancel = Float32MultiArray._TYPE;

		this.topicSubGoal = context.getResources().getString(R.string.simple_goal_topic);
		this.messageTypeSubGoal = PoseStamped._TYPE;

	}


	public void setNoneMode() {
		mode = NONE_MODE;
	}
	public void setPoseMode() {
		mode = POSE_MODE;
	}
	public void setGoalMode() {
		mode = GOAL_MODE;
	}
	public void setEditMode() {
		mode = EDIT_MODE;
	}

	public boolean isEditMode() {
		if(mode == EDIT_MODE) return true;
		else return false;
	}
	public boolean isPoseMode(){
		if(mode == POSE_MODE) return true;
		else return false;
	}

	public boolean isGoalMode(){
		if(mode == GOAL_MODE) return true;
		else return false;
	}

	public int getMode() {
		return mode;
	}

	@Override
	public void draw(VisualizationView view, GL10 gl) {
		if (visible) {
			Preconditions.checkNotNull(pose);
			shape.draw(view, gl);
		}
	}

	private double angle(double x1, double y1, double x2, double y2) {
		double deltaX = x1 - x2;
		double deltaY = y1 - y2;
		return Math.atan2(deltaY, deltaX);
	}

	@Override
	public boolean onTouchEvent(VisualizationView view, MotionEvent event) {
		if (visible) {
			Preconditions.checkNotNull(pose);

			Vector3 poseVector;
			Vector3 pointerVector;

			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				poseVector = pose.apply(Vector3.zero());
				pointerVector = view.getCamera().toCameraFrame((int) event.getX(),
						(int) event.getY());

				double angle = angle(pointerVector.getX(),
						pointerVector.getY(), poseVector.getX(),
						poseVector.getY());
				pose = Transform.translation(poseVector).multiply(
						Transform.zRotation(angle));

				shape.setTransform(pose);
				return false;
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {

				PoseStamped poseStamped;
				switch (mode) {
				case POSE_MODE:
//                    view.getCamera().setFrame(mapFrame);
					poseVector = fixedPose.apply(Vector3.zero());
					pointerVector = view.getCamera().toCameraFrame((int) event.getX(), (int) event.getY());
					double angle2 = angle(pointerVector.getX(),
							pointerVector.getY(), poseVector.getX(),
							poseVector.getY());
					fixedPose = Transform.translation(poseVector).multiply(
							Transform.zRotation(angle2));
//					view.getCamera().setFrame(robotFrame);
					poseStamped = fixedPose.toPoseStampedMessage(
							GraphName.of(robotFrame),
							connectedNode.getCurrentTime(),
							pubMoveBaseSimpleGoal.newMessage());

					PoseWithCovarianceStamped initialPose = pubInitialpose.newMessage();
					initialPose.getHeader().setFrameId(mapFrame);
					initialPose.getPose().setPose(poseStamped.getPose());
					double[] covariance = initialPose.getPose().getCovariance();
					covariance[6 * 0 + 0] = 0.5 * 0.5;
					covariance[6 * 1 + 1] = 0.5 * 0.5;
					covariance[6 * 5 + 5] = (float) (Math.PI / 12.0 * Math.PI / 12.0);

					pubInitialpose.publish(initialPose);
					Log.i(TAG, "pubInitialpose.publish(initialPose)");
					break;
				case GOAL_MODE:
					// 设置去目的地的坐标，放到robot层设置 --start
					if(mGestureXY == null) Log.e(TAG, "onTouchEvent: mGestureXY is null");
					else {
						GraphName graphNameTmp = view.getCamera().getFrame();
						view.getCamera().setFrame(robotFrame);
						Log.i(TAG, "onTouchEvent: graphNameTmp="+graphNameTmp); // graphNameTmp=map

						pose = Transform.translation(view.getCamera().toCameraFrame(mGestureXY[0], mGestureXY[1]));
						poseVector = pose.apply(Vector3.zero());
						pointerVector = view.getCamera().toCameraFrame((int) event.getX(), (int) event.getY());
						double angle = angle(pointerVector.getX(), pointerVector.getY(), poseVector.getX(), poseVector.getY());
						pose = Transform.translation(poseVector).multiply(Transform.zRotation(angle));

						view.getCamera().setFrame(graphNameTmp);
					}
					// 设置去目的地的坐标，放到robot层设置 --end

					poseStamped = pose.toPoseStampedMessage(
							GraphName.of(robotFrame),
							connectedNode.getCurrentTime(),
							pubMoveBaseSimpleGoal.newMessage());
					pubMoveBaseSimpleGoal.publish(poseStamped);
					Log.i(TAG, "pubMoveBaseSimpleGoal.publish(poseStamped)");

					MoveBaseActionGoal message = pubMoveBaseGoal.newMessage();
					message.setHeader(poseStamped.getHeader());
					message.getGoalId().setStamp(connectedNode.getCurrentTime());
					message.getGoalId().setId("move_base/move_base_client_android" + connectedNode.getCurrentTime().toString());
					message.getGoal().setTargetPose(poseStamped);
					pubMoveBaseGoal.publish(message);
					Log.i(TAG, "pubMoveBaseGoal.publish(message)"); //pubMoveBaseGoal注销后似乎不影响别的？

                    setGoingToGoal(true);
					break;

                    default:
                        break;
				}
				visible = false;
				return false; // 如果true，其他层就没办法点击了。
			}
		}
		else if(gestureDetector != null){
			gestureDetector.onTouchEvent(event);
		}
		return false;
	}

	@Override
	public void onStart(final VisualizationView view, ConnectedNode connectedNode) {
		this.connectedNode = connectedNode;
		this.view = view;

		shape = new PixelSpacePoseShape();
		mode = POSE_MODE;

		pubInitialpose = connectedNode.newPublisher(initialPoseTopic,
				"geometry_msgs/PoseWithCovarianceStamped");
		publishLastPose = connectedNode.newPublisher(initialPoseTopic,
				"geometry_msgs/PoseWithCovarianceStamped");
		pubMoveBaseSimpleGoal = connectedNode.newPublisher(simpleGoalTopic,
				"geometry_msgs/PoseStamped");
		pubMoveBaseGoal = connectedNode.newPublisher(moveBaseGoalTopic,
				"move_base_msgs/MoveBaseActionGoal");

		subGoal = connectedNode.newSubscriber(moveBaseGoalTopic,
				"move_base_msgs/MoveBaseActionGoal");

		subGoal.addMessageListener(new MessageListener<MoveBaseActionGoal>() {
			@Override
			public void onNewMessage(MoveBaseActionGoal moveBaseActionGoal) {
				Pose pose = moveBaseActionGoal.getGoal().getTargetPose().getPose();
				Log.i(TAG, "onNewMessage: pose.toString()="+pose.toString()+",pose.getOrientation().toString()="+pose.getOrientation().toString()+",pose.getPosition().toString()="+pose.getPosition().toString());
			}
		});
		pubCancel = connectedNode.newPublisher(topicPubCancel, messageTypePubCancel); //初始化

        subscriberCancel = connectedNode.newSubscriber(topicPubCancel, messageTypePubCancel); //初始化
        subscriberCancel.addMessageListener(new MessageListener<Float32MultiArray>() {
            @Override
            public void onNewMessage(Float32MultiArray float32MultiArray) {
				Log.i(TAG, "onNewMessage: Canceled to goal. ");
				if(mHandler != null && whatCancelOK != -1) mHandler.sendEmptyMessage(whatCancelOK);
            }
        });

		view.post(new Runnable() {
			@Override
			public void run() {
				gestureDetector = new GestureDetector(view.getContext(), new GestureDetector.SimpleOnGestureListener() {
					@Override
					public void onLongPress(MotionEvent e) {
						Log.i(TAG, "onLongPress: isEditMode()=" + isEditMode());
						if(isPoseMode() || isGoalMode()) {
							pose = Transform.translation(view.getCamera().toCameraFrame((int) e.getX(), (int) e.getY()));
							shape.setTransform(pose);
                            Log.i(TAG, "onLongPress: view.getCamera().getFrame().toString()="+view.getCamera().getFrame().toString());
//							view.getCamera().setFrame(mapFrame);
							fixedPose = Transform.translation(view.getCamera().toCameraFrame((int) e.getX(), (int) e.getY()));
//							view.getCamera().setFrame(robotFrame);
							visible = true;

							Log.i(TAG, "onLongPress: pose.getRotationAndScale().toString()="+pose.getRotationAndScale().toString()+",pose.getTranslation().toString()="+pose.getTranslation().toString()+",pose.toString()="+pose.toString()); // pose.getRotationAndScale().toString()=Quaternion<x: 0.0000, y: 0.0000, z: 0.0000, w: 1.0000>,pose.getTranslation().toString()=Vector3<x: 0.2067, y: -3.9133, z: 0.0000>,pose.toString()=Transform<Vector3<x: 0.2067, y: -3.9133, z: 0.0000>, Quaternion<x: 0.0000, y: 0.0000, z: 0.0000, w: 1.0000>>

							if(mGestureXY == null) mGestureXY = new int[2];
							mGestureXY[0] = (int)e.getX();
							mGestureXY[1] = (int)e.getY();

						}
					}

//					@Override
//					public boolean onSingleTapUp(MotionEvent e) {
//						Log.i(TAG, "onSingleTapUp: "); // 点击事件
////						if (isGoingToGoal && mHandler != null) {
//						if (mHandler != null) {
//							mHandler.sendEmptyMessage(whatCancel);
//							isGoingToGoal = false;
//							Log.i(TAG, "onSingleTapUp: go to stop.");
//						}
//						return super.onSingleTapUp(e);
//					}
				});
			}
		});
	}

	/** 发布最后坐标 */
	public void publishLastPose(float lastX, float lastY, float lastZ, float lastW){
		if(this.view == null) {
			Log.e(TAG, "this.view is null");
			return;
		}
		if (publishLastPose == null) {
			Log.e(TAG, "publishLastPose is null");
			return;
		}
//		this.view.getCamera().setFrame(mapFrame);

		PoseWithCovarianceStamped initialPose = pubInitialpose.newMessage();
		Transform transform = new Transform(new Vector3(lastX, lastY, 0.010), new Quaternion(0.000, 0.000, lastZ, lastW));
		PoseStamped poseStampedNew = transform.toPoseStampedMessage(
				GraphName.of(robotFrame),
				connectedNode.getCurrentTime(),
				pubMoveBaseSimpleGoal.newMessage());

		initialPose.getPose().setPose(poseStampedNew.getPose());
		initialPose.getHeader().setFrameId("map"); // Received initial pose with empty frame_id. You should always supply a frame_id.
		publishLastPose.publish(initialPose);
		Log.i(TAG, "已发布最后的坐标: " + lastX + ", " + lastY + ", " + lastZ + ", " + lastW);
	}


	/** 发布消息 */
	public void send(Context context){
		// sxg 已测试或运算是短路运算，第二个条件不会报空指针。
		if(pubCancel == null || pubCancel.newMessage() == null){

			Looper.prepare();
			Toast.makeText(context, "停止指令发送失败，请重试", Toast.LENGTH_SHORT).show();
			Looper.loop();

			return;
		}
		/** 被发布的类 */
		Float32MultiArray contentToSend = pubCancel.newMessage();
		contentToSend.setData(new float[]{0, 0, 0, 0});
		pubCancel.publish(contentToSend);
		Log.i(TAG, "send: 停止指令发送成功");
	}

	@Override
	public void onShutdown(VisualizationView view, Node node) {
		pubInitialpose.shutdown();
		pubMoveBaseSimpleGoal.shutdown();
		pubMoveBaseGoal.shutdown();
	}

	public void initHandler(Handler handler, int whatCancelOK) {
		this.mHandler = handler;
		this.whatCancelOK = whatCancelOK;
	}

	public boolean isGoingToGoal() {
		return isGoingToGoal;
	}

	public void setGoingToGoal(boolean goingToGoal) {
		isGoingToGoal = goingToGoal;
	}
}
