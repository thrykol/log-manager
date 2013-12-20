Logging Manager
================

pom.xml

	<dependencies>
		<dependency>
			<groupId>us.my-family</groupId>
			<artifactId>log-manager-servlet</artifactId>
			<version>${project.version}</version>
			<type>war</type>
			<scope>runtime</scope>
		</dependency>
	</dependencies>
	
	<plugins>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-war-plugin</artifactId>
			<version>2.4</version>
			<configuration>
				<overlays>
					<overlay>
						<groupId>us.my-family</groupId>
						<artifactId>log-manager-servlet</artifactId>
					</overlay>
				</overlays>
			</configuration>
		</plugin>
	</plugins>
	
web.xml

	<filter>
		<filter-name>ApiRedirect</filter-name>
		<filter-class>us.myfamily.jersey.servlet.ApiRedirectFilter</filter-class>
	</filter>
  
	<servlet>
		<servlet-name>LoggingServlet</servlet-name>
		<servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>
		<init-param>
			<param-name>javax.ws.rs.Application</param-name>
			<param-value>us.myfamily.jersey.servlet.Application</param-value>
		</init-param>
	</servlet>
	
	<filter-mapping>
		<filter-name>ApiRedirect</filter-name>
		<url-pattern>/logging-ui/manage/*</url-pattern>
	</filter-mapping>
	
	<servlet-mapping>
		<servlet-name>default</servlet-name>
		<url-pattern>/logging-ui/*</url-pattern>
	</servlet-mapping>
	
	<servlet-mapping>
		<servlet-name>LoggingServlet</servlet-name>
		<url-pattern>/logging-api/*</url-pattern>
	</servlet-mapping>

Browse to http://localhost:8080/[context]/logging-ui/index.html.
