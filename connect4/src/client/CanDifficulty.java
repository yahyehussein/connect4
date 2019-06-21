package client;

public interface CanDifficulty {

	int easy();
	int medium(int currentRow, int currentColumn, String player, String AI);
	int advanced(int currentRow, int currentColumn, String player, String AI);
	
}
