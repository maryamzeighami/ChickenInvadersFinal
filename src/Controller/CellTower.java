package Controller;

import GUI.MainStageHolder;
import GUI.PlayersList;
import GUI.gameplay.GameSceneBuilder;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import models.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.GarbageCollectorMXBean;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CellTower {
    private ArrayList<Socket> sockets = new ArrayList<>();
    private ServerSocket serverSocket;
    private ArrayList<Scanner> scanners = new ArrayList<>();
    private ArrayList<PrintWriter> printWriters = new ArrayList<>();
    private final Object lock = new Object();
    private final Object lock2 = new Object();
    private Player player;
    int myIndex;
    private ArrayList<Player> players = new ArrayList<>();
    public GUI.menues.PlayersList playersList;

    public CellTower(Socket socket, Player player) throws IOException {
        this.player = player;
        addSocket(socket);
        transmitMyName(player.getName());
        playersList = new GUI.menues.PlayersList().build(players, false, this);
        MainStageHolder.stage.setScene(playersList.getScene());
    }

    public CellTower(ServerSocket serverSocket, Player player) {
        this.serverSocket = serverSocket;
        this.player = player;
        players.add(player);
        playersList = new GUI.menues.PlayersList().build(players, true, this);
        MainStageHolder.stage.setScene(playersList.getScene());
    }

    public void addSocket(Socket socket) throws IOException {
        synchronized (lock) {
            sockets.add(socket);
            Scanner scanner = new Scanner(socket.getInputStream());
            scanners.add(scanner);
            printWriters.add(new PrintWriter(socket.getOutputStream(), true));
            Thread thread = new Thread(() -> {
                String command = receiveText(scanner);
                while (true) { // dar vaghe ta zamani ke bazi tamoom nashode
                    switch (command) {
                        case "My Name:":
                            //client mifrese
                            //make new player, add to players, send everyone the Player Names
                            String line5 = receiveText(scanner);
                            while (!line5.equals("End of Transmission")) {
                                String name = line5;
                                Player player = new Player(name);
                                players.add(player);
                                Platform.runLater(() -> playersList.setBox(players));
                                //send everyone???
                                transmitPlayerNames();
                                line5 = receiveText(scanner);
                            }
                            break;
                        case "Player Names:":
                            //server mifrese
                            //if player names empty make new players add them to list and for the last one add yourself else make one new player and add the new player only


                            ArrayList<String> strings = new ArrayList<>();
                            String line7 = receiveText(scanner);
                            while (!line7.equals("End of Transmission")) {
                                strings.add(line7);
                                line7 = receiveText(scanner);
                            }


                            if (players.size() == 0) {
                                myIndex = strings.size() - 1;
                                for (String string : strings) {
                                    if (!string.equals(strings.get(myIndex))) {
                                        Player player1 = new Player(string);
                                        players.add(player1);
                                    } else {
                                        players.add(player);
                                    }


                                }
                            } else {
                                Player player1 = new Player(strings.get(strings.size() - 1));
                                players.add(player1);
                            }
                            Platform.runLater(() -> playersList.setBox(players));
                            break;
                        case "Shoot:":
                            //server va client mifresan
                            //shoot for player with the given index and send this to clients

                            String line4 = receiveText(scanner);
                            while (!line4.equals("End of Transmission")) {
                                int i = Integer.parseInt(line4);
                                transmitShoot(i, printWriters.get(scanners.indexOf(scanner)));
                                Beam[] temp = players.get(i).spaceShip.attackSystem.getBeams(players.get(i).spaceShip);
                                if (temp != null) {
                                    players.get(i).spaceShip.attackSystem.increaseTemp();
                                    Platform.runLater(() -> {
                                        GameSceneBuilder.currentGameSceneBuilder.beams.addAll(Arrays.asList(temp));
                                        GameSceneBuilder.currentGameSceneBuilder.stackPane.getChildren().addAll(temp);
                                    });
                                }

                                line4 = receiveText(scanner);
                            }
                            break;
                        case "Bomb:":
                            String line6 = receiveText(scanner);
                            while (!line6.equals("End of Transmission")) {
                                System.out.println("bomb");
                                int i = Integer.parseInt(line6);
                                transmitBomb(i, printWriters.get(scanners.indexOf(scanner)));
                                Platform.runLater(()->GameSceneBuilder.currentGameSceneBuilder.getBomb(i));
                                line6 = receiveText(scanner);
                            }
                            break;
                        case "Start":
                            //server mifrese
                            //start game
                            GameSceneBuilder gameSceneBuilder = new GameSceneBuilder();
                            gameSceneBuilder.builder(players, myIndex, this);
                            Platform.runLater(() -> MainStageHolder.stage.setScene(gameSceneBuilder.getScene()));
                            break;
                        case "Spaceship Positions:":
                        case "My Position:":
                            // server mifrese
                            // read all the given positions and set them
                            String line3 = receiveText(scanner);
                            while (!line3.equals("End of Transmission")) {
                                int i = Integer.parseInt(line3.split(" ")[0]);
                                double x = Double.parseDouble(line3.split(" ")[1]);
                                double y = Double.parseDouble(line3.split(" ")[2]);
                                if (i != myIndex)
                                    Platform.runLater(() -> {
                                        GameSceneBuilder.currentGameSceneBuilder.spaceShips.get(i).setTranslateY(y);
                                        GameSceneBuilder.currentGameSceneBuilder.spaceShips.get(i).setTranslateX(x);
                                    });
                                line3 = receiveText(scanner);
                            }
                            break;
                        case "Chicken Positions:":
                            // server mifrese
                            // read all the given positions and set them
                            String line2 = receiveText(scanner);
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
                                Platform.runLater(() -> {
                                    //GameSceneBuilder.currentGameSceneBuilder.stackPane.getChildren().removeAll(chickens);
                                    synchronized (lock2) {
                                        GameSceneBuilder.currentGameSceneBuilder.stackPane.getChildren().removeAll(GameSceneBuilder.currentGameSceneBuilder.chickens);
                                        GameSceneBuilder.currentGameSceneBuilder.chickens = chickens;
                                        GameSceneBuilder.currentGameSceneBuilder.stackPane.getChildren().addAll(GameSceneBuilder.currentGameSceneBuilder.chickens);
                                    }
                                });
                                line2 = receiveText(scanner);
                            }
                            break;
                        case "Chicken Drops:":
                            // server mifrese
                            // given chicken drops given item
                            String line1 = receiveText(scanner);
                            while (!line1.equals("End of Transmission")) {
                                synchronized (GameSceneBuilder.lock) {
                                    int x = Integer.parseInt(line1.split(" ")[0]);
                                    String stuff = line1.split(" ")[1];

                                    if (stuff.equals("egg")) {
                                        if (GameSceneBuilder.currentGameSceneBuilder.chickens.get(x) instanceof Giant)
                                            Platform.runLater(() -> GameSceneBuilder.currentGameSceneBuilder.throwGiantEgg(x));
                                        else {
                                            Platform.runLater(() -> GameSceneBuilder.currentGameSceneBuilder.throwEggP(x));
                                        }
                                    } else if (stuff.equals("seed"))
                                        Platform.runLater(() -> GameSceneBuilder.currentGameSceneBuilder.throwSeedP(x));
                                    else if (stuff.equals("powerUp1"))
                                        Platform.runLater(() -> GameSceneBuilder.currentGameSceneBuilder.throwPowerUp(x, 1));
                                    else if (stuff.equals("powerUp2"))
                                        Platform.runLater(() -> GameSceneBuilder.currentGameSceneBuilder.throwPowerUp(x, 2));
                                    line1 = receiveText(scanner);
                                }

                            }
                            break;
                        case "Scores:":
                            // server mifrese
                            // set all the scores to the given numbers
                            String line = receiveText(scanner);
                            while (!line.equals("End of Transmission")) {
                                int playerIndex = Integer.parseInt(line.split(" ")[0]);
                                int score = Integer.parseInt(line.split(" ")[1]);
                                players.get(playerIndex).score = score;
                                line = receiveText(scanner);
                            }
                            break;
                    }
                    try {
                        command = receiveText(scanner);
                    } catch (NoSuchElementException ignored) {
                    }

                }

            });
            thread.start();
        }
    }


    private void transmitText(String text) {
        synchronized (lock) {
            for (PrintWriter printWriter : printWriters)
                printWriter.println(text);
        }
    }

    void transmitText(String text, PrintWriter exception) {
        synchronized (lock) {
            for (PrintWriter printWriter : printWriters)
                if (!printWriter.equals(exception))
                    printWriter.println(text);
        }
    }

    String receiveText(Scanner scanner) {
        String receivedText = scanner.nextLine();
        //System.out.println("client " + scanners.indexOf(scanner) + ": " + receivedText);
        return receivedText;
    }

    public void transmitScores() {
        synchronized (lock) {
            transmitText("Scores:");
            for (int i = 0; i < players.size(); i++) {
                transmitText(i + " " + players.get(i).score);
            }
            transmitText("End of Transmission");
        }
    }

    public void transmitChickenDrops(int i, String stuff) {
        synchronized (lock) {
            transmitText("Chicken Drops:");
            transmitText(Integer.toString(i) + " " + stuff);
            transmitText("End of Transmission");
        }
    }

    public void transmitChickenPositions(ArrayList<Chicken> chickens) {
        synchronized (lock) {
            transmitText("Chicken Positions:");
            StringBuilder positions = new StringBuilder();
            for (int i = 0; i < chickens.size(); i++) {
                int level = chickens.get(i).getlevel();
                double x = chickens.get(i).getTranslateX();
                double y = chickens.get(i).getTranslateY();
                if (!positions.toString().equals(""))
                    positions.append(";");
                positions.append(Integer.toString(level)).append(" ").append(Double.toString(x)).append(" ").append(Double.toString(y));
            }
            transmitText(positions.toString());
            transmitText("End of Transmission");
        }
    }

    public void transmitSpaceShipPositions(ArrayList<SpaceShip> spaceShips) {
        synchronized (lock) {
            transmitText("Spaceship Positions:");
            for (int i = 0; i < spaceShips.size(); i++) {
                double x = spaceShips.get(i).getTranslateX();
                double y = spaceShips.get(i).getTranslateY();
                transmitText(Integer.toString(i) + " " + Double.toString(x) + " " + Double.toString(y));
            }
            transmitText("End of Transmission");
        }
    }

    public void transmitMyPosition(ArrayList<SpaceShip> spaceShips) {
        synchronized (lock) {
            transmitText("My Position:");
            double x = spaceShips.get(myIndex).getTranslateX();
            double y = spaceShips.get(myIndex).getTranslateY();
            transmitText(Integer.toString(myIndex) + " " + Double.toString(x) + " " + Double.toString(y));
            transmitText("End of Transmission");
        }
    }

    public void transmitStart() {
        synchronized (lock) {
            transmitText("Start");
            transmitText("End of Transmission");
            GameSceneBuilder gameSceneBuilder = new GameSceneBuilder();
            gameSceneBuilder.builder(players, myIndex, this);
            MainStageHolder.stage.setScene(gameSceneBuilder.getScene());
        }
    }

    public void transmitShoot(int i) {
        synchronized (lock) {
            transmitText("Shoot:");
            transmitText(Integer.toString(i));
            transmitText("End of Transmission");
        }
    }

    private void transmitShoot(int i, PrintWriter exception) {
        synchronized (lock) {
            transmitText("Shoot:", exception);
            transmitText(Integer.toString(i), exception);
            transmitText("End of Transmission", exception);
        }
    }

    public void transmitBomb(int i) {
        synchronized (lock) {
            transmitText("Bomb:");
            transmitText(Integer.toString(i));
            transmitText("End of Transmission");
        }
    }

    private void transmitBomb(int i, PrintWriter exception) {
        synchronized (lock) {
            transmitText("Bomb:", exception);
            transmitText(Integer.toString(i), exception);
            transmitText("End of Transmission", exception);
        }
    }

    private void transmitMyName(String name) {
        synchronized (lock) {
            transmitText("My Name:");
            transmitText(name);
            transmitText("End of Transmission");
        }
    }


    private void transmitPlayerNames() {
        synchronized (lock) {
            transmitText("Player Names:");
            for (Player player : players) {
                transmitText(player.getName());
            }
            transmitText("End of Transmission");
        }
    }

    void closeSockets() {
        synchronized (lock) {
            try {
                for (Socket socket : sockets)
                    socket.close();
                if (serverSocket != null)
                    serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}