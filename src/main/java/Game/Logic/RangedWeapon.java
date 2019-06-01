package Game.Logic;

import Game.Graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class RangedWeapon {

    protected int cooldown; //millis before next hit
    protected boolean canShot = true;
    protected int damage = 1;
    private int passedFromLastAttack = 0;
    private int passedFrpmLastShot = 0;

    private int countOfBarrel;
    private float sprayAngle;
    private float widthBetweenBurrel;
    private Bullet ammo;


    public RangedWeapon(int countOfBarrel, float sprayAngle, float widthBetweenBarrel, int cooldown, int damage, Bullet ammo) {
        this.countOfBarrel = countOfBarrel;
        this.sprayAngle = sprayAngle;
        this.widthBetweenBurrel = widthBetweenBarrel;
        this.cooldown = cooldown;
        this.damage = damage;
        this.ammo = ammo;

    }

    public Bullet[] shot(Vector3f pos, float angle) {
        if (canShot) {
            canShot = false;
            Bullet[] bullets = new Bullet[countOfBarrel];
            if (countOfBarrel == 1) {
                Vector2f velocity = new Vector2f((float) (Bullet.SPEED * Math.cos(angle)),
                        (float) (Bullet.SPEED * Math.sin(angle)));
                bullets[0] = new Bullet(pos.x, pos.y, ammo.r, damage, ammo.ttl, velocity, ammo.texture);
            } else {
                float d_angle = sprayAngle / (countOfBarrel - 1);
                float cur_angle = angle - sprayAngle / 2;
                for (int i = 0; i < countOfBarrel; i++) {
                    Vector2f velocity = new Vector2f((float) (Bullet.SPEED * Math.cos(cur_angle)),
                            (float) (Bullet.SPEED * Math.sin(cur_angle)));
                    bullets[i] = new Bullet(pos.x, pos.y, ammo.r, damage, ammo.ttl, velocity, ammo.texture);
                    cur_angle += d_angle;
                }
            }
            return bullets;
        }
        return null;
    }


    public boolean canShot() {
        return canShot;
    }

    public void update(int dTime) {
        if (!canShot) {
            passedFromLastAttack += dTime;
            if (passedFromLastAttack >= cooldown) {
                passedFromLastAttack = 0;
                canShot = true;
            }
        }
    }

}
