package engine.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Created by Jordan Fisher on 03/06/2017.
 */
public class Keyboard extends GLFWKeyCallback
{
    private static final int KEY_TOTAL = 350;

    private static boolean[] currentKeyState;
    private static boolean[] previousKeyState;

    public Keyboard()
    {
        currentKeyState = new boolean[KEY_TOTAL];
        previousKeyState = new boolean[KEY_TOTAL];
    }

    public void invoke(long window, int key, int scancode, int action, int mods)
    {
        if (key >= 0)
        {
            currentKeyState[key] = action != GLFW.GLFW_RELEASE;
        }
    }

    public static void update()
    {
        previousKeyState = currentKeyState.clone();
    }

    private static boolean isKeyDown(int keyCode, boolean[] keySet)
    {
        return keySet[keyCode];
    }

    private static boolean isKeyUp(int keyCode, boolean[] keySet)
    {
        return !keySet[keyCode];
    }

    public static boolean isKeyDown(int keyCode)
    {
        return isKeyDown(keyCode, currentKeyState);
    }

    public static boolean isKeyUp(int keyCode)
    {
        return isKeyUp(keyCode, currentKeyState);
    }

    public static boolean isKeyTapped(int keyCode)
    {
        return isKeyUp(keyCode, currentKeyState) & isKeyDown(keyCode, previousKeyState);
    }
}
