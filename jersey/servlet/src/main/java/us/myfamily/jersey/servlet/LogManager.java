package us.myfamily.jersey.servlet;

import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/")
public class LogManager
{

  @GET
	@SuppressWarnings("resource")
	public Response get()
	                          throws IOException
	{
		log.debug("Loading base page");
		InputStream entity = this.getClass().getClassLoader().getResourceAsStream("web/index.html");
		return Response.ok(entity).build();
	}
}
