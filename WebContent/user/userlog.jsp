<%--
  Created by IntelliJ IDEA.
  User: 16320
  Date: 2019/1/19
  Time: 14:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    <script src="page/pagetool.js" type="text/javascript"></script>
    <title>登陆日志</title>
    <script type="text/javascript">
        var page = 1;
        $(function(){
            getUserlog(page);
        });
        function getUserlog(pageNum){
            $.ajax({
                url : "user/userCenterServlet",
                type : "post",
                async : "true",
                data : {"action" : "getUserlog", "pageNum" : pageNum},
                dataType : "json",
                success : function(data){
                    if (data.res==1){
                        $(".list").html("");
                        $.each(data.data.data, function(index, userlogItem) {
                            var userlog = $(".template").clone();
                            userlog.show();
                            userlog.removeClass("template");
                            userlog.find(".ip").text(userlogItem.ip);
                            userlog.find(".place").text(userlogItem.place);
                            userlog.find(".logintime").text(userlogItem.logintime);
                            $(".list").append(userlog);
                        });
                        page = setPage(pageNum, data.data.totalPage, "getUserlog");
                    }
                }
            });
        }
    </script>
</head>
<body>
<jsp:include flush="true" page="../header.jsp" />
<div class="container">
    <div class="row">
        <div class="col-sm-12 msgtitle">
            <h3 class="pull-left" style="color: #39a4ff">登录日志</h3>
        </div>
    </div>
    <div class="col-sm-12">
        <table class="table table-striped">
            <thead>
            <th>登录IP</th>
            <th>登录地区</th>
            <th>登录时间</th>
            </thead>
            <tbody class="list">
            </tbody>
        </table>
    </div>
    <table class="table table-striped">
        <tr class="template">
            <td class="userloginfo ip">登录IP</td>
            <td class="userloginfo place">登录地区</td>
            <td class="userloginfo logintime">登录时间</td>
        </tr>
    </table>
    <div class="row" style="text-align: center">
        <jsp:include page="/page/pagetool.jsp"></jsp:include>
    </div>
</div>
<jsp:include flush="true" page="../footer.jsp" />
</body>
</html>
