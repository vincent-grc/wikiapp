//package com.jiawa.wiki.interceptor;// package com.jiawa.wiki.interceptor;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Component;
// import org.springframework.web.servlet.HandlerInterceptor;
// import org.springframework.web.servlet.ModelAndView;
//
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
//
// /**
//  * Interceptor：Spring frame exclusive，usually used for login check, permission check
//  * and request log print.
//  * Pay attention to interface /login
//  */
// @Component
// public class LogInterceptor implements HandlerInterceptor {
//
//     private static final Logger LOG = LoggerFactory.getLogger(LogInterceptor.class);
//
//     @Override
//     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//         // 打印请求信息
//         LOG.info("------------- LogInterceptor Start -------------");
//         LOG.info("Request Address: {} {}", request.getRequestURL().toString(), request.getMethod());
//         LOG.info("Remote Address: {}", request.getRemoteAddr());
//
//         long startTime = System.currentTimeMillis();
//         request.setAttribute("requestStartTime", startTime);
//         return true;
//     }
//
//     @Override
//     public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//         long startTime = (Long) request.getAttribute("requestStartTime");
//         LOG.info("------------- LogInterceptor End, Used: {} ms -------------", System.currentTimeMillis() - startTime);
//     }
// }
