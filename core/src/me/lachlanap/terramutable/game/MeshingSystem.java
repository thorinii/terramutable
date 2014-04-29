package me.lachlanap.terramutable.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import me.lachlanap.terramutable.game.stat.StatsCollector;
import me.lachlanap.terramutable.game.terrain.Mesher;
import me.lachlanap.terramutable.game.terrain.PixelData;

/**
 *
 * @author Lachlan Phillips
 */
public class MeshingSystem extends AbstractTimedSystem {

    private final Mesher mesher;

    private final BlockingQueue<MeshingUnit> toProcess;
    private final ConcurrentMap<Chunk, MeshBuilder> finishedChunks;

    private final int numberOfExecutors;
    private final ThreadGroup executorGroup;

    @Mapper
    ComponentMapper<Chunk> cm;
    @Mapper
    ComponentMapper<ChunkData> cdm;
    @Mapper
    ComponentMapper<Position> pm;

    public MeshingSystem(StatsCollector collector, Mesher mesher) {
        super(collector, Aspect.getAspectForAll(Chunk.class, ChunkData.class).exclude(MeshView.class));

        this.mesher = mesher;

        this.toProcess = new LinkedBlockingQueue<>();
        this.finishedChunks = new ConcurrentHashMap<>();

        this.numberOfExecutors = Runtime.getRuntime().availableProcessors();
        this.executorGroup = new ThreadGroup("Mesher Executors");
        this.executorGroup.setDaemon(true);
    }

    @Override
    protected void initialize() {
        for (int i = 0; i < numberOfExecutors; i++) {
            Thread thread = new Thread(executorGroup, new AsynchronousMesher(mesher, toProcess, finishedChunks));
            thread.start();
        }
    }

    @Override
    protected void dispose() {
        executorGroup.interrupt();
    }

    @Override
    protected void inserted(Entity e) {
        Chunk chunk = cm.get(e);
        PixelData data = cdm.get(e).pixelData;

        toProcess.add(new MeshingUnit(chunk, data));
    }

    @Override
    protected void process(Entity e) {
        Chunk chunk = cm.get(e);

        MeshBuilder meshBuilder = finishedChunks.get(chunk);
        if (meshBuilder != null) {
            finishedChunks.remove(chunk);

            Mesh mesh = meshBuilder.end();

            PixelData data = cdm.get(e).pixelData;
            data.clearDirty();

            Position position = pm.getSafe(e);

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

    private static class MeshingUnit {

        final Chunk chunk;
        final PixelData data;

        private MeshingUnit(Chunk chunk, PixelData data) {
            this.chunk = chunk;
            this.data = data;
        }
    }

    private static class AsynchronousMesher implements Runnable {

        final Mesher mesher;
        final BlockingQueue<MeshingUnit> toProcess;
        final ConcurrentMap<Chunk, MeshBuilder> finishedChunks;

        public AsynchronousMesher(Mesher mesher, BlockingQueue<MeshingUnit> toProcess, ConcurrentMap<Chunk, MeshBuilder> finishedChunks) {
            this.mesher = mesher;
            this.toProcess = toProcess;
            this.finishedChunks = finishedChunks;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    MeshingUnit unit = toProcess.take();
                    MeshBuilder mesh = process(unit.data);
                    finishedChunks.put(unit.chunk, mesh);
                }
            } catch (InterruptedException ie) {
            }
        }

        private MeshBuilder process(PixelData data) {
            return mesher.mesh(data);
        }
    }
}
