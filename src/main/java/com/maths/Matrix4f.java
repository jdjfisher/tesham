package com.maths;



import com.maths.vectors.Vector2f;
import com.maths.vectors.Vector3f;
import com.utils.DataUtils;
import org.apache.commons.math3.util.FastMath;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class Matrix4f{

    public static final int ORDER = 4;
    public float[][] elements;

    public Matrix4f() {
        set(new float[ORDER][ORDER]);
    }

    public Matrix4f(float[][] elements){
        set(elements);
    }

    public Matrix4f(Matrix4f matrix4f){
        set(matrix4f);
    }

    public void set(Matrix4f matrix4f){
        set(matrix4f.elements);
    }

    public void set(float[][] elements){
        this.elements = elements;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        final int MIN_SPACING = 3;

        String[][] stringData = new String[ORDER][ORDER];
        int[] longestColStringElements = new int[ORDER];

        for(int col = 0; col < ORDER; col++) {
            int longestColStringElement = 0;

            for(int row = 0; row < ORDER; row++){
                String stringElement = Float.toString(elements[row][col]);

                stringData[row][col] = stringElement;

                int stringElementLength = stringElement.length();
                if(stringElementLength > longestColStringElement){
                    longestColStringElement = stringElementLength;
                }
            }

            longestColStringElements[col] = longestColStringElement;
        }

        for(int row = 0; row < ORDER; row++){
            stringBuilder.append("|");
            for (int i = 0; i < MIN_SPACING; i++){
                stringBuilder.append(" ");
            }
            for(int col = 0; col < ORDER; col++) {
                String stringElement = stringData[row][col];
                stringBuilder.append(stringElement);
                for (int i = 0; i < longestColStringElements[col] - stringElement.length() + MIN_SPACING; i++) {
                    stringBuilder.append(" ");
                }
            }
            stringBuilder.append("|\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        float[][] elements2 = ((Matrix4f)obj).elements;

        for(int row = 0; row < ORDER; row++){
            for(int col = 0; col < ORDER; col++){
                if(elements2[row][col] != this.elements[row][col]){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(DataUtils.toFloatArray(elements));
    }

    public FloatBuffer toFloatBuffer() {
        return DataUtils.toFloatBuffer(DataUtils.toFloatArray(elements));
    }

    public void identity(){
        set(Identity());
    }

    public void multiply(Matrix4f multiplier){
        set(Multiply(multiplier, this));
    }

    public void translate(Vector3f translation){
        multiply(Translation(translation));
    }

    public void stretch(Vector3f stretch){
        multiply(Stretch(stretch));
    }

    public void rotate(Quaternion rotation){
        multiply(QuaternionRotation(rotation));
    }

    ///////////////////////////////////////////////////STATIC///////////////////////////////////////////////////////////

    public static Matrix4f Identity() {
        return new Matrix4f(
                new float[][]{
                        {1, 0, 0, 0},
                        {0, 1, 0, 0},
                        {0, 0, 1, 0},
                        {0, 0, 0, 1}
                }
        );
    }

    public static Matrix4f Multiply(Matrix4f multiplier, Matrix4f multiplicand) {
        final Matrix4f result = new Matrix4f();
        float sum;
        for (int row = 0; row < ORDER; row++) {
            for (int col = 0; col < ORDER; col++) {
                sum = 0;
                for (int i = 0; i < ORDER; i++) {
                    sum += (multiplier.elements[row][i] * multiplicand.elements[i][col]);
                }
                result.elements[row][col] = sum;
            }
        }
        return result;
    }

    public static Matrix4f Perspective(float FOV, float aspectRatio, float zNear, float zFar){
        final Matrix4f result = new Matrix4f();
        final float fov = (float) FastMath.toRadians(FOV);
        final float zm = zFar - zNear;
        final float zp = zFar + zNear;
        result.elements[0][0] = (float) ((1/FastMath.tan(fov/2))/aspectRatio);
        result.elements[1][1] = (float) (1/FastMath.tan(fov/2));
        result.elements[2][2] = -zp / zm;
        result.elements[2][3] = -(2f * zFar * zNear) / zm;
        result.elements[3][2] = -1f;
        return result;
    }

    public static Matrix4f Orthographic(float left, float right, float top, float bottom, float zNear, float zFar){
        final Matrix4f result = Identity();
        result.elements[0][0] = 2f / (right - left);
        result.elements[1][1] = 2f / (top - bottom);
        result.elements[2][2] = -2f / (zFar - zNear);
        result.elements[0][3] = -(right + left) / (right - left);
        result.elements[1][3] = -(top + bottom) / (top - bottom);
        result.elements[2][3] = -(zFar + zNear) / (zFar - zNear);
        return result;
    }

    public static Matrix4f Orthographic2D(int left, int right, int top, int bottom){
        final Matrix4f result = Identity();
        result.elements[0][0] = 2f / (right - left);
        result.elements[0][3] = -(right + left) / (right - left);
        result.elements[1][1] = 2f / (top - bottom);
        result.elements[1][3] = -(top + bottom) / (top - bottom);
        result.elements[2][2] = -1f;
        return result;
    }

    public static Matrix4f Translation(Vector3f translation) {
        return Translation(translation.getX(), translation.getY(), translation.getZ());
    }

    public static Matrix4f Translation(float dX, float dY, float dZ) {
        Matrix4f result = Identity();
        result.elements[0][ORDER - 1] = dX;
        result.elements[1][ORDER - 1] = dY;
        result.elements[2][ORDER - 1] = dZ;
        return result;
    }

    public static Matrix4f Translation(Vector2f translation) {
        return Translation(translation.getX(), translation.getY());
    }

    public static Matrix4f Translation(float dX, float dY) {
        Matrix4f result = Identity();
        result.elements[0][ORDER - 1] = dX;
        result.elements[1][ORDER - 1] = dY;
        return result;
    }

    public static Matrix4f EulerRotation(Vector3f eulerRotation){
        return EulerRotation(eulerRotation.getX(), eulerRotation.getY(), eulerRotation.getZ());
    }

    public static Matrix4f EulerRotation(float eulerX, float eulerY, float eulerZ){
        final Matrix4f result = Identity();
        if(eulerX != 0) {
            result.multiply(EulerRotationX(eulerX));
        }
        if(eulerY != 0) {
            result.multiply(EulerRotationY(eulerY));
        }
        if(eulerZ != 0) {
            result.multiply(EulerRotationZ(eulerZ));
        }
        return result;
    }

    public static Matrix4f EulerRotationX(float eulerX) {
        final Matrix4f result = Identity();
        final double theta = FastMath.toRadians(eulerX);
        final float sinθ = (float) FastMath.sin(theta);
        final float cosθ = (float) FastMath.cos(theta);

        result.elements[1][1] =  cosθ;
        result.elements[1][2] = -sinθ;
        result.elements[2][1] =  sinθ;
        result.elements[2][2] =  cosθ;
        return result;
    }

    public static Matrix4f EulerRotationY(float eulerY) {
        final Matrix4f result = Identity();
        final double theta = FastMath.toRadians(eulerY);
        final float sinθ = (float) FastMath.sin(theta);
        final float cosθ = (float) FastMath.cos(theta);

        result.elements[0][0] = cosθ;
        result.elements[0][2] = sinθ;
        result.elements[2][0] = -sinθ;
        result.elements[2][2] = cosθ;
        return result;
    }

    public static Matrix4f EulerRotationZ(float eulerZ) {
        final Matrix4f result = Identity();
        final double theta = FastMath.toRadians(eulerZ);
        final float sinθ = (float) FastMath.sin(theta);
        final float cosθ = (float) FastMath.cos(theta);

        result.elements[0][0] = cosθ;
        result.elements[0][1] = -sinθ;
        result.elements[1][0] = sinθ;
        result.elements[1][1] = cosθ;
        return result;
    }

    public static Matrix4f QuaternionRotation(final Quaternion quaternion) {
        final Matrix4f result = Identity();
        final float x = quaternion.getX(), y = quaternion.getY(), z = quaternion.getZ(), w = quaternion.getW();
        result.elements[0][0] = 1 - 2 * y * y - 2 * z * z;
        result.elements[1][0] = 2 * x * y - 2 * z * w;
        result.elements[2][0] = 2 * x * z + 2 * y * w;

        result.elements[0][1] = 2 * x * y + 2 * z * w;
        result.elements[1][1] = 1 - 2 * x * x - 2 * z * z;
        result.elements[2][1] = 2 * y * z - 2 * x * w;

        result.elements[0][2] = 2 * x * z - 2 * y * w;
        result.elements[1][2] = 2 * y * z + 2 * x * w;
        result.elements[2][2] = 1 - 2 * x * x - 2 * y * y;

        result.elements[3][3] = 1;

        return result;
    }

    public static Matrix4f Stretch(Vector3f vec) {
        return Stretch(vec.getX(), vec.getY(), vec.getZ());
    }

    public static Matrix4f Stretch(float sfX, float sfY, float sfZ) {
        final Matrix4f result = Identity();
        if(sfX != 0) {
            result.multiply(StretchX(sfX));
        }
        if(sfY != 0) {
            result.multiply(StretchY(sfY));
        }
        if(sfZ != 0) {
            result.multiply(StretchZ(sfZ));
        }
        return result;
    }

    public static Matrix4f StretchX(float sfX) {
        final Matrix4f result = Identity();
        result.elements[0][0] = sfX;
        return result;
    }

    public static Matrix4f StretchY(float sfY) {
        final Matrix4f result = Identity();
        result.elements[1][1] = sfY;
        return result;
    }

    public static Matrix4f StretchZ(float sfZ) {
        final Matrix4f result = Identity();
        result.elements[2][2] = sfZ;
        return result;
    }

    public static Matrix4f Enlargement(float sf) {
        final Matrix4f result = Identity();
        for (int i = 0; i < 3; i++) {
            result.elements[i][i] = sf;
        }
        return result;
    }
}
