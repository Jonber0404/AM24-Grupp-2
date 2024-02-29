package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GameOverScreen implements Screen {

    private final JumpyBirb jumpyBirb;
    private FreeTypeFontGenerator fontGen;
    private FreeTypeFontParameter fontParam;
    private BitmapFont fontSmall;

    private List<ScoreWithName> highscores;

    private String test;


    public GameOverScreen(JumpyBirb jumpyBirb) {
        this.jumpyBirb = jumpyBirb;

        test = "GAME OVER";
        fontGen = new FreeTypeFontGenerator(Gdx.files.internal("COMIC.TTF"));
        fontParam = new FreeTypeFontParameter();

        fontParam.size = 40;
        fontSmall = fontGen.generateFont(fontParam);

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
        if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))) {
            jumpyBirb.newGame();
        }
        jumpyBirb.getBatch().begin();
        fontSmall.draw(jumpyBirb.getBatch(), test + "\nScore: " + jumpyBirb.getScore() + "\nHigh Score: " + highscores.get(0).score(),
                Gdx.graphics.getWidth() / 2 - 70, Gdx.graphics.getHeight() / 2 + 70);
        jumpyBirb.getBatch().end();
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
        fontSmall.dispose();
        fontGen.dispose();
    }

    private void addHighScore(String name) {
        for (int i = 0; i < 10; i++) {
            if (jumpyBirb.getScore() > highscores.get(i).score()) {
                highscores.add(i, new ScoreWithName(name, jumpyBirb.getScore()));
                highscores = highscores.subList(0, 10);
                break;
            }
        }
    }
}