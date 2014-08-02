package me.lachlanap.terramutable.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.IntervalEntityProcessingSystem;
import me.lachlanap.terramutable.game.terrain.SquelData;

/**
 *
 * @author Lachlan Phillips
 */
public class SquelDataUpdateSystem extends IntervalEntityProcessingSystem {

    @Mapper
    ComponentMapper<ChunkData> cdm;

    public SquelDataUpdateSystem() {
        super(Aspect.getAspectForAll(ChunkData.class), 1f);
    }

    @Override
    protected void process(Entity e) {
        SquelData data = cdm.get(e).pixelData;

        data.set((int) (Math.random() * data.getWidth()),
                 (int) (Math.random() * data.getHeight()),
                 true);
    }
}