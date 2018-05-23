package self.totality.webServer;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WebServer extends Thread
{	
	ServerSocket serverSocket;
	
	public WebServer(int port)
	{
		this.setName("Totality - Web Server");
		
		try
		{
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(100);
		}
		catch (BindException e)
		{
			if (e.getMessage().equals("Address already in use (Bind failed)"))
			{
				System.err.println("[Totality Server] Port already in use, perhaps another instance of Totality is already running.");
			}
			else
			{
				System.err.println("[Totality Server] Error administrator permissions needed to run Totality on port 80. Read the FAQs for more info.");
			}
			e.printStackTrace();
			System.exit(1);
		}
		catch (IOException e)
		{
			System.out.println("[Totality Server] Error starting web server");
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
		
		try
		{
			serverSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
