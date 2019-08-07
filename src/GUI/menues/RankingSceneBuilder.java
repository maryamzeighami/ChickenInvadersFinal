package GUI.menues;

import GUI.MainStageHolder;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

public class RankingSceneBuilder {

    ArrayList<Player> players;
    Button button;

    public static Scene scene;
    public ArrayList<Text> texts = new ArrayList<>();

    public RankingSceneBuilder build(ArrayList<Player> players){
        this.players=players;
        button= new Button("back to menu");
        button.setMinHeight(20);
        button.setMinWidth(60);
        button.setStyle("-fx-background-color: #0c807d ; -fx-font-size: 30");
        button.setOnAction(event -> {
            MainStageHolder.stage.setScene(MainMenuSceneBuilder.getScene());
        });

        players.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {

                 return o2.score-o1.score;
            }
        });

        VBox vBox= new VBox();
        for (Player player : players) {
            Text text= new Text(player.getName() + "............"+ Integer.toString(player.score));
            text.setStyle("-fx-font-size: 36 ; -fx-fill: White");
            texts.add(text);

        }

        vBox.getChildren().addAll(texts);
        vBox.getChildren().addAll(button);
        vBox.setAlignment(Pos.CENTER);


        String addr = System.getProperty("user.dir") + "/src/pics/back4.png";
        File file = new File(addr);
        Image image = new Image(file.toURI().toString());
        StackPane pane = new StackPane();
        pane.getChildren().add(new ImageView(image));
        pane.getChildren().addAll(vBox);


        scene = new Scene(pane, 1400, 800);
        return this;

    }
    public static Scene getScene(){
        return scene;
    }

}
