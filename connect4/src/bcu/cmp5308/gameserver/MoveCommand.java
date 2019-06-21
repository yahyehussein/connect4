package bcu.cmp5308.gameserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class implements the Move Command 
 * @author Abdel-Rahman Tawil
 * (c) Birmingham City University, March 2018
 * You may modify this code and use it in your own programs
 */

public class MoveCommand implements Command{
	//private PrintWriter out;
	private BufferedReader in;
	private GameConnection serverConn;
	GameServer server;

	GameConnection opponent;

	public void execute() throws IOException
	{	
		String move="";

		try{
			move = in.readLine();
		}catch(IOException e){
			System.err.println(e);
		}
		this.opponent = serverConn.getOpponent();

		if (opponent != null)

			opponent.send(GameProtocol.OK + " >> " + "MOVE %s %s", serverConn.getUsername(), move);

	}

	public MoveCommand(BufferedReader in, PrintWriter out, 
			GameConnection serverConn)
	{
		//this.out = out;
		this.in = in;
		this.serverConn = serverConn;
		this.server = serverConn.returnServer();

	}
}
