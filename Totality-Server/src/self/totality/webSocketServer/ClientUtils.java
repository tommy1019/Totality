package self.totality.webSocketServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

public class ClientUtils
{
	static final String MAGIC_WEBSOCKET_STRING = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

	static Tuple<byte[], Integer> readMessageFully(DataInputStream in) throws Exception
	{
		ArrayList<byte[]> finalData = new ArrayList<>();
		boolean fin = true;

		int opcode = -1;

		do
		{
			byte byte1 = in.readByte();
			byte byte2 = in.readByte();

			fin = (byte1 < 0);
			if (opcode == -1)
				opcode = (byte) (byte1 & 0b1111);

			boolean mask = (byte2 < 0);
			if (!mask)
			{
				throw new RuntimeException("Error: Client must mask data as per spec");
			}
			byte[] maskBytes = new byte[4];

			int payloadLength = Byte.toUnsignedInt((byte) (byte2 & 0b01111111));

			if (payloadLength > 125)
			{
				if (payloadLength == 126)
				{

				}
				else
				{

				}
				throw new RuntimeException("Large packet sizes not supported.");
			}

			if (mask)
			{
				in.read(maskBytes);
			}

			byte[] data = new byte[payloadLength];
			in.read(data);

			if (mask)
			{
				for (int i = 0; i < payloadLength; i++)
					data[i] = (byte) (data[i] ^ maskBytes[i % 4]);
			}

			finalData.add(data);
		}
		while (!fin);

		if (finalData.size() == 1)
			return new Tuple<byte[], Integer>(finalData.get(0), opcode);

		int size = 0;
		for (byte[] b : finalData)
			size += b.length;
		byte[] bytes = new byte[size];

		int index = 0;

		for (byte[] b : finalData)
		{
			System.arraycopy(b, 0, bytes, index, b.length);
			index += b.length;
		}

		return new Tuple<byte[], Integer>(bytes, opcode);
	}

	static void sendMessage(DataOutputStream out, int opcode, byte[] data) throws Exception
	{
		out.writeByte(0b10000000 + opcode);

		if (data.length < 125)
		{
			out.writeByte(data.length);
			out.write(data);
		}
		else if (data.length < 65536)
		{
			out.writeByte(126);
			out.writeChar(data.length);
			out.write(data);
		}
		else
			throw new RuntimeException("Packet size not supported");
	}

	static String getWebsocketKey(String client)
	{
		try
		{
			return Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((client + MAGIC_WEBSOCKET_STRING).getBytes()));
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
}
