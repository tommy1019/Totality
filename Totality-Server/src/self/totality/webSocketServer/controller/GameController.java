package self.totality.webSocketServer.controller;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class GameController
{
	@Expose
	public ArrayList<ControllerElement> controllerElements;
	
	public GameController()
	{
		controllerElements = new ArrayList<ControllerElement>();
	}
	
	public void addControllerElement(ControllerElement ce)
	{
		controllerElements.add(ce);
	}
	
	public void addControllerElement(String id, ControllerElementType type)
	{
		if (type.visible)
			throw new IllegalArgumentException("Controller element is visible: " + id);
		
		this.controllerElements.add(new ControllerElement(id, type));
	}
	
	public void addControllerElement(String id, ControllerElementType type, float x, float y, float width, float height)
	{
		if (!type.visible)
			throw new IllegalArgumentException("Controller element is not visible: " + id);
		
		this.controllerElements.add(new VisibleControllerElement(id, type, x, y, width, height));
	}
}
