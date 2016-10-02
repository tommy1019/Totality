package self.trek_example;

import self.trek.Joystick;
import self.trek.TrekServer;

public class Example
{
	public static void main(String args[])
	{
		TrekServer.instance.defaultController.addControllerElement(new Joystick("joystick1"));
		
		TrekServer.instance.start();
	}
}