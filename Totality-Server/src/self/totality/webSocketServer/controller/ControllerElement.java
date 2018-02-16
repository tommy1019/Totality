package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class ControllerElement
{
	@Expose
	public String id;
	
	@Expose
	public ControllerElementType type;
	
	@Expose
	public boolean visible = false;
	
	public ControllerElement(String id, ControllerElementType type)
	{
		this.id = id;
		this.type = type;
	}
	
	public ControllerElement(String id, ControllerElementType type, float x, float y, float width, float height)
	{
		this.id = id;
		this.type = type;
	}
	
	public String getId()
	{
		return id;
	}
}
