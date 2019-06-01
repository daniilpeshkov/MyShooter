package Game.Logic;

import java.util.*;

public class GameWorld {
    private List<Entity> entities = new ArrayList<>();

    public List<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }




    public void update(int delta_t) {

        List<Entity> entitiesToRemove = new ArrayList<>();
        for (int i =0; i < entities.size(); i++) {
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
                    if (e1.pos.distance(e2.pos) < (e1.getR() + e2.getR()) / 2.0f) {
                        float fi = (float) Math.atan2(e1.pos.y - e2.pos.y, e1.pos.x - e2.pos.x);
                        if (!(e1 instanceof Player)) {
                            e1.velocity.x += RunningEnemy.SPEED * Math.cos(fi);
                            e1.velocity.y += RunningEnemy.SPEED * Math.sin(fi);
                        }
                        if (!(e2 instanceof Player)) {
                        e2.velocity.x -= RunningEnemy.SPEED * Math.cos(fi);
                        e2.velocity.y -= RunningEnemy.SPEED * Math.sin(fi);
                        }
                    }
                }

                if (e1.pos.distance(e2.pos) < (e1.getR() + e2.getR())/ 2) {
                    if (e1 instanceof Bullet && !(e2 instanceof Bullet)) {
                        e1.addHP(-1);
                        e2.addHP(-((Bullet)e1).damage);
                    } else if (e2 instanceof Bullet && !(e1 instanceof Bullet)) {
                        e2.addHP(-1);
                        e1.addHP(-((Bullet)e2).damage);
                    }
                    if (!e1.shouldExist) entitiesToRemove.add(e1);
                    if (!e2.shouldExist) entitiesToRemove.add(e2);
                }
            }
        }

        for (Entity entity: entitiesToRemove) {
            entities.remove(entity);
        }

        for (int i =0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            entity.moveY(entity.getVelocity().y * delta_t);
            entity.moveX(entity.getVelocity().x * delta_t);
        }
    }


}
