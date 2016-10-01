package self.trek;

public abstract class ControllerElement
{
	//The inputListener is alerted if the state is changed
	InputListener inputListener;
	
	public void registerInputListener(InputListener listener)
	{
		inputListener = listener;
	}
	
	public void update()
	{
		
	}
}
