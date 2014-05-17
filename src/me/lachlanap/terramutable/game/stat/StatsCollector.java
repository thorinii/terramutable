/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.stat;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lachlan
 */
public class StatsCollector {

    private final List<StatBuffer> buffers;

    public StatsCollector() {
        this.buffers = new ArrayList<>();
    }

    private StatBuffer create(String name, int size) {
        StatBuffer buffer = new StatBuffer(name, size);
        buffers.add(buffer);
        return buffer;
    }

    public StatBuffer create(String name) {
        return create(name, 500);
    }

    public int numberOfStats() {
        return buffers.size();
    }

    public StatBuffer get(int currentIndex) {
        return buffers.get(currentIndex);
    }

    public StatBuffer get(String name) {
        for (StatBuffer buffer : buffers)
            if (buffer.getName().equals(name))
                return buffer;
        throw new IllegalArgumentException("Could not find StatBuffer " + name);
    }

    public List<StatBuffer> getSub(String parentName) {
        List<StatBuffer> found = new ArrayList<>();
        for (StatBuffer buffer : buffers)
            if (buffer.getName().startsWith(parentName))
                found.add(buffer);
        return found;
    }
}
