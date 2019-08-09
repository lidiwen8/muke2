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
   <link rel="stylesheet" href="http://love.lidiwen.club/bootstrap.min.css">
    <link rel="stylesheet" href="css/site.css">
    <script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js" type="text/javascript"></script>
    <script src="https://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="page/pagetool.js" type="text/javascript"></script>
    <title>登陆日志</title>
    <script type="text/javascript">
        var page = 1;
        var username="";
        $(function(){
            getUserlog(page);
        });
        function getUserlog(pageNum){
            $.ajax({
                url : "admin/adminUserServlet",
                type : "post",
                async : "true",
                data : {"action" : "getUserlog", "pageNum" : pageNum,"username":username},
                dataType : "json",
                success : function(data){
                    if (data.res==1){
                        $(".list").html("");
                        $.each(data.data.data, function(index, userlogItem) {
                            var userlog = $(".template").clone();
                            userlog.show();
                            userlog.removeClass("template");
                            userlog.find(".username").text(userlogItem.username);
                            userlog.find(".realname").text(userlogItem.realname);
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

        function queryUserLogByName(){
            username = $("#username").val();
            getUserlog(1);
        }
    </script>
</head>
<body>
<jsp:include flush="true" page="header.jsp" />
<div class="container">
    <div class="row">
        <div class="col-sm-12 msgtitle">
            <h3 class="pull-left" style="color: #39a4ff">用户登录日志</h3>
            <div class="replybtn">
                <button type="button" class="btn btn-success" data-toggle="modal"
                        data-target="#search">搜索</button>
            </div>
        </div>
    </div>
    <div class="col-sm-12">
        <table class="table table-striped">
            <thead>
            <th>用户名</th>
            <th>昵称</th>
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
            <td class="userloginfo username">用户名</td>
            <td class="userloginfo realname">昵称</td>
            <td class="userloginfo ip">登录IP</td>
            <td class="userloginfo place">登录地区</td>
            <td class="userloginfo logintime">登录时间</td>
        </tr>
    </table>
    <div class="row" style="text-align: center">
        <jsp:include page="/page/pagetool.jsp"></jsp:include>
    </div>
</div>

<!-- 模态框（Modal） -->
<div class="modal fade" id="search" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content modalcenter">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="replyLabel">搜索</h4>
            </div>
            <div class="modal-body">
                <form role="form">
                    <div class="form-group">
                        <label for="username">用户名：</label>
                        <input type="text" class="form-control" id="username" placeholder="">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="queryUserLogByName()">搜索</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<jsp:include flush="true" page="footer.jsp" />
</body>
</html>
