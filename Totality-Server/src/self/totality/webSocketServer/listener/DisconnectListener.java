package self.totality.webSocketServer.listener;

import java.util.UUID;

public interface DisconnectListener
{
	public void onDisconnect(UUID uuid);
}
