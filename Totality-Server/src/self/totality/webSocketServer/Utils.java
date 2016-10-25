package self.totality.webSocketServer;

public class Utils
{
	public static String toBinary(byte b)
	{
		return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
	}
}
