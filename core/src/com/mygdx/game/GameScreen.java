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

	private Array<Rectangle> underPillars;
	private Array<Rectangle> overPillars;
	private long spawnTime;

	private float gravity = -0.5f; // Gravitationskraft som påverkar fågeln varje frame
	private float velocity = 0; // Fågelns vertikala hastighet

	private List<Score> highscores;


	private float spawnInterval = 2.0f;
	private float timeSinceLastSpawn = 0.0f;

	private float pointInterval = 1.0f;
	private float timeSinceLastPoint = 0.0f;

	private JumpyBirb jumpyBirb;


    private boolean movingPillars = false;
    private boolean gravityEnabled = false;


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

		this.highscores = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			highscores.add(new Score("", 0));
		}
		this.score = 0;

		this.overPillars = new Array<Rectangle>();
		this.underPillars = new Array<Rectangle>();
		spawnPillars();

	}

	@Override
	public void render (float delta) {
		//velocity += gravity; // Lägg till gravitationen till hastigheten
		//bird.y += velocity; // Uppdatera fågelns position med den nya hastigheten

		//Förhindra fågeln från att falla innan man trycker på Space.
        if(gravityEnabled == true){
            velocity += gravity; // Lägg till gravitationen till hastigheten
            bird.y += velocity; // Uppdatera fågelns position med den nya hastigheten
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            gravityEnabled = !gravityEnabled;   //this toggles gravity on from being off.
            movingPillars = !movingPillars;     //this toggles the "pillars" from moving left.
        }

		// Förhindra fågeln från att falla genom marken
		if (bird.y < 0) {
			bird.y = 0;
			velocity = 0; // Stoppa ytterligare fall när fågeln når marken
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			velocity = 10; // Justera detta värde för att ändra hur högt fågeln hoppar
		}

        //Förhindra pellare från att spawn/röra sig åt vänster för än Spacebar har tryckts.
        if (movingPillars == true) {
            timeSinceLastSpawn += Gdx.graphics.getDeltaTime();
            if (timeSinceLastSpawn >= spawnInterval) {
                spawnPillars();
                timeSinceLastSpawn = 0.0f;
            }


        /*timeSinceLastSpawn += Gdx.graphics.getDeltaTime();
		if (timeSinceLastSpawn >= spawnInterval) {
			spawnPillars();
			timeSinceLastSpawn = 0.0f;
		}*/

            for (Iterator<Rectangle> iter = underPillars.iterator(); iter.hasNext(); ) {
                Rectangle pillar = iter.next();
                pillar.x -= 200 * Gdx.graphics.getDeltaTime();
                if (pillar.x + 64 < 0) iter.remove();
            }
            for (Iterator<Rectangle> iter = overPillars.iterator(); iter.hasNext(); ) {
                Rectangle pillar = iter.next();
                pillar.x -= 200 * Gdx.graphics.getDeltaTime();
                if (pillar.x + 64 < 0) iter.remove();
            }

            timeSinceLastPoint += Gdx.graphics.getDeltaTime();
            for (Rectangle underPillar : underPillars) {
                if (bird.x > underPillar.x && bird.x < underPillar.x + underPillar.width) {
                    if (timeSinceLastPoint >= pointInterval) {
                        updateScore();
                        System.out.println(score);
                        timeSinceLastPoint = 0.0f;
                    }

                }
            }

        } //this is the point

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
