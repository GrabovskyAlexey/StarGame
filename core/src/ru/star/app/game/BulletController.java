package ru.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import ru.star.app.game.helpers.ObjectPool;
import ru.star.app.screen.utils.Assets;

public class BulletController extends ObjectPool<Bullet> {
    private TextureRegion bulletTexture;
    private GameController gc;

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    public BulletController(GameController gc) {
        this.bulletTexture = Assets.getInstance().getAtlas().findRegion("bullet");
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size() ; i++) {
            Bullet bullet = activeList.get(i);
            batch.draw(bulletTexture,
                    bullet.getPosition().x - 16,
                    bullet.getPosition().y - 16);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size() ; i++) {
            Bullet bullet = activeList.get(i);
            bullet.update(dt);
            float bx = bullet.getPosition().x;
            float by = bullet.getPosition().y;
            for (int j = 0; j < 3; j++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        bullet.getVelocity().x * 0.1f + MathUtils.random(-10, 10), bullet.getVelocity().y * 0.1f + MathUtils.random(-10, 10),
                        0.15f,
                        1.2f, 0.2f,
                        1.0f, 0.0f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 0.0f);
            }
        }
        checkPool();
    }

    public void setup(float x, float y, float vx, float vy) {
        getActiveElement().activate(x, y, vx, vy);
    }
}
