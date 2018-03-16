package com.maths.vectors;

import java.util.Objects;

public class Vector3i {

	public static final int SIZE = 3;

	public static Vector3i Identity(){
		return new Vector3i(0,0,0);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Vector3i() {
		this(Identity());
	}

	public Vector3i(Vector3i vec){
		this(vec.getX(), vec.getY(), vec.getZ());
	}

	public Vector3i(int x, int y, int z) {
		set(x, y, z);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void set(Vector3i vec){
		this.set(vec.x, vec.y, vec.z);
	}

	public void set(int x, int y, int z){
		setX(x);
		setY(y);
		setZ(z);
	}

	public void add(Vector3i deltaVec){
		this.add(deltaVec.x, deltaVec.y, deltaVec.z);
	}

	public void add(int dx, int dy, int dz){
		addX(dx);
		addY(dy);
		addZ(dz);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private int x;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void addX(int dx) {
		setX(getX() + dx);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private int y;

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void addY(int dy) {
		setY(getY() + dy);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private int z;

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public void addZ(int dz) {
		setZ(getZ() + dz);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void multiply(final int multiplier){
		set(Multiply(this, multiplier));
	}

	public void negative(){
		set(Negative(this));
	}

	@Override
	public String toString() {
		return String.format("Vec3f: [ %d, %d, %d ]", x, y, z);
	}

	@Override
	public boolean equals(Object obj) {
		Vector3i vec = (Vector3i)obj;
		return x == vec.x && y == vec.y && z == vec.z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	///////////////////////////////////////////////////STATIC///////////////////////////////////////////////////////////

	public static Vector3i Add(final Vector3i vec1, final Vector3i vec2){
		return new Vector3i(
				vec1.x + vec2.x,
				vec1.y + vec2.y,
				vec1.z + vec2.z
		);
	}

	public static Vector3i Add(final Vector3i vec1, final Vector3i vec2, final Vector3i... vector2fs) {
		Vector3i result = Vector3i.Add(vec1, vec2);
		for (Vector3i vec : vector2fs) {
			result.add(vec);
		}
		return result;
	}

	public static Vector3i Subtract(final Vector3i vec1, final Vector3i vec2){
		return new Vector3i(
				vec1.x - vec2.x,
				vec1.y - vec2.y,
				vec1.z - vec2.z
		);
	}

	public static Vector3i Multiply(final Vector3i multiplicand, final int multiplier){
		return new Vector3i(
				multiplicand.x * multiplier,
				multiplicand.y * multiplier,
				multiplicand.z * multiplier
		);
	}

	public static Vector3i Negative(final Vector3i vec){
		return Multiply(vec, -1);
	}
}
