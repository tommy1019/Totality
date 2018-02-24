package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class TextInput extends ControllerElement
{
	public static final String TYPE = "TEXTINPUT";
	
	@Expose
	public String type = TYPE;
	
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
	
	public TextInput(String id, float x, float y, float width, float height)
	{
		super(id, x, y, width, height);
	}
	
}
