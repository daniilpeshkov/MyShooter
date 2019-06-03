package Game.Logic.Enemies;

import Game.Graphics.Texture;
import Game.Logic.*;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

public class RunningEnemy extends Enemy {

    public static float RANG_FOR_CHARGE = 100;

    public RunningEnemy(GameWorld world, float x, float y, float r, int hp, float speed, int textureID, List<Player> players) {
        super(world, x, y, r, hp, speed, textureID, players);
        weapon = new MeleeWeapon(0.1f, 500, 1,
                new Bullet(0, 0, 0.3f, 1, 4000, new Vector2f(0, 0), 0));
    }

    @Override
    public void update(int dTime) {
        weapon.update(dTime);

        Player nearestPlayer = players.get(0);
        float dist = players.get(0).getPos().distance(pos);
        for(int i = 1; i < players.size(); i++){
            float tmpDist = players.get(i).getPos().distance(pos);
            if (tmpDist < dist) {
                dist = tmpDist;
                nearestPlayer = players.get(i);
            }
        }



        if (dist <= (nearestPlayer.getR() + getR()) / 2) {
            Vector3f shooting_pos = new Vector3f(pos.x, pos.y, 0);
            shooting_pos.x += (getR() + Bullet.RADIUS) / 2 * Math.cos(fi);
            shooting_pos.y += (getR() + Bullet.RADIUS) / 2 * Math.sin(fi);
            if (weapon.canShot()) {
                for (Entity entity : weapon.shot(shooting_pos, fi)) {
                    world.addEntity(entity);
                }
            }
        } else if (dist <= RANG_FOR_CHARGE) {
            follow(nearestPlayer);
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
