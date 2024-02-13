package com.mygdx.game;

import com.badlogic.gdx.Game;

public class JumpyBirb extends Game {

    private GameScreen gameScreen;

    @Override
    public void create() {
            gameScreen = new GameScreen(this);
            setScreen(gameScreen);
    }
}