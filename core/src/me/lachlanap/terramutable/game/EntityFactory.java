package me.lachlanap.terramutable.game;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.math.Vector3;
import me.lachlanap.terramutable.game.terrain.Mesher;

/**
 *
 * @author Lachlan Phillips
 */
public class EntityFactory {

    public static Entity makeEntityA(World world, float x, float y) {
        Entity e = world.createEntity();

        e.addComponent(new Position(x, y));
        e.addComponent(new MeshView(buildAMesh()));

        return e;
    }

    public static Entity makeEntityB(World world, float x, float y) {
        Entity e = world.createEntity();

        e.addComponent(new Position(x, y));
        e.addComponent(new MeshView(buildBMesh()));

        return e;
    }

    public static Entity makeChunk(World world, PixelData pixelData, int chunkX, int chunkY) {
        Entity e = world.createEntity();

        e.addComponent(new Position(chunkX * PixelData.SIZE_IN_PIXELS * Mesher.PIXEL_SIZE_IN_METRES,
                                    chunkY * PixelData.SIZE_IN_PIXELS * Mesher.PIXEL_SIZE_IN_METRES));
        e.addComponent(pixelData);

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

    private static Mesh buildBMesh() {
        MeshBuilder builder = new MeshBuilder();
        builder.begin(new VertexAttributes(VertexAttribute.Position(), VertexAttribute.Color()), GL20.GL_TRIANGLES);
        builder.triangle(new Vector3(0, 2, 0), Color.GREEN,
                         new Vector3(2, 0, 0), Color.YELLOW,
                         new Vector3(2, 2, 0), Color.CYAN);
        return builder.end();
    }
}
