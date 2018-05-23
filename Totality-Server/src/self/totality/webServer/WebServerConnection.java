package self.totality.webServer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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

import javax.imageio.ImageIO;

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

			BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream()));

			if (!requestParts[0].equals("GET"))
			{
				out.write("HTTP/1.1 400 Bad Request\r\n");
				out.write("\r\n");
				out.flush();
				return;
			}

			String requestedFile = requestParts[1];
			if (requestedFile.contains("?"))
				requestedFile = requestParts[1].substring(0, requestParts[1].indexOf('?'));

			switch (requestedFile)
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
		String file = path;
		String[] args = {};
		if (path.contains("?"))
		{
			file = path.substring(0, path.indexOf('?'));
			args = path.substring(path.indexOf('?') + 1).split("&");
		}
		String extension = file.substring(file.lastIndexOf('.') + 1);

		// For debug purposes
		// System.out.println("[Totality server] Serving file: " + CONTENT_DIRECTORY + path);

		InputStream in;

		// Try to get an input stream of the file
		in = getClass().getResourceAsStream("/" + CONTENT_DIRECTORY + file);

		if (in == null)
		{
			File fallbackFile = new File(file.substring(1));

			if (!fallbackFile.exists())
			{
				// Give an error if we cannot find the file
				System.err.println("[Totality server] Could not find file: " + file);

				out.write("HTTP/1.1 404 Not Found\r\n");
				out.write("\r\n");
				out.flush();
				return;
			}

			in = new FileInputStream(fallbackFile);
		}

		boolean normalFile = true;

		ByteArrayOutputStream dataBuffer = new ByteArrayOutputStream();

		boolean colorFilter = false;
		int color = 0;
		float alpha = 0.5f;

		// Process arguments
		for (String s : args)
		{
			String[] parts = s.split("=");
			switch (parts[0])
			{
				case "color":
					colorFilter = true;
					color = Integer.parseInt(parts[1]);
					break;
				case "alpha":
					alpha = Float.parseFloat(parts[1]);
					break;
			}
		}

		if (colorFilter)
		{
			try
			{
				normalFile = false;

				BufferedImage b = ImageIO.read(in);

				Graphics2D g = b.createGraphics();
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
				g.setColor(new Color(color));
				g.fillRect(0, 0, b.getWidth(), b.getHeight());

				ImageIO.write(b, "PNG", dataBuffer);
			}
			catch (IOException e)
			{
				normalFile = true;
				e.printStackTrace();
			}
		}

		if (normalFile)
		{
			// Read inputstream into a byte[]

			int numBytesRead;
			byte[] data = new byte[16384];

			while ((numBytesRead = in.read(data, 0, data.length)) != -1)
				dataBuffer.write(data, 0, numBytesRead);
		}

		dataBuffer.flush();
		byte[] fileData = dataBuffer.toByteArray();

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
