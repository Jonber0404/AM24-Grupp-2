package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Input;

public class StartScreen implements Screen {
    private static final int SCREEN_CENTER_X = 1280 / 2;
    private static final int SCREEN_CENTER_Y = 720 / 2;
    private JumpyBirb jumpyBirb;
    private Texture background;
    private Bird bird;

    public StartScreen(final JumpyBirb jumpyBirb) {
        this.jumpyBirb = jumpyBirb;
        this.background = new Texture("background.jpg");
        this.bird = new Bird("pixlybird_red.png");

        this.bird.setSize();

        float birdX = SCREEN_CENTER_X - bird.getBounds().width / 2;
        float birdY = SCREEN_CENTER_Y - bird.getBounds().height / 2;
        bird.setPosition(birdX, birdY);
    }

    @Override
    public void show() {
        // Initialiseringslogik, om det behövs
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        jumpyBirb.getBatch().begin();
        jumpyBirb.getBatch().draw(background, 0, 0);
        bird.draw(jumpyBirb.getBatch());
        jumpyBirb.getBatch().end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            dispose();
            jumpyBirb.newGame();
        }
    }

    @Override
    public void resize(int width, int height) {
        // Hantera skärmstorleksändringar
    }

    @Override
    public void pause() {
        // Hantera pausläge
    }

    @Override
    public void resume() {
        // Hantera återupptagning
    }

    @Override
    public void hide() {
        // Kallas när denna skärm inte längre är den aktiva skärmen
    }

    @Override
    public void dispose() {
        background.dispose();
        bird.dispose();
    }
}