package me.lachlanap.terramutable;

import com.artemis.World;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import me.lachlanap.lct.LCTManager;
import me.lachlanap.lct.gui.LCTFrame;
import me.lachlanap.terramutable.game.*;
import me.lachlanap.terramutable.game.bus.MessageBus;
import me.lachlanap.terramutable.game.messages.DebugCycleStatsMessage;
import me.lachlanap.terramutable.game.messages.MoveCameraMessage;
import me.lachlanap.terramutable.game.stat.*;
import me.lachlanap.terramutable.game.system.InputCollectingSystem;
import me.lachlanap.terramutable.game.system.RenderingSystem;

public class TerraMutable extends ApplicationAdapter {

    private StatsCollector statsCollector;
    private GeneralStatGatherer generalStatGatherer;

    private AssetManager assetManager;

    private World world;
    private MessageBus messageBus;

    private RenderingSystem renderingSystem;
    private StatsRenderer statsRenderer;

    @Override
    public void create() {
        try {
            statsCollector = new StatsCollector();
            assetManager = new AssetManager();

            world = new World();
            messageBus = new MessageBus();

            renderingSystem = new RenderingSystem(statsCollector);
            world.setSystem(renderingSystem);
            world.setSystem(new InputCollectingSystem(Gdx.input, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

            world.initialize();

            Gdx.input.setInputProcessor(new KeyboardProcessor());

            EntityFactory.makePlayer(world, 0, 0).addToWorld();

            LCTManager lctManager = new LCTManager();
            LCTFrame lctFrame = new LCTFrame(lctManager);
            lctFrame.setVisible(true);

            generalStatGatherer = new GeneralStatGatherer(statsCollector, messageBus);
            setupDebugStats();

            renderingSystem.attachToBus(messageBus);
            statsRenderer.attachToBus(messageBus);
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }

    private void setupDebugStats() {
        statsRenderer = new StatsRenderer(statsCollector);

        statsRenderer.makePlot("FPS", 0, 100, "fps");
        statsRenderer.makeParentPlot("Memory Usage", "memory");
        statsRenderer.makeParentPlot("Systems", "system");
        statsRenderer.makeParentPlot("Entities", "entity");
        statsRenderer.makeParentPlot("Message Bus", "bus");
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
            messageBus.send(new MoveCameraMessage(MoveCameraMessage.Direction.UP));
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            messageBus.send(new MoveCameraMessage(MoveCameraMessage.Direction.DOWN));

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            messageBus.send(new MoveCameraMessage(MoveCameraMessage.Direction.LEFT));
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            messageBus.send(new MoveCameraMessage(MoveCameraMessage.Direction.RIGHT));

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
                messageBus.send(new DebugCycleStatsMessage(DebugCycleStatsMessage.Direction.Left));
            else if (keycode == Input.Keys.H)
                messageBus.send(new DebugCycleStatsMessage(DebugCycleStatsMessage.Direction.Right));
            return true;
        }

        @Override
        public boolean keyTyped(char character) {
            return true;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer,
                                 int button) {
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
