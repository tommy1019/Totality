package self.totality.webSocketServer.controller;

public class DPad extends VisibleControllerElement
{
	protected boolean up = false;
	protected boolean down = false;
	protected boolean left = false;
	protected boolean right = false;
	
	public DPad(String id) 
	{
		super(id, ControllerElementType.DPAD);
	}

	public DPad(String id, float x, float y, float width, float height)
	{
		super(id, ControllerElementType.DPAD);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public DPad(String id, boolean u, boolean d, boolean l, boolean r)
	{
		super(id, ControllerElementType.DPAD);
		
		this.up = u;
		this.down = d;
		this.left = l;
		this.right = r;
	}
	
	public boolean up()
	{
		return up;
	}
	
	public boolean down()
	{
		return down;
	}
	
	public boolean left()
	{
		return left;
	}
	
	public boolean right()
	{
		return right;
	}
}
