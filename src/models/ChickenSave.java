package models;

public class ChickenSave {
    int health;
    int level;
    double x;
    double y;


    public ChickenSave( Chicken chicken) {
        this.health= chicken.health;
        this.level= chicken.level;
        this.x= chicken.getTranslateX();
        this.y= chicken.getTranslateY();

    }

    public Chicken toChicken(){
        Chicken chicken= null;
        switch (level){
            case 1:
                chicken= new Chicken1();
                break;
            case 2:
                chicken= new Chicken2();
                break;
            case 3:
                chicken= new Chicken3();
                break;
            case 4:
                chicken= new Chicken4();
                break;
            case 5:
                chicken= new Giant(level);
                break;
        }
        chicken.health= this.health;
        chicken.setTranslateX(this.x);
        chicken.setTranslateY(this.y);
        return chicken;
    }
}
