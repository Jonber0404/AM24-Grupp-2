package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Iterator;
import java.util.List;

public class GameScreen implements Screen {

	private static final int SCREEN_CENTER_X = 1280 / 2;
	private static final int SCREEN_CENTER_Y = 720 / 2;
	Texture pillarImage;
	Texture backgroundImage;
	Bird bird;
	public Array<Pillar> underPillars = new Array<>();
	public Array<Pillar> overPillars = new Array<>();
	float timeSinceLastHit;
	private float spawnInterval = 2.0f;
	private float timeSinceLastSpawn = 0.0f;
	private float timeSinceLastPoint = 0.0f;
	private JumpyBirb jumpyBirb;
	private boolean movingPillarsEnabled = false;

	public GameScreen(JumpyBirb jumpyBirb) {

		this.jumpyBirb = jumpyBirb;

		this.backgroundImage = new Texture("background.jpg");
		this.pillarImage = new Texture("brick_pillar_long.png");
		this.bird = new Bird("pixlybird_red.png");

		this.bird.setSize();

		float birdX = SCREEN_CENTER_X - bird.getBounds().width / 2;
		float birdY = SCREEN_CENTER_Y - bird.getBounds().height / 2;
		bird.setPosition(birdX, birdY);

		this.overPillars = new Array<>();
		this.underPillars = new Array<>();
	}

	@Override
	public void render (float delta) {

		timeSinceLastHit += Gdx.graphics.getDeltaTime();
		pillarCollision(overPillars, -2.5f);
		pillarCollision(underPillars, 4.5f);
		groundCollision();

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			bird.jump();
		}

		run();

		movePillars(underPillars, delta);
		movePillars(overPillars, delta);

		addPointWhenPassingPillar();

		extraLife();

		ScreenUtils.clear(0, 0, 0, 1);
		jumpyBirb.getBatch().begin();
		jumpyBirb.getBatch().draw(backgroundImage, 0, 0, 1280, 720);
		bird.draw(jumpyBirb.getBatch());

		for (Pillar pillar : underPillars) {
			pillar.draw(jumpyBirb.getBatch());
		}
		for (Pillar pillar : overPillars) {
			jumpyBirb.getBatch().draw(pillarImage, pillar.getBounds().x, pillar.getBounds().y + pillar.getBounds().height, pillar.getBounds().width, -pillar.getBounds().height);
		}

		System.out.println(underPillars.size);

		jumpyBirb.getBatch().end();
	}

	private void pillarCollision(Array<Pillar> pillars, float velocityFactor) {
		for (int i = 0; i < pillars.size; i++) {
			if (bird.getBounds().overlaps(pillars.get(i).getBounds()) && timeSinceLastHit > 0.1f) {
				timeSinceLastHit = 0f;
				System.out.println("Touched over pillar");
				if (bird.getExtraLife() == 1) {
					bird.setVelocity(velocityFactor); //-2.5 och 4.5
					bird.setExtraLife(0);
				} else if (bird.getExtraLife() == 0) {
					gameOver();
				}
			}
		}
	}

	private void groundCollision () {
		if (bird.getYposition() < 0) {
			bird.setVelocity(0);
			gameOver();
		}
	}

	private void movePillars(Array<Pillar> pillars, float deltaTime) {
		Iterator<Pillar> iter = pillars.iterator();
		while (iter.hasNext() && movingPillarsEnabled) {
			Pillar pillar = iter.next();
			pillar.update(deltaTime);
			if (pillar.isOffScreen()) {
				iter.remove();
			}
		}
	}

	private void extraLife() {
		if (bird.getExtraLife() == 0) {
			timeSinceLastHit += Gdx.graphics.getDeltaTime();
			if (timeSinceLastHit > 3) {
				bird.setExtraLife(1);
			}
		}
	}

	private void addPointWhenPassingPillar() {
		timeSinceLastPoint += Gdx.graphics.getDeltaTime();
		float pointInterval = 1.0f;

		for (Pillar underPillar : underPillars) {
			Rectangle pillarBounds = underPillar.getBounds();
			if (bird.getBounds().x > pillarBounds.x && bird.getBounds().x < pillarBounds.x + pillarBounds.width) {
				if (timeSinceLastPoint >= pointInterval) {
					jumpyBirb.updateScore();
					System.out.println(jumpyBirb.getScore());
					timeSinceLastPoint = 0.0f;
				}
			}
		}
	}

	private void run() {
		if (bird.getIsGravityEnabled()) {
			bird.gravity();
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			bird.setGravityEnabled(true);  //this toggles gravity on from being off.
			movingPillarsEnabled = !movingPillarsEnabled;     //this toggles the "pillars" from moving left.
		}
		if (movingPillarsEnabled) {
			//
			timeSinceLastSpawn += Gdx.graphics.getDeltaTime();
			if (timeSinceLastSpawn >= spawnInterval) {
				//Pillar.createPillars(1280, 430, pillarImage, underPillars, overPillars);
				spawnPillars();
				timeSinceLastSpawn = 0.0f;
			}
		}
	}

	private void gameOver() {
		bird.setExtraLife(1);
		movingPillarsEnabled = false;
		bird.setGravityEnabled(false);
		jumpyBirb.setGameOver();
		System.out.println("birdCrashed");
	}

	/*private void spawnPillars() {
		Array<Pillar> pillars = Pillar.createPillars(1280, 430, pillarImage, underPillars, overPillars); // Antag att pillarImage redan är laddad
		//underPillars.add(pillars.get(0));
		//overPillars.add(pillars.get(1));
	}*/

	//Test test test
	private void spawnPillars() {
		int randomRange = 360;
		int spaceBetweenPillars = 430;
		int pillarOffset = -125;
		int position = MathUtils.random(0, randomRange) + pillarOffset;
		float scale = 0.2f;

		Pillar pillarUnder = new Pillar
				(1280, position - spaceBetweenPillars, pillarImage.getWidth() * scale,
						pillarImage.getHeight() * scale);

		underPillars.add(pillarUnder);

		Pillar pillarOver = new Pillar
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
		bird.setGravityEnabled(false);
		movingPillarsEnabled = false;
		bird.getBounds().x = Gdx.graphics.getWidth() / 2f - 64 / 2f;
		bird.getBounds().y = Gdx.graphics.getHeight() / 2f;
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
		pillarImage.dispose();
		bird.dispose();
	}
}
