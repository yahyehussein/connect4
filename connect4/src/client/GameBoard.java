package client;

import javafx.scene.image.Image;

public class GameBoard extends Player {
	private static final int COLUMN = 7;
	private static final int ROW = 6;

	private String[] playerTurn = { "Player 1", "Player 2" };

	private final Image redDiceImg = new Image("client/GUI/images/red-dice.png");
	private final Image yellowDiceImg = new Image("client/GUI/images/yellow-dice.png");
	private Image playerDiceColour;

	private int[] checkColumnFull = { 5, 5, 5, 5, 5, 5, 5 };
	private int[] motionDiceNextPosition = { 450, 450, 450, 450, 450, 450, 450 };
	private String[][] board_2d_Grid;

	private int playerTurnRotation = 0;

	public GameBoard() {
		// Used for validation only
		this.board_2d_Grid = new String[COLUMN][ROW];
	}

	public int getColumn() {
		return COLUMN;
	}

	public int getRow() {
		return ROW;
	}

	public void setBoard_2d_Grid(int col, int row, String player) {
		this.board_2d_Grid[col][row] = player;
	}

	public String[][] getBoard_2d_Grid() {
		return this.board_2d_Grid;
	}

	public int[] getCheckColumnFull() {
		return checkColumnFull;
	}

	public int[] getMotionDiceNextPosition() {
		return motionDiceNextPosition;
	}

	public int getPlayerTurnRotation() {
		return playerTurnRotation;
	}

	public void setPlayerTurnRotation(int playerTurnRotation) {
		this.playerTurnRotation = playerTurnRotation;
	}

	public void setDice(int turn) {
		if (getPlayerDice().get(playerTurn[turn]).equals("Red")) {
			playerDiceColour = redDiceImg;
		} else if (getPlayerDice().get(playerTurn[turn]).equals("Yellow")) {
			playerDiceColour = yellowDiceImg;
		}
	}

	public String[] getPlayerTurn() {
		return playerTurn;
	}

	public Image getDice() {
		return playerDiceColour;
	}

	public boolean checkWinner(int currentRow, int currentColumn, String player) {
		int count = 0;

		// Check horizontally for winner
		for (int i = 0; i < COLUMN; i++) {
			if (board_2d_Grid[i][currentRow] == player) {
				count++;
			} else {
				count = 0;
			}

			if (count >= 4)
				return true;
		}

		// Check vertically for winner
		for (int i = 0; i < ROW; i++) {
			if (board_2d_Grid[currentColumn][i] == player) {
				count++;
			} else {
				count = 0;
			}

			if (count >= 4)
				return true;
		}

		// Check / diagonal for winner
		for (int i = 3; i < COLUMN - 1; i++) {
			for (int j = 0; j < ROW - 3; j++) {
				if (board_2d_Grid[i][j] == player && board_2d_Grid[i - 1][j + 1] == player
						&& board_2d_Grid[i - 2][j + 2] == player && board_2d_Grid[i - 3][j + 3] == player)
					return true;
			}
		}
		
		// Check \ diagonal for winner
		for (int i = 3; i < COLUMN; i++) {
			for (int j = 3; j < ROW; j++) {
				if (board_2d_Grid[i][j] == player && board_2d_Grid[i - 1][j - 1] == player
						&& board_2d_Grid[i - 2][j - 2] == player && board_2d_Grid[i - 3][j - 3] == player)
					return true;
			}
		}

		return false;
	}

}
