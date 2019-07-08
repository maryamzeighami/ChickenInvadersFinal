package models;

import java.io.Serializable;
import java.util.ArrayList;

public class DataBase implements Serializable {
    public static DataBase instance = null;
    private ArrayList<Player> players = new ArrayList<>();

    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
            return instance;
        } else return instance;
    }

    public static void setInstance(DataBase dataBase) {
        instance = dataBase;
    }


    public ArrayList<Player> getPlayers() {
        return players;
    }
}
