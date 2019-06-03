package Game.Logic;

import Game.Logic.Enemies.RunningEnemy;
import Main.Main;

import java.util.*;

public class GameWorld {

    public static int SPAWN_COOLDOWN = 1000;
    public float SPAWN_RANGE = 50f;
    public int timePassed;

    private List<Entity> entities = new ArrayList<>();

    private List<Player> players = new ArrayList<>();

    public List<Player> getPlayers()  {
        return players;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        if (entity instanceof Player) {
            players.add((Player) entity);
        }
        entities.add(entity);   
    }

    public void clearEntities() {
        entities.clear();
    }

    public void update(int delta_t) {

        timePassed += delta_t;

        if (timePassed > SPAWN_COOLDOWN) {
            timePassed = 0;
            spawnEnemy();
        }



        List<Entity> entitiesToRemove = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            if (!entity.shouldExist()) {
                entitiesToRemove.add(entity);
                continue;
            }
            entity.update(delta_t);
        }

        int size = entities.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                Entity e1 = entities.get(i);
                Entity e2 = entities.get(j);

                if (!(e1 instanceof Bullet) && !(e2 instanceof Bullet)) {

                    float distance = e1.pos.distance(e2.pos);

                    if (distance < (e1.r + e2.r) / 2.0f) {
                        float fi = (float) Math.atan2(e1.pos.y - e2.pos.y, e1.pos.x - e2.pos.x);
                        float r = (e1.r + e2.r) / 2 - distance;
                        e1.pos.x += r / 2 * Math.cos(fi);
                        e1.pos.y += r / 2 * Math.sin(fi);

                        e2.pos.x -= r / 2 * Math.cos(fi);
                        e2.pos.y -= r / 2 * Math.sin(fi);
                    }
                }

                if (e1.pos.distance(e2.pos) < (e1.getR() + e2.getR()) / 2) {
                    e1.collidesWith(e2);
                    e2.collidesWith(e1);
                    if (!e1.shouldExist) entitiesToRemove.add(e1);
                    if (!e2.shouldExist) entitiesToRemove.add(e2);
                }
            }
        }

        for (Entity entity : entitiesToRemove) {
            entities.remove(entity);
        }

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            if (entity instanceof Player) {
                ((Player)entity).updateVelocity();
            }
            entity.moveY(entity.getVelocity().y * delta_t);
            entity.moveX(entity.getVelocity().x * delta_t);
        }
    }

    private void spawnEnemy() {
        float fi = (float) (Math.random() * Math.PI * 2);
        addEntity(new RunningEnemy(this, (float)(SPAWN_RANGE * Math.cos(fi)), (float)(SPAWN_RANGE * Math.sin(fi)),1,
                5, 3f/ 1000, 9, players));

    }


}
