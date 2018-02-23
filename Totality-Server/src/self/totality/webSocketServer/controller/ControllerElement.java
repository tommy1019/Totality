package self.totality.webSocketServer.controller;

import java.util.ArrayList;
import java.util.UUID;

import com.google.gson.annotations.Expose;

public abstract class ControllerElement
{
	@Expose
	public String id;
	@Expose
	public float x;
	@Expose
	public float y;
	@Expose
	public float width;
	@Expose
	public float height;
	
	@Expose
	public boolean visible = true;
	
	public static class DataClass
	{
		
	}
	
	public static abstract class ControllerListener
	{
		public abstract void onData(UUID uuid, DataClass data);
	}
	
	public static ArrayList<ControllerListener> listeners;
	
	public ControllerElement(String id, float x, float y, float width, float height)
	{
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
