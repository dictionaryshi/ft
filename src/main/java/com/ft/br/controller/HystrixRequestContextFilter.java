package com.ft.br.controller;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import javax.servlet.*;
import java.io.IOException;

public class HystrixRequestContextFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HystrixRequestContext hystrixRequestContext = HystrixRequestContext.initializeContext();
		try {
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			hystrixRequestContext.shutdown();
		}
	}

	@Override
	public void destroy() {
	}
}
