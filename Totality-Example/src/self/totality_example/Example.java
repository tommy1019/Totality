package self.totality_example;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JPanel;

import self.totality.Totality;
import self.totality.webSocketServer.PacketProcessor;
import self.totality.webSocketServer.PacketProcessor.ControllerElementProcessor.Listener;
import self.totality.webSocketServer.controller.Button;
import self.totality.webSocketServer.controller.Button.DataClass;
import self.totality.webSocketServer.controller.GameController;
import self.totality.webSocketServer.controller.Image;
import self.totality.webSocketServer.controller.Joystick;
import self.totality.webSocketServer.controller.Text;
import self.totality.webSocketServer.controller.TextInput;
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

		Totality.instance.setWebPort(8080);
		Totality.instance.start();
		Totality.instance.startMulticastServer("e");
	}

	ArrayList<User> userList = new ArrayList<>();
	ArrayList<Bullet> bulletList = new ArrayList<>();
	HashMap<UUID, User> userMap = new HashMap<>();

	ArrayList<UUID> usersToRemove = new ArrayList<>();

	GameController newController;

	public Example()
	{
		GameController defaultController = new GameController();

		defaultController.addControllerElement(new TextInput("text", 0.5f, 0.2f, 0.3f, 0.1f));
		defaultController.addControllerElement(new Button("playButton", 0.5f, 0.7f, 0.3f, 0.3f));
		defaultController.addControllerElement(new Text("vText", 0.5f, .1f, "Enter Name and Press Button", 24));
		defaultController.addControllerElement(new Image("testImg", 0.5f, 0.43f, 0.2f, 0.2f, "resources/test.png"));

		Totality.instance.setDefaultController(defaultController);

		newController = new GameController();
		newController.addControllerElement(new Joystick("moveJoystick", 0.2f, 0.3f, 0.4f, 0.4f));
		newController.addControllerElement(new Joystick("aimJoystick", 0.8f, 0.3f, 0.4f, 0.4f));

		Totality.instance.addConnectListener(new ConnectListener()
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

		Totality.instance.addDisconnectListener(new DisconnectListener()
		{
			@Override
			public void onDisconnect(UUID uuid)
			{
				System.out.println(uuid + " disconnected");
				usersToRemove.add(uuid);
			}
		});

		Totality.addDataListener(Joystick.TYPE, new Listener<Joystick.DataClass>()
		{
			@Override
			public void onData(UUID uuid, Joystick.DataClass data)
			{
				User u = userMap.get(uuid);

				switch (data.id)
				{
					case "moveJoystick":
						u.xVel = data.xVal;
						u.yVel = data.yVal;
						u.speed = data.force / 100;
						break;
					case "aimJoystick":
						u.firing = (data.xVal == 0 && data.yVal == 0) ? false : true;
						u.angle = Math.atan2(-data.yVal, data.xVal);
						break;
				}
			}
		});

		Totality.addDataListener(Button.TYPE, new Listener<Button.DataClass>()
		{
			@Override
			public void onData(UUID uuid, DataClass data)
			{
				if (data.id.equals("playButton"))
				{
					Totality.instance.sendControllerToPlayer(uuid, newController);
				}
			}
		});

		PacketProcessor.registerListener(TextInput.TYPE, new Listener<TextInput.DataClass>()
		{
			@Override
			public void onData(UUID uuid, TextInput.DataClass data)
			{
				User u = userMap.get(uuid);
				u.name = data.text;
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
				u.xPos += u.xVel * u.speed;
				u.yPos += u.yVel * u.speed;

				// Keeps the players roughly contained within the window
				if (u.xPos <= 0)
				{
					u.xPos = 0;
				}
				else if (u.xPos >= this.getWidth() - u.width)
				{
					u.xPos = this.getWidth() - u.width;
				}
				if (u.yPos - u.height - u.height <= 0)
				{
					u.yPos = u.height * 2;
				}
				else if (u.yPos >= this.getHeight())
				{
					u.yPos = this.getHeight();
				}

				//Shoot bullets
				if ((u.firing) && u.timeSinceLastShot >= User.FIRE_RATE)
				{
					bulletList.add(new Bullet(u.xPos + u.width / 2, u.yPos - u.height / 2, u.angle, u));
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