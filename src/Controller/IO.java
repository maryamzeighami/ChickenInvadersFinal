package Controller;

import com.gilecode.yagson.com.google.gson.Gson;
import models.DataBase;
import models.Player;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IO {


    public static void loadSavedData() {

        // reading game.data
        FileReader fr = null;
        try {
            fr = new FileReader(System.getProperty("user.dir") + "/save/game.data");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String json = "";
        int i;
        try {
            while ((i = fr.read()) != -1)
                json = json.concat(String.valueOf((char) i));
            System.out.print((char) i);

        } catch (Exception e) {
        }

        // game.data (json) to object
        Gson gson = new Gson();
        DataBase loadedDataBase = gson.fromJson(json, DataBase.class);
        DataBase.setInstance(loadedDataBase);

    }

    public static void saveGame() {

        String json = dataBaseToJson();

        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(System.getProperty("user.dir") + "/save/game.data");
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String dataBaseToJson() {
        Gson gson = new Gson();
        String json = gson.toJson(DataBase.getInstance());
        return json;
    }

}
