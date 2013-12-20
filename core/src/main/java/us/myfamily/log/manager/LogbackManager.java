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
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import us.myfamily.log.LogManager;
import us.myfamily.log.LogManagerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

public class LogbackManager
                          extends LogManager
{
	private static final Logger log = (Logger)org.slf4j.LoggerFactory.getLogger(LogbackManager.class.getName());
	private static final List<String> levels;
	static
	{
		levels = Arrays.asList(new String[] {
		             Level.TRACE.toString(),
		             Level.DEBUG.toString(),
		             Level.INFO.toString(),
		             Level.WARN.toString(),
		             Level.ERROR.toString(),
		             Level.OFF.toString() });
	}

	@Override
	public void setLoggers(Map<String, String> parameters)
	{
		Level currentLevel = log.getLevel();
		log.setLevel(Level.INFO);

		for(String loggerName : parameters.keySet())
		{
			Logger logger;
			String levelName = parameters.get(loggerName);

			if(levelName == null)
			{
				continue;
			}

			logger = (Logger)org.slf4j.LoggerFactory.getLogger(loggerName);

			if("unset".equals(levelName))
			{
				if((logger != null) && (logger.getLevel() != null))
				{
					log.info("Setting {} to {}", new Object[] { loggerName, "null" });
					logger.setLevel(null);
				}
				else
				{
					log.info("Logger {} already unset", loggerName);
				}
			}
			else
			{
				Level level = Level.toLevel(levelName, Level.ERROR);
				if(loggerName.equals(log.getName()) && currentLevel != level)
				{
					log.info("Logger {} will be set to {}", new Object[] { loggerName, level });
					currentLevel = level;
				}
				else if(!loggerName.equals(log.getName()) && logger.getLevel() != level)
				{
					log.info("Setting {} to {}", new Object[] { loggerName, level });
					logger.setLevel(level);
				}
				else
				{
					log.info("Logger {} already set to {}", new Object[] { loggerName, level });
				}
			}
		}

		log.setLevel(currentLevel);
	}

	public String getLoggerType()
	{
		return LogManagerFactory.LOGBACK_CLASS;
	}

	public List<String> getLevels()
	{
		return levels;
	}

	@Override
	public TreeSet<LogManager.Wrapper> load()
	{
		TreeSet<LogManager.Wrapper> wrappers = new TreeSet<LogManager.Wrapper>();
		List<Logger> loggers = ((LoggerContext)org.slf4j.LoggerFactory.getILoggerFactory()).getLoggerList();

		for(Logger logger : loggers)
		{
			String loggerName = logger.getName();

			LogManager.Wrapper w = new Wrapper();
			w.setLogger(logger);
			w.setName(loggerName);

			wrappers.remove(w);
			wrappers.add(w);

			while(true)
			{
				int index = loggerName.lastIndexOf(".");
				if(index < 0)
				{
					break;
				}

				loggerName = loggerName.substring(0, index);
				w = new Wrapper();
				w.setName(loggerName);

				wrappers.add(w);
			}
		}

		return wrappers;
	}

	public class Wrapper
	                          extends LogManager.Wrapper
	{
		private Level[] LEVELS = { Level.TRACE, Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR, Level.OFF, };

		@Override
		public String getLevel()
		{
			if(this.logger != null)
			{
				Logger logger = (Logger)this.logger;
				Level level = logger.getLevel();
				if(level != null)
				{
					log.debug(getName() + " -> " + level);
					effectiveLevel = false;
					return level.toString();
				}

				for(Level l : LEVELS)
				{
					if(logger.isEnabledFor(l))
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
