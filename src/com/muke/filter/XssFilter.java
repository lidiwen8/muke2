package com.muke.filter;



import com.muke.util.XssHttpServletRequestWraper;

import java.io.IOException;
import javax.servlet.Filter;

import javax.servlet.FilterChain;

import javax.servlet.FilterConfig;

import javax.servlet.ServletException;

import javax.servlet.ServletRequest;

import javax.servlet.ServletResponse;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;


@WebFilter(filterName="XssFilter",urlPatterns = {"/*"})
public class XssFilter implements Filter {



    @Override

    public void destroy() {



    }



    @Override

    public void doFilter(ServletRequest request, ServletResponse response,

                         FilterChain chain) throws IOException, ServletException {

        chain.doFilter(new XssHttpServletRequestWraper(

                (HttpServletRequest)request), response);//对request和response进行过滤

    }



    @Override

    public void init(FilterConfig arg0) throws ServletException {



    }



}
