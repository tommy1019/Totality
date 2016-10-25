package self.totality.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WebServer extends Thread
{
	public static final int PORT = 8080;
	
	ServerSocket serverSocket;

	public WebServer()
	{
		try
		{
			serverSocket = new ServerSocket(PORT);
			serverSocket.setSoTimeout(1000);
		}
		catch (IOException e)
		{
			System.out.println("Error starting web server");
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
