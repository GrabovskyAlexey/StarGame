package ru.star.app.game;

import com.badlogic.gdx.math.MathUtils;

import static ru.star.app.screen.ScreenSettings.SCREEN_HEIGHT;
import static ru.star.app.screen.ScreenSettings.SCREEN_WIDTH;

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
                        MathUtils.random(0, 360)
                );
            }
        }
        asteroidController.update(dt);
        checkCollision();
    }

    private void checkCollision(){
        for (Bullet bullet : bulletController.getActiveList()){
            for (Asteroid asteroid : asteroidController.getActiveList()){
                if(asteroid.getPosition().dst(bullet.getPosition()) < 112.0f){
                    bullet.deactivate();
                    asteroid.deactivate();
                }
            }
        }
    }
}
