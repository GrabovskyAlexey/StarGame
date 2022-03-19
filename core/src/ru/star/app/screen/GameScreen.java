package ru.star.app.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.star.app.game.GameController;
import ru.star.app.game.WorldRenderer;
import ru.star.app.screen.utils.Assets;

public class GameScreen extends AbstractScreen {
    private SpriteBatch batch;
    private GameController gc;
    private WorldRenderer renderer;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenSettings.ScreenType.GAME);
        gc = new GameController();
        renderer = new WorldRenderer(gc, batch);
    }

    @Override
    public void render(float delta) {
        gc.update(delta);
        renderer.render();
    }
}
