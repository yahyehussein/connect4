package bcu.cmp5308.gameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ExchangeCommand implements Command {
	//private PrintWriter out;
	private BufferedReader in;
	private GameConnection serverConn;
	GameServer server;

	GameConnection opponent;

	public void execute() throws IOException
	{	
		this.opponent = serverConn.getOpponent();
		if (opponent != null)
			opponent.send("opponent="+serverConn.getUsername());
	}

	public ExchangeCommand(BufferedReader in, PrintWriter out, 
			GameConnection serverConn)
	{
		//this.out = out;
		this.in = in;
		this.serverConn = serverConn;
		this.server = serverConn.returnServer();

	}
}
