package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import org.w3c.dom.css.Rect;

import java.util.Iterator;

public class JumpyBirb extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;
	Texture birdImage;
	Texture pillarImage;
	Rectangle bird;

	private Array<Rectangle> pillars;
	private long spawnTime;

	private float gravity = -0.5f; // Gravitationskraft som påverkar fågeln varje frame
	private float velocity = 0; // Fågelns vertikala hastighet

	private float spawnInterval = 2.0f;
	private float timeSinceLastSpawn = 0.0f;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		birdImage = new Texture("pixlybird.png");
		pillarImage = new Texture("pillar.png");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		bird = new Rectangle(800 / 2 - 64 / 2, 480 / 2, birdImage.getWidth(), birdImage.getHeight());

		float scale = 0.1f; // Adjust the scale factor as needed
		bird.setSize(bird.width * scale, bird.height * scale);

		pillars = new Array<Rectangle>();
		spawnPillars();

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

		timeSinceLastSpawn += Gdx.graphics.getDeltaTime();
		if (timeSinceLastSpawn >= spawnInterval) {
			spawnPillars();
			timeSinceLastSpawn = 0.0f;
		}

		for (Iterator<Rectangle> iter = pillars.iterator(); iter.hasNext(); ) {
			Rectangle pillar = iter.next();
			pillar.x -= 200 * Gdx.graphics.getDeltaTime();
			if(pillar.x + 64 < 0) iter.remove();
		}



		// Resten av din render-kod...
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(birdImage, bird.x, bird.y, bird.width, bird.height);
		for(Rectangle pillar: pillars) {
			batch.draw(pillarImage, pillar.x, pillar.y, pillar.width, pillar.height);
		}
		batch.end();

	}

	private void spawnPillars(){
		int position = MathUtils.random(100, 380);
		float scale = 0.2f;
		Rectangle pillarUnder = new Rectangle
				(800, position -400, pillarImage.getWidth() * scale, pillarImage.getHeight() * scale);

		pillars.add(pillarUnder);

		Rectangle pillarOver = new Rectangle
				(800, position - 200, pillarImage.getWidth() * scale, pillarImage.getHeight() * scale);

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		birdImage.dispose();
		pillarImage.dispose();
	}
}
