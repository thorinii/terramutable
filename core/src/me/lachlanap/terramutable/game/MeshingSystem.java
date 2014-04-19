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
    ComponentMapper<Chunk> cm;
    @Mapper
    ComponentMapper<PixelData> pdm;
    @Mapper
    ComponentMapper<Position> pm;

    public MeshingSystem(Mesher mesher) {
        super(Aspect.getAspectForAll(Chunk.class, PixelData.class).exclude(MeshView.class));

        this.mesher = mesher;
    }

    @Override
    protected void process(Entity e) {
        Chunk chunk = cm.get(e);
        PixelData data = pdm.get(e);
        Position position = pm.getSafe(e);

        Mesh mesh = mesher.mesh(data);
        data.clearDirty();

        if (position == null) {
            position = new Position();
            e.addComponent(position);
        }

        position.x = chunk.cx * PixelData.SIZE_IN_PIXELS * Mesher.PIXEL_SIZE_IN_METRES;
        position.y = chunk.cy * PixelData.SIZE_IN_PIXELS * Mesher.PIXEL_SIZE_IN_METRES;

        e.addComponent(new MeshView(mesh));
        e.changedInWorld();
    }
}
