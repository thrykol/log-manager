package us.myfamily.jersey.servlet;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
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
