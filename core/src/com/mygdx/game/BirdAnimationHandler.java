/*package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import javax.swing.plaf.nimbus.State;

public class BirdAnimationHandler {
    public enum AnimationStates { JUMPING, FALLING, FLAPPING, DYING };
    public State currentState;
    public State previousState;
    private Animation birdJumpingRightLeg;
    private Animation birdJumpingLeftLeg;
    private boolean isJumpingRightLeg;   //For alterating between jumping with the left leg and with the right leg.
    private float animationStateTimer;  //Tracks the time we are in any Animation State.

    public BirdAnimationHandler() {
        currentState = State.FLAPPING;
        previousState = State.FLAPPING;
        isJumpingRightLeg = true;
        animationStateTimer = 0f;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        // For setting the animations of the birb jumping with their right leg.
        for (int i = 2; i < 4; i++){
            frames.add(new TextureRegion(getTexture(), i * 60, 0, 60, 60));
        }
        birdJumpingRightLeg = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 6; i < 8; i++){
            frames.add(new TextureRegion(getTexture(), i * 60, 0, 60, 60));
        }
        birdJumpingLeftLeg = new Animation(0.1f, frames);
        frames.clear();
    }
    public void updateAnimations(float dt){
        setRegion(getFrame(dt));
    }
    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case JUMPING && isJumpingRightLeg:
                region = birdJumpingRightLeg.getKeyFrame(animationStateTimer);
                break;
            case JUMPING && !isJumpingRightLeg:
                region = birdJumpingLeftLeg.getKeyFrame(animationStateTimer);
                break;
            case FALLING:
            case FLAPPING:
            default:
                region = birdFlapping;
                break;
        }
        if()
    }

    public State getState(){
        if (getVelocity() > 0){
            return State.JUMPING;
        }
        else if (getVelocity() < 0) {
            return State.FALLING;
        } else {
            return State.FLAPPING;
        }
    }
}
*/