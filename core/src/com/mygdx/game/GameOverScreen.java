package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GameOverScreen implements Screen {

    private final JumpyBirb jumpyBirb;
    private final BitmapFont fontSmall;

    private Preferences prefs;

    private FreeTypeFontGenerator fontGen;
    private FreeTypeFontParameter fontParam;
    private BitmapFont fontSmall;
    private BitmapFont fontLarge;
    private List<ScoreWithName> highscores;
    private String test;

    public GameOverScreen(JumpyBirb jumpyBirb) {
        this.jumpyBirb = jumpyBirb;

        test = "GAME OVER";
        fontGen = new FreeTypeFontGenerator(Gdx.files.internal("ARCADECLASSIC.TTF"));
        fontParam = new FreeTypeFontParameter();

        fontParam.size = 40;
        fontParam.borderWidth = 2; // Bredden på ramen
        fontParam.borderColor = Color.BLUE;
        fontSmall = fontGen.generateFont(fontParam);

        fontParam.size = 60; // Exempel på en större storlek för "Game Over"
        fontLarge = fontGen.generateFont(fontParam); // Skapa en större font för "Game Over"

        this.highscores = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            highscores.add(new ScoreWithName("", 0));
        }
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

        GlyphLayout gameOverLayout = new GlyphLayout(fontLarge, "GAME OVER");
        float gameOverX = (Gdx.graphics.getWidth() - gameOverLayout.width) / 2;
        float gameOverY = Gdx.graphics.getHeight() / 2 + gameOverLayout.height * 2; // Justera Y för att flytta upp texten
        fontLarge.draw(jumpyBirb.getBatch(), "GAME OVER", gameOverX, gameOverY);

        // För poängtexten
        GlyphLayout scoreLayout = new GlyphLayout(fontSmall, "Difficulty     " + GameScreen.currentDifficulty + jumpyBirb.getScore() + "\nHigh Score     " + highscores.get(0).score());
        float scoreX = (Gdx.graphics.getWidth() - scoreLayout.width) / 2;
        float scoreY = Gdx.graphics.getHeight() / 2 - scoreLayout.height; // Justera Y för att flytta ner texten
        fontSmall.draw(jumpyBirb.getBatch(), "Difficulty     " + GameScreen.currentDifficulty + "\nScore     " + jumpyBirb.getScore() + "\nHigh Score     " + highscores.get(0).score(), scoreX, scoreY);

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