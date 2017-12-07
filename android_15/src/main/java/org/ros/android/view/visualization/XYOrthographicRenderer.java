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

package org.ros.android.view.visualization;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.util.Log;

import org.ros.android.view.visualization.layer.Layer;
import org.ros.android.view.visualization.layer.TfLayer;
import org.ros.namespace.GraphName;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 画布之外的背景
 * Renders all layers of a navigation view.
 * 
 * @author damonkohler@google.com (Damon Kohler)
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */
public class XYOrthographicRenderer implements GLSurfaceView.Renderer {
  private static final String TAG = "Print-XYOr";

//  private static final Color BACKGROUND_COLOR = new Color(0.87f, 0.87f, 0.87f, 1.f);  // 浅灰
//  private static final Color BACKGROUND_COLOR = new Color(1.0f, 1.0f, 1.0f, 1.f); //纯白
//  private static final Color BACKGROUND_COLOR = Color.fromHexAndAlpha("6b6b6b", 1.f); // 背景787676,f8c806,6b6b6b只能是6位
  private static final Color BACKGROUND_COLOR = Color.fromHexAndAlpha("aaaaaa", 1.0f); // 背景787676,f8c806,6b6b6b只能是6位
  //要同OccupancyGridLayerCustom的COLOR_UNKNOWN一样

  private final VisualizationView view;
  private boolean isStop = false;

/** 画文字 start */
  //位图
  private Bitmap bitmap;
  //四边形的顶点坐标系
  private float[] vertex = new float[]{
          -2.5f,-2.5f,0,
          2.5f,-2.5f,0,
          -2.5f,2.5f,0,
          2.5f,2.5f,0
  };
  //纹理坐标系
  private float[] coord = new float[]{
          0,1.0f,
          1.0f,1.0f,
          0,0,
          1.0f,0
  };
  //纹理存储定义，一般用来存名称
  private int[] textures = new int[1];
  //顶点、纹理缓冲
  FloatBuffer vertexBuffer;
  FloatBuffer coordBuffer;


  /** 画文字 end */

  public XYOrthographicRenderer(VisualizationView view) {
    this.view = view;
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
//    Log.i(TAG, "onSurfaceChanged");
    Viewport viewport = new Viewport(width, height);
    viewport.apply(gl);
    view.getCamera().setViewport(viewport);
    gl.glMatrixMode(GL10.GL_MODELVIEW);
    gl.glEnable(GL10.GL_BLEND);
    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    gl.glDisable(GL10.GL_DEPTH_TEST);
    gl.glClearColor(BACKGROUND_COLOR.getRed(), BACKGROUND_COLOR.getGreen(),
        BACKGROUND_COLOR.getBlue(), BACKGROUND_COLOR.getAlpha());
    for (Layer layer : view.getLayers()) {
      layer.onSurfaceChanged(view, gl, width, height);
    }

  }

  @Override
  public void onDrawFrame(GL10 gl) {
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT); //原
    gl.glLoadIdentity();
    view.getCamera().apply(gl);
    drawLayers(gl); // java.util.ConcurrentModificationException

    //
  }

  private void drawLayers(GL10 gl) {
    if(isStop) {
      Log.e(TAG, "drawLayers: 停止渲染");
      return;
    }
    for (Layer layer : view.getLayers()) {  // java.util.ConcurrentModificationException
      gl.glPushMatrix();
      if (layer instanceof TfLayer) {
        GraphName layerFrame = ((TfLayer) layer).getFrame(); // RobotLayer的话题也在这里使用。
        if (layerFrame != null && view.getCamera().applyFrameTransform(gl, layerFrame)) {
          layer.draw(view, gl);
        }
      } else {
        layer.draw(view, gl);
      }
      gl.glPopMatrix();
    }
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//    Log.i(TAG, "onSurfaceCreated");
    for (Layer layer : view.getLayers()) {
      layer.onSurfaceCreated(view, gl, config);
    }


  }


  /** 停止之后，就不渲染了，为了迭代不报错 */
  public void setIsStop(boolean isStop) {
    this.isStop = isStop;
  }
}