package self.trek;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class GameController
{
	@Expose
	private List<ControllerElement> controllerElements = new ArrayList<ControllerElement>();
	
	public GameController()
	{
		
	}
	
	public void addControllerElement(ControllerElement ce)
	{
		controllerElements.add(ce);
	}
}
