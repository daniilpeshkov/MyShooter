package Game.Logic;

import Game.Graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class RangedWeapon extends Weapon {

    private Texture bullet_tex;
    private int passedFrpmLastShot = 0;

    private int countOfBarrel;
    private float sprayAngle;
    private float widthBetweenBurrel;

    public RangedWeapon(int countOfBarrel, float sprayAngle, float widthBetweenBarrel, int cooldown, int damage, Texture bullet_tex) {
        this.countOfBarrel = countOfBarrel;
        this.sprayAngle = sprayAngle;
        this.widthBetweenBurrel = widthBetweenBarrel;
        this.bullet_tex = bullet_tex;
        this.cooldown = cooldown;
        this.damage = damage;
    }

    @Override
    public Bullet[] shot(Vector3f pos, float angle) {
        if (canShot) {
            canShot = false;
            Bullet[] bullets = new Bullet[countOfBarrel];
            if (countOfBarrel == 1) {
                Vector2f velocity = new Vector2f((float) (Bullet.SPEED * Math.cos(angle)), (float) (Bullet.SPEED * Math.sin(angle)));
                bullets[0] = new Bullet(pos.x, pos.y, Bullet.RADIUS, damage,bullet_tex, Bullet.TTL, velocity);
            } else {
                float d_angle = sprayAngle / (countOfBarrel - 1);
                float cur_angle = angle - sprayAngle / 2;
                for (int i = 0; i < countOfBarrel; i++) {
                    Vector2f velocity = new Vector2f((float) (Bullet.SPEED * Math.cos(cur_angle)),
                            (float) (Bullet.SPEED * Math.sin(cur_angle)));
                    bullets[i] = new Bullet(pos.x, pos.y, Bullet.RADIUS, damage, bullet_tex, Bullet.TTL, velocity);
                    cur_angle += d_angle;
                }

            }
            return bullets;
        }
        return null;
    }





}
