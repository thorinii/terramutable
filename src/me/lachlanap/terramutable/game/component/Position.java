package me.lachlanap.terramutable.game.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Lachlan Phillips
 */
public class Position extends Component {

    public float x, y, angle;

    public Position() {
        this(0, 0);
    }

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 toVector() {
        return new Vector2(x, y);
    }
}
