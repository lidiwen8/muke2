package com.muke.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.muke.pojo.Admin;
import com.muke.pojo.User;


/**
 * Servlet Filter implementation class UserFilter
 */
@WebFilter(filterName="userFilter",urlPatterns = {"/user/*"})
public class UserFilter implements Filter {

    /**
     * Default constructor. 
     */
    public UserFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		HttpSession session =  ((HttpServletRequest)request).getSession();
		User user = (User)session.getAttribute("user");
		Admin admin = (Admin) session.getAttribute("admin");
		if(user == null&&admin==null) {
			
			if (httpRequest.getRequestURI().endsWith(".json")){
				httpResponse.getWriter().println("{\"res\":-2, \"info\":\"未登录\"}");
			}
			else{
				((HttpServletRequest)request).getRequestDispatcher("/login.jsp").forward(request, response);
			}
	}else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
