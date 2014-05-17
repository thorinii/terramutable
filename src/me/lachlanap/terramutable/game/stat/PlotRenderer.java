/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.stat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author lachlan
 */
public class PlotRenderer {

    private final StatBuffer stat;
    private final Color colour;
    private final float lowerBound, upperBound;
    private final float[] buffer;
    private boolean renderMinMax;

    public PlotRenderer(StatBuffer stat, Color colour, float lowerBound, float upperBound) {
        this.stat = stat;

        this.colour = colour;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

        buffer = new float[stat.size()];

        renderMinMax = true;
    }

    public String getName() {
        return stat.getName();
    }

    public float getCurrent() {
        return stat.getLatest();
    }

    public void setRenderMinMax(boolean renderMinMax) {
        this.renderMinMax = renderMinMax;
    }

    public void render(ShapeRenderer shapeRenderer, int width, int height) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        renderPlot(shapeRenderer, width, height);

        if (renderMinMax)
            renderMinMax(shapeRenderer, width, height);

        shapeRenderer.end();
    }

    private void renderPlot(ShapeRenderer shapeRenderer, int width, int height) {
        stat.copyTo(buffer);

        float xinc = (float) width / buffer.length;
        for (int i = 1; i < buffer.length; i++) {
            float val1 = scale(buffer[i - 1], height);
            float val2 = scale(buffer[i], height);

            shapeRenderer.line(xinc * i, val1,
                               xinc * i + xinc, val2,
                               colour, colour);
        }
    }

    private void renderMinMax(ShapeRenderer shapeRenderer, int width, int height) {
        renderHLine(shapeRenderer,
                    width, scale(stat.getMax(), height),
                    colour);
        renderHLine(shapeRenderer,
                    width, scale(stat.getMin(), height),
                    colour);
    }

    private void renderHLine(ShapeRenderer shapeRenderer, int width, float y, Color colour) {
        shapeRenderer.line(0, y,
                           width, y,
                           colour, colour);
    }

    private float scale(float in, int height) {
        if (lowerBound == upperBound) {
            return in / stat.getMax() * height / 1.1f;
        } else {
            float range = upperBound - lowerBound;
            return (in - lowerBound) / range * height;
        }
    }
}
