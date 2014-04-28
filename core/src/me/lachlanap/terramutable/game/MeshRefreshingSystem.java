package me.lachlanap.terramutable.game;

import me.lachlanap.terramutable.game.terrain.PixelData;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

/**
 *
 * @author Lachlan Phillips
 */
public class MeshRefreshingSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<ChunkData> cdm;

    public MeshRefreshingSystem() {
        super(Aspect.getAspectForAll(ChunkData.class, MeshView.class));
    }

    @Override
    protected void process(Entity e) {
        PixelData data = cdm.get(e).pixelData;

        //if (data.isDirty()) {
        e.removeComponent(ChunkData.class);
        e.removeComponent(MeshView.class);
        e.changedInWorld();
        //}
    }
}
