package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class TextElement extends ControllerElement
{
private final static int DEFAULT_FONT_SIZE = 12;
	
	@Expose
	public String type = "TEXT";

	@Expose
	public String content;
	
	@Expose
	public int fontSize = DEFAULT_FONT_SIZE;

	public TextElement(String id, float x, float y, float width, float height, String content)
	{
		this(id, x, y, width, height, content, DEFAULT_FONT_SIZE);
	}
	
	public TextElement(String id, float x, float y, float width, float height, String content, int fontSize)
	{
		super(id, x, y, width, height);
		
		this.content = content;
		this.fontSize = fontSize;
	}

}
