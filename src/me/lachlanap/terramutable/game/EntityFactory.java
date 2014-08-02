package me.lachlanap.terramutable.game;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector2;
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
                .addComponent(new MeshView(buildPlayerMesh()))
                .addComponent(new InputStatus());
    }

    public static Entity makeBullet(World world, Entity owner, Vector2 start, Vector2 target) {
        return world.createEntity()
                .addComponent(new Position(start.x, start.y))
                .addComponent(new MeshView(buildBulletMesh()))
                .addComponent(new Target(target));
    }

    private static Mesh buildPlayerMesh() {
        MeshBuilder builder = new MeshBuilder();
        builder.begin(new VertexAttributes(VertexAttribute.Position(), VertexAttribute.Normal()), GL20.GL_TRIANGLES);
        builder.setColor(0, 0, 1, 1);
        builder.rect(new Vector3(-.5f, -.5f, 0), new Vector3(-.5f, .5f, 0),
                     new Vector3(.5f, .5f, 0), new Vector3(.5f, -.5f, 0),
                     new Vector3(0, 0, 1));
        return builder.end();
    }

    private static Mesh buildBulletMesh() {
        MeshBuilder builder = new MeshBuilder();
        builder.begin(new VertexAttributes(VertexAttribute.Position(), VertexAttribute.Normal()), GL20.GL_TRIANGLES);
        builder.setColor(0, 1, 0, 1);
        builder.rect(new Vector3(-.1f, -.1f, 0), new Vector3(-.1f, .1f, 0),
                     new Vector3(.1f, .1f, 0), new Vector3(.1f, -.1f, 0),
                     new Vector3(0, 0, 1));
        return builder.end();
    }
}
