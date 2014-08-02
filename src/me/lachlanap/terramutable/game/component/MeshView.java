package me.lachlanap.terramutable.game.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Mesh;

/**
 *
 * @author Lachlan Phillips
 */
public class MeshView extends Component {

    public Mesh mesh;

    public MeshView(Mesh mesh) {
        this.mesh = mesh;
    }

}
