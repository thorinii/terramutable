package me.lachlanap.terramutable.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.math.Rectangle;
import java.util.HashSet;
import java.util.Set;
import me.lachlanap.terramutable.game.stat.StatsCollector;
import me.lachlanap.terramutable.game.terrain.Mesher;

/**
 *
 * @author Lachlan Phillips
 */
public class ChunkPagingSystem extends AbstractTimedSystem {

    private static final int PADDING = 2;
    private static final float CHUNK_SIZE_IN_SCREEN_PIXELS = Mesher.CHUNK_SIZE_IN_METRES * RenderingSystem.SCREEN_PIXELS_PER_METRE;

    private final RenderingSystem renderingSystem;

    private final Set<ChunkId> chunksCurrentlyPaged;

    @Mapper
    ComponentMapper<Chunk> cm;

    private int baseChunkX;
    private int baseChunkY;
    private int chunksInX;
    private int chunksInY;

    public ChunkPagingSystem(StatsCollector collector, RenderingSystem renderingSystem) {
        super(collector, Aspect.getAspectForAll(Chunk.class));

        this.renderingSystem = renderingSystem;

        this.chunksCurrentlyPaged = new HashSet<>();
    }

    @Override
    protected void begin() {
        super.begin();

        Rectangle viewport = renderingSystem.getViewportRectangle();

        baseChunkX = (int) Math.floor(viewport.x / CHUNK_SIZE_IN_SCREEN_PIXELS);
        baseChunkY = (int) Math.floor(viewport.y / CHUNK_SIZE_IN_SCREEN_PIXELS);
        chunksInX = (int) (viewport.width / CHUNK_SIZE_IN_SCREEN_PIXELS) + 1;
        chunksInY = (int) (viewport.height / CHUNK_SIZE_IN_SCREEN_PIXELS) + 1;
    }

    @Override
    protected void process(Entity e) {
        Chunk chunk = cm.get(e);

        if (chunk.cx < baseChunkX - PADDING || chunk.cx > (baseChunkX + chunksInX + PADDING)
            || chunk.cy < baseChunkY - PADDING || chunk.cy > (baseChunkY + chunksInY + PADDING)) {
            ChunkId id = new ChunkId(chunk);

            chunksCurrentlyPaged.remove(id);
            e.deleteFromWorld();
        }
    }

    @Override
    protected void end() {
        super.end();

        for (int x = -PADDING; x < chunksInX + PADDING; x++) {
            for (int y = -PADDING; y < chunksInY + PADDING; y++) {
                ChunkId id = new ChunkId(baseChunkX + x, baseChunkY + y);

                if (!chunksCurrentlyPaged.contains(id)) {
                    chunksCurrentlyPaged.add(id);
                    EntityFactory.makeChunk(world, id.cx, id.cy).addToWorld();
                }
            }
        }
    }

    private static class ChunkId {

        final int cx, cy;

        public ChunkId(int cx, int cy) {
            this.cx = cx;
            this.cy = cy;
        }

        public ChunkId(Chunk chunk) {
            this(chunk.cx, chunk.cy);
        }

        @Override
        public String toString() {
            return cx + "," + cy;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 97 * hash + this.cx;
            hash = 97 * hash + this.cy;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final ChunkId other = (ChunkId) obj;
            if (this.cx != other.cx)
                return false;
            return this.cy == other.cy;
        }
    }
}
