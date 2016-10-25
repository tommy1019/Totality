package self.totality.webSocketServer.controller;

public class Button extends VisibleControllerElement
{
	protected boolean pressed;

	public Button(String id, boolean isPressed, float x, float y, float width, float height)
	{
		super(id, ControllerElementType.BUTTON, x, y, width, height);
		
		this.pressed = isPressed;
	}

	public boolean pressed()
	{
		return pressed;
	}
}
