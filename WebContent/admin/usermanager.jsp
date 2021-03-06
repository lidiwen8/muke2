﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
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
<title>爱之家网站管理后台</title>
<script type="text/javascript">
	var page = 1;
	var username = "";

	$(function(){
		getUser(page);
	});
	
	function getUser(pageNum){
		$.ajax({
			url : "admin/adminUserServlet",
			type : "post",
			async : "true",
			data : {"action" : "getUser", "pageNum" : pageNum, "username" : username},
			dataType : "json",
			success : function(data){
              if(data.res==5){
                    alert(data.info);
                    window.location.replace("admin/login.jsp");
                }
				if (data.res==1){
                                        $("#usermanger").text("用户管理-"+data.data.rows);
					$(".list").html("");
					$.each(data.data.data, function(index, userItem) {
						var user = $(".template").clone();
						user.show();
						user.removeClass("template");
						user.find(".num").text(index+1);
						user.find(".name").text(userItem.username);
                                               	user.find(".name").attr("onclik", "jumpUsercenter("+userItem.username+")");
                                                user.find(".name").css("cursor","pointer");
						user.find(".realname").text(userItem.realname);
						user.find(".name").text(userItem.username);
						user.find(".sex").text(userItem.sex);
						user.find(".hobbys").text(userItem.hobbys);
						user.find(".birthday").text(userItem.birthday);
						user.find(".city").text(userItem.city);
						user.find(".qq").text(userItem.qq);
						user.find(".email").text(userItem.email);
						user.find(".time").text(userItem.createtime);
						
						user.find(".primary").attr("onclick", "deleteUser("+userItem.userid+")");//禁用可以恢复
                        user.find(".delete").attr("onclick", "deleteuser("+userItem.userid+")");//真正的删除
						user.find(".restore").attr("onclick", "restoreUser("+userItem.userid+")");
						if (userItem.state == -1){
							user.find(".primary").hide();
							user.find(".restore").show();
						}
						else {

							user.find(".primary").show();
							user.find(".restore").hide();
						}
						
						$(".list").append(user);
					});
	
					page = setPage(pageNum, data.data.totalPage, "getUser");
										
				}
				else if (data.res==-2){
					alert(data.info);
				}
			}
		});
	}
        function jumpUsercenter(username) {
            window.location.replace("user.jsp?username="+username);
        }
	function deleteUser(userid) {
        if (confirm("确认禁用吗？")) {
            $.ajax({
                url: "admin/adminUserServlet",
                type: "post",
                async: "true",
                data: {"action": "deleteUser", "userid": userid},
                dataType: "json",
                success: function (data) {
                    if (data.res == 1) {
                        alert("禁用成功");
                        getUser(page);
                    }
                    else {
                        alert(data.info);
                    }
                }
            });
        };
    }

	function restoreUser(userid){
		$.ajax({
			url : "admin/adminUserServlet",
			type : "post",
			async : "true",
			data : {"action" : "restoreUser", "userid" : userid},
			dataType : "json",
			success : function(data){
				if (data.res == 1){
					alert ("恢复成功");
					getUser(page);
				}
				else {
					alert(data.info);
				}
			}
		});
	}

    function deleteuser(userid){
        if (confirm("确认删除【"+userid+"】吗？")) {
            $.ajax({
                url: "admin/adminUserServlet",
                type: "post",
                async: "true",
                data: {"action": "delete", "userid": userid},
                dataType: "json",
                success: function (data) {
                    if (data.res == 1) {
                        alert("删除成功");
                        getUser(page);
                    }else if(data.res==2){
                        if (confirm(data.info)) {
                            $.ajax({
                                url: "admin/adminUserServlet",
                                type: "post",
                                async: "true",
                                data: {"action": "cascadelete2", "userid":userid},
                                dataType: "json",
                                success: function (data) {
                                    if (data.res == 1) {
                                        alert(data.info);
                                        getUser(page);
                                    } else {
                                        alert(data.info);
                                    }
                                }
                            });
                        }else {
                            getUser(page);
						}

					}else if(data.res==3){
                        if (confirm(data.info)) {
                            $.ajax({
                                url: "admin/adminUserServlet",
                                type: "post",
                                async: "true",
                                data: {"action": "cascadelete3", "userid":userid},
                                dataType: "json",
                                success: function (data) {
                                    if (data.res == 1) {
                                        alert(data.info);
                                        getUser(page);
                                    } else {
                                        alert(data.info);
                                    }
                                }
                            });
                        }else {
                            getUser(page);
                        }
					}else if(data.res==4){
                        if (confirm(data.info)) {
                            $.ajax({
                                url: "admin/adminUserServlet",
                                type: "post",
                                async: "true",
                                data: {"action": "cascadelete4", "userid":userid},
                                dataType: "json",
                                success: function (data) {
                                    if (data.res == 1) {
                                        alert(data.info);
                                        getUser(page);
                                    } else {
                                        alert(data.info);
                                    }
                                }
                            });
                        }else {
                            getUser(page);
                        }
					}
                    else {
                        alert(data.info);
                    }
                }
            });
        };
    }
	function searchUser(){
		username = $("#username").val();
		
		getUser(1);
	}
	
</script>
</head>
<body>
	<jsp:include flush="fasle" page="header.jsp" />
	<div class="container">		
		<div class="row">
			<div class="col-sm-12 msgtitle">
				<h3 class="pull-left" id="usermanger">用户管理
				</h3>
				<div class="replybtn">
					<button type="button" class="btn btn-success" data-toggle="modal"
							data-target="#search">搜索</button>
				</div>
			</div>
		</div>
		<div class="col-sm-12">
			<table class="table table-striped">
				<thead>
				<th>编号</th>
				<th>用户名</th>
				<th>昵称</th>
				<th>性别</th>
				<th>爱好</th>
				<th>生日</th>
				<th>城市</th>
				<th>QQ</th>
				<th>邮箱</th>
				<th>创建日期</th>
				<th>操作</th>
				</thead>
				<tbody class="list">
				</tbody>
			</table>
		</div>
		<table class="table table-striped">
		<tr class="template">
				<td class="userinfo num">1</td>
				<td class="title name">用户名</td>
				<td class="userinfo realname">昵称</td>
				<td class="userinfo sex">性别</td>
				<td class="userinfo hobbys">爱好</td>
				<td class="userinfo birthday">生日</td>
				<td class="userinfo city">城市</td>
				<td class="userinfo qq">QQ</td>
				<td
					class="userinfo text-limit email tooltip-test"
					data-toggle="tooltip" title="zhuangzhuangzhuang@foxmail.com">zhuangzhuangzhuang@foxmail.com</td>
				<td class=" userinfo time">2017-01-01</td>
			    <td>
					<button class="btn btn-primary primary">禁用</button>
					<button class="btn btn-warning restore">恢复</button>
				</td>
				<td>
					<button class="btn btn-danger delete">删除</button>
				</td>
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
							<label for="username">账号：</label>
							<input type="text" class="form-control" id="username" placeholder="">
						</div>
					</form>
				</div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="searchUser()">搜索</button>
	            </div>
	        </div><!-- /.modal-content -->
	    </div><!-- /.modal -->
	</div>
	<jsp:include flush="fasle" page="footer.jsp" />
</body>
</html>