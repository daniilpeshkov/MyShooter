package Game.Logic;

import Game.Graphics.Animation;
import Game.Graphics.Texture;
import org.joml.Vector3f;

public class Player extends TexturedEntity {

    Weapon weapon = null;

    Animation animation;

    public static float SPEED = 5.0f / 1000;

    public Player(float x, float y, float r, int hp, Texture texture) {
        super(x, y, r, texture);
        healthPoint = hp;
    }

    @Override
    public void update(int dTime) {
        if (weapon != null) {
            weapon.update(dTime);
        }
    }

    public void equipWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void shot(GameWorld world) {
//        System.currentTimeMillis()
        if (weapon != null && weapon.canShot()) {
            Vector3f shooting_pos = new Vector3f(pos.x, pos.y, 0);
            shooting_pos.x +=  (getR() + Bullet.RADIUS + getR() / 8) / 2 * Math.cos(fi);
            shooting_pos.y += (getR() + Bullet.RADIUS + getR() / 8) / 2 * Math.sin(fi);
            for(Entity entity :weapon.shot(shooting_pos, fi)){
                world.addEntity(entity);
            }
        }
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

}
