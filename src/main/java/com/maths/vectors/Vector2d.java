package com.maths.vectors;

import org.apache.commons.math3.util.FastMath;

import java.util.Objects;

public class Vector2d {

	public static final int SIZE = 2;

	public static Vector2d Identity(){
		return new Vector2d(0, 0);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Vector2d() {
		this(Identity());
	}

	public Vector2d(Vector2d vec){
		this(vec.getX(), vec.getY());
	}

	public Vector2d(double x, double y) {
		set(x, y);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void set(Vector2d vec){
		this.set(vec.x, vec.y);
	}

	public void set(double x, double y){
		setX(x);
		setY(y);
	}

	public void add(Vector2d deltaVec){
		this.add(deltaVec.x, deltaVec.y);
	}

	public void add(double dx, double dy){
		addX(dx);
		addY(dy);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private double x;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void addX(double dx) {
		setX(getX() + dx);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private double y;

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void addY(double dy) {
		setY(getY() + dy);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public double getLength() {
		return (double) FastMath.sqrt(x * x + y * y);
	}

	public void multiply(final double multiplier){
		set(Multiply(this, multiplier));
	}

	public void normalize(){
		set(Normalize(this));
	}

	public void negative(){
		set(Negative(this));
	}

	@Override
	public String toString() {
		return String.format("Vec3f: [ %f, %f ]", x, y);
	}

	@Override
	public boolean equals(Object obj) {
		Vector2d vec = (Vector2d)obj;
		return x == vec.x && y == vec.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	///////////////////////////////////////////////////STATIC///////////////////////////////////////////////////////////

	public static Vector2d Add(final Vector2d vec1, final Vector2d vec2){
		return new Vector2d(
				vec1.x + vec2.x,
				vec1.y + vec2.y
		);
	}

	public static Vector2d Add(final Vector2d vec1, final Vector2d vec2, final Vector2d... vector2ds) {
		Vector2d result = Vector2d.Add(vec1, vec2);
		for (Vector2d vec : vector2ds) {
			result.add(vec);
		}
		return result;
	}

	public static Vector2d Subtract(final Vector2d vec1, final Vector2d vec2){
		return new Vector2d(
				vec1.x - vec2.x,
				vec1.y - vec2.y
		);
	}

	public static Vector2d Multiply(final Vector2d multiplicand, final double multiplier){
		return new Vector2d(
				multiplicand.x * multiplier,
				multiplicand.y * multiplier
		);
	}

	public static Vector2d Normalize(final Vector2d vec){
		return Multiply(vec, 1 / vec.getLength());
	}

	public static Vector2d Negative(final Vector2d vec){
		return Multiply(vec, -1);
	}
}
