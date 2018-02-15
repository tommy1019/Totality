package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class Text extends VisibleControllerElement
{
	private final static int DEFAULT_FONT_SIZE = 12;
	
	@Expose
	public String content;
	
	@Expose
	public int fontSize = DEFAULT_FONT_SIZE;
	
	public Text(String id, String content) 
	{
		super(id, ControllerElementType.TEXT);
		this.content = content;
	}
	
	public Text(String id, String content, int fontSize)
	{
		super(id, ControllerElementType.TEXT);
		this.content = content;
		this.fontSize = fontSize;
	}
	
	public Text(String id, String content, float x, float y)
	{
		super(id, ControllerElementType.TEXT);
		this.content = content;
		this.x = x;
		this.y = y;
	}
	
	public Text(String id, String content, int fontSize, float x, float y)
	{
		super(id, ControllerElementType.TEXT);
		this.content = content;
		this.fontSize = fontSize;
		this.x = x;
		this.y = y;
	}

}
