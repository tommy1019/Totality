package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public abstract class ControllerElement
{
	@Expose
	public String id;
	
	@Expose
	public ControllerElementType type;
		
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
