package me.lachlanap.terramutable;

import com.artemis.World;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import me.lachlanap.terramutable.game.*;
import me.lachlanap.terramutable.game.terrain.SquareMesher;
import me.lachlanap.terramutable.game.terrain.TerrainGenerator;

public class TerraMutable extends ApplicationAdapter {

    private AssetManager assetManager;

    private World world;

    private RenderingSystem renderingSystem;

    private TerrainGenerator terrainGenerator;

    @Override
    public void create() {
        assetManager = new AssetManager();

        renderingSystem = new RenderingSystem();

        world = new World();

        world.setSystem(new PixelDataUpdateSystem());

        world.setSystem(new MeshRefreshingSystem());
        world.setSystem(new MeshingSystem(new SquareMesher()));

        world.setSystem(renderingSystem);

        world.initialize();

        terrainGenerator = new TerrainGenerator();

        EntityFactory.makeEntityA(world, 0, 0).addToWorld();

        PixelData pixelData = terrainGenerator.generate(0, 0);
        EntityFactory.makeChunk(world, pixelData, 0, 0).addToWorld();
    }

    @Override
    public void resize(int width, int height) {
        renderingSystem.resize(width, height);
    }

    @Override
    public void render() {
        world.setDelta(Gdx.graphics.getDeltaTime());
        world.process();

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
}
