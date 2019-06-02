package Game.Logic;

import Game.Network.BitsFormatHandler;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Entity {

    /* Core format:
    * 0 - textureId
    * 1_4 - x
    * 5_8 - y
    * 9_12 - r
    * 13_17 - fi
    * 18_21 - entityId
    * 22 - service field
    * */

    protected Vector3f pos;
    protected float r;
    protected float fi = 0;
    protected Vector2f velocity;
    protected boolean shouldExist = true;
    protected int healthPoint = 1;
    protected int entityId;
    public static int id = 0;

    private byte[] core = new byte[23];

    public byte[] getCore() {
        return core;
    }

    public int getEntityId() { return entityId; }

    public Entity(float x, float y, float r) {
        pos = new Vector3f(x, y, 0);
        this.r = r;
        this.entityId = id++;
        velocity = new Vector2f();

        BitsFormatHandler.writeFloatBits(x, core, BitsFormatHandler.x);
        BitsFormatHandler.writeFloatBits(y, core, BitsFormatHandler.y);
        BitsFormatHandler.writeFloatBits(r, core, BitsFormatHandler.r);
        BitsFormatHandler.writeFloatBits(id, core, BitsFormatHandler.id);
    }

    public boolean shouldExist() {
        return shouldExist;
    }

    public float getFi() {
        return fi;
    }

    public void setFi(float fi) {
        this.fi = fi;
        BitsFormatHandler.writeFloatBits(fi, core, BitsFormatHandler.fi);
    }

    public float getX() { return pos.x; }
    public float getY() { return pos.y; }

    public void moveX(float delta) {
        pos.x += delta;
        BitsFormatHandler.writeFloatBits(pos.x, core, BitsFormatHandler.x);
    }

    public void moveY(float delta) {
        pos.y += delta;
        BitsFormatHandler.writeFloatBits(pos.y, core, BitsFormatHandler.y);
    }

    public float getR() {
        return r;
    }

    public Vector3f getPos() {
        return pos;
    }

    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public abstract void update(int dTime);

    public void setVelocity(float x, float y) {
        velocity.x = x;
        velocity.y = y;
    }
    public Vector2f getVelocity() {
        return velocity;
    }

    public void addHP(int d_hp) {
        healthPoint += d_hp;
        if (healthPoint <= 0) {
            shouldExist = false;
        }
    }

    public int getHP() {
        return healthPoint;
    }

    public abstract void collidesWith(Entity entity);

}