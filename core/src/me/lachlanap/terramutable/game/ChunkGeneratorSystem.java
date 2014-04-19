package me.lachlanap.terramutable.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import me.lachlanap.terramutable.game.terrain.Generator;

/**
 *
 * @author Lachlan Phillips
 */
public class ChunkGeneratorSystem extends EntityProcessingSystem {

    private final Generator generator;

    @Mapper
    ComponentMapper<Chunk> cm;

    public ChunkGeneratorSystem(Generator generator) {
        super(Aspect.getAspectForAll(Chunk.class).exclude(PixelData.class));

        this.generator = generator;
    }

    @Override
    protected void process(Entity e) {
        Chunk chunk = cm.get(e);

        PixelData data = generator.generate(chunk.cx, chunk.cy);

        e.addComponent(data);
        e.changedInWorld();
    }
}
