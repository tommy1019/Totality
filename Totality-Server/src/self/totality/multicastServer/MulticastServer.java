package self.totality.multicastServer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

public class MulticastServer extends Thread
{
	MulticastSocket socket;

	String domain;

	public MulticastServer(String domain)
	{
		this.domain = domain;
		
		this.setName("Totality - Multicast Server");

		if (domain.equals("local"))
		{
			throw new IllegalArgumentException("Domain name must not be local");
		}

		System.out.println("[Totality server] Started multicast server.");
		try
		{
			socket = new MulticastSocket(5353);
			socket.setInterface(InetAddress.getLocalHost());
			socket.joinGroup(InetAddress.getByName("224.0.0.251"));
			socket.setTimeToLive(10);
			socket.setSoTimeout(100);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		while (!this.isInterrupted())
		{
			byte[] data = new byte[65507];
			DatagramPacket packet = new DatagramPacket(data, 65507);

			try
			{
				socket.receive(packet);

				DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));

				short identification = in.readShort();
				if (identification != 0)
					continue;

				byte flags = in.readByte();

				boolean query = (flags & 0b10000000) == 0;
				if (!query)
					continue;

				int opcode = (flags >> 3) & 0b1111;
				if (opcode != 0)
					continue;

				flags = in.readByte();
				if (flags != 0)
					continue;

				short numQuestions = in.readShort();
				short numAnswers = in.readShort();
				short numAuthority = in.readShort();
				short numAdditional = in.readShort();
				
				if (numQuestions == 0)
					continue;

				if (numAnswers != 0 || numAuthority != 0 || numAdditional != 0)
					continue;

				String request = "";

				for (int i = 0; i < 63; i++)
				{
					byte len = in.readByte();

					if (len == 0)
						break;

					if (len < 0 || len > 63)
						break;

					byte[] nameData = new byte[len];
					in.read(nameData);

					request += new String(nameData) + ".";
				}

				if (!request.equals((domain + ".local.")))
					continue;

				// Write packet
				ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(byteOut);

				out.writeShort(0);

				out.write(0b10000100); // Flags
				out.write(0); // Flags

				out.writeShort(0); // Num Questions
				out.writeShort(1); // Num Answers
				out.writeShort(0); // Num AA
				out.writeShort(0); // Num additional

				out.write(domain.length());
				out.write(domain.getBytes());
				out.write("local".length());
				out.write("local".getBytes());
				out.write(0);
				out.writeShort(1); // Type
				out.writeShort(1); // Class
				out.writeInt(299); // TTL

				out.writeShort(4); // RDataLength
				out.write(InetAddress.getLocalHost().getAddress()); //IP

				InetAddress addr = InetAddress.getByName("224.0.0.251");

				DatagramPacket reponse = new DatagramPacket(byteOut.toByteArray(), byteOut.size(), addr, 5353);
				socket.send(reponse);
			}
			catch (SocketTimeoutException e)
			{

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
		
		socket.close();
	}
}
