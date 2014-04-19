package me.lachlanap.terramutable.game.terrain;

import me.lachlanap.terramutable.game.PixelData;

/**
 *
 * @author Lachlan Phillips
 */
public interface Generator {

    public PixelData generate(int x, int y);
}
