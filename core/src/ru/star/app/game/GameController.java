package ru.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.star.app.screen.ScreenManager;

import static ru.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static ru.star.app.screen.ScreenManager.SCREEN_WIDTH;
import static ru.star.app.screen.ScreenManager.ScreenType.GAME_OVER;

public class GameController {
    private Background bg;
    private Hero hero;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private Vector2 tempVector;
    private boolean pause;
    private Stage stage;
    private int level;

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public Background getBg() {
        return bg;
    }

    public Hero getHero() {
        return hero;
    }

    public Stage getStage() {
        return stage;
    }

    public GameController(SpriteBatch batch) {
        this.bg = new Background(this);
        this.bulletController = new BulletController(this);
        this.asteroidController = new AsteroidController();
        this.powerUpsController = new PowerUpsController();
        this.hero = new Hero(this);
        this.tempVector = new Vector2();
        this.particleController = new ParticleController();
        this.pause = false;
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        this.level = 0;
        Gdx.input.setInputProcessor(stage);
    }

    public int getLevel() {
        return level;
    }

    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pause = !pause;
            hero.getShop().setVisible(pause);
        }
        if (!hero.getShop().isVisible() && pause) {
            pause = false;
        }

        stage.act(dt);
        if (!pause) {
            bg.update(dt);
            bulletController.update(dt);
            particleController.update(dt);
            powerUpsController.update(dt);
            if (asteroidController.getActiveList().size() == 0) {
                level++;
                createAsteroids(level);
            }
            asteroidController.update(dt);
            hero.update(dt);
        }
        checkMagnetic();
        checkCollision();
        checkShipCollision();
        checkPickPowerUps();
    }

    public void createAsteroids(int count) {
        for (int i = 0; i < count; i++) {
            asteroidController.setup(MathUtils.random(0, SCREEN_WIDTH),
                    MathUtils.random(0, SCREEN_HEIGHT),
                    MathUtils.random(-150, 150), MathUtils.random(-150, 150), 1, level);
        }
    }

    private void checkPickPowerUps() {
        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUps powerUps = powerUpsController.getActiveList().get(i);
            if (hero.getHitArea().overlaps(powerUps.getHitArea())) {
                hero.pickupPowerUps(powerUps);
                particleController.getEffectBuilder().takePowerUpsEffect(powerUps);
            }
        }
    }

    private void checkShipCollision() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid asteroid = asteroidController.getActiveList().get(i);
            if (hero.getHitArea().overlaps(asteroid.getHitArea())) {
                float dst = asteroid.getPosition().dst(hero.getPosition());
                float halfOverlaps = (asteroid.getHitArea().radius + hero.getHitArea().radius - dst) / 2.0f;
                tempVector.set(hero.getPosition()).sub(asteroid.getPosition()).nor();
                hero.getPosition().mulAdd(tempVector, halfOverlaps);
                asteroid.getPosition().mulAdd(tempVector, -halfOverlaps);
                float sumScale = hero.getHitArea().radius * 2 + asteroid.getHitArea().radius;
                hero.getVelocity().mulAdd(tempVector,
                        200 * asteroid.getHitArea().radius / sumScale);
                asteroid.getVelocity().mulAdd(tempVector,
                        -200 * hero.getHitArea().radius / sumScale);
                if (asteroid.takeDamage(2)) {
                    powerUpsController.setup(asteroid.getPosition().x, asteroid.getPosition().y, 0.1f);
                    float scale = asteroid.getScale() - 0.3f;
                    if (scale >= 0.39) {
                        asteroidController.breakAsteroid(asteroid.getPosition().x, asteroid.getPosition().y, scale, level);
                    }
                    hero.addScore(asteroid.getHpMax() * 50);
                }
                if (hero.takeDamage(asteroid.getDamage())) {
                    ScreenManager.getInstance().changeScreen(GAME_OVER, hero);
                }
            }
        }
    }

    private void checkMagnetic() {
        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUps powerUps = powerUpsController.getActiveList().get(i);
            if (hero.getMagneticArea().overlaps(powerUps.getHitArea())) {
                float dst = powerUps.getPosition().dst(hero.getPosition());
                float speed = (powerUps.getHitArea().radius + hero.getMagneticArea().radius - dst) / (level * 10.0f);
                tempVector.set(hero.getPosition()).sub(powerUps.getPosition()).nor();
                powerUps.getPosition().mulAdd(tempVector, speed);
            }
        }
    }

    private void checkCollision() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet bullet = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid asteroid = asteroidController.getActiveList().get(j);
                if (asteroid.getHitArea().contains(bullet.getPosition())) {
                    particleController.setup(bullet.getPosition().x + MathUtils.random(-4, 4),
                            bullet.getPosition().y + MathUtils.random(-4, 4),
                            bullet.getVelocity().x * -0.3f + MathUtils.random(-30, 30),
                            bullet.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.1f,
                            3.0f, 2.0f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            1.0f, 1.0f, 0.0f, 0.0f);
                    bullet.deactivate();
                    if (asteroid.takeDamage(bullet.getDamage())) {
                        powerUpsController.setup(asteroid.getPosition().x, asteroid.getPosition().y, 0.25f);
                        float scale = asteroid.getScale() - 0.3f;
                        if (scale >= 0.39) {
                            asteroidController.breakAsteroid(asteroid.getPosition().x, asteroid.getPosition().y, scale, level);
                        }
                        hero.addScore(asteroid.getHpMax() * 100);
                    }
                    break;
                }
            }
        }
    }
}
