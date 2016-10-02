package self.totality;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.UUID;

public class ConnectedClient extends Thread
{
	public static final int TEXT_OPCODE = 1;
	public static final int BINA_OPCODE = 2;
	public static final int PING_OPCODE = 9;
	public static final int PONG_OPCODE = 10;
	
	Socket socket;
	
	DataInputStream in;
	DataOutputStream out;
		
	public float tmpX = 0.0f;
	public float tmpY = 0.0f;
	
	boolean connected = true;
	
	UUID uuid;
	
	public ConnectedClient(Socket socket)
	{
		this.socket = socket;
				
		Random random = new Random();
		uuid = new UUID(random.nextLong(), random.nextLong());
		
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
		
		for (DisconnectListener l : TotalityServer.instance.disconnectListeners)
			l.onDisconnect(uuid);
	}
	
	public void run()
	{
		for (ConnectListener l : TotalityServer.instance.connectionListeners)
			l.onConnect(uuid);
		
		ClientUtils.sendMessage(out, TEXT_OPCODE, TotalityServer.instance.gson.toJson(TotalityServer.instance.defaultController).getBytes());
		
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
						
						//System.out.println(new String(clientMessage.x));
						
						String[] msgParts = msg.split(":");
						
						ControllerElement e = null;
						
						switch (msgParts[0])
						{
							case "BUTTON":
								e = new Button(msgParts[1], msgParts[3].equals("true"));
								break;
							case "JOYSTICK":
								e = new Joystick(msgParts[1], Double.parseDouble(msgParts[3]), Double.parseDouble(msgParts[4]));
								break;
						}
						
						for (DataListener l : TotalityServer.instance.dataListeners)
							l.onDataUpdate(uuid, e);
						
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
