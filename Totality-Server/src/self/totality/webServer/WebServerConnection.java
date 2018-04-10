package self.totality.webServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import self.totality.Totality;
import self.totality.webSocketServer.WebSocketServer;

public class WebServerConnection extends Thread
{
	public static final String CONTENT_DIRECTORY = "resources";
	public static final String INDEX_PAGE = "/TotalityClient.html";

	Socket socket;

	public WebServerConnection(Socket socket)
	{
		this.socket = socket;
		
		this.setName("Totality - Web Server Connection");
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

			switch (requestParts[1])
			{
				case "/":
					handleFile(INDEX_PAGE, out);
					break;
				case "/webSocketIp":
					handleIp(out);
					break;
				case "/getController":
					handleController(out);
					break;
				default:
					handleFile(requestParts[1], out);
					break;
			}

			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void handleController(BufferedWriter out) throws IOException
	{
		String data = Totality.gson.toJson(Totality.instance.getDefaultController());

		out.write("HTTP/1.1 200 OK\r\n");
		out.write("Content-Length: " + data.length());
		out.write("Content-Type: text/plain\r\n");
		out.write("Content-Encoding: UTF-8\r\n");
		out.write("Connection: close\r\n");
		out.write("\r\n");
		out.write(data);
		out.flush();
	}

	void handleIp(BufferedWriter out) throws IOException
	{
		String ip = Totality.localIp + ":" + WebSocketServer.PORT;

		out.write("HTTP/1.1 200 OK\r\n");
		out.write("Content-Length: " + ip.length());
		out.write("Content-Type: text/html; charset=utf-8\r\n");
		out.write("Content-Encoding: identity\r\n");
		out.write("X-Content-Type-Options: nosniff\r\n");
		out.write("Connection: close\r\n");
		out.write("\r\n");
		out.write(ip);
		out.flush();
	}

	void handleFile(String path, BufferedWriter out) throws IOException
	{
		String extension = path.substring(path.lastIndexOf('.') + 1);

		// For debug purposes
		// System.out.println("[Totality server] Serving file: " + CONTENT_DIRECTORY + path);

		InputStream in;

		// Try to get an input stream of the file
		in = getClass().getResourceAsStream("/" + CONTENT_DIRECTORY + path);

		if (in == null)
		{
			File fallbackFile = new File(path.substring(1));
			
			if (!fallbackFile.exists())
			{				
				// Give an error if we cannot find the file
				System.err.println("[Totality server] Could not find file: " + path);

				out.write("HTTP/1.1 404 Not Found\r\n");
				out.write("\r\n");
				out.flush();
				return;
			}
			
			in = new FileInputStream(fallbackFile);
		}

		// Read inputstream into a byte[]
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int numBytesRead;
		byte[] data = new byte[16384];

		while ((numBytesRead = in.read(data, 0, data.length)) != -1)
		{
			buffer.write(data, 0, numBytesRead);
		}

		buffer.flush();

		byte[] fileData = buffer.toByteArray();

		// Make sure the file isn't too big to send
		if (fileData.length > Integer.MAX_VALUE)
		{
			out.write("HTTP/1.1 413 Payload Too Large\r\n");
			out.write("\r\n");
			out.flush();
			return;
		}

		out.write("HTTP/1.1 200 OK\r\n");
		out.write("Content-Length: " + fileData.length);
		out.write("Content-Type: " + WebUtils.getMimeType(extension) + "\r\n");
		out.write("Content-Encoding: UTF-8\r\n");
		out.write("Connection: close\r\n");
		out.write("\r\n");
		out.flush();

		// Write file data to the socket outputstream
		socket.getOutputStream().write(fileData);
	}
}
