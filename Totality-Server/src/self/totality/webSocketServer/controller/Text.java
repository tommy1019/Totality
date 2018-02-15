package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class Text extends ControllerElement
{
	final static int DEFAULT_FONT_SIZE = 12;
	
	@Expose
	public String content;
	
	@Expose
	public int fontSize;
	
	public Text(String id, String content) 
	{
		this(id, content, DEFAULT_FONT_SIZE);
	}
	
	public Text(String id, String content, int fontSize)
	{
		super(id, ControllerElementType.TEXT);
		this.id = id;
		this.content = content;
		this.fontSize = fontSize;
		this.width = 1;
		this.height = 1;
	}

}
