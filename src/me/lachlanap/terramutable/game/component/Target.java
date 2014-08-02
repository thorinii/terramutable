package me.lachlanap.terramutable.game.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author lachlan
 */
public class Target extends Component {

    public final Vector2 xy;

    public Target() {
        this.xy = new Vector2();
    }

    public Target(Vector2 xy) {
        this.xy = new Vector2(xy);
    }

    public Target(float x, float y) {
        this.xy = new Vector2(x, y);
    }
}
