package self.totality_example;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Random;

public class User
{
	static Random random = new Random();
	static Font f = Font.decode("comic sans ms");

	static final int RESPAWN_TIME = 800;
	static final int FIRE_RATE = 50;

	boolean firing = false;

	boolean alive = true;

	int width = 20;
	int height = 20;

	double xPos = Math.random() * Example.windowWidth;
	double yPos = Math.random() * Example.windowHeight;

	double xVel = 0;
	double yVel = 0;

	double speed = 0;
	double angle = 0;

	int timeSinceLastShot = 0;
	int timeSinceDeath = 0;

	String name = "";

	Color color;

	public User()
	{
		color = new Color(random.nextInt(255), random.nextInt(128), random.nextInt(255));
	}

	public void draw(Graphics g)
	{
		// Draws the player in the appropriate color
		if (firing)
			g.setColor(Color.green);
		else
			g.setColor(color);

		g.fillOval((int) xPos, Example.windowHeight - (int) yPos, width, height);
		if (firing)
			g.fillOval((int) xPos + (int) (5 * width * Math.cos(angle)), Example.windowHeight - (int) yPos + (int) (5 * height * Math.sin(angle)), width / 2, height / 2);

		g.setFont(f);
		FontMetrics fm = g.getFontMetrics(f);
		int nameWidth = fm.stringWidth(name);

		g.drawString(name, (int) xPos - nameWidth / 2 + width / 2, Example.windowHeight - 5 - (int) yPos);
	}
}
