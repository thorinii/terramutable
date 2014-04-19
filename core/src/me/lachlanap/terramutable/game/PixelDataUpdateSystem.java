package me.lachlanap.terramutable.game;

import me.lachlanap.terramutable.game.terrain.PixelData;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.IntervalEntityProcessingSystem;

/**
 *
 * @author Lachlan Phillips
 */
public class PixelDataUpdateSystem extends IntervalEntityProcessingSystem {

    @Mapper
    ComponentMapper<ChunkData> cdm;

    public PixelDataUpdateSystem() {
        super(Aspect.getAspectForAll(ChunkData.class), 1f);
    }

    @Override
    protected void process(Entity e) {
        PixelData data = cdm.get(e).pixelData;

        data.set((int) (Math.random() * data.getWidth()),
                 (int) (Math.random() * data.getHeight()),
                 true);
    }
}
