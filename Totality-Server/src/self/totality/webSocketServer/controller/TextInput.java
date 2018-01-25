package self.totality.webSocketServer.controller;

public class TextInput extends ControllerElement
{
	String value;
	
	public TextInput(String id, ControllerElementType type, String value) 
	{
		super(id, type);
		
		this.value = value;
	}

}
