package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bird {
    private static final float SCALE = 0.1f;
    private Texture image;
    private Rectangle bounds;
    private float velocity = 0;
    private float gravity = -0.5f;
    private int extraLife;
    private boolean gravityEnabled = false;

    public Bird(String imagePath) {
        this.image = new Texture(imagePath);
        this.bounds = new Rectangle(0, 0, image.getWidth(), image.getHeight());
        this.extraLife = 1;
    }

    public float getYposition () {
        return bounds.y;
    }
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getVelocity() {
        return velocity;
    }

    public int getExtraLife() {
        return extraLife;
    }

    public void setExtraLife(int extraLife) {
        this.extraLife = extraLife;
    }

    public void setSize() {
        bounds.setWidth(image.getWidth() * SCALE);
        bounds.setHeight(image.getHeight() * SCALE);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(image, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void gravity() {
        velocity += gravity;
        bounds.y += velocity;
    }

    public boolean getIsGravityEnabled () {
        return gravityEnabled;
    }

    public void setGravityEnabled(boolean gravityEnabled) {
        this.gravityEnabled = gravityEnabled;
    }

    public void jump() {
        velocity = 10; // Justera detta värde efter behov
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setPosition(float x, float y) {
        bounds.setPosition(x, y);
    }

    public void dispose() {
        image.dispose();
    }
}
