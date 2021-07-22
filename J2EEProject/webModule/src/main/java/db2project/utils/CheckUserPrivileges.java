package db2project.utils;

import db2project.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//TODO CHECKKKKKK
public class CheckUserPrivileges implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        User u = (User) req.getSession().getAttribute("user");
        if (u.isAdmin()) {
            System.err.print("\nUser permissions violated\n");
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "You are an admin! This page is for visitors!");
            return;
        }
        chain.doFilter(request, response);
    }

    public CheckUserPrivileges() { }
    public void destroy() {	}
    public void init(FilterConfig fConfig) throws ServletException { }
}