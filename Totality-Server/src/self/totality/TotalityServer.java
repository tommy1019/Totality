package self.totality;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import self.totality.webServer.WebServer;
import self.totality.webSocketServer.WebSocketServer;
import self.totality.webSocketServer.controller.ControllerElement;
import self.totality.webSocketServer.controller.ControllerElementType;
import self.totality.webSocketServer.controller.GameController;
import self.totality.webSocketServer.controller.VisibleControllerElement;
import self.totality.webSocketServer.listener.ConnectListener;
import self.totality.webSocketServer.listener.DataListener;
import self.totality.webSocketServer.listener.DisconnectListener;

public class TotalityServer
{
	
	public static TotalityServer instance;
	
	public static String localIp;
	public static Gson gson;
	
	static
	{
		instance = new TotalityServer();
	}
	
	boolean running = true;
	
	WebServer webServer;
	WebSocketServer webSocketServer;
	
	private GameController defaultController;
	
	ArrayList<ConnectListener> connectionListeners;
	private ArrayList<DataListener> dataListeners;
	private ArrayList<DisconnectListener> disconnectListeners;
	
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
			localIp = "0.0.0.0";
		}
		
		System.out.println("[Totality server] Local ip: " + localIp);
		
		webSocketServer.start();		
		webServer.start();
	}
	
	public void addControllerElement(String id, ControllerElementType type)
	{
		if (type.visible)
			throw new IllegalArgumentException("Controller element is visible: " + id);
		
		defaultController.controllerElements.add(new ControllerElement(id, type));
	}
	
	public void addControllerElement(String id, ControllerElementType type, float x, float y, float width, float height)
	{
		if (!type.visible)
			throw new IllegalArgumentException("Controller element is not visible: " + id);
		
		defaultController.controllerElements.add(new VisibleControllerElement(id, type, x, y, width, height));
	}
	
	public GameController getDefaultController()
	{
		return defaultController;
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
