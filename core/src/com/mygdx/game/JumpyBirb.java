package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class JumpyBirb extends ApplicationAdapter {
	SpriteBatch batch;
	Texture birdImage;
	Rectangle bird;
	OrthographicCamera camera;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		birdImage = new Texture("pixlybird.png");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		bird = new Rectangle(800 / 2 - 64 / 2, 480 / 2, birdImage.getWidth(), birdImage.getHeight());

		float scale = 0.2f; // Adjust the scale factor as needed
		bird.setSize(bird.width * scale, bird.height * scale);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(birdImage, bird.x, bird.y, bird.width, bird.height);
		batch.end();

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			bird.y += 1000;
		} else {
			bird.y -= 4;
		}

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		birdImage.dispose();
	}
}
