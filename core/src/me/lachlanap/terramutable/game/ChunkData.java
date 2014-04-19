package me.lachlanap.terramutable.game;

import me.lachlanap.terramutable.game.terrain.PixelData;
import com.artemis.Component;

/**
 *
 * @author Lachlan Phillips
 */
public class ChunkData extends Component {

    public final PixelData pixelData;

    public ChunkData(PixelData pixelData) {
        this.pixelData = pixelData;
    }

    public ChunkData() {
        this(new PixelData());
    }
}
