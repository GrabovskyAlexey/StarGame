package ru.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.star.app.game.helpers.Poolable;

import static ru.star.app.screen.ScreenSettings.SCREEN_HEIGHT;
import static ru.star.app.screen.ScreenSettings.SCREEN_WIDTH;

public class Asteroid implements Poolable {

    private Vector2 position;
    private float angle;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public Asteroid() {
        position = new Vector2();
        angle = MathUtils.random(0, 360);
    }

    public void activate(float x, float y, float angle){
        this.position.set(x, y);
        this.angle = angle;
        this.active = true;
    }

    public void deactivate(){
        active = false;
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
