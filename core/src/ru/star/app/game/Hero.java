package ru.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.star.app.screen.utils.Assets;

import static ru.star.app.screen.ScreenSettings.SCREEN_HEIGHT;
import static ru.star.app.screen.ScreenSettings.SCREEN_WIDTH;

public class Hero {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float SHIP_SPEED;
    private float fireTimer;
    private int score;
    private int scoreView;
    private int hpMax;
    private int hp;
    private Circle hitArea;

    public int getScoreView() {
        return scoreView;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getHp() {
        return hp;
    }

    public Circle getHitArea() {
        return hitArea;
    }

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
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.SHIP_SPEED = 500.0f;
        this.hpMax = 100;
        this.hp = hpMax;
        this.hitArea = new Circle(position, 31.0f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle);
    }

    public void update(float dt) {
        fireTimer += dt;
        if(scoreView < score){
            scoreView += 1500 *dt;
            if(scoreView > score){
                scoreView = score;
            }
        }

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
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            if (fireTimer > 0.2f) {
                fireTimer = 0.0f;
                float wx = position.x + MathUtils.cosDeg(angle + 90) * 20;
                float wy = position.y + MathUtils.sinDeg(angle + 90) * 20;

                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500 + velocity.x,
                        MathUtils.sinDeg(angle) * 500 + velocity.y
                );

                wx = position.x + MathUtils.cosDeg(angle - 90) * 20;
                wy = position.y + MathUtils.sinDeg(angle - 90) * 20;

                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500 + velocity.x,
                        MathUtils.sinDeg(angle) * 500 + velocity.y
                );
            }
        }
        position.mulAdd(velocity, dt);
        float lessEnginePowerKoef = 1.0f - dt;
        if (lessEnginePowerKoef < 0.0f) {
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
        this.hitArea.setPosition(position);
    }

    public boolean takeDamage(int damage){
        hp -= damage;
        return hp <=0;
    }

}
