package ru.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.star.app.game.helpers.ObjectPool;

public class BulletController extends ObjectPool<Bullet> {
    private Texture bulletTexture;

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    public BulletController() {
        this.bulletTexture = new Texture("bullet.png");
    }

    public void render(SpriteBatch batch) {
        for (Bullet b : activeList) {
            batch.draw(bulletTexture,
                    b.getPosition().x - 16,
                    b.getPosition().y - 16);
        }
    }

    public void update(float dt) {
        for (Bullet b : activeList) {
            b.update(dt);
        }
        checkPool();
    }

    public void setup(float x, float y, float vx, float vy) {
        getActiveElement().activate(x, y, vx, vy);
    }
}
