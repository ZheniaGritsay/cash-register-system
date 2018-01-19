package com.projects.controller.filter;

import com.projects.controller.util.i18n.Internationalization;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

@WebFilter("/*")
public class InternationalizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String lang = httpRequest.getParameter("language");
        if (lang != null) {
            HttpSession session = httpRequest.getSession();
            session.setAttribute("language", lang);

            String[] lc = lang.split("_");
            Locale locale = new Locale(lc[0], lc[1]);
            Internationalization.changeLocale(locale);
        }
        chain.doFilter(request, response);
    }
}
