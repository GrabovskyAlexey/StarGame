package ru.star.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static ru.star.app.ScreenSettings.*;

public class Hero {

    private Texture texture;
    private Vector2 position;
    private Vector2 lastDisplacement;
    private float angle = 0.0f;
    private final int SHIP_SPEED = 500;

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getLastDisplacement() {
        return lastDisplacement;
    }

    public float getAngle() {
        return angle;
    }

    public Hero() {
        texture = new Texture("ship.png");
        position = new Vector2(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        lastDisplacement = new Vector2(0, 0);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle,
                0, 0, 64, 64, false, false);
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180 * dt;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.x += MathUtils.cosDeg(angle) * SHIP_SPEED * dt;
            position.y += MathUtils.sinDeg(angle) * SHIP_SPEED * dt;
            lastDisplacement.set(MathUtils.cosDeg(angle) * SHIP_SPEED * dt,
                    MathUtils.sinDeg(angle) * SHIP_SPEED * dt);
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.x -= MathUtils.cosDeg(angle) * (SHIP_SPEED / 2) * dt;
            position.y -= MathUtils.sinDeg(angle) * (SHIP_SPEED / 2) * dt;
            lastDisplacement.set(MathUtils.cosDeg(angle) * (SHIP_SPEED / 2) * dt * -1,
                    MathUtils.sinDeg(angle) * (SHIP_SPEED / 2) * dt * -1);
        } else {
            lastDisplacement.set(0, 0);
        }
        checkBorder();
    }

    private void checkBorder() {
        if (position.x < 32) {
            position.x = 32;
        } else if (position.x > SCREEN_WIDTH - 32) {
            position.x = SCREEN_WIDTH - 32;
        }
        if (position.y < 32) {
            position.y = 32;
        } else if (position.y > SCREEN_HEIGHT - 32) {
            position.y = SCREEN_HEIGHT - 32;
        }
    }

}
