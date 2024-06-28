package com.pleewson.poker.filters;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class UserSessionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        HttpSession session = httpRequest.getSession(false);
        String requestURI = httpRequest.getRequestURI();

        // Check if the request is for static resources (CSS, JS, images)
        if (requestURI.startsWith("/game/css") || requestURI.startsWith("/js") || requestURI.startsWith("/images")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String welcomeURI = httpRequest.getContextPath() + "/welcome";
        boolean loginPageRequest = requestURI.equals(httpRequest.getContextPath() + "/login");
        boolean registerPageRequest = requestURI.equals(httpRequest.getContextPath() + "/register");
        boolean welcomePageRequest = requestURI.equals(httpRequest.getContextPath() + "/welcome");

        boolean loggedIn = (session != null && session.getAttribute("player") != null);

        if (loggedIn || loginPageRequest || registerPageRequest || welcomePageRequest) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            log.info("redirected user to welcome page");
            httpResponse.sendRedirect(welcomeURI);
        }
    }
}
