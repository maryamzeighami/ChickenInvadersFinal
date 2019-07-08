package GUI.gameplay;

import Controller.Constants;
import enums.BeamLevel;
import enums.KeyState;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Duration;
import models.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GameSceneBuilder {

    private Scene scene;
    static Timeline timeline;
    private javafx.scene.control.TableView<Player> table;
    StackPane stackPane;
    Random random = new Random();
    Text scoreBox = new Text();
    ImageView seed;
    ImageView egg;

    ArrayList<ImageView> stuff = new ArrayList<>();


    int score;
    int coins = 0;
    int heartNum = 5;
    int bomb = 3;

    // texts
    Text heartText = new Text();
    Text coinText = new Text();


    // key states
    KeyState spaceKey = KeyState.NOT_PRESSED;
    KeyState escapeKey = KeyState.NOT_PRESSED;

    // AttackSystem
    AttackSystem attackSystem = new AttackSystem();
    EnemySystem enemySystem = new EnemySystem();
    ArrayList<Chicken> chickens;
    int level = 1;
    int wave = 1;

    static Player currentPlayer;
    SpaceShip spaceShip;

    public GameSceneBuilder builder(Player player) {
        currentPlayer = player;

        // resuummmmmm
        if (currentPlayer.getCurrentGame() != null) {
            spaceShip = currentPlayer.getCurrentGame().spaceShip;
            chickens = currentPlayer.getCurrentGame().chickens;

            // TODO: 6/29/19 scores and ...
        }


        // --- set pictures ---

        // background
        String addr = System.getProperty("user.dir") + "/src/pics/back3.png";
        File file = new File(addr);
        Image background = new Image(file.toURI().toString());

        // space ship pic
        String addrs = System.getProperty("user.dir") + "/src/pics/spaceship.png";
        File file2 = new File(addrs);
        Image spaceShipImage = new Image(file2.toURI().toString());
        spaceShip = new SpaceShip(spaceShipImage);
        spaceShip.setFitHeight(130);
        spaceShip.setFitWidth(130);


        // heart pic
        String addrss = System.getProperty("user.dir") + "/src/pics/powerUp.png";
        File file3 = new File(addrss);
        Image heart1 = new Image(file3.toURI().toString());
        ImageView heart = new ImageView(heart1);
        heart.setFitHeight(40);
        heart.setFitWidth(40);

        // mega seed pic
        String miniMegaSeed = System.getProperty("user.dir") + "/src/pics/seed.png";
        File miniFile = new File(miniMegaSeed);
        Image mega1 = new Image(miniFile.toURI().toString());
        ImageView megaSeed = new ImageView(mega1);
        megaSeed.setFitHeight(40);
        megaSeed.setFitWidth(40);

        // green pic!
        String gAdres = System.getProperty("user.dir") + "/src/pics/green2.gif";
        File greenFile = new File(gAdres);
        Image greenImage = new Image(greenFile.toURI().toString());
        ImageView green = new ImageView(greenImage);
        green.setFitHeight(50);
        green.setFitWidth(50);

        // --- set info (health, score, ...) ---
        // heart VBox (health infoVBox actually!)
        HBox heartBox = new HBox();
        heartBox.setSpacing(5);
        heartText.setText(Integer.toString(heartNum));
        heartText.setStyle("-fx-font-size: 35; -fx-fill: white");
        coinText.setStyle("-fx-font-size: 35; -fx-fill: white");
        heartBox.getChildren().addAll(heart, heartText);

        // mega VBox
        HBox megaBox = new HBox();
        megaBox.setSpacing(5);

        coinText.setText(Integer.toString(coins));
        megaBox.getChildren().addAll(megaSeed, coinText);

        scoreBox.setText(Integer.toString(score));
        scoreBox.setStyle(" -fx-font-size: 80 ; -fx-fill: White");
        HBox sc = new HBox();
        sc.getChildren().addAll(scoreBox);
        sc.setAlignment(Pos.BOTTOM_LEFT);

        // green VBox
        HBox greenBox = new HBox();
        greenBox.setSpacing(5);
        Text bombText = new Text();
        bombText.setText(Integer.toString(bomb));
        bombText.setStyle("-fx-font-size: 35 ; -fx-fill: White");
        greenBox.getChildren().addAll(green, bombText);

        // main VBox containing all info
        VBox mainVBox = new VBox();
        mainVBox.setSpacing(5);
        mainVBox.setAlignment(Pos.BOTTOM_LEFT);
        mainVBox.getChildren().addAll(megaBox, heartBox, greenBox, sc);

        // space ship VBox (space ship and every thing attached to it is in this part)
        VBox infoVBox = new VBox();
        infoVBox.setAlignment(Pos.BOTTOM_CENTER);
        infoVBox.getChildren().addAll(spaceShip);

        // beams Heat! and temperature
        ProgressBar heatBar = new ProgressBar();
        heatBar.setProgress(0);
        heatBar.setPrefSize(Constants.GAME_SCENE_WIDTH, 3);
//        heatBar.setBorder(Border.EMPTY);

        VBox barVbox = new VBox(heatBar);
        barVbox.setAlignment(Pos.TOP_LEFT);

        // --- panes ---
        // main stack pain


        stackPane = new StackPane();


        stackPane.getChildren().add(new ImageView(background));
        stackPane.getChildren().addAll(infoVBox, mainVBox, barVbox);
        scene = new Scene(stackPane, Constants.GAME_SCENE_WIDTH, Constants.GAME_SCENE_HEIGHT);
        getRec(45, level);
        scene.getStylesheets().add("GUI/gameplay/bar.css");
        // TODO: 4/20/2019 replace with actual fire system
        // beam!
        ArrayList<Beam> beams = new ArrayList<>();

        // --- key and mouse handlers! ---
        // keyboard handler - setOnKeyPressed
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case SPACE:
                    spaceKey = KeyState.PRESSED;
                    break;
                case ESCAPE:
                    // TODO: 4/21/2019 after pressing scape, the game should be paused!
                    timeline.pause();
                    save();
                    ExitMenuStage exitMenuStage = new ExitMenuStage();
                    exitMenuStage.Display();
                    break;

                case DIGIT1:
                    attackSystem.setLevel(BeamLevel.LEVEL_1);
                    break;
                case DIGIT2:
                    attackSystem.setLevel(BeamLevel.LEVEL_2);
                    break;
                case DIGIT3:
                    attackSystem.setLevel(BeamLevel.LEVEL_3);
                    break;
                case B:
                    getBomb();
                    //todo killing anything and counting the score


                    bomb--;
                    bombText.setText(Integer.toString(bomb));
                    break;
            }
        });

        // keyboard handler - setOnKeyReleased
        scene.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case SPACE:
                    spaceKey = KeyState.NOT_PRESSED;
                    break;
            }
        });

        // keyboard handler - action (does something if key is pressed!) - timeLines and keyFrames
        //// keyFrames
        KeyFrame shootKeyFrame = new KeyFrame(Duration.millis(25), event -> {
            heatBar.setProgress(attackSystem.getTemperature() / Constants.TEMPERATURE_LIMIT);
            if (attackSystem.getTemperature() > Constants.TEMPERATURE_LIMIT)
                heatBar.setStyle("-fx-background-color: red");
            else
                heatBar.setStyle(" -fx-accent: #ffc12b");

            // space key for shooting beams! and handling temperature and hits!
            attackSystem.decreaseTemp();
            if (spaceKey == KeyState.PRESSED) {
                Beam[] temp = attackSystem.getBeams(spaceShip);
                if (temp != null) {
                    attackSystem.increaseTemp();
                    beams.addAll(Arrays.asList(temp));
                    stackPane.getChildren().addAll(temp);
                }


            }

        });
        KeyFrame mainKeyFrame = new KeyFrame(Duration.millis(25), event -> {

            // moving beams forward!
            for (Beam beam : beams) {
                if (beam.getLayoutY() < 0) {
                    beams.remove(beam);
                    stackPane.getChildren().remove(beam);
                } else {
                    beam.setTranslateY(beam.getTranslateY() - beam.getHeight());
                }
            }


        });


        // losing
        KeyFrame hitKeyFrame = new KeyFrame(Duration.millis(25), event -> {
            for (int i = 0; i < chickens.size(); i++) {
                if (isHitToChicken(chickens.get(i), spaceShip)) {
                    System.out.println(chickens.get(i));
                    heartNum--;
                    heartText.setText(Integer.toString(heartNum));
                    stackPane.getChildren().remove(spaceShip);
                    spaceShip.setTranslateX(0);
                    spaceShip.setTranslateY(0);
                    if (heartNum == 0) {
                        System.out.println(" looooser");
                        // todo game finishes
//                        save();
//                        if (heartNum==0)
//                        MainStageHolder.stage.setScene(MainMenuSceneBuilder.getScene());
                    }

                }
            }
            catchStuff();

        });

        KeyFrame killKeyFrame = new KeyFrame(Duration.millis(25), event -> {

// todo killing chickens
            for (int i = beams.size() - 1; i >= 0; i--) {
                for (int j = chickens.size() - 1; j >= 0; j--) {
                    if (isHit(chickens.get(j), beams.get(i))) {
                        stackPane.getChildren().remove(beams.get(i));
                        ((Chicken) chickens.get(j)).decHealth(beams.get(i).firePower);

                        glow(chickens.get(j));
                        beams.remove(i);
                        break;
                    }
                }
            }


            for (int i = chickens.size() - 1; i >= 0; i--) {
                ImageView chicken = chickens.get(i);
                if (((Chicken) chicken).isDead()) {
                    stackPane.getChildren().remove(chicken);
                    score = score + chickens.get(i).getlevel();
                    scoreBox.setText(Integer.toString(score));
                    chickens.remove(chicken);
                }
            }
            checkChickens();
        });


        // moving the chickens
        KeyFrame moveKeyFrame = new KeyFrame(Duration.millis(3000), event -> {
            move();
        });

        // throwing stuff
        KeyFrame throwKeyFrame = new KeyFrame(Duration.millis(1000), event -> {

            for (int i = 0; i < chickens.size(); i++) {
                System.out.println(chickens.get(i).getClass());
                thowEgg(chickens.get(i));
                throwSeed(chickens.get(i));
            }
        });


        //blink
        // todo fixing keyframe
        KeyFrame picFrame = new KeyFrame(Duration.millis(2000), event -> {
            for (int i = 0; i < chickens.size(); i++) {

                (chickens.get(i)).blink();

            }
        });

        //// timeline
        timeline = new Timeline();
        timeline.getKeyFrames().addAll(mainKeyFrame, killKeyFrame, shootKeyFrame, hitKeyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.playFromStart();

        Timeline timeline1 = new Timeline();
        timeline1.getKeyFrames().addAll( throwKeyFrame);
        timeline1.setCycleCount(Timeline.INDEFINITE);
        timeline1.playFromStart();

        Timeline timeline2 = new Timeline();
        timeline2.getKeyFrames().addAll(moveKeyFrame);
        timeline2.setCycleCount(Timeline.INDEFINITE);
        timeline2.playFromStart();


        // mouse handler
        scene.setOnMouseMoved(event -> {
            TranslateTransition transition = new TranslateTransition();
            transition.setDuration(Duration.millis(5));
            transition.setToX(event.getSceneX() - spaceShip.getLayoutX() - 65);
            transition.setToY(event.getSceneY() - spaceShip.getLayoutY() - 65);
            transition.setNode(spaceShip);
            transition.playFromStart();
        });


        return this;
    }

    private void throwSeed(Chicken chicken) {

        if (random.nextInt(1000) < 40) {
            seed = new ImageView(new Image(new File(
                    System.getProperty("user.dir") + "/src/pics/seed.png").toURI().toString()));
            seed.setFitHeight(50);
            seed.setFitWidth(50);
            // todo fix the position
            seed.setTranslateX(chicken.getTranslateX());
            seed.setTranslateY(chicken.getTranslateY());

            stackPane.getChildren().addAll(seed);
            stuff.add(seed);
            System.out.println("thrown");

            TranslateTransition transition = new TranslateTransition();
            transition.setNode(seed);
            transition.setDuration(Duration.millis(2000));
            // todo fix the position
            transition.setToY(Constants.GAME_SCENE_HEIGHT+200);
            System.out.println("tra");
            transition.playFromStart();
        }


    }

    public void thowEgg(Chicken chicken) {
        boolean flag = false;
        if (chicken instanceof Chicken1) {
            if (random.nextInt(1000) < 50)
                flag = true;

        } else if (chicken instanceof Chicken2) {
            if (random.nextInt(1001) < 50)
                flag = true;
        } else if (chicken instanceof Chicken3) {
            if (random.nextInt(1000) < 100)
                flag = true;
        } else if (chicken instanceof Chicken4) {
            if (random.nextInt(1000) < 200)
                flag = true;
        } else if (chicken instanceof Giant) {
            throwGiantEgg(chicken, level);
            return;

        }
        if (flag) {

                egg = new ImageView(new Image(new File(
                        System.getProperty("user.dir") + "/src/pics/losing.png").toURI().toString()));
                egg.setFitHeight(50);
                egg.setFitWidth(50);
                // todo fix the position
                egg.setTranslateX(chicken.getTranslateX());
                egg.setTranslateY(chicken.getTranslateY());

                stackPane.getChildren().addAll(egg);
                stuff.add(egg);
                System.out.println("thrown egg");
                //todo fixing the speed
                TranslateTransition transition = new TranslateTransition();
                transition.setNode(egg);
                transition.setDuration(Duration.millis(2000));
                // todo fix the position

                transition.setToY(Constants.GAME_SCENE_HEIGHT);
                System.out.println("tra");
                transition.playFromStart();

        }


    }

    public void throwGiantEgg(Chicken chicken, int level) {
        //todo compeleting the method
    }


    public void catchStuff() {
        if (stuff != null) {
            for (int i = 0; i < stuff.size(); i++) {
                ImageView view = stuff.get(i);

                if (isHitToSeed(spaceShip, view)) {
                    if (view.equals(seed)) {
                        System.out.println("hit to seed");
                        coins++;
                        coinText.setText(Integer.toString(coins));
                    } else if (view.equals(egg)) {
                        heartNum--;
                        heartText.setText(Integer.toString(heartNum));
                        spaceShip.setTranslateX(Constants.GAME_SCENE_WIDTH / 2);
                        spaceShip.setTranslateY(Constants.GAME_SCENE_HEIGHT);
                    }
                    stackPane.getChildren().remove(view);
                    stuff.remove(view);
                }
            }
        }

    }

    private void checkChickens() {
        if (chickens.size() == 0) {
            System.out.println("wave=" + wave);
            wave++;
            if (wave == 5) {
                wave = 1;
                level++;
                score = score + coins * 3;
                coins = 0;
            }
            getNextWave(wave, level);
        }
    }

    private void getNextWave(int wave, int level) {
        if (wave == 1) {
            getRec(10, level);
        }
        if (wave == 2) {
            getRandom(8, level);
        }
        if (wave == 3) {
            getCircle(20, level);
        }
        if (wave == 4) {
            getGiant(level);
        }
    }

    private void glow(ImageView imageView) {
        new Thread(() -> {

            Glow glow = new Glow();
            glow.setLevel(0.9);
            imageView.setEffect(glow);
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            glow.setLevel(0);
        }).start();
    }

    public Scene getScene() {
        return scene;
    }

    public void getBomb() {
        Bomb bomb = new Bomb();
        bomb.setTranslateY(spaceShip.getTranslateY()+300);
        bomb.setTranslateX(spaceShip.getTranslateX());


        stackPane.getChildren().addAll(bomb);

        TranslateTransition transition = new TranslateTransition();
        transition.setNode(bomb);
        transition.setDuration(Duration.millis(3000));
        transition.setToY(0);
        transition.setToX(0);
        transition.playFromStart();
        transition.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==Animation.Status.STOPPED){
                stackPane.getChildren().remove(bomb);
                for (int i = chickens.size() - 1; i >= 0; i--) {
                    ImageView chicken = chickens.get(i);
                    stackPane.getChildren().remove(chicken);
                    score = score + chickens.get(i).getlevel();
                    scoreBox.setText(Integer.toString(score));
                    chickens.remove(chicken);
                }
            }
        });


    }

    public void getRec(int number, int level) {
        chickens = enemySystem.addRec(number, level);
        initMove();
        stackPane.getChildren().addAll(chickens);
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            recMove();
        }).start();

    }

    private void initMove() {
        for (ImageView chicken : chickens) {
            double destX = chicken.getTranslateX();
            chicken.setTranslateX(random.nextInt(2) == 1 ? -Constants.GAME_SCENE_WIDTH : Constants.GAME_SCENE_WIDTH);
            TranslateTransition transition = new TranslateTransition();
            transition.setToX(destX);
            transition.setDuration(Duration.millis(2000));
            transition.setNode(chicken);
            transition.playFromStart();
        }
    }

    public void getRandom(int number, int level) {
        chickens = enemySystem.addRandom(number, level);


        stackPane.getChildren().addAll(chickens);
//        randomMove();
//        randomAttack();
    }

    public void getCircle(int num, int level) {
        chickens = enemySystem.addCircle(num, level);

        stackPane.getChildren().addAll(chickens);
        // todo circle move

    }

    public void getGiant(int level) {
        chickens = enemySystem.getGiant(level);
    }


    private void recMove() {

        for (int i = 0; i < chickens.size(); i++) {
            TranslateTransition transition = new TranslateTransition();
            transition.setNode(chickens.get(i));
            transition.setDuration(Duration.millis(1500));
            transition.setToX(chickens.get(i).getTranslateX() + 30);
            transition.setCycleCount(2);
            transition.setAutoReverse(true);
            transition.playFromStart();
        }

    }

    // todo random attack
    public void randomAttack() {
        if (chickens.size() > 0) {
            TranslateTransition transition = new TranslateTransition();
            transition.setNode(chickens.get(random.nextInt(chickens.size())));
            transition.setDuration(Duration.millis(3000));
            transition.setToX(spaceShip.getTranslateX());
            transition.setToY(spaceShip.getTranslateY());
            transition.setCycleCount(Animation.INDEFINITE);
            transition.setAutoReverse(true);
            transition.playFromStart();
        }

    }

    public void randomMove() {

        for (int i = 0; i < chickens.size(); i++) {

            TranslateTransition transition = new TranslateTransition();
            transition.setNode(chickens.get(i));
            transition.setDuration(Duration.millis(3000));
            transition.setToX(random.nextInt(Constants.GAME_SCENE_WIDTH)- Constants.GAME_SCENE_WIDTH/2);
            transition.setToY(random.nextInt(Constants.GAME_SCENE_HEIGHT) -  Constants.GAME_SCENE_HEIGHT/2);
            transition.setCycleCount(Animation.INDEFINITE);
            transition.setAutoReverse(true);
            transition.playFromStart();
        }

    }

    public void move() {
        switch (wave) {
            case 1:
                recMove();
                break;
            case 2:
                randomMove();
                //randomAttack();
                break;
            case 3:
                recMove();
                break;
        }
    }

    // todo :blink


    public boolean isHit(ImageView view, Beam view2) {
//        System.out.println("x:" + view.getTranslateX() + " \\ y:" + view.getTranslateY() + " \\ width:" + view.getFitWidth() + " \\ height:" + view.getFitHeight());
//        System.out.println("x:" + view2.getTranslateX() + " \\ y:" + view2.getTranslateY() + " \\ width:" + view2.getWidth() + " \\ height:" + view2.getHeight());
//        System.out.println();

        // 1 is pic and 2 is beam
        double x1 = view.getTranslateX(), x2 = view2.getTranslateX();
        double y1 = view.getTranslateY(), y2 = view2.getTranslateY();
        double w1 = view.getFitWidth(), w2 = view2.getWidth();
        double h1 = view.getFitHeight(), h2 = view2.getHeight();


        // only for straight arrows!
        /*if ((x1 < x2 && x2 < x1 + w1) && ((y2 > y1 && y2 < y1 + h1) || (y2 + h2 > y1 && y2 + h2 < y1 + h1)))
            return true;*/
        return Math.abs(x1-x2) < w1/2 + w2/2 && Math.abs(y1-y2) < h1/2 + h2/2;

        //return false;


    }

    public boolean isHitToSeed(ImageView view, ImageView view2) {
//        System.out.println("x:" + view.getTranslateX() + " \\ y:" + view.getTranslateY() + " \\ width:" + view.getFitWidth() + " \\ height:" + view.getFitHeight());
//        System.out.println("x:" + view2.getTranslateX() + " \\ y:" + view2.getTranslateY() + " \\ width:" + view2.getWidth() + " \\ height:" + view2.getHeight());
//        System.out.println();

        // 1 is pic and 2 is beam
        double x1 = view.getTranslateX(), x2 = view2.getTranslateX();
        double y1 = view.getTranslateY() + view.getLayoutY(), y2 = view2.getTranslateY() + view2.getLayoutY();
        double w1 = view.getFitWidth(), w2 = view2.getFitWidth();
        double h1 = view.getFitHeight(), h2 = view2.getFitHeight();


        // only for straight arrows!
        /*if ((x1 < x2 && x2 < x1 + w1) && ((y2 > y1 && y2 < y1 + h1) || (y2 + h2 > y1 && y2 + h2 < y1 + h1)))
            return true;*/
        return Math.abs(x1-x2) < w1/2 + w2/2 && Math.abs(y1-y2) < h1/2 + h2/2;

        //return false;


    }

    public boolean isHitToChicken(Chicken chicken, SpaceShip spaceShip) {
        // todo fixing the method

        double x1 = chicken.getTranslateX() + chicken.getLayoutX(), x2 = spaceShip.getTranslateX() + spaceShip.getLayoutY();
        double y1 = chicken.getTranslateY() + chicken.getLayoutY() - 50, y2 = spaceShip.getTranslateY() + spaceShip.getLayoutY();
        double w1 = chicken.getFitWidth(), w2 = spaceShip.getFitWidth();
        double h1 = chicken.getFitHeight(), h2 = spaceShip.getFitHeight();
        return Math.abs(x1-x2) < w1/2 + w2/2 && Math.abs(y1-y2) < h1/2 + h2/2;

    }

    void save() {
        Game game = new Game();
        game.spaceShip = this.spaceShip;
//        game.chickens = this.chickens;
        // TODO: 6/29/19 save scores and ....

        currentPlayer.setCurrentGame(game);
    }


}



