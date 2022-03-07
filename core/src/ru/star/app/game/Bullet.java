package ru.star.app.game;

import com.badlogic.gdx.math.Vector2;
import ru.star.app.game.helpers.Poolable;

import static ru.star.app.screen.ScreenSettings.*;

public class Bullet implements Poolable {
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private int damage;

    public int getDamage() {
        return damage;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Bullet() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.active = false;
    }

    public void activate(float x, float y, float vx, float vy) {
        this.position.x = x;
        this.position.y = y;
        this.velocity.x = vx;
        this.velocity.y = vy;
        active = true;
        damage = 1;
    }

    public void deactivate() {
        active = false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < -20 || position.x > SCREEN_WIDTH + 20 ||
                position.y < -20 || position.y > SCREEN_HEIGHT + 20) {
            deactivate();
        }
    }
}
