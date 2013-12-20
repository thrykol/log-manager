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
	public static final String LOG_4J_CLASS = "org.slf4j.impl.Log4jLoggerAdapter";
	public static final String LOGBACK_CLASS = "ch.qos.logback.classic.Logger";

	static
	{
		org.slf4j.Logger slf4jLogger = LoggerFactory.getLogger(LogManagerFactory.class);

		String klass = slf4jLogger.getClass().getName();
		if(klass.equals(LOG_4J_CLASS))
		{
			logger = new Log4jManager();
		}
		else if(klass.equals(LOGBACK_CLASS))
		{
			logger = new LogbackManager();
		}
		else
		{
			log.warn("Logging architecture {} is unhandled.", klass);
			logger = null;
		}
	}

	public static TreeSet<Wrapper> getLoggers()
	{
		if(logger != null)
		{
			return logger.load();
		}
		else
		{
			return new TreeSet<>();
		}
	}

	// TODO: return a mapping of log name to status
	public static void register(Map<String, String> parameters)
	{
		if(logger != null && parameters != null && !parameters.isEmpty())
		{
			logger.setLoggers(parameters);
		}
	}
}
