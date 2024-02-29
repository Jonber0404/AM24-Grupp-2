package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator implements ApplicationListener {
    private static final int FRAME_COLS = 6, FRAME_ROWS = 5;
    public Texture birbSheet;
    SpriteBatch spriteBatch;

    public Animation<TextureRegion> flappyAnimation;
    public Animation<TextureRegion> jumpyAnimation;

 //   flappyAnimation = new Animation<TextureRegion>(0.033f, atlas.findRegions("running"), Animation.PlayMode.LOOP);
//    jumpyAnimation = new Animation<TextureRegion>(0.033f, atlas.findRegions("running", Animation.PlayMode.LOOP));

    //En variable för att hålla koll på den förflutna tiden
    float stateTime;

    @Override
    public void create(){
        birbSheet = new Texture(Gdx.files.internal("sheetname.png"));	//insert actual name.

        // Använder split utility metoden för att skapa en 2D array av TextureRegions. Detta är
        // möjligt för att denna sprite sheet har frames av samma storlek och de är alla justerade.
        TextureRegion[][] tmp = TextureRegion.split(birbSheet, birbSheet.getWidth() / FRAME_COLS,
                birbSheet.getHeight() / FRAME_ROWS);

        //Provar att läga texture regionerna i en 1D array i en korrekt order, startande från top vänster.
        //Animations konstruktorn behöver tydligen en 1D array.
        TextureRegion[] jumpingFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int jumpingIndex = 0;
        for (int i = 0; i < FRAME_ROWS; i++){
            for (int j = 0; j < FRAME_COLS; j++){
                jumpingFrames[jumpingIndex++] = tmp[i][j];
            }
        }

        //Initierar flappyAnimation med frame intervalet och arrayen från jumpingFrames.
        flappyAnimation = new Animation<TextureRegion>(0.025f, jumpingFrames);

        //Instansiera en SpriteBatch för rita (drawing) och reset den förflutna (elapsed) animation
        // tid till 0.
        spriteBatch = new SpriteBatch();
        stateTime = 0f;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
