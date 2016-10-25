package self.totality.webSocketServer.controller;

public class Button extends ControllerElement
{
	protected boolean pressed;

	public Button(String id, boolean isPressed)
	{
		super(id, ControllerElementType.BUTTON);
		
		this.pressed = isPressed;
	}

	public boolean pressed()
	{
		return pressed;
	}
}
