package us.myfamily.log;

/** Copyright 2013 Shane Perry
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License. **/

import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import lombok.EqualsAndHashCode;

public abstract class LogManager
{
	public abstract void setLoggers(Map<String, String> parameters);

	public abstract TreeSet<Wrapper> load();

	public abstract List<String> getLevels();

	public abstract String getLoggerType();

	@EqualsAndHashCode(doNotUseGetters = true, of = "name")
	public abstract class Wrapper
	                          implements Comparable<Wrapper>
	{
		protected Object logger;
		protected String name;
		protected boolean effectiveLevel = true;

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

		public boolean isEffectiveLevel()
		{
			return this.effectiveLevel;
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
