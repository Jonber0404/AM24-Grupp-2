package com.mygdx.game;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

public class GameInputProcessor extends InputAdapter {
    private GameScreen gameScreen;

    public GameInputProcessor(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Forward touch events to GameScreen
        return gameScreen.touchDown(screenX, screenY, pointer, button);
    }

}