package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animatronica {
    private static final int FRAME_COLS = 8, FRAME_ROWS = 1;
    private Array<TextureRegion> frames;
    private float maxFrameTime;
    private float currentFrameTime;
    private int frameCount;     //number of frames in an animation.
    private int currentFrame;

    public Animatronica (TextureRegion region, int frameCount, float cycleTime){
        frames = new Array<TextureRegion>();
        int frameWidth = region.getRegionWidth() / frameCount;
        for(int i = 0; i < frameCount; i++){
            frames.add(new TextureRegion(region, i * frameWidth, 0, frameWidth, region.getRegionHeight()));
        }
        this.frameCount = frameCount;
        maxFrameTime = cycleTime / frameCount;
        currentFrame = 0;
    }

    public void update(float dt){
        currentFrameTime += dt;
        if(currentFrame > maxFrameTime){
            currentFrame++;
            currentFrameTime = 0;
        }
        if(currentFrame >= frameCount){
            currentFrame = 0;
        }
    }

    public TextureRegion getFrame(){
        return frames.get(currentFrame);
    }
}
