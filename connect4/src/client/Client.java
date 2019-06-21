package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Optional;
import client.GUI.GameBoardScene;
import client.GUI.Interface;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Client implements Runnable {
    public class Chat implements Runnable {
        @Override
        public void run() {
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(isr);
            try {
                while(!isClosed()) {
                	System.out.println("Type Message: ");
                    String say = in.readLine();
                    writeToServer("CHAT");
                    writeToServer(say);
                }
            } catch(IOException e) {
                System.err.println("Console error: " + e);
            }
        }
    }
    
	/* The Socket of the Client */
	private Socket clientSocket;
	private Client clientCon;
	private Stage primaryStage;
	private BufferedReader serverToClientReader;
	private PrintWriter clientToServerWriter;
	private String serverResponseStorage;

	private GameBoardScene gameBoardScene;
	private String[] usernames = new String[2];
	private String currentPlayer;
	private String previousPlayerName;
	private int playerTurn = 0;
	private int previousPlayerTurn = 0;

	private int column;
	private int row;

	public Client(Socket clientSocket, String currentPlayer, Stage primaryStage) throws UnknownHostException, IOException {
		/* Try to establish a connection to the server */
		this.clientSocket = clientSocket;
		this.primaryStage = primaryStage;
		this.currentPlayer = currentPlayer;

		/* Instantiate writers and readers to the socket */
		serverToClientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		clientToServerWriter = new PrintWriter(clientSocket.getOutputStream(), true);

		clientToServerWriter.println("HELLO");

		this.clientCon = this;
	}

	public void writeToServer(String input) {
		clientToServerWriter.println(input);
	}

	public String readFromServer() {
		return serverResponseStorage;
	}

    public Chat getChat() {
        return new Chat();
    }
	
	public boolean isClosed() {
		return clientSocket.isClosed();
	}

	public void close() {
		if (!isClosed()) {
			try {
				clientSocket.close();
			} catch (IOException e2) {
				System.err.println("While closing, connection " + this + " caused " + e2);
			}
		}
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	/*
	 * bbb >> 200 >> READY #aaa bbb aaa The one that connects to a exisiting channel
	 * which means that it will have dices.setDisable(false) will aaa >> 200 >>
	 * READY #aaa bbb aaa The one that creates the channel will be the second player
	 * When the first player places a dice it will change to the second player which
	 * in the setDisble for the first client and false for the second client
	 */

	public void run() {
		new Thread(clientCon.getChat()).start();
		
		/* Infinite loop to update the wait log from the server */
		while (true) {
			try {
				serverResponseStorage = serverToClientReader.readLine();
				
				// DEBUGGING PURPOSE ONLY 
//				System.out.println(serverResponseStorage);
							
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						if (findMatch(serverResponseStorage)) {
							setUsernames(serverResponseStorage);
							gameBoardScene = new GameBoardScene(clientCon, usernames, currentPlayer);
							primaryStage.setScene(gameBoardScene.layout(primaryStage));
							
						} else if (hasClientMouseEntered(serverResponseStorage)) {
							gameBoardScene.setDice(playerTurn);
							setColumn(serverResponseStorage);
							gameBoardScene.setEnteredDiceHover(column);
							
						} else if (hasClientMouseExited(serverResponseStorage)) {
							setColumn(serverResponseStorage);
							gameBoardScene.setExitedDiceHover(column);
							
						} else if (hasClientMouseClicked(serverResponseStorage)) {	
							previousPlayerTurn = playerTurn;
	
							setPlayerTurn(serverResponseStorage);

							previousPlayerName = GameBoardScene.getPlayerNickname()
									.get(gameBoardScene.getPlayerTurn()[previousPlayerTurn]);
							String playerTurnName = GameBoardScene.getPlayerNickname()
									.get(gameBoardScene.getPlayerTurn()[playerTurn]);

							if (playerTurnName.equals(currentPlayer)) {
								gameBoardScene.getGridDices().setDisable(false);
							}

							gameBoardScene.setPlayerName(playerTurnName.toUpperCase());

							setPlayerMoves(serverResponseStorage);

							gameBoardScene.setDice(previousPlayerTurn);
							gameBoardScene.diceMotionAnimation(column);
							
							gameBoardScene.setBoard_2d_Grid(column, row, previousPlayerName);
							
						}
						
						if (clientChat(serverResponseStorage)) {
							System.out.println(serverResponseStorage);
							System.out.println("");
							System.out.println("Please press enter until the Type Message:");
						}
						
						if (serverResponseStorage.matches("200 >> OPPONENT_CLOSED \\w+")) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Winner Winner chicken winner");
							alert.setHeaderText(null);
							alert.setContentText("Look, like " + previousPlayerName + " have won the game");

							Optional<ButtonType> result = alert.showAndWait();
							if (result.get() == ButtonType.OK) {
								Interface mainManu = new Interface();
								try {
									mainManu.start(primaryStage);
								} catch (Exception ec) {
									ec.printStackTrace();
								}
							}

							if (clientCon != null) {
								clientCon.close();
							}
							
						}

					}
				});
			} catch (SocketException e) {
				System.out.println("200 >> " + e.getMessage());
				break;
			} catch (IOException e) {
				System.out.println("200 >> " + e.getMessage());
				break;
			}
		}

	}

	public boolean findMatch(String line) {
		return line.matches("\\d+ >> READY #\\w+ \\w+ \\w+");
	}

	public boolean hasClientMouseEntered(String line) {
		return line.matches("\\d+ >> MOVE \\w+ MouseEntered \\d+");
	}

	public boolean hasClientMouseExited(String line) {
		return line.matches("\\d+ >> MOVE \\w+ MouseExited \\d+");
	}

	public boolean hasClientMouseClicked(String line) {
		return line.matches("\\d+ >> MOVE \\w+ MouseClicked \\d+ \\d+ \\d+");
	}
	
	public boolean clientChat(String line) {
		return line.matches("200 >> CHAT \\w+ \\w+");
	}
	
	public void setUsernames(String line) {
		String[] words = line.split("\\s+");
		usernames[0] = words[4];
		usernames[1] = words[5];
	}
	
	public void setColumn(String line) {
		String[] words = line.split("\\s+");
		column = Integer.parseInt(words[5]);
	}

	public void setPlayerMoves(String line) {
		String[] words = line.split("\\s+");
		column = Integer.parseInt(words[5]);
		row = Integer.parseInt(words[6]);
	}

	public void setPlayerTurn(String line) {
		String[] words = line.split("\\s+");
		playerTurn = Integer.parseInt(words[7]);
	}

	public void setPlayerTurnRotation(int turn) {
		this.playerTurn = turn;
	}

	public int getPlayerTurnRotation() {
		return playerTurn;
	}
	
	public String getPreviousPlayerName() {
		return previousPlayerName;
	}
}
