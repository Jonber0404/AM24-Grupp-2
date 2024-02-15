package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
	Texture birdImage;
	Texture pillarImage;
	Texture backgroundImage;

	Rectangle bird;
	int extraLife;

	private Array<Rectangle> underPillars;
	private Array<Rectangle> overPillars;

	// TODO: Kan dessa oanvända variabler tas bort?
	OrthographicCamera camera;
	private long spawnTime;

	float timeSinceLastHit;

	// TODO: Ska vi flytta initieringen av variabler till konstruktorn?
	private float gravity = -0.5f; // Gravitationskraft som påverkar fågeln varje frame
	private float velocity = 0; // Fågelns vertikala hastighet


	private float spawnInterval = 2.0f;
	private float timeSinceLastSpawn = 0.0f;

	private float timeSinceLastPoint = 0.0f;
	private boolean birdCrashed = false;

	private JumpyBirb jumpyBirb;

    private boolean movingPillarsEnabled = false;
    private boolean gravityEnabled = false;


	public GameScreen(JumpyBirb jumpyBirb) {

		this.jumpyBirb = jumpyBirb;

		this.backgroundImage = new Texture("background.jpg");
		this.birdImage = new Texture("pixlybird_red.png");
		this.pillarImage = new Texture("brick_pillar_long.png");


		//camera = new OrthographicCamera();
		//camera.setToOrtho(false, 1280, 720);

		this.bird = new Rectangle(1280 / 2 - 64 / 2, 720 / 2, birdImage.getWidth(), birdImage.getHeight());

		float scale = 0.1f; // Adjust the scale factor as needed
		this.bird.setSize(bird.width * scale, bird.height * scale);

		this.overPillars = new Array<Rectangle>();
		this.underPillars = new Array<Rectangle>();

		extraLife = 1;
	}

	@Override
	public void render (float delta) {

		if (!birdCrashed) {

			timeSinceLastHit += Gdx.graphics.getDeltaTime();

			PillarCollision(overPillars, -2.5f);
			PillarCollision(underPillars, 4.5f);

			if (!birdCrashed) {
				//Förhindra fågeln från att falla innan man trycker på Space.
				BirdMovement();

				//Förhindra pellare från att spawn/röra sig åt vänster för än Spacebar har tryckts.
				StartGame();

				MovePillars(overPillars);
				MovePillars(underPillars);

				AddPointWhenPassingPillar();

				ExtraLife();
			}
				// Rendering logic

				// Resten av din render-kod...
				ScreenUtils.clear(0, 0, 0, 1);
				//camera.update();
				//batch.setProjectionMatrix(camera.combined);
				jumpyBirb.getBatch().begin();
				jumpyBirb.getBatch().draw(backgroundImage, 0, 0, 1280, 720);
				jumpyBirb.getBatch().draw(birdImage, bird.x, bird.y, bird.width, bird.height);
				for (Rectangle pillar : underPillars) {
					jumpyBirb.getBatch().draw(pillarImage, pillar.x, pillar.y, pillar.width, pillar.height);
				}
				for (Rectangle pillar : overPillars) {
					jumpyBirb.getBatch().draw(pillarImage, pillar.x, pillar.y + pillar.height, pillar.width, -pillar.height);
				}
				jumpyBirb.getBatch().end();
		}
	}

	private void PillarCollision(Array<Rectangle> pillars, float velocityFactor) {
		for (int i = 0; i < pillars.size; i++) {
			if (bird.overlaps(pillars.get(i)) && timeSinceLastHit > 0.1f) {
				timeSinceLastHit = 0f;
				System.out.println("Touched over pillar");
				if (extraLife == 1) {
					velocity = velocityFactor; //-2.5 och 4.5
					extraLife = 0;
				} else if (extraLife == 0) {
					GameOver();
				}
			}
		}
	}

	private void BirdMovement() {
		// Förhindra fågeln från att falla genom marken
		if (bird.y < 0) {
			bird.y = 0;
			velocity = 0; // Stoppa ytterligare fall när fågeln når marken
		}

		if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) && !birdCrashed) {
			velocity = 10; // Justera detta värde för att ändra hur högt fågeln hoppar
		}
	}

	private void StartGame() {
		if (gravityEnabled) {
			velocity += gravity; // Lägg till gravitationen till hastigheten
			bird.y += velocity; // Uppdatera fågelns position med den nya hastigheten
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			gravityEnabled = !gravityEnabled;   //this toggles gravity on from being off.
			movingPillarsEnabled = !movingPillarsEnabled;     //this toggles the "pillars" from moving left.
		}
		if (movingPillarsEnabled) {
			//
			timeSinceLastSpawn += Gdx.graphics.getDeltaTime();
			if (timeSinceLastSpawn >= spawnInterval) {
				spawnPillars();
				timeSinceLastSpawn = 0.0f;
			}
		}
	}

	private void MovePillars(Array<Rectangle> pillars) {
		Iterator<Rectangle> iter = pillars.iterator();
		while (iter.hasNext() && !birdCrashed) {
			Rectangle pillar = iter.next();
			pillar.x -= 200 * Gdx.graphics.getDeltaTime();
			if (pillar.x + 64 < 0) {
				iter.remove();
			}
		}
	}

	private void ExtraLife() {
		//Extra livs system(?)
		if (extraLife == 0) {
			timeSinceLastHit += Gdx.graphics.getDeltaTime();
			if (timeSinceLastHit > 3) {
				extraLife = 1;
			}
		}
	}

	private void AddPointWhenPassingPillar() {
		timeSinceLastPoint += Gdx.graphics.getDeltaTime();
		float pointInterval = 1.0f;
		for (Rectangle underPillar : underPillars) {
			if (bird.x > underPillar.x && bird.x < underPillar.x + underPillar.width) {
				if (timeSinceLastPoint >= pointInterval) {
					jumpyBirb.updateScore();
					System.out.println(jumpyBirb.getScore());

					timeSinceLastPoint = 0.0f;
				}
			}
		}
	}
	
	private void GameOver() {
		//Måste ändras till en riktig game over metod
		extraLife = 1;
		birdCrashed = true;
		render(100000);
		jumpyBirb.setGameOver();
		System.out.println(birdCrashed);
	}


	private void spawnPillars() {
		int randomRange = 360;
		int spaceBetweenPillars = 430;
		int pillarOffset = -125;
		int position = MathUtils.random(0, randomRange) + pillarOffset;
		float scale = 0.2f;

		Rectangle pillarUnder = new Rectangle
				(1280, position - spaceBetweenPillars, pillarImage.getWidth() * scale,
						pillarImage.getHeight() * scale);

		underPillars.add(pillarUnder);

		Rectangle pillarOver = new Rectangle
				(1280, position + spaceBetweenPillars, pillarImage.getWidth() * scale,
						pillarImage.getHeight() * scale);

		overPillars.add(pillarOver);
	}


	@Override
	public void show() {
		resetGame();
	}

	/**
	 * Resets everything to initial values
	 */
	private void resetGame() {
		underPillars.clear();
		overPillars.clear();
		gravityEnabled = false;
		movingPillarsEnabled = false;
		birdCrashed = false;
		bird.x = Gdx.graphics.getWidth() / 2 - 64 / 2;
		bird.y = Gdx.graphics.getHeight() / 2;
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
    	backgroundImage.dispose();
		birdImage.dispose();
		pillarImage.dispose();
	}
}
