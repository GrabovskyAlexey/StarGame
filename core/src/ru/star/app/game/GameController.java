package ru.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static ru.star.app.screen.ScreenSettings.*;

public class GameController {
    private Background bg;
    private Hero hero;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private final int ASTEROID_MAX_COUNT = 2;
    private Vector2 tempVector;

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


    public GameController() {
        this.bg = new Background(this);
        this.bulletController = new BulletController(this);
        this.asteroidController = new AsteroidController();
        this.powerUpsController = new PowerUpsController();
        this.hero = new Hero(this);
        this.tempVector = new Vector2();
        this.particleController = new ParticleController();
    }

    public void update(float dt) {
        bg.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        particleController.update(dt);
        powerUpsController.update(dt);
        if (asteroidController.getActiveList().size() < ASTEROID_MAX_COUNT) {
            if (MathUtils.random(0, 400) < 1) {
                asteroidController.setup(MathUtils.random(0, SCREEN_WIDTH),
                        MathUtils.random(0, SCREEN_HEIGHT),
                        MathUtils.random(-150, 150), MathUtils.random(-150, 150), 1);
            }
        }
        asteroidController.update(dt);
        checkCollision();
        checkShipCollision();
        checkPickPowerUps();
    }

    private void checkPickPowerUps(){
        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUps powerUps = powerUpsController.getActiveList().get(i);
            if (hero.getHitArea().overlaps(powerUps.getHitArea())) {
                hero.pickupPowerUps(powerUps);
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
                    powerUpsController.setup(asteroid.getPosition().x, asteroid.getPosition().y);
                    float scale = asteroid.getScale() - 0.3f;
                    if (scale >= 0.39) {
                        asteroidController.breakAsteroid(asteroid.getPosition().x, asteroid.getPosition().y, scale);
                    }
                    hero.addScore(asteroid.getHpMax() * 50);
                }
                if (hero.takeDamage(2)) {
                    //GAME OVER
                }
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
                        powerUpsController.setup(asteroid.getPosition().x, asteroid.getPosition().y);
                        float scale = asteroid.getScale() - 0.3f;
                        if (scale >= 0.39) {
                            asteroidController.breakAsteroid(asteroid.getPosition().x, asteroid.getPosition().y, scale);
                        }
                        hero.addScore(asteroid.getHpMax() * 100);
                    }
                    break;
                }
            }
        }
    }
}
