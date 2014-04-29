/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game;

import com.artemis.World;
import me.lachlanap.terramutable.game.stat.StatBuffer;
import me.lachlanap.terramutable.game.stat.StatsCollector;

/**
 *
 * @author lachlan
 */
public class GeneralStatGatherer {

    private static final float BYTES_TO_MB = 1024f * 1024f;

    private final StatBuffer fpsStat;
    private final StatBuffer memoryHeapStat, memoryUsedStat;
    private final StatBuffer entityCount;

    public GeneralStatGatherer(StatsCollector collector) {
        fpsStat = collector.create("fps");

        memoryHeapStat = collector.create("memory.heap");
        memoryUsedStat = collector.create("memory.used");

        entityCount = collector.create("entity.count");
    }

    public void update(World world, float dt) {
        doFps(dt);
        doMemory();
        doEntities(world);
    }

    private void doFps(float dt) {
        float fps = 1 / dt;
        if (fps != 0 && fps != Float.NaN)
            fpsStat.push(fps);
    }

    private void doMemory() {
        Runtime r = Runtime.getRuntime();
        long heap = r.totalMemory();
        long used = heap - r.freeMemory();
        memoryHeapStat.push(heap / BYTES_TO_MB);
        memoryUsedStat.push(used / BYTES_TO_MB);
    }

    private void doEntities(World world) {
        entityCount.push(world.getEntityManager().getActiveEntityCount());
    }
}
