package Main;


import Game.GameInput.Keyboard;
import Game.Graphics.Camera;
import Game.Graphics.GameRenderer;
import Game.Logic.*;
import Game.Logic.Enemies.WormSegment;
import Game.Network.Server;
import Game.Network.Client;
import org.joml.Vector2f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
    GameWorld gameWorld;
    boolean isOnline = false;
    private Player player;
    Vector2f cursor_pos;

    private BufferedReader input;
    private Client clientService;
    private Server server;
    private boolean isServer = false;

    volatile public static ArrayList<TexturedEntity> buffer = new ArrayList<>();

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

        WormSegment worm = new WormSegment(gameWorld, 0, 5,  1f, 1, 5f / 1000.0f, 4,
                new Player[] {player});

        WormSegment.generateWorm(gameWorld, worm, (float) (Math.PI / 2), 10);

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
            GameRenderer.pollEvents();
        }
    }

    void processGameLogic(int delta_time) {
        gameWorld.update(delta_time);
    }

    void render() {
        GameRenderer.clearWindow();

        if (isOnline) {
            if (!buffer.isEmpty()) {
                ArrayList<TexturedEntity> tmp = buffer;
                for (int i = 1; i < tmp.size(); i++) {
                    GameRenderer.renderEntity(tmp.get(i).getPos(), tmp.get(0).getPos(), tmp.get(i).getR(), tmp.get(i).getFi(), tmp.get(i).getTextureID());
                }
                GameRenderer.renderHUD(cursor_pos.x, cursor_pos.y, tmp.get(0).getHP());
            }
        } else {
            gameWorld.getEntities().forEach(entity -> {
                if (entity instanceof TexturedEntity) {
                    GameRenderer.renderEntity(entity.getPos(), player.getPos(), entity.getR(), entity.getFi(), ((TexturedEntity) entity).getTextureID());
                    //TODO this parameters can be overridden from network interface because now it is just numbers
                }
            });
            GameRenderer.renderHUD(cursor_pos.x, cursor_pos.y, player.getHP());
        }
        GameRenderer.updateScreen();
    }

    void processInput() {
        byte direction = 0;

        int state = Keyboard.getKeyState(Keyboard.KEY_W);
        if (state == Keyboard.PRESS) {
            direction |= Player.UP;
        }
        state = Keyboard.getKeyState(Keyboard.KEY_S);
        if (state == Keyboard.PRESS) {
            direction |= Player.DOWN;
        }
        state = Keyboard.getKeyState(Keyboard.KEY_A);
        if (state == Keyboard.PRESS) {
            direction |= Player.LEFT;
        }
        state = Keyboard.getKeyState(Keyboard.KEY_D);
        if (state == Keyboard.PRESS) {
            direction |= Player.RIGHT;
        }

        if (isOnline) {
            Client.updateDirection(direction);
        } else {
            player.updateDirection(direction);
        }

        state = Keyboard.getKeyState(Keyboard.KEY_SPACE);
        if (state == Keyboard.PRESS) {
            player.shot(gameWorld);
            Client.fireBullets();
        }

        state = Keyboard.getKeyState(Keyboard.KEY_R);
        if (state == Keyboard.PRESS) {
            initGame();
        }

        state = Keyboard.getKeyState(Keyboard.KEY_UP);
        if (state == Keyboard.PRESS) {
            Camera.zoomIn();
        }

        state = Keyboard.getKeyState(Keyboard.KEY_DOWN);
        if (state == Keyboard.PRESS) {
            Camera.zoomOut();
        }

        Vector2f cursorPos = GameRenderer.getCursorPos();

        float angle = (float) Math.atan2(cursorPos.y, cursorPos.x * Camera.getAspectRatio());

        if (isOnline) {
            Client.angleNude(angle);
        } else {
            player.setFi(angle);
        }

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
                        isServer = true;
                    } else if (string.contains("/connect")) {
                        String[] str = string.split(" ");
                        if (str[1].contains(":")) {
                            String[] s = str[1].split(":");
                            Client.init(s[0], Integer.parseInt(s[1]));
                        }
                        else
                            Client.initByIp(str[1]);

                        gameWorld.clearEntities();
                        isOnline = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
