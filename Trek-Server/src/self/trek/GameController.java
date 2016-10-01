package self.trek;

import java.util.ArrayList;
import java.util.List;

public class GameController
{
	private List<ControllerElement> controllerElements = new ArrayList<ControllerElement>();
	
	public GameController(List<ControllerElement> elements)
	{
		controllerElements = elements;
	}
	
	public void addControllerElement(ControllerElement ce)
	{
		controllerElements.add(ce);
	}
	
	//Updates all of the elements on the controller
	//Should be called during the update loop of the game
	public void update()
	{
		for(ControllerElement ce : controllerElements)
		{
			ce.update();
		}
	}
}
