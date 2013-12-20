package us.myfamily.example.servlet;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleServlet
                          extends HttpServlet
{
	private static final long serialVersionUID = -217676047932669578L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	                          throws IOException
	{
		log.debug("Base servlet");
		response.getWriter().append("Hello World!").flush();
	}
}
