package me.lachlanap.terramutable.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Mesh;
import me.lachlanap.terramutable.game.terrain.Mesher;

/**
 *
 * @author Lachlan Phillips
 */
public class MeshingSystem extends EntityProcessingSystem {

    private final Mesher mesher;

    @Mapper
    ComponentMapper<PixelData> pdm;

    public MeshingSystem(Mesher mesher) {
        super(Aspect.getAspectForAll(PixelData.class).exclude(MeshView.class));

        this.mesher = mesher;
    }

    @Override
    protected void process(Entity e) {
        PixelData data = pdm.get(e);

        Mesh mesh = mesher.mesh(data);
        data.clearDirty();

        e.addComponent(new MeshView(mesh));
        e.changedInWorld();
    }
}
