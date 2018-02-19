package self.totality;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import self.totality.multicastServer.MulticastServer;
import self.totality.webServer.WebServer;
import self.totality.webSocketServer.WebSocketServer;
import self.totality.webSocketServer.controller.GameController;
import self.totality.webSocketServer.listener.ConnectListener;
import self.totality.webSocketServer.listener.DataListener;
import self.totality.webSocketServer.listener.DisconnectListener;

public class TotalityServer
{
	public static TotalityServer instance;
	
	public static String localIp;
	public static Gson gson;
	
	private static int webServerPort = 80;
	
	static
	{
		instance = new TotalityServer();
	}
		
	private WebServer webServer;
	private WebSocketServer webSocketServer;
	private MulticastServer multicastServer;
	
	private GameController defaultController;
	
	private ArrayList<ConnectListener> connectionListeners;
	private ArrayList<DataListener> dataListeners;
	private ArrayList<DisconnectListener> disconnectListeners;
	
	private TotalityServer()
	{
		webSocketServer = new WebSocketServer();
		webServer = new WebServer(webServerPort);
		
		defaultController = new GameController();
		
		connectionListeners = new ArrayList<>();
		dataListeners = new ArrayList<>();
		disconnectListeners = new ArrayList<>();
		
		GsonBuilder builder = new GsonBuilder();
		gson = builder.serializeNulls().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
	}
	
	public void startMulticastServer(String domain)
	{
		multicastServer = new MulticastServer(domain);
		multicastServer.start();
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
			localIp = "0.0.0.0";
		}
		
		System.out.println("[Totality server] Local ip: " + localIp);
		
		webSocketServer.start();		
		webServer.start();
	}
	
	public void setWebPort(int port)
	{
		webServerPort = port;
	}
	
	public void setDefaultController(GameController defaultController)
	{
		this.defaultController = defaultController;
	}
	
	public GameController getDefaultController()
	{
		return defaultController;
	}
	
	public void sendControllerToPlayer(UUID uuid, GameController gameController)
	{
		webSocketServer.sendControllerToPlayer(uuid, gameController);
	}
	
	public void addDataListener(DataListener l)
	{
		dataListeners.add(l);
	}
	
	public void addConnectListener(ConnectListener l)
	{
		connectionListeners.add(l);
	}
	
	public void addDisconnectListener(DisconnectListener l)
	{
		disconnectListeners.add(l);
	}
	
	public ArrayList<DataListener> getDataListeners()
	{
		return dataListeners;
	}
	
	public ArrayList<ConnectListener> getConnectListeners()
	{
		return connectionListeners;
	}
	
	public ArrayList<DisconnectListener> getDisconnectListeners()
	{
		return disconnectListeners;
	}
}
