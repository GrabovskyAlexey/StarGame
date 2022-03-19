package ru.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import ru.star.app.game.helpers.ObjectPool;

public class PowerUpsController extends ObjectPool<PowerUps> {

    @Override
    protected PowerUps newObject() {
        return new PowerUps();
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        checkPool();
    }

    public void setup(float x, float y) {
        if(MathUtils.random(0, 3) == 0) {
            PowerUps.PowerUpsType type = PowerUps.PowerUpsType.values()[MathUtils.random(0, 3)];
            getActiveElement().activate(x, y, type);
        }
    }
}
