# Totality
Totality is a Java library that allows you to use any smartphone as a video game controller.

## How Does it Work?
Totality hosts a web server on the main machine. Users connect to the server using the web browser on their phone. The server sends a javascript controller to the phone, and listens for input.

## Setting up Totality
1. Download TotalityServer.jar and include it in your project.

2. Create a controller object and define its layout:
```
    //Create a controller
    GameController gc = new GameController();

    gc.addControllerElement(new TextInput("textInput", 0.5f, 0.2f, 0.3f, 0.1f));
    gc.addControllerElement(new Button("playButton", 0.5f, 0.7f, 0.3f, 0.3f));
    gc.addControllerElement(new Text("text", 0.5f, .1f, "Enter Name and Press Button", 24));
  
    //Set the default controller for new users
    Totality.instance.setDefaultController(gc);
```
(Need help with this? Check out our [controller formatting guide](https://github.com/tommy1019/Totality/wiki/Controller-Formatting-Guide))

3. Define what to do when a user connects or disconnects:
```
Totality.instance.addConnectListener(new ConnectListener()
{
  @Override
  public void onConnect(UUID uuid)
  {
      //This is called when a new user connects
      //We recommend storing the uuid in a hashmap for easy access
  }
});

Totality.instance.addDisconnectListener(new DisconnectListener()
{
  @Override
  public void onDisconnect(UUID uuid)
  {
      //This is called when a user disconnects
      //We recommend removing the uuid from your hashmap here
  }
});
```

4. Define what happens when the user interacts with a specific controller element:
```
Totality.addDataListener(Button.TYPE, new Listener<Button.DataClass>()
{
	@Override
	public void onData(UUID uuid, DataClass data)
	{
		if (data.id.equals("playButton"))
		{
			Totality.instance.sendControllerToPlayer(uuid, newController);
		}
	}
});

Totality.addDataListener(TextInput.TYPE, new Listener<TextInput.DataClass>()
{
    @Override
    public void onData(UUID uuid, TextInput.DataClass data)
	{
		User u = userMap.get(uuid);
		u.name = data.text;
	}
});
```

5. Start the totality server with:
```
Totality.instance.start();
```

And you're ready to go!

## Using Totality
Once your server is up and running, users can connect to it with their smartphones. To do so, simply:
1. Connect your phone to the same wifi network that the main computer is connected to.
2. Open the web browser on your phone.
3. Type in the ip address of the main computer to your search bar [(or use a multicast address, if supported)](https://github.com/tommy1019/Totality/wiki/Multicast-DNS).
4. Press enter. You should now be connected to the Totality server!

## Still have questions?
[Check out our wiki!](https://github.com/tommy1019/Totality/wiki)
If you can't find an answer there, feel free to [open an issue.](https://github.com/tommy1019/Totality/issues)
