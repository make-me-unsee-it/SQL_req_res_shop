package com.step.hryshkin.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class BasketNullFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(BasketNullFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        checkOrderNotNull(servletResponse, request);
        try {
            filterChain.doFilter(request, servletResponse);
        } catch (IOException exception) {
            LOGGER.error("IOException in doFilter " + exception);
        } catch (ServletException exception) {
            LOGGER.error("ServletException in doFilter " + exception);
        }
    }

    private void checkOrderNotNull(ServletResponse servletResponse, HttpServletRequest request) {
        if (request.getSession().getAttribute("order") == null) {
            String path = "/empty_basket.jsp";
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
            try {
                requestDispatcher.forward(request, servletResponse);
            } catch (ServletException exception) {
                LOGGER.error("ServletException in checkOrderNotNull " + exception);
            } catch (IOException exception) {
                LOGGER.error("IOException in checkOrderNotNull" + exception);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
