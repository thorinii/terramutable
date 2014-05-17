package me.lachlanap.terramutable.game;

import com.artemis.Component;
import me.lachlanap.terramutable.game.physics.BodyMesh;

/**
 *
 * @author Lachlan Phillips
 */
public class PhysicsBody extends Component {

    public BodyMesh mesh;
    public boolean fixed;

    public int bodyId;

    public PhysicsBody(BodyMesh mesh, boolean fixed) {
        this.mesh = mesh;
        this.fixed = fixed;
    }

}
