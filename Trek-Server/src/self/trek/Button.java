package self.trek;

public class Button extends ControllerElement
{
	protected boolean isPressed;

	public Button(String id)
	{
		super(id, ControllerElementType.BUTTON);
	}

	public boolean isPressed()
	{
		return isPressed;
	}
}
