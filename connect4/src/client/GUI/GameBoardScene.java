package client.GUI;

import java.util.Optional;

import client.AI;
import client.Client;
import client.GameBoard;
import client.GameProgress;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameBoardScene extends GameBoard {
	private Stage primaryStage;

	private static int escOnOff = 0;

	private final Image placeholder = new Image("client/GUI/images/placeholder.png");

	// Stores a 2D representation of the GUI grid layout
	private GridPane gridMotionDice = new GridPane();
	private GridPane gridHover = new GridPane();
	private GridPane dices = new GridPane();

	private Label diceMotionImg[];
	private Label diceHoverImg[];
	private Label diceImg[][];

	private Button playerName;

	private Client clientCon;
	private String currentPlayer;

	// Constructor for offline mode
	public GameBoardScene() {
		diceImg = new Label[getColumn()][getRow()];

		diceHoverImg = new Label[getColumn()];

		this.diceMotionImg = new Label[getColumn()];

		for (int cols = 0; cols < 7; cols++) {
			diceMotionImg[cols] = new Label();
			diceMotionImg[cols].setGraphic(new ImageView(placeholder));
		}

		for (int cols = 0; cols < 7; cols++) {
			diceHoverImg[cols] = new Label();
			diceHoverImg[cols].setGraphic(new ImageView(placeholder));
		}

		for (int cols = 0; cols < 7; cols++) {
			for (int rows = 0; rows < 6; rows++) {
				// 2D representation of the GUI
				getBoard_2d_Grid()[cols][rows] = " ";
				diceImg[cols][rows] = new Label();
				diceImg[cols][rows].setGraphic(new ImageView(placeholder));
			}
		}

		if (getPlayerGameMode().get("Player 1") != null) {

		}

		if (!getPlayerGameMode().get("Player 1").equals("AI")) {
			mouseEvent();
		} else if (getPlayerGameMode().get("Player 1").equals("AI")) {
			AI ai = new AI(this, primaryStage);
			new Thread(ai).start();
		} else {
			mouseEvent();
		}

		this.playerName = new Button();
	}

	// Constructor for online mode
	public GameBoardScene(Client clientCon, String[] usernames, String currentPlayer) {
		diceImg = new Label[getColumn()][getRow()];

		diceHoverImg = new Label[getColumn()];

		this.diceMotionImg = new Label[getColumn()];

		for (int cols = 0; cols < 7; cols++) {
			diceMotionImg[cols] = new Label();
			diceMotionImg[cols].setGraphic(new ImageView(placeholder));
		}

		for (int cols = 0; cols < 7; cols++) {
			diceHoverImg[cols] = new Label();
			diceHoverImg[cols].setGraphic(new ImageView(placeholder));
		}

		for (int cols = 0; cols < 7; cols++) {
			for (int rows = 0; rows < 6; rows++) {
				// 2D representation of the GUI
				getBoard_2d_Grid()[cols][rows] = " ";
				diceImg[cols][rows] = new Label();
				diceImg[cols][rows].setGraphic(new ImageView(placeholder));
			}
		}

		playerName = new Button();

		this.clientCon = clientCon;
		this.currentPlayer = currentPlayer;

		getPlayerNickname().put("Player 1", usernames[0]);
		getPlayerNickname().put("Player 2", usernames[1]);
		getPlayerDice().put("Player 1", "Red");
		getPlayerDice().put("Player 2", "Yellow");

		if (!getPlayerNickname().get(getPlayerTurn()[getPlayerTurnRotation()]).equals(currentPlayer)) {
			dices.setDisable(true);
		}
	}

	public GridPane getGridDices() {
		return dices;
	}

	// OFFLINE MODE ONLY
	public void diceMotionAnimation(Node source) {
		this.diceHoverImg[GridPane.getColumnIndex(source)].setGraphic(new ImageView(placeholder));
		this.diceMotionImg[GridPane.getColumnIndex(source)].setGraphic(new ImageView(getDice()));

		TranslateTransition transition = new TranslateTransition(Duration.millis(450 - 50),
				this.diceMotionImg[GridPane.getColumnIndex(source)]);
		transition.setByY(getMotionDiceNextPosition()[GridPane.getColumnIndex(source)]);
		transition.setFromY(0);

		transition.setOnFinished((event -> {
			try {
				this.diceImg[GridPane.getColumnIndex(source)][getCheckColumnFull()[GridPane.getColumnIndex(source)]--]
						.setGraphic(new ImageView(getDice()));

				if (getPlayerGameMode().get("Player 1") != null && getPlayerGameMode().get("Player 2") != null) {
					if (getPlayerGameMode().get("Player 1").equals("AI")
							|| getPlayerGameMode().get("Player 2").equals("AI")) {
						AI ai = new AI(this, primaryStage);
						new Thread(ai).start();
					}
				}

				if (getPlayerTurnRotation() == 0) {
					setPlayerTurnRotation(1);
				} else if (getPlayerTurnRotation() == 1) {
					setPlayerTurnRotation(0);
				}

			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				System.out.println("You have clicked the board to much time");
			}

		}));

		transition.play();

	}

	// Overload method used only for ONLINE Mode
	public void diceMotionAnimation(int column) {
		if (clientCon == null) {
			if (getPlayerGameMode().get("Player 1").equals("AI") || getPlayerGameMode().get("Player 2").equals("AI")) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		this.diceHoverImg[column].setGraphic(new ImageView(placeholder));
		this.diceMotionImg[column].setGraphic(new ImageView(getDice()));

		TranslateTransition transition = new TranslateTransition(Duration.millis(450 - 50), this.diceMotionImg[column]);
		transition.setByY(getMotionDiceNextPosition()[column]);
		transition.setFromY(0);

		transition.setOnFinished((event -> {
			try {
				// Set a dice in the other client that is the differnt colour
				this.diceImg[column][getCheckColumnFull()[column]--].setGraphic(new ImageView(getDice()));

				if (getPlayerTurnRotation() == 0) {
					setPlayerTurnRotation(1);
				} else if (getPlayerTurnRotation() == 1) {
					setPlayerTurnRotation(0);
				}
				
				dices.setDisable(false);
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				System.out.println("You have clicked the board to much time");
			}

		}));

		transition.play();

	}

	public VBox gridMotionDice() {
		VBox vBox = new VBox();

		gridMotionDice = new GridPane();
		gridMotionDice.setAlignment(Pos.BASELINE_CENTER);
		gridMotionDice.setHgap(9);

		for (int cols = 0; cols < 7; cols++) {
			gridMotionDice.add(this.diceMotionImg[cols], cols, 0);
		}

		vBox.getChildren().add(gridMotionDice);

		return vBox;
	}

	public VBox gridDiceHover() {
		VBox vBox = new VBox();

		gridHover = new GridPane();
		gridHover.setAlignment(Pos.BASELINE_CENTER);
		gridHover.setHgap(9);

		for (int cols = 0; cols < 7; cols++) {
			gridHover.add(this.diceHoverImg[cols], cols, 0);
		}

		vBox.getChildren().add(gridHover);

		return vBox;
	}

	// Used for online because we do not have access to JavaFX events
	public void setEnteredDiceHover(int column) {
		diceHoverImg[column].setGraphic(new ImageView(getDice()));
	}

	// Used for online because we do not have access to JavaFX events
	public void setExitedDiceHover(int column) {
		diceHoverImg[column].setGraphic(new ImageView(placeholder));
	}

	public VBox dicesGrid() {		
		VBox vBox = new VBox();

		dices.setAlignment(Pos.BASELINE_CENTER);
		dices.setPadding(new Insets(85, 0, 0, 0));
		dices.setHgap(9);
		dices.setVgap(10);

		for (int cols = 0; cols < 7; cols++) {
			for (int rows = 0; rows < 6; rows++) {
				dices.add(diceImg[cols][rows], cols, rows);
			}
		}

		mouseEvent();

		vBox.getChildren().add(this.dices);

		return vBox;
	}

	public void setDiceGridDisable(boolean state) {
		dices.setDisable(state);
	}

	// Issue the dice changes the colour when the player hovers in any grid cell
	public void mouseEvent() {
		for (Node node : dices.getChildren()) {
			node.setOnMouseEntered((MouseEvent e) -> {
				Node source = (Node) e.getSource();
				diceHoverImg[GridPane.getColumnIndex(source)].setGraphic(new ImageView(getDice()));

				if (clientCon != null) {
					setDice(clientCon.getPlayerTurnRotation());
					clientCon.writeToServer("MOVE");
					clientCon.writeToServer("MouseEntered " + Integer.toString(GridPane.getColumnIndex(source)));
				} else {
					setDice(getPlayerTurnRotation());
					playerName.setText(getPlayerNickname().get(getPlayerTurn()[getPlayerTurnRotation()]).toUpperCase());
				}

			});
			node.setOnMouseExited((MouseEvent e) -> {
				Node source = (Node) e.getSource();
				diceHoverImg[GridPane.getColumnIndex(source)].setGraphic(new ImageView(placeholder));

				if (clientCon != null) {
					// setDice(clientCon.getPlayerTurnRotation());
					// clientCon.writeToServer("MOVE");
					// clientCon.writeToServer("MouseExited " +
					// Integer.toString(GridPane.getColumnIndex(source)));
				} else {
					// setDice(getPlayerTurnRotation());
				}

			});
			node.setOnMousePressed((MouseEvent e) -> {
				if (clientCon != null) {
					dices.setDisable(true);
				}

				Node source = (Node) e.getSource();

				if (getCheckColumnFull()[GridPane.getColumnIndex(source)] >= 0) {
					String player = getPlayerNickname().get(getPlayerTurn()[getPlayerTurnRotation()]);
					int row = getCheckColumnFull()[GridPane.getColumnIndex(source)];
					int column = GridPane.getColumnIndex(source);
					setHuamnPlayerCol(column);
					setHumanPlayerRow(row);
					
					diceMotionAnimation(source);

					if (getCheckColumnFull()[GridPane.getColumnIndex(source)] >= 1) {
						getMotionDiceNextPosition()[GridPane.getColumnIndex(source)] -= 75;
					}

					if (clientCon != null) {
						setDice(clientCon.getPlayerTurnRotation());

						if (clientCon.getPlayerTurnRotation() == 0) {
//							getBoard_2d_Grid()[column][row] = "Player 1";
							clientCon.setPlayerTurnRotation(1);
						} else if (clientCon.getPlayerTurnRotation() == 1) {
//							getBoard_2d_Grid()[column][row] = "Player 2";
							clientCon.setPlayerTurnRotation(0);
						}

						setBoard_2d_Grid(column, row, currentPlayer);
						
						if (checkWinner(row, column, currentPlayer)) {
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

							if (clientCon != null) {
								clientCon.close();
							}
						}
						
						playerName.setText(getPlayerNickname().get(getPlayerTurn()[clientCon.getPlayerTurnRotation()])
								.toUpperCase());

						clientCon.writeToServer("MOVE");
						clientCon.writeToServer("MouseClicked " + GridPane.getColumnIndex(source) + " "
								+ getCheckColumnFull()[GridPane.getColumnIndex(source)] + " "
								+ clientCon.getPlayerTurnRotation());

					} else {
						setDice(getPlayerTurnRotation());

						if (getPlayerTurnRotation() == 0) {
							getBoard_2d_Grid()[column][row] = "Player 1";
						} else if (getPlayerTurnRotation() == 1) {
							getBoard_2d_Grid()[column][row] = "Player 2";
						}

						playerName.setText(player.toUpperCase());

					}

					if (checkWinner(row, column, "Player 1")) {
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

						if (clientCon != null) {
							clientCon.close();
						}
					}
					
					if (checkWinner(row, column, "Player 2")) {
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

						if (clientCon != null) {
							clientCon.close();
						}
					}

					// DEBUGING PURPOSES
//					System.out.println(Arrays.deepToString(getBoard_2d_Grid()));

				} else {
					System.out.println("Column full");
				}
			});
		}
	}

	public HBox playerNameTxt(Stage primaryStage) {
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(535, 0, 0, 0));
		hBox.setAlignment(Pos.BASELINE_CENTER);

		playerName.setStyle("-fx-padding: 8 15 15 15;\r\n"
				+ "	-fx-background-insets: 0, 0 0 5 0, 0 0 6 0, 0 0 7 0;\r\n" + "	-fx-background-radius: 8;\r\n"
				+ "	-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%),\r\n"
				+ "		#9d4024, #d86e3a,\r\n"
				+ "		radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);\r\n"
				+ "	-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.75), 4, 0, 0, 1);\r\n"
				+ "	-fx-font-weight: bold;\r\n" + "	-fx-font-size: 1.9em;\r\n" + "	-fx-text-fill: #FFFFFF;");

		playerName.setText(getPlayerNickname().get(getPlayerTurn()[0]).toUpperCase());

		hBox.getChildren().add(playerName);

		return hBox;
	}

	public String getPlayerName() {
		return playerName.getText();
	}

	public void setPlayerName(String name) {
		playerName.setText(name);
	}

	public StackPane escMenu(Stage primaryStage) {
		StackPane stack = new StackPane();
		stack.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		stack.setMaxWidth(300);
		stack.setMaxHeight(290);
		stack.setPadding(new Insets(0, 0, 0, 0));

		Color color = Color.rgb(0, 0, 0, 0.35);
		BackgroundFill fill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
		Background background = new Background(fill);

		stack.setBackground(background);
		stack.setTranslateX(10);
		stack.setTranslateY(1);

		Label Menulbl = new Label("Menu");
		Menulbl.getStyleClass().add("menulbl");
		Button saveGameBtn = new Button("SAVE GAME");
		saveGameBtn.getStyleClass().add("menuBtn");
		saveGameBtn.setStyle("-fx-padding: 8 68 15 68");
		saveGameBtn.setOnAction(e -> {
			GameProgress gameProgress = new GameProgress(getPlayerDice().get("Player 1"),
					getPlayerDice().get("Player 2"), getPlayerTurnRotation(), getBoard_2d_Grid());
			gameProgress.save();
		});
		Button loadGameBtn = new Button("LOAD GAME");
		loadGameBtn.getStyleClass().add("menuBtn");
		loadGameBtn.setStyle("-fx-padding: 8 65 15 65");
		loadGameBtn.setOnAction(e -> {
			GameProgress gameProgress = new GameProgress(primaryStage);
			gameProgress.load();
			GameBoardScene gb = new GameBoardScene();
		});
		Button quitMBtn = new Button("QUIT TO MAIN MENU");
		quitMBtn.getStyleClass().add("menuBtn");

		Interface mainManu = new Interface();

		quitMBtn.setOnAction(e -> {
			try {
				if (clientCon != null) {
					clientCon.close();
				}
				mainManu.start(primaryStage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		VBox vBox = new VBox();
		vBox.setAlignment(Pos.BASELINE_CENTER);

		VBox.setMargin(loadGameBtn, new Insets(0, 0, 10, 0));
		VBox.setMargin(quitMBtn, new Insets(0, 0, 10, 0));

		vBox.getChildren().addAll(Menulbl, saveGameBtn, loadGameBtn, quitMBtn);

		stack.getChildren().add(vBox);
		stack.setAlignment(Pos.BASELINE_RIGHT);

		return stack;
	}

	public HBox gameBoardBg() {
		HBox hBox = new HBox();

		hBox.setAlignment(Pos.BASELINE_CENTER);
		hBox.setPadding(new Insets(60, 0, 0, 0));

		Image vsImg = new Image("client/GUI/images/GameBoard.png");
		ImageView vsImgView = new ImageView(vsImg);

		hBox.getChildren().add(vsImgView);

		return hBox;
	}

	public Label[] getdiceHoverImg() {
		return diceHoverImg;
	}

	public Label[][] getdiceImg() {
		return diceImg;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public Label[] getDiceHoverImg() {
		return diceHoverImg;
	}

	public Scene layout(Stage primaryStage) {		
		setPrimaryStage(primaryStage);
		getPlayerGameMode();

		StackPane stack = new StackPane();

		Scene scene = new Scene(stack, 800, 600);
		stack.getChildren().clear();

		stack.getChildren().add(playerNameTxt(primaryStage));
		stack.getChildren().add(gridDiceHover());
		stack.getChildren().add(gridMotionDice());
		stack.getChildren().add(gameBoardBg());
		stack.getChildren().add(dicesGrid());

		scene.setOnKeyPressed(event -> {
			switch (event.getCode()) {
			case ESCAPE:
				if (escOnOff == 0) {
					escOnOff++;
					dices.setDisable(true);
					stack.getChildren().add(escMenu(primaryStage));
				} else {
					escOnOff--;
					dices.setDisable(false);
					// Bug java.lang.IndexOutOfBoundsException
					stack.getChildren().remove(5);
				}
			default:
				break;
			}

		});

		scene.getStylesheets().add(Interface.class.getResource("style.css").toExternalForm());

		return scene;
	}
}
