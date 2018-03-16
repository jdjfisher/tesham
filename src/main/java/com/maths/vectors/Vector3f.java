package com.maths.vectors;

import com.maths.Maths;
import com.maths.Matrix4f;
import com.maths.Quaternion;
import org.apache.commons.math3.util.FastMath;

import java.util.Objects;

public class Vector3f {

	public static final int SIZE = 3;

	public static Vector3f Identity() {
		return new Vector3f(0,0,0);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Vector3f() {
        this(Identity());
    }

    public Vector3f(Vector3f vec){
        this(vec.getX(), vec.getY(), vec.getZ());
    }

    public Vector3f(float x, float y, float z) {
        set(x, y, z);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void set(Vector3f vec){
        this.set(vec.x, vec.y, vec.z);
    }

    public void set(float x, float y, float z){
        setX(x);
        setY(y);
        setZ(z);
    }

    public void add(Vector3f deltaVec){
        this.add(deltaVec.x, deltaVec.y, deltaVec.z);
    }

    public void add(float dx, float dy, float dz){
        addX(dx);
        addY(dy);
        addZ(dz);
    }

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private float x;

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void addX(float dx) {
		setX(getX() + dx);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private float y;

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void addY(float dy) {
		setY(getY() + dy);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private float z;

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public void addZ(float dz) {
		setZ(getZ() + dz);
	}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public float getLength() {
		return Length(this);
	}

	public float getLargestComponent(){
		return LargestComponent(this);
	}

    public float getPhi() {
        return (float)FastMath.toDegrees(FastMath.atan2(FastMath.sqrt(x * x + z * z), y));
    }

    public float getTheta() {
        return (float)FastMath.toDegrees(FastMath.atan2(z, x));
    }

    public void multiply(final float multiplier){
		set(Multiply(this, multiplier));
	}

    public void multiply(final Quaternion multiplier){
        set(Multiply(this, multiplier));
    }

	public void multiply(final Matrix4f multiplier){
	    set(Multiply(this, multiplier));
    }

    public void normalize(){
        set(Normalize(this));
    }

    public void negate(){
    	set(Negative(this));
	}

	@Override
	public String toString() {
		return String.format("Vec3f: [ %f, %f, %f ]", x, y, z);
	}

	@Override
	public boolean equals(Object obj) {
    	Vector3f vec = (Vector3f)obj;
		return x == vec.x && y == vec.y && z == vec.z;
	}

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

	///////////////////////////////////////////////////STATIC///////////////////////////////////////////////////////////

	public static Vector3f Add(final Vector3f vec1, final Vector3f vec2){
		return new Vector3f(
				vec1.x + vec2.x,
				vec1.y + vec2.y,
				vec1.z + vec2.z
		);
	}

	public static Vector3f Add(final Vector3f vec1, final Vector3f vec2, final Vector3f... vector3fs) {
		Vector3f result = Vector3f.Add(vec1, vec2);
		for (Vector3f vec : vector3fs) {
			result.add(vec);
		}
		return result;
	}

	public static Vector3f Subtract(final Vector3f vec1, final Vector3f vec2){
		return new Vector3f(
				vec1.x - vec2.x,
				vec1.y - vec2.y,
				vec1.z - vec2.z
		);
	}

	public static Vector3f Multiply(final Vector3f multiplicand, final float multiplier){
		return new Vector3f(
				multiplicand.x * multiplier,
				multiplicand.y * multiplier,
				multiplicand.z * multiplier
		);
	}

	public static Vector3f Multiply(final Vector3f multiplicand, final Quaternion multiplier){
		final Vector3f quatrtnionXYZ = new Vector3f(multiplier.getX(), multiplier.getY(), multiplier.getZ());
		final Vector3f vec = Multiply(MultiplyCrossProduct(multiplicand, quatrtnionXYZ), 2);
		return Add(multiplicand, Multiply(vec, multiplier.getW()), MultiplyCrossProduct(vec, quatrtnionXYZ));
	}

    public static Vector3f MultiplyAlt(final Vector3f multiplicand, final Quaternion multiplier){
	    return Multiply(multiplicand, Matrix4f.QuaternionRotation(multiplier));
    }

    public static Vector3f Multiply(final Vector3f multiplicand, final Matrix4f multiplier){
        final float[] component = new float[Matrix4f.ORDER];
        for (int row = 0; row < Matrix4f.ORDER; row++) {
            component[row] = multiplier.elements[row][0] * multiplicand.getX() +  multiplier.elements[row][1] * multiplicand.getY() +  multiplier.elements[row][2] * multiplicand.getZ() +  multiplier.elements[row][3];
        }
        return new Vector3f(component[0], component[1], component[2]);
    }

	public static float MultiplyDotProduct(final Vector3f multiplicand, final Vector3f multiplier){
		return multiplicand.x * multiplier.x + multiplicand.y * multiplier.y + multiplicand.z * multiplier.z;
	}

	public static Vector3f MultiplyCrossProduct(final Vector3f multiplicand, final Vector3f multiplier){
		return new Vector3f(
				multiplier.z * multiplicand.y - multiplier.y * multiplicand.z,
				multiplier.x * multiplicand.z - multiplier.z * multiplicand.x,
				multiplier.y * multiplicand.x - multiplier.x * multiplicand.y
		);
	}

	public static float Length(final Vector3f vec){
		return (float)FastMath.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
	}

	public static Vector3f Normalize(final Vector3f vec){
		return Multiply(vec, 1 / vec.getLength());
	}

	public static Vector3f Negative(final Vector3f vec){
		return Multiply(vec,-1);
	}

	public static float LargestComponent(final Vector3f vec){
		return Maths.max(vec.x, vec.y, vec.z);
	}

}
