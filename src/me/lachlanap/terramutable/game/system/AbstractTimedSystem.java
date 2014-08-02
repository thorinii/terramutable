/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.system;

import com.artemis.Aspect;
import com.artemis.systems.EntityProcessingSystem;
import me.lachlanap.terramutable.game.stat.StatBuffer;
import me.lachlanap.terramutable.game.stat.StatsCollector;

/**
 *
 * @author lachlan
 */
public abstract class AbstractTimedSystem extends EntityProcessingSystem {

    private final StatBuffer stat;
    private long beginTime;

    public AbstractTimedSystem(StatsCollector collector, Aspect aspect) {
        super(aspect);

        stat = collector.create(getName());
    }

    private String getName() {
        String classname = getClass().getSimpleName()
                .toLowerCase();

        return "system." + classname.substring(0, classname.lastIndexOf("system"));
    }

    @Override
    protected void begin() {
        beginTime = System.currentTimeMillis();
    }

    @Override
    protected void end() {
        long endTime = System.currentTimeMillis();
        long millisTaken = endTime - beginTime;

        float timeTaken = millisTaken / 1000f;

        stat.push(timeTaken);
    }
}
