package ru.star.app;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class StarGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private Background bg;
    private Hero hero;
    private Asteroid asteroid;

    public Hero getHero() {
        return hero;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        bg = new Background(this);
        hero = new Hero();
        asteroid = new Asteroid();
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        bg.render(batch);
        hero.render(batch);
        asteroid.render(batch);
        batch.end();
    }

    public void update(float dt){
        bg.update(dt);
        hero.update(dt);
        asteroid.update(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
