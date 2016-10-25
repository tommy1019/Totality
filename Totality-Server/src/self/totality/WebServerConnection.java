package self.totality;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.Files;

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
			
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			if (!requestParts[0].equals("GET"))
			{
				out.write("HTTP/1.1 400 Bad Request\r\n");
				out.write("\r\n");
				out.flush();
				return;
			}
			
			System.out.println(requestParts[1]);
			
			switch (requestParts[1])
			{
				case "/":
					handelFile(INDEX_PAGE, out);
					break;
				case "/ip.txt":
					handelIp(out);
					break;
				default:
					handelFile(requestParts[1], out);
					break;
			}
			
			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	void handelIp(BufferedWriter out) throws IOException
	{
		out.write("HTTP/1.1 200 OK\r\n");
		out.write("Content-Length: " + TotalityServer.instance.localIp.length());
		out.write("Content-Type: text/plain\r\n");
		out.write("Content-Encoding: UTF-8\r\n");
		out.write("Connection: close\r\n");
		out.write("\r\n");
		out.write(TotalityServer.instance.localIp);
		out.flush();
	}
	
	void handelFile(String path, BufferedWriter out) throws IOException
	{
		File f = new File(CONTENT_DIRECTORY, path);
		String extension = path.substring(path.lastIndexOf('.') + 1);
		
		if (!f.exists())
		{
			out.write("HTTP/1.1 404 Not Found\r\n");
			out.write("\r\n");
			out.flush();
			return;
		}
		
		if (f.length() > Integer.MAX_VALUE)
		{
			out.write("HTTP/1.1 413 Payload Too Large\r\n");
			out.write("\r\n");
			out.flush();
			return;
		}
		
		// HashMap<String, String> clientHTTP =
		// WebUtils.readHTTPSettings(in);
		
		out.write("HTTP/1.1 200 OK\r\n");
		out.write("Content-Length: " + f.length());
		out.write("Content-Type: " + WebUtils.getMimeType(extension) + "\r\n");
		out.write("Content-Encoding: UTF-8\r\n");
		out.write("Connection: close\r\n");
		out.write("\r\n");
		out.flush();
		
		Files.copy(f.toPath(), socket.getOutputStream());
	}
}
