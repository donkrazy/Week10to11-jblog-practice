package com.estsoft.jblog.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.estsoft.jblog.service.UserService;
import com.estsoft.jblog.vo.UserVo;

public class AuthLoginInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired()
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if ( "GET".equals( request.getMethod() )){
			return true;
		}
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		UserVo userVo = new UserVo();
		userVo.setName(name);
		userVo.setPassword(password);
		UserVo authUser = userService.login(userVo);
		if(authUser == null){
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/loginFail.jsp");
			rd.forward( request, response );	
			return false;
		}
		HttpSession session = request.getSession(true);
		session.setAttribute("authUser", authUser );
		String next_url = request.getParameter("next")==null?"/":request.getParameter("next");
		response.sendRedirect( next_url );
		return false;
	}
}
