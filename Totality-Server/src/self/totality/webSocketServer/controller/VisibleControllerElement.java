package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class VisibleControllerElement extends ControllerElement
{
	@Expose
	public float x = 0;
	@Expose
	public float y = 0;
	@Expose
	public float width = 0;
	@Expose
	public float height = 0;
	
	public VisibleControllerElement(String id, ControllerElementType type)
	{
		super(id, type);
		this.visible = true;
	}
	
	public VisibleControllerElement(String id, ControllerElementType type, float x, float y, float width, float height)
	{
		super(id, type);
		this.visible = true;
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
