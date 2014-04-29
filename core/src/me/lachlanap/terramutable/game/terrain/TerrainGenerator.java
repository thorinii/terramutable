package me.lachlanap.terramutable.game.terrain;

import java.util.Random;
import me.lachlanap.lct.Constant;

/**
 *
 * @author Lachlan Phillips
 */
public class TerrainGenerator implements Generator {

    @Constant(name = "Basic Ground Level", constraints = "-100,100")
    public static int BASIC_GROUND_LEVEL = 0;

    @Constant(name = "Caves Shift")
    public static float CAVES_SHIFT = 0f;

    @Constant(name = "Caves Scale", constraints = "0,1000")
    public static float CAVES_SCALE = 76.83f;

    @Constant(name = "Caves Density", constraints = "0,1")
    public static float CAVES_DENSITY = 0.283f;

    private final Random random;

    public TerrainGenerator() {
        this.random = new Random();
    }

    @Override
    public PixelData generate(int cx, int cy) {
        PixelData data = new PixelData();

        int bx = cx * data.getWidth();
        int by = cy * data.getHeight();

        for (int x = 0; x < data.getWidth(); x++) {
            for (int y = 0; y < data.getHeight(); y++) {
                data.set(x, y, binaryDensityFunction(bx + x, by + y));
            }
        }

        return data;
    }

    private boolean binaryDensityFunction(int x, int y) {
        return densityFunction(x, y) > 0.5f;
    }

    private float densityFunction(int x, int y) {
        float heightMap = heightMap(x, y);
        //return caves(x, y);
        return heightMap - caves(x, y);
    }

    private float heightMap(int x, int y) {
        if ((BASIC_GROUND_LEVEL - y) - smoothedNoise(x) > 0.5) {
            return 1;
        } else {
            return 0;
        }
    }

    private float caves(int x, int y) {
        float caves = Math.min(1, Math.max(0,
                                           smoothedNoise2d(x, y) * CAVES_DENSITY));
        float gradient = Math.min(1,
                                  (1 / (CAVES_SCALE * CAVES_SCALE)) * (float) Math.pow(Math.min(0, y - CAVES_SHIFT), 2));
        return caves * gradient;
    }

    private float smoothedNoise(int i) {
        return (multiNoise(i - 1) + multiNoise(i + 1)) / 2f;
    }

    private float multiNoise(int i) {
        return noise(i) / 2f + noise(i / 3) + noise(i / 6) * 2 + noise(i / 12) * 4 + noise(i / 24) * 8;
    }

    private float noise(int i) {
        int x = (i << 13) ^ i;
        float v = 1f - ((x * (x * x * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824f;
        return (v + 1) / 2f;
    }

    private float smoothedNoise2d(int i, int j) {
        return (multiNoise2d(i - 1, j - 1) + multiNoise2d(i + 1, j - 1)
                + multiNoise2d(i - 1, j + 1) + multiNoise2d(i + 1, j + 1)) / 16f;
    }

    private float multiNoise2d(int i, int i2) {
        return noise2d(i, i2) / 2f
               + noise2d(i / 3, i2 / 3)
               + noise2d(i / 6, i2 / 3) * 2
               + noise2d(i / 12, i2 / 3) * 4
               + noise2d(i / 24, i2 / 3) * 8;
    }

    private float noise2d(int i, int j) {
        int n = i + j * 57;
        return noise(n);
    }
}
