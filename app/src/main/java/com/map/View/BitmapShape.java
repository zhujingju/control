package com.map.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.opengl.GLUtils;
import android.util.Log;

import com.grasp.control.R;

import org.ros.android.view.visualization.VisualizationView;
import org.ros.android.view.visualization.shape.BaseShape;
import org.ros.rosjava_geometry.Transform;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/5/11-011.
 */

public class BitmapShape extends BaseShape {
    private final static String TAG = "Print-BitmapS";

    private float mWidth;
    private float mHeight;

    private int mPow2Width;
    private int mPow2Height;

    private float maxU = 1.0f;
    private float maxV = 1.0f;

    private Bitmap mBitmap = null;
    private int textureId = 0;

    private double pixelsPerMeter = 150;
    private Transform origin = null;


    public BitmapShape(Context context, int bitmapResId, double pixelsPerMeter) {
        super();
        init(context, bitmapResId);
        setLocation((int)(getTransform().getTranslation().getX()), (int)(getTransform().getTranslation().getY()));
        this.pixelsPerMeter = pixelsPerMeter;
        this.origin = getTransform();
    }

    public static int pow2(float size)
    {
        int small = (int)(Math.log((double)size)/Math.log(2.0f)) ;
        if ( (1 << small) >= size)
            return 1 << small;
        else
            return 1 << (small + 1);
    }

    // 构建，推迟到第一次绑定时
    public void init(Context context, int bitmapResId)
    {
        Bitmap bmp = null;
        if(bitmapResId <= 0) {
            bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.btn_map_robot);
        }else {
            bmp = BitmapFactory.decodeResource(context.getResources(), bitmapResId);
        }

        // 如果是标记的圆圈
        int diameter = (int)(Math.ceil(4.0f * 160 / 3));
        if (bitmapResId == R.mipmap.btn_map_oo) {

            // 定义矩阵对象
            Matrix matrix = new Matrix();
            // 缩放原图
            matrix.postScale((((float)diameter)/bmp.getWidth()), (((float)diameter)/bmp.getHeight()));
            //bmp.getWidth(), 500分别表示重绘后的位图宽高
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true); // x must be >= 0, x + width must be <= bitmap.width()， width and height must be > 0
        }

        mWidth = (float)(bmp.getWidth());
        mHeight = (float)(bmp.getHeight());

        mPow2Width =pow2(mWidth);
        mPow2Height = pow2(mHeight);

        // 如果是标记的圆圈
        if (bitmapResId == R.mipmap.btn_map_oo && (mPow2Width < diameter || mPow2Height < diameter)) {
            mPow2Width = diameter;
            mPow2Height = diameter;

            mPow2Width =pow2(mPow2Width);
            mPow2Height = pow2(mPow2Height);
        }

        maxU = mWidth/(float)mPow2Width;
        maxV = mHeight/(float)mPow2Height;

        Bitmap bitmap = Bitmap.createBitmap(mPow2Width, mPow2Height,
                bmp.hasAlpha() ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(0x0000ff);
        canvas.drawBitmap(bmp, 0, 0, null);
//        canvas.drawBitmap(bmp, - (bitmap.getWidth() / 2.0f), - (bitmap.getHeight() / 2.0f), null);

//        Log.i(TAG, "init: mWidth,mHeight=("+ mWidth+","+mHeight+"), mPow2Width,mPow2Height=(" +mPow2Width+","+mPow2Height+"), maxU,maxV=("+maxU+","+maxV+")");// mWidth,mHeight=(100.0,100.0), mPow2Width,mPow2Height=(128,128), maxU,maxV=(0.78125,0.78125)
        mBitmap = bitmap;
    }


    // 映射
    private FloatBuffer verticleBuffer = null;
    private FloatBuffer coordBuffer = null;

    private float[] vertex;
    private float[] coord;
    public void setLocation(float x, float y) {
//        if (mBitmap.getWidth() > 0 && mBitmap.getHeight() > 0) {
//            x -= mBitmap.getWidth();
//            y -= mBitmap.getHeight();
//        }
//        Log.i(TAG, "setLocation: x=" + x + ", y=" + y);
//        vertex = new float[]{
//
//                x,y,
//
//                x+mWidth, 0,
//
//                x, y+mHeight,
//
//                x+mWidth, y+mHeight,
//
//        };
//        Log.i(TAG, "setLocation: (x, y)=" + x + ", " + y + "), \t(mWidth, mHeight)=" + mWidth + ", " + mHeight + "), \t(maxU, maxV)=" + maxU + ", " + maxV + ")"); // (x, y)=0.0, 0.0), 	(mWidth, mHeight)=64.0, 64.0), 	(maxU, maxV)=1.0, 1.0)
        vertex = new float[]{
                x - mWidth / 2, y - mHeight / 2,
                x + mWidth / 2, y - mHeight / 2,
                x - mWidth / 2, y + mHeight / 2,
                x + mWidth / 2, y + mHeight / 2,

        };
//        coord = new float[]{
//                - maxU / 2, - maxV / 2,
//                maxU / 2, - maxV / 2,
//                - maxU / 2, maxV / 2,
//                maxU / 2, maxV / 2,
//        };

        coord = new float[]{
                0,0,

                maxU,0,

                0,maxV,

                maxU,maxV,
        };

        //准备顶点缓冲，画布
        ByteBuffer bb = ByteBuffer.allocateDirect(vertex.length * 4);
        bb.order(ByteOrder.nativeOrder());
        verticleBuffer = bb.asFloatBuffer();
        verticleBuffer.put(vertex);
        verticleBuffer.position(0);
        //准备纹理缓冲
        ByteBuffer coordbb = ByteBuffer.allocateDirect(coord.length * 4);
        coordbb.order(ByteOrder.nativeOrder());
        coordBuffer = coordbb.asFloatBuffer();
        coordBuffer.put(coord);
        coordBuffer.position(0);

    }
    // 绘制到屏幕上

    public void draw(GL10 gl)
    {
//        Log.i(TAG, "draw: ");

        gl.glEnable(GL10.GL_TEXTURE_2D); // invalid operation
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); // invalid operation
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        //  绑定
        this.bind(gl);

        // 缩放-start
        gl.glPushMatrix();
//        OpenGlTransform.apply(gl, getTransform());// 位置
        if (pixelsPerMeter > 0) {
            gl.glScalef((float)(1 / pixelsPerMeter), (float)(1 / pixelsPerMeter), 1.0f); // 白地图和绿背景的固定缩放，注销后手指仍可缩放。
        }else {
            Log.e(TAG, "draw: pixelsPerMeter <= 0, 出错了");
            gl.glScalef(1 / 150.0f, 1 / 150.0f, 1.0f); // 白地图和绿背景的固定缩放，注销后手指仍可缩放。
        }
        // 缩放-end

        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, verticleBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,0,4);

        // 缩放-start
        gl.glPopMatrix();
        // 缩放-end

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);

    }
    // 第一次会加载纹理数据
    public void bind(GL10 gl)
    {
        if (textureId ==0)
        {
            int[] textures = new int[1];
            gl.glGenTextures(1, textures, 0);
            textureId = textures[0];

            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0);

            mBitmap.recycle();
            mBitmap = null;
        }

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    }
    @Override
    protected void scale(VisualizationView view, GL10 gl) {
        // Counter adjust for the camera pixelsPerMeter.
//        gl.glScalef(1 / (float) view.getCamera().getZoom(), 1 / (float) view.getCamera().getZoom(),
//                1.0f); // 不缩放
    }

    @Override
    protected void drawShape(VisualizationView view, GL10 gl) {
        
        draw(gl);
        
    }
}

