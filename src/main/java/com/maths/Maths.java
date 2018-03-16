package com.maths;

import com.maths.vectors.Vector3f;
import org.apache.commons.math3.util.FastMath;

/**
 * Created by Jordan Fisher on 24/06/2017.
 */
public class Maths {
    public static final float e = 2.7182818284590452353602874713527f;

    private Maths(){}

    public static float sigmoid(float f){
        return 1f / (1 + (float)FastMath.pow(e, -f));
    }

    public static float floorMod(float x, float y){
        int quo = FastMath.round(x/y);
        float rem = x - quo * y;
        return rem;
    }

    public static int factorial(int n){
        if(n <= 1){
            return 1;
        }else {
            return n * factorial(--n);
        }
    }

    public static Vector3f cartesianToSpherical(Vector3f cartesian){ // (x, y, z) -> (φ, θ, r)
        final float x = cartesian.getX();
        final float y = cartesian.getY();
        final float z = cartesian.getZ();

        return new Vector3f(
                (float)FastMath.toDegrees(FastMath.atan2(FastMath.sqrt(x * x + y * y), z)),
                (float)FastMath.toDegrees(FastMath.atan2(y, x)),
                cartesian.getLength()
        );
    }

    public static Vector3f sphericalToCartesian(Vector3f spherical){// (φ, θ, r) -> (x, y, z)
        final double φ = FastMath.toRadians(spherical.getX());
        final double θ = FastMath.toRadians(spherical.getY());
        final float r = spherical.getZ();

        final float sinφ = (float)FastMath.sin(φ);
        final float cosφ = (float)FastMath.cos(φ);
        final float sinθ = (float)FastMath.sin(θ);
        final float cosθ = (float)FastMath.cos(θ);

        return new Vector3f(
                r * sinφ * cosθ,
                r * sinφ * sinθ,
                r * cosφ
        );
    }

    public static float max(float f0, float f1, float... fs) {
        float result = FastMath.max(f0, f1);
        for(float f : fs){
            result = FastMath.max(result, f);
        }
        return result;
    }

    public static float interpolateRangeLinearly(float initialRangeMin, float initialRangeMax, float newRangeMin, float newRangeMax, float value){
        if(value < initialRangeMin){
            return newRangeMin;
        }else if(value > initialRangeMax){
            return newRangeMax;
        }else {
            float initialRange = initialRangeMax - initialRangeMin;
            float newRange = newRangeMax - newRangeMin;
            return newRangeMin + ((value - initialRangeMin)/ initialRange) * newRange;
        }
    }
}
