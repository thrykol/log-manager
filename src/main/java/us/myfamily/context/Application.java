package us.myfamily.context;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import us.myfamily.jersey.LogManager;
import us.myfamily.jersey.api.Manager;

@ApplicationPath("/")
public class Application
                          extends ResourceConfig
{

	public Application()
	{
		super(LogManager.class, Manager.class);
	}
}
