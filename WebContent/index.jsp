<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String basePath2 = request.getServerName() + ":" + request.getServerPort() + path;
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
            $.get("messageServlet?action=homepageTopNew",
                function (data) {
                    $.each(data.newMsg.data, function (index, element) {
                        var time = element.msgtime.split("/");
                        var msg = $(".template").clone();
                        msg.show();
                        msg.removeClass("template");
                        msg.find(".text-limit").text(element.msgtopic);
                        msg.find(".text-limit").attr("href", "<%=basePath%>message.jsp?msgid=" + element.msgid + "");
//				msg.find(".badge").text(time[0]+"年"+time[1]+"月"+time[2]+"号");
                        msg.find(".badge").text(element.msgtime);
                        $(".newList").append(msg);
                    });

                }, "json");
        }

        function getHot() {

            // Ajax异步请求最热五条,就是评论最多的五条
            $.get("messageServlet?action=homepageTopHot",
                function (data) {
                    $.each(data.hotMsg.data, function (index, element) {
                        var msg = $(".template").clone();
                        msg.show();
                        msg.removeClass("template");
                        msg.find(".text-limit").text(element.msgtopic);
                        msg.find(".text-limit").attr("href", "<%=basePath%>message.jsp?msgid=" + element.msgid + "");
                        msg.find(".badge").text("浏览:" + element.accessCount + "次");
                        $(".hotList").append(msg);
                    });

                }, "json");
        }

        function getTheme() {

            // Ajax异步请求, 最热的五个主题
            $.get("messageServlet?action=homepageTopTheme",
                function (data) {
                    $.each(data.themeMsg.data, function (index, element) {
                        var msg = $(".template").clone();
                        msg.show();
                        msg.removeClass("template");
                        msg.find(".text-limit").text(element.msgtopic);
                        msg.find(".text-limit").attr("href", "<%=basePath%>message.jsp?msgid=" + element.msgid + "");
                        msg.find(".badge").text(element.thename);
                        $(".themeList").append(msg);
                    });

                }, "json");
        }

        var websocket = null;
        //判断当前浏览器是否支持WebSocket
        if ('WebSocket' in window) {
            //建立连接，这里的/websocket ，是ManagerServlet中开头注解中的那个值
            websocket = new WebSocket("ws://<%=basePath2%>/websocket");
        }
        else {
            alert('当前浏览器 Not support websocket')
        }
        //连接发生错误的回调方法
        websocket.onerror = function () {
            setMessageInnerHTML("WebSocket连接发生错误");
        };
        //连接成功建立的回调方法
        websocket.onopen = function () {
            setMessageInnerHTML("WebSocket连接成功");
        }
        //接收到消息的回调方法
        websocket.onmessage = function (event) {
            setMessageInnerHTML(event.data);
            if(event.data.indexOf("add")!=-1){
                location.reload();
            }
        }
        //连接关闭的回调方法
        websocket.onclose = function () {
            setMessageInnerHTML("WebSocket连接关闭");
        }
        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function () {
            closeWebSocket();
        }
        //将消息显示在网页上
        function setMessageInnerHTML(innerHTML) {
            $('message').innerHTML += innerHTML + '<br/>';
        }
        //关闭WebSocket连接
        function closeWebSocket() {
            websocket.close();
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
<span id="message"></span>
<jsp:include flush="fasle" page="footer.jsp"/>
</body>
</html>