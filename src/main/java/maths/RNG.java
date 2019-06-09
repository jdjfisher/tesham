package maths;

import maths.vectors.Vector3f;

import java.awt.*;
import java.util.Random;

public class RNG
{

    private static Random RNG = new Random();

    private RNG()
    {
    }

    public static int Int(int min, int max)
    {
        return RNG.nextInt(max - min + 1) + min;
    }

    public static double Double(double min, double max)
    {
        return RNG.nextDouble() * (max - min) + min;
    }

    public static float Float(float min, float max)
    {
        return RNG.nextFloat() * (max - min) + min;
    }

    public static boolean Boolean()
    {
        return RNG.nextBoolean();
    }

    public static Color Colour()
    {
        return new Color(Int(0, 255), Int(0, 255), Int(0, 255));
    }

    public static Vector3f Vector3f()
    {
        return Vector3f.Normalize(new Vector3f(Float(-1, 1), Float(-1, 1), Float(-1, 1)));
    }
}
