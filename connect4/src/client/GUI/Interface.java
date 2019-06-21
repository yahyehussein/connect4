package client.GUI;

import client.GameProgress;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Interface extends Application {
	private BorderPane border = new BorderPane();

	public static void main(String[] args) {
		launch(args);
	}

	public static HBox logo() {
		HBox logoImg = new HBox();
		logoImg.setPadding(new Insets(10, 0, 0, 0));
		logoImg.setAlignment(Pos.BASELINE_CENTER);

		Image connect4Logo = new Image("client/GUI/images/header_logo.png", 500, 150, false, false);
		ImageView ivLogo = new ImageView(connect4Logo);

		logoImg.getChildren().add(ivLogo);

		return logoImg;
	}

	public VBox menu(Stage primaryStage) {
		VBox menus = new VBox();

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.BOTTOM_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 40, 50));

		Button newgameBtn = new Button("NEW GAME");
		newgameBtn.getStyleClass().add("menuBtn");
		newgameBtn.setOnAction(e -> primaryStage.setScene(GameModeScene.layout(primaryStage)));
		Button loadBtn = new Button("LOAD GAME");
		loadBtn.getStyleClass().add("menuBtn");
		loadBtn.setOnAction(e -> {
			GameProgress gameProgress = new GameProgress(primaryStage);
			gameProgress.load();
		});
		Button creditsBtn = new Button("CREDITS");
		creditsBtn.getStyleClass().add("menuBtn");
		creditsBtn.setOnAction(e -> primaryStage.setScene(CreditsScene.layout(primaryStage)));

		grid.add(newgameBtn, 0, 0, 2, 1);
		grid.add(loadBtn, 0, 1, 2, 1);
		grid.add(creditsBtn, 0, 2, 2, 1);

		menus.getChildren().add(grid);

		return menus;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	    double width = 800;
	    double height = 500;

		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("client/GUI/images/c4_icon.png"));

		border.setTop(logo());
		border.setLeft(menu(primaryStage));

		Scene scene = new Scene(border, width, height);

		scene.getStylesheets().add(Interface.class.getResource("style.css").toExternalForm());

		primaryStage.setTitle("Connect4");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
