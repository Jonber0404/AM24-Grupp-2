package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;
import java.util.List;

public class GameOverScreen implements Screen {

    private final JumpyBirb jumpyBirb;
    private final BitmapFont fontSmall;

    private Preferences prefs;

    public GameOverScreen(JumpyBirb jumpyBirb) {
        this.jumpyBirb = jumpyBirb;

        fontSmall = TextUtil.generateFont("COMIC.TTF", 40, Color.BLACK);
    }

    @Override
    public void show() {
        prefs = switch (jumpyBirb.getCurrentDifficulty()) {
            case "easy" -> Gdx.app.getPreferences("easyHighscores");
            case "normal" -> Gdx.app.getPreferences("normalHighscores");
            case "hard" -> Gdx.app.getPreferences("hardHighscores");
            default -> null;
        };

        addHighScore("Bertil");
    }

    @Override
    public void render(float delta) {
        if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))) {
            jumpyBirb.newGame();
        }
        jumpyBirb.getBatch().begin();
        fontSmall.draw(jumpyBirb.getBatch(), "GAME OVER\nScore: " + jumpyBirb.getScore() + "\nHigh Score: "
                        + prefs.getInteger("score" + 1),
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

    /**
     * If score is in top 10, it gets stored to preferences in the correct spot. The entrys below the score will get
     * pushed down by 1 step if this happens.
     * @param name the name of the player
     */
    private void addHighScore(String name) {
        for (int i = 1; i < 11; i++) {
            if (jumpyBirb.getScore() > prefs.getInteger("score" + i)) {
                for (int j = 10; j > i; j--) {
                    prefs.putString("name" + j, prefs.getString("name" + (j - 1)));
                    prefs.putInteger("score" + j, prefs.getInteger("score" + (j - 1)));
                }
                prefs.putString("name" + i, name);
                prefs.putInteger("score" + i, jumpyBirb.getScore());
                prefs.flush();
                break;
            }
        }
    }
}