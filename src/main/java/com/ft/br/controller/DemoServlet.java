package com.ft.br.controller;

import com.ft.web.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;


/**
 * DemoServlet
 *
 * @author shichunyang
 */
@Slf4j
public class DemoServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpUtil.httpCode(request, response, null);
        Enumeration<String> initParameterNames = this.getInitParameterNames();
        while (initParameterNames.hasMoreElements()) {
            String initParameterName = initParameterNames.nextElement();
            String initParameterValue = this.getInitParameter(initParameterName);
            log.info("{} => {}", initParameterName, initParameterValue);
            response.getWriter().println(initParameterName + " => " + initParameterValue);
        }
    }
}
