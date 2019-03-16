<%@ page import="com.muke.util.SessionCountUtil" %>
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
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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

</script>
	<script>(function(T,h,i,n,k,P,a,g,e){g=function(){P=h.createElement(i);a=h.getElementsByTagName(i)[0];P.src=k;P.charset="utf-8";P.async=1;a.parentNode.insertBefore(P,a)};T["ThinkPageWeatherWidgetObject"]=n;T[n]||(T[n]=function(){(T[n].q=T[n].q||[]).push(arguments)});T[n].l=+new Date();if(T.attachEvent){T.attachEvent("onload",g)}else{T.addEventListener("load",g,false)}}(window,document,"script","tpwidget","//widget.seniverse.com/widget/chameleon.js"))</script>
	<script>tpwidget("init", {
        "flavor": "bubble",
        "location": "WX4FBXXFKE4F",
        "geolocation": "enabled",
        "position": "top-right",
        "margin": "50px 10px",
        "language": "zh-chs",
        "unit": "c",
        "theme": "chameleon",
        "uid": "U19A9ABF8C",
        "hash": "21890f52f22e7df031617e096c53cb98"
    });
    tpwidget("show");</script>
	<script src="js/baidu_statistics.js" type="text/javascript"></script>
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
		<a class="navbar-brand" href="">爱之家-当前在线人数为：<%=SessionCountUtil.getActiveSessions() %></a>
                <!-- <a href="images/爱你.jpg" class="navbar-brand">黄晓琪我爱你</a> -->
	</div>

	<div class="collapse navbar-collapse" id="example-navbar-collapse">
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
			    	<a class="dropdown-toggle" data-toggle="dropdown" href="#">
			        	<img style="border-radius:50%;vertical-align:middle" title="${sessionScope.user.realname}" alt="${sessionScope.user.realname}" width="40px" height="40px" src="${sessionScope.user.user_img}" name="userimg" id="userimg">
						<span class="caret"></span>
					</a>
			    	<ul class="dropdown-menu">
			     		<li><a href="user/mymsg.jsp">我的问题</a></li>
                        <li><a href="user.jsp?username=${sessionScope.user.username}">个人中心</a></li>
			     		<li><a href="user/center.jsp">个人设置</a></li>
                        <li><a href="user/modifypw.jsp">修改密码</a></li>
						<li><a href="user/userlog.jsp">登陆日志</a></li>
			        	<li><a href="javascript:logout()">退出</a></li>
			      	</ul>
				</li>
			</c:if>
			<li>
				<button type="button" class="btn btn-info btn-search glyphicon glyphicon-search" onclick="getinfo();">搜索</button>
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
</body>
</html>