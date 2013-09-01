package us.myfamily.jersey.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import us.myfamily.log.LogManager.Wrapper;
import us.myfamily.log.LogManagerFactory;

@Path("/api/manager")
public class Manager
{
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces(MediaType.APPLICATION_JSON)
	public String post(MultivaluedMap<String, String> parameters)
	                          throws JSONException
	{
		Map<String, String> mapping = new HashMap<String, String>();
		for(Entry<String, List<String>> entry : parameters.entrySet())
		{
			mapping.put(entry.getKey(), entry.getValue().get(0));
		}

		LogManagerFactory.resgister(mapping);

		return new JSONObject().put("status", "success").toString();
	}

	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public String get()
	                          throws JSONException
	{
		LinkedHashMap<String, String> mapping = new LinkedHashMap<String, String>();

		TreeSet<Wrapper> wrappers = LogManagerFactory.getLoggers();
		for(Wrapper wrapper : wrappers)
		{
			mapping.put(wrapper.getName(), wrapper.getLevel());
		}

		String result;
		if(LogManagerFactory.logger == null)
		{
			result = new JSONObject().put("implementation", "unset").put("levels", new JSONArray()).put("loggers", mapping)
			                          .put("status", "error").toString();
		}
		else
		{
			result = new JSONObject().put("implementation", LogManagerFactory.logger.getClass().getName())
			                          .put("levels", LogManagerFactory.logger.getLevels()).put("loggers", mapping)
			                          .put("status", "success").toString();
		}

		return result;
	}
}
