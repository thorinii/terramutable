package me.lachlanap.terramutable.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

/**
 *
 * @author Lachlan Phillips
 */
public class MovementSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<Position> pm;

    public MovementSystem() {
        super(Aspect.getAspectForAll(Position.class));
    }

    @Override
    protected void process(Entity e) {
        Position position = pm.get(e);

        position.x += 0.4f * world.getDelta();
    }
}
