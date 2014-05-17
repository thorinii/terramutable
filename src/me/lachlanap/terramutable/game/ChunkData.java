package me.lachlanap.terramutable.game;

import me.lachlanap.terramutable.game.terrain.SquelData;
import com.artemis.Component;

/**
 *
 * @author Lachlan Phillips
 */
public class ChunkData extends Component {

    public final SquelData pixelData;

    public ChunkData(SquelData pixelData) {
        this.pixelData = pixelData;
    }

    public ChunkData() {
        this(new SquelData());
    }
}
