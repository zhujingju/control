package com.map;

/**
 * 用作最后的坐标值。
 * Created by Administrator on 2017/3/22-022.
 */
public class PoseCustom {
    private float x = 0f;
    private float y = 0f;
    private float z = 0f;
    private float w = 0f;

    public PoseCustom(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getW() {
        return w;
    }
}
