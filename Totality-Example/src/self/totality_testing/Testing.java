package self.totality_testing;

import java.util.UUID;

import self.totality.TotalityServer;
import self.totality.webSocketServer.controller.ControllerElement;
import self.totality.webSocketServer.controller.ControllerElementType;
import self.totality.webSocketServer.listener.ConnectListener;
import self.totality.webSocketServer.listener.DataListener;
import self.totality.webSocketServer.listener.DisconnectListener;

public class Testing
{
	public static void main(String args[])
	{
		TotalityServer.instance.addDefaultControllerElement(ControllerElementType.BUTTON, "button1");
		TotalityServer.instance.addDefaultControllerElement(ControllerElementType.BUTTON, "button2");
		TotalityServer.instance.addDefaultControllerElement(ControllerElementType.JOYSTICK, "joystick1");
		
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
