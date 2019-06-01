package Game.Logic;

import Game.Graphics.Texture;
import org.joml.Vector3f;

public class ShootingEnemy extends Enemy {

    public static float SPEED = 3.0f / 1000;
    public static float RANG_FOR_CHARGE = 40;
    public static float RANG_FOR_SHOT = 3;

    public ShootingEnemy(GameWorld world, float x, float y, float r, int hp, RangedWeapon weapon, Texture texture, Player[] players) {
        super(world,x, y, r, hp, texture, players);
        this.weapon = weapon;
    }

    @Override
    public void update(int delta_time) {
        weapon.update(delta_time);
        float dist = players[0].pos.distance(pos);
        if (dist <= RANG_FOR_CHARGE) {
            float angle = (float) Math.atan2(players[0].pos.y - pos.y, players[0].pos.x - pos.x);


            if (dist < RANG_FOR_SHOT ) {
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
            shooting_pos.x +=  (players[0].getR() + Bullet.RADIUS) * Math.cos(fi);
            shooting_pos.y += (players[0].getR() + Bullet.RADIUS) * Math.sin(fi);


            if (weapon != null && weapon.canShot()) {
                for(Entity entity :weapon.shot(shooting_pos, fi)){
                    world.addEntity(entity);
                }
            }

        } else {
            this.velocity.x = 0;
            this.velocity.y = 0;
        }
    }

    @Override
    public void shot() {

    }
}
