package self.trek;

public class Joystick extends ControllerElement
{
	//TODO: implement these later
	private boolean xInvert;
	private boolean yInvert;
	
	private int xVal = 0;
	private int yVal = 0;
	
	private int deadzone = 5;
	
	public void update()
	{
		int oldXVal = xVal;
		int oldYVal = yVal;
		
		//TODO: implement this
		//The new x and y vals will be supplied by the javascript
		//xVal = newXVal;
		//yVal = newYVal;
		
		if(Math.abs(xVal) < deadzone)
		{
			xVal = 0;
		}
		
		if(Math.abs(yVal) < deadzone)
		{
			yVal = 0;
		}
		
		//If the joystick value has been changed, alert the server
		if(xVal != oldXVal || yVal != oldYVal)
		{
			stateChanged();
		}
	}
	
	public void stateChanged() 
	{
		inputListener.stateChanged();
	}
}
