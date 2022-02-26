package ru.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static ru.star.app.screen.ScreenSettings.*;

public class Hero {
    private GameController gc;
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float SHIP_SPEED;
    private float fireTimer;

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getAngle() {
        return angle;
    }

    public Hero(GameController gc) {
        this.gc = gc;
        texture = new Texture("ship.png");
        position = new Vector2(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        velocity = new Vector2(0, 0);
        angle = 0.0f;
        SHIP_SPEED = 500.0f;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle,
                0, 0, 64, 64, false, false);
    }

    public void update(float dt) {
        fireTimer += dt;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180 * dt;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angle) * SHIP_SPEED * dt;
            velocity.y += MathUtils.sinDeg(angle) * SHIP_SPEED * dt;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x -= MathUtils.cosDeg(angle) * (SHIP_SPEED / 2) * dt;
            velocity.y -= MathUtils.sinDeg(angle) * (SHIP_SPEED / 2) * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            if(fireTimer > 0.2f){
                fireTimer = 0.0f;
                gc.getBulletController().setup(position.x, position.y,
                        MathUtils.cosDeg(angle) * 500 + velocity.x,
                        MathUtils.sinDeg(angle) * 500 + velocity.y
                );
            }
        }
        position.mulAdd(velocity, dt);
        float lessEnginePowerKoef = 1.0f - dt;
        if(lessEnginePowerKoef < 0.0f){
            lessEnginePowerKoef = 0.0f;
        }

        velocity.scl(lessEnginePowerKoef);
        checkBorder();
    }

    private void checkBorder() {
        if (position.x < 32) {
            position.x = 32;
            velocity.x *= -0.5f;
        } else if (position.x > SCREEN_WIDTH - 32) {
            position.x = SCREEN_WIDTH - 32;
            velocity.x *= -0.5f;
        }
        if (position.y < 32) {
            position.y = 32;
            velocity.y *= -0.5f;
        } else if (position.y > SCREEN_HEIGHT - 32) {
            position.y = SCREEN_HEIGHT - 32;
            velocity.y *= -0.5f;
        }
    }

}
