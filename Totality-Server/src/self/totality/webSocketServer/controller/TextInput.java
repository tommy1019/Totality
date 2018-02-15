package self.totality.webSocketServer.controller;

public class TextInput extends VisibleControllerElement
{
	public String value;
	
	public TextInput(String id)
	{
		this(id, null);
	}
	
	public TextInput(String id, float x, float y, float width, float height)
	{
		super(id, ControllerElementType.TEXTINPUT);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public TextInput(String id, String value) 
	{
		super(id, ControllerElementType.TEXTINPUT);
		
		this.value = value;
	}

}
