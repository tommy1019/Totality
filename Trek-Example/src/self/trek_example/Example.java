package self.trek_example;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import self.trek.Button;
import self.trek.ConnectedClient;
import self.trek.ControllerElementType;
import self.trek.TrekServer;

public class Example extends JPanel
{
	public static void main(String args[])
	{
		
		TrekServer.instance.addDefaultControllerElement(ControllerElementType.BUTTON, "button1");
		TrekServer.instance.addDefaultControllerElement(ControllerElementType.BUTTON, "button2");
		
		//JFrame frame = new JFrame("test!");
		//frame.setSize(800, 600);
		//frame.setLocationRelativeTo(null);
		//frame.setContentPane(new Example());
		//frame.setVisible(true);
		
		TrekServer.instance.start();
	}
	
	public void paint(Graphics g)
	{
		g.fillRect(20, 20, 20, 20);
		
		int x = 0;
		
		for (ConnectedClient c : TrekServer.instance.connectedClients)
		{
			x += 30;
			
			if (((Button)c.gameController.controllerElements.get("button2")).isPressed())
			{
				g.setColor(Color.green);
			}
			else
			{
				g.setColor(Color.red);
			}
			
			g.fillRect(x, 20, 20, 20);
		}
				
		repaint();
	}
}