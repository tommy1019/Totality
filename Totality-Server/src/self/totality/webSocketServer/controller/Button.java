package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class Button extends ControllerElement
{
	public static final String TYPE = "BUTTON";

	@Expose
	public String type = TYPE;
	
	@Expose
	public int color;
	
	@Expose
	public float alpha;

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
		this(id, x, y, width, height, 0x000000);
	}

	public Button(String id, float x, float y, float width, float height, int color)
	{
		this(id, x, y, width, height, 0x000000, 0.0f);
	}

	public Button(String id, float x, float y, float width, float height, int color, float alpha)
	{
		super(id, x, y, width, height);
		
		this.color = color;
		this.alpha = alpha;
	}
}
