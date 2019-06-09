package maths.vectors;

import org.apache.commons.math3.util.FastMath;

import java.util.Objects;

public class Vector2f
{

    public static final int SIZE = 2;
    private float x;
    private float y;

    public static Vector2f Identity()
    {
        return new Vector2f(0, 0);
    }

    public Vector2f()
    {
        this(Identity());
    }

    public Vector2f(Vector2f vec)
    {
        this(vec.getX(), vec.getY());
    }

    public Vector2f(float x, float y)
    {
        set(x, y);
    }

    public void set(Vector2f vec)
    {
        this.set(vec.x, vec.y);
    }

    public void set(float x, float y)
    {
        setX(x);
        setY(y);
    }

    public void add(Vector2f deltaVec)
    {
        this.add(deltaVec.x, deltaVec.y);
    }

    public void add(float dx, float dy)
    {
        addX(dx);
        addY(dy);
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

    public float getLength()
    {
        return (float) FastMath.sqrt(x * x + y * y);
    }

    public void multiply(final float multiplier)
    {
        set(Multiply(this, multiplier));
    }

    public void normalize()
    {
        set(Normalize(this));
    }

    public void negative()
    {
        set(Negative(this));
    }

    @Override
    public String toString()
    {
        return String.format("Vec3f: [ %f, %f ]", x, y);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vector2f vec = (Vector2f) obj;
        return x == vec.x && y == vec.y;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

    public static Vector2f Add(final Vector2f vec1, final Vector2f vec2)
    {
        return new Vector2f(
                vec1.x + vec2.x,
                vec1.y + vec2.y
        );
    }

    public static Vector2f Add(final Vector2f vec1, final Vector2f vec2, final Vector2f... vector2fs)
    {
        Vector2f result = Vector2f.Add(vec1, vec2);
        for (Vector2f vec : vector2fs)
        {
            result.add(vec);
        }
        return result;
    }

    public static Vector2f Subtract(final Vector2f vec1, final Vector2f vec2)
    {
        return new Vector2f(
                vec1.x - vec2.x,
                vec1.y - vec2.y
        );
    }

    public static Vector2f Multiply(final Vector2f multiplicand, final float multiplier)
    {
        return new Vector2f(
                multiplicand.x * multiplier,
                multiplicand.y * multiplier
        );
    }

    public static Vector2f Normalize(final Vector2f vec)
    {
        return Multiply(vec, 1 / vec.getLength());
    }

    public static Vector2f Negative(final Vector2f vec)
    {
        return Multiply(vec, -1);
    }
}
