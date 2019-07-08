package GUI.menues.choosePlayerScene;

import GUI.MainStageHolder;
import GUI.menues.MainMenuSceneBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import models.DataBase;
import models.Player;

import java.io.File;
import java.util.ArrayList;

public class ChoosePlayerSceneBuilder {

    private Scene scene;
    private Button addPlayerBut;
    private Button deletPlayerBut;
    private Button enterBut;
    public VBox vbox = new VBox(0.5);
    private TableView<Player> table;
    public static  boolean reNewTable= false;



    public ChoosePlayerSceneBuilder build (ArrayList<Player> players) {

        //buttons
        addPlayerBut = new Button("add player");
        deletPlayerBut = new Button("delete player");
        enterBut = new Button("enter");

        enterBut.setDisable(true);
        deletPlayerBut.setDisable(true);

        //buttons style
        enterBut.setStyle("-fx-padding: 20;-fx-background-color:#ffba2d; -fx-text-fill: Black");
        addPlayerBut.setStyle("-fx-padding: 20;-fx-background-color:#ffba2d; -fx-text-fill: Black");
        deletPlayerBut.setStyle("-fx-padding: 20;-fx-background-color:#ffba2d; -fx-text-fill: Black");


        TableColumn<Player, String> playerNameColumn = new TableColumn<>("Player Name");
        playerNameColumn.setMinWidth(300);
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        table = new TableView<>();

        table.setItems(getPlayers());
        table.getColumns().addAll(playerNameColumn);
        table.setStyle("-fx-background-color: #aebdab ; -fx-graphic-text-gap: 10");
        table.getOnScrollTo();
//        table.setMinHeight(100);
//        table.setMinWidth(50);



        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(table.getSelectionModel().getSelectedItem() != null){
                enterBut.setDisable(false);
                deletPlayerBut.setDisable(false);
            } else {
                enterBut.setDisable(true);
                deletPlayerBut.setDisable(true);
            }
        });


        vbox.getChildren().addAll(table);
        vbox.setAlignment(Pos.CENTER);



        // delet players
        deletPlayerBut.setOnAction(event -> {
            Player selectedItem = table.getSelectionModel().getSelectedItem();
            DataBase.getInstance().getPlayers().remove(selectedItem);
            table.getItems().remove(selectedItem);
        });


        // addRec player to list
        addPlayerBut.setOnAction(event -> {
            add();
        });

        //enter game
        enterBut.setOnAction(event -> {
            //todo
            MainMenuSceneBuilder builder = new MainMenuSceneBuilder();
//            builder.build(getPlayer(table.getSelectionModel().getSelectedItem()));
            builder.build(table.getSelectionModel().getSelectedItem());
            MainStageHolder.stage.setScene(builder.getScene());
        });


        HBox hBox = new HBox(0.5);
        hBox.getChildren().addAll(enterBut, deletPlayerBut, addPlayerBut);

//
        GridPane gridPane = new GridPane();
        gridPane.setHgap(8);
        gridPane.setPadding(new Insets(130, 130, 130, 130));
        GridPane.setConstraints(hBox, 5, 100);
        GridPane.setConstraints(vbox, 5,200);

        gridPane.getChildren().addAll(hBox,vbox);

        hBox.setAlignment(Pos.CENTER_LEFT);
        vbox.setAlignment(Pos.CENTER);


        // BackGround
        String addr = System.getProperty("user.dir") + "/src/pics/back1.jpg";
        File file = new File(addr);
        Image image = new Image(file.toURI().toString());
        StackPane pane = new StackPane();
        pane.getChildren().add(new ImageView(image));
        pane.getChildren().addAll(gridPane);

        scene = new Scene(pane, 1100, 800);


        return this;
    }

    private void add() {
        AddPlayerBox addPlayerBox = new AddPlayerBox(vbox);
        addPlayerBox.Display(table);


    }

    //get player
//    public Player getPlayer(Player player) {
//        for (int i = 0; i < vbox.getChildren().size(); i++) {
//            if (vbox.getChildren().get(i) instanceof CheckBox) {
//                if (((CheckBox) vbox.getChildren().get(i)).isSelected()) {
//                    for (Player player : DataBase.getInstance().getPlayers()) {
//                        if (player.getName().equals(((CheckBox) vbox.getChildren().get(i)).getText())){
//                            return player;
//                        }
//                    }
//                }
//            }
//        } return null;
//    }

    public static ObservableList<Player> getPlayers() {
        ObservableList<Player> players = FXCollections.observableArrayList();
        players.addAll(DataBase.getInstance().getPlayers());
        return players;
    }


    public Scene getScene() {
        return scene;
    }
}
