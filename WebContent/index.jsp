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
        var wsUrl = "ws://<%=basePath2%>/websocket";
        var lockReconnect = false;  //避免ws重复连接
        var reconnectcount = 0; //重连的次数
        createWebSocket(wsUrl);   //连接ws
        //判断当前浏览器是否支持WebSocket
        function createWebSocket(wsUrl) {
            try {
                if ('WebSocket' in window) {
                    //建立连接，这里的/websocket ，是ManagerServlet中开头注解中的那个值
                    websocket = new WebSocket(wsUrl);
                } else if ('MozWebSocket' in window) {
                    websocket = new MozWebSocket(wsUrl);
                }
                else {
                    alert('您的浏览器不支持websocket协议,建议使用新版谷歌、火狐等浏览器，请勿使用IE10以下浏览器，360浏览器请使用极速模式，不要使用兼容模式！');
                }
                //连接发生错误的回调方法
                websocket.onerror = function () {
                    reconnect(wsUrl);
                    setMessageInnerHTML("WebSocket连接发生错误");
                };
                //连接成功建立的回调方法
                websocket.onopen = function () {
                    heartCheck.reset().start();      //心跳检测重置
                    setMessageInnerHTML("WebSocket连接成功");
                }
                //接收到消息的回调方法
                websocket.onmessage = function (event) {
                    heartCheck.reset().start();      //拿到任何消息都说明当前连接是正常的
                    setMessageInnerHTML(event.data);
                    if(event.data.indexOf("add")!=-1){
                        location.reload();
                    }
                    if(event.data=="deleteUser"+$("#userid").val()){
                        var messageVary = confirm("最新通知：你的当前账号已被管理员禁用！");
                        if (messageVary == true||messageVary==false) {
                            logout();
                        }
                    }
                }
                //连接关闭的回调方法
                websocket.onclose = function () {
                    reconnect(wsUrl);
                    setMessageInnerHTML("WebSocket连接关闭");
                }
            } catch (e) {
                reconnect(wsUrl);
            }
        }
        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function () {
            closeWebSocket();
        }

        $(window).on('beforeunload unload', function() {
            closeWebSocket();
        });

        //将消息显示在网页上
        function setMessageInnerHTML(innerHTML) {
            $('message').innerHTML += innerHTML + '<br/>';
        }
        //关闭WebSocket连接
        function closeWebSocket() {
            websocket.close();
        }
        function reconnect(wsUrl) {
            if(lockReconnect) return;
            lockReconnect = true;
            if(reconnectcount>=10){
                websocket.close();//重连超过10次共30秒自动放弃连接请求
                return;
            }
            setTimeout(function () {     //没连接上会一直重连，设置延迟避免请求过多
                createWebSocket(wsUrl);
                reconnectcount++;
                lockReconnect = false;
            }, 3000);
        }
        //心跳检测
        var heartCheck = {
            timeout: 540000,        //9分钟发一次心跳
            timeoutObj: null,
            serverTimeoutObj: null,
            reset: function(){
                clearTimeout(this.timeoutObj);
                clearTimeout(this.serverTimeoutObj);
                return this;
            },
            start: function(){
                var self = this;
                this.timeoutObj = setTimeout(function(){
                    //这里发送一个心跳，后端收到后，返回一个心跳消息，
                    //onmessage拿到返回的心跳就说明连接正常
                    websocket.send("ping");
                    self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了
                        websocket.close();     //如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
                    }, self.timeout)
                }, this.timeout)
            }
        }
        function logout() {
            // Ajax 异步请求退出登录
            $.ajax({
                url: "userServlet?action=logout",
                type: "POST",
                async: "true",
                dataType: "json",
                success: function (data) {
                    if (data.res == 1) {
                        alert("你已经被强制退出！");
                        window.location.replace("login.jsp");
                    }
                }
            });
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
<input type="hidden" id="userid" value="${sessionScope.user.userid}">
<jsp:include flush="fasle" page="footer.jsp"/>
</body>
</html>