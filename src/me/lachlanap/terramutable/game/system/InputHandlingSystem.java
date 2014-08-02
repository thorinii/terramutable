package me.lachlanap.terramutable.game.system;

import com.artemis.*;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import me.lachlanap.terramutable.game.EntityFactory;
import me.lachlanap.terramutable.game.component.InputStatus;
import me.lachlanap.terramutable.game.component.Position;

/**
 *
 * @author lachlan
 */
public class InputHandlingSystem extends EntityProcessingSystem {

    @Mapper
    private ComponentMapper<InputStatus> im;
    @Mapper
    private ComponentMapper<Position> pm;

    public InputHandlingSystem() {
        super(Aspect.getAspectForAll(InputStatus.class, Position.class));
    }

    @Override
    protected void process(Entity e) {
        InputStatus i = im.get(e);
        Position p = pm.get(e);

        i.reloadTimer -= world.getDelta();
        if (i.reloadTimer < 0)
            i.reloadTimer = 0;

        if (i.shooting) {
            if (i.reloadTimer <= 0) {
                EntityFactory.makeBullet(world, e, p.toVector(), i.target).addToWorld();
                System.out.println(i);
                i.reloadTimer = 0.1f;
            }
        }
    }
}
