package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
	SpriteBatch batch;
	OrthographicCamera camera;
	Texture birdImage;

	Texture pillarImage;
	Texture pillarImageUpsideDown;

	Texture backgroundImage;

	Rectangle bird;
	int score;
	int extraLife;

	private Array<Rectangle> underPillars;
	private Array<Rectangle> overPillars;
	private long spawnTime;
	float timeSinceLastHit;

	private float gravity = -0.5f; // Gravitationskraft som påverkar fågeln varje frame
	private float velocity = 0; // Fågelns vertikala hastighet


	private float spawnInterval = 2.0f;
	private float timeSinceLastSpawn = 0.0f;

	private float pointInterval = 1.0f;
	private float timeSinceLastPoint = 0.0f;
	private boolean birdCrashed = false;

	private JumpyBirb jumpyBirb;

	public GameScreen(JumpyBirb jumpyBirb) {

		this.jumpyBirb = jumpyBirb;

		this.batch = new SpriteBatch();
		this.backgroundImage = new Texture("background.jpg");
		this.birdImage = new Texture("pixlybird.png");
		this.pillarImage = new Texture("pillar.png");


		//camera = new OrthographicCamera();
		//camera.setToOrtho(false, 1280, 720);

		this.bird = new Rectangle(1280 / 2 - 64 / 2, 720 / 2, birdImage.getWidth(), birdImage.getHeight());

		float scale = 0.1f; // Adjust the scale factor as needed
		this.bird.setSize(bird.width * scale, bird.height * scale);

		this.overPillars = new Array<Rectangle>();
		this.underPillars = new Array<Rectangle>();
		spawnPillars();
		extraLife = 1;
	}

	@Override
	public void render (float delta) {
		if(!birdCrashed) {
			velocity += gravity; // Lägg till gravitationen till hastigheten
			bird.y += velocity; // Uppdatera fågelns position med den nya hastigheten

			// Förhindra fågeln från att falla genom marken
			if (bird.y < 0) {
				bird.y = 0;
				velocity = 0; // Stoppa ytterligare fall när fågeln når marken
			}

			if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) && !birdCrashed) {
				velocity = 10; // Justera detta värde för att ändra hur högt fågeln hoppar
			}

			timeSinceLastSpawn += Gdx.graphics.getDeltaTime();
			if (timeSinceLastSpawn >= spawnInterval) {
				spawnPillars();
				timeSinceLastSpawn = 0.0f;
			}

			Iterator<Rectangle> iterUnder = underPillars.iterator();
			while (iterUnder.hasNext() && !birdCrashed) {
				Rectangle pillar = iterUnder.next();
				pillar.x -= 200 * Gdx.graphics.getDeltaTime();
				if (pillar.x + 64 < 0) {
					iterUnder.remove();
				}
			}


			Iterator<Rectangle> iterOver = overPillars.iterator();
			while (iterOver.hasNext() && !birdCrashed) {
				Rectangle pillar = iterOver.next();
				pillar.x -= 200 * Gdx.graphics.getDeltaTime();
				if (pillar.x + 64 < 0) {
					iterOver.remove();
				}
			}

			timeSinceLastPoint += Gdx.graphics.getDeltaTime();
			for (Rectangle underPillar : underPillars) {
				if (bird.x > underPillar.x && bird.x < underPillar.x + underPillar.width) {
					if (timeSinceLastPoint >= pointInterval) {
						jumpyBirb.updateScore();
						System.out.println(jumpyBirb.getScore());

						timeSinceLastPoint = 0.0f;
					}

				}
			}
			if (extraLife == 0) {
				timeSinceLastHit += Gdx.graphics.getDeltaTime();
				if (timeSinceLastHit > 3) {
					extraLife = 1;
				}
			}


			timeSinceLastHit += Gdx.graphics.getDeltaTime();

			for (int i = 0; i < overPillars.size; i++) {
				if (bird.overlaps(overPillars.get(i)) && timeSinceLastHit > 0.1f) {
					timeSinceLastHit = 0f;
					System.out.println("Touched over pillar");
					if (extraLife == 1) {
						velocity = -2.5f;
						extraLife = 0;
					} else if (extraLife == 0) {
						GameOver(iterOver, iterUnder);
					}

				} else if (bird.overlaps(underPillars.get(i)) && timeSinceLastHit > 0.1f) {
					timeSinceLastHit = 0f;
					System.out.println("Touched under pillar");
					if (extraLife == 1) {
						velocity = 4.5f;
						extraLife = 0;
					} else if (extraLife == 0) {
						GameOver(iterOver, iterUnder);
					}
				}
			}


		}
		// Resten av din render-kod...
		ScreenUtils.clear(0, 0, 0, 1);
		//camera.update();
		//batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(backgroundImage, 0, 0, 1280, 720);
		batch.draw(birdImage, bird.x, bird.y, bird.width, bird.height);
		for(Rectangle pillar: underPillars) {
			batch.draw(pillarImage, pillar.x, pillar.y, pillar.width, pillar.height);
		}
		for(Rectangle pillar: overPillars) {
			batch.draw(pillarImage, pillar.x, pillar.y + pillar.height, pillar.width, -pillar.height);
		}
		batch.end();


	}

	private void GameOver(Iterator<Rectangle> iterOver, Iterator<Rectangle> iterUnder) {
		//Måste ändras till en riktig game over metod
		/*iterOver.remove();
		iterUnder.remove();
		overPillars.clear();
		underPillars.clear(); */
		extraLife = 1;
		birdCrashed = true;
		render(5);
		jumpyBirb.setGameOver();

	}


	private void spawnPillars(){
		int position = MathUtils.random(80, 600);
		float scale = 0.2f;
		Rectangle pillarUnder = new Rectangle
				(1280, position -400, pillarImage.getWidth() * scale, pillarImage.getHeight() * scale);

		underPillars.add(pillarUnder);

		Rectangle pillarOver = new Rectangle
				(1280, position + 140, pillarImage.getWidth() * scale, pillarImage.getHeight() * scale);

		overPillars.add(pillarOver);

	}


	@Override
	public void show() {

	}


	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		batch.dispose();
    	backgroundImage.dispose();
		birdImage.dispose();
		pillarImage.dispose();
	}
}
