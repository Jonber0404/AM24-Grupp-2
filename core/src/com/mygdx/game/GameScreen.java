package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.sun.tools.javac.Main;
import org.w3c.dom.Text;

import javax.management.MBeanAttributeInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameScreen implements Screen {

    private static final int SCREEN_CENTER_X = 1280 / 2;
    private static final int SCREEN_CENTER_Y = 720 / 2;
    private final BitmapFont fontSmall;
    private final BitmapFont scoreFont;
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
    private final Sound jumpSound;

    private final Texture difficultyButtonsTexture;
    private Rectangle[] difficultyButtons;
    private final String[] difficultyButtonNames = {"EASY", "NORMAL", "HARD"};
    public static String currentDifficulty;
    private float difficultyFactor = 0;
    private boolean birdHasCollided = false;

    float buttonWidth = 150;
    float buttonHeight = 50;
    float buttonSpacing = 10;
    float totalHeight = (buttonHeight + buttonSpacing) * 1;
    float startY = (SCREEN_CENTER_Y - totalHeight) / 2 + 80;
    float buttonX = SCREEN_CENTER_X - buttonWidth / 2;

    private Texture arrowTexture;
    private Rectangle arrow;
    private int arrowValue = 2; //Mellan 1 och 3, 1 lätt 2 normal 3 svårt
    private boolean difficultyChosen = false;

    float nextSpawnInterval = 2;
    float n = 2;

    private int pillarHeight = 50; //50 mitten, 250 top, -150 bottom
    List<Grass> grassList = new ArrayList<>();
    Texture grassTexture = new Texture("grass.png");

    //MENU STUFF
    public enum GameState {
        MAIN, HIGHSCORES, DIFFICULTYSELECTION, ABOUTTOPLAY, PLAYING, PAUSED, GAMEOVER;
    }

    GameState gameState = GameState.MAIN;

    private Texture mainMenuButtonTexture;
    private Rectangle[] mainMenuButtons;
    private final String[] mainMenuButtonNames = {"START", "HIGHSCORES", "QUIT"};


    public GameScreen(JumpyBirb jumpyBirb) {

        this.mainMenuButtonTexture = new Texture("difficultybutton.png"); //MIGHT NEED TO CHANGE LATER
        mainMenuButtons = new Rectangle[3];
        this.difficultyButtonsTexture = new Texture("difficultybutton.png");
        difficultyButtons = new Rectangle[3];

        fontSmall = TextUtil.generate("ARCADECLASSIC.TTF", 40, Color.WHITE, 2, Color.BLUE);
        scoreFont = TextUtil.generate("ARCADECLASSIC.TTF", 75, Color.WHITE, 3, Color.BLUE);
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump_sound_1.mp3"));
        this.arrowTexture = new Texture("Arrow.png");


        // Set positions for difficulty buttons
     /*   for (int i = 0; i < difficultyButtons.length; i++) {
            difficultyButtons[i] = new Rectangle(buttonX, startY - i * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight);
        }
*/      //Positions for main menu buttons
        for (int i = 0; i < mainMenuButtons.length; i++) {
            mainMenuButtons[i] = new Rectangle(buttonX, startY - i * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight);
        }
        //Arrow start position
        arrow = new Rectangle(mainMenuButtons[1].x - 200, mainMenuButtons[1].y, 150, 50);


        this.jumpyBirb = jumpyBirb;

        this.backgroundImage = new Texture("background.jpg");
        // original image path was not an animation.
        this.bird = new Bird("birbsheet.png");

        this.bird.setSize();

        float birdX = SCREEN_CENTER_X - bird.getBounds().width / 2;
        float birdY = SCREEN_CENTER_Y - bird.getBounds().height / 2;
        bird.setPosition(birdX, birdY);

        this.overPillars = new Array<>();
        this.underPillars = new Array<>();

        //   GRÄS
        for (float i = 0; i < Gdx.graphics.getWidth() / grassTexture.getWidth() + 2; i += 0.5) {
            grassList.add(new Grass(i * grassTexture.getWidth(), 0, 100, grassTexture)); // Y-positionen kan ändras beroende på var du vill placera gräset
        }
        // GRÄS

    }

    @Override
    public void render(float delta) {


        // Set positions for difficulty buttons
        if (gameState == GameState.DIFFICULTYSELECTION) {
            for (int i = 0; i < difficultyButtons.length; i++) {
                difficultyButtons[i] = new Rectangle(buttonX, startY - i * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight);
            }
        }
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.bird.updateAnimations(Gdx.graphics.getDeltaTime());

        // GRÄS --------------------
        float deltaTime = Gdx.graphics.getDeltaTime();
        for (Grass grass : grassList) {
            grass.update(deltaTime);
        }
        // GRÄS ------------------

        jumpyBirb.getCamera().update();
        jumpyBirb.getBatch().setProjectionMatrix(jumpyBirb.getCamera().combined);

        timeSinceLastHit += Gdx.graphics.getDeltaTime();
        pillarCollision(overPillars, -5.5f);
        pillarCollision(underPillars, 4.5f);
        groundCollision();

        if (!birdHasCollided) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                //Change later to Gamestate ABOUTTOPLAY
                bird.jump();
                jumpSound.play();
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
            jumpyBirb.getBatch().draw(pillar.getImage(), pillar.getBounds().x, pillar.getBounds().y + pillar.getBounds().height, pillar.getBounds().width, -pillar.getBounds().height);
        }

        bird.draw(jumpyBirb.getBatch());

        if (gameState == GameState.DIFFICULTYSELECTION) {

                for (int i = 0; i < difficultyButtons.length; i++) {
                    if (difficultyButtons[i] != null) {
                        jumpyBirb.getBatch().draw(difficultyButtonsTexture, difficultyButtons[i].x,
                                difficultyButtons[i].y, difficultyButtons[i].width, difficultyButtons[i].height);
                        fontSmall.draw(jumpyBirb.getBatch(), difficultyButtonNames[i], difficultyButtons[i].x + 10, difficultyButtons[i].y + 30);
                    }
                }
        }

        if (gameState == GameState.MAIN)
            for (int i = 0; i < mainMenuButtons.length; i++) {
                jumpyBirb.getBatch().draw(mainMenuButtonTexture, mainMenuButtons[i].x,
                        mainMenuButtons[i].y, mainMenuButtons[i].width, mainMenuButtons[i].height);
                fontSmall.draw(jumpyBirb.getBatch(), mainMenuButtonNames[i], mainMenuButtons[i].x + 10, mainMenuButtons[i].y + 30);
            }

        jumpyBirb.getBatch().draw(arrowTexture, arrow.x, arrow.y, arrow.width, arrow.height);

        fontSmall.draw(jumpyBirb.getBatch(), "SCORE", 30f, 680);
        scoreFont.draw(jumpyBirb.getBatch(), String.valueOf(jumpyBirb.getScore()), 30f, 640f);

        // GRÄS ------------------
        for (Grass grass : grassList) {
            grass.draw(jumpyBirb.getBatch());
        }
        // GRÄS ------------------

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


            if (difficultyFactor == 1.05f) {//normal
                if (jumpyBirb.getScore() < 40) {
                    pillar.update(deltaTime, jumpyBirb.getScore());
                } else {
                    pillar.update(deltaTime, 40 + (jumpyBirb.getScore()) / 10);
                }

            }
            if (difficultyFactor == 1f) {//easy
                if (jumpyBirb.getScore() < 40) {
                    pillar.update(deltaTime, jumpyBirb.getScore());
                } else {
                    pillar.update(deltaTime, 40 + (jumpyBirb.getScore()) / 20);
                }

            } else {
                pillar.update(deltaTime, jumpyBirb.getScore());
            }
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
                if (timeSinceLastPoint >= pointInterval && !birdHasCollided) {
                    jumpyBirb.updateScore();
                    timeSinceLastPoint = 0.0f;
                }
            }
        }
    }

    private void run() {
        if (bird.getIsGravityEnabled()) {
            bird.gravity();
        } else if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) && gameState == GameState.ABOUTTOPLAY) {
            if (difficultyFactor != 0) {
                gameState = GameState.PLAYING;
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
        } else {
            Pillar.setPillarSpeed(3.33f);
        }
        if (!difficultyChosen) {
            moveArrow();
        }
    }

    private void moveArrow() {

        if (arrowValue == 1 || arrowValue == 2) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                arrowValue++;
                System.out.println(arrowValue);
            }
        }
        if (arrowValue == 2 || arrowValue == 3) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                arrowValue--;
                System.out.println(arrowValue);
            }
        }


        switch (arrowValue) {
            case 1:
                arrow.y = mainMenuButtons[0].y;
                break;
            case 2:
                arrow.y = mainMenuButtons[1].y;
                break;
            case 3:
                arrow.y = mainMenuButtons[2].y;
                break;
            default:
                break;
        }
        if (gameState == GameState.DIFFICULTYSELECTION) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Buttons.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                switch (arrowValue) {
                    case 1:
                        updateDifficultyFactor(0);
                        currentDifficulty = difficultyButtonNames[0];
                        gameState = GameState.ABOUTTOPLAY;
                        break;
                    case 2:
                        updateDifficultyFactor(1);
                        currentDifficulty = difficultyButtonNames[1];
                        gameState = GameState.ABOUTTOPLAY;
                        break;
                    case 3:
                        updateDifficultyFactor(2);
                        currentDifficulty = difficultyButtonNames[2];
                        gameState = GameState.ABOUTTOPLAY;
                        break;
                    default:
                        break;
                }
                removeButtons(difficultyButtons);
                difficultyChosen = true;
                arrow = new Rectangle();
            }
        }

        if (gameState == GameState.MAIN) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
                    Gdx.input.isKeyJustPressed(Input.Buttons.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                switch (arrowValue) {
                    case 1:
                        System.out.println("Choose diff");
                        gameState = GameState.DIFFICULTYSELECTION;
                        break;
                    case 2:
                        System.out.println("See highscores");
                        gameState = GameState.HIGHSCORES;
                        break;
                    case 3:
                        System.out.println("Close game");
                        dispose();
                        Gdx.app.exit();
                        break;
                    default:
                        break;

                }

                removeButtons(mainMenuButtons);
            }
        }


    }

    private void decreaseSpawnInterval() {

        if (difficultyFactor == 1) { //EASY
            if (jumpyBirb.getScore() > 45) {
                spawnInterval *= 1.000001f;
            } else {
                spawnInterval *= 0.99f;
            }
        }
        if (difficultyFactor == 1.05f) { //NORMAL

            if (jumpyBirb.getScore() > 40) {
                spawnInterval *= 1.000001f;
            } else {
                spawnInterval *= 0.98f;
            }

        } else if (difficultyFactor == 1.10f) { //HARD
            if (jumpyBirb.getScore() < 26) {
                spawnInterval *= 0.97f;
            } else if (jumpyBirb.getScore() < 60) {
                spawnInterval *= 1.0001f;
            } else {
                spawnInterval *= 0.999f;
            }

        }

    }


    private void gameOver() {
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

        Random random = new Random();
        int randomNumber1 = random.nextInt(51) - 25; //-25 till 25
        int randomNumber2 = random.nextInt(10) + 5; //5 till 20
        float spaceBetweenPillars;

        if (jumpyBirb.getScore() < 15) {
            spaceBetweenPillars = (450 + randomNumber2) * (2 - difficultyFactor);
        } else {
            spaceBetweenPillars = (450 + randomNumber1) * (2 - difficultyFactor);
        }

        int oldPillarHeight = pillarHeight;
        while (true) {
            pillarHeight += random.nextInt(250) - 125; //-125 till 125

            if (pillarHeight < 250 && pillarHeight > -150 && (pillarHeight > oldPillarHeight + 30 || pillarHeight < oldPillarHeight - 30)) { //Ser till att nästa pelare skiljer minst 30 i höjd från den förra
                break;
            }
            pillarHeight = oldPillarHeight;

        }


        Pillar pillarUnder = new Pillar
                (1280, pillarHeight - spaceBetweenPillars);

        underPillars.add(pillarUnder);

        Pillar pillarOver = new Pillar
                (1280, pillarHeight + spaceBetweenPillars);

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
        birdHasCollided = false;
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
        bird.dispose();
        difficultyButtonsTexture.dispose();
        mainMenuButtonTexture.dispose();
        arrowTexture.dispose();
        fontSmall.dispose();
        scoreFont.dispose();
        jumpSound.dispose();
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPoint = new Vector3(screenX, screenY, 0);
        jumpyBirb.getCamera().unproject(touchPoint);

        if (gameState == GameState.DIFFICULTYSELECTION) {
            for (int i = 0; i < difficultyButtons.length; i++) {
                if (difficultyButtons[i].contains(touchPoint.x, touchPoint.y)) {
                    System.out.println("Pressed on a difficulty button");
                    difficultyChosen = true;

                    updateDifficultyFactor(i);
                    currentDifficulty = difficultyButtonNames[i];
                    removeButtons(difficultyButtons);
                    gameState = GameState.ABOUTTOPLAY;
                    arrow = new Rectangle(); //Deletes arrow
                    return true;
                }
            }
        }
        if (gameState == GameState.MAIN) {
            for (int i = 0; i < mainMenuButtons.length; i++) {
                if (mainMenuButtons[i].contains(touchPoint.x, touchPoint.y)) {
                    if (gameState == GameState.MAIN) {
                        switch (i) {
                            case 0:
                                System.out.println("Choose diff");
                                gameState = GameState.DIFFICULTYSELECTION;
                                break;
                            case 1:
                                System.out.println("See highscores");
                                gameState = GameState.HIGHSCORES;
                                break;
                            case 2:
                                System.out.println("Close game");
                                dispose();
                                Gdx.app.exit();
                                break;
                            default:
                                break;

                        }

                        removeButtons(mainMenuButtons);
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void removeButtons(Rectangle[] buttons) {
        // Remove all buttons
        buttons = new Rectangle[0];
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
