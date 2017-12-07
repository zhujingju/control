package com.map.make_a_map;

import android.util.Log;

import org.ros.android.view.visualization.Color;
import org.ros.android.view.visualization.Vertices;
import org.ros.android.view.visualization.VisualizationView;
import org.ros.android.view.visualization.shape.GoalShape;
import org.ros.android.view.visualization.shape.Shape;
import org.ros.rosjava_geometry.Transform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * 在地图上标记各个位置。
 * Created by Administrator on 2017/3/8-008.
 */

/** 仿照RobotLayer.java */
//public class FlagBean extends DefaultLayer{
public class FlagBean {//extends DefaultLayer{

    /** 形状 */
    private Shape shape;
    /** 位置 */
    private Transform pose;
    /** id号 */
    private String id;
    /** 位置的名称 */
    private String name;
    /** 是否已取到位置 */
    private boolean isReady = false;

    private double x;
    private double y;

    public FlagBean(String name, double x, double y) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public FlagBean(String id, String name, double x, double y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public FlagBean(Shape shape, Transform pose, String id, String name) {
        this.shape = shape;
        this.pose = pose;
        this.id = id;
        this.name = name;

        this.shape.setTransform(pose);
    }
    public FlagBean(Shape shape, String id, String name) {
        this.shape = shape;
        this.pose = shape.getTransform();
        this.id = id;
        this.name = name;

        this.shape.setTransform(pose);
    }
    public FlagBean(String id, String name, Transform pose) {
//        this.shape = shape;
//        this.pose = shape.getTransform();
        this.pose = pose;
        this.id = id;
        this.name = name;

//        this.shape.setTransform(pose);
    }



    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Transform getPose() {
        return pose;
    }

    public void setPose(Transform pose) {
        this.pose = pose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setIsReady(boolean isReady) {
        this.isReady = isReady;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

//    @Override
//    public String toString() {
//        return "FlagBean.toString: "+super.toString()+", name: "+getName()+", id: "+getId()+", x: "+getX()+", y: "+getY()+", shape: "+getShape();
//    }
}

///** 没有shape的标记 */
//class FlagTemp {
//
//}

//class PixcelFlagShape extends BaseShape {
class PixcelFlagShape extends GoalShape {

    @Override
    protected void scale(VisualizationView view, GL10 gl) {
//        // Adjust for metric scale definition of MetricSpacePoseShape vertices.
//        gl.glScalef(PIXELS_PER_METER, 250.f, 1.f); // 注销后圆圈画不出来。
//        // Counter adjust for the camera pixelsPerMeter.
//        gl.glScalef(1 / (float) view.getCamera().getZoom(), 1 / (float) view.getCamera().getZoom(), 1.0f);  // 注销后圆圈画不出来。

    }

    private static final String TAG = "Print-RoundFlagShape";

//    private FloatBuffer vertices;
//    private float size;
    private float[] vertices = new float[720];//720
    private static FloatBuffer verBuffer ;
    private static final Color COLOR = Color.fromHexAndAlpha("ff5913", 0.1f);//0.1很透明，1.0不透明
    private static final float WIDTH = 5.0f;
//    private static float radius = 0.5f; //0.08f; //从0.1到1，0.5标记的时候被蓝箭头完全遮住了看不到。
    private static float radius = 80f; //0.5333333333333333f; // 80f; //0.08f; //从0.1到1，0.5标记的时候被蓝箭头完全遮住了看不到。
    private boolean isSolid = false; // 默认不是实心圆

    private double pixelsPerMeter = 150;


    public PixcelFlagShape(double pixelsPerMeter, float radius){
//        super(glText, name);
        this.pixelsPerMeter = pixelsPerMeter;
        this.radius = radius; // (float)(radius * pixelsPerMeter / zoomModulle); 半径值必须一样
        init();
        setColor(COLOR);
    }

    @Override
    public void drawShape(VisualizationView view, GL10 gl) {
        // 缩放-start
        gl.glPushMatrix();
//        OpenGlTransform.apply(gl, getTransform());// 位置
//        double pixelsPerMeter = 150d;
        if (pixelsPerMeter > 0) {
            gl.glScalef((float)(1 / pixelsPerMeter), (float)(1 / pixelsPerMeter), 1.0f); // 白地图和绿背景的固定缩放，注销后手指仍可缩放。
        }else {
            gl.glScalef(1 / 150.0f, 1 / 150.0f, 1.0f); // 白地图和绿背景的固定缩放，注销后手指仍可缩放。
        }
        // 缩放-end



        if (isSolid) {
            Vertices.drawSolidRound(gl, verBuffer, getColor(), WIDTH);//画圆，实心
        }
        else {
            Vertices.drawCircle(gl, verBuffer, getColor(), WIDTH); // 空心
        }


        // 缩放-start
        gl.glPopMatrix();
        // 缩放-end


    }
    public void init()
    {
        Log.i(TAG, "init()");
        //初始化圆形数据
        for (int i = 0; i < vertices.length; i += 2) {
            // x 坐标
            vertices[i]   =  (float) (Math.cos(DegToRad(i)) * radius);//(float) (Math.cos(DegToRad(i))
            // * 1)
            // y 坐标 0.2是指半径
            vertices[i+1] =  (float) (Math.sin(DegToRad(i)) * radius);//(float) (Math.sin(DegToRad(i))
            // * 1)
        }
        //设置圆形顶点数据
        ByteBuffer qbb = ByteBuffer.allocateDirect(vertices.length * 4);
        qbb.order(ByteOrder.nativeOrder());
        verBuffer = qbb.asFloatBuffer();
        verBuffer.put(vertices);
        verBuffer.position(0);
    }

    public float DegToRad(float deg)
    {
        return (float) (3.14159265358979323846 * deg / 180.0);
    }


    private static final float PIXELS_PER_METER = 250.f;

}