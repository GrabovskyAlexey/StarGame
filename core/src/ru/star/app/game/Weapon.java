package ru.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Weapon {
    public enum WeaponType {
        SINGLE, DUAL, TRIPLE
    }

    private int maxBullets;
    private int currBullets;
    private BulletController bulletController;
    private float firePeriod;
    private int damage;
    private float bulletSpeed;
    private WeaponType type;

    private Vector3[] slots;

    public Weapon(BulletController bulletController, WeaponType type) {
        this.bulletController = bulletController;
        this.type = type;
        createSlots();
    }

    public WeaponType getType() {
        return type;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public int getCurrBullets() {
        return currBullets;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public void increaseAmmo(int amount){
        currBullets += amount;
        if(currBullets > maxBullets){
            currBullets = maxBullets;
        }
    }

    public void upgradeWeapon(WeaponType type){
        this.type = type;
        createSlots();
    }

    private void createSlots() {
        switch (type) {
            case SINGLE:
                slots = new Vector3[]{new Vector3(28, 0, 0)};
                firePeriod = 0.4f;
                damage = 1;
                bulletSpeed = 300.0f;
                maxBullets = 100;
                break;
            case DUAL:
                slots = new Vector3[]{new Vector3(20, 90, 0), new Vector3(20, -90, 0)};
                firePeriod = 0.3f;
                damage = 2;
                bulletSpeed = 500.0f;
                maxBullets = 150;
                break;
            case TRIPLE:
                slots = new Vector3[]{new Vector3(28, 0, 0), new Vector3(28, 90, 25), new Vector3(28, -90, -25)};
                firePeriod = 0.2f;
                damage = 2;
                bulletSpeed = 600.0f;
                maxBullets = 170;
                break;
        }
        currBullets = maxBullets;
    }

    public void fire(Vector2 position, Vector2 velocity, float angle) {
        if (currBullets > 0) {
            currBullets--;
            for (int i = 0; i < slots.length; i++) {
                float x, y, vx, vy;
                x = position.x + slots[i].x * MathUtils.cosDeg(angle + slots[i].y);
                y = position.y + slots[i].x * MathUtils.sinDeg(angle + slots[i].y);
                vx = velocity.x + bulletSpeed * MathUtils.cosDeg(angle + slots[i].z);
                vy = velocity.y + bulletSpeed * MathUtils.sinDeg(angle + slots[i].z);
                bulletController.setup(x, y, vx, vy);
            }
        }

    }
}
