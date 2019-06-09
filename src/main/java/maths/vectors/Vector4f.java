package maths.vectors;

import maths.Maths;
import maths.Matrix4f;
import org.apache.commons.math3.util.FastMath;

import java.util.Objects;

/**
 * Created by Jordan Fisher on 02/07/2017.
 */
public class Vector4f
{

    public static final int SIZE = 4;
    private float x;
    private float y;
    private float z;
    private float w;

    public static Vector4f Identity()
    {
        return new Vector4f(0, 0, 0, 0);
    }

    public Vector4f()
    {
        this(Identity());
    }

    public Vector4f(Vector3f vec, float w)
    {
        this(vec.getX(), vec.getY(), vec.getZ(), w);
    }

    public Vector4f(Vector4f vec)
    {
        this(vec.getX(), vec.getY(), vec.getZ(), vec.getW());
    }

    public Vector4f(float x, float y, float z, float w)
    {
        set(x, y, z, w);
    }

    public void set(Vector4f vec)
    {
        this.set(vec.x, vec.y, vec.z, vec.w);
    }

    public void set(float x, float y, float z, float w)
    {
        setX(x);
        setY(y);
        setZ(z);
        setW(w);
    }

    public void add(Vector4f deltaVec)
    {
        this.add(deltaVec.x, deltaVec.y, deltaVec.z, deltaVec.w);
    }

    public void add(float dx, float dy, float dz, float dw)
    {
        addX(dx);
        addY(dy);
        addZ(dz);
        addW(dw);
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void addX(float dx)
    {
        setX(getX() + dx);
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public void addY(float dy)
    {
        setY(getY() + dy);
    }

    public float getZ()
    {
        return z;
    }

    public void setZ(float z)
    {
        this.z = z;
    }

    public void addZ(float dz)
    {
        setZ(getZ() + dz);
    }

    public float getW()
    {
        return w;
    }

    public void setW(float w)
    {
        this.w = w;
    }

    public void addW(float dw)
    {
        setW(getW() + dw);
    }

    public float getLength()
    {
        return Length(this);
    }

    public float getLargestComponent()
    {
        return LargestComponent(this);
    }

    public void multiply(final float multiplier)
    {
        set(Multiply(this, multiplier));
    }

    public void multiply(final Matrix4f multiplier)
    {
        set(Multiply(this, multiplier));
    }

    public void normalize()
    {
        set(Normalize(this));
    }

    public void normalize3()
    {
        set(Normalize3(this));
    }

    public void negate()
    {
        set(Negative(this));
    }

    @Override
    public String toString()
    {
        return String.format("Vec4f: [ %f, %f, %f, %f ]", x, y, z, w);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vector4f vec = (Vector4f) obj;
        return x == vec.x && y == vec.y && z == vec.z && w == vec.w;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y, z, w);
    }

    public static Vector4f Add(final Vector4f vec1, final Vector4f vec2)
    {
        return new Vector4f(
                vec1.x + vec2.x,
                vec1.y + vec2.y,
                vec1.z + vec2.z,
                vec1.w + vec2.w
        );
    }

    public static Vector4f Add(final Vector4f vec1, final Vector4f vec2, final Vector4f... vector4fs)
    {
        Vector4f result = Vector4f.Add(vec1, vec2);
        for (Vector4f vec : vector4fs)
        {
            result.add(vec);
        }
        return result;
    }

    public static Vector4f Subtract(final Vector4f vec1, final Vector4f vec2)
    {
        return new Vector4f(
                vec1.x - vec2.x,
                vec1.y - vec2.y,
                vec1.z - vec2.z,
                vec1.w - vec2.w
        );
    }

    public static Vector4f Multiply(final Vector4f multiplicand, final float multiplier)
    {
        return new Vector4f(
                multiplicand.x * multiplier,
                multiplicand.y * multiplier,
                multiplicand.z * multiplier,
                multiplicand.w * multiplier
        );
    }

    public static Vector4f Multiply(final Vector4f multiplicand, final Matrix4f multiplier)
    {
        final float[] component = new float[Matrix4f.ORDER];
        for (int row = 0; row < Matrix4f.ORDER; row++)
        {
            component[row] = multiplier.elements[row][0] * multiplicand.getX() + multiplier.elements[row][1] * multiplicand.getY() + multiplier.elements[row][2] * multiplicand.getZ() + multiplier.elements[row][3] * multiplicand.getW();
        }
        return new Vector4f(component[0], component[1], component[2], component[3]);
    }

    public static float Length(final Vector4f vec)
    {
        return (float) FastMath.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z + vec.w * vec.w);
    }

    public static Vector4f Normalize(final Vector4f vec)
    {
        return Multiply(vec, 1 / vec.getLength());
    }

    public static Vector4f Normalize3(final Vector4f vec)
    {
        return Multiply(vec, 1 / (vec.x * vec.x + vec.y * vec.y + vec.z * vec.z));
    }

    public static Vector4f Negative(final Vector4f vec)
    {
        return Multiply(vec, -1);
    }

    public static float LargestComponent(final Vector4f vec)
    {
        return Maths.max(vec.x, vec.y, vec.z, vec.w);
    }
}
