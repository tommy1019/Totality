package self.totality.webSocketServer.controller;

public class VisibleControllerElement extends ControllerElement
{
	
	public VisibleControllerElement(String id, ControllerElementType type, float x, float y, float width, float height)
	{
		super(id, type);
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
