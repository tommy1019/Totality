package self.totality;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TotalityServer
{

	public static TotalityServer instance;

	static
	{
		instance = new TotalityServer();
	}

	boolean running = true;

	WebServer webServer;
	WebSocketServer webSocketServer;

	GameController defaultController;

	ArrayList<ConnectListener> connectionListeners;
	ArrayList<DataListener> dataListeners;
	ArrayList<DisconnectListener> disconnectListeners;

	Gson gson;

	private TotalityServer()
	{
		webSocketServer = new WebSocketServer();
		webServer = new WebServer();

		defaultController = new GameController();

		connectionListeners = new ArrayList<>();
		dataListeners = new ArrayList<>();
		disconnectListeners = new ArrayList<>();

		GsonBuilder builder = new GsonBuilder();
		gson = builder.serializeNulls().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
	}

	public void start()
	{
		try
		{
			JmDNS jmDNS = JmDNS.create(InetAddress.getByName("192.168.1.175"), "totality");
			ServiceInfo info = ServiceInfo.create("_http._tcp.local.", "totality", 8080, "Totality Webserver");

			jmDNS.registerService(info);
		}
		catch (IOException e)
		{
			System.out.println("Error setting up bonjour");
			e.printStackTrace();
		}

		// TODO: Better system for removing users
		// new Thread(new Runnable()
		// {
		//
		// @Override
		// public void run()
		// {
		// while(true)
		// {
		// for (int i = 0; i < connectedClients.size(); i++)
		// if (!connectedClients.get(i).connected)
		// connectedClients.remove(i);
		//
		// try
		// {
		// Thread.sleep(1000);
		// }
		// catch (InterruptedException e)
		// {
		// e.printStackTrace();
		// }
		// }
		// }
		// }).start();

		webSocketServer.start();
		webServer.start();
	}

	public void addDefaultControllerElement(ControllerElementType type, String id)
	{
		switch (type)
		{
			case BUTTON:
				defaultController.controllerElements.add(new Button(id, false));
				break;
			case JOYSTICK:
				defaultController.controllerElements.add(new Joystick(id, 0, 0, 0));
				break;
			default:
				break;
		}
	}

	public void addConnectListener(ConnectListener l)
	{
		connectionListeners.add(l);
	}

	public void addDataListener(DataListener l)
	{
		dataListeners.add(l);
	}

	public void addDisconnectListener(DisconnectListener l)
	{
		disconnectListeners.add(l);
	}
}
