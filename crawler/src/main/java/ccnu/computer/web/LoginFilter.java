package ccnu.computer.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccnu.computer.model.SystemContext;
import ccnu.computer.model.User;

public class LoginFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest hsq=(HttpServletRequest) req;
		User u = (User)hsq.getSession().getAttribute("loginUser");
		if(u==null){
			((HttpServletResponse)resp).sendRedirect(hsq.getContextPath()+"/login");
		}
		chain.doFilter(req, resp);
	}
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}

}
