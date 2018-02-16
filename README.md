# Totality
Totality is a Java framework that allows you to use any smartphone as a video game controller.

**Note: Totality is currently a work in progress. Use at your own risk.**

## How Does it Work?
Totality hosts a web server on the main machine. Users connect to the server using the web browser on their phone. The server sends a javascript controller to the phone, and listens for input.

## Getting started
1. Download TotalityServer.jar and include it in your project.

2. Create a controller object and define its layout:
```
    //Creates a controller with one button and one joystick
    GameController gc = new GameController();
    gc.addButton("button1", 0.5f, 0.25f, 1.0f, 0.5f);
    gc.addJoystick("joystick1", 0.5f, 0.75f, 1.0f, 0.5f);
  
    //Sends the controller to the Totality server
    TotalityServer.instance.setDefaultController(gc);
```

3. Define the controller behavior using the following code:
```
TotalityServer.instance.addConnectListener(new ConnectListener()
{
  @Override
  public void onConnect(UUID uuid)
  {
      //This is called when a new user connects
      //We recommend storing the uuid in a hashmap for easy access
  }
});

TotalityServer.instance.addDisconnectListener(new DisconnectListener()
{
  @Override
  public void onDisconnect(UUID uuid)
  {
      //This is called when a user disconnects
      //We recommend removing the uuid from your hashmap here
  }
});

TotalityServer.instance.addDataListener(new DataListener()
{
  @Override
  public void onDataUpdate(UUID uuid, ControllerElement e)
  {
      //This is called whenever any controller sends data
      //The uuid corresponds to the user
      //The ControllerElement object contains the data from the controller
      
      if(e.id.equals("button1")
      {
           System.out.println("User " + uuid + " pressed button 1!");
      }
  }
});
```

4. Start the totality server with:
```
TotalityServer.instance.start();
```

And you're done!
