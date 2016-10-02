package self.trek;

import java.util.HashMap;

import com.google.gson.annotations.Expose;

public class GameController
{
	@Expose
	public HashMap<String, ControllerElement> controllerElements = new HashMap<String, ControllerElement>();
	
	public GameController()
	{
		
	}
	
	public void addControllerElement(ControllerElement ce)
	{
		controllerElements.put(ce.getId(), ce);
	}
}
