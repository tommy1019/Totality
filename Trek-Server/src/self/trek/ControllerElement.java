package self.trek;

//A ControllerElement is the superclass for any button, joystick, etc on a GameController

public abstract class ControllerElement
{
	private boolean stateChanged = false;
	
	public void update()
	{
		
	}
	
	public void changeState()
	{
		stateChanged = true;
	}
	
	public boolean isStateChanged()
	{
		if(stateChanged)
		{
			stateChanged = false;
			return true;
		}
		else
		{
			return false;
		}
	}
}
