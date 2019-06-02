package Main;


import Game.Graphics.Camera;
import Game.Graphics.GameRenderer;
import Game.Logic.*;
import Game.Network.Server;
import Game.Network.ServerService;
import org.joml.Vector2f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.lwjgl.glfw.GLFW.*;

public class Main {
    GameWorld gameWorld;
    boolean isOnline = false;
    private Player player;
    Vector2f cursor_pos;

    private BufferedReader input;
    private ServerService clientService;
    private Server server;

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        initGame();
        GameRenderer.init();
        GameRenderer.loadTextures();
        loop();
        GameRenderer.destroy();
    }


    private void initGame() {
        cursor_pos = new Vector2f();
        gameWorld = new GameWorld();
        gameWorld = new GameWorld();
        player = new Player(5, 1, 1f, 5, 2);

        gameWorld.addEntity(player);

//        WormSegment worm = new WormSegment(gameWorld, 0, 5,  1f, 1, 5f / 1000.0f, worm_tex,
//                new Player[] {player});
//
//        WormSegment.generateWorm(gameWorld, worm, (float) (Math.PI / 2), 40);

        player.equipWeapon(new RangedWeapon(1, (float) (Math.PI / 4), 0,
                300, 1, new Bullet(0, 0, 0.3f, 1, 4000, new Vector2f(0, 0), 1)));
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

        while (!GameRenderer.windowShouldClose()) {

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

        GameRenderer.clearWindow();
        gameWorld.getEntities().forEach(entity -> {
            if (entity instanceof TexturedEntity) {
                GameRenderer.renderEntity(entity.getPos(), player.getPos(), entity.getR(), entity.getFi(), ((TexturedEntity) entity).getTextureID());
                //TODO this parameters can be overridden from network interface because now it is just numbers
            }
        });

        GameRenderer.renderHUD(cursor_pos.x, cursor_pos.y, player.getHP());
        GameRenderer.updateScreen();
    }



    void processInput() {
        byte direction = 0;

        int state = GameRenderer.getKeyState(GLFW_KEY_W);
        if (state == GLFW_PRESS) {
            direction |= Player.UP;
        }
        state = GameRenderer.getKeyState(GLFW_KEY_S);
        if (state == GLFW_PRESS) {
            direction |= Player.DOWN;
        }
        state = GameRenderer.getKeyState(GLFW_KEY_A);
        if (state == GLFW_PRESS) {
            direction |= Player.LEFT;
        }
        state = GameRenderer.getKeyState(GLFW_KEY_D);
        if (state == GLFW_PRESS) {
            direction |= Player.RIGHT;
        }

        player.move(direction);
        if (isOnline) clientService.moveNude(direction);

        state = GameRenderer.getKeyState(GLFW_KEY_SPACE);
        if (state == GLFW_PRESS) {
            player.shot(gameWorld);
        }

        state = GameRenderer.getKeyState(GLFW_KEY_R);
        if (state == GLFW_PRESS) {
            initGame();
        }

        Vector2f cursorPos = GameRenderer.getCursorPos();

        // crutch for sending angle though network
        int angle = (int) (10000 * Math.atan2(cursorPos.y, cursorPos.x * Camera.getAspectRatio()));

        player.setDeciFi(angle);
        if (isOnline) clientService.angleNude(angle);

        cursor_pos.x = cursorPos.x;
        cursor_pos.y = cursorPos.y;
    }



    private class ConsoleEvent extends Thread {
        @Override
        public void run() {
            while (!GameRenderer.windowShouldClose()) {
                String string;

                try {
                    string = input.readLine();

                    if (string.equals("/server start")) {
                        server = new Server(gameWorld);
                        System.out.println("Server started");
                    } else if (string.contains("/connect")) {
                        String[] str = string.split(" ");
                        String[] s = str[1].split(":");
                        clientService = new ServerService(s[0], Integer.parseInt(s[1]));
                        isOnline = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
