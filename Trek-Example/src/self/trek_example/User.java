package self.trek_example;

import java.awt.Color;
import java.util.Random;

public class User
{
	static Random random = new Random();
	
	boolean pressed = false;
	
	int width = 20;
	int height = 20;
	
	double xVel = 0;
	double yVel = 0;
	
	double xPos = 400;
	double yPos = 300;
	
	Color color;
	
	public User()
	{
		color = new Color(random.nextInt(255), random.nextInt(128), random.nextInt(255));
	}
}
