package ru.star.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static ru.star.app.ScreenSettings.SCREEN_HEIGHT;
import static ru.star.app.ScreenSettings.SCREEN_WIDTH;

public class Asteroid {
    private Texture texture;
    private Vector2 position;
    private float angle;

    public Asteroid() {
        texture = new Texture("asteroid.png");
        position = new Vector2(MathUtils.random(0, SCREEN_WIDTH), MathUtils.random(0, SCREEN_HEIGHT));
        angle = MathUtils.random(0, 360);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128,
                256, 256, 1, 1, angle,
                0, 0, 256, 256, false, false);
    }

    public void update(float dt) {
        position.x += MathUtils.cosDeg(angle) * 300 * dt;
        position.y += MathUtils.sinDeg(angle) * 300 * dt;
        checkBorder();
    }

    private void checkBorder() {
        if (position.x < -128) {
            position.x = SCREEN_WIDTH + 128;
            position.y = MathUtils.random(0, SCREEN_HEIGHT);
        } else if (position.x > SCREEN_WIDTH + 128) {
            position.x = -128;
            position.y = MathUtils.random(0, SCREEN_HEIGHT);
        }
        if (position.y < -128) {
            position.x = MathUtils.random(0, SCREEN_WIDTH);
            position.y = SCREEN_HEIGHT + 128;
        } else if (position.y > SCREEN_HEIGHT + 128) {
            position.x = MathUtils.random(0, SCREEN_WIDTH);
            position.y = -128;
        }
    }

}
