package me.lachlanap.terramutable;

import com.artemis.World;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import me.lachlanap.lct.LCTManager;
import me.lachlanap.lct.gui.LCTFrame;
import me.lachlanap.terramutable.game.*;
import me.lachlanap.terramutable.game.stat.*;
import me.lachlanap.terramutable.game.terrain.SquareMesher;
import me.lachlanap.terramutable.game.terrain.TerrainGenerator;

public class TerraMutable extends ApplicationAdapter {

    private StatsCollector statsCollector;
    private GeneralStatGatherer generalStatGatherer;

    private AssetManager assetManager;

    private World world;

    private RenderingSystem renderingSystem;
    private StatsRenderer statsRenderer;

    @Override
    public void create() {
        statsCollector = new StatsCollector();
        generalStatGatherer = new GeneralStatGatherer(statsCollector);

        assetManager = new AssetManager();

        renderingSystem = new RenderingSystem(statsCollector);

        world = new World();

        world.setSystem(new ChunkPagingSystem(statsCollector, renderingSystem));
        world.setSystem(new MeshRefreshingSystem(statsCollector));

        //world.setSystem(new PixelDataUpdateSystem());
        world.setSystem(new ChunkGeneratorSystem(statsCollector, new TerrainGenerator()));

        world.setSystem(new MeshingSystem(statsCollector, new SquareMesher()));

        world.setSystem(renderingSystem);

        world.initialize();

        EntityFactory.makeEntityA(world, 0, 0).addToWorld();

        LCTManager lCTManager = new LCTManager();
        lCTManager.register(TerrainGenerator.class);
        LCTFrame lCTFrame = new LCTFrame(lCTManager);
        lCTFrame.setVisible(true);

        Gdx.input.setInputProcessor(new KeyboardProcessor());
        setupDebugStats();
    }

    private void setupDebugStats() {
        statsRenderer = new StatsRenderer(statsCollector);
        statsRenderer.makePlot("FPS", 0, 100, "fps");
        statsRenderer.makeParentPlot("Memory Usage", 0, 1000, "memory");

        statsRenderer.makeParentPlot("Systems", 0, 0.05f, "system");

        statsRenderer.makeParentPlot("Entities", "entity");
    }

    @Override
    public void resize(int width, int height) {
        renderingSystem.resize(width, height);
    }

    @Override
    public void render() {
        try {
            doRender();
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }

    private void doRender() {
        float delta = Gdx.graphics.getDeltaTime();

        world.setDelta(delta);
        world.process();

        generalStatGatherer.update(world, delta);
        statsRenderer.render();

        assetManager.update();
        update();
    }

    private void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            renderingSystem.translate(0, 5 * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            renderingSystem.translate(0, -5 * Gdx.graphics.getDeltaTime());

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            renderingSystem.translate(-5 * Gdx.graphics.getDeltaTime(), 0);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            renderingSystem.translate(5 * Gdx.graphics.getDeltaTime(), 0);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
        }
    }

    @Override
    public void dispose() {
        world.dispose();
        assetManager.dispose();
    }

    private class KeyboardProcessor implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            if (keycode == Input.Keys.G)
                statsRenderer.cycleLeft();
            if (keycode == Input.Keys.H)
                statsRenderer.cycleRight();
            return true;
        }

        @Override
        public boolean keyTyped(char character) {
            return true;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return true;
        }

        @Override
        public boolean scrolled(int amount) {
            return true;
        }

    }
}
