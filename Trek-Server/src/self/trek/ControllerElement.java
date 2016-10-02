package self.trek;

import com.google.gson.annotations.Expose;

//A ControllerElement is the superclass for any button, joystick, etc on a GameController

public abstract class ControllerElement
{
	@Expose
	private String id = "Default";
	
	@Expose
	private ControllerElementType type;
		
	public ControllerElement(String id, ControllerElementType type)
	{
		this.id = id;
		this.type = type;
	}
}
