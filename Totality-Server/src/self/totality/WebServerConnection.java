package self.totality;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class WebServerConnection extends Thread
{
	public static final String CONTENT_DIRECTORY = "../Totality-Client/";
	public static final String INDEX_PAGE = "TotalityClient.html";
	
	Socket socket;
	
	public WebServerConnection(Socket socket)
	{
		this.socket = socket;
	}

	public void run()
	{
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String curLine = in.readLine();

			if (curLine == null)
				return;

			String[] requestParts = curLine.split(" ");

			if (!requestParts[0].equals("GET"))
			{
				//TODO:Send a bad request header
				System.out.println("Bad request");
				return;
			}

			if (requestParts[1].equals("/"))
				requestParts[1] = INDEX_PAGE;

			File f = new File(CONTENT_DIRECTORY, requestParts[1]);

			if (f.length() > Integer.MAX_VALUE)
			{
				//TODO:Send a bad request header
				System.out.println("Requested file is too big");
				return;
			}

			// HashMap<String, String> clientHTTP =
			// WebUtils.readHTTPSettings(in);

			//TODO: Content type
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			out.write("HTTP/1.1 200 OK\r\n");
			out.write("Content-Size: " + f.length());
			// out.write("Content-Type: text/html; charset=UTF-8\r\n");
			out.write("Content-Encoding: UTF-8\r\n");
			out.write("Connection: close\r\n");
			out.write("\r\n");

			out.flush();

			DataOutputStream sOut = new DataOutputStream(socket.getOutputStream());
			DataInputStream fIn = new DataInputStream(new FileInputStream(f));

			byte[] buffer = new byte[(int) f.length()];
			fIn.readFully(buffer);
			fIn.close();

			sOut.write(buffer);
			sOut.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
