package com.engine.core;

import com.graphics.component.mesh._3D.Mesh3D;
import com.maths.Matrix4f;
import com.maths.TransformationSet;
import com.maths.vectors.Vector3f;
import com.maths.vectors.Vector4f;

/**
 * Created by Jordan Fisher on 02/07/2017.
 */
public class FrustumCuller {

    private static final Vector4f[] frustumPlanes = initPlanes();

    private FrustumCuller(){ }

    private static Vector4f[] initPlanes(){
        Vector4f[] frustumPlanes = new Vector4f[6];
        for (int i = 0; i < frustumPlanes.length; i++){
            frustumPlanes[i] = new Vector4f();
        }
        return frustumPlanes;
    }

    public static void setClipMatrix(Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        setClipMatrix(Matrix4f.Multiply(projectionMatrix, viewMatrix));
    }

    public static void setClipMatrix(Matrix4f clipMatrix) {
        float[][] e = clipMatrix.elements;

        frustumPlanes[0].set(e[3][0] + e[0][0], e[3][1] + e[0][1], e[3][2] + e[0][2], e[3][3] + e[0][3]);
        frustumPlanes[1].set(e[3][0] - e[0][0], e[3][1] - e[0][1], e[3][2] - e[0][2], e[3][3] - e[0][3]);
        frustumPlanes[2].set(e[3][0] + e[1][0], e[3][1] + e[1][1], e[3][2] + e[1][2], e[3][3] + e[1][3]);
        frustumPlanes[3].set(e[3][0] - e[1][0], e[3][1] - e[1][1], e[3][2] - e[1][2], e[3][3] - e[1][3]);
        frustumPlanes[4].set(e[3][0] + e[2][0], e[3][1] + e[2][1], e[3][2] + e[2][2], e[3][3] + e[2][3]);
        frustumPlanes[5].set(e[3][0] - e[2][0], e[3][1] - e[2][1], e[3][2] - e[2][2], e[3][3] - e[2][3]);

        for (int i = 0; i < frustumPlanes.length; i++){
            frustumPlanes[i].normalize3();
        }
    }

    public static boolean isInsideFrustum(Mesh3D mesh3D, TransformationSet transformationSet) {
        return isInsideFrustum(transformationSet.getPosition(), mesh3D.getBoundingSphereRadius() * transformationSet.getScale());
    }

    public static boolean isInsideFrustum(Vector3f position, float objectRadius) {
        for (int i = 0; i < frustumPlanes.length; i++) {
            Vector4f plane = frustumPlanes[i];
            if (plane.getX() * position.getX() + plane.getY() * position.getY() + plane.getZ() * position.getZ() + plane.getW() <= - objectRadius) {
                return false;
            }
        }
        return true;
    }
}
