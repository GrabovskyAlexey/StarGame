package ru.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.star.app.game.Background;
import ru.star.app.screen.utils.Assets;


public class GameOverScreen extends AbstractScreen {
    private BitmapFont font72;
    private Stage stage;
    private Background bg;
    private StringBuilder sb;

    public void setScore(int score) {
        this.score = score;
    }

    private int score;

    public GameOverScreen(SpriteBatch batch) {
        super(batch);
        this.bg = new Background();
        this.sb = new StringBuilder();
    }

    @Override
    public void show() {
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");

        Gdx.input.setInputProcessor(stage);
    }

    public void update(float dt) {
        stage.act(dt);
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        sb.append("GameOver").append("\n").append("Your score: ").append(score);
        ScreenUtils.clear(0.0f, 0.0f, 0.2f, 1);
        batch.begin();
        bg.render(batch);
        font72.draw(batch, sb, 0, 600, 1280, 1, false);
        sb.setLength(0);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {

    }
}
