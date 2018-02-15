package self.totality.webSocketServer.controller;

public class VisibleControllerElement extends ControllerElement
{
	public VisibleControllerElement(String id, ControllerElementType type)
	{
		super(id, type);
		this.visible = true;
	}
	
	public VisibleControllerElement(String id, ControllerElementType type, float x, float y, float width, float height)
	{
		super(id, type);
		this.visible = true;
	}
}
