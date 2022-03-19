package ru.star.app.game;

import com.badlogic.gdx.math.MathUtils;

import static ru.star.app.screen.ScreenSettings.*;

public class GameController {
    private Background bg;
    private Hero hero;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private final int ASTEROID_MAX_COUNT = 2;

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public Background getBg() {
        return bg;
    }

    public Hero getHero() {
        return hero;
    }


    public GameController() {
        bg = new Background(this);
        hero = new Hero(this);
        bulletController = new BulletController();
        asteroidController = new AsteroidController();
    }

    public void update(float dt) {
        bg.update(dt);
        hero.update(dt);
        bulletController.update(dt);
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
    }

    private void checkShipCollision() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid asteroid = asteroidController.getActiveList().get(i);
            if(hero.getHitArea().overlaps(asteroid.getHitArea())){
                asteroid.deactivate();
                if(hero.takeDamage(asteroid.getHpMax())){
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
                    bullet.deactivate();
                    if (asteroid.takeDamage(bullet.getDamage())) {
                        float scale = asteroid.getScale() - 0.3f;
                        if(scale >= 0.39) {
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
