package me.lachlanap.terramutable.game;

import com.artemis.Component;

/**
 *
 * @author Lachlan Phillips
 */
public class Chunk extends Component {

    public int cx, cy;

    public Chunk(int cx, int cy) {
        this.cx = cx;
        this.cy = cy;
    }
}
