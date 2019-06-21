package client.GUI;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import bcu.cmp5308.gameserver.JoinCommand;
import bcu.cmp5308.gameserver.ServerProgram;
import bcu.cmp5308.gameserver.UsernameCommand;
import client.Client;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class OnlineScene {
	private static Client clientCon;
	private static BorderPane border;

	private static int i = 0;

	public static Scene layout(Stage primaryStage) throws IOException {
		border = new BorderPane();

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(15, 0, 0, 0));
		grid.setAlignment(Pos.BASELINE_CENTER);
		grid.setStyle("-fx-background-color:#eeeeee; -fx-opacity:1;");
		grid.setHgap(20);

		TextField ipAddressTxt = new TextField();
		ipAddressTxt.setPromptText("Enter host IP Address");
		ipAddressTxt.setStyle("-fx-padding: 8; -fx-font-size: 18px;");
		ipAddressTxt.setText("192.168.x.x");

		TextField usernameTxt = new TextField();
		usernameTxt.setPromptText("Enter Username");
		usernameTxt.setStyle("-fx-padding: 8; -fx-font-size: 18px;");

		TextField channelTxt = new TextField();
		channelTxt.setPromptText("Enter Channel");
		channelTxt.setStyle("-fx-padding: 8; -fx-font-size: 18px;");

		Button connectToSocketBtn = new Button("CONNECT TO SERVER");
		connectToSocketBtn.getStyleClass().add("menuBtn");

		connectToSocketBtn.setOnAction(event -> {
			try {
				if (UsernameCommand.validUsername(usernameTxt.getText())
						&& JoinCommand.validChannelName("#" + channelTxt.getText())) {
					setUpClientConnection(ipAddressTxt.getText(), usernameTxt.getText(), primaryStage);

					// USERNAME Command
					clientCon.writeToServer("USERNAME");
					// Username
					clientCon.writeToServer(usernameTxt.getText());
					// JOIN Command
					clientCon.writeToServer("JOIN");
					// Channel name
					clientCon.writeToServer("#" + channelTxt.getText());

					waitingRoom(primaryStage, usernameTxt.getText(), "#" + channelTxt.getText());

					new Thread(clientCon).start();
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Username or Channel");
					alert.setHeaderText(null);
					alert.setContentText("Ooops, it seems that the username is not valid or channel name\n"
							+ "Please try again with more then three character in each text field");

					alert.showAndWait();
				}

			} catch (ConnectException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Server");
				alert.setHeaderText("Connection refused: connect");
				alert.setContentText("Ooops, it seems that the server is not running, please try again");
				alert.showAndWait();

			} catch (UnknownHostException e1) {
				e1.printStackTrace();

			} catch (IOException e1) {
				e1.printStackTrace();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		grid.add(ipAddressTxt, 0, 0);
		grid.add(usernameTxt, 0, 1);
		grid.add(channelTxt, 0, 2);

		grid.add(connectToSocketBtn, 0, 5);

		border.setTop(Interface.logo());
		border.setCenter(grid);
		border.setBottom(backBtn(primaryStage));

		Scene scene = new Scene(border, 800, 500);

		scene.getStylesheets().add(Interface.class.getResource("style.css").toExternalForm());

		return scene;
	}

	public static void waitingRoom(Stage primaryStage, String yourUsername, String channel)
			throws InterruptedException {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(15, 0, 0, 0));
		grid.setAlignment(Pos.BASELINE_CENTER);
		grid.setStyle("-fx-background-color:#eeeeee; -fx-opacity:1;");
		grid.setHgap(20);

		Label waitingLbl = new Label();
		waitingLbl.setStyle("-fx-padding: 8; -fx-font-size: 40px; -fx-alignment: center;");

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						waitingLbl.setText("WAITING " + channel + " for " + Integer.toString(i++) + " seconds");
					}
				});
			}
		}, 0, 1000);

		grid.add(waitingLbl, 0, 0);
		
		border.setCenter(grid);
	}

	public static HBox backBtn(Stage primaryStage) {
		HBox hBox = new HBox();

		hBox.setAlignment(Pos.BASELINE_LEFT);
		hBox.setPadding(new Insets(0, 0, 15, 20));

		Button backBtn = new Button("Back");
		backBtn.getStyleClass().add("round-red");
		backBtn.setOnAction(e -> {
			if (clientCon != null) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Connection");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to exit the server?");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					primaryStage.setScene(GameModeScene.layout(primaryStage));
					clientCon.close();
				} else {
					alert.close();
				}
			} else {
				primaryStage.setScene(GameModeScene.layout(primaryStage));
			}
		});

		hBox.getChildren().add(backBtn);

		return hBox;
	}

	public static void setUpClientConnection(String ip, String username, Stage primaryStage) throws UnknownHostException, IOException {
		Socket socket = new Socket(ip, ServerProgram.getPort());
		clientCon = new Client(socket, username, primaryStage);
	}
}
