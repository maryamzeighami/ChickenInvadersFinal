package models;

import java.util.ArrayList;

public class DataBase {
    public static DataBase instance = null;
    private ArrayList<Player> players = new ArrayList<>();

    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
            return instance;
        } else return instance;
    }

    public static void setInstance(ArrayList<Player> players) {
        instance = new DataBase();
        instance.players = players;
    }


    public ArrayList<Player> getPlayers() {
        return players;
    }
}
