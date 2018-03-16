package com.maths.vectors;

import java.util.Objects;

public class Vector2i {

	public static final int SIZE = 2;

	public static Vector2i Identity(){
		return new Vector2i(0,0);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Vector2i() {
		this(Identity());
	}

	public Vector2i(Vector2i vec){
		this(vec.getX(), vec.getY());
	}

	public Vector2i(int x, int y) {
		set(x, y);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void set(Vector2i vec){
		this.set(vec.x, vec.y);
	}

	public void set(int x, int y){
		setX(x);
		setY(y);
	}

	public void add(Vector2i deltaVec){
		this.add(deltaVec.x, deltaVec.y);
	}

	public void add(int dx, int dy){
		addX(dx);
		addY(dy);
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


	public void multiply(final int multiplier){
		set(Multiply(this, multiplier));
	}

	public void negative(){
		set(Negative(this));
	}

	@Override
	public String toString() {
		return String.format("Vec3f: [ %d, %d ]", x, y);
	}

	@Override
	public boolean equals(Object obj) {
		Vector2i vec = (Vector2i)obj;
		return x == vec.x && y == vec.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	///////////////////////////////////////////////////STATIC///////////////////////////////////////////////////////////

	public static Vector2i Add(final Vector2i vec1, final Vector2i vec2){
		return new Vector2i(
				vec1.x + vec2.x,
				vec1.y + vec2.y
		);
	}

	public static Vector2i Add(final Vector2i vec1, final Vector2i vec2, final Vector2i... vector2fs) {
		Vector2i result = Vector2i.Add(vec1, vec2);
		for (Vector2i vec : vector2fs) {
			result.add(vec);
		}
		return result;
	}

	public static Vector2i Subtract(final Vector2i vec1, final Vector2i vec2){
		return new Vector2i(
				vec1.x - vec2.x,
				vec1.y - vec2.y
		);
	}

	public static Vector2i Multiply(final Vector2i multiplicand, final int multiplier){
		return new Vector2i(
				multiplicand.x * multiplier,
				multiplicand.y * multiplier
		);
	}

	public static Vector2i Negative(final Vector2i vec){
		return Multiply(vec, -1);
	}
}
