package us.myfamily.jersey.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import us.myfamily.log.LogManagerFactory;
import us.myfamily.log.LogManager.Wrapper;
import com.google.gson.Gson;

@Path("/api/manager")
public class Manager
{
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String post(Map<String, String> parameters)
	{
		// TODO: may require MultivaluedMap

		LogManagerFactory.resgister(parameters);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", "success");

		return new Gson().toJson(resultMap);
	}

	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public String get()
	{
		LinkedHashMap<String, String> mapping = new LinkedHashMap<String, String>();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("logger", LogManagerFactory.logger != null ? LogManagerFactory.logger.getClass().getName() : "unset");
		resultMap.put("levels", mapping);
		resultMap.put("status", "success");
		TreeSet<Wrapper> wrappers = LogManagerFactory.getLoggers();
		for(Wrapper wrapper : wrappers)
		{
			mapping.put(wrapper.getName(), wrapper.getLevel());
		}

		return new Gson().toJson(resultMap);
	}
}
