package me.lachlanap.terramutable.game.system;

import com.artemis.*;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Input;
import me.lachlanap.terramutable.game.component.InputStatus;
import me.lachlanap.terramutable.game.component.Position;

/**
 *
 * @author lachlan
 */
public class InputCollectingSystem extends EntityProcessingSystem {

    private final Input input;
    private final int screenWidth, screenHeight;

    @Mapper
    private ComponentMapper<InputStatus> im;
    @Mapper
    private ComponentMapper<Position> pm;

    public InputCollectingSystem(Input input, int screenWidth, int screenHeight) {
        super(Aspect.getAspectForAll(InputStatus.class, Position.class));

        this.input = input;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @Override
    protected void process(Entity e) {
        InputStatus i = im.get(e);
        Position p = pm.get(e);

        i.shooting = input.isButtonPressed(Input.Buttons.LEFT);
        i.target.x = (input.getX() - screenWidth / 2f) / RenderingSystem.SCREEN_PIXELS_PER_METRE - p.x;
        i.target.y = (screenHeight / 2f - input.getY()) / RenderingSystem.SCREEN_PIXELS_PER_METRE - p.y;
    }
}
