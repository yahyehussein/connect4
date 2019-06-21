package client.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreditsScene {
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
		StackPane stack = new StackPane();
		
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.BASELINE_CENTER);
		
		vBox.getChildren().addAll(
				Interface.logo(),
				new Label("Authors"),
				new Label("Percy Wepngong Shey | S15117403"),
				new Label("Yahye Hussein | S16111235"),
				new Label("Eshan Ali | s16116049"),
				new Label("Hanad Ahmed | S16106640"),
				backBtn(primaryStage)
				
		);
		
		stack.getChildren().add(vBox);
		
		Scene scene = new Scene(stack, 800, 500);
		scene.getStylesheets().add(Interface.class.getResource("style.css").toExternalForm());
		scene.getStylesheets().add(Interface.class.getResource("labelStyle.css").toExternalForm());
		
		return scene;
	}
}
