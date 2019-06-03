package Game.Logic.Enemies;

import Game.Graphics.Texture;
import Game.Logic.*;
import org.joml.Vector3f;

import java.util.List;

public class ShootingEnemy extends Enemy {

    public static float SPEED = 3.0f / 1000;
    public static float RANG_FOR_CHARGE = 40;
    public static float RANG_FOR_SHOT = 3;

    public ShootingEnemy(GameWorld world, float x, float y, float r, int hp, RangedWeapon weapon, int textureID, List<Player> players) {
        super(world, x, y, r, hp, textureID, players);
        this.weapon = weapon;
    }

    @Override
    public void update(int dTime) {
        weapon.update(dTime);
        float dist = players.get(0).getPos().distance(pos);
        if (dist <= RANG_FOR_CHARGE) {
            float angle = (float) Math.atan2(players.get(0).getPos().y - pos.y, players.get(0).getPos().x - pos.x);


            if (dist < RANG_FOR_SHOT) {
                this.velocity.x = -(float) (SPEED * Math.cos(angle));
                this.velocity.y = -(float) (SPEED * Math.sin(angle));
            } else if (dist > RANG_FOR_SHOT) {
                this.velocity.x = (float) (SPEED * Math.cos(angle));
                this.velocity.y = (float) (SPEED * Math.sin(angle));
            }

            this.velocity.x += -(float) (SPEED * Math.cos(angle + Math.PI / 2));
            this.velocity.y += -(float) (SPEED * Math.sin(angle + Math.PI / 2));

            fi = angle;

            Vector3f shooting_pos = new Vector3f(pos.x, pos.y, 0);
            shooting_pos.x += (players.get(0).getR() + Bullet.RADIUS) * Math.cos(fi);
            shooting_pos.y += (players.get(0).getR() + Bullet.RADIUS) * Math.sin(fi);


            if (weapon != null && weapon.canShot()) {
                for (Entity entity : weapon.shot(shooting_pos, fi)) {
                    world.addEntity(entity);
                }
            }

        } else {
            this.velocity.x = 0;
            this.velocity.y = 0;
        }
    }

    @Override
    public void collidesWith(Entity entity) {

    }

    @Override
    public void shot() {

    }
}
