package me.lachlanap.terramutable.game;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import me.lachlanap.terramutable.game.bus.Message;
import me.lachlanap.terramutable.game.bus.MessageBus;
import me.lachlanap.terramutable.game.bus.MessageBusListener;
import me.lachlanap.terramutable.game.messages.MoveCameraMessage;
import me.lachlanap.terramutable.game.stat.StatsCollector;

/**
 *
 * @author Lachlan Phillips
 */
public class RenderingSystem extends AbstractTimedSystem {

    public static final float SCREEN_PIXELS_PER_METRE = 50f;

    @Mapper
    ComponentMapper<Position> pm;
    @Mapper
    ComponentMapper<MeshView> mm;

    private final OrthographicCamera camera;
    private final ScreenViewport viewport;

    private final ShaderProgram shader;

    public RenderingSystem(StatsCollector collector) {
        super(collector, Aspect.getAspectForAll(Position.class).one(MeshView.class));

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

    public void attachToBus(MessageBus bus) {
        bus.watchFor(new MessageBusListener() {

            @Override
            public void receive(Message message) {
                switch (((MoveCameraMessage) message).direction) {
                    case UP:
                        translate(0, 5 * Gdx.graphics.getDeltaTime());
                        break;
                    case DOWN:
                        translate(0, -5 * Gdx.graphics.getDeltaTime());
                        break;
                    case LEFT:
                        translate(-5 * Gdx.graphics.getDeltaTime(), 0);
                        break;
                    case RIGHT:
                        translate(5 * Gdx.graphics.getDeltaTime(), 0);
                        break;
                }
            }
        }, MoveCameraMessage.class);
    }

    public Rectangle getViewportRectangle() {
        Rectangle view = new Rectangle(0, 0,
                                       viewport.getViewportWidth(), viewport.getViewportHeight());
        view.x = camera.position.x * SCREEN_PIXELS_PER_METRE - view.width / 2;
        view.y = camera.position.y * SCREEN_PIXELS_PER_METRE - view.height / 2;
        return view;
    }

    @Override
    protected void begin() {
        super.begin();

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
        super.end();
        shader.end();
    }
}
