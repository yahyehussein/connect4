package client.GUI;

import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GameModeScene {
	public static HBox backBtn(Stage primaryStage) {
		HBox hBox = new HBox();

		hBox.setAlignment(Pos.BASELINE_LEFT);
		hBox.setPadding(new Insets(0, 0, 15, 20));

		Button backBtn = new Button("Back");
		backBtn.getStyleClass().add("round-red");

		Interface mainManu = new Interface();

		backBtn.setOnAction(e -> {
			try {
				mainManu.start(primaryStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		hBox.getChildren().add(backBtn);

		return hBox;
	}

	public static Scene layout(Stage primaryStage) {
		BorderPane border = new BorderPane();
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(15, 0, 0, 0));
		grid.setAlignment(Pos.BASELINE_CENTER);
		grid.setHgap(10);
		
		Button online = new Button("Online");
		online.getStyleClass().add("modeBtn");
		online.setOnAction(e -> {
			try {
				primaryStage.setScene(OnlineScene.layout(primaryStage));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		Button offline = new Button("Offline");
		offline.getStyleClass().add("modeBtn");
		offline.setOnAction(e -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information ");
			alert.setHeaderText("While playing the game you use ESC Keypress to display the main menus with Save, Load and Exit to Main Menu");
			alert.setContentText(null);
			
			alert.showAndWait();
			
			primaryStage.setScene(PlayerOptionScene.layout(primaryStage));
		});
		
		grid.add(online, 0, 0);
		grid.add(offline, 1, 0);
		
		border.setTop(Interface.logo());
		border.setCenter(grid);
		border.setBottom(backBtn(primaryStage));
		
		Scene scene = new Scene(border, 800, 500);
		
		scene.getStylesheets().add(Interface.class.getResource("style.css").toExternalForm());
		
		return scene;
	}
}
