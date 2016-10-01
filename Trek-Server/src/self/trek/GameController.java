package self.trek;

import java.util.ArrayList;

public class GameController implements InputListener
{
	private ArrayList<ControllerElement> controllerElements = new ArrayList<ControllerElement>();
	
	public void addControllerElement(ControllerElement ce)
	{
		controllerElements.add(ce);
	}
	
	public void stateChanged()
	{
		System.out.println("The state was changed!");
	}
}
