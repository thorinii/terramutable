package me.lachlanap.terramutable.game;

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
    ComponentMapper<PixelData> pdm;

    public PixelDataUpdateSystem() {
        super(Aspect.getAspectForAll(PixelData.class), 1f);
    }

    @Override
    protected void process(Entity e) {
        PixelData data = pdm.get(e);

        data.set((int) (Math.random() * data.getWidth()),
                 (int) (Math.random() * data.getHeight()),
                 true);
    }
}
