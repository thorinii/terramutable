package me.lachlanap.terramutable.game;

import com.artemis.Component;

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
}
