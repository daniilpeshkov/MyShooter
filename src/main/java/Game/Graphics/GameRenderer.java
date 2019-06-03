package Game.Graphics;

import Game.Logic.TexturedEntity;
import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.File;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GameRenderer {

    public static long window;
    public static int H = 800;
    public static int W = 920;

    public static Map<Integer, Texture> textureMap;
    static float HEART_SIZE = 0.1f;
    static float CURSOR_SIZE = 0.06f;

    public static void loadTextures() {
        File texturePath = new File("src\\main\\resources");
        textureMap = new HashMap();
        for (File a : texturePath.listFiles()) {
            String name = a.getName();
            int number = Integer.valueOf(name.substring(0, name.indexOf('.')));
            textureMap.put(number, Texture.genTexture(a.getAbsolutePath()));
            System.out.println("Texture " + number + " loaded");
        }
    }

    public static void bindTexture(int id) {
        textureMap.get(id).bind();
    }

    public static void renderEntity(TexturedEntity entity, Vector3f cameraPos) {
        Matrix3f rotation_matrix = new Matrix3f();
        rotation_matrix.rotate(entity.getFi(), 0, 0, 1, rotation_matrix);
        Vector3f pos = entity.getPos();

        Vector3f p1 = Camera.transform(entity.getPos(), cameraPos, new Vector3f(pos.x - entity.getR() / 2.0f, pos.y + entity.getR() / 2.0f, 0), rotation_matrix);
        Vector3f p2 = Camera.transform(entity.getPos(), cameraPos, new Vector3f(pos.x + entity.getR() / 2.0f, pos.y + entity.getR() / 2.0f, 0), rotation_matrix);
        Vector3f p3 = Camera.transform(entity.getPos(), cameraPos, new Vector3f(pos.x + entity.getR() / 2.0f, pos.y - entity.getR() / 2.0f, 0), rotation_matrix);
        Vector3f p4 = Camera.transform(entity.getPos(), cameraPos, new Vector3f(pos.x - entity.getR() / 2.0f, pos.y - entity.getR() / 2.0f, 0), rotation_matrix);

        glShadeModel(GL_SMOOTH);

        glColor3f(1f, 1.0f, 1f);

        glEnable(GL_TEXTURE_2D);
        bindTexture(entity.getTextureID());
        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex2f(p1.x, p1.y);

        glTexCoord2f(0, 1);
        glVertex2f(p2.x, p2.y);

        glTexCoord2f(1, 1);
        glVertex2f(p3.x, p3.y);

        glTexCoord2f(1, 0);
        glVertex2f(p4.x, p4.y);

        glEnd();

        glDisable(GL_TEXTURE_2D);
    }

    public static void renderEntity(Vector3f pos, Vector3f cameraPos, float r, float fi, int textureID) {
        Matrix3f rotation_matrix = new Matrix3f();
        rotation_matrix.rotate(fi, 0, 0, 1, rotation_matrix);

        Vector3f p1 = Camera.transform(pos, cameraPos, new Vector3f(pos.x - r / 2.0f, pos.y + r / 2.0f, 0), rotation_matrix);
        Vector3f p2 = Camera.transform(pos, cameraPos, new Vector3f(pos.x + r / 2.0f, pos.y + r / 2.0f, 0), rotation_matrix);
        Vector3f p3 = Camera.transform(pos, cameraPos, new Vector3f(pos.x + r / 2.0f, pos.y - r / 2.0f, 0), rotation_matrix);
        Vector3f p4 = Camera.transform(pos, cameraPos, new Vector3f(pos.x - r / 2.0f, pos.y - r / 2.0f, 0), rotation_matrix);

        glShadeModel(GL_SMOOTH);

        glColor3f(1f, 1.0f, 1f);

        glEnable(GL_TEXTURE_2D);
        bindTexture(textureID);
        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex2f(p1.x, p1.y);

        glTexCoord2f(0, 1);
        glVertex2f(p2.x, p2.y);

        glTexCoord2f(1, 1);
        glVertex2f(p3.x, p3.y);

        glTexCoord2f(1, 0);
        glVertex2f(p4.x, p4.y);

        glEnd();

        glDisable(GL_TEXTURE_2D);
    }

    public static void renderHUD(float x, float y, float hp) {
        GameRenderer.bindTexture(6);
        for (int i = 0; i < hp; i++) {

            glEnable(GL_TEXTURE_2D);
            glBegin(GL_QUADS);

            glTexCoord2f(0, 0);
            glVertex2f(-1 + i * HEART_SIZE / Camera.getAspectRatio(), 1);

            glTexCoord2f(0, 1);
            glVertex2f(-1 + (i + 1) * HEART_SIZE / Camera.getAspectRatio(), 1);

            glTexCoord2f(1, 1);
            glVertex2f(-1 + (i + 1) * HEART_SIZE / Camera.getAspectRatio(), 1 - HEART_SIZE);

            glTexCoord2f(1, 0);
            glVertex2f(-1 + i * HEART_SIZE / Camera.getAspectRatio(), 1 - HEART_SIZE);
            glEnd();
            glPopMatrix();
            glDisable(GL_TEXTURE_2D);
        }

        GameRenderer.bindTexture(3);

        glEnable(GL_TEXTURE_2D);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(x - CURSOR_SIZE / Camera.getAspectRatio(), y + CURSOR_SIZE);

        glTexCoord2f(0, 1);
        glVertex2f(x + CURSOR_SIZE / Camera.getAspectRatio(), y + CURSOR_SIZE);

        glTexCoord2f(1, 1);
        glVertex2f(x + CURSOR_SIZE / Camera.getAspectRatio(), y - CURSOR_SIZE);

        glTexCoord2f(1, 0);
        glVertex2f(x - CURSOR_SIZE / Camera.getAspectRatio(), y - CURSOR_SIZE);

        glEnd();
        glDisable(GL_TEXTURE_2D);
    }

    public static void renderBackground(Vector3f cam_pos) {
        bindTexture(11);

        TexturedEntity entity = new TexturedEntity(0, 0, 1, 11);

        for (int i = (int) cam_pos.y - (int) (Camera.view_y * Camera.scale) - 4; i <= (int) cam_pos.y + (Camera.view_y * Camera.scale) + 4; i++) {
            for (int j = (int) cam_pos.x - (int) (Camera.view_x * Camera.scale) - 4; j <= cam_pos.x + (Camera.view_x * Camera.scale) + 4; j++) {
                entity.setPos(new Vector3f(j, i, 0));
                renderEntity(entity, cam_pos);
            }
        }
    }

    public static void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        window = glfwCreateWindow(W, H, "test", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);

        glfwShowWindow(window);

        GL.createCapabilities();
        Camera.updateAspectRatio(W, H);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    }

    public static void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static boolean windowShouldClose() {
        return glfwWindowShouldClose(GameRenderer.window);
    }

    public static void clearWindow() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glClearColor(1, 1, 1, 1);
        glColor4f(1, 1, 1, 1f);
    }

    public static void updateScreen() {
        glfwSwapBuffers(GameRenderer.window); // swap the color buffers
    }

    public static void pollEvents() {
        glfwPollEvents();
    }

    public static Vector2f getCursorPos() {
        double[] cursor_x = new double[1], cursor_y = new double[1];

        glfwGetCursorPos(GameRenderer.window, cursor_x, cursor_y);
        return new Vector2f((float) ((cursor_x[0] - GameRenderer.W / 2.0d) / GameRenderer.W * 2.0f),
                (float) ((GameRenderer.H / 2.0d - cursor_y[0]) / GameRenderer.H * 2.0f));
    }
}
