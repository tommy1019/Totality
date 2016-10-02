package self.trek_example;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class User
{
	static Random random = new Random();

	boolean pressed = false;

	int width = 20;
	int height = 20;

	double xPos = 400;
	double yPos = 300;

	double xVel = 0;
	double yVel = 0;

	double angle = 0;

	Color color;

	public User()
	{
		color = new Color(random.nextInt(255), random.nextInt(128), random.nextInt(255));
	}

	public void draw(Graphics g)
	{
		//Draws the player in the appropriate color
		if (pressed)
			g.setColor(Color.green);
		else
			g.setColor(color);

		g.fillOval((int) xPos, Example.windowHeight - (int) yPos, width, height);
		g.fillOval((int) xPos + (int)(width / 2 * Math.cos(angle)), Example.windowHeight - (int) yPos + (int)(height / 2 * Math.sin(angle)), 
				width / 2, height / 2);
	}
}
