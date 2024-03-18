package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen {

    private static final int SCREEN_CENTER_X = 1280 / 2;
    private static final int SCREEN_CENTER_Y = 720 / 2;
    private final BitmapFont fontSmall;
    Texture pillarImage;
    Texture backgroundImage;
    Bird bird;
    public Array<Pillar> underPillars;
    public Array<Pillar> overPillars;
    float timeSinceLastHit;
    private float spawnInterval = 2.0f;
    private float timeSinceLastSpawn = 0.0f;
    private float timeSinceLastPoint = 0.0f;
    private final JumpyBirb jumpyBirb;
    private boolean movingPillarsEnabled = false;

    private final Texture difficultyButtonsTexture;
    private Rectangle[] difficultyButtons;
    private final String[] difficultyButtonNames = {"EASY", "NORMAL", "HARD"};
    public static String currentDifficulty;
    private float difficultyFactor = 0;
    private boolean birdHasCollided = false;


    private static final float INITIAL_SPEED = 200;
    private static final float SPEED_INCREMENT = 10;
    private static final float INITIAL_SPAWN_INTERVAL = 2; // in seconds
    private static final float CONSTANT_DISTANCE = 200; // for example
    private final float lastSpawnPosition = 0;
    private final float speedIncrement = 1;
    float nextSpawnInterval = 2;
    float n = 2;

    public GameScreen(JumpyBirb jumpyBirb) {

        this.difficultyButtonsTexture = new Texture("difficultybutton.png");
        difficultyButtons = new Rectangle[3];
        float buttonWidth = 150;
        float buttonHeight = 50;
        float buttonSpacing = 10;
        float totalHeight = (buttonHeight + buttonSpacing) * 1;
        float startY = (SCREEN_CENTER_Y - totalHeight) / 2 + 80;
        float buttonX = SCREEN_CENTER_X - buttonWidth / 2;
        fontSmall = TextUtil.generate("ARCADECLASSIC.TTF", 40, Color.WHITE, 2, Color.BLUE);

        // Set positions for buttons
        for (int i = 0; i < difficultyButtons.length; i++) {
            difficultyButtons[i] = new Rectangle(buttonX, startY - i * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight);
        }

        this.jumpyBirb = jumpyBirb;

        this.backgroundImage = new Texture("background.jpg");
        this.pillarImage = new Texture("brick_pillar_long.png");
        // original image path was not an animation.
        this.bird = new Bird("birbsheet.png");

        this.bird.setSize();

        float birdX = SCREEN_CENTER_X - bird.getBounds().width / 2;
        float birdY = SCREEN_CENTER_Y - bird.getBounds().height / 2;
        bird.setPosition(birdX, birdY);

        this.overPillars = new Array<>();
        this.underPillars = new Array<>();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.bird.updateAnimations(Gdx.graphics.getDeltaTime());

        jumpyBirb.getCamera().update();
        jumpyBirb.getBatch().setProjectionMatrix(jumpyBirb.getCamera().combined);

        timeSinceLastHit += Gdx.graphics.getDeltaTime();
        pillarCollision(overPillars, -5.5f);
        pillarCollision(underPillars, 4.5f);
        groundCollision();

        if (!birdHasCollided) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                bird.jump();
            }
        }
        if (bird.getYposition() > 740) {
            birdFalling();
        }

        run();
        movePillars(underPillars, delta);
        movePillars(overPillars, delta);

        addPointWhenPassingPillar();

        extraLife();

        ScreenUtils.clear(0, 0, 0, 1);
        jumpyBirb.getBatch().begin();

        jumpyBirb.getBatch().draw(backgroundImage, 0, 0, 1280, 720);

        for (Pillar pillar : underPillars) {
            pillar.draw(jumpyBirb.getBatch());
        }
        for (Pillar pillar : overPillars) {
            jumpyBirb.getBatch().draw(pillarImage, pillar.getBounds().x, pillar.getBounds().y + pillar.getBounds().height, pillar.getBounds().width, -pillar.getBounds().height);
        }

        bird.draw(jumpyBirb.getBatch());

        for (int i = 0; i < difficultyButtons.length; i++) {
            jumpyBirb.getBatch().draw(difficultyButtonsTexture, difficultyButtons[i].x,
                    difficultyButtons[i].y, difficultyButtons[i].width, difficultyButtons[i].height);
            fontSmall.draw(jumpyBirb.getBatch(), difficultyButtonNames[i], difficultyButtons[i].x + 10, difficultyButtons[i].y + 30);
        }

        jumpyBirb.getBatch().end();
    }

    private void pillarCollision(Array<Pillar> pillars, float velocityFactor) {
        for (int i = 0; i < pillars.size; i++) {
            if (bird.getBounds().overlaps(pillars.get(i).getBounds()) && timeSinceLastHit > 0.1f && !birdHasCollided) {
                timeSinceLastHit = 0f;
                if (bird.getExtraLife() == 1) {
                    bird.setVelocity(velocityFactor); //-5.5 och 4.5
                    bird.setExtraLife(0);
                } else if (bird.getExtraLife() == 0) {
                    birdFalling();
                }
            }
        }
    }

    private void birdFalling() {
        birdHasCollided = true;
        bird.setVelocity(0);
        bird.setGravity(-1.0f);
    }

    private void groundCollision() {
        if (bird.getYposition() < 0) {
            bird.setVelocity(0);
            gameOver();
        }
    }

    private void movePillars(Array<Pillar> pillars, float deltaTime) {
        Iterator<Pillar> iter = pillars.iterator();
        while (iter.hasNext() && movingPillarsEnabled) {
            Pillar pillar = iter.next();
            pillar.update(deltaTime, jumpyBirb.getScore());
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
            if (difficultyFactor != 0) {
                bird.setGravityEnabled(true);  //this toggles gravity on from being off.
                movingPillarsEnabled = !movingPillarsEnabled;     //this toggles the "pillars" from moving left.
                timeSinceLastSpawn = spawnInterval;
            }
        }
        if (movingPillarsEnabled) {

            timeSinceLastSpawn += Gdx.graphics.getDeltaTime();

            if (timeSinceLastSpawn >= spawnInterval) {

                spawnPillars();
                decreaseSpawnInterval();
                timeSinceLastSpawn = 0;

            }
        }
    }

    private void decreaseSpawnInterval(){
        if(difficultyFactor == 1){
            spawnInterval *= 0.99f;
        }
        if(difficultyFactor == 1.05f){
            spawnInterval *= 0.98f;
        }
        else if(difficultyFactor == 1.10f){
            spawnInterval *= 0.97f;
        }

    }


    private void gameOver() {
        birdHasCollided = false;
        bird.setGravity(-0.5f);
        bird.setExtraLife(1);
        movingPillarsEnabled = false;
        bird.setGravityEnabled(false);
        spawnInterval = 2;
        n = 2;
        nextSpawnInterval = 2;

        jumpyBirb.setGameOver();
    }

    private void spawnPillars() {
        int randomRange = 360;

        Random random = new Random();
        int randomNumber = random.nextInt(51) - 25;
        //float spaceBetweenPillars = 450 * (2 - difficultyFactor);
        float spaceBetweenPillars = (450 + randomNumber) * (2 - difficultyFactor);
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
    public void dispose() {
        backgroundImage.dispose();
        pillarImage.dispose();
        bird.dispose();
        difficultyButtonsTexture.dispose();
        fontSmall.dispose();
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPoint = new Vector3(screenX, screenY, 0);
        jumpyBirb.getCamera().unproject(touchPoint);

        for (int i = 0; i < difficultyButtons.length; i++) {
            if (difficultyButtons[i].contains(touchPoint.x, touchPoint.y)) {
                updateDifficultyFactor(i);
                currentDifficulty = difficultyButtonNames[i];
                handleButtonClick(i);
                return true;
            }
        }
        return false;
    }

    private void handleButtonClick(int clickedButtonIndex) {
        // Remove all buttons
        difficultyButtons = new Rectangle[0];
    }

    private void updateDifficultyFactor(int i) {
        switch (i) {
            case 0:
                difficultyFactor = 1;
                break;
            case 1:
                difficultyFactor = 1.05f;
                break;
            case 2:
                difficultyFactor = 1.1f;
                break;
        }
    }
}
