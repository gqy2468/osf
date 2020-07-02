package com.moefilm.web.interceptor;

import com.moefilm.web.util.Property;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class CommonInterceptor implements HandlerInterceptor{
	
	public void afterCompletion(HttpServletRequest req, HttpServletResponse resp,
			Object arg2, Exception arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void postHandle(HttpServletRequest req, HttpServletResponse resp,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
	}

	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,
			Object arg2) throws Exception {
		// TODO Auto-generated method stub
		HttpSession session = req.getSession();
		session.setAttribute("img_base_url", Property.IMG_BASE_URL);
		session.setAttribute("post_cover_thumbnail", Property.POST_COVER_THUMBNAIL);
		session.setAttribute("album_thumbnail", Property.ALBUM_THUMBNAIL);
		return true;
	}

}
