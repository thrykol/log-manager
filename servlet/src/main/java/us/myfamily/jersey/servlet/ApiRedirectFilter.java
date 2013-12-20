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

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiRedirectFilter
                          implements Filter
{

	@Override
	public void init(FilterConfig filterConfig)
	                          throws ServletException
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	                          throws IOException,
	                          ServletException
	{
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;

		if(req.getMethod().equals("GET"))
		{
			// Redirect the GET
			String requestURI = req.getRequestURI();
			String newURI = requestURI.replace("logging-ui", "logging-api");
			log.debug("Redirecting {} {} to {}", new Object[] { req.getMethod(), requestURI, newURI });
			res.sendRedirect(newURI);
		}
		else
		{
			// Forward the request
			String requestPath = req.getServletPath() + req.getPathInfo();
			String newURI = requestPath.replace("logging-ui", "logging-api");
			log.debug("Forwarding {} {} to {}", new Object[] { req.getMethod(), requestPath, newURI });
			RequestDispatcher rd = request.getRequestDispatcher(newURI);
			rd.forward(request, response);
		}
	}

	@Override
	public void destroy()
	{
	}
}
