<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><%  
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
String basePath2 = request.getServerName() + ":" + request.getServerPort() + path;
%>  
<!DOCTYPE>
<html>
<head>
<base href="<%=basePath%>">  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="http://love.lidiwen.club/bootstrap.min.css">
	<script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="jquery/emoji.js"></script>
	<link href="http://love.lidiwen.club/monokai_sublime.min.css" rel="stylesheet">
	<script src="http://love.lidiwen.club/highlight.min.js"></script>
        <script src="http://love.lidiwen.club/bootstrap.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="css/site.css">
<title>爱之家网站答疑平台</title>
<script type="text/javascript">

	var pageNum = 1;

	function loadscript(url, callback) {
		var script = document.createElement("script")
		script.type = "text/javascript";
		if (script.readyState) {
			script.onreadystatechange = function () {
				if (script.readyState == "loaded" || script.readyState == "complete") {
					script.onreadystatechange = null;
					callback();
				}
			};
		} else {
			script.onload = function () {
				callback();
			};
		}
		script.src = url;
		document.getElementsByTagName("head")[0].appendChild(script);
	}

	function getMsg(){
		// Ajax 获取最新的问题
		$.ajax({
			url : "messageServlet", 
			type : "POST",
			async : "true",
			data : {"action" : "topNew", "pageNum": pageNum},
			dataType : "json",
			success : function(data) {
                $('.emoji').emoji();
				if (data.res==1){
					$.each(data.newMsg.data, function(index, msgItem) {
						var msg = $(".template").clone();
						msg.show();
						msg.removeClass("template");
                        $('.emoji').emoji();
						msg.find(".msg-title").text(msgItem.msgtopic);
						msg.find(".msg-title").attr("href", "message.jsp?msgid="+msgItem.msgid);
						msg.find(".author").text(msgItem.realname+" •  "+msgItem.msgtime);
                        $('.emoji').emoji();
						msg.find(".msgcontent").html(msgItem.msgcontents);
						hljs.initHighlightingOnLoad();
                        $('.emoji').emoji();
						msg.find(".count").text(msgItem.accessCount+"次浏览 • "+msgItem.replyCount+"个回复 • "+msgItem.likecount+"个赞 • ");
						msg.find(".msglink").attr("href", "message.jsp?msgid="+msgItem.msgid);
						
						$("#msgList").append(msg);
					});
                    $('.emoji').emoji();
					pageNum++;
					// 加载更多
					if (pageNum > 1) {
						loadscript("https://cdn.bootcss.com/highlight.js/8.0/highlight.min.js", function () {
							hljs.initHighlighting();
						});
					} else {
						hljs.initHighlightingOnLoad();
					}
					if (parseInt(data.newMsg.totalPage) >= parseInt(pageNum)){
						$("#loadmore").html("加载更多...");
						$("#loadmore").removeAttr("disabled");
                        $('.emoji').emoji();
					}
					else{
						$("#loadmore").html("没有更多数据了！");
						$("#loadmore").attr("disabled","disabled");
					}
				} 
			}

		});
	}
	$(function(){
		// 加载数据
        $('.emoji').emoji();
		getMsg();
	});
  window.onscroll = function(){

  var distance = document.documentElement.scrollTop || document.body.scrollTop; //距离页面顶部的距离

  if( distance >= 300 ) { //当距离顶部超过300px时，显示图片
    document.getElementById('to_top').style.display = "";
  } else { //距离顶部小于300px，隐藏图片
    document.getElementById('to_top').style.display = "none";
  }

  var toTop = document.getElementById("to_top"); //获取图片所在的div

  toTop.onclick = function(){ //点击图片时触发的点击事件
    document.documentElement.scrollTop = document.body.scrollTop = 0; //页面移动到顶部
  }
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
</script>
</head>
<body>
	<jsp:include flush="true" page="header.jsp" />
           <div id="to_top" title="返回顶部">
            <img src="images/top.png" width="40" height="40" />
           </div>
	<div class="container">		
		<div class="row" id="msgList">
			<div class="col-sm-12 msgitem template">
				<div class="emoji">
				<h3><a class="msg-title"></a></h3>
				<p class="author"></p>
				<p class="msgcontent"></p>
					<script type="text/javascript">
                        $('.emoji').emoji();
					</script>
				</div>
				<div class="rightinfo">
					<span class="count"></span>
					<a class="msglink">详细&gt;&gt;</a>
				</div>
			</div>
			<!-- <div class="col-sm-12 msgitem" style="display: block;">
				<h3><a class="msg-title" href="#">不错哦221133</a></h3>
				<p class="author">哈哈 •  8/29</p>
				<p class="msgcontent">六个字六个字</p>
				<div class="rightinfo">
					<span class="count">11次浏览 • 0个回复 • </span>
					<a class="msglink" href="#">详细&gt;&gt;</a>
				</div>
			</div>
			<div class="col-sm-12 msgitem" style="display: block;">
				<h3><a class="msg-title" href="#">网站Bug请在此留下</a></h3>
				<p class="author">冰城小象 •  8/28</p>
				<p class="msgcontent">网站Bug请在此留下</p>
				<div class="rightinfo">
					<span class="count">4次浏览 • 0个回复 • </span>
					<a class="msglink" href="#">详细&gt;&gt;</a>
				</div>
			</div>
			<div class="col-sm-12 msgitem" style="display: block;">
				<h3><a class="msg-title" href="#">如何完全卸载MySQL数据库</a></h3>
				<p class="author">冰城小象 •  8/28</p>
				<p class="msgcontent">1.&nbsp;控制面板——》所有控制面板项——》程序和功能，卸载mysql&nbsp;server!</p>
				<div class="rightinfo">
					<span class="count">9次浏览 • 5个回复 • </span>
					<a class="msglink" href="#">详细&gt;&gt;</a>
				</div>
			</div>
			<div class="col-sm-12 msgitem" style="display: block;">
				<h3><a class="msg-title" href="#">盒子模型是怎么回事?</a></h3>
				<p class="author">冰城小象 •  8/28</p>
				<p class="msgcontent">盒子模型的四要素分别是：<br>content(内容)、<br>border(边框)、<br>padding(内边距)、<br>margin(外边距)，</p>
				<div class="rightinfo">
					<span class="count">3次浏览 • 1个回复 • </span>
					<a class="msglink" href="#">详细&gt;&gt;</a>
				</div>
			</div>
			<div class="col-sm-12 msgitem" style="display: block;">
				<h3><a class="msg-title" href="#">JDK配置环境变量</a></h3>
				<p class="author">冰城小象 •  8/28</p>
				<p class="msgcontent">如题:</p>
				<div class="rightinfo">
					<span class="count">8次浏览 • 5个回复 • </span>
					<a class="msglink" href="#">详细&gt;&gt;</a>
				</div>
			</div> -->
		</div>
		<div class="row">
			<div class="col-sm-12">
				<br/>
				<button id="loadmore" type="button" class="btn btn-default btn-lg btn-block" 
				onclick="javascript:getMsg();">加载更多...</button>
			</div>
		</div>
	</div>
        <span id="message"></span>
	<jsp:include flush="true" page="footer.jsp" />
</body>
</html>