package ru.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import ru.star.app.game.helpers.Poolable;
import ru.star.app.screen.utils.Assets;

public class PowerUps implements Poolable {
    public enum PowerUpsType{
        HP, AMMO, COINS, WEAPON_UPGRADE
    }

    private Vector2 position;
    private PowerUpsType type;
    private Circle hitArea;
    private boolean active;
    private TextureRegion texture;

    public PowerUps() {
        this.position = new Vector2();
        this.hitArea = new Circle(position, 16);
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public PowerUpsType getType() {
        return type;
    }
    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate(){
        active = false;
    }



    public void render(SpriteBatch batch){
        batch.draw(texture, position.x - 16, position.y - 16, 32, 32);
    }

    public void activate(float x, float y, PowerUpsType type){
        position.set(x, y);
        hitArea.setPosition(position);
        this.type = type;
        switch(type){
            case HP:
                texture = Assets.getInstance().getAtlas().findRegion("health");
                break;
            case AMMO:
                texture = Assets.getInstance().getAtlas().findRegion("ammo");
                break;
            case WEAPON_UPGRADE:
                texture = Assets.getInstance().getAtlas().findRegion("upgrade");
                break;
            case COINS:
                texture = Assets.getInstance().getAtlas().findRegion("coin");
                break;
        }
        this.active = true;
    }
}
