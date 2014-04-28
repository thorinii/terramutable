package me.lachlanap.terramutable.game.terrain;

import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;

/**
 *
 * @author Lachlan Phillips
 */
public interface Mesher {

    public static final float PIXEL_SIZE_IN_METRES = 0.1f;
    public static final float CHUNK_SIZE_IN_METRES = PIXEL_SIZE_IN_METRES * PixelData.SIZE_IN_PIXELS;

    public MeshBuilder mesh(PixelData data);

}
