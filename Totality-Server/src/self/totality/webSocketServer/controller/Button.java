package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class Button extends ControllerElement
{
	public static final String TYPE = "BUTTON";
	
	@Expose
	public String type = TYPE;
	
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
	
	public Button(String id, float x, float y, float width, float height)
	{
		super(id, x, y, width, height);
	}
}
