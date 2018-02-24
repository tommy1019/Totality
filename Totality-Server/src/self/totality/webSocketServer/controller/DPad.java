package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class DPad extends ControllerElement
{
	public static final String TYPE = "DPAD";
	
	@Expose
	public String type = TYPE;
	
	public static class DataClass extends ControllerElement.DataClass
	{
		public String id;
		public boolean up;
		public boolean down;
		public boolean left;
		public boolean right;

		public DataClass(String id, boolean up, boolean down, boolean left, boolean right)
		{
			this.id = id;
			this.up = up;
			this.down = down;
			this.left = left;
			this.right = right;
		}
	}

	public DPad(String id, float x, float y, float width, float height)
	{
		super(id, x, y, width, height);
	}
}
