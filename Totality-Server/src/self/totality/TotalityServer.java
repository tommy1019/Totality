package self.totality;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

	String localIp = "0.0.0.0";
	
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
		System.out.println("[Totality server] Starting");
		
		try
		{
			localIp = InetAddress.getLocalHost().getHostAddress();
		}
		catch (UnknownHostException e)
		{
			
		}
		
		System.out.println("[Totality server] Local ip: " + localIp);

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
		System.out.println("[Totality server] Started web socket server.");
		
		webServer.start();
		System.out.println("[Totality server] Started web server.");
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
