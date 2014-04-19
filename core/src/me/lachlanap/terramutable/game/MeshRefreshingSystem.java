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
public class MeshRefreshingSystem extends EntityProcessingSystem {

    @Mapper
    ComponentMapper<PixelData> pdm;

    public MeshRefreshingSystem() {
        super(Aspect.getAspectForAll(PixelData.class, MeshView.class));
    }

    @Override
    protected void process(Entity e) {
        PixelData data = pdm.get(e);

        if (data.isDirty()) {
            e.removeComponent(MeshView.class);
            e.changedInWorld();
        }
    }
}
