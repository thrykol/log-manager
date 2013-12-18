package us.myfamily.jersey.servlet;

/** Copyright 2013 Shane Perry
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. **/

import java.util.*;
import java.util.Map.Entry;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;
import us.myfamily.log.LogManager.Wrapper;
import us.myfamily.log.LogManagerFactory;

/** Some awesome stuff goes here
 * 
 * @author shane */
@Path("/manage")
public class Manage
{
	/** Update the log level of the given loggers
	 * 
	 * @summary Update the level of a logger
	 * @author shane
	 * @param parameters Mapping of each logger with it's new log level
	 * @return JSONObject representing the status of the update */
	@POST()
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces(MediaType.APPLICATION_JSON)
	public BasicResponse post(MultivaluedMap<String, String> parameters)
	{
		Map<String, String> mapping = new HashMap<String, String>();
		for(Entry<String, List<String>> entry : parameters.entrySet())
		{
			mapping.put(entry.getKey(), entry.getValue().get(0));
		}

		LogManagerFactory.resgister(mapping);

		BasicResponse response = new BasicResponse();
		response.status = "success";

		return response;
	}

	/** Get the logger implementation and all registered loggers
	 * 
	 * @summary Get all registered loggers
	 * @author shane
	 * @return JSONObject representing the current logger state */
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public JacksonLogger get()
	{
		LinkedHashMap<String, Map<String, Object>> mapping = new LinkedHashMap<String, Map<String, Object>>();

		TreeSet<Wrapper> wrappers = LogManagerFactory.getLoggers();
		for(Wrapper wrapper : wrappers)
		{
			Map<String, Object> level = new HashMap<String, Object>();
			level.put("level", wrapper.getLevel());
			level.put("isEffective", wrapper.isEffectiveLevel());

			mapping.put(wrapper.getName(), level);
		}

		JacksonLogger result;
		if(LogManagerFactory.logger == null)
		{
			result = new JacksonLogger();
			result.implementation = "unset";
			result.loggers = mapping;
			result.status = "error";
		}
		else
		{
			result = new JacksonLogger();
			result.implementation = LogManagerFactory.logger.getLoggerType();
			result.levels = LogManagerFactory.logger.getLevels();
			result.loggers = mapping;
			result.status = "success";
		}

		return result;
	}

	public class JacksonLogger
	{
		@Getter
		@Setter
		private String implementation;
		@Getter
		@Setter
		private String status;
		@Getter
		@Setter
		private List<String> levels = new ArrayList<String>();
		@Getter
		@Setter
		@XmlElement(name = "loggers")
		private Map<String, Map<String, Object>> loggers;
	}

	public class BasicResponse
	{
		@Getter
		@Setter
		private String status;
	}
}
