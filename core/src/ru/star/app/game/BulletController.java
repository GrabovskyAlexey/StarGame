package ru.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.star.app.game.helpers.ObjectPool;
import ru.star.app.screen.utils.Assets;

public class BulletController extends ObjectPool<Bullet> {
    private TextureRegion bulletTexture;

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    public BulletController() {
        this.bulletTexture = Assets.getInstance().getAtlas().findRegion("bullet");
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
