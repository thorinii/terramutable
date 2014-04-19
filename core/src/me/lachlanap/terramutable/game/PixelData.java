package me.lachlanap.terramutable.game;

import com.artemis.Component;
import java.util.BitSet;

/**
 *
 * @author Lachlan Phillips
 */
public class PixelData extends Component {

    public static final int SIZE_IN_PIXELS = 16;

    private final BitSet pixels;

    public PixelData() {
        pixels = new BitSet(SIZE_IN_PIXELS * SIZE_IN_PIXELS);
    }

    public int getWidth() {
        return SIZE_IN_PIXELS;
    }

    public int getHeight() {
        return SIZE_IN_PIXELS;
    }

    public void set(int x, int y, boolean solid) {
        pixels.set(indexOf(x, y), solid);
    }

    public boolean get(int x, int y) {
        return pixels.get(indexOf(x, y));
    }

    private int indexOf(int x, int y) {
        return x + y * getWidth();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int y = getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < getWidth(); x++) {
                builder.append(get(x, y) ? '8' : ' ');
            }

            builder.append('\n');
        }

        return builder.toString();
    }

}
