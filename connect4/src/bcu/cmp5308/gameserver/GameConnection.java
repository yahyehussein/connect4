
package bcu.cmp5308.gameserver;

import java.io.*;
import java.net.Socket;

/**
 * This class implements the Game Connection Thread
 * A new game connection object is created for each
 * new user connection 
 * @author Andrew Kay & Abdel-Rahman Tawil
 * (c) Birmingham City University, March 2018
 * You may modify this code and use it in your own programs
 */


public class GameConnection implements Runnable {
	private boolean verbose;
	String selectedCommand;

	Command command = null;

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	private GameServer server;
	private String username, channelName;
	private GameConnection opponent;

	public GameConnection(GameServer server, Socket socket) throws IOException {
		this.server = server;
		this.socket = socket;

		InputStreamReader isr = new InputStreamReader(socket.getInputStream());
		this.in = new BufferedReader(isr);

		OutputStream os = socket.getOutputStream();
		this.out = new PrintWriter(os);

		// We want debugging messages
		verbose = true;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChannelName() {
		return channelName;
	}

	@Override
	public String toString() {
		if(username == null) {
			return String.format("@%x", hashCode());
		} else if(channelName == null) {
			return username;
		} else {
			return username + channelName;
		}
	}

	void send(String msg, Object... args) {
		if(!isClosed()) {
			if(args.length > 0) {
				msg = String.format(msg, args);
			}

			System.out.println(this + " >> " + msg);
			out.println(msg);
			out.flush();
		}
	}

	String receive() throws IOException {
		if(isClosed()) {
			return null;
		} else {
			String line = in.readLine();
			if(line == null) {
				throw new IOException("Received null from connection " + this + ".");
			}

			System.out.println(this + " << " + line);
			return line;
		}
	}

	public void close() {
		server.deregisterConnection(this);
		if(!isClosed()) {
			try {
				send(GameProtocol.OK + " >> " + "GOODBYE %s", username);
				socket.close();
			} catch(IOException e2) {
				System.err.println("While closing, connection " + this + " caused " + e2);
			}
		}
	}

	public boolean isClosed() {
		return socket.isClosed();
	}

	void setOpponent(GameConnection opponent) throws GameProtocolException {
		if(this.opponent != null) {
			throw new GameProtocolException("Connection " + this + " already has opponent.");
		}

		this.opponent = opponent;
	}

	@Override
	public void run() {

		try {
			System.out.println("Connection " + this + " opened.");
			send(GameProtocol.OK + " >> "+ "HI");


			// get the appropriate Command class from the CommandFactory
			command = CommandFactory.getCommand(in, out, this);

			// Print logging information
			userMsg("CommandFactory returned a " + getCommandString(command));
					

			selectedCommand =  getCommandString(command);
			while(!getCommand(selectedCommand).equals("StartCommand")){
				send(GameProtocol.ERROR + " >> " + "ERROR Client must issue HELLO command.");
				command = CommandFactory.getCommand(in, out, this);
				selectedCommand =  getCommandString(command);
			}

			// Execute the Command 
			command.execute();

			send(GameProtocol.OK + " >> " + "CHOOSE_USERNAME");

			//Username Command
			command = CommandFactory.getCommand(in, out, this);

			userMsg("CommandFactory returned a " + getCommandString(command));

			selectedCommand =  getCommandString(command);
			while(!getCommand(selectedCommand).equals("UsernameCommand")){
				send(GameProtocol.ERROR + " >> " + "ERROR Client must issue USERNAME command.");
				command = CommandFactory.getCommand(in, out, this);
				selectedCommand =  getCommandString(command);
			}

			// Execute the Command 
			command.execute();

			send(GameProtocol.OK + " >> " + "WELCOME %s", username);

			//JOIN or TEST Command
			command = CommandFactory.getCommand(in, out, this);

			userMsg("CommandFactory returned a " + getCommandString(command));

			// wait for JOIN or TEST command
			selectedCommand =  getCommandString(command);

			while(!getCommand(selectedCommand).equals("JoinCommand") && !getCommand(selectedCommand).equals("TestCommand") ){
				send(GameProtocol.ERROR + " >> " + "ERROR Client must issue JOIN or TEST command.");
				command = CommandFactory.getCommand(in, out, this);
				selectedCommand =  getCommandString(command);
			}

			// Execute the Command 
			command.execute();

		} catch(Exception e) {
			if(!isClosed()) {
				System.err.println("Connection " + this + " already closed, caused " + e);
			} else {
				System.err.println("Closing connection " + this + " due to " + e);
				send(GameProtocol.ERROR + " >> " + "ERROR %s", e);
			}
		} finally {
			if(opponent != null && !opponent.isClosed()) {
				opponent.send(GameProtocol.OK + " >> " + "OPPONENT_CLOSED %s", username);
			}
			close();
			System.out.println("Connection " + this + " closed.");
		}
	}

	public void echoLoop() throws IOException {
		while(!isClosed()) {
			//Move, CHAT, EXCHANGE or CLOSE Command
			command = CommandFactory.getCommand(in, out, this);

			userMsg("CommandFactory returned a " + getCommandString(command));

			//Move, CHAT, EXCHANGE or CLOSE Command
			selectedCommand =  getCommandString(command);

			while(!getCommand(selectedCommand).equals("MoveCommand") && !getCommand(selectedCommand).equals("ChatCommand") 
					&& !getCommand(selectedCommand).equals("CloseCommand") && !getCommand(selectedCommand).equals("ExchangeCommand")){

				send(GameProtocol.ERROR + " >> " + "ERROR Client must issue MOVE, CHAT, EXCHANGE or a CLOSE command.");
				command = CommandFactory.getCommand(in, out, this);
				selectedCommand =  getCommandString(command);
	
			}

			// Execute the Command 
			command.execute();

			if(getCommand(selectedCommand).equals("CloseCommand")) return;

		}
	}

	public GameServer returnServer() {
		return server;
	}

	public void testProtocol(String gameName) {
		// TODO: implement the TEST command
		send(GameProtocol.ERROR + " >> " + "ERROR The TEST command is not implemented.");
	}

	private String getCommand(String selectedCommand){
		return selectedCommand.split("\\.")[3];
	}

	public GameConnection getOpponent(){
		return opponent;
	}

	void userMsg(String msg) {
		if (verbose) {
			System.out.println(msg);
		}
	}
	
	String getCommandString(Command command){
		return ((Object)command).getClass().getName();
	}

}
