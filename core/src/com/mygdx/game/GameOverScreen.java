package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class GameOverScreen implements Screen {

    private JumpyBirb jumpyBirb;
    private SpriteBatch batch;

    private List<ScoreWithName> highscores;

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

        fontParam.size = 40;
        font = fontGen.generateFont(fontParam);

        this.highscores = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            highscores.add(new ScoreWithName("", 0));
        }
    }

    @Override
    public void show() {
        if (jumpyBirb.getScore() > highscores.get(9).score()) {
            // Lägg till input för namn här...?
            var placeholderName = "Bertil";
            addHighScore(placeholderName);
        }
    }

    @Override
    public void render(float delta) {
        batch.begin();
        font.draw(batch, test + "\nScore: " + jumpyBirb.getScore() + "\nHigh Score: " + highscores.get(0).score(),
                Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
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
        jumpyBirb.resetScore();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }


    public void addHighScore(String name) {
        for (int i = 0; i < 10; i++) {
            if (jumpyBirb.getScore() > highscores.get(i).score()) {
                highscores.add(i, new ScoreWithName(name, jumpyBirb.getScore()));
                highscores = highscores.subList(0, 10);
                break;
            }
        }
    }
}