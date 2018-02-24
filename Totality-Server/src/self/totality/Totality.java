package self.totality;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import self.totality.multicastServer.MulticastServer;
import self.totality.webServer.WebServer;
import self.totality.webSocketServer.PacketProcessor;
import self.totality.webSocketServer.PacketProcessor.ControllerElementProcessor.Listener;
import self.totality.webSocketServer.RemoveThread;
import self.totality.webSocketServer.WebSocketServer;
import self.totality.webSocketServer.controller.ControllerElement.DataClass;
import self.totality.webSocketServer.controller.GameController;
import self.totality.webSocketServer.listener.ConnectListener;
import self.totality.webSocketServer.listener.DisconnectListener;

public class Totality
{
	/**
	 * Holds the main Totality Server instance. All calls to Totality should go through this.
	 */
	public static Totality instance;

	public static String localIp;
	public static Gson gson;

	static
	{
		instance = new Totality();
	}

	private WebServer webServer;
	private WebSocketServer webSocketServer;
	private RemoveThread removeThread;
	private MulticastServer multicastServer;

	private GameController defaultController;

	private ArrayList<ConnectListener> connectionListeners;
	private ArrayList<DisconnectListener> disconnectListeners;

	private int webServerPort = 80;

	private Totality()
	{
		defaultController = new GameController();

		connectionListeners = new ArrayList<>();
		disconnectListeners = new ArrayList<>();

		GsonBuilder builder = new GsonBuilder();
		gson = builder.serializeNulls().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
	}

	/**
	 * Start a multicast dns resolver. This allows clients that support multicast dns (mainly iOS) to connect using a .local domain name instead of typing in an ip address.
	 * 
	 * @param domain
	 *            The domain to be used for the multicast dns resolver. Clients can use "domain".local to connect to Totality
	 */
	public void startMulticastServer(String domain)
	{
		multicastServer = new MulticastServer(domain);
		multicastServer.start();
	}

	/**
	 * Starts all of Totality's services, including:
	 * 
	 * WebServer - Serves files from the server to allow clients to download the required web page. WebSocketServer - Handles connections and data transfer from controllers. RemoveThread -
	 * Periodically removes clients that have stopped responding.
	 */
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

		webServer = new WebServer(webServerPort);
		webSocketServer = new WebSocketServer();

		webSocketServer.start();
		webServer.start();

		removeThread = new RemoveThread(webSocketServer);
		removeThread.start();
	}

	/**
	 * Changes the port that the web server uses.
	 * 
	 * @param port
	 *            The port to be used. Clients will connect at http://[ip]:[port]
	 */
	public void setWebPort(int port)
	{
		this.webServerPort = port;
	}

	/**
	 * Sets the controller that will be sent to the clients on connect.
	 * 
	 * @param defaultController
	 */
	public void setDefaultController(GameController defaultController)
	{
		this.defaultController = defaultController;
	}

	/**
	 * @return The current default controller.
	 */
	public GameController getDefaultController()
	{
		return defaultController;
	}

	/**
	 * Sends a controller to a player. This updates the visible controller for the specified client.
	 * 
	 * @param uuid
	 *            The UUID of the player to send the controller to.
	 * @param gameController
	 *            The controller to send to the player.
	 */
	public void sendControllerToPlayer(UUID uuid, GameController gameController)
	{
		webSocketServer.sendControllerToPlayer(uuid, gameController);
	}

	/**
	 * Adds a ConnectListener to be called whenever a player connects
	 * 
	 * @param l
	 *            The ConnectListener to be added.
	 */
	public void addConnectListener(ConnectListener l)
	{
		connectionListeners.add(l);
	}

	/**
	 * Adds a DisconnectListener to be called whenever a player disconnects
	 * 
	 * @param l
	 *            The DisconnectListener to be added.
	 */
	public void addDisconnectListener(DisconnectListener l)
	{
		disconnectListeners.add(l);
	}

	/**
	 * Adds a listener to listen for data on a specific type of controller element.
	 * @param type The type of controller element to listen for.
	 * @param listener The listener to be called when there is new data.
	 */
	public static void addDataListener(String type, Listener<? extends DataClass> listener)
	{
		PacketProcessor.registerListener(type, listener);
	}

	/**
	 * Gives all listeners currently listening for connects.
	 * @return
	 */
	public ArrayList<ConnectListener> getConnectListeners()
	{
		return connectionListeners;
	}

	/**
	 * Gives all listeners currently listening for disconnects.
	 * @return
	 */
	public ArrayList<DisconnectListener> getDisconnectListeners()
	{
		return disconnectListeners;
	}
}
