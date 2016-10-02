package self.totality;

import java.util.UUID;

public interface DataListener
{
	public void onDataUpdate(UUID uuid, ControllerElement e);
}
