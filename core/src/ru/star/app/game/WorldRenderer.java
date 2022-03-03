package ru.star.app.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.star.app.screen.utils.Assets;

public class WorldRenderer {
    private GameController gc;
    private SpriteBatch batch;
    private StringBuilder sb;
    private BitmapFont font32;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");
        this.sb = new StringBuilder();
    }

    public void render() {
        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        gc.getBg().render(batch);
        gc.getBulletController().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getHero().render(batch);
        sb.setLength(0);
        sb.append("SCORE: ").append(gc.getHero().getScoreView());
        font32.draw(batch, sb, 20, 700);
        sb.setLength(0);
        sb.append("HERO HP: ").append(gc.getHero().getHp());
        font32.draw(batch, sb, 20, 52);
        batch.end();
    }
}
