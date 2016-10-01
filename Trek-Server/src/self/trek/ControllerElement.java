package self.trek;

//A ControllerElement is the superclass for any button, joystick, etc on a GameController

public abstract class ControllerElement
{
	//The inputListener is alerted if the state is changed
	private InputListener inputListener;
	
	public void registerInputListener(InputListener listener)
	{
		inputListener = listener;
	}
	
	public void update()
	{
		
	}
	
	public void stateChanged()
	{
		inputListener.stateChanged();
	}
}
