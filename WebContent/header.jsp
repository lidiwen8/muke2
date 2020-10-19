<%@ page import="com.muke.util.SessionCountUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath2 = request.getServerName() + ":" + request.getServerPort() + path;
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="keywords" content="lidiwen, 爱之家, 答疑平台, 互动平台，发帖平台">
	<meta name="Description" content="爱之家，爱之家网站是为了方便用户通过输入自己的相关信息来获取想要信息，主要提供一些基本的查询，爱之家网站的爱情宣言是 我爱黄晓琪 --(李弟文)。">
	<base href="<%=basePath%>">
	<title>头部</title>
	<script type="text/javascript">
		function logout(){
			// Ajax 异步请求退出登录
			$.ajax({
				url:"userServlet?action=logout",
				type : "POST",
				async : "true",
				dataType : "json",
				success : function(data) {
					if (data.res == 1){
						alert("欢迎下次登录！");
						window.location.replace("login.jsp");
					}
				}
			});
		}
		function getTheme2() {
			$.ajax({
				url: "messageServlet",
				type: "POST",
				async: "true",
				data: {"action": "getAllTheme"},
				dataType: "json",
				success: function (data) {
					$("#theid").append($("<option value='-1'>未选择</option>"));
					$.each(data.theme, function (index, themeItem) {
						$("#theid").append($("<option value='" + themeItem.theid + "'>" + themeItem.thename + "</option>"));
					});
				}
			});
		}
		function getinfo() {
			getTheme2();
			$('#search').modal('show');
			$('#search').on('shown.bs.modal', function (e) {
				$("#key").focus();//获取焦点
			});
		}
		function searchMsg() {
			var key = $("#key").val();
			var username = $("#builder").val();
			var theid = $("#theid").val();
			window.location.href="searchmsg.jsp?key="+key+"&username="+username+"&theid="+theid;
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
					// setMessageInnerHTML(event.data);
					if(event.data=="deleteUser"+$("#userid").val()){
						var messageVary = confirm("最新通知：你的当前账号已被管理员禁用！");
						if (messageVary == true||messageVary==false) {
							logout2();
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
		function logout2() {
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
	<script>
		(function(a,h,g,f,e,d,c,b){b=function(){d=h.createElement(g);c=h.getElementsByTagName(g)[0];d.src=e;d.charset="utf-8";d.async=1;c.parentNode.insertBefore(d,c)};a["SeniverseWeatherWidgetObject"]=f;a[f]||(a[f]=function(){(a[f].q=a[f].q||[]).push(arguments)});a[f].l=+new Date();if(a.attachEvent){a.attachEvent("onload",b)}else{a.addEventListener("load",b,false)}}(window,document,"script","SeniverseWeatherWidget","//cdn.sencdn.com/widget2/static/js/bundle.js?t="+parseInt((new Date().getTime() / 100000000).toString(),10)));
		window.SeniverseWeatherWidget('show', {
			flavor: "bubble",
			location: "WM6N2PM3WY2K",
			geolocation: true,
			language: "zh-Hans",
			unit: "c",
			theme: "auto",
			token: "058f35bc-18a0-4a03-ab84-62cbcb75d799",
			hover: "enabled",
			container: "tp-weather-widget"
		})
	</script>
	<script src="js/baidu_statistics.js" type="text/javascript"></script>
	<script src="http://love.lidiwen.club/bootstrap-hover-dropdown.min.js"></script>
</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
	<div class="container">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
						data-target="#example-navbar-collapse">
					<span class="sr-only">切换导航</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="">爱之家-当前在线人数：<%=SessionCountUtil.getActiveSessions() %></a>
				<!-- <a href="images/爱你.jpg" class="navbar-brand">黄晓琪我爱你</a> -->
			</div>

			<div class="collapse navbar-collapse" id="example-navbar-collapse">
				<div id="tp-weather-widget"></div>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="./user/addmsg.jsp">我要求助</a></li>
					<li><a href="messag.jsp">我要留言</a></li>
					<c:if test="${sessionScope.user == null}">
						<li><a href="login.jsp">登录</a></li>
						<li><a href="register.jsp">注册</a></li>
						<%--<li><a href="#">在线人数为：<%=SessionCountUtil.getActiveSessions() %></a> </li>--%>
					</c:if>
					<c:if test="${sessionScope.user != null}">
						<li class="dropdown">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#" id="dropdownMenu1" data-hover="dropdown">
								<img style="border-radius:50%;vertical-align:middle" title="${sessionScope.user.realname}" alt="${sessionScope.user.realname}" width="40px" height="40px" src="${sessionScope.user.user_img}" name="userimg" id="userimg">
									${sessionScope.user.realname}
								<span class="caret"></span>
							</a>
							<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
								<li role="presentation"><a role="menuitem" tabindex="-1" href="user/mymsg.jsp">我的问题</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1" href="user.jsp?username=${sessionScope.user.username}">个人中心</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1" href="user/center.jsp">个人设置</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1" href="user/modifypw.jsp">修改密码</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1" href="user/userlog.jsp">登陆日志</a></li>
								<li role="presentation"><a role="menuitem" tabindex="-1" href="javascript:logout()">退出</a></li>
							</ul>
						</li>
					</c:if>
					<li>
						<button type="button" class="btn btn-info btn-search glyphicon glyphicon-search" onclick="getinfo();" style="margin-top:8px">搜索</button>
					</li>

				</ul>
			</div>
		</div>
	</div>
</nav>
<div class="jumbotron masthead">
	<div class="container">
		<h1>爱之家网站答疑平台</h1>
		<p>jQuery Ajax Bootstrap</p>
	</div>
</div>
<!-- 模态框（Modal） -->
<div class="modal fade" id="search" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content modalcenter">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="replyLabel">帖子搜索</h4>
			</div>
			<div class="modal-body">
				<form role="form">
					<div class="form-group">
						<label for="key">关键字：</label>
						<input type="text" class="form-control" id="key" placeholder="请输入关键字">
					</div>
					<div class="form-group">
						<label for="builder">楼主姓名：</label>
						<input type="text" class="form-control" id="builder" placeholder="请输入楼主姓名">
					</div>
					<div class="form-group">
						<label for="theid">主题：</label>
						<select class="form-control" id="theid">
						</select>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="searchMsg()">搜索</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
<input type="hidden" id="userid" value="${sessionScope.user.userid}">
</body>
</html>