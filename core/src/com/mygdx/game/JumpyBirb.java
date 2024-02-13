package com.mygdx.game;

import com.badlogic.gdx.Game;

<<<<<<< HEAD
public class JumpyBirb extends ApplicationAdapter {
	SpriteBatch batch;
	Texture birdImage;
	Rectangle bird;
	OrthographicCamera camera;
	private float gravity = -0.5f; // Gravitationskraft som påverkar fågeln varje frame
	private float velocity = 0; // Fågelns vertikala hastighet



	@Override
	public void create () {
		batch = new SpriteBatch();
		birdImage = new Texture("pixlybird.png");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
=======
public class JumpyBirb extends Game {
>>>>>>> 7c617ea9eeed4d0c219a6c38c1cc3edd661d36f9

    private GameScreen gameScreen;

<<<<<<< HEAD
		float scale = 0.2f; // Adjust the scale factor as needed
		bird.setSize(bird.width * scale, bird.height * scale);
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
	
	@Override
	public void dispose () {
		batch.dispose();
		birdImage.dispose();
	}
}
=======
    @Override
    public void create() {
            gameScreen = new GameScreen(this);
            setScreen(gameScreen);
    }
}
>>>>>>> 7c617ea9eeed4d0c219a6c38c1cc3edd661d36f9
