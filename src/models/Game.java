package models;

import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

public class Game {

    public SpaceShip spaceShip;
    public ArrayList<Chicken> chickens = new ArrayList<>();
    public  int score;
    public int healthNumber;
    public int numberOfBombs;
    public int numberOfSeeds;
    public int level;
    public int wave;


    // TODO: 6/29/19 (SAVE_JSON)


    public Game() {
    }

    public Game(int score, int healthNumber, int numberOfBombs, int numberOfSeeds, int level, int wave) {
        this.score = score;
        this.healthNumber = healthNumber;
        this.numberOfBombs = numberOfBombs;
        this.numberOfSeeds = numberOfSeeds;
        this.level = level;
        this.wave = wave;
    }
}
