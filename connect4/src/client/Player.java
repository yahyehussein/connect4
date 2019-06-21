package client;

import java.util.HashMap;

public class Player {	
	private static String playerAIDifficulty;
	private static HashMap<String, String> playerGameMode = new HashMap<String, String>();
	private static HashMap<String, String> playerDice = new HashMap<String, String>();
	private static HashMap<String, String> playerNickname = new HashMap<String, String>();

	private int col;
	private int row;
	
	public int getHumanPlayerRow() {
		return row;
	}
	
	public void setHumanPlayerRow(int row) {
		this.row = row;
	}
	
	public int getHuamnPlayerCol() {
		return col;
	}
	public void setHuamnPlayerCol(int col) {
		this.col = col;
	}
		
	public static HashMap<String, String> getPlayerGameMode() {
		return playerGameMode;
	}
	
	public static String getPlayerAIDifficulty() {
		return playerAIDifficulty;
	}
	
	public static void setPlayerAIDifficulty(String difficulty) {
		playerAIDifficulty = difficulty;
	}
	
	public static HashMap<String, String> getPlayerDice() {
		return playerDice;
	}
	
	public static HashMap<String, String> getPlayerNickname() {
		return playerNickname;
	}
	
	// Hanad
	public void getComputerTurn() {
		
	}
	
	// do Setters and Getters
	// Sets the both player name if their humans
	// Sets both the player dice colours
	// Sets the USERNAME for online
	// 
}
