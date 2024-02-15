package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class JumpyBirb extends Game {

    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;
    private SpriteBatch batch;
    private int score;


    @Override
    public void create() {
            gameScreen = new GameScreen(this);
            gameOverScreen = new GameOverScreen(this);
            batch = new SpriteBatch();

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

    public void dispose() {
        gameScreen.dispose();
        gameOverScreen.dispose();
    }

}
