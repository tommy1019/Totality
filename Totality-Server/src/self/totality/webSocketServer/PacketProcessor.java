package self.totality.webSocketServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import self.totality.webSocketServer.PacketProcessor.ControllerElementProcessor.Listener;
import self.totality.webSocketServer.controller.Button;
import self.totality.webSocketServer.controller.ControllerElement;
import self.totality.webSocketServer.controller.ControllerElement.DataClass;
import self.totality.webSocketServer.controller.DPad;
import self.totality.webSocketServer.controller.Joystick;
import self.totality.webSocketServer.controller.TextInput;

public class PacketProcessor
{
	static HashMap<String, ControllerElementProcessor<?, ?>> typeMap = new HashMap<>();

	public static abstract class ControllerElementProcessor<Element extends ControllerElement, Data extends DataClass>
	{
		public abstract Data process(String packet);

		public static abstract class Listener<Data extends DataClass>
		{
			public abstract void onData(UUID uuid, Data data);
		}

		public ArrayList<Listener<Data>> listeners = new ArrayList<>();

		@SuppressWarnings("unchecked")
		public void addListener(Listener<? extends ControllerElement.DataClass> l)
		{
			listeners.add((Listener<Data>) l);
		}

		public void onData(UUID uuid, String packet)
		{
			Data data = process(packet);
			for (Listener<Data> l : listeners)
				l.onData(uuid, data);
		}
	}

	static
	{
		registerControllerElement("JOYSTICK", new ControllerElementProcessor<Joystick, Joystick.DataClass>()
		{
			@Override
			public Joystick.DataClass process(String packet)
			{
				String[] msgParts = packet.split(":");
				return new Joystick.DataClass(msgParts[1], Double.parseDouble(msgParts[3]), Double.parseDouble(msgParts[4]), Double.parseDouble(msgParts[5]));
			}
		});

		registerControllerElement("BUTTON", new ControllerElementProcessor<Button, Button.DataClass>()
		{
			@Override
			public Button.DataClass process(String packet)
			{
				String[] msgParts = packet.split(":");
				return new Button.DataClass(msgParts[1], msgParts[3].equals("true"));
			}
		});

		registerControllerElement("TEXTINPUT", new ControllerElementProcessor<TextInput, TextInput.DataClass>()
		{
			@Override
			public TextInput.DataClass process(String packet)
			{
				String[] msgParts = packet.split(":");

				if (msgParts.length > 3)
				{
					return new TextInput.DataClass(msgParts[1], msgParts[3]);
				}
				else
				{
					return new TextInput.DataClass(msgParts[1], "");
				}
			}
		});
		
		registerControllerElement("DPAD", new ControllerElementProcessor<DPad, DPad.DataClass>()
		{
			@Override
			public DPad.DataClass process(String packet)
			{
				String[] msgParts = packet.split(":");
				return new DPad.DataClass(msgParts[1], Boolean.parseBoolean(msgParts[3]), 
						  Boolean.parseBoolean(msgParts[4]),
						  Boolean.parseBoolean(msgParts[5]),
						  Boolean.parseBoolean(msgParts[6]));
			}
		});
	}

	public static void processPacket(String packetData, UUID userUUID)
	{
		ControllerElementProcessor<?, ?> processor = typeMap.get(packetData.substring(0, packetData.indexOf(':')));
		if (processor != null)
		{
			processor.onData(userUUID, packetData);
		}
		else
		{
			System.out.println("Unsupported packet:");
			System.out.println(packetData);
		}
	}

	public static void registerControllerElement(String idString, ControllerElementProcessor<? extends ControllerElement, ? extends DataClass> processor)
	{
		typeMap.put(idString, processor);
	}

	public static void registerListener(String idString, Listener<? extends ControllerElement.DataClass> l)
	{
		typeMap.get(idString).addListener(l);
	}
}