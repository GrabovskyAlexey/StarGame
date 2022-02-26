package ru.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.star.app.game.helpers.ObjectPool;

public class AsteroidController extends ObjectPool<Asteroid> {
    private Texture texture;

    public AsteroidController() {
        texture = new Texture("asteroid.png");
    }

    @Override
    protected Asteroid newObject() {
        return new Asteroid();
    }

    public void render(SpriteBatch batch) {
        for (Asteroid a : activeList){
            batch.draw(texture, a.getPosition().x - 128, a.getPosition().y - 128, 128, 128,
                    256, 256, 1, 1, a.getAngle(),
                    0, 0, 256, 256, false, false);
        }        
    }

    public void update(float dt) {
        for (Asteroid a : activeList){
            a.update(dt);
        }
        checkPool();
    }

    public void setup(float x, float y, float angle) {
        getActiveElement().activate(x, y, angle);
    }
}
