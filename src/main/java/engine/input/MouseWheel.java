package engine.input;

import org.lwjgl.glfw.GLFWScrollCallback;

/**
 * Created by Jordan Fisher on 03/06/2017.
 */
public class MouseWheel extends GLFWScrollCallback
{
    private static final float WHEEL_SENSITIVITY = 1;
    private static double deltaScroll;

    public MouseWheel()
    {
        deltaScroll = 0;
    }

    public void invoke(long window, double dx, double dy)
    {
        deltaScroll = dy;
    }

    public static void update()
    {
        deltaScroll = 0;
    }

    public static int getDeltaScroll()
    {
        return (int) (deltaScroll * WHEEL_SENSITIVITY);
    }

    public static boolean isScrolled()
    {
        return deltaScroll != 0;
    }
}
