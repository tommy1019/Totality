package self.totality_example;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Bullet
{
	static final double SPEED = 1;
	
	static Random random = new Random();

	boolean pressed = false;

	int width = 5;
	int height = 5;

	double xPos = -10;
	double yPos = -10;

	double xVel = 0;
	double yVel = 0;
	
	double angle;

	User source;
	
	Color color;

	public Bullet(double x, double y, double angle, User source)
	{
		xPos = x;
		yPos = y;
		this.angle = angle;
		
		xVel = SPEED * Math.cos(angle);
		yVel = -SPEED * Math.sin(angle);
		
		this.source = source;
	}

	public void draw(Graphics g)
	{
		//Draws the player in the appropriate color
		g.setColor(Color.GRAY.darker());
		g.fillOval((int) xPos, Example.windowHeight - (int) yPos, width, height);
	}
}
