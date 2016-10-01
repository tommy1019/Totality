package self.trek;

import java.util.ArrayList;

public class GameController
{
	private ArrayList<ControllerElement> controllerElements = new ArrayList<ControllerElement>();
	
	public void addControllerElement(ControllerElement ce)
	{
		controllerElements.add(ce);
	}
}
