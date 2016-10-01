package self.trek;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

public class TrekServer
{
	public static final int PORT = 8000;
	
	public static final String MAGIC_WEBSOCKET_STRING = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
	
	ServerSocket serverSocket;
	
	public TrekServer()
	{
		try
		{
			serverSocket = new ServerSocket(PORT);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void start()
	{
		System.out.println("Accepting...");
		
		try
		{
			Socket client = serverSocket.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			HashMap<String, String> clientHttp = new HashMap<>();
			
			String curLine = in.readLine();
			
			if (!curLine.equals("GET / HTTP/1.1"))
			{
				System.out.println("Failed to get a GET request.");
				return;
			}
			
			while ((curLine = in.readLine()) != null)
			{
				if (curLine.equals(""))
					break;
				
				int colenIndex = curLine.indexOf(':');
				
				if (colenIndex == -1)
				{
					System.out.println("Can't find: ':'");
					System.out.println(curLine);
					break;
				}
				
				clientHttp.put(curLine.substring(0, colenIndex), curLine.substring(colenIndex + 2));
			}
			
			if (!(clientHttp.get("Connection").equals("Upgrade") && clientHttp.get("Upgrade").equals("websocket")))
			{
				System.out.println("No upgrade request.");
				// TODO: Send failure message.
				return;
			}
			
			String webSocketKey = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1")
					.digest((clientHttp.get("Sec-WebSocket-Key") + MAGIC_WEBSOCKET_STRING).getBytes()));
			
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			out.write("HTTP/1.1 101 Switching Protocols\r\n");
			out.write("Upgrade: websocket\r\n");
			out.write("Connection: Upgrade\r\n");
			out.write("Sec-WebSocket-Accept: " + webSocketKey + "\r\n");
			out.write("\r\n");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
	}
}
