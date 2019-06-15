package com.muke.filter;


import com.muke.util.XssHttpServletRequestWraper;

import java.io.IOException;
import java.util.*;
import javax.servlet.Filter;

import javax.servlet.FilterChain;

import javax.servlet.FilterConfig;

import javax.servlet.ServletException;

import javax.servlet.ServletRequest;

import javax.servlet.ServletResponse;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebFilter(filterName = "XssFilter", urlPatterns = {"/user/*"})
public class XssFilter implements Filter {
    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/user/userMessageServlet?action=getMyMsg", "/user/userMessageServlet?action=getMsg", "/user/userMessageServlet?action=deleteMymsg", "/user/userMessageServlet?action=restoreMymsg","/user/userMessageServlet?action=getReplyInfo","/user/userMessageServlet?action=replyzan","/user/userMessageServlet?action=cancelreplyzan","/user/userMessageServlet?action=messagezan","/user/userMessageServlet?action=cancelmessagezan","/user/userMessageServlet?action=deleteReply","/user/userMessageServlet?action=allowreply","/user/userMessageServlet?action=Notallowreply","/user/userCenterServlet?action=getUser","/user/userCenterServlet?action=getUserlog","/user/userCenterServlet?action=UsercollectionMsgid","/user/userCenterServlet?action=UserCancelLikeMsgid","/user/userCenterServlet?action=getUserLikeMsgid")));

    @Override

    public void destroy() {


    }


    @Override

    public void doFilter(ServletRequest request, ServletResponse response,

                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String action=req.getParameter("action");
        String path = req.getRequestURI().substring(req.getContextPath().length()).replaceAll("[/]+$", "")+"?action="+action;
//        System.out.println(path);
//        if(action==null){
//            System.out.println("单纯jsp页面不拦截");
//            chain.doFilter(req, res);//单纯的jsp界面不拦截
//        }
        boolean allowedPath = ALLOWED_PATHS.contains(path);
        if(allowedPath){
//           System.out.println("未拦截该路径："+new Date() +"--"+req.getContextPath());
            chain.doFilter(req, res);
        }else {
//            System.out.println("正在拦截该路径："+new Date() +"--"+req.getContextPath());
            chain.doFilter(new XssHttpServletRequestWraper(
                    req), res);//对request和response进行过滤
        }


    }


    @Override

    public void init(FilterConfig arg0) throws ServletException {


    }


}
