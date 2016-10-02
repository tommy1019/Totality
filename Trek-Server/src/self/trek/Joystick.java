package self.trek;

//Joystick emulates the behavior of an analog joystick on a controller
//Joystick has an xVal and yVal that represent its current position

public class Joystick extends ControllerElement
{
	//These are floats ranging from -1 to 1 that represent the coordinates of the joystick
	protected double xVal = 0;
	protected double yVal = 0;
		
	protected Joystick(String id, double xVal, double yVal)
	{
		super(id, ControllerElementType.JOYSTICK);
		
		this.xVal = xVal;
		this.yVal = yVal;
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
}
