package us.myfamily.jersey.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SampleServlet
                          extends HttpServlet
{
	private static final long serialVersionUID = -217676047932669578L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	                          throws IOException
	{
		response.getWriter().append("Hello World!").flush();
	}
}
