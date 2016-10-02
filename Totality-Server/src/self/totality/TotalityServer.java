package self.totality;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TotalityServer
{
	public static final int PORT = 8000;
	
	public static TotalityServer instance;
	
	static
	{
		instance = new TotalityServer();
	}
	
	boolean running = true;
	
	ServerSocket serverSocket;
	public ArrayList<ConnectedClient> connectedClients;
	
	GameController defaultController;
	
	ArrayList<ConnectListener> connectionListeners;
	ArrayList<DataListener> dataListeners;
	ArrayList<DisconnectListener> disconnectListeners;
	
	Gson gson;
	
	private TotalityServer()
	{
		connectedClients = new ArrayList<>();
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
			serverSocket = new ServerSocket(PORT);
			serverSocket.setSoTimeout(1000);
			
			try
			{
				JmDNS jmDNS = JmDNS.create(InetAddress.getByName("192.168.1.142"), "trek");
				ServiceInfo info = ServiceInfo.create("_http._tcp.local.", "trek", 80, "Trek Webserver");
				
				jmDNS.registerService(info);
			}
			catch (IOException e)
			{
				System.out.println("Error setting up bonjour");
				e.printStackTrace();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				while(true)
				{
					for (int i = 0; i < connectedClients.size(); i++)
						if (!connectedClients.get(i).connected)
							connectedClients.remove(i);
					
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}).start();
		
		acceptLoop();
	}
	
	void acceptLoop()
	{
		while (running)
		{
			try
			{
				Socket socket = serverSocket.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				String curLine = in.readLine();
				
				if (curLine == null)
					continue;
				
				if (!curLine.equals("GET / HTTP/1.1"))
				{
					System.out.println("Failed to get a GET request.");
					continue;
				}
				
				HashMap<String, String> clientHTTP = ClientUtils.readHTTPSettings(in);
				
				if (clientHTTP.containsKey("Connection") && clientHTTP.containsKey("Upgrade"))
					if (!(clientHTTP.get("Connection").contains("Upgrade") && clientHTTP.get("Upgrade").equals("websocket")))
					{
						System.out.println("No upgrade request.");
						continue;
					}
				
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				out.write("HTTP/1.1 101 Switching Protocols\r\n");
				out.write("Upgrade: websocket\r\n");
				out.write("Connection: Upgrade\r\n");
				out.write("Sec-WebSocket-Accept: " + ClientUtils.getWebsocketKey(clientHTTP.get("Sec-WebSocket-Key")) + "\r\n");
				out.write("\r\n");
				out.flush();
				
				System.out.println("Client connected");
				ConnectedClient client = new ConnectedClient(socket);
				connectedClients.add(client);
				client.start();
			}
			catch (SocketTimeoutException e)
			{
				
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void addDefaultControllerElement(ControllerElementType type, String id)
	{
		switch (type)
		{
			case BUTTON:
				defaultController.controllerElements.add(new Button(id, false));
				break;
			case JOYSTICK:
				defaultController.controllerElements.add(new Joystick(id, 0, 0));
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
