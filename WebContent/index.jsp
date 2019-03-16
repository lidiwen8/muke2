<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <base href="<%=basePath%>">
    <link rel="stylesheet" href="bootstrap-3.3.7-dist/css/bootstrap.css">
    <link rel="stylesheet" href="css/site.css">
    <script src="jquery/jquery-2.2.4.min.js" type="text/javascript"></script>
    <script src="bootstrap-3.3.7-dist/js/bootstrap.min.js" type="text/javascript"></script>
    <title>爱之家网站答疑平台</title>
    <script language="javascript">
        $(function () {
            getNew();
            getHot();
            getTheme();
        });

        function getNew() {
            // Ajax异步请求最新五条
            $.get("messageServlet?action=topNew",
                function (data) {
                    $.each(data.newMsg.data, function (index, element) {
                        var time = element.msgtime.split("/");
                        var msg = $(".template").clone();
                        msg.show();
                        msg.removeClass("template");
                        msg.find(".text-limit").text(element.msgtopic);
                        msg.find(".text-limit").attr("href", "<%=basePath%>newmsg.jsp?msgid=" + element.msgid + "");
//				msg.find(".badge").text(time[0]+"年"+time[1]+"月"+time[2]+"号");
                        msg.find(".badge").text(element.msgtime);
                        $(".newList").append(msg);
                    });

                }, "json");
        }

        function getHot() {

            // Ajax异步请求最热五条,就是评论最多的五条
            $.get("messageServlet?action=topHot",
                function (data) {
                    $.each(data.hotMsg.data, function (index, element) {
                        var msg = $(".template").clone();
                        msg.show();
                        msg.removeClass("template");
                        msg.find(".text-limit").text(element.msgtopic);
                        msg.find(".text-limit").attr("href", "<%=basePath%>hotmsg.jsp?msgid=" + element.msgid + "");
                        msg.find(".badge").text("浏览:" + element.accessCount + "次");
                        $(".hotList").append(msg);
                    });

                }, "json");
        }

        function getTheme() {

            // Ajax异步请求, 最热的五个主题
            $.get("messageServlet?action=topTheme",
                function (data) {
                    $.each(data.themeMsg.data, function (index, element) {
                        var msg = $(".template").clone();
                        msg.show();
                        msg.removeClass("template");
                        msg.find(".text-limit").text(element.msgtopic);
                        msg.find(".text-limit").attr("href", "<%=basePath%>thememsg.jsp?msgid=" + element.msgid + "");
                        msg.find(".badge").text(element.thename);
                        $(".themeList").append(msg);
                    });

                }, "json");
        }
    </script>
</head>
<body>
<jsp:include flush="true" page="header.jsp"/>
<div class="container">
    <div class="row">
        <div class="col-sm-4">
            <div
                    style="overflow: auto; height: 60px; line-height: 40px; padding-top: 20px;">
                <div style="float: left">
                    <h3 style="display: inline">最新</h3>
                </div>
                <div style="float: right; vertical-align: bottom;">
                    <a href="newmsg.jsp">更多>></a>
                </div>
            </div>
            <div>
                <ul class="list-group newList" style="list-style-type:none">
                    <li class="list-group-item template">
                        <span class="badge"></span>
                        <a class="msgtile text-limit"></a>
                    </li>
                </ul>
            </div>

        </div>
        <div class="col-sm-4">
            <div
                    style="overflow: auto; height: 60px; line-height: 40px; padding-top: 20px;">
                <div style="float: left">
                    <h3 style="display: inline">最热</h3>
                </div>
                <div style="float: right; vertical-align: bottom;">
                    <a href="hotmsg.jsp">更多>></a>
                </div>
            </div>

            <div>
                <ul class="list-group hotList" style="list-style-type:none">
                </ul>
            </div>
        </div>
        <div class="col-sm-4">
            <div
                    style="overflow: auto; height: 60px; line-height: 40px; padding-top: 20px;">
                <div style="float: left">
                    <h3 style="display: inline">话题</h3>
                </div>
                <div style="float: right; vertical-align: bottom;">
                    <a href="thememsg.jsp">更多>></a>
                </div>
            </div>

            <div>
                <ul class="list-group themeList" style="list-style-type:none">
                </ul>
            </div>
        </div>
    </div>
</div>
<jsp:include flush="fasle" page="footer.jsp"/>

</body>
</html>