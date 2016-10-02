package self.totality_example;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Bullet
{
	static final double SPEED = 5;
	
	static Random random = new Random();

	boolean pressed = false;

	int width = 5;
	int height = 5;

	double xPos = -10;
	double yPos = -10;

	double xVel = 0;
	double yVel = 0;
	
	double angle;

	Color color;

	public Bullet(double angle)
	{
		this.angle = angle;
		
		xVel = SPEED * Math.cos(angle);
		yVel = SPEED * Math.sin(angle);
	}

	public void draw(Graphics g)
	{
		//Draws the player in the appropriate color
		g.setColor(Color.GRAY.darker());
		g.fillOval((int) xPos, Example.windowHeight - (int) yPos, width, height);
	}
}
