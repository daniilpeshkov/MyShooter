package Game.GameInput;

import Game.Graphics.GameRenderer;


import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {

    public static final int KEY_W = GLFW_KEY_W;
    public static final int KEY_S = GLFW_KEY_S;
    public static final int KEY_D = GLFW_KEY_D;
    public static final int KEY_A = GLFW_KEY_A;
    public static final int KEY_SPACE = GLFW_KEY_SPACE;
    public static final int KEY_UP = GLFW_KEY_UP;
    public static final int KEY_DOWN = GLFW_KEY_DOWN;
    public static final int KEY_R = GLFW_KEY_R;

    public static final int PRESS = GLFW_PRESS;



    public static int getKeyState(int key) {
        return glfwGetKey(GameRenderer.window, key);
    }

}
