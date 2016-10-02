package self.totality;

public class Button extends ControllerElement
{
	protected boolean pressed;

	protected Button(String id, boolean isPressed)
	{
		super(id, ControllerElementType.BUTTON);
		
		this.pressed = isPressed;
	}

	public boolean pressed()
	{
		return pressed;
	}
}
