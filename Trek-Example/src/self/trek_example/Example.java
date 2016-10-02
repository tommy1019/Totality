package self.trek_example;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JPanel;

import self.trek.Button;
import self.trek.ConnectListener;
import self.trek.ControllerElement;
import self.trek.ControllerElementType;
import self.trek.DataListener;
import self.trek.Joystick;
import self.trek.TrekServer;

public class Example extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	public static void main(String args[])
	{
		JFrame frame = new JFrame("test!");
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setContentPane(new Example());
		frame.setVisible(true);
		
		TrekServer.instance.start();
	}
	
	ArrayList<User> userList = new ArrayList<>();
	HashMap<UUID, User> userMap = new HashMap<>();
	
	public Example()
	{
		TrekServer.instance.addDefaultControllerElement(ControllerElementType.BUTTON, "button1");
		TrekServer.instance.addDefaultControllerElement(ControllerElementType.BUTTON, "button2");
		TrekServer.instance.addDefaultControllerElement(ControllerElementType.JOYSTICK, "joystick1");
		//TrekServer.instance.addDefaultControllerElement(ControllerElementType.JOYSTICK, "joystick2");
		
		TrekServer.instance.addConnectListener(new ConnectListener()
		{
			@Override
			public void onConnect(UUID uuid)
			{
				User user = new User();
				userList.add(user);
				userMap.put(uuid, user);
			}
		});
		
		TrekServer.instance.addDataListener(new DataListener()
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
			
			//Draws the player in the appropriate color
			if (u.pressed)
				g.setColor(Color.green);
			else
				g.setColor(u.color);
			
			g.fillOval((int) u.xPos, this.getHeight() - (int) u.yPos, u.width, u.height);
		}
		
		repaint();
	}
}