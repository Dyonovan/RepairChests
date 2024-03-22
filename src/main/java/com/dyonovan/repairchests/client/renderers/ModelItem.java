package com.dyonovan.repairchests.client.renderers;

import org.joml.Vector3f;

public class ModelItem {

    private final Vector3f center;
    private final float size;

    private Vector3f centerScaled;
    private Float sizeScaled;

    public ModelItem(Vector3f center, float size) {
        this.center = center;
        this.size = size;
    }

    public Vector3f getCenter() {
        return center;
    }

    public float getSize() {
        return size;
    }

    public Vector3f getCenterScaled() {
        if (centerScaled == null) {
            centerScaled = new Vector3f(center);
            centerScaled.mul(1f / 16f);
        }
        return centerScaled;
    }

    public Float getSizeScaled() {
        if (sizeScaled == null) {
            sizeScaled = size / 16f;
        }
        return sizeScaled;
    }
}
