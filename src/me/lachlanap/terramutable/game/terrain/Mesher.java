package me.lachlanap.terramutable.game.terrain;

import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;

/**
 *
 * @author Lachlan Phillips
 */
public interface Mesher {

    public static final float SQUEL_SIZE_IN_METRES = 0.1f;
    public static final float CHUNK_SIZE_IN_METRES = SQUEL_SIZE_IN_METRES * SquelData.SIZE_IN_SQUELS;

    public MeshBuilder mesh(SquelData data);

}
