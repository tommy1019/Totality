package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class Text extends ControllerElement
{
	public static final String TYPE = "TEXT";
	
	private final static int DEFAULT_FONT_SIZE = 12;

	@Expose
	public String type = TYPE;

	@Expose
	public String content;

	@Expose
	public int fontSize;

	public Text(String id, float x, float y, String content)
	{
		this(id, x, y, content, DEFAULT_FONT_SIZE);
	}
	
	public Text(String id, float x, float y, String content, int fontSize)
	{
		//Width and height is meaningless for text
		//Size is determined by the font size and content
		//So we just pass 1 as a placeholder value
		super(id, x, y, 1, 1);
		
		this.content = content;
		this.fontSize = fontSize;
	}

}
