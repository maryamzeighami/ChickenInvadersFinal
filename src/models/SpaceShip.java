package models;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpaceShip extends ImageView {
    public boolean haveSheild;
    public boolean dontMove;
    public AttackSystem attackSystem= new AttackSystem();

    public SpaceShip(Image image) {
        super(image);
        haveSheild=false;
        dontMove=false;

    }
}
