package Game.Logic;

import org.joml.Vector3f;

public class Player extends TexturedEntity {

    RangedWeapon weapon = null;

    private static final float SPEED = 4.0f / 1000f;

    public static final byte UP = 0b1;
    public static final byte DOWN = 0b10;
    public static final byte LEFT = 0b100;
    public static final byte RIGHT = 0b1000;
    public byte direction;

    public void updateDirection(byte direction) {
        this.direction = direction;
    }

    public void updateVelocity() {
        velocity.x = (-1 * (direction & LEFT) / LEFT + (direction & RIGHT)/ RIGHT) * SPEED;
        velocity.y = (-1 * (direction & DOWN) / DOWN + (direction & UP) / UP) * SPEED;
    }

    public Player(float x, float y, float r, int hp, int textureID) {
        super(x, y, r, textureID);
        setHP(hp);
    }

    @Override
    public void update(int dTime) {
        if (weapon != null) {
            weapon.update(dTime);
        }
    }

    @Override
    public void collidesWith(Entity entity) {

    }

    public void equipWeapon(RangedWeapon weapon) {
        this.weapon = weapon;
    }

    public RangedWeapon getWeapon() {
        return weapon;
    }

    public void shot(GameWorld world) {
        if (weapon != null && weapon.canShot()) {
            Vector3f shooting_pos = new Vector3f(pos.x, pos.y, 0);
            shooting_pos.x +=  (getR() + Bullet.RADIUS + getR() / 8) / 2 * Math.cos(fi);
            shooting_pos.y += (getR() + Bullet.RADIUS + getR() / 8) / 2 * Math.sin(fi);
            for(Entity entity :weapon.shot(shooting_pos, fi)){
                world.addEntity(entity);
            }
        }
    }

}
