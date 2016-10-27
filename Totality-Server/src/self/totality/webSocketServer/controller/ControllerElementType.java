package self.totality.webSocketServer.controller;

public enum ControllerElementType
{	
	JOYSTICK(true), BUTTON(true), DEFAULT(false);
	
	public boolean visible;
	
	ControllerElementType(boolean visible)
	{
		this.visible = visible;
	}
}
