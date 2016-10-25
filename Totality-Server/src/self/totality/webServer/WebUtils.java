package self.totality.webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class WebUtils
{
	public static HashMap<String, String> readHTTPSettings(BufferedReader in)
	{
		HashMap<String, String> clientHTTP = new HashMap<>();
		String curLine;

		try
		{
			while ((curLine = in.readLine()) != null)
			{
				if (curLine.equals(""))
					break;

				int colenIndex = curLine.indexOf(':');

				if (colenIndex == -1)
				{
					break;
				}

				clientHTTP.put(curLine.substring(0, colenIndex), curLine.substring(colenIndex + 2));
			}

			return clientHTTP;
		}
		catch (IOException e)
		{
			return null;
		}
	}
	
	static String getMimeType(String extension)
	{
		switch(extension)
		{
			case "html":
				return "text/html";
			case "css":
				return "text/css";
			case "js":
				return "text/javascript";
				
			case "txt":
				return "text/plain";
				
			case "png":
				return "image/png";
			case "gif":
				return "image/gif";
			case "jpeg":
				return "image/jpeg";
			case "bmp":
				return "image/bmp";
		}
		
		return "application/unknown";
	}
}
