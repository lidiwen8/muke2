<%--
  Created by IntelliJ IDEA.
  User: 16320
  Date: 2019/4/4
  Time: 12:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="http://love.lidiwen.club/bootstrap.min.css">
    <link rel="stylesheet" href="css/site.css">
    <script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js" type="text/javascript"></script>
    <title>密码重置</title>
</head>
<script type="text/javascript">
    $(function (){
        $.get("userServlet", {"action": "passwordResetByemail","username": "${param.username}","key":"${param.key}","email":"${param.email}"}, function (data) {
                  if(data.res==1){
                      alert(data.info);
                      window.location.replace("login.jsp");
                  }else if(data.res==-2){
                      alert(data.info);
                      window.location.replace("mailpassword.jsp");
                  }else {
                      alert(data.info);
                      $(".text-warning").text(data.info);
                      setTimeout(function(){//4秒后跳转
                         location.href = "index.jsp";//PC网页式跳转
                      },4000);
                  }
        }, "json");
    });
</script>
<body>
<jsp:include flush="true" page="header.jsp"/>
<div class="text-center">
   <span class="text-warning" style="color: red"></span><br>
    <div><a href="index.jsp"> 【如果您的浏览器没有自动跳转，请点击此链接】</a></div>
</div>
<jsp:include flush="true" page="footer.jsp"/>
</body>
</html>
