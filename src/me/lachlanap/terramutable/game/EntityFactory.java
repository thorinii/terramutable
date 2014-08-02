package me.lachlanap.terramutable.game;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector3;
import me.lachlanap.terramutable.game.component.*;

/**
 *
 * @author Lachlan Phillips
 */
public class EntityFactory {

    public static Entity makePlayer(World world, float x, float y) {
        return world.createEntity()
                .addComponent(new Position(x, y))
                .addComponent(new MeshView(buildAMesh()))
                .addComponent(new InputStatus());
    }

    private static Mesh buildAMesh() {
        MeshBuilder builder = new MeshBuilder();
        builder.begin(new VertexAttributes(VertexAttribute.Position(), VertexAttribute.Normal()), GL20.GL_TRIANGLES);
        builder.rect(new Vector3(-.5f, -.5f, 0), new Vector3(-.5f, .5f, 0),
                     new Vector3(.5f, .5f, 0), new Vector3(.5f, -.5f, 0),
                     new Vector3(0, 0, 1));
        return builder.end();
    }
}
