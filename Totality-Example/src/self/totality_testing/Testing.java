package self.totality_testing;

import java.util.UUID;

import self.totality.ConnectListener;
import self.totality.ControllerElement;
import self.totality.DataListener;
import self.totality.DisconnectListener;
import self.totality.TotalityServer;

public class Testing
{
	public static void main(String args[])
	{
		TotalityServer.instance.addConnectListener(new ConnectListener()
		{
			@Override
			public void onConnect(UUID uuid)
			{
				System.out.println("Connection: " + uuid);
			}
		});

		TotalityServer.instance.addDisconnectListener(new DisconnectListener()
		{
			@Override
			public void onDisconnect(UUID uuid)
			{
				System.out.println("Disconnection: " + uuid);
			}
		});

		TotalityServer.instance.addDataListener(new DataListener()
		{
			@Override
			public void onDataUpdate(UUID uuid, ControllerElement e)
			{
				System.out.println("Data Update: " + uuid);
			}
		});
		
		TotalityServer.instance.start();
	}
}
