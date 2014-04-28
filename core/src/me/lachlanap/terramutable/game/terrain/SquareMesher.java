package me.lachlanap.terramutable.game.terrain;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;

/**
 *
 * @author Lachlan Phillips
 */
public class SquareMesher implements Mesher {

    private final Color colour = Color.RED;
    //private final MeshBuilder builder;

    public SquareMesher() {
        //this.builder = new MeshBuilder();
    }

    @Override
    public MeshBuilder mesh(PixelData data) {
        VertexInfo i00 = new VertexInfo(),
                i01 = new VertexInfo(),
                i10 = new VertexInfo(),
                i11 = new VertexInfo();

        i00.setCol(colour);
        i01.setCol(colour);
        i10.setCol(colour);
        i11.setCol(colour);

        MeshBuilder builder = new MeshBuilder();
        builder.begin(new VertexAttributes(VertexAttribute.Position(), VertexAttribute.Color()), GL20.GL_TRIANGLES);
        builder.ensureVertices(4 * PixelData.SIZE_IN_PIXELS * PixelData.SIZE_IN_PIXELS);

        for (int x = 0; x < data.getWidth(); x++) {
            for (int y = 0; y < data.getHeight(); y++) {
                if (!data.get(x, y)) {
                    continue;
                }

                i00.setPos((x) * Mesher.PIXEL_SIZE_IN_METRES,
                        (y) * Mesher.PIXEL_SIZE_IN_METRES, 0);
                i10.setPos((x + 1) * Mesher.PIXEL_SIZE_IN_METRES,
                        (y) * Mesher.PIXEL_SIZE_IN_METRES, 0);
                i11.setPos((x + 1) * Mesher.PIXEL_SIZE_IN_METRES,
                        (y + 1) * Mesher.PIXEL_SIZE_IN_METRES, 0);
                i01.setPos((x) * Mesher.PIXEL_SIZE_IN_METRES,
                        (y + 1) * Mesher.PIXEL_SIZE_IN_METRES, 0);

                builder.rect(i00, i10, i11, i01);
            }
        }

        return builder;
    }

}
