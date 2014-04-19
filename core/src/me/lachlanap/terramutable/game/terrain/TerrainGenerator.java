package me.lachlanap.terramutable.game.terrain;

import me.lachlanap.terramutable.game.PixelData;

/**
 *
 * @author Lachlan Phillips
 */
public class TerrainGenerator {

    public PixelData generate(int x, int y) {
        PixelData data = new PixelData();

        for (int i = 0; i < data.getWidth(); i++)
            data.set(i, 0, true);

        for (int i = 1; i < data.getWidth() - 1; i++)
            data.set(i, 1, true);

        for (int i = 2; i < data.getWidth() - 2; i++)
            data.set(i, 2, true);

        return data;
    }

}
