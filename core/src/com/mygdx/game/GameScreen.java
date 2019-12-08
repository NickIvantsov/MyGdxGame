package com.mygdx.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Теперь у нас пустой экран, но давайте заставим наш экран что-то делать!
 * Для начала давайте поместим на экран код проекта по умолчанию - красивый логотип LibGDX.
 */
class GameScreen extends ScreenAdapter {
    /**
     * batch class
     * Далее нам нужно что-то, что будет рисовать наши текстуры;
     * это - то, где классы пакета входят. Пакет используется,
     * чтобы нарисовать 2D прямоугольники, которые ссылаются на текстуру.
     * Среди различных реализаций класса Batch есть SpriteBatch,
     * который мы будем использовать для нашей игры.
     */
    private SpriteBatch batch;
    private static int SNAKE_MOVEMENT = 32;
    private int snakeX = 0, snakeY = 0;
    private int leftBtnX = 300, leftBtnY = 75;
    private int rightBtnX = 600, rightBtnY = 75;
    private int downBtnX = 450, downBtnY = 0;
    private int upBtnX = 445, upBtnY = 160;

    private static int GRID_CELL = 32;
    private int screenWidth;
    private int screenHeigth;
    private STATE state = STATE.PLAYING;
    private BitmapFont bitmapFont;
    private BitmapFont mBitmapFont2;
    private BitmapFont mBitmapFontPause;

    // private static final float WORLD_WIDTH = 1280;
    //  private static final float WORLD_HEIGHT = 960;
    private static float WORLD_WIDTH = 1920;
    private static float WORLD_HEIGHT = 1080;

    private Viewport viewport;
    private Camera camera;

    /**
     * Давайте определим наш таймер. Мы собираемся начать с интервалом в одну секунду между
     * движениями. Итак, давайте создадим постоянное поле:
     */
    private static float MOVE_TIME = 0.3F;
    /**
     * Теперь определите переменную для отслеживания времени:
     */
    private float timer = MOVE_TIME;
    /**
     * Класс текстуры. Текстура - это растровое изображение, которое отображается на
     * экране посредством отображения. Класс текстуры оборачивает текстуру OpenGL,
     * не беспокоясь о внутренней работе OpenGL - помните, что мы здесь, чтобы создавать игры!
     * При использовании класса текстуры мы должны обеспечить управление нашими текстурами;
     * под этим мы подразумеваем, что, если, например, контекст OpenGL потерян, такая потеря
     * может произойти при переключении пользователя на другое приложение, тогда наши управляемые
     * текстуры будут автоматически перезагружены для нас. Превосходно!
     */
    private Texture snakeHead;
    private Texture leftBtn;
    private Texture downBtn;
    private Texture upBtn;
    private Texture rightBtn;
    private Texture pauseBtn;
    private Texture apple;
    private Texture snakeBody;
    private int snakeXBeforeUpdate = 0, snakeYBeforeUpdate = 0;

    private boolean appleAvailable = false;
    private boolean directionSet = false;
    private int appleX, appleY;
    private GlyphLayout layout = new GlyphLayout();

    private static final String GAME_OVER_TEXT = "Game Over... Tap space to restart!";
    private static final String PAUSE_TEXT = "Pause... Tap pause to continue!";


    private Drawable leftDrawable;
    private Drawable downDrawable;
    private Drawable upDrawable;
    private Drawable rightDrawable;
    private Drawable pauseDrawable;

    private ImageButton upButton;
    private ImageButton downButton;
    private ImageButton rightButton;
    private ImageButton leftButton;
    private ImageButton pauseButton;
    private boolean hasHit = false;
    private boolean isTouch = false;
    private boolean isPaused = false;
    private boolean isFirstStart = true;
    private long idSnakehiss0_ssio3rdw;
    private long idSnakehiss_1235;


    private Stage stage;

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private int snakeDirection = RIGHT;

    private Array<BodyPart> bodyParts = new Array<BodyPart>();

    ShapeRenderer shapeRenderer;
    ShapeRenderer shapeRenderer2;
    ShapeRenderer snakeHeadShapeRenderer;

    Sound snakehiss0_ssio3rdw;
    Sound snakehiss_1235;
    private static final int POINTS_PER_APPLE = 20;
    private int score = 0;

    private void addToScore() {
        score += POINTS_PER_APPLE;
    }

    /**
     * Вы заметите, что у нас нет метода create () для инициализации объекта.
     * Однако у нас есть метод show ().
     * Давайте поместим это там. Так что теперь ваш класс GameScreen должен выглядеть примерно так
     */
    @Override
    public void show() {
        snakehiss0_ssio3rdw = Gdx.audio.newSound(Gdx.files.internal("data/snakehiss0_ssio3rdw.mp3"));
        snakehiss_1235 = Gdx.audio.newSound(Gdx.files.internal("data/snakehiss_1235.mp3"));

        WORLD_WIDTH = Gdx.graphics.getWidth();
        WORLD_HEIGHT = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        bitmapFont = new BitmapFont();
        bitmapFont.getData().setScale(4, 4);
        mBitmapFont2 = new BitmapFont();
        mBitmapFont2.getData().setScale(2, 2);
        mBitmapFontPause = new BitmapFont();
        mBitmapFontPause.getData().setScale(2, 2);
        // layout.setText(bitmapFont, text);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer2 = new ShapeRenderer();
        snakeHeadShapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            /*WORLD_WIDTH = 1920;
            WORLD_HEIGHT = 1080;*/


            leftBtn = new Texture(Gdx.files.internal("left.png"));
            rightBtn = new Texture(Gdx.files.internal("right.png"));
            downBtn = new Texture(Gdx.files.internal("down.png"));
            upBtn = new Texture(Gdx.files.internal("up.png"));
            pauseBtn = new Texture(Gdx.files.internal("pause.png"));
           /* apple = new Texture(Gdx.files.internal("apple.png"));
           snakeHead = new Texture(Gdx.files.internal("snakehead3.png"));
            snakeBody = new Texture(Gdx.files.internal("snakeBody.png"));*/

            downDrawable = new TextureRegionDrawable(new TextureRegion(downBtn));
            upDrawable = new TextureRegionDrawable(new TextureRegion(upBtn));
            leftDrawable = new TextureRegionDrawable(new TextureRegion(leftBtn));
            rightDrawable = new TextureRegionDrawable(new TextureRegion(rightBtn));
            pauseDrawable = new TextureRegionDrawable(new TextureRegion(pauseBtn));

            stage = new Stage(new ScreenViewport()); //Set up a stage for the ui


            // ImageButton
            upButton = getImageButton(upDrawable, upBtnX, upBtnY, UP, "up.png", "up_pressed.png");
            stage.addActor(upButton);

            downButton = getImageButton(downDrawable, downBtnX, downBtnY, DOWN, "down.png", "down_pressed.png");
            stage.addActor(downButton);

            rightButton = getImageButton(rightDrawable, rightBtnX, rightBtnY, RIGHT, "right.png", "right_pressed.png");
            stage.addActor(rightButton);

            leftButton = getImageButton(leftDrawable, leftBtnX, leftBtnY, LEFT, "left.png", "left_pressed.png");
            stage.addActor(leftButton);

            pauseButton = getImageButton(pauseDrawable, (int) WORLD_WIDTH - 150, (int) WORLD_HEIGHT - 120, "pause.png", "pause_pressed.png");
            stage.addActor(pauseButton);

        }


        snakeHead = new Texture(Gdx.files.internal("old/snakehead_1.png"));
        apple = new Texture(Gdx.files.internal("old/apple.png"));
        snakeBody = new Texture(Gdx.files.internal("old/snakeBody.png"));

        BodyPart bodyPart = new BodyPart(snakeBody);
        bodyPart.updateBodyPosition(snakeX, snakeY);
        BodyPart bodyPart2 = new BodyPart(snakeBody);
        bodyPart.updateBodyPosition(snakeX, snakeY);
        bodyParts.insert(0, bodyPart);
        bodyParts.insert(1, bodyPart2);


        screenWidth = (int) viewport.getWorldWidth();
        screenHeigth = (int) viewport.getWorldHeight();

        System.out.println("screenWidth = " + screenWidth + " screenHeigth = " + screenHeigth);

        Gdx.input.setInputProcessor(stage);
    }

    /***
     * этот метод создан для того чтоб производить кнопки связаные с управлением змейки
     * @param drawable
     * @param x
     * @param y
     * @param direction
     * @param imageUp
     * @param imageDown
     * @return
     */
    private ImageButton getImageButton(final Drawable drawable, final int x, final int y,
                                       final int direction, String imageUp, String imageDown) {
        ImageButton leftButton = new ImageButton(drawable);
        leftButton.setSize(200, 200);
        leftButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(imageUp))));
        leftButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(imageDown))));
        leftButton.setPosition(x, y);
        leftButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                System.out.println("touch (touchUp()) event: " + event.getListenerActor() + " x: " + x + " y: " + y);
                MOVE_TIME = 0.3F;
                isTouch = false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touch (touchDown()) event: " + event.getListenerActor() + " x: " + x + " y: " + y);
                //snakeDirection = direction;
                updateDirection(direction);
                Gdx.input.vibrate(50);
                isTouch = true;
                return true;
            }
        });
        return leftButton;
    }

    /**
     * этот метод предназначен для создания кнопик только без изменения направления змейки
     * его я импользовал для создания кнопки паузы
     * @param drawable
     * @param x
     * @param y
     * @param imageUp
     * @param imageDown
     * @return
     */
    private ImageButton getImageButton(final Drawable drawable, final int x, final int y, String imageUp, String imageDown) {
        ImageButton leftButton = new ImageButton(drawable);
        leftButton.setSize(100, 100);
        leftButton.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(imageUp))));
        leftButton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(imageDown))));
        leftButton.setPosition(x, y);
        leftButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                System.out.println("touch (touchUp()) event: " + event.getListenerActor() + " x: " + x + " y: " + y);
                MOVE_TIME = 0.3F;
                isTouch = false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("touch (touchDown()) event: " + event.getListenerActor() + " x: " + x + " y: " + y);
                System.out.println("touch (touchDown()) event: " + event.getListenerActor() + " isPaused = " + isPaused);
                if (!isPaused) {
                    state = STATE.PAUSED;
                    isPaused = true;
                } else {
                    state = STATE.PLAYING;
                    isPaused = false;
                }

                Gdx.input.vibrate(50);
                isTouch = true;
                return true;
            }
        });
        return leftButton;
    }

    /**
     * метод который проверяет не натолкнулась ли змейка на свое тело
     */
    private void checkSnakeBodyCollision() {
        for (BodyPart bodyPart : bodyParts) {
            if (bodyPart.x == snakeX && bodyPart.y == snakeY) state = STATE.GAME_OVER;
        }
    }

    /**
     * этот метод можно использоваеть для того чтоб нарисовать сетку для дэбага
     */
    private void drawGrid() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x < (int) viewport.getWorldWidth(); x += GRID_CELL) {
            for (int y = 0; y < (int) viewport.getWorldHeight(); y += GRID_CELL) {
                shapeRenderer.rect(x, y, GRID_CELL, GRID_CELL);
            }
        }
        shapeRenderer.end();
    }

/*    private void drawFilled() {
        shapeRenderer2.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer2.rect(500, 300, GRID_CELL, GRID_CELL);
        shapeRenderer2.setColor(Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a);
        shapeRenderer2.end();
    }

    private void drawFilledCircly() {
        snakeHeadShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        snakeHeadShapeRenderer.circle(snakeX, snakeY, 20);
        snakeHeadShapeRenderer.setColor(Color.RED.r, Color.RED.g, Color.RED.b, Color.RED.a);
        snakeHeadShapeRenderer.end();
    }*/

    @Override
    public void render(final float delta) {

        switch (state) {
            case PLAYING: {
                if (isFirstStart) {
                    idSnakehiss_1235 = snakehiss_1235.play(1.0f);
                   // idSnakehiss0_ssio3rdw = snakehiss0_ssio3rdw.play(1.0f);
                    isFirstStart = false;
                }
                queryInput();
                updateSnake(delta);
                checkAppleCollision();
                checkAndPlaceApple();
                upButton.setVisible(true);
                downButton.setVisible(true);
                leftButton.setVisible(true);
                rightButton.setVisible(true);
                if (isTouch) {

                    if (MOVE_TIME < 0.1f) {
                        MOVE_TIME = 0.1f;
                    } else {
                        MOVE_TIME -= 0.03f;
                    }
                }
            }
            break;
            case GAME_OVER: {
                snakehiss_1235.stop(idSnakehiss_1235);
                snakehiss0_ssio3rdw.stop(idSnakehiss0_ssio3rdw);
                checkForRestart();
                upButton.setVisible(false);
                downButton.setVisible(false);
                leftButton.setVisible(false);
                rightButton.setVisible(false);
                isFirstStart = true;

            }
            break;
            case PAUSED:
                // checkForRestart();
                upButton.setVisible(false);
                downButton.setVisible(false);
                leftButton.setVisible(false);
                rightButton.setVisible(false);
                break;
        }
        //Наконец, давайте обновим его в каждом кадре.
        //queryInput();
        // updateSnake(delta);
        // queryInput();
        //checkAndPlaceApple();
        //checkAppleCollision();
        clearScreen();
        //drawGrid();
        // drawFilled();
        //drawFilledCircly();
        draw();

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            stage.act(delta);
            stage.draw();
        }


    }

    /**
     * метод который перезапускает игру
     */
    private void checkForRestart() {
        if (Gdx.app.getType() == Application.ApplicationType.Android) {

            if (Gdx.input.isTouched()) doRestart();
        } else {

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) doRestart();
        }
    }

    /**
     * метод обнуляет все данные об игре
     */
    private void doRestart() {
        state = STATE.PLAYING;
        bodyParts.clear();
        snakeDirection = RIGHT;
        directionSet = false;
        timer = MOVE_TIME;
        snakeX = 0;
        snakeY = 0;
        snakeXBeforeUpdate = 0;
        snakeYBeforeUpdate = 0;
        appleAvailable = false;
        score = 0;
        upButton.setVisible(true);
        downButton.setVisible(true);
        leftButton.setVisible(true);
        rightButton.setVisible(true);
    }

    /**
     * метод очистки экрана от старых картинок
     */
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * метод который содержит все необходимые компоненты которые будут отрисовываться на экране
     */
    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        // bitmapFont.draw(batch, "This Snake Game is AWESOME!", 0, 0);
        //  bitmapFont.draw(batch, text, 0, layout.height);

        //Our rending code will live here!
        //Так что теперь каждый раз, когда таймер достигает нуля или ниже, змея будет двигаться вправо на 92 пикселя.
        batch.draw(snakeHead, snakeX, snakeY);
        for (BodyPart bodyPart : bodyParts) {
            bodyPart.draw(batch);
        }
        if (appleAvailable) {
            batch.draw(apple, appleX, appleY);
        }
        drawScore();
        drawPause();
        batch.end();
    }


    /**
     * Все, о чем мы говорили до сих пор, где-то во внутренней работе LibGDX, касается реальных
     * ресурсов, таких как память. Чтобы наши игры хорошо себя вели и не использовали всю
     * доступную системную память, мы должны помнить о том, чтобы утилизировать наш
     * SpriteBatch и текстуры, когда мы их закончили. Вы заметите, что все они имеют метод
     * dispose (). При вызове это освободит все ресурсы, связанные с этим объектом. Класс Screen
     * имеет метод dispose (). Мы рассмотрим это позже.
     */
    @Override
    public void dispose() {
        batch.dispose();
        snakeHead.dispose();
        leftBtn.dispose();
        downBtn.dispose();
        upBtn.dispose();
        rightBtn.dispose();
    }

    /**
     * метод размещает рандомно яблоко на поле
     */
    private void checkAndPlaceApple() {
        if (!appleAvailable) {
            do {
                appleX = MathUtils.random((int) (viewport.getWorldWidth() / SNAKE_MOVEMENT) - 1) * SNAKE_MOVEMENT;
                appleY = MathUtils.random((int) (viewport.getWorldHeight() / SNAKE_MOVEMENT) - 1) * SNAKE_MOVEMENT;
                appleAvailable = true;
            } while (appleX == snakeX && appleY == snakeY);
        }
    }

    /**
     * метод который добавляет к телу еще одну чать когда змейка сьест яблоко
     */
    private void checkAppleCollision() {
        if (appleAvailable && appleX == snakeX && appleY == snakeY) {
            BodyPart bodyPart = new BodyPart(snakeBody);
            bodyPart.updateBodyPosition(snakeX, snakeY);
            bodyParts.insert(0, bodyPart);
            addToScore();
            appleAvailable = false;
            idSnakehiss0_ssio3rdw = snakehiss0_ssio3rdw.play(1.0f);
        }
    }

    /**
     * метод который исходя из состояния игры принимает решение что нарисовать
     * на экране сообщение об проиграше или же какой текущий счет игры
     */
    private void drawScore() {
        if (state == STATE.PLAYING) {

            float width = layout.width;// contains the width of the current set text
            float height = layout.height; // contains the height of the current set text
            String scoreAsString = Integer.toString(score);
            layout.setText(mBitmapFont2, scoreAsString);

            mBitmapFont2.draw(batch, scoreAsString,
                    (int) (viewport.getWorldWidth() - width) / 2,
                    ((int) (4 * viewport.getWorldHeight() / 5)) - height / 2);
        } else if (state == STATE.GAME_OVER) {
            layout.setText(bitmapFont, GAME_OVER_TEXT);
            bitmapFont.draw(batch, GAME_OVER_TEXT,
                    (int) (viewport.getWorldWidth() - layout.width) / 2,
                    (int) (viewport.getWorldHeight() - layout.height) / 2);
        }
    }

    /**
     * метод который рисует сообщение о том что игра приостановлена и требуеться возобновить её
     */
    private void drawPause() {
        if (state == STATE.PAUSED) {
            float width = layout.width;// contains the width of the current set text
            float height = layout.height; // contains the height of the current set text
            layout.setText(mBitmapFontPause, PAUSE_TEXT);

            mBitmapFontPause.draw(batch, PAUSE_TEXT,
                    (int) (viewport.getWorldWidth() - width) / 2,
                    ((int) (4 * viewport.getWorldHeight() / 5)) - height / 2);
        }
    }

    /**
     * этот метод предоствращает прохождение замейки самой по себе
     * к примеру когда она двигаеться на право он не может идти на лево но может идти вниз и вверх
     * @param newSnakeDirection
     * @param oppositeDirection
     */
    private void updateIfNotOppositeDirection(int newSnakeDirection, int oppositeDirection) {
        if (snakeDirection != oppositeDirection) snakeDirection = newSnakeDirection;
    }

    /**
     * собственно здесь происходит магия с определением направления движения
     * @param newSnakeDirection
     */
    private void updateDirection(int newSnakeDirection) {
        if (!directionSet && snakeDirection != newSnakeDirection) {
            directionSet = true;
            switch (newSnakeDirection) {
                case LEFT: {
                    updateIfNotOppositeDirection(newSnakeDirection, RIGHT);
                }
                break;
                case RIGHT: {
                    updateIfNotOppositeDirection(newSnakeDirection, LEFT);
                }
                break;
                case UP: {
                    updateIfNotOppositeDirection(newSnakeDirection, DOWN);
                }
                break;
                case DOWN: {
                    updateIfNotOppositeDirection(newSnakeDirection, UP);
                }
                break;
            }
        }
    }

    /**
     * Давайте создадим метод, чтобы проверить положение змеи
     */
    private void checkForOutOfBounds() {
        if (snakeX >= (int) viewport.getWorldWidth()) {
            snakeX = 0;
        }
        if (snakeX < 0) {
            snakeX = (int) (viewport.getWorldWidth() - SNAKE_MOVEMENT);
        }
        if (snakeY >= (int) viewport.getWorldHeight()) {
            snakeY = 0;
        }
        if (snakeY < 0) {
            snakeY = (int) viewport.getWorldHeight() - SNAKE_MOVEMENT;
        }
    }

    /**
     * Добавляя некоторые константы и переменную, мы можем отслеживать направление змеи.
     * Теперь, когда у нас есть направление, мы должны быть более точными в том, как мы хотим
     * двигать змею. Заменим текущий код для перемещения следующим методом:
     */
    private void moveSnake() {
        snakeXBeforeUpdate = snakeX;
        snakeYBeforeUpdate = snakeY;
        switch (snakeDirection) {
            case RIGHT: {
            }
            snakeX += SNAKE_MOVEMENT;
            return;
            case LEFT: {
                snakeX -= SNAKE_MOVEMENT;
                return;
            }
            case UP: {
                snakeY += SNAKE_MOVEMENT;
                return;
            }
            case DOWN: {
                snakeY -= SNAKE_MOVEMENT;
                return;
            }
        }

    }

    /**
     * метод перемещает первый элемент массива в конец
     * тем самым имитируеться движениие змейки
     */
    private void updateBodyPartsPosition() {
        if (bodyParts.size > 0) {
            BodyPart bodyPart = bodyParts.removeIndex(0);
            bodyPart.updateBodyPosition(snakeXBeforeUpdate, snakeYBeforeUpdate);
            bodyParts.add(bodyPart);
        }
    }

    /**
     * этот метод предназначен для того чтоб замускайть и управлять змейкой на компе
     */
    private void queryInput() {
        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);
       /* if (lPressed) snakeDirection = LEFT;
        if (rPressed) snakeDirection = RIGHT;
        if (uPressed) snakeDirection = UP;
        if (dPressed) snakeDirection = DOWN;*/
        if (lPressed) updateDirection(LEFT);
        if (rPressed) updateDirection(RIGHT);
        if (uPressed) updateDirection(UP);
        if (dPressed) updateDirection(DOWN);
    }

    /**
     * класс тела змейки
     */
    public class BodyPart {
        /**
         * координаты
         */
        private int x, y;
        /**
         * текстура (изображение части тела)
         */
        private Texture texture;

        public BodyPart(Texture texture) {
            this.texture = texture;
        }

        public void updateBodyPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void draw(Batch batch) {
            if (!(x == snakeX && y == snakeY)) batch.draw(texture, x, y);
        }
    }

    private void updateSnake(float delta) {
        if (!hasHit) {     //Update snake code omitted
            timer -= delta;
            if (timer <= 0) {
                timer = MOVE_TIME;
                //snakeX += SNAKE_MOVEMENT;
                moveSnake();
                checkForOutOfBounds();
                updateBodyPartsPosition();
                checkSnakeBodyCollision();
                directionSet = false;
            }
        }
    }

    /**
     * состояния игры
     */
    private enum STATE {PLAYING, GAME_OVER, PAUSED, RESUME}

}