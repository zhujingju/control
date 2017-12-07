/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ros.android.view.visualization.layer;

import android.util.Log;

import org.ros.android.view.visualization.VisualizationView;
import org.ros.android.view.visualization.shape.Shape;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public class RobotLayer extends DefaultLayer implements TfLayer {
  private final String TAG = "Print-Rob";

  private final GraphName frame;

  private Shape shape;
//  private Subscriber<std_msgs.> subRob = null;
//  private String topic;

  public RobotLayer(GraphName frame) {
    this.frame = frame;
  }

  public RobotLayer(String frame) {
    this(GraphName.of(frame));
//    this.topic = frame;
  }

  public void setShape(Shape shape) {
    this.shape = shape;
    this.shape.getTransform().toMatrix(); // 不加这句，经常出不来，加了偶尔出不来。
  }

  int count = 0;
  @Override
  public void draw(VisualizationView view, GL10 gl) {
    if (shape != null) {
      shape.draw(view, gl); // 蓝色箭头
//      Log.i(TAG, "draw: "+shape.getTransform().toString()); // draw: Transform<Vector3<x: -0.3361, y: -0.0251, z: 0.0000>, Quaternion<x: 0.0000, y: 0.0000, z: 0.1146, w: 0.9934>> 刷屏
//        Log.i(TAG, "draw: "+view.getCamera().getScreenTransform(GraphName.of("odom")).toString()+", " +view.getCamera().getCameraToRosTransform().toString()); //  draw: Transform<Vector3<x: NaN, y: NaN, z: NaN>, Quaternion<x: NaN, y: NaN, z: NaN, w: NaN>>, Transform<Vector3<x: 0.0000, y: 0.0000, z: 0.0000>, Quaternion<x: 0.0000, y: 0.0000, z: 0.0000, w: 1.0000>>
    }
    else {
      Log.e(TAG, "draw: shape is null.");
    }
  }

  @Override
  public void onStart(VisualizationView view, ConnectedNode connectedNode) {
//    shape = new PixelSpacePoseShape();
//    this.shape = new GoalShape();
//    shape.getTransform().toMatrix();

//    Subscriber<nav_msgs.Odometry> subscriber = connectedNode.newSubscriber("/grobot/odom", Odometry._TYPE);
//    subscriber.addMessageListener(new MessageListener<Odometry>() {
//      @Override
//      public void onNewMessage(Odometry odometry) {
////        Log.i(TAG, "onNewMessage: odometry = " +odometry+","+odometry.getHeader().getFrameId()+", " + odometry.getChildFrameId()+", " + odometry.getPose().getPose().getPosition().getX()+", " + odometry.getPose().getPose().getPosition().getY()+", " + odometry.getPose().getPose().getPosition().getZ() +", \n shape, " + shape.getTransform().toString()); // 刷屏，odometry和shape的坐标不同，shape都是0，odometry与主机打印相同。
//
//        // shape值为0时不打印
////        onNewMessage: odometry = MessageImpl<nav_msgs/Odometry>,odom, base_footprint, -0.3907283152371685, -0.012573384354402495, 0.0,
////                shape, Transform<Vector3<x: 0.0000, y: 0.0000, z: 0.0000>, Quaternion<x: 0.0000, y: 0.0000, z: 0.0000, w: 1.0000>>
//
//        // shape值不为0时也不打印
////        onNewMessage: odometry = MessageImpl<nav_msgs/Odometry>,odom, base_footprint, -0.3360571078674626, -0.025066801241071916, 0.0,
////                shape, Transform<Vector3<x: -0.3361, y: -0.0251, z: 0.0000>, Quaternion<x: 0.0000, y: 0.0000, z: 0.1146, w: 0.9934>>
//
//        if(Math.abs(odometry.getPose().getPose().getPosition().getX() - shape.getTransform().getTranslation().getX()) > 0.0001 ||
//                Math.abs(odometry.getPose().getPose().getPosition().getY() - shape.getTransform().getTranslation().getY()) > 0.0001 ||
//                Math.abs(odometry.getPose().getPose().getOrientation().getZ() - shape.getTransform().getRotationAndScale().getZ()) > 0.0001 ||
//                Math.abs(odometry.getPose().getPose().getOrientation().getW() - shape.getTransform().getRotationAndScale().getW()) > 0.0001){
//          Log.i(TAG, "onNewMessage: 位置不同，重新设置shape位置"); // 第5位有值，则可能向前进1，相差1的时候其实是相等的
//          shape.setTransform(new Transform(new Vector3(odometry.getPose().getPose().getPosition().getX(), odometry.getPose().getPose().getPosition().getY(), 0D),
//                  new Quaternion(0D, 0D, odometry.getPose().getPose().getOrientation().getZ(), odometry.getPose().getPose().getOrientation().getW())));
//        }
//
//      }
//    });
  }

  @Override
  public GraphName getFrame() {
    return frame;
  }

  public Shape getShape(){
    return this.shape;
  }

}