package me.lachlanap.terramutable.game.terrain;

import java.util.BitSet;

/**
 *
 * @author Lachlan Phillips
 */
public class SquelData {

    public static final int SIZE_IN_SQUELS = 16;

    private final BitSet squels;
    private boolean dirty;

    public SquelData() {
        squels = new BitSet(SIZE_IN_SQUELS * SIZE_IN_SQUELS);
    }

    public int getWidth() {
        return SIZE_IN_SQUELS;
    }

    public int getHeight() {
        return SIZE_IN_SQUELS;
    }

    public void set(int x, int y, boolean solid) {
        squels.set(indexOf(x, y), solid);
        dirty = true;
    }

    public boolean get(int x, int y) {
        return squels.get(indexOf(x, y));
    }

    private int indexOf(int x, int y) {
        return x + y * getWidth();
    }

    public boolean isDirty() {
        return dirty;
    }

    public void clearDirty() {
        dirty = false;
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
