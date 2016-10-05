package self.totality;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class WebUtils
{
	static HashMap<String, String> readHTTPSettings(BufferedReader in)
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
					System.out.println("Can't find: ':'");
					System.out.println(curLine);
					break;
				}

				clientHTTP.put(curLine.substring(0, colenIndex), curLine.substring(colenIndex + 2));
			}

			return clientHTTP;
		}
		catch (IOException e)
		{
			System.out.println("Error reading client HTTP settings.");
			return null;
		}
	}
}
