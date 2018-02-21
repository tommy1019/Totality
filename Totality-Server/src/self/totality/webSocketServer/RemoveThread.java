package self.totality.webSocketServer;

import java.util.ArrayList;
import java.util.UUID;

public class RemoveThread extends Thread
{
	WebSocketServer server;
	
	public RemoveThread(WebSocketServer server)
	{
		this.server = server;
	}
	
	public void run()
	{
		ArrayList<UUID> toRemove = new ArrayList<>();

		while (this.isInterrupted())
		{
			for (UUID u : server.connectedClients.keySet())
			{
				if (!server.connectedClients.get(u).connected)
					toRemove.add(u);
			}
			
			for (UUID u : toRemove)
			{
				server.connectedClients.get(u).disconnect();
			}
			
			toRemove.clear();
			
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				
			}
		}
	}
}
