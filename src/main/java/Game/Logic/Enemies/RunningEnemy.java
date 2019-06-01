package Game.Logic.Enemies;

import Game.Graphics.Texture;
import Game.Logic.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class RunningEnemy extends Enemy {

    public static float RANG_FOR_CHARGE = 20;

    public RunningEnemy(GameWorld world, float x, float y, float r, int hp, float speed, Texture texture, Player[] players) {
        super(world, x, y, r, hp, speed, texture, players);
        weapon = new MeleeWeapon(0.1f, 500, 1,
                new Bullet(0, 0, 0.3f, 1, 4000, new Vector2f(0, 0), new Texture()));
    }

    @Override
    public void update(int dTime) {
        weapon.update(dTime);

        float dist = players[0].getPos().distance(pos);
        if (dist <= (players[0].getR() + getR()) / 2) {
            Vector3f shooting_pos = new Vector3f(pos.x, pos.y, 0);
            shooting_pos.x += (getR() + Bullet.RADIUS) / 2 * Math.cos(fi);
            shooting_pos.y += (getR() + Bullet.RADIUS) / 2 * Math.sin(fi);
            if (weapon.canShot()) {
                for (Entity entity : weapon.shot(shooting_pos, fi)) {
                    world.addEntity(entity);
                }
            }
        } else if (dist <= RANG_FOR_CHARGE) {
            follow(players[0]);
        } else {
            stop();
        }
    }

    @Override
    public void collidesWith(Entity entity) {

    }

    @Override
    public void shot() {

    }
}
