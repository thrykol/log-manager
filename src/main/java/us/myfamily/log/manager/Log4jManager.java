package us.myfamily.log.manager;

import java.util.Enumeration;
import java.util.Map;
import java.util.TreeSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import us.myfamily.log.LogManager;

@Slf4j
public class Log4jManager
                          extends LogManager
{
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
