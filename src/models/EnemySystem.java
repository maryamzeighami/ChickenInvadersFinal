package models;


import Controller.Constants;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class EnemySystem {

    int number;
    int colum;
    Chicken[] chickens;
    Random random = new Random();


    public Chicken[] getChickens() {
        return chickens;
    }

    public ArrayList<Chicken> addRec(int numbers, int level) {
        this.number = numbers;
        chickens = new Chicken[numbers];

        if (number >= 40) {
            colum = 9;
        } else if (number > 30) {
            colum = 8;
        } else {
            colum = 7;
        }


        switch (level) {
            case 1:
                for (int i = 0; i < numbers; i++) {
                    chickens[i] = new Chicken1();

                }
                break;

            case 2:

                for (int i = 0; i < numbers; i++) {
                    if (i < numbers / 2) {
                        chickens[i] = new Chicken1();
                    } else {
                        chickens[i] = new Chicken2();

                    }
                }
                break;

            case 3:
                for (int i = 0; i < numbers; i++) {
                    if (i < numbers / 3) {
                        chickens[i] = new Chicken1();

                    } else if (i < (numbers / 3) * 2) {
                        chickens[i] = new Chicken2();

                    } else {
                        chickens[i] = new Chicken3();
                    }
                }
                break;

            case 4:
                for (int i = 0; i < numbers; i++) {
                    if (i < numbers / 4) {
                        chickens[i] = new Chicken1();

                    } else if (i < (numbers / 4) * 2) {
                        chickens[i] = new Chicken2();

                    } else if (i < (numbers / 4) * 3) {
                        chickens[i] = new Chicken3();

                    } else {
                        chickens[i] = new Chicken4();

                    }
                }
                break;

        }
        for (int i = 0; i < numbers; i++) {

            chickens[i].setTranslateX(((colum-1)/2 - i % colum) * Constants.CHICKEN_SIZE);
            chickens[i].setTranslateY((i / colum) * Constants.CHICKEN_SIZE / 2 - 300);
        }

        return new ArrayList<Chicken>(Arrays.asList(chickens));

    }


    public ArrayList<Chicken> addCircle(int numbers, int level) {
//        this.number = numbers;
//        for (int i = 0; i < numbers; i++) {
//            chickens[i] = new Chicken1();
//
//        }
//        for (int i = 0; i < chickens.length; i++) {
//
//        }
//
//
//        // todo
//        return new ArrayList<Chicken>(Arrays.asList(chickens));

        this.number = numbers;
        chickens = new Chicken[numbers];

        if (number >= 40) {
            colum = 9;
        } else if (number > 30) {
            colum = 8;
        } else {
            colum = 7;
        }


        switch (level) {
            case 1:
                for (int i = 0; i < numbers; i++) {
                    chickens[i] = new Chicken3();


                }break;

            case 2:

                for (int i = 0; i < numbers; i++) {
                    if (i < numbers / 2) {
                        chickens[i] = new Chicken1();
                    } else {
                        chickens[i] = new Chicken2();

                    }


                }break;

            case 3:
                for (int i = 0; i < numbers; i++) {
                    if (i < numbers / 3) {
                        chickens[i] = new Chicken1();

                    } else if (i < (numbers / 3) * 2) {
                        chickens[i] = new Chicken2();

                    } else {
                        chickens[i] = new Chicken3();

                    }


                }break;

            case 4:
                for (int i = 0; i < numbers; i++) {
                    if (i < numbers / 4) {
                        chickens[i] = new Chicken1();

                    } else if (i < (numbers / 4) * 2) {
                        chickens[i] = new Chicken2();

                    } else if (i < (numbers / 4) * 3) {
                        chickens[i] = new Chicken3();

                    } else {
                        chickens[i] = new Chicken4();

                    }


                }break;

        }
        for (int i = 0; i < numbers; i++) {

            chickens[i].setTranslateX((i % colum - (colum-1)/2) * Constants.CHICKEN_SIZE + Constants.GAME_SCENE_WIDTH/2);
            chickens[i].setTranslateY((i / colum) * Constants.CHICKEN_SIZE / 2 + 30 );
        }

        return new ArrayList<Chicken>(Arrays.asList(chickens));


    }


    public ArrayList<Chicken> addRandom(int numbers, int level) {
        this.number = numbers;
        chickens = new Chicken[number];
        switch (level) {
            case 1:
                for (int i = 0; i < numbers; i++) {
                    chickens[i] = new Chicken2();

                }
                break;

            case 2:

                for (int i = 0; i < numbers; i++) {
                    if (i < numbers / 2) {
                        chickens[i] = new Chicken1();
                    } else {
                        chickens[i] = new Chicken2();

                    }


                } break;


            case 3:
                for (int i = 0; i < numbers; i++) {
                    if (i < numbers / 3) {
                        chickens[i] = new Chicken1();

                    } else if (i < (numbers / 3) * 2) {
                        chickens[i] = new Chicken2();

                    } else {
                        chickens[i] = new Chicken3();

                    }


                } break;

            case 4:
                for (int i = 0; i < numbers; i++) {

                    if (i < (numbers / 4) * 2) {
                        chickens[i] = new Chicken2();

                    } else if (i < (numbers / 4) * 3) {
                        chickens[i] = new Chicken3();

                    } else {
                        chickens[i] = new Chicken4();

                    }


                }break;

        }
        for (Chicken chicken : chickens) {
            chicken.setTranslateX(random.nextInt(Constants.GAME_SCENE_WIDTH) - Constants.GAME_SCENE_WIDTH/2);
            chicken.setTranslateY(random.nextInt(Constants.GAME_SCENE_HEIGHT) - Constants.GAME_SCENE_HEIGHT/2);
        }


        return new ArrayList<Chicken>(Arrays.asList(chickens));


    }
    // todo giant
    public ArrayList<Chicken> getGiant(int level) {
        ArrayList<Chicken> chickens= new ArrayList<>();
        Giant giant=new Giant(level);
        giant.setTranslateX(200);
        giant.setTranslateY(200);
        chickens.add(giant);
        return chickens;

    }
}
