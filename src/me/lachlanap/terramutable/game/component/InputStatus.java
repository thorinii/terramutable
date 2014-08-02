package me.lachlanap.terramutable.game.component;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class InputStatus extends Component {

    public boolean shooting;

    public final Vector2 target = new Vector2();

    public float reloadTimer;

    @Override
    public String toString() {
        return "[shooting: " + shooting + "; target: " + target + "]";
    }
}
