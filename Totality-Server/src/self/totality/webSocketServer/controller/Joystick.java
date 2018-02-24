package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class Joystick extends ControllerElement
{
	public static final String TYPE = "JOYSTICK";
	
	@Expose
	public String type = TYPE;
	
	public static class DataClass extends ControllerElement.DataClass
	{
		public String id;
		public double xVal = 0;
		public double yVal = 0;
		public double force = 0;
		
		public DataClass(String id, double xVal, double yVal, double force)
		{
			this.id = id;
			this.xVal = xVal;
			this.yVal = yVal;
			this.force = force;
		}
	}
	
	public Joystick(String id, float x, float y, float width, float height)
	{
		super(id, x, y, width, height);
	}
}
