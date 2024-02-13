package com.mygdx.game;

import com.badlogic.gdx.Game;

public class JumpyBirb extends Game {

    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;
    private int score;

    @Override
    public void create() {
            gameScreen = new GameScreen(this);
            gameOverScreen = new GameOverScreen(this);
            score = 0;
            setScreen(gameScreen);
    }

    /**
     * Call this method in GameScreen when bird dies
     */
    public void setGameOver() {
        setScreen(gameOverScreen);
    }

    /**
     * Resets the score to 0
     */
    public void resetScore() {
        score = 0;
    }

    /**
     * Adds 1 to the score
     */
    public void updateScore() {
        score += 1;
    }

    /**
     * Returns the current score
     * @return
     */
    public int getScore() {
        return score;
    }
}
