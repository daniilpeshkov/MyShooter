package Game.Logic;

import Game.Graphics.Texture;

public class MeleeWeapon extends RangedWeapon {

    public MeleeWeapon(float attackRange, int cooldown, int damage, Bullet ammo) {
        super(1, 0, 0, cooldown, ammo);
        ammo.ttl = 100;
    }


}
