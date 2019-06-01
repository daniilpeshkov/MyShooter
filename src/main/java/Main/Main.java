package Main;


import Game.Graphics.Camera;
import Game.Graphics.GameRenderer;
import Game.Graphics.Texture;
import Game.Logic.*;
import Game.Network.ClientService;
import Game.Network.Server;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

    private static int H = 800;
    private static int W = 920;
    float cursor_size = 0.02f;
    float heart_size = 0.09f;
    GameWorld gameWorld;
    float scale = 0.099999999f;
    Entity field;
    Entity field2;
    boolean isOnline = false;
    private float FPS = 60;
    private long millis_per_frame = (long) ((1.0f / 30.0f) * 1000.0f);
    private Player player;
    private Texture cursor_tex;
    private static Texture player_tex;
    private Texture field_tex;
    private Texture bullet_tex;
    private Texture walkingEnemy_tex;
    private Texture shooter_tex;
    private Texture heart_tex;
    private Texture worm_tex;
    private Vector2f cursor_pos;
    // The window handle
    private long window;
    private BufferedReader input;
    private ClientService clientService;

    public static void main(String[] args) throws InterruptedException {
        new Main().run();
    }

    public void run() throws InterruptedException {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);


        window = glfwCreateWindow(W, H, "test", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
            if (key == GLFW_KEY_UP) {
                Camera.zoomIn();
                scale *= 1.1;
            }
            if (key == GLFW_KEY_DOWN) {
                Camera.zoomOut();
                scale /= 1.1;
            }
        });

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

        player_tex = Texture.genTexture("src\\main\\resources\\character.png");
        field_tex = Texture.genTexture("src\\main\\resources\\field.png");
        cursor_tex = Texture.genTexture("src\\main\\resources\\cursor.png");
        bullet_tex = Texture.genTexture("src\\main\\resources\\bullet.png");
        shooter_tex = Texture.genTexture("src\\main\\resources\\shooter.png");
        walkingEnemy_tex = Texture.genTexture("src\\main\\resources\\zombie.png");
        heart_tex = Texture.genTexture("src\\main\\resources\\heart.png");
        worm_tex = Texture.genTexture("src\\main\\resources\\worm.png");

        cursor_pos = new Vector2f();

        initGame();
    }

    public static Texture getPlayer_tex() {
        return player_tex;
    }

    private void initGame() {

        gameWorld = new GameWorld();
        player = new Player(5, 1, 1f, 5, player_tex);

        gameWorld.addEntity(player);

//        WormSegment worm = new WormSegment(gameWorld, 0, 5,  1f, 1, 5f / 1000.0f, worm_tex,
//                new Player[] {player});
//
//        WormSegment.generateWorm(gameWorld, worm, (float) (Math.PI / 2), 40);

        player.equipWeapon(new RangedWeapon(1, (float) (Math.PI / 4), 0,
                300, 1, new Bullet(0, 0, 0.3f, 1, 4000, new Vector2f(0, 0), bullet_tex)));
    }

    private void loop() {
        long start_time;
        long last_time;

        input = new BufferedReader(new InputStreamReader(System.in));
        new ConsoleEvent().start();

        start_time = System.currentTimeMillis();
        render();
        processInput();
        last_time = start_time;

        while (!glfwWindowShouldClose(window)) {

            start_time = System.currentTimeMillis();

            processGameLogic((int) (start_time - last_time));
            render();
            processInput();

            last_time = start_time;
            glfwPollEvents();
        }
    }

    void processGameLogic(int delta_time) {
        gameWorld.update(delta_time);
    }

    void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glClearColor(1, 1, 1, 1);

        glColor4f(1, 1, 1, 1f);

        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();

        gameWorld.getEntities().forEach(entity -> {
            if (entity instanceof TexturedEntity) {
                GameRenderer.renderEntity((TexturedEntity) entity, player.getPos());
            }
        });

        renderHUD();

        renderCursor(cursor_pos.x, cursor_pos.y);

        glfwSwapBuffers(window); // swap the color buffers
    }

    void renderHUD() {
        heart_tex.bind();
        for (int i = 0; i < player.getHP(); i++) {

            glEnable(GL_TEXTURE_2D);
            glBegin(GL_QUADS);

            glTexCoord2f(0, 0);
            glVertex2f(-1 + i * heart_size / Camera.getAspectRatio(), 1);

            glTexCoord2f(0, 1);
            glVertex2f(-1 + (i + 1) * heart_size / Camera.getAspectRatio(), 1);

            glTexCoord2f(1, 1);
            glVertex2f(-1 + (i + 1) * heart_size / Camera.getAspectRatio(), 1 - heart_size);

            glTexCoord2f(1, 0);
            glVertex2f(-1 + i * heart_size / Camera.getAspectRatio(), 1 - heart_size);
            glEnd();
            glPopMatrix();
            glDisable(GL_TEXTURE_2D);
        }
    }

    void processInput() {
        byte direction = 0;

        int state = glfwGetKey(window, GLFW_KEY_W);
        if (state == GLFW_PRESS) {
            direction |= Player.UP;
        }
        state = glfwGetKey(window, GLFW_KEY_S);
        if (state == GLFW_PRESS) {
            direction |= Player.DOWN;
        }
        state = glfwGetKey(window, GLFW_KEY_A);
        if (state == GLFW_PRESS) {
            direction |= Player.LEFT;
        }
        state = glfwGetKey(window, GLFW_KEY_D);
        if (state == GLFW_PRESS) {
            direction |= Player.RIGHT;
        }

        player.move(direction);
        if (isOnline) clientService.moveNude(direction);

        state = glfwGetKey(window, GLFW_KEY_SPACE);
        if (state == GLFW_PRESS) {
            player.shot(gameWorld);
        }

        state = glfwGetKey(window, GLFW_KEY_R);
        if (state == GLFW_PRESS) {
            initGame();
        }

        double[] cursor_x = new double[1], cursor_y = new double[1];

        glfwGetCursorPos(window, cursor_x, cursor_y);

        float norm_x, norm_y;

        norm_x = (float) ((cursor_x[0] - W / 2) / W * 2.0f);
        norm_y = (float) ((H / 2 - cursor_y[0]) / H * 2.0f);

        // crutch for sending angle though network
        int angle = (int) (10000 * Math.atan2(norm_y, norm_x * Camera.getAspectRatio()));

        player.setDeciFi(angle);
        if (isOnline) clientService.angleNude(angle);

        cursor_pos.x = norm_x;
        cursor_pos.y = norm_y;
    }

    void renderCursor(float x, float y) {
        cursor_tex.bind();

        glEnable(GL_TEXTURE_2D);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(x - cursor_size / Camera.getAspectRatio(), y + cursor_size);

        glTexCoord2f(0, 1);
        glVertex2f(x + cursor_size / Camera.getAspectRatio(), y + cursor_size);

        glTexCoord2f(1, 1);
        glVertex2f(x + cursor_size / Camera.getAspectRatio(), y - cursor_size);

        glTexCoord2f(1, 0);
        glVertex2f(x - cursor_size / Camera.getAspectRatio(), y - cursor_size);

        glEnd();
        glDisable(GL_TEXTURE_2D);
    }

    private class ConsoleEvent extends Thread {
        @Override
        public void run() {
            while (!glfwWindowShouldClose(window)) {
                String string;

                try {
                    string = input.readLine();

                    if (string.equals("/server start")) {
                        Server.start(gameWorld);
                    } else if (string.contains("/connect")) {
                        String[] str = string.split(" ");
                        String[] s = str[1].split(":");
                        clientService = new ClientService(s[0], Integer.parseInt(s[1]));
                        isOnline = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
