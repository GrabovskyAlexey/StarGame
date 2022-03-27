package ru.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.star.app.screen.utils.Assets;

import static ru.star.app.screen.ScreenManager.SCREEN_HEIGHT;
import static ru.star.app.screen.ScreenManager.SCREEN_WIDTH;

public class Hero extends Ship{
    public enum HeroUpgrade {
        HPMAX(10, 10), HP(5, 10), WEAPON(100, 1), MAGNETIC(50, 10), BULLET(10, 20);
        int cost;
        int power;

        HeroUpgrade(int cost, int power) {
            this.cost = cost;
            this.power = power;
        }
    }


    private TextureRegion starTexture;
    private int score;
    private int scoreView;
    private Circle magneticArea;
    private float magneticRadius;
    private StringBuilder sb;
    private int coins;
    private Shop shop;


    public int getCoins() {
        return coins;
    }

    public Circle getMagneticArea() {
        return magneticArea;
    }

    public Shop getShop() {
        return shop;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public Hero(GameController gc) {
        super(gc, Weapon.WeaponOwner.HERO);
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.starTexture = Assets.getInstance().getAtlas().findRegion("star16");
        this.position = new Vector2(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.SHIP_SPEED = 500.0f;
        this.magneticRadius = 56.0f;
        this.hpMax = 100;
        this.hp = hpMax;
        this.hitArea = new Circle(position, 28.0f);
        this.magneticArea = new Circle(position, magneticRadius);
        this.sb = new StringBuilder();
        this.shop = new Shop(this);
        this.coins = 100;
    }

    public boolean isMoneyEnough(int amount) {
        return coins >= amount;
    }

    public void decreaseMoney(int amount) {
        coins -= amount;
    }

    public void upgrade(HeroUpgrade upgrade) {
        if (isMoneyEnough(upgrade.cost)) {
            switch (upgrade) {
                case HPMAX:
                    decreaseMoney(upgrade.cost);
                    hpMax += upgrade.power;
                    break;
                case HP:
                    if (hp < hpMax) {
                        decreaseMoney(upgrade.cost);
                        increaseHP(upgrade.power);
                    }
                    break;
                case WEAPON:
                    if (weaponUpgrade()) {
                        decreaseMoney(upgrade.cost);
                    }
                    break;
                case MAGNETIC:
                    decreaseMoney(upgrade.cost);
                    magneticRadius += upgrade.power;
                    magneticArea.setRadius(magneticRadius);
                    break;
                case BULLET:
                    if (weapon.getCurrBullets() < weapon.getMaxBullets()) {
                        decreaseMoney(upgrade.cost);
                        weapon.increaseAmmo(upgrade.power);
                    }
                    break;
            }
        }
    }

    public void pickupPowerUps(PowerUps powerUps) {
        sb.setLength(0);
        Color drawColor = Color.WHITE;
        switch (powerUps.getType()) {
            case MEDKIT:
                int incHp = increaseHP(10);
                sb.append("HP +").append(incHp);
                drawColor = Color.GREEN;
                break;
            case AMMOS:
                int incAmmo = weapon.increaseAmmo(20);
                sb.append("AMMO +").append(incAmmo);
                drawColor = Color.FIREBRICK;
                break;
            case MONEY:
                increaseCoin(100);
                sb.append("COINS +").append(100);
                drawColor = Color.GOLD;
                break;
        }
        powerUps.deactivate();
        gc.getInfoController().setup(powerUps.getPosition().x, powerUps.getPosition().y, sb, drawColor);
        sb.setLength(0);
    }

    private boolean weaponUpgrade() {
        if(currWeapon < weapons.length -1){
            currWeapon++;
            weapon = weapons[currWeapon];
            return true;
        }
        return false;
    }

    private int increaseHP(int amount) {
        int oldHp = hp;
        hp += amount;
        if (hp > hpMax) {
            hp = hpMax;
        }
        return hp - oldHp;
    }

    public void increaseCoin(int amount) {
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
        this.magneticArea.setPosition(position);
    }

    private void updateScore(float dt) {
        if (scoreView < score) {
            scoreView += 1500 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font32) {
        sb.setLength(0);
        sb.append("SCORE: ").append(scoreView).append("\n")
                .append("HP: ").append(hp).append("/").append(hpMax).append("\n")
                .append("AMMO: ").append(weapon.getCurrBullets()).append("/").append(weapon.getMaxBullets()).append("\n")
                .append("COINS: ").append(coins);
        font32.draw(batch, sb, 20, 700);
        sb.setLength(0);
        font32.draw(batch, sb, 1080, 700);
    }

    public void drawMagneticField(SpriteBatch batch) {
        batch.setColor(Color.WHITE);
        for (int i = 0; i < 120; i++) {
            batch.draw(starTexture, magneticArea.x + magneticRadius * MathUtils.cosDeg(360.0f / 120.0f * i) - 8,
                    magneticArea.y + magneticRadius * MathUtils.sinDeg(360.0f / 120.0f * i) - 8);
        }
    }
}
