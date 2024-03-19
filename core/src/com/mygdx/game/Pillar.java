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
    private static Texture image = new Texture("bean.png");
    private float time;
    private final float scale;
    private static float pillarSpeed;

    public Pillar(float x, float y) {
        scale = 0.2f;
        if (image == null) {
            image = new Texture("bean.png"); // Ladda textur om den inte redan är laddad
        }
        bounds = new Rectangle(x, y, image.getWidth() * scale, image.getHeight() * scale);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void update(float deltaTime, int timeFactor) {
        pillarSpeed = (200 + (timeFactor * 10)) * Gdx.graphics.getDeltaTime();
        bounds.x -= pillarSpeed;
    }

    public static float getPillarSpeed() {
        return pillarSpeed;
    }

    public static void setPillarSpeed(float pillarSpeed) {
        Pillar.pillarSpeed = pillarSpeed;
    }

    public Texture getImage() {
        return image;
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
                xPosition, position - spaceBetween);
        underPillars.add(underPillar);
        // Korrigera positionen för den övre pelaren så att den tar hänsyn till den faktiska höjden på pelaren
        // och det definierade mellanrummet mellan pelarna.
        Pillar overPillar = new Pillar(
                xPosition, position + spaceBetween);
        overPillars.add(overPillar);


    }

    public void dispose() {
        this.image.dispose();
    }
}
