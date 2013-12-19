package us.myfamily.jersey.servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiFilter
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
		log.warn("Servlet Path: {}", req.getServletPath());
		log.warn("Context: {}", req.getContextPath());
		log.warn("Path: {}", req.getPathInfo());

		RequestWrapper wrapper = new RequestWrapper(req);
		wrapper.setServletPath("/logging-api");

		log.warn("New Path: {}", wrapper.getServletPath());
		request = wrapper;
		chain.doFilter(request, response);
	}

	@Override
	public void destroy()
	{
	}

	@Setter
	public static class RequestWrapper
	                          extends HttpServletRequestWrapper
	{

		private String servletPath;

		public RequestWrapper(HttpServletRequest request)
		{
			super(request);
		}

		public String getServletPath()
		{
			log.warn("Servlet Path: {}", this.servletPath);
			log.warn("Context: {}", super.getContextPath());
			log.warn("Path: {}", super.getPathInfo());
			return this.servletPath;
		}
	}
}
