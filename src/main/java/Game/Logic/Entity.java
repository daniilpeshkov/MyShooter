package Game.Logic;

import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class Entity {

    protected Vector3f pos;
    protected float r;
    protected float fi = 0;
    protected Vector2f velocity;
    protected boolean shouldExist = true;
    protected int healthPoint = 1;

    private byte[] core = new byte[10];

    public byte[] getCore() {
        return core;
    }

    public Entity(float x, float y, float r) {
        pos = new Vector3f(x, y, 0);
        this.r = r;
        velocity = new Vector2f();
    }

    public boolean shouldExist() {
        return shouldExist;
    }

    public float getFi() {
        return fi;
    }

    public void setFi(float fi) {
        this.fi = fi;
    }
    public void setDeciFi(int fi) {
        this.fi = fi / 10000f;
        core[8] = (byte)(fi >> 8);
        core[9] = (byte)(fi);
    }

    public float getX() { return pos.x; }
    public float getY() { return pos.y; }

    public void moveX(float delta) { pos.x += delta; }
    public void moveY(float delta) { pos.y += delta; }

    public void moveDeciX(float delta) {
        pos.x += delta;
        int posx = (int) (pos.x * 10000);

        core[0] = (byte)(posx >> 24);
        core[1] = (byte)(posx >> 16);
        core[2] = (byte)(posx >> 8);
        core[3] = (byte)posx;

        pos.x = posx / 10000f;
    }
    public void moveDeciY(float delta) {
        pos.y += delta;
        int posy = (int) (pos.y * 10000);

        core[4] = (byte)(posy >> 24);
        core[5] = (byte)(posy >> 16);
        core[6] = (byte)(posy >> 8);
        core[7] = (byte)posy;

        pos.y = posy / 10000f;
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