package engine.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

/**
 * Created by Jordan Fisher on 03/06/2017.
 */
public class MouseButtons extends GLFWMouseButtonCallback
{
    private static final int BUTTON_TOTAL = 10;

    private static boolean[] currentButtonState;
    private static boolean[] previousButtonState;

    public MouseButtons()
    {
        currentButtonState = new boolean[BUTTON_TOTAL];
        previousButtonState = new boolean[BUTTON_TOTAL];
    }

    @Override
    public void invoke(long window, int button, int action, int mods)
    {
        currentButtonState[button] = action != GLFW.GLFW_RELEASE;
    }

    public static void update()
    {
        previousButtonState = currentButtonState.clone();
    }

    public static boolean isButtonDown(int buttonCode)
    {
        return currentButtonState[buttonCode];
    }

    public static boolean isButtonUp(int buttonCode)
    {
        return !currentButtonState[buttonCode];
    }

    public static boolean isButtonTapped(int buttonCode)
    {
        return isButtonUp(buttonCode) & previousButtonState[buttonCode];
    }
}
