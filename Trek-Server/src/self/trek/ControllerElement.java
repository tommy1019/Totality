package self.trek;

import com.google.gson.annotations.Expose;

//A ControllerElement is the superclass for any button, joystick, etc on a GameController

public abstract class ControllerElement
{
	@Expose
	private String name = "Default";
	
	private boolean stateChanged = false;
	
	public ControllerElement()
	{
		this("Default");
	}
	
	public ControllerElement(String name)
	{
		this.name = name;
	}
	
	public void update()
	{
		
	}
	
	public void setName(String s)
	{
		name = s;
	}
	
	public void changeState()
	{
		stateChanged = true;
	}
	
	//Returns true if the state is different than the last time this was checked
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
