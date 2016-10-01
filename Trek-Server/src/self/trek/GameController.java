package self.trek;

import java.util.ArrayList;

//The game controller is stored server-side and keeps
//track of 

public abstract class GameController implements InputListener
{
	private ArrayList<ControllerElement> controllerElements = new ArrayList<ControllerElement>();
	
	private ArrayList<InputListener> listeners = new ArrayList<InputListener>();
	
	public void addListener(InputListener newListener)
	{
		listeners.add(newListener);
	}
	
	public void stateChanged()
	{
		System.out.println("The state was changed!");
	}
}
