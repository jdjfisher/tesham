package maths;

import maths.vectors.Vector3f;
import org.apache.commons.math3.util.FastMath;

import java.util.Objects;

/**
 * Created by fisherj16 on 9/20/2017.
 */
public class Quaternion
{
    private float x;
    private float y;
    private float z;
    private float w;

    public Quaternion()
    {
        this(Identity());
    }

    public Quaternion(final Quaternion quaternion)
    {
        set(quaternion);
    }

    public Quaternion(float x, float y, float z, float w)
    {
        set(x, y, z, w);
    }

    public Quaternion(final Vector3f eulerAngles)
    {
        set(eulerAngles);
    }

    public Quaternion(final float eulerX, final float eulerY, final float eulerZ)
    {
        set(eulerX, eulerY, eulerZ);
    }

    public Quaternion(final float theta, final Vector3f axisDirection)
    {
        set(theta, axisDirection);
    }

    @Override
    public String toString()
    {
        return String.format("Quaternion: [ %f, %f, %f, %f ]", x, y, z, w);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Quaternion quaternion = (Quaternion) obj;
        return x == quaternion.x && y == quaternion.y && z == quaternion.z && w == quaternion.w;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y, z, w);
    }

    public Quaternion getConjugate()
    {
        return Conjugate(this);
    }

    public void normalize()
    {
        set(Normalize(this));
    }

    //////////////////////////////////////////////MULTIPLY//////////////////////////////////////////////////////////////

    public static Quaternion Identity()
    {
        return new Quaternion(0, 0, 0, 1);
    }

    public void multiply(final Quaternion multiplier)
    {
        set(Multiply(multiplier, this));
    }

    public void multiply(final float x, final float y, final float z, final float w)
    {
        multiply(new Quaternion(x, y, z, w));
    }

    public void multiply(final Vector3f eulerAngles)
    {
        multiply(eulerAngles.getX(), eulerAngles.getY(), eulerAngles.getZ());
    }

    public void multiply(final float eulerX, final float eulerY, final float eulerZ)
    {
        multiply(EulerAnglesToQuaternion(eulerX, eulerY, eulerZ));
    }

    public void multiply(final float theta, final Vector3f axisDirection)
    {
        multiply(RotationAxisToQuaternion(theta, axisDirection));
    }

    public void multiply(final float scaler)
    {
        set(Multiply(this, scaler));
    }

    ////////////////////////////////////////////////SET/////////////////////////////////////////////////////////////////

    public void set(final Quaternion quaternion)
    {
        set(quaternion.x, quaternion.y, quaternion.z, quaternion.w);
    }

    public void set(final float x, final float y, final float z, final float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void set(final Vector3f eulerAngles)
    {
        set(eulerAngles.getX(), eulerAngles.getY(), eulerAngles.getZ());
    }

    public void set(final float eulerX, final float eulerY, final float eulerZ)
    {
        set(EulerAnglesToQuaternion(eulerX, eulerY, eulerZ));
    }

    public void set(final float theta, final Vector3f axisDirection)
    {
        set(RotationAxisToQuaternion(theta, axisDirection));
    }

    public void set(final Vector3f vec1, final Vector3f vec2)
    {  //WIP
        set(DirectionVectorsToQuaternion(vec1, vec2));
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public void setZ(float z)
    {
        this.z = z;
    }

    public void setW(float w)
    {
        this.w = w;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public float getW()
    {
        return w;
    }

    ///////////////////////////////////////////////STATIC///////////////////////////////////////////////////////////////

    private static Quaternion Multiply(final Quaternion multiplier, final Quaternion multiplicand)
    {
        return new Quaternion(
                multiplicand.x * multiplier.w + multiplicand.y * multiplier.z - multiplicand.z * multiplier.y + multiplicand.w * multiplier.x,
                -multiplicand.x * multiplier.z + multiplicand.y * multiplier.w + multiplicand.z * multiplier.x + multiplicand.w * multiplier.y,
                multiplicand.x * multiplier.y - multiplicand.y * multiplier.x + multiplicand.z * multiplier.w + multiplicand.w * multiplier.z,
                -multiplicand.x * multiplier.x - multiplicand.y * multiplier.y - multiplicand.z * multiplier.z + multiplicand.w * multiplier.w
        );
    }

    private static Quaternion Multiply(final Quaternion multiplicand, final float multiplier)
    {
        return new Quaternion(
                multiplicand.x * multiplier,
                multiplicand.y * multiplier,
                multiplicand.z * multiplier,
                multiplicand.w * multiplier
        );
    }

    private static Quaternion EulerAnglesToQuaternion(final Vector3f eulerAngles)
    { //Eulers angles
        return EulerAnglesToQuaternion(eulerAngles.getX(), eulerAngles.getY(), eulerAngles.getZ());
    }

    private static Quaternion EulerAnglesToQuaternion(final float eulerX, final float eulerY, final float eulerZ)
    { //Eulers angles
        final double halfX = FastMath.toRadians(eulerX / 2);
        final double halfY = FastMath.toRadians(eulerY / 2);
        final double halfZ = FastMath.toRadians(eulerZ / 2);

        final float cosX = (float) FastMath.cos(halfX);
        final float cosY = (float) FastMath.cos(halfY);
        final float cosZ = (float) FastMath.cos(halfZ);

        final float sinX = (float) FastMath.sin(halfX);
        final float sinY = (float) FastMath.sin(halfY);
        final float sinZ = (float) FastMath.sin(halfZ);

        return new Quaternion(
                cosZ * cosY * sinX - sinZ * sinY * cosX,
                cosZ * sinY * cosX + sinZ * cosY * sinX,
                sinZ * cosY * cosX - cosZ * sinY * sinX,
                cosZ * cosY * cosX + sinZ * sinY * sinX
        );
    }

    public static Quaternion DirectionVectorsToQuaternion(Vector3f startDirectionVector, Vector3f endDirectionVector)
    {  //WIP
        return RotationAxisToQuaternion((float) FastMath.toDegrees(FastMath.acos(Vector3f.MultiplyDotProduct(startDirectionVector, endDirectionVector))), Vector3f.MultiplyCrossProduct(startDirectionVector, endDirectionVector));
    }

    public static Vector3f QuaternionToEulerAngles(final Quaternion quaternion)
    {
        double ysqr = quaternion.y * quaternion.y;

        double t0 = 2.0 * (quaternion.w * quaternion.x + quaternion.y * quaternion.z);
        double t1 = 1.0 - 2.0 * (quaternion.x * quaternion.x + ysqr);

        double t2 = 2.0 * (quaternion.w * quaternion.y - quaternion.z * quaternion.x);
        t2 = t2 > 1.0 ? (1.0) : (t2 < -1.0 ? -1.0 : t2);

        double t3 = 2.0 * (quaternion.w * quaternion.z + quaternion.x * quaternion.y);
        double t4 = 1.0 - 2.0 * (ysqr + quaternion.z * quaternion.z);

        return new Vector3f(
                (float) FastMath.toDegrees(FastMath.atan2(t0, t1)),
                (float) FastMath.toDegrees(FastMath.asin(t2)),
                (float) FastMath.toDegrees(FastMath.atan2(t3, t4))
        );
    }

    private static Quaternion RotationAxisToQuaternion(final float theta, final Vector3f axisDirection)
    {
        final double Theta = FastMath.toRadians(theta);
        final float sinHalfTheta = (float) FastMath.sin(Theta / 2);
        final float cosHalfTheta = (float) FastMath.cos(Theta / 2);

        return new Quaternion(
                axisDirection.getX() * sinHalfTheta,
                axisDirection.getY() * sinHalfTheta,
                axisDirection.getZ() * sinHalfTheta,
                cosHalfTheta + 0
        );
    }

    public static Quaternion Conjugate(Quaternion quaternion)
    {
        return new Quaternion(-quaternion.x, -quaternion.y, -quaternion.z, quaternion.w);
    }

    private static Quaternion Normalize(Quaternion quaternion)
    {
        return Multiply(quaternion, (float) FastMath.sqrt(quaternion.x * quaternion.x + quaternion.y * quaternion.y + quaternion.z * quaternion.z + quaternion.w * quaternion.w));
    }
}
