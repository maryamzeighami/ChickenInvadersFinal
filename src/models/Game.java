package models;

import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

public class Game {

    public SpaceShip spaceShip;
    public ArrayList<Chicken> chickens;
    public  int score;
    public int healthNumber;
    public int numberOfBombs;
    public int numberOfSeeds;
    public int level;
    public int wave;


    // TODO: 6/29/19 (SAVE_JSON)


    public Game() {
    }

    public void save(SpaceShip spaceShip, ArrayList<Chicken> chickens) {
        this.spaceShip = spaceShip;
        this.chickens = chickens;

    }
}
