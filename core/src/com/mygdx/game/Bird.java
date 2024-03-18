package com.mygdx.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import javax.swing.plaf.nimbus.State;

public class Bird {
    private static final float SCALE = 2.2f;        // previously 0.1f, controls the scale of the bird sprite

    private Animatronica birdAnimation;
    private Texture birdTexture;
    private final Texture birdSizeLimiter;

    //private Texture image;
    private Rectangle bounds;
    private float velocity = 0;
    private float gravity = -0.5f;
    private int extraLife;
    private boolean gravityEnabled = false;

    public Bird(String imagePath) {
        //this.image = new Texture(imagePath);
        this.extraLife = 1;

        birdTexture = new Texture(imagePath);
        birdAnimation = new Animatronica(new TextureRegion(birdTexture), 3, 0.5f);
        birdSizeLimiter = new Texture("birbSizeLimiter.png");

        // birdSizeLimiter was previously birdTexture
        this.bounds = new Rectangle(0, 0, birdSizeLimiter.getWidth(), birdSizeLimiter.getHeight());
    }

    public void updateAnimations(float dt) {
        birdAnimation.update(dt);
    }

    public TextureRegion getBirdTexture(){
        return birdAnimation.getFrame();
    }

    public Texture getBirdSize() { return birdSizeLimiter; }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }
    public float getYposition () {
        return bounds.y;
    }
    public float getXposition () {
        return bounds.x;
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
        bounds.setWidth(getBirdSize().getWidth() / 2f * SCALE);
        bounds.setHeight(getBirdSize().getHeight() / 2f * SCALE);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(getBirdTexture(), bounds.x, bounds.y, bounds.width, bounds.height);  // previously image
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
        birdTexture.dispose();
    }
}
