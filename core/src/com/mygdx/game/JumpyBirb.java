package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class JumpyBirb extends Game {

    private StartScreen startScreen;
    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;
    private SpriteBatch batch;
    private int score;
    private OrthographicCamera camera;

    @Override
    public void create() {

            gameScreen = new GameScreen(this);
            gameOverScreen = new GameOverScreen(this);
            batch = new SpriteBatch();

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

    public Camera getCamera() {
        return camera;
    }

    public void dispose() {
        startScreen.dispose();
        gameScreen.dispose();
        gameOverScreen.dispose();
    }

}
