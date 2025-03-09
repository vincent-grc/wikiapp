 //package com.jiawa.wiki.filter;// package com.jiawa.wiki.filter;
 //
 //import org.slf4j.Logger;
 //import org.slf4j.LoggerFactory;
 //import org.springframework.stereotype.Component;
 //
 //import jakarta.servlet.*;
 //import jakarta.servlet.http.HttpServletRequest;
 //import java.io.IOException;
 //
 //@Component
 //public class LogFilter implements Filter {
 //
 //    private static final Logger LOG = LoggerFactory.getLogger(LogFilter.class);
 //
 //    @Override
 //    public void init(FilterConfig filterConfig) throws ServletException {
 //
 //    }
 //
 //    @Override
 //    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
 //        // Print Request
 //        HttpServletRequest request = (HttpServletRequest) servletRequest;
 //        LOG.info("------------- LogFilter Start -------------");
 //        LOG.info("Request Address: {} {}", request.getRequestURL().toString(), request.getMethod());
 //        LOG.info("Remote Address: {}", request.getRemoteAddr());
 //
 //        long startTime = System.currentTimeMillis();
 //        filterChain.doFilter(servletRequest, servletResponse);
 //        LOG.info("------------- LogFilter End, Used: {} ms -------------", System.currentTimeMillis() - startTime);
 //    }
 //}
