<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2018/8/17
  Time: 16:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="bootstrap-3.3.7-dist/css/bootstrap.css">
    <link rel="stylesheet" href="css/site.css">
    <script src="jquery/jquery-2.2.4.min.js" type="text/javascript"></script>
    <script src="bootstrap-3.3.7-dist/js/bootstrap.min.js" type="text/javascript"></script>
    <title>爱之家网站-留言板</title>
</head>
<body>
<jsp:include flush="fasle" page="header.jsp" />
<div class="container">
<header class="entry-header">
    <h1 class="entry-title" style="position: relative;
    font-size: 16px;
    font-size: 1.8rem;
    line-height: 30px;
    text-align: center;
    margin: 35px -20px 20px -20px;
    padding: 5px 20px;
    border-left: 5px solid #009688;
    border-right: 5px solid #009688; margin-top: 40px;
    margin-right: -20px;
    margin-bottom: 10px;
    margin-left: -20px; text-align: center;">
       <img src="images/youjian.png" style="width:15px;height:15px;margin-right:5px;margin-bottom:5px;">留言板
    </h1>
</header>
    <div class="row">
        <!--PC和WAP自适应版-->
        <div id="SOHUCS" sid="message" ></div>
        <script type="text/javascript">
            (function(){
                var appid = 'cytLOVtYY';
                var conf = 'prod_e51665bf22c2186abb580922c582e203';
                var width = window.innerWidth || document.documentElement.clientWidth;
                if (width < 960) {
                    window.document.write('<script id="changyan_mobile_js" charset="utf-8" type="text/javascript" src="http://changyan.sohu.com/upload/mobile/wap-js/changyan_mobile.js?client_id=' + appid + '&conf=' + conf + '"><\/script>'); } else { var loadJs=function(d,a){var c=document.getElementsByTagName("head")[0]||document.head||document.documentElement;var b=document.createElement("script");b.setAttribute("type","text/javascript");b.setAttribute("charset","UTF-8");b.setAttribute("src",d);if(typeof a==="function"){if(window.attachEvent){b.onreadystatechange=function(){var e=b.readyState;if(e==="loaded"||e==="complete"){b.onreadystatechange=null;a()}}}else{b.onload=a}}c.appendChild(b)};loadJs("http://changyan.sohu.com/upload/changyan.js",function(){window.changyan.api.config({appid:appid,conf:conf})}); } })(); </script>
    </div>
</div>
<jsp:include flush="fasle" page="footer.jsp" />
</body>
</html>
