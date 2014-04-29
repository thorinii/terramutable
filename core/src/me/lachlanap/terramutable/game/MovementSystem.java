package me.lachlanap.terramutable.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import me.lachlanap.terramutable.game.stat.StatsCollector;

/**
 *
 * @author Lachlan Phillips
 */
public class MovementSystem extends AbstractTimedSystem {

    @Mapper
    ComponentMapper<Position> pm;

    public MovementSystem(StatsCollector collector) {
        super(collector, Aspect.getAspectForAll(Position.class));
    }

    @Override
    protected void process(Entity e) {
        Position position = pm.get(e);

        position.x += 0.4f * world.getDelta();
    }
}
