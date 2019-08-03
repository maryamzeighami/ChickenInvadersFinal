package Controller;

import GUI.MainStageHolder;
import GUI.PlayersList;
import GUI.gameplay.GameSceneBuilder;
import javafx.scene.image.ImageView;
import models.Beam;
import models.Game;
import models.Giant;
import models.Player;

import java.io.IOException;
import java.io.PrintWriter;
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
    private Player player;
    int myIndex;
    private ArrayList<Player> players = new ArrayList<>();
    public GUI.menues.PlayersList  playersList = new GUI.menues.PlayersList();

    public CellTower(Socket socket, Player player) throws IOException {
        this.player = player;
        addSocket(socket);
        transmitMyName(player.getName());
        GUI.menues.PlayersList playersList = new GUI.menues.PlayersList().build(players);
        MainStageHolder.stage.setScene(playersList.getScene());

    }

    public CellTower(ServerSocket serverSocket, Player player) {
        this.serverSocket = serverSocket;
        this.player = player;
        players.add(player);
        playersList=playersList.build(players);
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
                            while (!line5.equals("Enf of Transmission")) {
                                String name = line5;
                                Player player = new Player(name);
                                players.add(player);
                                playersList.setBox(players);
                                //send everyone???
                                transmitPlayerNames();
                                line5 = receiveText(scanner);
                            }
                            System.out.println("Myname");
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
                                        playersList.setBox(players);
                                    }


                                }
                            } else {
                                Player player1 = new Player(strings.get(strings.size() - 1));
                                players.add(player1);
                                playersList.setBox(players);
                            }
                            System.out.println("playernames");

                            break;
                        case "Shoot:":
                            //server va client mifresan
                            //shoot for player with the given index and send this to clients

                            String line4 = receiveText(scanner);
                            while (!line4.equals("End of Transmission")) {
                                int i = Integer.parseInt(line4);

                                Beam[] temp = players.get(i).spaceShip.attackSystem.getBeams(players.get(i).spaceShip);
                                if (temp != null) {
                                    players.get(i).spaceShip.attackSystem.increaseTemp();
                                    GameSceneBuilder.currentGameSceneBuilder.beams.addAll(Arrays.asList(temp));
                                    GameSceneBuilder.currentGameSceneBuilder.stackPane.getChildren().addAll(temp);
                                }

                                line4 = receiveText(scanner);
                            }
                            break;
                        case "Start":
                            //server mifrese
                            //start game
                            GameSceneBuilder gameSceneBuilder = new GameSceneBuilder();
                            gameSceneBuilder.builder(players, myIndex);
                            break;
                        case "Spaceship Positions:":
                            // server mifrese
                            // read all the given positions and set them
                            String line3 = receiveText(scanner);
                            while (!line3.equals("End of Transmission")) {
                                int i = Integer.parseInt(line3.split(" ")[0]);
                                double x = Double.parseDouble(line3.split(" ")[1]);
                                double y = Double.parseDouble(line3.split(" ")[2]);
                                GameSceneBuilder.currentGameSceneBuilder.spaceShips.get(i).setTranslateY(y);
                                GameSceneBuilder.currentGameSceneBuilder.spaceShips.get(i).setTranslateX(x);
                                line3 = receiveText(scanner);

                            }
                            break;
                        case "Chicken Positions:":
                            // server mifrese
                            // read all the given positions and set them
                            String line2 = receiveText(scanner);
                            while (!line2.equals("End of Transmission")) {

                                int i = Integer.parseInt(line2.split(" ")[0]);
                                double x = Double.parseDouble(line2.split(" ")[1]);
                                double y = Double.parseDouble(line2.split(" ")[2]);
                                GameSceneBuilder.currentGameSceneBuilder.chickens.get(i).setTranslateY(y);
                                GameSceneBuilder.currentGameSceneBuilder.chickens.get(i).setTranslateX(x);
                                line2 = receiveText(scanner);

                            }
                            break;
                        case "Chicken Drops:":
                            // server mifrese
                            // given chicken drops given item
                            String line1 = receiveText(scanner);
                            while (!line1.equals("End of Transmission")) {
                                int x = Integer.parseInt(line1.split(" ")[0]);
                                String stuff = line1.split(" ")[1];

                                if (stuff.equals("egg")) {
                                    if (GameSceneBuilder.currentGameSceneBuilder.chickens.get(x) instanceof Giant)
                                        GameSceneBuilder.currentGameSceneBuilder.throwGiantEgg(x);
                                    else {
                                        GameSceneBuilder.currentGameSceneBuilder.throwEggP(x);
                                    }
                                } else if (stuff.equals("seed"))
                                    GameSceneBuilder.currentGameSceneBuilder.throwSeedP(x);
                                else if (stuff.equals("powerUp1"))
                                    GameSceneBuilder.currentGameSceneBuilder.throwPowerUp(x,1);
                                else if (stuff.equals("powerUp2"))
                                    GameSceneBuilder.currentGameSceneBuilder.throwPowerUp(x,2);
                                line1 = receiveText(scanner);

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


    void transmitText(String text) {
        synchronized (lock) {
            for (PrintWriter printWriter : printWriters)
                printWriter.println(text);
        }
    }

    String receiveText(Scanner scanner) {
        String receivedText = scanner.nextLine();
        System.out.println("client " + scanners.indexOf(scanner) + ": " + receivedText);

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

    public void transmitChickenPositions(int i, double x, double y) {
        synchronized (lock) {
            transmitText("Chicken Positions:");
            transmitText(Integer.toString(i) + " " + Double.toString(x) + " " + Double.toString(y));
            transmitText("End of Transmission");
        }
    }


   public void transmitSpaceShipPositions(int i, double x, double y) {
        synchronized (lock) {
            transmitText("Spaceship Positions");
            transmitText(Integer.toString(i) + " " + Double.toString(x) + " " + Double.toString(y));
            transmitText("End of Transmission");
        }
    }

    public void transmitStart() {
        synchronized (lock) {
            transmitText("Start");
            transmitText("End of Transmission");
        }
    }

   public void transmitShoot(int i) {
        synchronized (lock) {
            transmitText("Shoot");
            transmitText(Integer.toString(i));
            transmitText("End of Transmission");
        }
    }

    public void transmitMyName(String name) {
        synchronized (lock) {
            transmitText("My Name:");
            transmitText(name);
            transmitText("End of Transmission");
        }
    }


    void transmitPlayerNames() {
        synchronized (lock) {
            transmitText("My Name:");
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