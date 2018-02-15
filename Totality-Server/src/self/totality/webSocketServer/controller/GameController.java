package self.totality.webSocketServer.controller;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class GameController
{
	@Expose
	public ArrayList<ControllerElement> controllerElements;
	
	public GameController()
	{
		controllerElements = new ArrayList<ControllerElement>();
	}
	
	public void addControllerElement(ControllerElement ce)
	{
		controllerElements.add(ce);
	}
	
	public void addControllerElement(ControllerElement ce, float x, float y, float width, float height)
	{
		ce.x = x;
		ce.y = y;
		ce.width = width;
		ce.height = height;
		
		controllerElements.add(ce);
	}
	
	public Button addButton(String id, float x, float y, float width, float height)
	{
		Button button = new Button(id, x, y, width, height);		
		controllerElements.add(button);
		return button;
	}
	
	public TextInput addTextInput(String id, float x, float y, float width, float height)
	{
		TextInput textInput = new TextInput(id, x, y, width, height);
		controllerElements.add(textInput);
		return textInput;
	}
	
	public Joystick addJoystick(String id, float x, float y, float width, float height)
	{
		Joystick joystick = new Joystick(id, x, y, width, height);
		controllerElements.add(joystick);
		return joystick;
	}
	
	public Text addText(String id, String content, float x, float y)
	{
		Text text = new Text(id, content, x, y);
		controllerElements.add(text);
		return text;
	}
	
	public Text addText(String id, String content, int fontSize, float x, float y)
	{
		Text text = new Text(id, content, fontSize, x, y);
		controllerElements.add(text);
		return text;
	}
}
