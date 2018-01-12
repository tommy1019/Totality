package self.totality_example;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JPanel;

import self.totality.TotalityServer;
import self.totality.webSocketServer.controller.Button;
import self.totality.webSocketServer.controller.ControllerElement;
import self.totality.webSocketServer.controller.ControllerElementType;
import self.totality.webSocketServer.controller.Joystick;
import self.totality.webSocketServer.listener.ConnectListener;
import self.totality.webSocketServer.listener.DataListener;
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

		TotalityServer.instance.start();
	}

	ArrayList<User> userList = new ArrayList<>();
	ArrayList<Bullet> bulletList = new ArrayList<>();
	HashMap<UUID, User> userMap = new HashMap<>();

	ArrayList<UUID> usersToRemove = new ArrayList<>();

	public Example()
	{
		TotalityServer.instance.addControllerElement("button1", ControllerElementType.BUTTON, 0.6f, 0.5f, 0.1f, 0.1f);
		TotalityServer.instance.addControllerElement("button2", ControllerElementType.BUTTON, 0.8f, 0.4f, 0.1f, 0.1f);
		TotalityServer.instance.addControllerElement("joystick1", ControllerElementType.JOYSTICK, 0.1f, 0.3f, 0.4f, 0.4f);

		TotalityServer.instance.addConnectListener(new ConnectListener()
		{
			@Override
			public void onConnect(UUID uuid)
			{
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
				usersToRemove.add(uuid);
			}
		});

		TotalityServer.instance.addDataListener(new DataListener()
		{
			@Override
			public void onDataUpdate(UUID uuid, ControllerElement e)
			{
				User u = userMap.get(uuid);

				if (e.type == ControllerElementType.JOYSTICK)
				{
					Joystick j = (Joystick) e;

					u.xVel = j.getXVal();
					u.yVel = j.getYVal();
					u.speed = j.getForce();

					if (u.xVel != 0 && u.yVel != 0)
					{
						u.angle = Math.atan2(-u.yVel, u.xVel);
					}
				}
				else if (e.type == ControllerElementType.BUTTON)
				{
					Button b = (Button) e;

					if (b.id.equals("button1"))
					{
						u.pressed1 = b.pressed();
					}
					else if (b.id.equals("button2"))
					{
						u.pressed2 = b.pressed();
					}
				}
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