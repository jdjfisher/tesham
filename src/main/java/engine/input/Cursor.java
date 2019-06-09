package engine.input;

import engine.core.Window;
import maths.vectors.Vector2d;
import maths.vectors.Vector2f;
import utils.DataUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Jordan Fisher on 03/06/2017.
 */
public class Cursor extends GLFWCursorPosCallback
{
    private static Window window;

    private static Vector2d currentPosition;
    private static Vector2d previousPosition;

    private static boolean visible;
    private static boolean onScreen;

    public Cursor(Window window)
    {
        Cursor.window = window;
        currentPosition = new Vector2d();
        previousPosition = new Vector2d();
        visible = true;
    }

    public void invoke(long window, double x, double y)
    {
        currentPosition.set(x, y);
    }

    public static void update()
    {
        previousPosition.set(currentPosition);

        if (visible)
        {
            onScreen = (currentPosition.getX() > 0 && currentPosition.getY() > 0 && currentPosition.getX() < window.getWidth() && currentPosition.getY() < window.getHeight());
        }
    }

    public static Vector2d getCurrentPosition()
    {
        return currentPosition;
    }

    public static Vector2f getDeltaPosition()
    {
        return DataUtils.toVector2f(Vector2d.Subtract(currentPosition, previousPosition));
    }

    public static void toggleVisibility()
    {
        setVisibility(!visible);
    }

    public static void setVisibility(boolean state)
    {
        if (visible != state)
        {
            glfwSetInputMode(window.getHandle(), GLFW_CURSOR, state ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
            glfwSetCursorPos(window.getHandle(), window.getWidth() / 2, window.getHeight() / 2);

            visible = state;
        }
    }

    public static boolean isVisible()
    {
        return visible;
    }

    public static boolean onScreen()
    {
        return onScreen;
    }
}
