package self.totality_example;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JPanel;

import self.totality.Button;
import self.totality.ConnectListener;
import self.totality.ControllerElement;
import self.totality.ControllerElementType;
import self.totality.DataListener;
import self.totality.Joystick;
import self.totality.TotalityServer;

public class Example extends JPanel
{
	private static final long serialVersionUID = 1L;
	public static int windowWidth = 800;
	public static int windowHeight = 600;
	
	public static void main(String args[])
	{
		JFrame frame = new JFrame("test!");
		frame.setSize(windowWidth, windowHeight);
		frame.setLocationRelativeTo(null);
		frame.setContentPane(new Example());
		frame.setVisible(true);
		
		TotalityServer.instance.start();
	}
	
	ArrayList<User> userList = new ArrayList<>();
	ArrayList<Bullet> bulletList = new ArrayList<>();
	HashMap<UUID, User> userMap = new HashMap<>();
	
	public Example()
	{
		TotalityServer.instance.addDefaultControllerElement(ControllerElementType.BUTTON, "button1");
		TotalityServer.instance.addDefaultControllerElement(ControllerElementType.BUTTON, "button2");
		TotalityServer.instance.addDefaultControllerElement(ControllerElementType.JOYSTICK, "joystick1");
		//TrekServer.instance.addDefaultControllerElement(ControllerElementType.JOYSTICK, "joystick2");
		
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
					
					if(u.xVel != 0 && u.yVel != 0)
					{
						u.angle = Math.atan2(u.yVel, u.xVel);
					}
				}
				else if (e.type == ControllerElementType.BUTTON)
				{
					Button b = (Button) e;
					
					u.pressed = b.pressed();
				}
			}
		});
	}
	
	public void paint(Graphics g)
	{
		for (User u : userList)
		{
			u.xPos += u.xVel;
			u.yPos += u.yVel;
			
			//Keeps the players roughly contained within the window
			if(u.xPos <= 0)
			{
				u.xPos = 0;
			}
			else if(u.xPos >= this.getWidth() - u.width)
			{
				u.xPos = this.getWidth() - u.width;
			}
			if(u.yPos - u.height <= 0)
			{
				u.yPos = u.height;
			}
			else if(u.yPos >= this.getHeight())
			{
				u.yPos = this.getHeight();
			}
			
			if(u.pressed && u.timeSinceLastShot <= 200)
			{
				bulletList.add(new Bullet(u.angle));
				u.timeSinceLastShot = 0;
			}
			
			u.draw(g);
		}
		
		Iterator<Bullet> itr = bulletList.iterator();
		while (itr.hasNext())
		{
			Bullet b = itr.next();
			
			b.xPos += b.xVel;
			b.yPos += b.yVel;
			
			b.draw(g);
			
			//Disposes of the bullet if it goes off screen
			if(b.xPos + b.width <= 0
					|| b.xPos >= this.getWidth()
					|| b.yPos - b.height >= this.getHeight()
					|| b.yPos <= 0)
			{
				itr.remove();
			}			
		}
		
		repaint();
	}
}