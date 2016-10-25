package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public abstract class VisibleControllerElement extends ControllerElement
{
	@Expose
	float x;
	@Expose
	float y;
	@Expose
	float width;
	@Expose
	float height;
	
	public VisibleControllerElement(String id, ControllerElementType type, float x, float y, float width, float height)
	{
		super(id, type);
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
