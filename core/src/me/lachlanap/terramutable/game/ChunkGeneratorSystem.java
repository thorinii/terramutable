package me.lachlanap.terramutable.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import me.lachlanap.terramutable.game.stat.StatsCollector;
import me.lachlanap.terramutable.game.terrain.Generator;
import me.lachlanap.terramutable.game.terrain.PixelData;

/**
 *
 * @author Lachlan Phillips
 */
public class ChunkGeneratorSystem extends AbstractTimedSystem {

    private final Generator generator;

    @Mapper
    ComponentMapper<Chunk> cm;

    public ChunkGeneratorSystem(StatsCollector collector, Generator generator) {
        super(collector, Aspect.getAspectForAll(Chunk.class).exclude(ChunkData.class));

        this.generator = generator;
    }

    @Override
    protected void process(Entity e) {
        Chunk chunk = cm.get(e);

        PixelData data = generator.generate(chunk.cx, chunk.cy);

        e.addComponent(new ChunkData(data));
        e.changedInWorld();
    }
}
