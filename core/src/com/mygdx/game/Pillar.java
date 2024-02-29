package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class Pillar {
    private Rectangle bounds;
    private static Texture image = new Texture("brick_pillar_long.png");

    public Pillar(float x, float y, float width, float height) {
        if (image == null) {
            image = new Texture("brick_pillar_long.png"); // Ladda textur om den inte redan är laddad
        }
        bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void update(float deltaTime) {
        bounds.x -= 200 * Gdx.graphics.getDeltaTime();
    }

    public boolean isOffScreen() {
        return bounds.x + bounds.width < 0;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(image, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public static void createPillars(float xPosition, float spaceBetween, Array<Pillar> underPillars, Array<Pillar> overPillars) {
        int randomRange = 360;
        int pillarOffset = -125;
        int position = MathUtils.random(0, randomRange) + pillarOffset;
        float scale = 0.2f;

        // Skapa den undre pelaren med dess position som baseras på `position - spaceBetween`
        Pillar underPillar = new Pillar(
                xPosition, position - spaceBetween, image.getWidth() * scale, image.getHeight() * scale);
        underPillars.add(underPillar);
        // Korrigera positionen för den övre pelaren så att den tar hänsyn till den faktiska höjden på pelaren
        // och det definierade mellanrummet mellan pelarna.
        Pillar overPillar = new Pillar(
                xPosition, position + spaceBetween, image.getWidth() * scale, image.getHeight() * scale);
        overPillars.add(overPillar);


    }

    public void dispose() {
        this.image.dispose();
    }
}
