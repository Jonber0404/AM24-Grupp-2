package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;
import java.util.List;

public class GameOverScreen implements Screen {

    private final JumpyBirb jumpyBirb;
    private final BitmapFont fontSmall;

    private List<ScoreWithName> highscores;


    public GameOverScreen(JumpyBirb jumpyBirb) {
        this.jumpyBirb = jumpyBirb;

        fontSmall = TextUtil.generateFont("COMIC.TTF", 40, Color.BLACK);

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
        fontSmall.draw(jumpyBirb.getBatch(), "GAME OVER\nScore: " + jumpyBirb.getScore() + "\nHigh Score: "
                        + highscores.get(0).score(),
                Gdx.graphics.getWidth() / 2f - 70,
                Gdx.graphics.getHeight() / 2f + 70);
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