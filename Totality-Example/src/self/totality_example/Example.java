package self.totality_example;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JPanel;

import self.totality.TotalityServer;
import self.totality.webSocketServer.PacketProcessor;
import self.totality.webSocketServer.PacketProcessor.ControllerElementProcessor.Listener;
import self.totality.webSocketServer.controller.ButtonElement;
import self.totality.webSocketServer.controller.ButtonElement.DataClass;
import self.totality.webSocketServer.controller.GameController;
import self.totality.webSocketServer.controller.JoystickElement;
import self.totality.webSocketServer.controller.TextElement;
import self.totality.webSocketServer.controller.TextInputElement;
import self.totality.webSocketServer.listener.ConnectListener;
import self.totality.webSocketServer.listener.DisconnectListener;

public class Example extends JPanel
{
	private static final long serialVersionUID = 1L;
	public static int windowWidth = 1400;
	public static int windowHeight = 800;

	public static void main(String args[])
	{
		System.setProperty("sun.java2d.opengl", "True");

		JFrame frame = new JFrame("Totality Example");
		frame.setSize(windowWidth, windowHeight);
		frame.setLocationRelativeTo(null);
		frame.setContentPane(new Example());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		TotalityServer.instance.setWebPort(8080);
		TotalityServer.instance.start();
		TotalityServer.instance.startMulticastServer("e");
	}

	ArrayList<User> userList = new ArrayList<>();
	ArrayList<Bullet> bulletList = new ArrayList<>();
	HashMap<UUID, User> userMap = new HashMap<>();

	ArrayList<UUID> usersToRemove = new ArrayList<>();

	GameController newController;

	public Example()
	{
		GameController defaultController = new GameController();
		defaultController.addControllerElement(new TextInputElement("text", 0.5f, 0.2f, 0.3f, 0.1f));
		defaultController.addControllerElement(new ButtonElement("playButton", 0.5f, 0.7f, 0.3f, 0.3f));
		defaultController.addControllerElement(new TextElement("vText", 0.5f, .1f, "Enter Name and Press Button", 24));
		TotalityServer.instance.setDefaultController(defaultController);

		newController = new GameController();
		newController.addControllerElement(new ButtonElement("button1", 0.6f, 0.6f, 0.1f, 0.1f));
		newController.addControllerElement(new ButtonElement("button2", 0.8f, 0.5f, 0.1f, 0.1f));
		newController.addControllerElement(new JoystickElement("joystick1", 0.2f, 0.3f, 0.4f, 0.4f));

		TotalityServer.instance.addConnectListener(new ConnectListener()
		{
			@Override
			public void onConnect(UUID uuid)
			{
				System.out.println(uuid + " connected");
				User user = new User();
				userList.add(user);
				userMap.put(uuid, user);
			}
		});

		TotalityServer.instance.addDisconnectListener(new DisconnectListener()
		{
			@Override
			public void onDisconnect(UUID uuid)
			{
				System.out.println(uuid + " disconnected");
				usersToRemove.add(uuid);
			}
		});

		PacketProcessor.registerListener("JOYSTICK", new Listener<JoystickElement.DataClass>()
		{
			@Override
			public void onData(UUID uuid, JoystickElement.DataClass data)
			{
				User u = userMap.get(uuid);

				u.xVel = data.xVal;
				u.yVel = data.yVal;
				u.speed = data.force;

				if (u.xVel != 0 && u.yVel != 0)
				{
					u.angle = Math.atan2(-u.yVel, u.xVel);
				}
			}
		});

		PacketProcessor.registerListener("BUTTON", new Listener<ButtonElement.DataClass>()
		{

			@Override
			public void onData(UUID uuid, DataClass data)
			{
				User u = userMap.get(uuid);

				if (data.id.equals("button1"))
				{
					u.pressed1 = data.pressed;
				}
				else if (data.id.equals("button2"))
				{
					u.pressed2 = data.pressed;
				}
				else if (data.id.equals("playButton"))
				{
					TotalityServer.instance.sendControllerToPlayer(uuid, newController);
				}
			}
		});

		PacketProcessor.registerListener("TEXTINPUT", new Listener<TextInputElement.DataClass>()
		{
			@Override
			public void onData(UUID uuid, TextInputElement.DataClass data)
			{
				System.out.println(data.text);

			}
		});
	}

	public void paint(Graphics g)
	{
		g.clearRect(0, 0, windowWidth, windowHeight);
		for (User u : userList)
		{
			if (u.alive)
			{
				u.xPos += Math.cos(u.angle);
				u.yPos += Math.sin(u.angle);
				//u.xPos += u.xVel * u.speed;
				//u.yPos += u.yVel * u.speed;

				// Keeps the players roughly contained within the window
				if (u.xPos <= 0)
				{
					u.xPos = 0;
				}
				else if (u.xPos >= this.getWidth() - u.width)
				{
					u.xPos = this.getWidth() - u.width;
				}
				if (u.yPos - u.height <= 0)
				{
					u.yPos = u.height;
				}
				else if (u.yPos >= this.getHeight())
				{
					u.yPos = this.getHeight();
				}

				// Shoot a bullet either forwards or backwards, depending on
				// which button was pressed
				if ((u.pressed1 || u.pressed2) && u.timeSinceLastShot >= User.FIRE_RATE)
				{
					if (u.pressed1)
					{
						bulletList.add(new Bullet(u.xPos + u.width / 2, u.yPos - u.height / 2, u.angle, u));
					}
					else if (u.pressed2)
					{
						bulletList.add(new Bullet(u.xPos + u.width / 2, u.yPos - u.height / 2, u.angle + Math.PI, u));
					}
					u.timeSinceLastShot = 0;
				}

				u.timeSinceLastShot++;

				u.draw(g);
			}
			else if (u.timeSinceDeath >= User.RESPAWN_TIME)
			{
				u.alive = true;
				u.xPos = windowWidth / 2;
				u.yPos = windowHeight / 2;
				u.timeSinceDeath = 0;
			}
			else
			{
				u.timeSinceDeath++;
			}
		}

		for (int j = 0; j < bulletList.size(); j++)
		{
			Bullet b = bulletList.get(j);

			b.xPos += b.xVel;
			b.yPos += b.yVel;

			b.draw(g);

			// Disposes of the bullet if it goes off screen
			if (b.xPos + b.width <= 0 || b.xPos >= this.getWidth() || b.yPos - b.height >= this.getHeight() || b.yPos <= 0)
			{
				bulletList.remove(j);
			}
			else
			{
				for (int i = 0; i < userList.size(); i++)
				{
					User u = userList.get(i);

					if (u.alive && !b.source.equals(u))
					{
						if (Math.sqrt(Math.pow(u.xPos - b.xPos, 2) + Math.pow(u.yPos - b.yPos, 2)) < u.width + b.width)
						{
							u.alive = false;
							if (bulletList.size() > 0)
								bulletList.remove(j);
						}
					}
				}
			}
		}

		for (UUID uuid : usersToRemove)
		{
			User u = userMap.get(uuid);
			userList.remove(u);
			userMap.remove(uuid);
		}

		try
		{
			Thread.sleep(1);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		repaint();
	}
}