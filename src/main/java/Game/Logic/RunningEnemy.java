package Game.Logic;

import Game.Graphics.Texture;
import org.joml.Vector3f;

public class RunningEnemy extends Enemy {
    public static float SPEED = 4.0f / 1000;
    public static float RANG_FOR_CHARGE = 20;

    public RunningEnemy(GameWorld world, float x, float y, float r, int hp, float speed, Texture texture, Player[] players) {
        super(world, x, y, r, hp, speed, texture, players);
        weapon = new RangedWeapon(1,0,0,2000,1,new Texture());
    }

    @Override
    public void update(int delta_time) {
        weapon.update(delta_time);

        float dist = players[0].pos.distance(pos);
        if (dist <= (players[0].getR() + getR()) / 2 ) {
            Vector3f shooting_pos = new Vector3f(pos.x, pos.y, 0);
            shooting_pos.x +=   (getR() + Bullet.RADIUS) / 2 * Math.cos(fi);
            shooting_pos.y +=   (getR() + Bullet.RADIUS) / 2 * Math.sin(fi);
            if (weapon.canShot) {
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
    public void shot() {

    }
}
