package Controller;

import GUI.MainStageHolder;
import GUI.gameplay.GameSceneBuilder;
import javafx.application.Platform;
import javafx.scene.image.Image;
import models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Save {
    private File file = new File(System.getProperty("user.dir") + "/save/game.data");
    private Scanner scanner;
    private PrintWriter printWriter;

    public void save() throws FileNotFoundException {
        printWriter = new PrintWriter(file);
        DataBase dataBase = DataBase.getInstance();
        for (Player player: dataBase.getPlayers()){
            transmitMyName(player.name);
            if (player.getCurrentGame() != null) {
                transmitGame(player);
                transmitMyPosition(player.spaceShip);
                transmitChickenPositions(player.getCurrentGame().chickens);
            }
        }
        transmitText("End of File");
        printWriter.close();
    }

    public void load() throws IOException {
        scanner = new Scanner(file);
        String command = receiveText();
        ArrayList<Player> players = new ArrayList<>();
        Player player = null;
        SpaceShip spaceShip = null;
        Game game = null;
        while (!command.equals("End of File")) {
            switch (command) {
                case "My Name:":
                    String line5 = receiveText();
                    while (!line5.equals("End of Transmission")) {
                        String name = line5;
                        player = new Player(name);
                        players.add(player);
                        line5 = receiveText();
                    }
                    break;
                case "My Position:":
                    String line3 = receiveText();
                    while (!line3.equals("End of Transmission")) {
                        double x = Double.parseDouble(line3.split(" ")[0]);
                        double y = Double.parseDouble(line3.split(" ")[1]);
                        spaceShip = new SpaceShip(new Image(new File(System.getProperty("user.dir") + "/src/pics/spaceship.png").toURI().toString()));
                        spaceShip.setTranslateY(y);
                        spaceShip.setTranslateX(x);
                        game.spaceShip = spaceShip;
                        line3 = receiveText();
                    }
                    break;
                case "Chicken Positions:":
                    // server mifrese
                    // read all the given positions and set them
                    String line2 = receiveText();
                    while (!line2.equals("End of Transmission")) {
                        ArrayList<Chicken> chickens = new ArrayList<>();
                        for (String position : line2.split(";")) {
                            int level = Integer.parseInt(position.split(" ")[0]);
                            double x = Double.parseDouble(position.split(" ")[1]);
                            double y = Double.parseDouble(position.split(" ")[2]);
                            Chicken chicken;
                            switch (level) {
                                case 1:
                                    chicken = new Chicken1();
                                    break;
                                case 2:
                                    chicken = new Chicken2();
                                    break;
                                case 3:
                                    chicken = new Chicken3();
                                    break;
                                case 4:
                                    chicken = new Chicken4();
                                    break;
                                default:
                                    chicken = new Giant(1);
                            }
                            chicken.setTranslateX(x);
                            chicken.setTranslateY(y);
                            chickens.add(chicken);
                        }
                        game.chickens = chickens;
                        player.setCurrentGame(game);
                        line2 = receiveText();
                    }
                    break;
                case "Game:":
                    String line = receiveText();
                    while (!line.equals("End of Transmission")) {
                        int score = Integer.parseInt(line.split(" ")[0]);
                        int heartNum = Integer.parseInt(line.split(" ")[1]);
                        int numberOfBombs = Integer.parseInt(line.split(" ")[2]);
                        int numberOfSeeds = Integer.parseInt(line.split(" ")[3]);
                        int level = Integer.parseInt(line.split(" ")[4]);
                        int wave= Integer.parseInt(line.split(" ")[5]);
                        game = new Game(score,heartNum,numberOfBombs,numberOfSeeds,level,wave);
                        line = receiveText();
                    }
                    break;
            }
            try {
                command = receiveText();
            } catch (NoSuchElementException ignored) { }
        }
        DataBase.setInstance(players);
        scanner.close();
    }


    private void transmitText(String text) {
        printWriter.println(text);
    }


    private String receiveText() {
        return scanner.nextLine();
    }

    private void transmitGame(Player player) {
        transmitText("Game:");
        transmitText(String.valueOf(player.getCurrentGame().score) + " " + String.valueOf(player.getCurrentGame().healthNumber) + " " + String.valueOf(player.getCurrentGame().numberOfBombs) + " " +String.valueOf(player.getCurrentGame().numberOfSeeds) + " " +String.valueOf(player.getCurrentGame().level) + " " + String.valueOf(player.getCurrentGame().wave) + " ");
        transmitText("End of Transmission");
    }

    private void transmitChickenPositions(ArrayList<Chicken> chickens) {
            transmitText("Chicken Positions:");
            StringBuilder positions = new StringBuilder();
            for (Chicken chicken : chickens) {
                int level = chicken.getlevel();
                double x = chicken.getTranslateX();
                double y = chicken.getTranslateY();
                if (!positions.toString().equals(""))
                    positions.append(";");
                positions.append(Integer.toString(level)).append(" ").append(Double.toString(x)).append(" ").append(Double.toString(y));
            }
            transmitText(positions.toString());
            transmitText("End of Transmission");
    }

    private void transmitMyPosition(SpaceShip spaceShip) {
        transmitText("My Position:");
        double x = spaceShip.getTranslateX();
        double y = spaceShip.getTranslateY();
        transmitText(Double.toString(x) + " " + Double.toString(y));
        transmitText("End of Transmission");
    }


    private void transmitMyName(String name) {
        transmitText("My Name:");
        transmitText(name);
        transmitText("End of Transmission");
    }

}