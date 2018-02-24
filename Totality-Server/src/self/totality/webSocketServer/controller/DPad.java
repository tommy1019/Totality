package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class DPad extends ControllerElement
{
	public static final String TYPE = "DPAD";
	
	public static final int NONE = 0;
	public static final int UP = 1;
	public static final int UPRIGHT = 2;
	public static final int RIGHT = 3;
	public static final int DOWNRIGHT = 4;
	public static final int DOWN = 5;
	public static final int DOWNLEFT = 6;
	public static final int LEFT = 7;
	public static final int UPLEFT = 8;
	
	@Expose
	public String type = TYPE;
	
	public static class DataClass extends ControllerElement.DataClass
	{
		public String id;
		public int direction;

		public DataClass(String id, String directionString)
		{
			this.id = id;
			
			switch(directionString)
			{
			case "UP":
				this.direction = UP;
				break;
			case "UPRIGHT":
				this.direction = UPRIGHT;
				break;
			case "RIGHT":
				this.direction = RIGHT;
				break;
			case "DOWNRIGHT":
				this.direction = DOWNRIGHT;
				break;
			case "DOWN":
				this.direction = DOWN;
				break;
			case "DOWNLEFT":
				this.direction = DOWNLEFT;
				break;
			case "LEFT":
				this.direction = LEFT;
				break;
			case "UPLEFT":
				this.direction = UPLEFT;
				break;
			default:
				this.direction = NONE;
				break;
			}
		}
	}

	public DPad(String id, float x, float y, float width, float height)
	{
		super(id, x, y, width, height);
	}
}
