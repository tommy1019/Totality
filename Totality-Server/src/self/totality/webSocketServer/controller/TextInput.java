package self.totality.webSocketServer.controller;

public class TextInput extends ControllerElement
{
	String value;
	
	public TextInput(String id, String value) 
	{
		super(id, ControllerElementType.TEXTINPUT);
		
		this.value = value;
	}

}
