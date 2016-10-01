package self.trek;

//Joystick emulates the behavior of an analog joystick on a controller
//Joystick has an xVal and yVal that represent its current position

public class Joystick extends ControllerElement
{
	//TODO: implement these later
	private boolean xInvert = false;
	private boolean yInvert = false;
	
	//These are floats ranging from -1 to 1 that represent the coordinates of the joystick
	private double xVal = 0;
	private double yVal = 0;
	
	private double sensitivity = 0.05;
	
	
	public void update()
	{
		double oldXVal = xVal;
		double oldYVal = yVal;
		
		//TODO: implement this
		//Update xVal and yVal to reflect the position on the player's phone
		//xVal = newXVal;
		//yVal = newYVal;
		
		
		//If the x or y axes are inverted, change the values accordingly
		if(xInvert)
		{
			xVal = -xVal;
		}
		
		if(yInvert)
		{
			yVal = -yVal;
		}
		
		//If the joystick value has been changed, alert the GameController
		if(Math.abs(xVal - oldXVal) >= sensitivity || yVal != oldYVal)
		{
			stateChanged();
		}
	}
	
	//Returns a value between -1 and 1 that reflects the x position of the joystick
	public double getXVal()
	{
		return xVal;
	}
	
	//Returns a value between -1 and 1 that reflects the y position of the joystick
	public double getYVal()
	{
		return yVal;
	}
	
	//Toggles whether or not the x axis is inverted
	public void invertX()
	{
		if(xInvert)
		{
			xInvert = false;
		}
		else
		{
			xInvert = true;
		}
	}
	
	//Toggles whether or not the y axis is inverted
	public void invertY()
	{
		if(xInvert)
		{
			yInvert = false;
		}
		else
		{
			yInvert = true;
		}
	}
	
	//Returns true if the x axis is inverted
	public boolean isXInverted()
	{
		return xInvert;
	}
	
	//Returns true if the y axis is inverted
	public boolean isYInverted()
	{
		return yInvert;
	}
}
