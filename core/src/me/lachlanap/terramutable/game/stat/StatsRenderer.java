/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.stat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import me.lachlanap.terramutable.game.TextRenderer;
import me.lachlanap.terramutable.game.bus.Message;
import me.lachlanap.terramutable.game.bus.MessageBus;
import me.lachlanap.terramutable.game.bus.MessageBusListener;
import me.lachlanap.terramutable.game.messages.DebugCycleStatsMessage;

/**
 *
 * @author lachlan
 */
public class StatsRenderer {

    private static final Color[] colours;

    static {
        int number = 6;
        colours = new Color[number];
        for (int i = 0; i < number; i++) {
            float hue = i / ((float) number + 0.99f);
            float saturation = 1;
            float value = 1;

            int h = (int) (hue * 6);
            float f = hue * 6 - h;
            float p = value * (1 - saturation);
            float q = value * (1 - f * saturation);
            float t = value * (1 - (1 - f) * saturation);

            Color colour;
            switch (h) {
                case 0:
                    colour = new Color(value, t, p, 1f);
                    break;
                case 1:
                    colour = new Color(q, value, p, 1f);
                    break;
                case 2:
                    colour = new Color(p, value, t, 1f);
                    break;
                case 3:
                    colour = new Color(p, q, value, 1f);
                    break;
                case 4:
                    colour = new Color(t, p, value, 1f);
                    break;
                case 5:
                    colour = new Color(value, p, q, 1f);
                    break;
                default:
                    throw new RuntimeException(
                            "Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
            }
            colours[i] = colour;
        }
    }

    private final StatsCollector collector;

    private final TextRenderer textRenderer;
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;
    private final List<AggregatePlotRenderer> renderers;

    private int current;

    public StatsRenderer(StatsCollector collector) {
        this.collector = collector;

        textRenderer = new TextRenderer();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        renderers = new ArrayList<>();

        current = -1;
    }

    public void render() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        if (current >= 0)
            renderers.get(current).render(batch, shapeRenderer, width, height);
    }

    public void attachToBus(MessageBus messageBus) {
        messageBus.watchFor(new MessageBusListener() {
            @Override
            public void receive(Message message) {
                switch (((DebugCycleStatsMessage) message).direction) {
                    case Left:
                        cycleLeft();
                        break;
                    case Right:
                        cycleRight();
                        break;
                }
            }
        }, DebugCycleStatsMessage.class);
    }

    private void cycleLeft() {
        current--;
        if (current < -1)
            current = renderers.size() - 1;
    }

    private void cycleRight() {
        current++;
        if (current >= renderers.size())
            current = -1;
    }

    public void makePlot(String plotName, float min, float max, String... stats) {
        List<PlotRenderer> plotRenderers = new ArrayList<>();
        for (int i = 0; i < stats.length; i++) {
            String stat = stats[i];
            PlotRenderer renderer = new PlotRenderer(collector.get(stat),
                                                     colours[i], min, max);
            if (stats.length > 1)
                renderer.setRenderMinMax(false);
            plotRenderers.add(renderer);
        }

        renderers.add(new AggregatePlotRenderer(textRenderer, plotName,
                                                plotRenderers));
    }

    public void makeParentPlot(String plotName, float min, float max,
                               String parentName) {
        List<StatBuffer> buffers = collector.getSub(parentName);
        List<PlotRenderer> plotRenderers = new ArrayList<>();
        for (int i = 0; i < buffers.size(); i++) {
            StatBuffer buffer = buffers.get(i);
            PlotRenderer renderer = new PlotRenderer(buffer, colours[i], min,
                                                     max);
            renderer.setRenderMinMax(false);
            plotRenderers.add(renderer);
        }

        renderers.add(new AggregatePlotRenderer(textRenderer, plotName,
                                                plotRenderers));
    }

    public void makeParentPlot(String plotName, String parentName) {
        List<StatBuffer> buffers = collector.getSub(parentName);
        List<PlotRenderer> plotRenderers = new ArrayList<>();
        for (int i = 0; i < buffers.size(); i++) {
            StatBuffer buffer = buffers.get(i);
            PlotRenderer renderer = new PlotRenderer(buffer, colours[i], 0, 0);
            renderer.setRenderMinMax(false);
            plotRenderers.add(renderer);
        }

        renderers.add(new AggregatePlotRenderer(textRenderer, plotName,
                                                plotRenderers));
    }

    private static class AggregatePlotRenderer {

        final TextRenderer textRenderer;
        final String plotName;
        final List<PlotRenderer> renderers;
        final NumberFormat formatter;

        public AggregatePlotRenderer(TextRenderer textRenderer, String plotName,
                                     List<PlotRenderer> renderers) {
            this.textRenderer = textRenderer;
            this.plotName = plotName;
            this.renderers = renderers;

            formatter = NumberFormat.getIntegerInstance();
            formatter.setMaximumFractionDigits(3);
            formatter.setRoundingMode(RoundingMode.CEILING);
        }

        public void render(SpriteBatch batch, ShapeRenderer shapeRenderer,
                           int width, int height) {
            batch.begin();
            textRenderer.render(batch, plotName, 5, height - 5);
            batch.end();

            float total = 0;
            for (int i = 0; i < renderers.size(); i++) {
                PlotRenderer renderer = renderers.get(i);
                total += renderer.getCurrent();

                batch.begin();
                textRenderer.render(batch, renderer.getName() + ": " + formatter
                                    .format(renderer.getCurrent()),
                                    5, height - 20 - 15 * i, colours[i]);
                batch.end();

                renderer.render(shapeRenderer, width, height);
            }

            if (renderers.size() > 1) {
                batch.begin();
                textRenderer.render(batch, "total: " + formatter.format(total),
                                    5, height - 20 - 15 * renderers.size());
                batch.end();
            }
        }
    }
}
