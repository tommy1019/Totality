package self.trek;

public class Button extends ControllerElement
{
	private boolean isPressed;
	
	public void update()
	{
		boolean previousState = isPressed;
		
		//TODO: implement this
		//The value will be supplied by the javascript
		//isPressed = 
		
		if(previousState != isPressed)
		{
			changeState();
		}
	}
}
