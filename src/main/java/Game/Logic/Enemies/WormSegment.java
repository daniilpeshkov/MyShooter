package Game.Logic.Enemies;

import Game.Logic.Enemy;
import Game.Logic.Entity;
import Game.Logic.GameWorld;
import Game.Logic.Player;

import java.util.List;

public class WormSegment extends Enemy {

    //to the head
    private WormSegment previousSegment = null;

    private WormSegment nextSegment = null;

    public WormSegment(GameWorld world, float x, float y, float r, int hp, float speed, int textureID, List<Player> players) {
        super(world, x, y, r, hp, speed, textureID, players);
    }

    public static void generateWorm(GameWorld gameWorld, WormSegment head, float fi, int length) {
        gameWorld.addEntity(head);
        for (int i = 0; i < length; i++) {
            WormSegment worm = new WormSegment(gameWorld, (float) (head.pos.x + head.r * Math.cos(fi)),
                    (float) (head.pos.y + head.r * Math.sin(fi)), head.r, head.healthPoint, head.speed, head.textureID, head.players);
            head.addTail(worm);
            head = worm;
            gameWorld.addEntity(head);
        }
    }

    @Override
    public void shot() {

    }

    @Override
    public void update(int dTime) {
        if (previousSegment != null) {
            if (!previousSegment.shouldExist) {
                previousSegment = null;
            }
        }

        if (previousSegment != null) {
            follow(previousSegment);
        } else {
            follow(players.get(0));
        }

    }

    @Override
    public void collidesWith(Entity entity) {

    }

    public void setPreviousSegment(WormSegment previousSegment) {
        this.previousSegment = previousSegment;
    }

    public void addTail(WormSegment nextSegment) {
        this.nextSegment = nextSegment;
        nextSegment.previousSegment = this;
    }

}
