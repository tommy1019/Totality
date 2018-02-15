package self.totality.webSocketServer.controller;

public enum ControllerElementType
{	
	JOYSTICK(1), BUTTON(2), TEXTINPUT(3), TEXT(4), DEFAULT(0);
	
	public int typeID;
	
	private ControllerElementType(int id)
	{
		this.typeID = id;
	}
}
