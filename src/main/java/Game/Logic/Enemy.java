package Game.Logic;

public abstract class Enemy extends TexturedEntity {

    protected Player[] players;
    protected RangedWeapon weapon = null;
    protected GameWorld world;
    protected float speed = 0;

    public Enemy(GameWorld world, float x, float y, float r, int hp, int textureID, Player[] players) {
        super(x, y, r, textureID);
        this.players = players;
        healthPoint = hp;
        this.world = world;
    }

    public Enemy(GameWorld world, float x, float y, float r, int hp, float speed, int textureID, Player[] players) {
        super(x, y, r, textureID);
        this.players = players;
        healthPoint = hp;
        this.world = world;
        this.speed = speed;
    }

    public void equipWeapon(RangedWeapon weapon) {
        this.weapon = weapon;
    }

    public RangedWeapon getWeapon() {
        return weapon;
    }

    public void follow(Entity entity) {
        float angle = (float) Math.atan2(entity.pos.y - pos.y, entity.pos.x - pos.x);
        setFi(angle);

        this.velocity.x = (float) (speed * Math.cos(angle));
        this.velocity.y = (float) (speed * Math.sin(angle));
    }

    public void stop() {
        this.velocity.x = 0;
        this.velocity.y = 0;
    }

    public void setMaxVelocity(float velocity) {
        this.speed = velocity;
    }

    @Deprecated
    public abstract void shot();

}
