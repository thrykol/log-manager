package us.myfamily.log.manager;

/** Copyright 2013 Shane Perry
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. **/

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import us.myfamily.log.LogManager;
import us.myfamily.log.LogManagerFactory;

@Slf4j
public class Log4jManager
                          extends LogManager
{
	private static final List<String> levels;
	static
	{
		levels = Arrays.asList(new String[] {
		             Level.TRACE.toString(),
		             Level.DEBUG.toString(),
		             Level.INFO.toString(),
		             Level.WARN.toString(),
		             Level.ERROR.toString(),
		             Level.FATAL.toString(),
		             Level.OFF.toString() });

	}

	@Override
	public void setLoggers(Map<String, String> parameters)
	{
		for(String name : parameters.keySet())
		{
			Logger log4jLogger;
			String level = parameters.get(name);

			if(level == null)
			{
				continue;
			}

			log.debug("Setting " + name + " to " + level);

			if("root".equals(name))
			{
				log4jLogger = org.apache.log4j.LogManager.getLogger(name);
			}
			else
			{
				log4jLogger = org.apache.log4j.LogManager.getLogger(name);
			}

			if("unset".equals(level))
			{
				if((log4jLogger != null) && (log4jLogger.getLevel() != null))
				{
					log4jLogger.setLevel(null);
				}
			}
			else
			{
				Level l = Level.toLevel(level);
				if(log4jLogger == null)
				{
					log4jLogger = Logger.getLogger(name);
				}
				if(log4jLogger.getLevel() != l)
				{
					log4jLogger.setLevel(l);
				}
			}
		}
	}

	public String getLoggerType()
	{
		return LogManagerFactory.LOG_4J_CLASS;
	}

	public List<String> getLevels()
	{
		return levels;
	}

	@Override
	public TreeSet<LogManager.Wrapper> load()
	{
		TreeSet<LogManager.Wrapper> wrappers = new TreeSet<LogManager.Wrapper>();

		@SuppressWarnings("rawtypes")
		Enumeration loggers = org.apache.log4j.LogManager.getCurrentLoggers();

		while(loggers.hasMoreElements())
		{
			Logger l = (Logger)loggers.nextElement();
			String name = l.getName();

			Wrapper w = new Wrapper();
			w.setLogger(l);
			w.setName(name);

			wrappers.remove(w);
			wrappers.add(w);

			while(true)
			{
				int index = name.lastIndexOf(".");
				if(index < 0)
				{
					break;
				}

				name = name.substring(0, index);
				w = new Wrapper();
				w.setName(name);

				wrappers.add(w);
			}
		}

		return wrappers;
	}

	public class Wrapper
	                          extends LogManager.Wrapper
	{
		private Level[] LEVELS = { Level.TRACE, Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR, Level.FATAL, Level.OFF, };

		@Override
		public String getLevel()
		{
			if(this.logger != null)
			{
				Logger log4j = (Logger)this.logger;
				Level level = log4j.getLevel();
				if(level != null)
				{
					log.debug(getName() + " -> " + level);
					return level.toString();
				}

				for(Level l : LEVELS)
				{
					if(log4j.isEnabledFor(l))
					{
						log.debug("Effective: " + getName() + " -> " + l);
						return l.toString();
					}
				}
			}

			return Level.OFF.toString();
		}

		@Override
		public String getName()
		{
			if(this.logger != null)
			{
				return ((Logger)this.logger).getName();
			}
			else
			{
				return this.name;
			}
		}
	}
}
