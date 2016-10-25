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
}
