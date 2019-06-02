package Game.Logic;

import Game.Graphics.Texture;
import org.joml.Vector2f;

public class Bullet extends TexturedEntity {

    public static float SPEED = 10.0f / 1000;
    public static float RADIUS = 0.3f;
    public static int TTL = 3000;
    public static int COUNT_OF_PENETRATIONS = 1;
    protected int ttl;
    protected int damage;
    private int time_lived = 0;

    public Bullet(float x, float y, float r, int damage, int ttl/*in millis*/, Vector2f velocity, int textureID) {
        super(x, y, r, textureID);
        this.ttl = ttl;
        this.velocity = velocity;
        this.damage = damage;
        healthPoint = COUNT_OF_PENETRATIONS;
    }

    @Override
    public void update(int dTime) {
        time_lived += dTime;
        if (time_lived >= ttl) {
            shouldExist = false;
        }
    }

    @Override
    public void collidesWith(Entity entity) {
        if (!(entity instanceof Bullet)) {
            addHP(-1);
            entity.addHP(-damage);
        }
    }
}
