package edu.pitt.nccih.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Interceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
                             Object handler) throws Exception {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res,
                           Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = req.getSession();
        try {
            if (ifLoggedIn(session))
                modelAndView.addObject("loggedIn", true);
        } catch (NullPointerException e) {
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest req,
                                HttpServletResponse res, Object handler, Exception ex)
            throws Exception {

    }

    public static Boolean ifLoggedIn(HttpSession session) {
        SecurityContext ctx = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (ctx != null) {
            Authentication authentication = ctx.getAuthentication();
            session.setAttribute("username", ((User) authentication.getPrincipal()).getUsername());
            return true;
        }

        return false;
    }

}