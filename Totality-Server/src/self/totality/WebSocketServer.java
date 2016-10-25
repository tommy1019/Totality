package self.totality;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.UUID;

import self.totality.webserver.WebUtils;

public class WebSocketServer extends Thread
{
	public static final int PORT = 8000;

	ServerSocket serverSocket;	
	public HashMap<UUID, ConnectedClient> connectedClients;

	public WebSocketServer()
	{
		connectedClients = new HashMap<UUID, ConnectedClient>();
		
		try
		{
			serverSocket = new ServerSocket(PORT);
			serverSocket.setSoTimeout(1000);
		}
		catch (IOException e)
		{
			System.out.println("Error starting web socket server");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void run()
	{
		while (!this.isInterrupted())
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

				HashMap<String, String> clientHTTP = WebUtils.readHTTPSettings(in);

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
				
				connectedClients.put(client.uuid, client);
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
}
