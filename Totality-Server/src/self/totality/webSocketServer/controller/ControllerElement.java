package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class ControllerElement
{
	@Expose
	public String id;
	
	@Expose
	public ControllerElementType type;
		
	@Expose
	public float x = 0;
	@Expose
	public float y = 0;
	@Expose
	public float width = 0;
	@Expose
	public float height = 0;
	
	public ControllerElement(String id, ControllerElementType type)
	{
		this.id = id;
		this.type = type;
	}
	
	public String getId()
	{
		return id;
	}
}
