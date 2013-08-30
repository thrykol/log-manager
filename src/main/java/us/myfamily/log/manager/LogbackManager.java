package us.myfamily.log.manager;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import us.myfamily.log.LogManager;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;

public class LogbackManager
                          extends LogManager
{
	private static final Logger log = (Logger)org.slf4j.LoggerFactory.getLogger(LogbackManager.class.getName());

	@Override
	public void setLoggers(Map<String, String> parameters)
	{
		Level currentLevel = log.getLevel();
		log.setLevel(Level.INFO);

		for(String logger : parameters.keySet())
		{
			Logger logback;
			String level = parameters.get(logger);

			if(level == null)
			{
				continue;
			}

			log.info("Setting {} to {}", new String[] { logger, level });

			logback = (Logger)org.slf4j.LoggerFactory.getLogger(logger);

			if("unset".equals(level))
			{
				if((logback != null) && (logback.getLevel() != null))
				{
					logback.setLevel(null);
				}
			}
			else
			{
				Level l = Level.toLevel(level, Level.ERROR);
				if(logback.getLevel() != l)
				{
					logback.setLevel(l);
				}
			}
		}

		log.setLevel(currentLevel);
	}

	@Override
	public TreeSet<LogManager.Wrapper> load()
	{
		TreeSet<LogManager.Wrapper> wrappers = new TreeSet<LogManager.Wrapper>();
		List<Logger> loggers = ((LoggerContext)org.slf4j.LoggerFactory.getILoggerFactory()).getLoggerList();

		for(Logger l : loggers)
		{
			String name = l.getName();

			LogManager.Wrapper w = new Wrapper();
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
		private Level[] LEVELS = { Level.TRACE, Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR, Level.OFF, };

		@Override
		public String getLevel()
		{
			if(this.logger != null)
			{
				Logger logback = (Logger)this.logger;
				Level level = logback.getLevel();
				if(level != null)
				{
					log.debug(getName() + " -> " + level);
					return level.toString();
				}

				for(Level l : LEVELS)
				{
					if(logback.isEnabledFor(l))
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
