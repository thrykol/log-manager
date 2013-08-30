package us.myfamily.jersey;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;

@Path("/")
public class LogManager
{
	private static String html;
	static
	{
		StringWriter writer = new StringWriter();
		String path = LogManager.class.getClassLoader().getResource("web/index.html").getPath();
		try
		{
			IOUtils.copy(new FileReader(path), writer);
			html = writer.toString();
		}
		catch(IOException e)
		{
			html = "<html><body>Error loading template file: " + e.getMessage() + "</body></html>";
		}
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String get()
	{
		return html;
	}
}
