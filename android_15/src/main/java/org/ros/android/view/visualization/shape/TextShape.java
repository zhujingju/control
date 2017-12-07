package org.ros.android.view.visualization.shape;

import android.util.Log;

import org.ros.android.view.visualization.Vertices;
import org.ros.android.view.visualization.VisualizationView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import uk.co.blogspot.fractiousg.texample.GLText;

public class TextShape extends BaseShape {
  private final String TAG = "Print-TextShape";

  private final GLText glText;
  private final String text;

  private float x;
  private float y;
  private FloatBuffer lines;


  private double pixelsPerMeter = 150;

//  private float[] verticesArray = new float[720];//720
//  private static FloatBuffer verBuffer ;
//  private static final Color COLOR = Color.fromHexAndAlpha("ff5913", 0.1f);//0.1很透明，1.0不透明
//  private static final float WIDTH = 5.0f;
//  private static final float RADIUS = 0.1f; //从0.1到1，0.5标记的时候被蓝箭头完全遮住了看不到。

  public TextShape(GLText glText, String text, double pixelsPerMeter) {
//    init();
//    setColor(COLOR);


    this.glText = glText;
    this.text = text;
    lines = Vertices.allocateBuffer(4 * 3);
    init();

    this.pixelsPerMeter = pixelsPerMeter;
  }

  public void setOffset(float x, float y) {
    this.x = x;
    this.y = y;
    lines.put(0.f);
    lines.put(0.f);
    lines.put(0.f);

    lines.put(x);
    lines.put(y);
    lines.put(0.f);

    lines.put(x);
    lines.put(y);
    lines.put(0.f);

    lines.put(x + glText.getLength(text));
    lines.put(y);
    lines.put(0.f);

    lines.flip();
  }

  @Override
  protected void scale(VisualizationView view, GL10 gl) {
//    // Counter adjust for the camera pixelsPerMeter.
//    gl.glScalef(1 / (float) view.getCamera().getZoom(), 1 / (float) view.getCamera().getZoom(),
//        1.0f);
  }

  @Override
  protected void drawShape(VisualizationView view, GL10 gl) {
//    Vertices.drawLines(gl, lines, getColor(), 3.f);
//    Vertices.drawLines(gl, lines, Color.fromHexAndAlpha("00ff00", 1.0f), 3.f);//没有绿的出现
//    Vertices.drawLineLoop(gl, verBuffer, Color.fromHexAndAlpha("00ff00", 1.0f), 5.0f);
//    gl.glClear(GL10.GL_COLOR_BUFFER_BIT); // clear之后，以前的图像都没了。


    //
//    Vertices.drawCircle(gl, verBuffer, getColor(), WIDTH);


    gl.glEnable(GL10.GL_TEXTURE_2D);
    glText.begin(getColor().getRed(), getColor().getGreen(), getColor().getBlue(), getColor()
        .getAlpha());






//    glText.draw(text, x, y);
    glText.drawC(text, x, y);
//    glText.end();


    glText.endBeforePush();

    gl.glPushMatrix();
//    OpenGlTransform.apply(gl, this.origin);// 位置
//    double pixelsPerMeter = 150d;
    if (pixelsPerMeter > 0) {
      gl.glScalef((float)(1 / pixelsPerMeter), (float)(1 / pixelsPerMeter), 1.0f); // 白地图和绿背景的固定缩放，注销后手指仍可缩放。
    }else {
      gl.glScalef(1 / 150.0f, 1 / 150.0f, 1.0f); // 白地图和绿背景的固定缩放，注销后手指仍可缩放。
    }

    glText.endDraw();

    gl.glPopMatrix();

    glText.endAfterPop();



//    drawCircle(gl);

    gl.glDisable(GL10.GL_TEXTURE_2D);


  }

  public void init()
  {
    Log.i(TAG, "init()");
    //初始化圆形数据
    for (int i = 0; i < verticesArray.length; i += 2) {
      // x 坐标
      verticesArray[i]   =  (float) (Math.cos(DegToRad(i)) * RADIUS);//(float) (Math.cos(DegToRad(i))
      // * 1)
      // y 坐标 0.2是指半径
      verticesArray[i+1] =  (float) (Math.sin(DegToRad(i)) * RADIUS);//(float) (Math.sin(DegToRad(i))
      // * 1)
    }
    //设置圆形顶点数据
    ByteBuffer qbb = ByteBuffer.allocateDirect(verticesArray.length * 4);
    qbb.order(ByteOrder.nativeOrder());
    verBuffer = qbb.asFloatBuffer();
    verBuffer.put(verticesArray);
    verBuffer.position(0);
  }

  public float DegToRad(float deg)
  {
    return (float) (3.14159265358979323846 * deg / 180.0);
  }


  private static final float WIDTH = 10.0f;
  private static final float RADIUS = 0.08f; //从0.1到1，0.5标记的时候被蓝箭头完全遮住了看不到。

  private float[] verticesArray = new float[720];//720
  private static FloatBuffer verBuffer ;
}
