package me.lachlanap.terramutable.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import me.lachlanap.terramutable.game.physics.BodyMesh;
import me.lachlanap.terramutable.game.stat.StatsCollector;
import me.lachlanap.terramutable.game.terrain.SquelData;

/**
 *
 * @author Lachlan Phillips
 */
public class PhysicsMeshingSystem extends AbstractTimedSystem {

    @Mapper
    ComponentMapper<Chunk> cm;
    @Mapper
    ComponentMapper<ChunkData> cdm;

    public PhysicsMeshingSystem(StatsCollector collector) {
        super(collector, Aspect.getAspectForAll(ChunkData.class).exclude(PhysicsBody.class));
    }

    @Override
    protected void process(Entity e) {
        Chunk chunk = cm.get(e);

        if (chunk.cy == -1 && chunk.cx <= 0) {
            SquelData data = cdm.get(e).pixelData;

            BodyMesh mesh = new BodyMesh(data);

            PhysicsBody body = new PhysicsBody(mesh, true);
            e.addComponent(body);
            e.changedInWorld();
        }
    }
}
