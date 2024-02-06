package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class JumpyBirb extends ApplicationAdapter {
	SpriteBatch batch;
	Texture birdImage;
	Rectangle bird;
	OrthographicCamera camera;
	private float gravity = -0.5f; // Gravitationskraft som påverkar fågeln varje frame
	private float velocity = 0; // Fågelns vertikala hastighet

	private List<Score> highscores;
	private int score;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		birdImage = new Texture("pixlybird.png");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		bird = new Rectangle(800 / 2 - 64 / 2, 480 / 2, birdImage.getWidth(), birdImage.getHeight());

		float scale = 0.2f; // Adjust the scale factor as needed
		bird.setSize(bird.width * scale, bird.height * scale);

		highscores = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			highscores.add(new Score("", 0));
		}
		score = 0;
	}

	@Override
	public void render () {
		velocity += gravity; // Lägg till gravitationen till hastigheten
		bird.y += velocity; // Uppdatera fågelns position med den nya hastigheten

		// Förhindra fågeln från att falla genom marken
		if (bird.y < 0) {
			bird.y = 0;
			velocity = 0; // Stoppa ytterligare fall när fågeln når marken
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			velocity = 10; // Justera detta värde för att ändra hur högt fågeln hoppar
		}


		// Resten av din render-kod...
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(birdImage, bird.x, bird.y, bird.width, bird.height);
		batch.end();

	}

	/**
	 * Anropa denna metod varje gång spelaren tar sig förbi ett hinder
	 */
	public void updateScore() {
		score += 1;
	}

	/**
	 * Anropas när spelaren dör.
	 * Det här är en skiss som kan behöva ändras
	 */
	public void onDeath() {
		if (score > highscores.get(9).score()) {
			// Lägg till input för namn här...?
			var placeholderName = "Bertil";
			addHighScore(placeholderName);
		}

		score = 0;
	}

	/**
	 * Anropas av onDeath(), lägger till ett score i highscore och tar bort alla scores som är inte är topp 10
	 * @param name
	 */
	public void addHighScore(String name) {
		for (int i = 0; i < 10; i++) {
			if (score > highscores.get(i).score()) {
				highscores.add(i, new Score(name, score));
				highscores = highscores.subList(0, 10);
				break;
			}
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		birdImage.dispose();
	}
}
