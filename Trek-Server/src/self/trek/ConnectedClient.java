package self.trek;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ConnectedClient extends Thread
{
	public static final int TEXT_OPCODE = 1;
	public static final int BINA_OPCODE = 2;
	public static final int PING_OPCODE = 9;
	public static final int PONG_OPCODE = 10;
	
	Socket socket;
	
	DataInputStream in;
	DataOutputStream out;
	
	public GameController gameController;
	
	boolean connected = true;
	
	public ConnectedClient(Socket socket)
	{
		this.socket = socket;
		
		gameController = new GameController();
		
		for (Tuple<ControllerElementType, String> t : TrekServer.instance.defaultController)
		{
			switch(t.x)
			{
				case BUTTON:
					gameController.addControllerElement(new Button(t.y));
					break;
				default:
					break;
			}
		}
		
		try
		{
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void disconnect()
	{
		connected = false;
		try
		{
			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		// Send client initlization data
		ClientUtils.sendMessage(out, TEXT_OPCODE, TrekServer.instance.gson.toJson(gameController.controllerElements.values()).getBytes());
		
		while (connected)
		{
			Tuple<byte[], Integer> clientMessage;
			try
			{
				clientMessage = ClientUtils.readMessageFully(in);
				
				switch (clientMessage.y)
				{
					case 1:
					case 2:
						String msg = new String(clientMessage.x);
						
						System.out.println(new String(clientMessage.x));
						
						String[] msgParts = msg.split(":");
						
						ControllerElement e = gameController.controllerElements.get(msgParts[1]);
						
						switch (msgParts[0])
						{
							case "BUTTON":
								Button b = (Button) e;
								b.isPressed = msgParts[3].equals("true");
								break;
						}
						
						break;
					case 9:
						ClientUtils.sendMessage(out, PONG_OPCODE, clientMessage.x);
						break;
					case 10:
						System.out.println(new String(clientMessage.x));
						break;
				}
			}
			catch (EOFException e)
			{
				System.out.println("Reached end of stream for client.");
				connected = false;
			}
		}
	}
}
