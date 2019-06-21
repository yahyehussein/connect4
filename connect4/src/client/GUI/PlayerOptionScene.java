package client.GUI;

import java.util.Optional;

import client.Player;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PlayerOptionScene extends Player {
	private static Button startGameBtn;

	private static TextField p1nicknameTxt;
	private static TextField p2nicknameTxt;

	public static HBox startGame(Stage primaryStage) {
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(50, 0, 0, 0));
		hBox.setAlignment(Pos.BASELINE_CENTER);

		startGameBtn = new Button("START GAME");

		startGameBtn.setOnAction(e -> {
			if (p1nicknameTxt.getText().equals("") && p1nicknameTxt.getText().equals("")) {
				getPlayerNickname().put("Player 1", "Player 1");
				getPlayerNickname().put("Player 2", "Player 2");
			} else {
				getPlayerNickname().put("Player 1", p1nicknameTxt.getText());
				getPlayerNickname().put("Player 2", p2nicknameTxt.getText());
			}

			if (getPlayerDice().get("Player 1") == getPlayerDice().get("Player 2")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Same Dice Colour");
				alert.setHeaderText(null);
				alert.setContentText(
						"Please every player must select a dice colour either (Red/Yellow) and have a unique dice colour");

				alert.showAndWait();
			} else if (getPlayerDice().get("Player 1") == "Select Dice"
					|| getPlayerDice().get("Player 2") == "Select Dice") {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Select Dice");
				alert.setHeaderText(null);
				alert.setContentText(
						"Please every player must select a dice colour either (Red/Yellow) and have a unique dice colour");

				alert.showAndWait();

			} else if (getPlayerDice().get("Player 1") == null || getPlayerDice().get("Player 2") == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Select Dice");
				alert.setHeaderText(null);
				alert.setContentText("Please every player must select a game mode either (Human/AI), but two player cannot be an AI");

				alert.showAndWait();
			} else if (getPlayerGameMode().get("Player 1").equals("AI") && getPlayerGameMode().get("Player 2").equals("AI")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Same Dice Colour");
				alert.setHeaderText(null);
				alert.setContentText("Please every player must select a game mode either (Human/AI), but two player cannot be an AI");

				alert.showAndWait();
			} else if (getPlayerGameMode().get("Player 1") == "Select Game Mode"
					|| getPlayerDice().get("Player 2") == "Select Game Mode") {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Select Dice");
				alert.setHeaderText(null);
				alert.setContentText("Please every player must select a game mode either (Human/AI), but two player cannot be an AI");

				alert.showAndWait();

			} else if (getPlayerGameMode().get("Player 1") == null || getPlayerGameMode().get("Player 2") == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Select Dice");
				alert.setHeaderText(null);
				alert.setContentText("Please every player must select a game mode either (Human/AI), but two player cannot be an AI");

				alert.showAndWait();
			} else {
				GameBoardScene gameBoardScene = new GameBoardScene();
				primaryStage.setScene(gameBoardScene.layout(primaryStage));
			}
		});

		startGameBtn.getStyleClass().add("menuBtn");

		hBox.getChildren().add(startGameBtn);

		return hBox;
	}

	public static VBox playerOneOptions() {
		VBox vBox = new VBox();

		ChoiceBox<String> GameModeCb = new ChoiceBox<String>(
				FXCollections.observableArrayList("Select Game Mode", "Human" + "                   ", "AI"));

		GameModeCb.getStyleClass().add("choiceBoxCB");
		GameModeCb.setMaxWidth(300);
		GameModeCb.setPadding(new Insets(5, 0, 5, 0));
		GameModeCb.getSelectionModel().selectFirst();
		GameModeCb.getStyleClass().add("dark-blue");

		GameModeCb.setOnAction(event -> {
			getPlayerGameMode().put("Player 1", GameModeCb.getSelectionModel().getSelectedItem());

			if (GameModeCb.getSelectionModel().getSelectedItem() == "AI") {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Difficulty");
				alert.setHeaderText("Choose the difficult level for you computer player");
				alert.setContentText("Choose your option.");

				ButtonType buttonTypeOne = new ButtonType("Easy");
				ButtonType buttonTypeTwo = new ButtonType("Medium");
				ButtonType buttonTypeThree = new ButtonType("Advanced");

				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonTypeOne) {
					setPlayerAIDifficulty("Easy");
				} else if (result.get() == buttonTypeTwo) {
					setPlayerAIDifficulty("Medium");
				} else if (result.get() == buttonTypeThree) {
					setPlayerAIDifficulty("Advanced");
				}
			}
		});

		HBox hBox = new HBox();
		hBox.setAlignment(Pos.BASELINE_CENTER);

		Image redDiceImg = new Image("client/GUI/images/red-dice.png");
		ImageView redDiceImgView = new ImageView(redDiceImg);
		HBox.setMargin(redDiceImgView, new Insets(0, 40, 0, 0));

		Image yellowDiceImg = new Image("client/GUI/images/yellow-dice.png");
		ImageView yellowDiceImgView = new ImageView(yellowDiceImg);

		ChoiceBox<String> DiceCb = new ChoiceBox<String>(
				FXCollections.observableArrayList("Select Dice", "Red", "Yellow"));
		DiceCb.getStyleClass().add("choiceBoxCB");
		DiceCb.setMaxWidth(300);
		DiceCb.setPadding(new Insets(5, 0, 5, 0));
		DiceCb.getSelectionModel().selectFirst();
		DiceCb.getStyleClass().add("dark-blue");

		DiceCb.setOnAction(event -> {
			getPlayerDice().put("Player 1", DiceCb.getSelectionModel().getSelectedItem());
		});

		HBox hBoxForTextField = new HBox();
		hBoxForTextField.setPadding(new Insets(20, 0, 0, 0));
		hBoxForTextField.setAlignment(Pos.BASELINE_CENTER);

		p1nicknameTxt = new TextField();
		p1nicknameTxt.setPromptText("Enter your nickname");
		p1nicknameTxt.setPadding(new Insets(10, 10, 10, 10));
		p1nicknameTxt.setPrefWidth(208);

		hBox.getChildren().addAll(redDiceImgView, yellowDiceImgView);
		hBoxForTextField.getChildren().add(p1nicknameTxt);

		vBox.setPadding(new Insets(60, 20, 0, 20));
		hBox.setPadding(new Insets(20, 0, 20, 0));

		vBox.getChildren().addAll(GameModeCb, hBox, DiceCb, hBoxForTextField);

		return vBox;
	}

	public static VBox playerTwoOptions() {
		VBox vBox = new VBox();

		ChoiceBox<String> GameModeCb = new ChoiceBox<String>(
				FXCollections.observableArrayList("Select Game Mode", "Human" + "                   ", "AI"));
		GameModeCb.getStyleClass().add("choiceBoxCB");
		GameModeCb.setMaxWidth(300);
		GameModeCb.setPadding(new Insets(5, 0, 5, 0));
		GameModeCb.getSelectionModel().selectFirst();
		GameModeCb.getStyleClass().add("dark-blue");

		GameModeCb.setOnAction(event -> {
			getPlayerGameMode().put("Player 2", GameModeCb.getSelectionModel().getSelectedItem());

			if (GameModeCb.getSelectionModel().getSelectedItem() == "AI") {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Difficulty");
				alert.setHeaderText("Choose the difficult level for you computer player");
				alert.setContentText("Choose your option.");

				ButtonType buttonTypeOne = new ButtonType("Easy");
				ButtonType buttonTypeTwo = new ButtonType("Medium");
				ButtonType buttonTypeThree = new ButtonType("Advanced");

				alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == buttonTypeOne) {
					setPlayerAIDifficulty("Easy");
				} else if (result.get() == buttonTypeTwo) {
					setPlayerAIDifficulty("Medium");
				} else if (result.get() == buttonTypeThree) {
					setPlayerAIDifficulty("Advanced");
				}
			}
		});

		HBox hBox = new HBox();
		hBox.setAlignment(Pos.BASELINE_CENTER);

		Image redDiceImg = new Image("client/GUI/images/red-dice.png");
		ImageView redDiceImgView = new ImageView(redDiceImg);
		HBox.setMargin(redDiceImgView, new Insets(0, 40, 0, 0));

		Image yellowDiceImg = new Image("client/GUI/images/yellow-dice.png");
		ImageView yellowDiceImgView = new ImageView(yellowDiceImg);

		ChoiceBox<String> DiceCb = new ChoiceBox<String>(
				FXCollections.observableArrayList("Select Dice", "Red", "Yellow"));
		DiceCb.getStyleClass().add("choiceBoxCB");
		DiceCb.setMaxWidth(300);
		DiceCb.setPadding(new Insets(5, 0, 5, 0));
		DiceCb.getSelectionModel().selectFirst();
		DiceCb.getStyleClass().add("dark-blue");

		DiceCb.setOnAction(event -> {
			getPlayerDice().put("Player 2", DiceCb.getSelectionModel().getSelectedItem());
		});

		HBox hBoxForTextField = new HBox();
		hBoxForTextField.setPadding(new Insets(20, 0, 0, 0));
		hBoxForTextField.setAlignment(Pos.BASELINE_CENTER);

		p2nicknameTxt = new TextField();
		p2nicknameTxt.setPromptText("Enter your nickname");
		p2nicknameTxt.setPadding(new Insets(10, 10, 10, 10));
		p2nicknameTxt.setPrefWidth(208);

		hBox.getChildren().addAll(redDiceImgView, yellowDiceImgView);
		hBoxForTextField.getChildren().add(p2nicknameTxt);

		vBox.setPadding(new Insets(60, 20, 0, 20));
		hBox.setPadding(new Insets(20, 0, 20, 0));

		vBox.getChildren().addAll(GameModeCb, hBox, DiceCb, hBoxForTextField);

		return vBox;
	}

	public static HBox backBtn(Stage primaryStage) {
		HBox hBox = new HBox();

		hBox.setAlignment(Pos.BASELINE_LEFT);
		hBox.setPadding(new Insets(0, 0, 15, 20));

		Button backBtn = new Button("Back");
		backBtn.getStyleClass().add("round-red");
		backBtn.setOnAction(e -> primaryStage.setScene(GameModeScene.layout(primaryStage)));

		hBox.getChildren().add(backBtn);

		return hBox;
	}

	public static Scene layout(Stage primaryStage) {
		BorderPane border = new BorderPane();

		Image vsImg = new Image("client/GUI/images/VS.png");
		ImageView vsImgView = new ImageView(vsImg);

		border.setTop(startGame(primaryStage));
		border.setCenter(vsImgView);
		border.setLeft(playerOneOptions());
		border.setRight(playerTwoOptions());
		border.setBottom(backBtn(primaryStage));

		Scene scene = new Scene(border, 800, 500);
		scene.getStylesheets().add(Interface.class.getResource("style.css").toExternalForm());

		return scene;
	}
}
