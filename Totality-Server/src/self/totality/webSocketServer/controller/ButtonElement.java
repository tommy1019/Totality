package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class ButtonElement extends ControllerElement
{
	@Expose
	public String type = "BUTTON";
	
	public static class DataClass extends ControllerElement.DataClass
	{
		public String id;
		public boolean pressed;
		
		public DataClass(String id, boolean pressed)
		{
			this.id = id;
			this.pressed = pressed;
		}
	}
	
	public ButtonElement(String id, float x, float y, float width, float height)
	{
		super(id, x, y, width, height);
	}
}
