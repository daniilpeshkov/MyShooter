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

    public boolean shouldExist() {
        return shouldExist;
    }

    public Entity(float x, float y, float r) {
        pos = new Vector3f(x, y, 0);
        this.r = r;
        velocity = new Vector2f();
    }

    public void setFi(float fi) {
        this.fi = fi;
    }

    public float getFi() {
        return fi;
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public void moveX(float delta) {pos.x += delta;}
    public void moveY(float delta) {pos.y += delta;}

    public float getR() {
        return r;
    }

    public Vector3f getPos(){ return pos;}

    public void setPos(Vector3f pos){
        this.pos = pos;
    }

    public abstract void update(int delta_time);

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

    public int getHP(){
        return healthPoint;
    }
}
