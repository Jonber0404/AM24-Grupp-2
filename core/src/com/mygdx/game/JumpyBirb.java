package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class JumpyBirb extends Game {

    private StartScreen startScreen;
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;
    private SpriteBatch batch;
    private int score;
    private OrthographicCamera camera;
    private FreeTypeFontGenerator fontGen;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParam;
    private BitmapFont fontSmall;


    @Override
    public void create() {



            gameScreen = new GameScreen(this);
            gameOverScreen = new GameOverScreen(this);
            batch = new SpriteBatch();

            fontGen = new FreeTypeFontGenerator(Gdx.files.internal("ARCADECLASSIC.TTF"));
            fontParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
            fontParam.size = 40;
            fontParam.borderWidth = 2; // Bredden på ramen
            fontParam.borderColor = Color.BLUE; // Färgen på ramen
            fontSmall = fontGen.generateFont(fontParam);

            camera = new OrthographicCamera();
            camera.setToOrtho(false,1280, 720);

            GameInputProcessor inputProcessor = new GameInputProcessor(gameScreen);
            Gdx.input.setInputProcessor(inputProcessor);

            score = 0;
            setScreen(gameScreen);
    }

    public void setGameOver(){
        setScreen(gameOverScreen);
    }

    public void newGame() {
        setScreen(gameScreen);
    }

    public void resetScore(){
        score = 0;
    }

    public void updateScore(){
        score += 1;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public int getScore(){
        return score;
    }

    public BitmapFont getFont() {
        return fontSmall;
    }

    public Camera getCamera() {
        return camera;
    }

    public void dispose() {
        startScreen.dispose();
        gameScreen.dispose();
        gameOverScreen.dispose();
    }

}
