/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game;

import com.artemis.*;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.math.Vector2;
import me.lachlanap.terramutable.game.physics.PhysicsEngine;
import me.lachlanap.terramutable.game.stat.StatsCollector;

/**
 *
 * @author lachlan
 */
public class PhysicsSystem extends AbstractTimedSystem {

    @Mapper
    ComponentMapper<Position> pm;

    @Mapper
    ComponentMapper<PhysicsBody> bm;

    private final PhysicsEngine engine;

    public PhysicsSystem(StatsCollector collector, PhysicsEngine engine) {
        super(collector, Aspect.getAspectForAll(Position.class, PhysicsBody.class));

        this.engine = engine;
    }

    @Override
    protected void inserted(Entity e) {
        Position pos = pm.get(e);
        PhysicsBody body = bm.get(e);

        int id = engine.addBody(body.mesh, pos.x, pos.y, body.fixed);
        body.bodyId = id;
    }

    @Override
    protected void removed(Entity e) {
        PhysicsBody body = bm.get(e);

        engine.removeBody(body.bodyId);
    }

    @Override
    protected void begin() {
        super.begin();
        engine.update(1 / 60f);
    }

    @Override
    protected void process(Entity e) {
        Position pos = pm.get(e);
        PhysicsBody body = bm.get(e);

        if (!body.fixed) {
            Vector2 position = engine.getPositionOf(body.bodyId);
            float rotation = engine.getRotationOf(body.bodyId);

            pos.x = position.x;
            pos.y = position.y;
            pos.angle = rotation;
        }
    }
}
