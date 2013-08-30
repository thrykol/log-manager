package us.myfamily.log;

import java.util.Map;
import java.util.TreeSet;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import us.myfamily.log.LogManager.Wrapper;
import us.myfamily.log.manager.Log4jManager;
import us.myfamily.log.manager.LogbackManager;

@Slf4j
public abstract class LogManagerFactory
{
	public static final LogManager logger;
	private static TreeSet<Wrapper> wrappers;

	static
	{
		org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger(LogManagerFactory.class);

		String klass = slf4jLogger.getClass().getName();
		if(klass.equals("org.slf4j.impl.Log4jLoggerAdapter"))
		{
			logger = new Log4jManager();
			wrappers = logger.load();
		}
		else if(klass.equals("ch.qos.logback.classic.Logger"))
		{
			logger = new LogbackManager();
			wrappers = logger.load();
		}
		else
		{
			log.warn("Logging architecture {} is unhandled.", klass);
			logger = null;
		}
	}

	public static TreeSet<Wrapper> getLoggers()
	{
		return wrappers;
	}

	// TODO: return a mapping of log name to status
	public static void resgister(Map<String, String> parameters)
	{
		if(logger == null)
		{
			wrappers = new TreeSet<Wrapper>();
		}
		else
		{
			if(parameters != null && !parameters.isEmpty())
			{
				logger.setLoggers(parameters);
			}

			wrappers = logger.load();
		}
	}
}
