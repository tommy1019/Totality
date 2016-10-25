package self.totality.webSocketServer.listener;

import java.util.UUID;

import self.totality.webSocketServer.controller.ControllerElement;

public interface DataListener
{
	public void onDataUpdate(UUID uuid, ControllerElement e);
}
