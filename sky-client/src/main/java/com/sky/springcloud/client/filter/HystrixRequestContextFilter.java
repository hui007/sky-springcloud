package com.sky.springcloud.client.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 用于hystrix，为请求的返回结果做缓存时，需要用来初始化HystrixRequestContext。
 */
@Component
@WebFilter(urlPatterns = "/*",asyncSupported = true)
@ConditionalOnProperty("cacheHystrixResult")
public class HystrixRequestContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            context.close();
        }
    }
}
