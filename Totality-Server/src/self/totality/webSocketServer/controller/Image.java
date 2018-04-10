package self.totality.webSocketServer.controller;

import com.google.gson.annotations.Expose;

public class Image extends ControllerElement
{
	public static final String TYPE = "IMAGE";
	
	@Expose
	public String type = TYPE;
	
	@Expose
	public boolean preserveAspectRatio = false;
	
	@Expose
	String imgPath;

	public Image(String id, float x, float y, float width, float height, String imgPath)
	{
		super(id, x, y, width, height);
		
		this.imgPath = imgPath;
	}

}
