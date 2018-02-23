package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class TextInputElement extends ControllerElement
{
	@Expose
	public String type = "TEXTINPUT";
	
	public static class DataClass extends ControllerElement.DataClass
	{
		public String id;
		public String text;
		
		public DataClass(String id, String text)
		{
			this.id = id;
			this.text = text;
		}
	}
	
	public TextInputElement(String id, float x, float y, float width, float height)
	{
		super(id, x, y, width, height);
	}
	
}
