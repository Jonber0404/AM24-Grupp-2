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


    public void setGameOver() {
        setScreen(gameOverScreen);
    }

    public void resetScore() {
        score = 0;
    }
    public void updateScore() {
        score += 1;
    }

    public int getScore() {
        return score;
    }
}
