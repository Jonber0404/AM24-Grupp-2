package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Grass {
    float x, y;
    float speed;
    Texture grassTexture;

    public Grass(float x, float y, float speed, Texture grassTexture) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.grassTexture = grassTexture;
    }

    public void update(float deltaTime) {
        speed = Pillar.getPillarSpeed() * 0.5f;
        x -= speed;

        if (x < -grassTexture.getWidth()) {
            // När gräset har rört sig helt utanför skärmen till vänster, flytta det till höger
            x = Gdx.graphics.getWidth();
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(grassTexture, x, y);
    }
}

