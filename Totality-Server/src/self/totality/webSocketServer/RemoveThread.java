package self.totality.webSocketServer;

import java.util.ArrayList;
import java.util.UUID;

public class RemoveThread extends Thread
{
	public static final byte[] PING_DATA = "ping".getBytes();
	
	static int disconnectTime = 1000;
	
	WebSocketServer server;
	
	public static void setDisconnectTime(int time)
	{
		disconnectTime = time;
	}
	
	public RemoveThread(WebSocketServer server)
	{
		this.server = server;
	}
	
	public void run()
	{
		ArrayList<UUID> toRemove = new ArrayList<>();

		while (!this.isInterrupted())
		{
			for (UUID u : server.connectedClients.keySet())
			{
				ConnectedClient c = server.connectedClients.get(u);
								
				if (!c.respondedToPing)
				{
					c.connected = false;
				}
				else
				{
					try
					{
						ClientUtils.sendMessage(c.out, ConnectedClient.PING_OPCODE, "ping".getBytes());
					}
					catch (Exception e)
					{
						c.connected = false;
					}
				}
				
				if (!c.connected)
				{
					toRemove.add(u);
				}
			}
			
			for (UUID u : toRemove)
			{
				server.connectedClients.get(u).disconnect();
			}
			
			toRemove.clear();
			
			try
			{
				Thread.sleep(disconnectTime);
			}
			catch (InterruptedException e)
			{
				
			}
		}
	}
}
