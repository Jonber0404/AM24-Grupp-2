package com.mygdx.game;

import com.badlogic.gdx.Game;

public class JumpyBirb extends Game {

    private GameScreen gameScreen;
    private GameOverScreen gameOverScreen;

    @Override
    public void create() {
            gameScreen = new GameScreen(this);
            gameOverScreen = new GameOverScreen(this);
            setScreen(gameOverScreen);
    }
}
