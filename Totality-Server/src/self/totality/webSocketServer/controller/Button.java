package self.totality.webSocketServer.controller;

public class Button extends VisibleControllerElement
{
	protected boolean pressed;

	public Button(String id)
	{
		this(id, false);
	}
	
	public Button(String id, float x, float y, float width, float height)
	{
		super(id, ControllerElementType.BUTTON);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
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
