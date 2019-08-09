﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<link rel="stylesheet" href="bootstrapvalidator/css/bootstrapValidator.css">
	<link rel="stylesheet" href="css/site.css">
<script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js" type="text/javascript"></script>
<script src="https://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js" type="text/javascript"></script>
<!-- 表单验证 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-validator/0.5.3/js/bootstrapValidator.min.js" type="text/javascript"></script>
<title>爱之家网站管理后台</title>
<script type="text/javascript">
	$(function(){	
		// 验证表单
        $("#loginform").bootstrapValidator({
		 	message: 'This value is not valid',
            feedbackIcons: {/*输入框不同状态，显示图片的样式*/
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {/*验证*/
                username: {/*键名username和input name值对应*/
                    message: 'The username is not valid',
                    validators: {
                        notEmpty: {/*非空提示*/
                            message: '用户名不能为空'
                        },
                        regexp: {
                        	regexp: /^[a-zA-Z0-9_\.]+$/,
                        	message: '用户名不合法, 请重新输入'
                        },
                        stringLength: {/*长度提示*/
                            min: 6,
                            max: 30,
                            message: '用户名长度必须在6到30之间'
                        }/*最后一个没有逗号*/
                        
                    }
                },
                password: {
                    message:'密码无效',
                    validators: {
                        notEmpty: {
                            message: '密码不能为空'
                        },
                        regexp: {
                        	regexp: /^[a-zA-Z0-9_\.]+$/,
                        	message: '密码不合法, 请重新输入'
                        },
                        stringLength: {
                            min: 6,
                            max: 30,
                            message: '密码长度必须在6到30之间'
                        }
                    }
                }
            }
        });
	});
//         .on("success.form.bv",function(e){// submit 按钮
    //        	e.preventDefault();//忽略表单默认提价方式    当点击提交按钮时阻止对表单的提交
    //        	//调用ajax方法
    //    			login();
    //        });
		//普通按钮提交表单
// 	$("#btnLogin").on("click",function(){
//	 //获取validator对象
//	 var bootstrapValidator=$("#loginform").data("bootstrapValidator");
//	 //触发验证
//	 bootstrapValidator.validate();
//	 //如果验证通过，则调用login方法
//	 if(bootstrapValidator.isValid()){
//	     login();
//	 }
//	 });
    function login(){
 	// Ajax 异步请求登录
		var username = $("input[name='username']").val();
		var password = $("input[name='password']").val();
		$.ajax({
			url:"adminServlet",
		    type:"post",
		    async:true,
		    data:{
		    	"action":"login",
                 "username":$("input[name='username']").val(),			    	
			         "password":$("input[name='password']").val(),
		    },
		    dataType:"json",
		    success:function(data){
		    	if(data.res==1){//登陆成功
		    		alert(data.info);
		    	window.location.replace("<%=basePath%>admin/index.jsp");//window.location.replace替换的地址前缀为 http://localhost:8080/muke
		    	}else{//用户名密码错误
		    		alert(data.info);
					 $(".text-warning").text(data.info);
		    	}
		    }
		}); 
	}
	function gg() {
        var bootstrapValidator=$("#loginform").data("bootstrapValidator");
        //触发验证
        bootstrapValidator.validate();
        //如果验证通过，则调用login方法
        if(bootstrapValidator.isValid()){
            login();
        }
    }
    //用户敲下回车触发登录按钮点击
	$(document).keydown(function (event) {
         if (event.keyCode == 13) {
         $("#btnLogin").click();
        }
     });
</script>
</head>
<body>
	<jsp:include flush="fasle" page="header.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-sm-offset-3 col-sm-6 text-center">
				<h3>管理员登录</h3>
			</div>
		</div>
		<%
			String username = "";
			String password = "";
			//获取当前站点的所有Cookie
			Cookie[] cookies = request.getCookies();
			for (int i = 0; i < cookies.length; i++) {//对cookies中的数据进行遍历，找到用户名、密码的数据
				if ("username".equals(cookies[i].getName())) {
					username = cookies[i].getValue();
				} else if ("password".equals(cookies[i].getName())) {
					password = cookies[i].getValue();
				}
			}
		%>
		<form class="form-horizontal col-sm-offset-3" id="loginform" method="post">
			<div class="form-group">
				<label for="username" class="col-sm-2 control-label">账号：</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" name="username" placeholder="请输入账号" id="username" value="<%=username%>">
				</div>
			</div>
			<div class="form-group">
				<label for="password" class="col-sm-2 control-label">密码：</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" name="password" placeholder="请输入密码" id="password" value="<%=password%>">
				</div>
			</div>
			<div class="form-group has-error">
				<div class="col-sm-offset-2 col-sm-4 col-xs-6 ">
					<span class="text-warning"></span>
					<p><label for="rememberme"><input name="rememberme" type="checkbox" id="rememberme" value="1" style="cursor:pointer;" checked /> 记住密码</label></p>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-2 col-xs-6">
				  <button type="button" id="btnLogin"  class="btn btn-success btn-block" onclick="gg()">登录</button>
				</div>
				
			</div>
		</form>
	</div>
	<jsp:include flush="fasle" page="footer.jsp" />
</body>
</html>