package me.lachlanap.terramutable.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import me.lachlanap.terramutable.game.stat.StatsCollector;
import me.lachlanap.terramutable.game.terrain.PixelData;

/**
 *
 * @author Lachlan Phillips
 */
public class MeshRefreshingSystem extends AbstractTimedSystem {

    @Mapper
    ComponentMapper<ChunkData> cdm;

    public MeshRefreshingSystem(StatsCollector collector) {
        super(collector, Aspect.getAspectForAll(ChunkData.class, MeshView.class));
    }

    @Override
    protected void process(Entity e) {
        PixelData data = cdm.get(e).pixelData;

        if (data.isDirty()) {
            e.removeComponent(MeshView.class);
            e.changedInWorld();
        }
    }
}
