package self.totality.webSocketServer.controller;

public class Text extends ControllerElement
{
	protected String data;
	
	public Text(String id) 
	{
		super(id, ControllerElementType.TEXT);
		this.data = id;
	}

}
