package models;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpaceShip extends ImageView {
    private AttackSystem attackSystem;
    private int tempreture;
    private int health;
    public boolean haveSheild;
    public boolean dontMove;

    public SpaceShip(Image image) {
        super(image);
        haveSheild=false;
        dontMove=false;

    }
}
