package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import client.GUI.GameBoardScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class GameProgress {
	private Stage primaryStage;

	private final Image redDiceImg = new Image("client/GUI/images/red-dice.png");
	private final Image yellowDiceImg = new Image("client/GUI/images/yellow-dice.png");
	private final Image placeholder = new Image("client/GUI/images/placeholder.png");

	private String playerOneDiceColour;
	private String playerTwoDiceColour;
	private String playerOneGameMode;
	private String playerTwoGameMode;

	private String playersAIDifficulty;
	private String[] settings = new String[6];
	private String[][] board_2d_Grid;

	private int currentPlayerTurnRotation;

	// Load
	public GameProgress(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	// save required values
	public GameProgress(String playerOneDiceColour, String playerTwoDiceColour, int currentPlayerTurnRotation,
			String[][] board_2d_Grid) {
		this.playerOneDiceColour = playerOneDiceColour;
		this.playerTwoDiceColour = playerTwoDiceColour;
		this.currentPlayerTurnRotation = currentPlayerTurnRotation;
		this.board_2d_Grid = board_2d_Grid;
	}

	public void save() {
		try {
			PrintWriter saveGame = new PrintWriter("saves/savedGame.txt");

			for (int rows = 0; rows < board_2d_Grid.length; rows++) {
				String[] val = board_2d_Grid[rows];
				saveGame.println(String.join(", ", val));
			}

			saveGame.close();

			PrintWriter savedGameSettings = new PrintWriter("saves/savedGameSettings.txt");
			savedGameSettings.println(playerOneDiceColour);
			savedGameSettings.println(GameBoardScene.getPlayerGameMode().get("Player 1"));
			savedGameSettings.println(playerTwoDiceColour);
			savedGameSettings.println(GameBoardScene.getPlayerGameMode().get("Player 2"));
			savedGameSettings.println(currentPlayerTurnRotation);
			savedGameSettings.println(GameBoardScene.getPlayerAIDifficulty());
			savedGameSettings.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void load() {
		GameBoardScene.getPlayerGameMode().put("Player 1", "Human");
		GameBoardScene.getPlayerGameMode().put("Player 2", "Human");
		GameBoardScene.getPlayerNickname().put("Player 1", "Player 1");
		GameBoardScene.getPlayerNickname().put("Player 2", "Player 2");

		GameBoardScene gameBoardScene = new GameBoardScene();

		try {
			FileReader loadGame = new FileReader(new File("saves/savedGame.txt"));
			BufferedReader game = new BufferedReader(loadGame);

			for (int rows = 0; rows < gameBoardScene.getBoard_2d_Grid().length; rows++) {
				gameBoardScene.getBoard_2d_Grid()[rows] = game.readLine().split(", ");
			}

			loadGame.close();

			FileReader loadGameSettings = new FileReader(new File("saves/savedGameSettings.txt"));
			BufferedReader settingIn = new BufferedReader(loadGameSettings);

			for (int i = 0; i < 6; i++) {
				settings[i] = settingIn.readLine();
			}

			playerOneDiceColour = settings[0];
			playerOneGameMode = settings[1];
			playerTwoDiceColour = settings[2];
			playerTwoGameMode = settings[3];
			currentPlayerTurnRotation = Integer.parseInt(settings[4]);
			playersAIDifficulty = settings[5];
			
			GameBoardScene.getPlayerGameMode().put("Player 1", playerOneGameMode);
			GameBoardScene.getPlayerGameMode().put("Player 2", playerTwoGameMode);
			
			loadGameSettings.close();

		} catch (

		IOException e) {
			e.printStackTrace();
		}

		GameBoardScene.getPlayerDice().put("Player 1", playerOneDiceColour);
		GameBoardScene.getPlayerDice().put("Player 2", playerTwoDiceColour);

		primaryStage.setScene(gameBoardScene.layout(primaryStage));

		gameBoardScene.setPlayerTurnRotation(currentPlayerTurnRotation);

		for (int cols = 0; cols < 7; cols++) {
			for (int rows = 0; rows < 6; rows++) {
				if (gameBoardScene.getBoard_2d_Grid()[cols][rows].equals("Player 1")) {
					gameBoardScene.getMotionDiceNextPosition()[cols]--;
					gameBoardScene.getCheckColumnFull()[cols]--;
					gameBoardScene.getBoard_2d_Grid()[cols][rows] = "Player 1";
					if (playerOneDiceColour.equals("Red")) {
						gameBoardScene.getdiceImg()[cols][rows].setGraphic(new ImageView(redDiceImg));
						continue;
					} else if (playerOneDiceColour.equals("Yellow")) {
						gameBoardScene.getdiceImg()[cols][rows].setGraphic(new ImageView(yellowDiceImg));
						continue;
					}
					continue;
				}

				if (gameBoardScene.getBoard_2d_Grid()[cols][rows].equals("Player 2")) {
					gameBoardScene.getMotionDiceNextPosition()[cols]--;
					gameBoardScene.getCheckColumnFull()[cols]--;
					gameBoardScene.getBoard_2d_Grid()[cols][rows] = "Player 2";
					if (playerTwoDiceColour.equals("Red")) {
						gameBoardScene.getdiceImg()[cols][rows].setGraphic(new ImageView(redDiceImg));
						continue;
					} else if (playerTwoDiceColour.equals("Yellow")) {
						gameBoardScene.getdiceImg()[cols][rows].setGraphic(new ImageView(yellowDiceImg));
						continue;
					}
					continue;
				}

				gameBoardScene.getBoard_2d_Grid()[cols][rows] = " ";
				gameBoardScene.getdiceImg()[cols][rows].setGraphic(new ImageView(placeholder));
			}
		}
	}
}
