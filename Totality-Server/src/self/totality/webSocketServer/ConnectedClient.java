package self.totality.webSocketServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.UUID;

import self.totality.TotalityServer;
import self.totality.webSocketServer.controller.GameController;
import self.totality.webSocketServer.listener.ConnectListener;
import self.totality.webSocketServer.listener.DisconnectListener;

class ConnectedClient extends Thread
{
	public static final int TEXT_OPCODE = 1;
	public static final int BINA_OPCODE = 2;
	public static final int PING_OPCODE = 9;
	public static final int PONG_OPCODE = 10;
	
	Socket socket;
	
	DataInputStream in;
	DataOutputStream out;
	
	boolean connected = true;
	
	boolean respondedToPing = true;
	
	UUID uuid;
	
	WebSocketServer server;
	
	public ConnectedClient(Socket socket, WebSocketServer server)
	{
		this.socket = socket;
		this.server = server;
		
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
		
		server.connectedClients.remove(uuid);
		
		for (DisconnectListener l : TotalityServer.instance.getDisconnectListeners())
			l.onDisconnect(uuid);
	}
	
	void updateController(GameController controller)
	{
		try
		{
			ClientUtils.sendMessage(out, TEXT_OPCODE, TotalityServer.gson.toJson(controller).getBytes());
		}
		catch (Exception e)
		{
			connected = false;
		}
	}
	
	public void run()
	{
		for (ConnectListener l : TotalityServer.instance.getConnectListeners())
			l.onConnect(uuid);
				
		try
		{
			ClientUtils.sendMessage(out, TEXT_OPCODE, TotalityServer.gson.toJson(TotalityServer.instance.getDefaultController()).getBytes());
		}
		catch (Exception e2)
		{
			connected = false;
		}
		
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
						PacketProcessor.processPacket(msg, uuid);
						break;
					case 9:
						ClientUtils.sendMessage(out, PONG_OPCODE, clientMessage.x);
						break;
					case 10:
						respondedToPing = true;
						break;
				}
			}
			catch (Exception e1)
			{
				connected = false;
			}
		}
	}
}
