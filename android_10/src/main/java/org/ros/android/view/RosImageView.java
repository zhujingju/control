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

package org.ros.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import org.ros.android.MessageCallable;
import org.ros.android.view.camera.ReceiveCameraListener;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

/**
 * Displays incoming sensor_msgs/CompressedImage messages.
 * 
 * @author ethan.rublee@gmail.com (Ethan Rublee)
 * @author damonkohler@google.com (Damon Kohler)
 */
public class RosImageView<T> extends ImageView implements NodeMain {
  private final String TAG = "Print-RosImageView";
  private String topicName;
  private String messageType;
  private MessageCallable<Bitmap, T> callable;
  private ReceiveCameraListener receiveCameraListener;
  private boolean isFirstReceive = true;

  public RosImageView(Context context) {
    super(context);
  }

  public RosImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RosImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public void setMessageToBitmapCallable(MessageCallable<Bitmap, T> callable) {
    this.callable = callable;
  }

  public void setReceiveCameraListener(ReceiveCameraListener receiveCameraListener) {
    this.receiveCameraListener = receiveCameraListener;
  }

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("ros_image_view");
  }

  private Subscriber<T> subscriber;
  @Override
  public void onStart(ConnectedNode connectedNode) {
    this.connectedNode = connectedNode;
//    Subscriber<T> subscriber = connectedNode.newSubscriber(topicName, messageType);
    subscriber = connectedNode.newSubscriber(topicName, messageType);
    subscriber.addMessageListener(new MessageListener<T>() {
      @Override
      public void onNewMessage(final T message) {
//        Log.i(TAG, "onNewMessage"); //想看是否实时刷新。确实是实时刷新
        if(isFirstReceive){
          isFirstReceive = false;
          Log.i(TAG, "onNewMessage");
          if (receiveCameraListener != null){
            // 表示收到视频了。
            receiveCameraListener.onSuccess();
            Log.i(TAG, "调用了onSuccess()");
          }
        }

        post(new Runnable() {
          @Override
          public void run() {
            setImageBitmap(callable.call(message)); // OutOfMemoryError
          }
        });
        postInvalidate();
      }
    });

  }

  public void setIsFirstReceive(boolean isFirstReceive) {
    this.isFirstReceive = isFirstReceive;
  }

  @Override
  public void onShutdown(Node node) {
  }

  @Override
  public void onShutdownComplete(Node node) {
  }

  @Override
  public void onError(Node node, Throwable throwable) {
  }
  /** 获取connectedNode，为了重连ros主机使得app换数据，旧的connectedNode需要先清理掉 */
  public ConnectedNode getConnectedNode() {
    return connectedNode;
  }

  private ConnectedNode connectedNode;
}
