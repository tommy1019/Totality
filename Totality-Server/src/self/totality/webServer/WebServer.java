package self.totality.webServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WebServer extends Thread
{	
	ServerSocket serverSocket;
	
	public WebServer(int port)
	{
		try
		{
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(1000);
		}
		catch (IOException e)
		{
			System.out.println("[TotalityServer] Error starting web server");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void run()
	{
		System.out.println("[Totality server] Started web server.");
		
		while (!this.isInterrupted())
		{
			try
			{
				Socket socket = serverSocket.accept();
				new WebServerConnection(socket).start();
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
