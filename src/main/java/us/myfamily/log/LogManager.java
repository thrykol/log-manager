package us.myfamily.log;

import java.util.Map;
import java.util.TreeSet;
import lombok.EqualsAndHashCode;

public abstract class LogManager
{
	public abstract void setLoggers(Map<String, String> parameters);

	public abstract TreeSet<Wrapper> load();

	@EqualsAndHashCode(doNotUseGetters = true, of = "name")
	public abstract class Wrapper
	                          implements Comparable<Wrapper>
	{
		protected Object logger;
		protected String name;

		public void setLogger(Object logger)
		{
			this.logger = logger;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public Object getLogger()
		{
			return this.logger;
		}

		public boolean isBasePackage()
		{
			return this.name.split("\\.").length <= 2;
		}

		@Override
		public int compareTo(LogManager.Wrapper obj)
		{
			if(this.equals(obj))
			{
				return 0;
			}

			return this.name.compareTo(((Wrapper)obj).getName());
		}

		public abstract String getLevel();

		public abstract String getName();
	}
}
