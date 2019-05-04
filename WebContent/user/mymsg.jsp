<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String basePath2 = request.getServerName() + ":" + request.getServerPort() + path;
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
    <link href="http://love.lidiwen.club/bootstrap-switch.min.css" rel="stylesheet">
    <script src="http://love.lidiwen.club/bootstrap-switch.min.js"></script>
    <title>爱之家网站答疑平台</title>
    <style>
        #to_top {
            right: 30px;
            bottom: 30px;
            position: fixed;
            cursor: pointer;
        }
    </style>
    <script type="text/javascript">
        var pageNum = 1;

        $(function () {
            getMyMsg();//获取我的问题
            // $("[name='my-checkbox']").bootstrapSwitch();
            // $("[name='my-checkbox']").bootstrapSwitch('onText','开启回复').bootstrapSwitch('offText','关闭回复').bootstrapSwitch("onColor",'info')
            //     .bootstrapSwitch('state',true);
        });

        function deleteMymsg(msgid, msgtopic) {
            if (confirm("确认删除该<" + msgtopic + ">帖子吗？")) {
                $.ajax({
                    url: "user/userMessageServlet",
                    type: "post",
                    data: {"action": "deleteMymsg", "msgid": msgid},
                    dataType: "json",
                    success: function (data) {
                        if (data.res == 1) {
                            alert("删除成功");
                            window.location.replace("<%=basePath%>user/mymsg.jsp");
                        } else {
                            alert(data.info);
                        }
                    }
                });
            }
            ;
        }

        function  bootstrapswitch() {
            /* 初始化控件 */
            $(".bootstrapswitch").bootstrapSwitch({
                onText : "关闭",      // 设置ON文本  
                offText : "开启",    // 设置OFF文本  
                onColor : "info",// 设置ON文本颜色     (info/success/warning/danger/primary)  
                offColor : "success",  // 设置OFF文本颜色        (info/success/warning/danger/primary)  
                size : "normal",    // 设置控件大小,从小到大  (mini/small/normal/large)  
                // handleWidth:"35",//设置控件宽度
                // 当开关状态改变时触发  

                onSwitchChange: function(event, state) {
                    var msgId = event.target.defaultValue;
                    if (state == true) {
                        allowreply(msgId);
                        /* alert("ON"); */
                    } else {
                        //下线
                        Notallowreply(msgId);
                        /* alert("OFF"); */
                    }
                }
            });
        }

        function allowreply(msgid){
            $.ajax({
                url: "user/userMessageServlet",
                type: "post",
                data: {"action": "allowreply", "msgid": msgid},
                dataType: "json",
                success: function (data) {
                    if (data.res == 1) {
                        alert(data.info);
                    }else if(data.res == -2){
                        alert(data.info);
                        location.reload();
                    }
                    else {
                        alert(data.info);
                    }
                }
            });
        }

        function Notallowreply(msgid){
            $.ajax({
                url: "user/userMessageServlet",
                type: "post",
                data: {"action": "Notallowreply", "msgid": msgid},
                dataType: "json",
                success: function (data) {
                    if (data.res == 1) {
                        alert(data.info);
                    } else if(data.res == -2){
                        alert(data.info);
                        location.reload();
                    }else {
                        alert(data.info);
                    }
                }
            });
        }


        function restoreMymsg(msgid) {
            $.ajax({
                url: "user/userMessageServlet",
                type: "post",
                async: "true",
                data: {"action": "restoreMymsg", "msgid": msgid},
                dataType: "json",
                success: function (data) {
                    if (data.res == 1) {
                        alert("恢复成功");
                        window.location.replace("<%=basePath%>user/mymsg.jsp");
                    }
                    else {
                        alert(data.info);
                    }
                }
            });
        }

        function getMyMsg() {
            // ajax 异步获取我的问题
            $.get("user/userMessageServlet",
                {
                    "action": "getMyMsg",
                    "pageNum": pageNum
                },
                function (data) {

                    if (data.res == 1) {
                        $("#kk").text("我的问题-" + data.message.rows + "个");
                        $.each(data.message.data, function (index, element) {
                            var msg = $(".template").clone();//复制模版
                            // var bootswitch=$(".bootstrapswitch").clone();
                            msg.show();//显示
                            msg.removeClass("template");//移除模版
                            // bootswitch.removeClass("bootstrapswitch");
                            // bootswitch.hide();
                            msg.find(".title").text(element.msgtopic);//帖子标题
                            //添加href属性
                            msg.find(".title").attr("href", "<%=basePath%>message.jsp?msgid=" + element.msgid);
                            msg.find(".time").text(element.msgtime);//发帖时间
                            msg.find(".count").text(element.accessCount + " • " + element.replyCount + " • " + element.likecount);//浏览量和回复量
                            msg.find(".edit_btn").attr("onclick", "getMsg(" + element.msgid + ")");
                            msg.find(".delete_btn").attr("onclick", "deleteMymsg(" + element.msgid + ",'" + element.msgtopic + "')");
                            msg.find(".restore_btn").attr("onclick", "restoreMymsg(" + element.msgid + ")");
                            msg.find(".allowReply input").attr("class", "bootstrapswitch");
                            msg.find(".bootstrapswitch").attr("value", element.msgid);
                            if(element.replyident==0){
                                msg.find(".bootstrapswitch").attr("checked","checked");
                            }
                            bootstrapswitch();
                            if (element.state == 3) {
                                msg.find(".edit_btn").hide();
                                msg.find(".delete_btn").hide();
                                msg.find(".restore_btn").show();
                            } else if (element.state == -1) {
                                msg.find(".edit_btn").hide();
                                msg.find(".delete_btn").hide();
                                msg.find(".restore_btn").hide();
                            }

                            else {
                                msg.find(".edit_btn").show();
                                msg.find(".delete_btn").show();
                                msg.find(".restore_btn").hide();
                            }
                            $(".list").append(msg);//将帖子信息添加到list中
                        });
                        //加载更多
                        pageNum++;
                        bootstrapswitch();
                        if (parseInt(data.message.totalPage) >= parseInt(pageNum)) {

                            $("#loadmore").html("加载更多...");
                            $("#loadmore").removeAttr("disabled");
                        } else {
                            $("#loadmore").html("没有更多数据了");
                            $("#loadmore").attr("disabled", "disabled");
                        }
                    }

                }, "json");
        }

        window.onscroll = function () {

            var distance = document.documentElement.scrollTop || document.body.scrollTop; //距离页面顶部的距离

            if (distance >= 300) { //当距离顶部超过300px时，显示图片
                document.getElementById('to_top').style.display = "";
            } else { //距离顶部小于300px，隐藏图片
                document.getElementById('to_top').style.display = "none";
            }

            var toTop = document.getElementById("to_top"); //获取图片所在的div

            toTop.onclick = function () { //点击图片时触发的点击事件
                document.documentElement.scrollTop = document.body.scrollTop = 0; //页面移动到顶部
            }
        }

        //模态框修改自己的问题
        function getMsg(msgid) {
            // ajax 异步修改自己的问题
            $.ajax({
                url: "user/userMessageServlet",
                type: "post",
                data: {"action": "getMsg", "msgid": msgid},
                dataType: "json",
                success: function (data) {
                    if (data.res == 1) {
                        alert(data.info);
                        window.location.replace("<%=basePath%>user/editmsg.jsp");
                    }
                    else {
                        alert(data.info);
                    }
                }
            });
            return false;
        }
        var websocket = null;
        var wsUrl = "ws://<%=basePath2%>/websocket";
        var lockReconnect = false;  //避免ws重复连接
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
            setTimeout(function () {     //没连接上会一直重连，设置延迟避免请求过多
                createWebSocket(wsUrl);
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
    </script>
</head>
<body>
<jsp:include flush="fasle" page="../header.jsp"/>
<div id="to_top" title="返回顶部">
    <img src="../images/top.png" width="40" height="40"/>
</div>
<div class="container">
    <br>
    <div class="row">
        <div class="col-sm-12 msgtitle"><h3 id="kk">我的问题</h3></div>
    </div>
    <div class="row">
        <div class="col-sm-6 col-xs-8"><h4>标题</h4></div>
        <div class="col-sm-2 col-xs-4 text-center"><h4>时间</h4></div>
        <div class="col-sm-2 hidden-xs text-center"><h4>浏览 • 回复 • 点赞</h4></div>
        <div class="col-sm-2 col-xs-4 text-center"><h4>是否开启回复</h4></div>
    </div>
    <div class="row msglist template">
        <div class="col-sm-12">
            <div class="col-sm-6 col-xs-8 text-limit">
                <a class="title">标题标题标题标题标题标题</a>
            </div>
            <div class="col-sm-2  col-xs-4 text-center time">时间</div>
            <div class="col-sm-2 hidden-xs text-center count">浏览/回复</div>
            <div class="col-sm-2 col-xs-4 text-center allowReply">
                <%--<input type="checkbox" class="bootstrapswitch" name="bootstrapswitch" checked>--%>
                    <input type="checkbox" name="bootstrapswitch">
                <%--<button id="allowReply" class="btn btn-danger btn-sm delete_btn">不允许回复</button>--%>
            </div>
            <button class="btn btn-primary btn-sm edit_btn"><span class="glyphicon glyphicon-pencil">编辑</span></button>
            <button class="btn btn-danger btn-sm delete_btn"><span class="glyphicon glyphicon-trash">删除</span></button>
            <button class="btn btn-warning restore_btn">恢复</button>
        </div>
    </div>
    <div class="list">

    </div>

    <div class="row p">
        <div class="col-sm-12">
            <br/>
            <button id="loadmore" disabled="disabled" type="button" class="btn btn-default btn-lg btn-block"
                    onclick="javascript:getMyMsg();">加载更多...
            </button>
        </div>
    </div>
</div>
<span id="message"></span>
<jsp:include flush="fasle" page="../footer.jsp"/>
</body>
</html>