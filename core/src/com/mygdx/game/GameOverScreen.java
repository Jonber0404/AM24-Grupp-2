package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GameOverScreen implements Screen {

    private JumpyBirb jumpyBirb;
    private SpriteBatch batch;

    private String test;

    private FreeTypeFontGenerator fontGen;
    private FreeTypeFontParameter fontParam;
    private BitmapFont font;

    public GameOverScreen(JumpyBirb jumpyBirb) {
        this.jumpyBirb = jumpyBirb;
        batch = new SpriteBatch();

        test = "GAME OVER";

        fontGen = new FreeTypeFontGenerator(Gdx.files.internal("COMIC.TTF"));
        fontParam = new FreeTypeFontParameter();

        fontParam.size = 80;
        font = fontGen.generateFont(fontParam);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        batch.begin();
        font.draw(batch, test, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
