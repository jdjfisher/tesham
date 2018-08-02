package com.utils;

import com.maths.vectors.Vector2d;
import com.maths.vectors.Vector2f;
import com.maths.vectors.Vector3f;
import com.maths.vectors.Vector4f;

import java.awt.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collection;

public class DataUtils {
	private DataUtils(){}

	public static byte[] toByteArray(ByteBuffer byteBuffer){
		byte[] byteArray = new byte[byteBuffer.remaining()];
		byteBuffer.get(byteArray);
		return byteArray;
	}

	public static FloatBuffer toFloatBuffer(Vector3f[] array){
		return toFloatBuffer(toFloatArray(array));
	}

	public static FloatBuffer toFloatBuffer(Vector2f[] array){
		return toFloatBuffer(toFloatArray(array));
	}

	public static FloatBuffer toFloatBuffer(float[] array){
		FloatBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
		result.put(array).flip();
		return result;
	}

	public static IntBuffer toIntBuffer(int[] array){
		IntBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
		result.put(array).flip();
		return result;
	}

	public static void putVector3f(FloatBuffer floatBuffer, Vector3f vector3f){
		floatBuffer.put(vector3f.getX());
		floatBuffer.put(vector3f.getY());
		floatBuffer.put(vector3f.getZ());
	}

	public static void putVector2f(FloatBuffer floatBuffer, Vector2f vector2f){
		floatBuffer.put(vector2f.getX());
		floatBuffer.put(vector2f.getY());
	}

	public static float[] toFloatArray(float[][] array){
		float[] result = new float[array.length * array[0].length];
		for (int row = 0; row < array.length; row++) {
			for (int col = 0; col < array[0].length; col++) {
				result[row + col * array[0].length] = array[row][col];
			}
		}
		return result;
	}

	public static float[] toFloatArray(Vector4f[] array){
		float[] result = new float[array.length * 4];
		for(int i = 0; i < array.length; i++){
			result[i * 4 + 0] = array[i].getX();
			result[i * 4 + 1] = array[i].getY();
			result[i * 4 + 2] = array[i].getZ();
			result[i * 4 + 3] = array[i].getW();
		}
		return result;
	}

	public static float[] toFloatArray(Vector3f[] array){
		float[] result = new float[array.length * 3];
		for(int i = 0; i < array.length; i++){
			result[i * 3 + 0] = array[i].getX();
			result[i * 3 + 1] = array[i].getY();
			result[i * 3 + 2] = array[i].getZ() ;
		}
		return result;
	}

	public static float[] toFloatArray(Vector2f[] array){
		float[] result = new float[array.length * 2];
		for(int i = 0; i < array.length; i++){
			result[i * 2 + 0] = array[i].getX();
			result[i * 2 + 1] = array[i].getY();
		}
		return result;
	}

	public static char[] toCharArray(String string){
		char[] result = new char[string.length()];
		for(int i = 0; i < string.length(); i++){
			result[i] = string.charAt(i);
		}
		return result;
	}

	public static Vector3f toVector3f(Color colour){
		float[] rgb = new float[3];
		colour.getColorComponents(rgb);
		return new Vector3f(rgb[0], rgb[1], rgb[2]);
	}

	public static Vector2f toVector2f(Vector2d vector2d){
		return new Vector2f((float) vector2d.getX(), (float) vector2d.getY());
	}
}
