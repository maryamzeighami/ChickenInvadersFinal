package GUI.gameplay;

import Controller.CellTower;
import Controller.Constants;
import GUI.MainStageHolder;
import GUI.menues.MainMenuSceneBuilder;
import GUI.menues.RankingSceneBuilder;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
    public static GameSceneBuilder currentGameSceneBuilder;

    public StackPane stackPane;
    Random random = new Random();
    Text scoreBox = new Text();
    boolean isServer=false;
    boolean isMulti= false;
    int myIndex;
    public final static Object lock = new Object();


    Media bombSound = new Media(new File(System.getProperty("user.dir") + "/src/sounds/bomb.wav").toURI().toString());
    MediaPlayer bombPlayer = new MediaPlayer(bombSound);

    Media catchSeedSound = new Media(new File(System.getProperty("user.dir") + "/src/sounds/seed.wav").toURI().toString());
    MediaPlayer seedPlayer = new MediaPlayer(catchSeedSound);

    Media shootSound = new Media(new File(System.getProperty("user.dir") + "/src/sounds/shoot.wav").toURI().toString());
    MediaPlayer shootPlayer = new MediaPlayer(shootSound);

    Media gameSound = new Media(new File(System.getProperty("user.dir") + "/src/sounds/gameSound.mp3").toURI().toString());
    MediaPlayer gameSoundPlayer = new MediaPlayer(gameSound);


    ArrayList<ImageView> stuff = new ArrayList<>();


    Image spaceShipImage = new Image(new File(System.getProperty("user.dir") + "/src/pics/spaceship.png").toURI().toString());
    Image spaceShipImage1 = new Image(new File(System.getProperty("user.dir") + "/src/pics/spaceship1.png").toURI().toString());



    public static CellTower cellTower;

    // texts
    Text heartText = new Text();
    Text coinText = new Text();

    public ArrayList<Beam> beams = new ArrayList<>();


    // key states
    KeyState spaceKey = KeyState.NOT_PRESSED;


    // AttackSystem

    EnemySystem enemySystem = new EnemySystem();
   public ArrayList<Chicken> chickens;
    int level = 1;
    int wave = 1;

    static Player currentPlayer;

//    SpaceShip spaceShip;
    public ArrayList<SpaceShip> spaceShips= new ArrayList<>();
    public ArrayList<Player> players = new ArrayList<>();

    public GameSceneBuilder builder(Player player) {

        currentPlayer= player;
        currentGameSceneBuilder= this;

        gameSoundPlayer.play();


        // resuummmmmm
        if (currentPlayer.getCurrentGame() != null) {
            currentPlayer.spaceShip = currentPlayer.getCurrentGame().spaceShip;
            chickens = new ArrayList<>();
            for (ChickenSave chicken : currentPlayer.getCurrentGame().chickens) {
                chickens.add(chicken.toChicken());
            }
            level= currentPlayer.getCurrentGame().level;
            wave= currentPlayer.getCurrentGame().wave;
            currentPlayer.score=currentPlayer.getCurrentGame().score;
            currentPlayer.coin=currentPlayer.getCurrentGame().numberOfSeeds;
            currentPlayer.heartNum=currentPlayer.getCurrentGame().healthNumber;
            currentPlayer.numberOfBombs=currentPlayer.getCurrentGame().numberOfBombs;

            // TODO: 6/29/19 (SAVE_JSON)
        }



        // --- set pictures ---

        // background
        String addr = System.getProperty("user.dir") + "/src/pics/back3.png";
        File file = new File(addr);
        Image background = new Image(file.toURI().toString());



        // heart pic
        String addrss = System.getProperty("user.dir") + "/src/pics/heart.png";
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
        String gAdres = System.getProperty("user.dir") + "/src/pics/bomb.png";
        File greenFile = new File(gAdres);
        Image greenImage = new Image(greenFile.toURI().toString());
        ImageView green = new ImageView(greenImage);
        green.setFitHeight(50);
        green.setFitWidth(50);

        // --- set info (health, score, ...) ---
        // heart VBox (health infoVBox actually!)
        HBox heartBox = new HBox();
        heartBox.setSpacing(5);

        heartText.setText(Integer.toString(currentPlayer.heartNum));
        heartText.setStyle("-fx-font-size: 35; -fx-fill: white");
        coinText.setStyle("-fx-font-size: 35; -fx-fill: white");
        heartBox.getChildren().addAll(heart, heartText);

        // mega VBox
        HBox megaBox = new HBox();
        megaBox.setSpacing(5);

        coinText.setText(Integer.toString(player.coin));
        megaBox.getChildren().addAll(megaSeed, coinText);

        scoreBox.setText(Integer.toString(player.score));
        scoreBox.setStyle(" -fx-font-size: 80 ; -fx-fill: White");
        HBox sc = new HBox();
        sc.getChildren().addAll(scoreBox);
        sc.setAlignment(Pos.BOTTOM_LEFT);

        // green VBox
        HBox bombBox = new HBox();
        bombBox.setSpacing(5);
        Text bombText = new Text();
        bombText.setText(Integer.toString(currentPlayer.numberOfBombs));
        bombText.setStyle("-fx-font-size: 35 ; -fx-fill: White");
        bombBox.getChildren().addAll(green, bombText);

        // main VBox containing all info
        VBox mainVBox = new VBox();
        mainVBox.setSpacing(5);
        mainVBox.setAlignment(Pos.BOTTOM_LEFT);
        mainVBox.getChildren().addAll(megaBox, heartBox, bombBox, sc);

        // space ship VBox (space ship and every thing attached to it is in this part)
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


        stackPane.getChildren().addAll(new ImageView(background),mainVBox, barVbox);
        if (!isMulti) {
            currentPlayer.spaceShip = new SpaceShip(spaceShipImage);
            currentPlayer.spaceShip.setFitHeight(130);
            currentPlayer.spaceShip.setFitWidth(130);
            VBox infoVBox = new VBox();
            infoVBox.setAlignment(Pos.BOTTOM_CENTER);
            infoVBox.getChildren().addAll(currentPlayer.spaceShip);
            stackPane.getChildren().add(infoVBox);
            spaceShips.add(currentPlayer.spaceShip);
            players.add(currentPlayer);
        }
        scene = new Scene(stackPane, Constants.GAME_SCENE_WIDTH, Constants.GAME_SCENE_HEIGHT);
        if (currentPlayer.getCurrentGame() == null) {
            getRec(45, level);
        }
        else {
            stackPane.getChildren().addAll(chickens);
        }
        scene.getStylesheets().add("GUI/gameplay/bar.css");
        // TODO: 4/20/2019 replace with actual fire system
        // beam!


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
                    gameSoundPlayer.pause();
                    gameSoundPlayer.getOnStopped();
                    //save();
                    ExitMenuStage exitMenuStage = new ExitMenuStage();
                    exitMenuStage.Display();
                    break;

                case B:
//                    if (numberOfBomb>0) {
                    getBomb(myIndex);
                    if (isMulti)
                        cellTower.transmitBomb(myIndex);
                    //todo killing anything and counting the score


                    currentPlayer.numberOfBombs--;
                    bombText.setText(Integer.toString(currentPlayer.numberOfBombs));
//                    }
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
            heatBar.setProgress(currentPlayer.spaceShip.attackSystem.getTemperature() / Constants.TEMPERATURE_LIMIT);
            if (currentPlayer.spaceShip.attackSystem.getTemperature() > Constants.TEMPERATURE_LIMIT)
                heatBar.setStyle("-fx-background-color: red");
            else
                heatBar.setStyle(" -fx-accent: #ffc12b");

            // space key for shooting beams! and handling temperature and hits!
            for (SpaceShip spaceShip: spaceShips)
                spaceShip.attackSystem.decreaseTemp();
            if (spaceKey == KeyState.PRESSED) {
                Beam[] temp = currentPlayer.spaceShip.attackSystem.getBeams(currentPlayer.spaceShip);
                if (temp != null) {
                    currentPlayer.spaceShip.attackSystem.increaseTemp();
                    beams.addAll(Arrays.asList(temp));
                    stackPane.getChildren().addAll(temp);
                    if (isMulti) cellTower.transmitShoot(myIndex);
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

            synchronized (lock) {
                for (ImageView view : stuff) {
                    if (view.getLayoutY() > 900) {
                        stuff.remove(view);
                        stackPane.getChildren().remove(view);
                    }
                }
            }


        });


        // losing
        KeyFrame hitKeyFrame = new KeyFrame(Duration.millis(25), event -> {
            for (int i = 0; i < chickens.size(); i++) {
                if (isHitToChicken(chickens.get(i), currentPlayer.spaceShip) && !currentPlayer.spaceShip.haveSheild) {

                    currentPlayer.heartNum--;
                    currentPlayer.spaceShip.haveSheild = true;
                    if (currentPlayer.heartNum == 0)
                        defeat();
                    heartText.setText(Integer.toString(currentPlayer.heartNum));
                    stackPane.getChildren().remove(currentPlayer.spaceShip);
                    currentPlayer.spaceShip.setTranslateX(0);
                    currentPlayer.spaceShip.setTranslateY(0);
                    currentPlayer.spaceShip.dontMove = true;

                    new Thread(() -> {
                        try {
                            TimeUnit.MILLISECONDS.sleep(5000);
                            currentPlayer.spaceShip.haveSheild = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();

                    new Thread(() -> {
                        try {
                            TimeUnit.MILLISECONDS.sleep(500);
                            currentPlayer.spaceShip.dontMove = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();


                }
            }
            catchStuff();


        });

        KeyFrame serverTransmitFrame = new KeyFrame(Duration.millis(100), event -> {
            cellTower.transmitChickenPositions(chickens);
            cellTower.transmitSpaceShipPositions(spaceShips);
        });

        KeyFrame clientTransmitFrame = new KeyFrame(Duration.millis(100), event -> {
            cellTower.transmitMyPosition(spaceShips);
        });

        KeyFrame killKeyFrame = new KeyFrame(Duration.millis(25), event -> {

// todo killing chickens
            for (int i = beams.size() - 1; i >= 0; i--) {
                for (int j = chickens.size() - 1; j >= 0; j--) {
                    if (isHit(chickens.get(j), beams.get(i))) {
                        stackPane.getChildren().remove(beams.get(i));
                        chickens.get(j).decHealth(beams.get(i).firePower);

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
                    currentPlayer.score = currentPlayer.score + chickens.get(i).getlevel();
                    if (isMulti && isServer){
                        //cellTower.transmitChickenDead(i);
                        cellTower.transmitScores();
                    }
                    scoreBox.setText(Integer.toString(currentPlayer.score));
                    chickens.remove(chicken);
                }
            }
            if (isServer || !isMulti)
                checkChickens();

        });


        // moving the chickens
        KeyFrame moveKeyFrame = new KeyFrame(Duration.millis(3000), event -> {
            move();
        });

        // throwing stuff
        KeyFrame throwKeyFrame = new KeyFrame(Duration.millis(1000), event -> {

            for (int i = 0; i < chickens.size(); i++) {
                thowEgg(i);
                throwSeed( i);
                throwPoweUp(i);
            }
        });


        //blink

        KeyFrame picFrame = new KeyFrame(Duration.millis(3000), event -> {
            for (int i = 0; i < chickens.size(); i++) {
                if (chickens.get(i) instanceof Giant) {

                } else {
                    (chickens.get(i)).blink();
                }

            }
        });

        KeyFrame motion = new KeyFrame(Duration.millis(200), event -> {
            for (int i = 0; i < chickens.size(); i++) {
                if (chickens.get(i) instanceof Giant) {
                    chickens.get(i).blink();
                }

            }
        });




        //// timeline
        timeline = new Timeline();
        timeline.getKeyFrames().addAll(mainKeyFrame, killKeyFrame, shootKeyFrame, hitKeyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.playFromStart();

        Timeline timeline1 = new Timeline();
        timeline1.getKeyFrames().addAll(throwKeyFrame);
        timeline1.setCycleCount(Timeline.INDEFINITE);
        if (!isMulti || isServer)
            timeline1.playFromStart();

        Timeline timeline2 = new Timeline();
        timeline2.getKeyFrames().addAll(moveKeyFrame, picFrame);
        timeline2.setCycleCount(Timeline.INDEFINITE);
        timeline2.playFromStart();

        Timeline timeline3 = new Timeline();
        timeline3.getKeyFrames().addAll(motion);
        timeline3.setCycleCount(Timeline.INDEFINITE);
        timeline3.playFromStart();


        Timeline timeline4 = new Timeline();
        timeline4.getKeyFrames().addAll(serverTransmitFrame);
        timeline4.setCycleCount(Timeline.INDEFINITE);
        if (isMulti && isServer)
            timeline4.playFromStart();

        Timeline timeline5 = new Timeline();
        timeline5.getKeyFrames().addAll(clientTransmitFrame);
        timeline5.setCycleCount(Timeline.INDEFINITE);
        if (isMulti && !isServer)
            timeline5.playFromStart();
        // mouse handler
        scene.setOnMouseMoved(event -> {
            if (!currentPlayer.spaceShip.dontMove) {
                currentPlayer.spaceShip.setTranslateX(event.getSceneX() - currentPlayer.spaceShip.getLayoutX() - 65);
                currentPlayer.spaceShip.setTranslateY(event.getSceneY() - currentPlayer.spaceShip.getLayoutY() - 65);
            }
        });
        return this;
    }


    public void builder(ArrayList<Player> players , int i,CellTower cellTower){
        GameSceneBuilder.cellTower = cellTower;
        myIndex=i;
        isMulti=true;
        if (i==0) isServer=true;
        currentPlayer=players.get(i);
        builder(currentPlayer);
        for (int j = 0; j < players.size(); j++) {
            players.get(j).spaceShip = new SpaceShip(spaceShipImage);
            players.get(j).spaceShip.setFitHeight(130);
            players.get(j).spaceShip.setFitWidth(130);
            VBox infoVBox = new VBox();
            infoVBox.setAlignment(Pos.BOTTOM_CENTER);
            infoVBox.getChildren().addAll(players.get(j).spaceShip);
            players.get(j).spaceShip.setTranslateX(200 * j);
            players.get(j).spaceShip.setTranslateY(0);
            stackPane.getChildren().add(infoVBox);
            spaceShips.add(players.get(j).spaceShip);
        }
        this.players = players;
    }

    private void defeat() {
        if (currentPlayer.heartNum == 0 && !isMulti) {
            MainStageHolder.stage.setScene(new LastSceneBuilder().build(currentPlayer, false).getScene());
            gameSoundPlayer.pause();
            seedPlayer.pause();
            shootPlayer.pause();
        }
        else if (currentPlayer.heartNum==0 && isMulti){
            MainStageHolder.stage.setScene(new RankingSceneBuilder().build(players).getScene());
        }

    }

    public void throwSeed( int i) {

        if (random.nextInt(1000) < 40) {
              throwSeedP(i);

            if (isServer)
                cellTower.transmitChickenDrops(i , "seed");


        }


    }
    public void throwSeedP(int i){
        synchronized (lock) {
            Seed seed = new Seed();

            seed.setTranslateX(chickens.get(i).getTranslateX());
            seed.setTranslateY(chickens.get(i).getTranslateY());

            stackPane.getChildren().addAll(seed);
            stuff.add(seed);

            TranslateTransition transition = new TranslateTransition();
            transition.setNode(seed);
            transition.setDuration(Duration.millis(3000));
            transition.setToY(Constants.GAME_SCENE_HEIGHT + 200);
            transition.playFromStart();
        }

    }

    public void throwPoweUp( int i) {
        if (random.nextInt(1000) < 15) {
            PowerUp powerUp;
            if (random.nextInt(2) == 1) {
                throwPowerUp(i,1);
                if (isServer)
                cellTower.transmitChickenDrops(i , "powerUp1");

            } else {
                throwPowerUp(i,2);
                if (isServer)
                cellTower.transmitChickenDrops(i , "powerUp2");
            }

        }


    }


    public void throwPowerUp(int index, int typ){
        synchronized (lock) {
            PowerUp powerUp;
            if (typ == 1) {
                powerUp = new PowerUp(1);
            } else {
                powerUp = new PowerUp(2);
            }

            powerUp.setTranslateX(chickens.get(index).getTranslateX());
            powerUp.setTranslateY(chickens.get(index).getTranslateY());

            powerUp.setFitHeight(50);
            powerUp.setFitWidth(50);

            stackPane.getChildren().addAll(powerUp);
            stuff.add(powerUp);


            TranslateTransition transition = new TranslateTransition();
            transition.setNode(powerUp);
            transition.setDuration(Duration.millis(3000));

            transition.setToY(Constants.GAME_SCENE_HEIGHT + 200);

            transition.playFromStart();
        }
    }

    public void thowEgg(int i) {
        boolean flag = false;
        if (chickens.get(i) instanceof Chicken1) {
            if (random.nextInt(1000) < 50)
                flag = true;

        } else if (chickens.get(i) instanceof Chicken2) {
            if (random.nextInt(1001) < 50)
                flag = true;
        } else if (chickens.get(i) instanceof Chicken3) {
            if (random.nextInt(1000) < 100)
                flag = true;
        } else if (chickens.get(i) instanceof Chicken4) {
            if (random.nextInt(1000) < 200)
                flag = true;
        } else if (chickens.get(i) instanceof Giant) {
            throwGiantEgg(i);
            return;

        }
        if (flag) {
            if (isServer) cellTower.transmitChickenDrops(i, "egg");
            throwEggP(i);

        }


    }

    public void throwEggP(int i){
        synchronized (lock) {
            Egg egg = new Egg();

            egg.setTranslateX(chickens.get(i).getTranslateX());
            egg.setTranslateY(chickens.get(i).getTranslateY());

            stackPane.getChildren().addAll(egg);
            stuff.add(egg);

            TranslateTransition transition = new TranslateTransition();
            transition.setNode(egg);
            transition.setDuration(Duration.millis(2000));


            transition.setToY(Constants.GAME_SCENE_HEIGHT);

            transition.playFromStart();
        }
    }

    public void throwGiantEgg(int i) {

        //todo compeleting the method
    }


    public void catchStuff() {
        synchronized (lock) {
            if (stuff != null) {
                for (int i = 0; i < stuff.size(); i++) {
                    ImageView view = stuff.get(i);
                    for (Player player : players) {
                        if (isHitToSeed(player.spaceShip, view)) {
                            if (view instanceof Seed) {
                                player.coin++;
                                coinText.setText(Integer.toString(player.coin));
                            } else if (view instanceof Egg) {
                                player.heartNum--;
                                if (player.heartNum == 0) {
                                    MainStageHolder.stage.setScene(MainMenuSceneBuilder.getScene());
                                    gameSoundPlayer.pause();
                                    return;
                                }
                                heartText.setText(Integer.toString(player.heartNum));
                                player.spaceShip.setTranslateX(Constants.GAME_SCENE_WIDTH / 2);
                                player.spaceShip.setTranslateY(Constants.GAME_SCENE_HEIGHT);
                            } else if (view instanceof PowerUp) {
                                if (((PowerUp) view).getType() == 1) {
                                    Constants.TEMPERATURE_LIMIT = Constants.TEMPERATURE_LIMIT + 5;
                                } else {

                                    if (player.spaceShip.attackSystem.getLevel() == BeamLevel.LEVEL_1)
                                        player.spaceShip.attackSystem.setLevel(BeamLevel.LEVEL_2);
                                    if (player.spaceShip.attackSystem.getLevel() == BeamLevel.LEVEL_2)
                                        player.spaceShip.attackSystem.setLevel(BeamLevel.LEVEL_3);

                                }
                            }
                            stackPane.getChildren().remove(view);
                            stuff.remove(view);
                        }
                    }
                }
            }
        }
    }

    private void checkChickens() {
        if (chickens.size() == 0) {
            System.out.println("wave=" + wave);
            wave++;
            if (wave == 6) {
                wave = 1;
                level++;
                currentPlayer.numberOfBombs++;
                //todo transmit wave change
                if (level == 5) {
                    win();
                }
                currentPlayer.score = currentPlayer.score + currentPlayer.coin * 3;
                if (isMulti) cellTower.transmitScores();
                currentPlayer.coin = 0;
            }
            getNextWave(wave, level);
        }
    }

    private void win() {
        MainStageHolder.stage.setScene(new LastSceneBuilder().build(currentPlayer, true).getScene());
        gameSoundPlayer.pause();
        seedPlayer.pause();
        shootPlayer.pause();
        bombPlayer.pause();
        //todo save
        //save();
    }

    private void getNextWave(int wave, int level) {
        if (wave == 1) {
            getRec(45, level);
        }
        if (wave == 2) {
            getRandom(8, level);
        }
        if (wave == 3) {
            getCircle(45, level);
        }
        if (wave == 4) {
            getRandom(10,level);
        }
        if (level==5){
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

    public void getBomb(int index) {

        Bomb bomb = new Bomb();
        bomb.setTranslateY(spaceShips.get(index).getTranslateY() + 300);
        bomb.setTranslateX(spaceShips.get(index).getTranslateX());


        stackPane.getChildren().addAll(bomb);

        TranslateTransition transition = new TranslateTransition();
        transition.setNode(bomb);
        transition.setDuration(Duration.millis(3000));
        transition.setToY(0);
        transition.setToX(0);
        transition.playFromStart();
        transition.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Animation.Status.STOPPED) {
                // todo boooom!
//                bombNumber.boom();
                bombPlayer.seek(Duration.ZERO);
                bombPlayer.play();
                stackPane.getChildren().remove(bomb);

                for (int i = chickens.size() - 1; i >= 0; i--) {
                    ImageView chicken = chickens.get(i);
                    if (chicken instanceof Giant){
                        ((Giant) chicken).health =- 50;
                        if (((Giant) chicken).health<=0){
                            stackPane.getChildren().remove(chicken);
                            currentPlayer.score = currentPlayer.score + chickens.get(i).getlevel();
                            if (isMulti) cellTower.transmitScores();
                            scoreBox.setText(Integer.toString(currentPlayer.score));
                            chickens.remove(chicken);
                        }
                    } else {
                        stackPane.getChildren().remove(chicken);
                        currentPlayer.score = currentPlayer.score + chickens.get(i).getlevel();
                        if (isMulti) cellTower.transmitScores();
                        scoreBox.setText(Integer.toString(currentPlayer.score));
                        chickens.remove(chicken);
                    }
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

//        randomAttack();
    }

    public void getCircle(int num, int level) {
        chickens = enemySystem.addCircle(num, level);

        stackPane.getChildren().addAll(chickens);
        // todo circle move

    }

    public void getGiant(int level) {
        chickens = enemySystem.getGiant(level);

        stackPane.getChildren().addAll(chickens);
        System.out.println("giant");
        System.out.println(chickens.get(0));
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
            transition.setToX(currentPlayer.spaceShip.getTranslateX());
            transition.setToY(currentPlayer.spaceShip.getTranslateY());
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
            transition.setToX(random.nextInt(Constants.GAME_SCENE_WIDTH) - Constants.GAME_SCENE_WIDTH / 2);
            transition.setToY(random.nextInt(Constants.GAME_SCENE_HEIGHT) - Constants.GAME_SCENE_HEIGHT / 2);
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
                circleMove();
                break;
            case 4:
                randomMove();
        }
    }

    private void circleMove() {
        for (int i = 0; i < chickens.size(); i++) {

            double teta = Math.atan(chickens.get(i).getTranslateX()/ chickens.get(i).getTranslateY()) + Math.toDegrees(1);


            TranslateTransition transition = new TranslateTransition();
            transition.setNode(chickens.get(i));
            transition.setDuration(Duration.millis(500));
            if (i < 10) {
                transition.setToX(150 * Math.cos(teta));
                transition.setToY(150 * Math.sin(teta));
            } else if (i<25){
                transition.setToX(230 * Math.cos(teta));
                transition.setToY(230 * Math.sin(teta));

            } else {
                transition.setToX(310 * Math.cos(teta));
                transition.setToY(310 * Math.sin(teta));
            }
            transition.setCycleCount(1);
            transition.playFromStart();

        }
    }


    public boolean isHit(ImageView view, Beam view2) {

        // 1 is pic and 2 is beam
        double x1 = view.getTranslateX(), x2 = view2.getTranslateX();
        double y1 = view.getTranslateY(), y2 = view2.getTranslateY();
        double w1 = view.getFitWidth(), w2 = view2.getWidth();
        double h1 = view.getFitHeight(), h2 = view2.getHeight();


        if (Math.abs(x1 - x2) < w1 / 2 + w2 / 2 && Math.abs(y1 - y2) < h1 / 2 + h2 / 2) {
            shootPlayer.seek(Duration.ZERO);
            shootPlayer.play();

        }
        return Math.abs(x1 - x2) < w1 / 2 + w2 / 2 && Math.abs(y1 - y2) < h1 / 2 + h2 / 2;


    }

    public boolean isHitToSeed(ImageView view, ImageView view2) {


        double x1 = view.getTranslateX(), x2 = view2.getTranslateX();
        double y1 = view.getTranslateY() + view.getLayoutY(), y2 = view2.getTranslateY() + view2.getLayoutY();
        double w1 = view.getFitWidth(), w2 = view2.getFitWidth();
        double h1 = view.getFitHeight(), h2 = view2.getFitHeight();


        if (Math.abs(x1 - x2) < w1 / 2 + w2 / 2 && Math.abs(y1 - y2) < h1 / 2 + h2 / 2) {
            seedPlayer.seek(Duration.ZERO);
            seedPlayer.play();

        }
        return Math.abs(x1 - x2) < w1 / 2 + w2 / 2 && Math.abs(y1 - y2) < h1 / 2 + h2 / 2;


    }

    public boolean isHitToChicken(Chicken chicken, SpaceShip spaceShip) {
        // todo fixing the methodz

        double x1 = chicken.getTranslateX() + chicken.getLayoutX(), x2 = spaceShip.getTranslateX() + spaceShip.getLayoutY();
        double y1 = chicken.getTranslateY() + chicken.getLayoutY() - 50, y2 = spaceShip.getTranslateY() + spaceShip.getLayoutY();
        double w1 = chicken.getFitWidth(), w2 = spaceShip.getFitWidth();
        double h1 = chicken.getFitHeight(), h2 = spaceShip.getFitHeight();
        return Math.abs(x1 - x2) < w1 / 2 + w2 / 2 && Math.abs(y1 - y2) < h1 / 2 + h2 / 2;

    }

//    void save() {
//        Game game = new Game();
//
//        game.spaceShip = currentPlayer.spaceShip;
//        game.score= currentPlayer.score;
//        game.level= this.level;
//        game.wave= this.wave;
//        game.numberOfBombs= currentPlayer.numberOfBombs;
//        game.numberOfSeeds=currentPlayer.coin;
//        game.healthNumber= currentPlayer.heartNum;
//
//        for (Chicken chicken : chickens) {
//            ChickenSave newChicken= new ChickenSave(chicken);
//            game.chickens.add(newChicken);
//        }
//
//        // TODO: 6/29/19 save scores and .... (SAVE_JSON)
//
//        currentPlayer.setCurrentGame(game);
//    }


    public void killChicken(int i) {
        stackPane.getChildren().remove(chickens.get(i));
        chickens.remove(chickens.get(i));
    }
}



