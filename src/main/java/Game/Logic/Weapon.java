package Game.Logic;

import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Weapon {

    protected int cooldown; //millis before next hit

    private int passedFromLastAttack = 0;

    protected boolean canShot = true;
    protected int damage = 1;

    public boolean canShot() {
        return canShot;
    }

    public abstract Bullet[] shot(Vector3f pos, float angle);


    public void update(int delta_t) {
        if (!canShot) {
            passedFromLastAttack += delta_t;
            if (passedFromLastAttack >= cooldown) {
                passedFromLastAttack = 0;
                canShot = true;
            }
        }
    }

}
