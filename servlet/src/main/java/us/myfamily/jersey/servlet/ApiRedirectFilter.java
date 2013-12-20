package us.myfamily.jersey.servlet;

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
