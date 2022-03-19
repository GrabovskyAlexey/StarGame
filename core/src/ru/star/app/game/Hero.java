package ru.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.star.app.screen.utils.Assets;

import static ru.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static ru.star.app.screen.ScreenManager.SCREEN_WIDTH;

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
    private StringBuilder sb;
    private Weapon weapon;
    private int coins;

    public void addScore(int score) {
        this.score += score;
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

    public int getScore() {
        return score;
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
        this.hitArea = new Circle(position, 28.0f);
        this.sb = new StringBuilder();
        this.weapon = new Weapon(gc.getBulletController(), Weapon.WeaponType.TRIPLE);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle);
    }

    public void pickupPowerUps(PowerUps powerUps){
        switch(powerUps.getType()){
            case MEDKIT:
                increaseHP(10);
                break;
            case AMMOS:
                weapon.increaseAmmo(20);
                break;
            case MONEY:
                increaseCoin(100);
                break;
        }
        powerUps.deactivate();
    }

    private void weaponUpgrade() {
        int weaponType = weapon.getType().ordinal() + 1;
        if(weaponType < Weapon.WeaponType.values().length){
            weapon.upgradeWeapon(Weapon.WeaponType.values()[weaponType]);
        } else {
            weapon.increaseAmmo(weapon.getMaxBullets() - weapon.getCurrBullets());
        }
    }

    private void increaseHP(int amount) {
        hp += amount;
        if(hp>hpMax) {
            hp = hpMax;
        }
    }

    private void increaseCoin(int amount) {
        coins += amount;
    }

    public void update(float dt) {
        fireTimer += dt;
        updateScore(dt);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180 * dt;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angle) * SHIP_SPEED * dt;
            velocity.y += MathUtils.sinDeg(angle) * SHIP_SPEED * dt;

            float bx = position.x + MathUtils.cosDeg(angle + 180) * 25;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 25;
            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.1f + MathUtils.random(-10, 10), velocity.y * -0.1f + MathUtils.random(-10, 10),
                        0.3f,
                        1.2f, 0.2f,
                        1.0f, 0.5f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 0.0f);
            }

        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x -= MathUtils.cosDeg(angle) * (SHIP_SPEED / 2) * dt;
            velocity.y -= MathUtils.sinDeg(angle) * (SHIP_SPEED / 2) * dt;

            float bx = position.x + MathUtils.cosDeg(angle + 90) * 25;
            float by = position.y + MathUtils.sinDeg(angle + 90) * 25;
            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-10, 10), velocity.y * 0.1f + MathUtils.random(-10, 10),
                        0.2f,
                        1.2f, 0.2f,
                        1.0f, 0.5f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 0.0f);
            }
            bx = position.x + MathUtils.cosDeg(angle - 90) * 25;
            by = position.y + MathUtils.sinDeg(angle - 90) * 25;
            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * 0.1f + MathUtils.random(-10, 10), velocity.y * 0.1f + MathUtils.random(-10, 10),
                        0.2f,
                        1.2f, 0.2f,
                        1.0f, 0.5f, 0.0f, 1.0f,
                        1.0f, 1.0f, 0.0f, 0.0f);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            tryToFire();
        }
        position.mulAdd(velocity, dt);
        float lessEnginePowerKoef = 1.0f - dt;
        if (lessEnginePowerKoef < 0.0f) {
            lessEnginePowerKoef = 0.0f;
        }

        velocity.scl(lessEnginePowerKoef);
        checkBorder();

    }

    private void updateScore(float dt) {
        if (scoreView < score) {
            scoreView += 1500 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

    private void tryToFire() {
        if (fireTimer > weapon.getFirePeriod()) {
            fireTimer = 0.0f;
            weapon.fire(position, velocity, angle);
        }
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font32) {
        sb.setLength(0);
        sb.append("SCORE: ").append(scoreView).append("\n")
                .append("HP: ").append(hp).append("/").append(hpMax).append("\n")
                .append("AMMO: ").append(weapon.getCurrBullets()).append("/").append(weapon.getMaxBullets()).append("\n")
                .append("COINS: ").append(coins);
        font32.draw(batch, sb, 20, 700);
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

    public boolean takeDamage(int damage) {
        hp -= damage;
        return hp <= 0;
    }

}
