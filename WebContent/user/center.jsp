<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
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
<link rel="stylesheet" href="bootstrap-3.3.7-dist/css/bootstrap.css">
<link rel="stylesheet" href="bootstrapvalidator/css/bootstrapValidator.css">
<link rel="stylesheet" href="css/site.css">
<link rel="stylesheet" href="bootstrap-datetimepicker/css/bootstrap-datetimepicker.css">
<script src="jquery/jquery-2.2.4.min.js" type="text/javascript"></script>
<script src="bootstrap-3.3.7-dist/js/bootstrap.min.js" type="text/javascript"></script>
<script src="bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
<script src="bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>
<!-- 表单验证 -->
<script src="bootstrapvalidator/js/bootstrapValidator.js" type="text/javascript"></script>
<title>爱之家网站答疑平台</title>
<script type="text/javascript">
	$(function(){
		 $(".form_datetime").datetimepicker({
		        format: 'yyyy-mm-dd',
		        minView:'month',
		        language: 'zh-CN',
		        autoclose: true,//选中自动关闭
		        startDate:'1900-01-01',
		        todayBtn: true//显示今日按钮
		    });
		 
		 getUser();
		 
		 validateForm();
	});
        function getUser(){
        $.ajax({
            url:"userCenterServlet?action=getUser",// 请求地址
            type:"POST",    // 请求类型
            async:"true",   // 是否异步方式
            dataType : "json",
            success:function(data) {
                if(data.res==1){
                    <%--$("img").html('<img width="50" class="emoji" style="vertical-align:middle" src="${user.user_img}">');--%>
                }
            }
    });
		// ajax 异步请求 获取个人信息
		$.get("userCenterServlet",{"action":"getUser"},function(data){
		    alert(data.user_img);
			if(data.res==1){
				var user=data.user;
                <%--$("img").html('<img width="50" class="emoji" style="vertical-align:middle" src="${data.user_img}">');--%>
			}else{
				$(".text-warning").text(data.user);
			}
		},"json");
	}
    function validateForm(){
		// 验证表单
		$("#updateform").bootstrapValidator({
		 	message: 'This value is not valid',
            feedbackIcons: {/*输入框不同状态，显示图片的样式*/
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {/*验证*/
                
                realname:{
                 	messaage: 'The realname is not valid',
                	validators : {
                		notEmpty: {
                       		message: '真实姓名不能为空'
                    	}
                	}
                },
                hobbys:{
                 	messaage: 'The hobbys is not valid',
                	validators : {
                		notEmpty: {
                       		message: '爱好不能为空'
                    	}
                	}
                },
//                city:{
//                 	messaage: 'The city is not valid',
//                	validators : {
//                		notEmpty: {
//                       		message: '城市不能为空'
//                    	}
//                	}
//                },
                email:{
                 	messaage: 'The email is not valid',
                	validators : {
                		notEmpty: {
                       		message: '邮箱不能为空'
                    	}
                     
                	}
                },
                qq:{
                 	messaage: 'The QQ is not valid',
                	validators : {
                		notEmpty: {
                       		message: 'QQ不能为空'
                    	}
                	}
                }
            }
        });
	}
	function update(){
		// ajax 异步请求Servlet 更新个人信息
		var formData=$("#updateform").serialize();
		$.ajax({
			url:"userCenterServlet?action=update",// 请求地址
			type:"POST",    // 请求类型
			async:"true",   // 是否异步方式
			data:formData,  // 表单的序列化
			dataType : "json",
			success:function(data){
				if(data.res==1){
					alert("更新成功！");
					window.location.replace("index.jsp");
				}else if(data.res == 5) {
                    alert(data.info);
                    $(".text-warning").text("尊敬的用户:你输入的邮箱格式不正确，请检查无误后重新输入！");
                }else if(data.res == 6) {
                    alert(data.info);
                    $(".text-warning").text("尊敬的用户:你输入的QQ格式不正确，请检查无误后重新输入！");
                } else if(data.res == 17){
                    alert(data.info);
                    $(".text-warning").text("尊敬的用户:为了你更好的交友,爱好不能为空，请至少勾选一个爱好！");
                }else if(data.res == 8){
                    alert(data.info);
                    $(".text-warning").text("尊敬的用户:为了保证你的合法性，修改个人资料时真实姓名不能为空，请如实填写！");
                }else if(data.res == 14){
                    alert(data.info);
                    $(".text-warning").text("尊敬的用户:为了保证你的合法性，修改个人资料时邮箱号输入不能为空，请重新输入！");
                }else if(data.res == 16){
                    alert(data.info);
                    $(".text-warning").text("尊敬的用户:为了保证你的合法性，修改个人资料时qq号输入不能为空，请重新输入！");
                }else if(data.res == 19){
                    alert(data.info);
                    $(".text-warning").text("尊敬的用户:为了保证你的合法性，修改个人资料时所在城市的选择不能为空，请点击城市下拉列表选择你所在的城市！");
                }else if(data.res == 20){
                    alert(data.info);
                    $(".text-warning").text("你输入的邮箱号已经被注册了!修改失败，请换一个邮箱呗！");
                    $("input[name='email']").val("");
                }

				else {
					$(".text-warning").text("更新失败！");
			 		$("input[name='realname']").val("");
					$("input[name='hobbys']").val("");
					$("input[name='birthday']").val("");
					$("input[name='city']").val("");
					$("input[name='email']").val("");
					$("input[name='qq']").val(""); 
				}
			}
		});
		return false;
	}

</script>
</head>
<body>
	<jsp:include flush="fasle" page="../header.jsp" />
	<div class="container">
		<div class="row">
			<div class="col-sm-offset-3 col-sm-6 text-center">
				<h3>修改资料</h3>
			</div>
		</div>
		<form class="form-horizontal col-sm-offset-3" id="updateform" method="post">
			<div class="form-group">
				<label for="realname" class="col-sm-2 control-label">头像：</label>
				<div class="col-sm-4">
					<img style="width:80px;height:80px" src="${user.user_img}">
					<br><br>
					<button class="btn btn-danger btn-sm delete_btn" onclick="javascript:window.location.href='user/upload.jsp'">修改头像</button>
				</div>
			</div>
			<div class="form-group">
				<label for="realname" class="col-sm-2 control-label">真实姓名：</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" name="realname" placeholder="请输入真实姓名" value="${user.realname}">
				</div>
			</div>
			<div class="form-group">
				<label for="sex" class="col-sm-2 control-label">性别：</label>
				<div class="col-sm-4">
					<label class="radio-inline">
				        <input type="radio" name="sex" value="男" <c:if test="${user.sex=='男' }">checked="checked"</c:if>/ > 男
					</label>
				    <label class="radio-inline">
				        <input type="radio" name="sex" value="女" <c:if test="${user.sex=='女' }">checked="checked"</c:if>/> 女
				    </label>
				</div>
			</div>
			<div class="form-group">
				<label for="hobbys" class="col-sm-2 control-label">爱好：</label>
				<div class="col-sm-4">
					<label class="checkbox-inline">
				        <input type="checkbox" name="hobbys" value="吃饭" checked>吃饭
				    </label>
				    <label class="checkbox-inline">
				        <input type="checkbox" name="hobbys" value="睡觉">睡觉
				    </label>
				    <label class="checkbox-inline">
				        <input type="checkbox" name="hobbys" value="打豆豆">打豆豆
				    </label>
				</div>
			</div>
			
			<div class="form-group">
				<label for="birthday" class="col-sm-2 control-label">生日：</label>
				<div class="col-sm-4">
					<div class="input-group date form_datetime" data-date-format="dd-MM-yyyy" data-link-field="dtp_input1">
	                    <input class="form-control" size="16" type="text" name="birthday" value="${user.birthday}" readonly>
	                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
						<span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
	                </div>
                </div>
			</div>
			<div class="form-group">
				<label for="city" class="col-sm-2 control-label">城市：</label>
				<div class="col-sm-4">
					<select id="province" size=1 >
					</select>
					<select id="city" >
					</select>
					<br>
					<input type="text" class="form-control" name="city" id="pro_city" readonly="readonly" placeholder="请选择自己所在的城市" value="${user.city}">
					<script src="http://www.lidiwen.club/muke_Web/jquery/city2.js" type="text/javascript"></script>
				</div>
			</div>
			<div class="form-group">
				<label for="email" class="col-sm-2 control-label">邮箱：</label>
				<div class="col-sm-4">
					<input type="email" class="form-control" name="email" placeholder="请输入邮箱" value="${user.email}">
				</div>
			</div>
			<div class="form-group">
				<label for="qq" class="col-sm-2 control-label">QQ：</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" name="qq" placeholder="请输入QQ" value="${user.qq}">
				</div>
			</div>
			<div class="form-group has-error">
				<div class="col-sm-offset-2 col-sm-4 col-xs-6 ">
					<span class="text-warning" style="color: #a94442"></span>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-4 col-xs-12">
					<button class="btn btn-success btn-block" onclick="update();">提交</button>
				</div>
			</div>
		</form>
	</div>
	<jsp:include flush="fasle" page="../footer.jsp" />
</body>
</html>