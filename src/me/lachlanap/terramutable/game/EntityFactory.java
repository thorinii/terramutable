package me.lachlanap.terramutable.game;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector3;
import me.lachlanap.terramutable.game.physics.BodyMesh;

/**
 *
 * @author Lachlan Phillips
 */
public class EntityFactory {

    public static Entity makeEntityA(World world, float x, float y) {
        Entity e = world.createEntity();

        e.addComponent(new Position(x, y));
        e.addComponent(new MeshView(buildAMesh()));
        e.addComponent(new PhysicsBody(buildAPhysicsMesh(), false));

        return e;
    }

    public static Entity makeChunk(World world, int chunkX, int chunkY) {
        Entity e = world.createEntity();

        e.addComponent(new Chunk(chunkX, chunkY));

        return e;
    }

    private static Mesh buildAMesh() {
        MeshBuilder builder = new MeshBuilder();
        builder.begin(new VertexAttributes(VertexAttribute.Position(), VertexAttribute.Color()), GL20.GL_TRIANGLES);
        builder.triangle(new Vector3(0, 0, 0), Color.RED,
                         new Vector3(2, 0, 0), Color.GREEN,
                         new Vector3(0, 2, 0), Color.BLUE);
        return builder.end();
    }

    private static BodyMesh buildAPhysicsMesh() {
        return BodyMesh.makeTriangle(2, 2, 0.1f);
    }
}
