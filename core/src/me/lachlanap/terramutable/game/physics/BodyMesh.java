/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.physics;

import me.lachlanap.terramutable.game.terrain.SquelData;

/**
 *
 * @author lachlan
 */
public class BodyMesh {

    public static BodyMesh makeTriangle(int w, int h, float particleRadius) {
        int fitX = (int) Math.ceil(w / (particleRadius));
        int fitY = (int) Math.ceil(h / (particleRadius));

        BodyMesh mesh = new BodyMesh(fitX, fitY);

        for (int x = 0; x < fitX; x++) {
            for (int y = 0; y < fitY; y++) {
                mesh.data[y * fitY + x] = true;//(x + y <= fitX);
            }
        }

        return mesh;
    }

    private final int width, height;
    private final boolean[] data;

    public BodyMesh(int width, int height) {
        this.width = width;
        this.height = height;

        this.data = new boolean[width * height];
    }

    public BodyMesh(SquelData squelData) {
        this(squelData.getWidth(), squelData.getHeight());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                data[y * height + x] = squelData.get(x, y);
            }
        }
    }

    public int getSize() {
        return width * height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getAt(int x, int y) {
        return data[y * height + x];
    }
}
