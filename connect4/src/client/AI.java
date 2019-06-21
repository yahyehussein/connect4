package client;

import java.util.Optional;
import java.util.Random;
import client.GUI.GameBoardScene;
import client.GUI.Interface;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class AI implements CanDifficulty, Runnable {
	private GameBoardScene gb;
	private Stage primaryStage;

	public AI(GameBoardScene gb, Stage primaryStage) {
		this.gb = gb;
		this.primaryStage = primaryStage;
	}

	public void play() {
		gb.getGridDices().setDisable(true);
		gb.setDice(gb.getPlayerTurnRotation());

		int column = 0;

		if (GameBoard.getPlayerAIDifficulty().equals("Easy")) {
			column = easy();
		} else if (GameBoard.getPlayerAIDifficulty().equals("Medium")) {
			if (GameBoard.getPlayerGameMode().get("Player 1").equals("AI")) {
				column = medium(gb.getHumanPlayerRow(), gb.getHuamnPlayerCol(), "Player 2", "Player 1");
			} else if (GameBoard.getPlayerGameMode().get("Player 2").equals("AI")) {
				column = medium(gb.getHumanPlayerRow(), gb.getHuamnPlayerCol(), "Player 1", "Player 2");
			}
		} else if (GameBoard.getPlayerAIDifficulty().equals("Advanced")) {
			if (GameBoard.getPlayerGameMode().get("Player 1").equals("AI")) {
				column = advanced(gb.getHumanPlayerRow(), gb.getHuamnPlayerCol(), "Player 2", "Player 1");
			} else if (GameBoard.getPlayerGameMode().get("Player 2").equals("AI")) {
				column = advanced(gb.getHumanPlayerRow(), gb.getHuamnPlayerCol(), "Player 1", "Player 2");
			}
		}

		int row = gb.getCheckColumnFull()[column];

		if (gb.getCheckColumnFull()[column] >= 0) {
			String player = GameBoard.getPlayerNickname().get(gb.getPlayerTurn()[gb.getPlayerTurnRotation()]);

			if (gb.getPlayerTurnRotation() == 0) {
				gb.getBoard_2d_Grid()[column][row] = "Player 1";
			} else if (gb.getPlayerTurnRotation() == 1) {
				gb.getBoard_2d_Grid()[column][row] = "Player 2";
			}

			gb.diceMotionAnimation(column);

			if (gb.checkWinner(row, column, player)) {
				gb.setDiceGridDisable(true);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Winner Winner chicken winner");
				alert.setHeaderText(null);
				alert.setContentText("Look, like " + player + " have won the game");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					Interface mainManu = new Interface();
					try {
						mainManu.start(primaryStage);
					} catch (Exception ec) {
						ec.printStackTrace();
					}
				}
			}

			if (gb.getCheckColumnFull()[column] >= 1) {
				gb.getMotionDiceNextPosition()[column] -= 75;
			}

			gb.setPlayerName(player.toUpperCase());

			gb.mouseEvent();
		} else {
			play();
		}

	}

	@Override
	public int easy() {
		Random rng = new Random();
		return rng.nextInt(6);
		// returns a random drop column
	}

	@Override
	public int medium(int currentRow, int currentColumn, String player, String AI) {
		int count = 0;

		for (int i = 0; i < gb.getColumn(); i++) {
			if (gb.getBoard_2d_Grid()[i][currentRow] == player) {
				count++;
			} else {
				count = 0;
			}

			if (count >= 3) {
				if (i <= 5) {
					count += 0;
					return i + 1;
				}
			}

		}

		for (int i = 0; i < gb.getRow(); i++) {
			if (gb.getBoard_2d_Grid()[currentColumn][i] == player) {
				count++;
			} else {
				count = 0;
			}

			if (count >= 3) {
				return currentColumn;
			}
		}
		
		for (int i = 0; i < gb.getColumn(); i++) {
			if (gb.getBoard_2d_Grid()[i][currentRow] == AI) {
				count++;
			} else {
				count = 0;
			}

			if (count >= 3) {
				if (i <= 5) {
					count += 0;
					return i + 1;
				}
			}

		}

		for (int i = 0; i < gb.getRow(); i++) {
			if (gb.getBoard_2d_Grid()[currentColumn][i] == AI) {
				count++;
			} else {
				count = 0;
			}

			if (count >= 3) {
				return currentColumn;
			}
		}

		Random rng = new Random();
		return rng.nextInt(6);
	}

	@Override
	public int advanced(int currentRow, int currentColumn, String player, String AI) {
		int count = 0;

		for (int i = 0; i < gb.getColumn(); i++) {
			if (gb.getBoard_2d_Grid()[i][currentRow] == player) {
				count++;
			} else {
				count = 0;
			}

			if (count >= 2) {
				if (i <= 5) {
					count += 0;
					return i + 1;
				}
			}

		}

		for (int i = 0; i < gb.getRow(); i++) {
			if (gb.getBoard_2d_Grid()[currentColumn][i] == player) {
				count++;
			} else {
				count = 0;
			}

			if (count >= 2) {
				return currentColumn;
			}
		}

		for (int i = 0; i < gb.getColumn(); i++) {
			if (gb.getBoard_2d_Grid()[i][currentRow] == AI) {
				count++;
			} else {
				count = 0;
			}

			if (count >= 3) {
				if (i <= 5) {
					count += 0;
					return i + 1;
				}
			}

		}

		for (int i = 0; i < gb.getRow(); i++) {
			if (gb.getBoard_2d_Grid()[currentColumn][i] == AI) {
				count++;
			} else {
				count = 0;
			}

			if (count >= 3) {
				return currentColumn;
			}
		}
		
		Random rng = new Random();
		return rng.nextInt(6);
	}

	@Override
	public void run() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				play();
			}
		});

	}

}
