package models;

import Controller.Constants;
import enums.BeamLevel;
import enums.BeamType;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class AttackSystem {
    private int spacShipTempreture;

    private BeamType beamType;
    private BeamLevel beamLevel;
    private int tempreture;

    public AttackSystem() {
        beamType = BeamType.RED;
        beamLevel = BeamLevel.LEVEL_1;
    }

    public Beam[] getBeams(ImageView spaceShip) {
        System.out.println(tempreture);
        if (this.tempreture >= Constants.TEMPERATURE_LIMIT)
            return null;

        Beam[] beams = new Beam[beamLevel.ordinal() + 1];

        switch (beamLevel) {
            case LEVEL_1:
                Beam laser_beam = new Beam(5, 40);
                laser_beam.dx = 1;
                laser_beam.dy = 0;
                laser_beam.setFill(Color.RED);
                laser_beam.setArcWidth(5);
                laser_beam.setArcHeight(5);
                laser_beam.setTranslateY(spaceShip.getTranslateY() + 300);
                laser_beam.setTranslateX(spaceShip.getTranslateX());
                beams[0] = laser_beam;
                break;

            case LEVEL_2:
                for (int i = 0; i < beamLevel.ordinal() + 1; i++) {
                    Beam temp = new Beam(5, 40);
                    temp.dx = 1;
                    temp.dy = 0;
                    temp.setFill(Color.RED);
                    temp.setArcWidth(5);
                    temp.setArcHeight(5);
                    temp.setTranslateY(spaceShip.getTranslateY() + 300);
                    temp.setTranslateX(spaceShip.getTranslateX() - 25 + 50 * i);
                    beams[i] = temp;
                }
                break;

            case LEVEL_3:
                for (int i = 0; i < beamLevel.ordinal() + 1; i++) {
                    Beam temp = new Beam(5, 40);
                    temp.dx = 1;
                    temp.dy = 0;
                    temp.setFill(Color.RED);
                    temp.setArcWidth(5);
                    temp.setArcHeight(5);
                    temp.setTranslateY(spaceShip.getTranslateY() + 300);
                    temp.setTranslateX(spaceShip.getTranslateX() - 25 + 25 * i);
                    beams[i] = temp;
                }
                beams[1].setTranslateY(beams[1].getTranslateY() - 20);
                break;
            case LEVEL_4:
                // TODO: 4/21/2019 addRec other levels
                break;

        }

        return beams;
    }

    public void setLevel(BeamLevel level) {
        beamLevel = level;
    }

    public void decreaseTemp() {
        int nextTemp = tempreture - Constants.TEMPERATURE_DEC;
        this.tempreture = nextTemp <= 0 ? 0 :( nextTemp >= Constants.TEMPERATURE_LIMIT && nextTemp <= Constants.TEMPERATURE_TO_ZERO_LIMIT? 0 :nextTemp );
    }

    public void increaseTemp() {
        int nextTemp = tempreture + Constants.TEMPERATURE_INC;
        this.tempreture = nextTemp >= Constants.TEMPERATURE_LIMIT ? Constants.TEMPERATURE_PAST_LIMIT : nextTemp;

    }

    public double getTemperature() {
        return tempreture;
    }

    public BeamLevel getLevel(){
        return beamLevel;
    }
}
