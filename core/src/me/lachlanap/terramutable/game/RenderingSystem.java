package me.lachlanap.terramutable.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


/**
 *
 * @author Lachlan Phillips
 */
public class RenderingSystem extends EntityProcessingSystem {

    public static final float SCREEN_PIXELS_PER_METRE = 50f;

    @Mapper
    ComponentMapper<Position> pm;
    @Mapper
    ComponentMapper<MeshView> mm;

    private final OrthographicCamera camera;
    private final ScreenViewport viewport;

    private final ShaderProgram shader;

    public RenderingSystem() {
        super(Aspect.getAspectForAll(Position.class).one(MeshView.class));

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(width, height);

        viewport = new ScreenViewport(camera);
        viewport.setUnitsPerPixel(1 / SCREEN_PIXELS_PER_METRE);

        shader = new ShaderProgram(Gdx.files.local("shader/base.vert"), Gdx.files.local("shader/base.frag"));
        if (!shader.isCompiled())
            throw new IllegalStateException("Failed to compile shader: " + shader.getLog());
    }

    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    public void translate(float dx, float dy) {
        camera.translate(dx, dy);
    }

    @Override
    protected void begin() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glViewport((int) viewport.getViewportX(), (int) viewport.getViewportY(),
                          (int) viewport.getViewportWidth(), (int) viewport.getViewportHeight());

        viewport.update();

        shader.begin();
        shader.setUniformMatrix("u_worldView", camera.combined);
    }

    @Override
    protected void process(Entity e) {
        Position position = pm.get(e);

        Matrix4 model = new Matrix4().translate(position.x, position.y, 0);

        shader.setUniformMatrix("u_model", model);

        MeshView mesh = mm.get(e);
        mesh.mesh.render(shader, GL20.GL_TRIANGLES);
    }

    @Override
    protected void end() {
        shader.end();
    }
}
