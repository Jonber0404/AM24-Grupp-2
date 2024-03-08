package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class GameOverScreen implements Screen {

    private final JumpyBirb jumpyBirb;

    private Preferences prefs;

    private final BitmapFont fontSmall;
    private final BitmapFont fontLarge;

    public GameOverScreen(JumpyBirb jumpyBirb) {
        this.jumpyBirb = jumpyBirb;

        fontSmall = TextUtil.generate("ARCADECLASSIC.TTF", 40, Color.WHITE, 2, Color.BLUE);
        fontLarge = TextUtil.generate("ARCADECLASSIC.TTF", 60, Color.WHITE, 2, Color.BLUE);
    }

    @Override
    public void show() {
        System.out.println(jumpyBirb.getScore());
        prefs = switch (GameScreen.currentDifficulty) {
            case "EASY" -> Gdx.app.getPreferences("easyHighscores");
            case "NORMAL" -> Gdx.app.getPreferences("normalHighscores");
            case "HARD" -> Gdx.app.getPreferences("hardHighscores");
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

        GlyphLayout gameOverLayout = new GlyphLayout(fontLarge, "GAME OVER");
        float gameOverX = (Gdx.graphics.getWidth() - gameOverLayout.width) / 2;
        float gameOverY = Gdx.graphics.getHeight() / 2f + gameOverLayout.height * 2; // Justera Y för att flytta upp texten
        fontLarge.draw(jumpyBirb.getBatch(), "GAME OVER", gameOverX, gameOverY);

        // För poängtexten
        GlyphLayout scoreLayout = new GlyphLayout(fontSmall, "Difficulty     " + GameScreen.currentDifficulty + jumpyBirb.getScore() + "\nHigh Score     " + prefs.getInteger("score1"));
        float scoreX = (Gdx.graphics.getWidth() - scoreLayout.width) / 2;
        float scoreY = Gdx.graphics.getHeight() / 2f - scoreLayout.height; // Justera Y för att flytta ner texten
        fontSmall.draw(jumpyBirb.getBatch(), "Difficulty     " + GameScreen.currentDifficulty + "\nScore     " + jumpyBirb.getScore() + "\nHigh Score     " + prefs.getInteger("score1"), scoreX, scoreY);

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
        fontLarge.dispose();
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