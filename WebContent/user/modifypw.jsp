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
<link rel="stylesheet" href="http://love.lidiwen.club/bootstrap.min.css">
<link rel="stylesheet" href="bootstrapvalidator/css/bootstrapValidator.css">
<link rel="stylesheet" href="css/site.css">
<script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js" type="text/javascript"></script>
<script src="https://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js" type="text/javascript"></script>
<!-- 表单验证 -->
<script src="http://love.lidiwen.club/bootstrapValidator.min.js" type="text/javascript"></script>
<title>爱之家网站答疑平台</title>
<script type="text/javascript">
    $(function(){
        validateForm();
    });
	
	function validateForm(){
		// 验证表单
		$("#modifyform").bootstrapValidator({
		 	message: 'This value is not valid',
            feedbackIcons: {/*输入框不同状态，显示图片的样式*/
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
            	 oldpassword: {
                     message:'密码无效',
                     validators: {
                         notEmpty: {
                             message: '旧密码不能为空'
                         },
                         regexp: {
                         	regexp: /^[a-zA-Z0-9_\.]+$/,
                         	message: '旧密码不合法, 请重新输入'
                         },
                         stringLength: {
                             min: 6,
                             max: 30,
                             message: '旧密码长度必须在6到30之间'
                         }
                     }
                 },
                 newpassword:{
                	 message:'密码无效',
                     validators: {
                         notEmpty: {
                             message: '新密码不能为空'
                         },
                         regexp: {
                         	regexp: /^[a-zA-Z0-9_\.]+$/,
                         	message: '新密码不合法, 请重新输入'
                         },
                         stringLength: {
                             min: 6,
                             max: 30,
                             message: '新密码长度必须在6到30之间'
                         }
                     }
                 },
                newpassword2:{
                   	messaage: 'The two password must be consistent',
                  	validators : {
                  		notEmpty: {
                         		message: '确认密码不能为空'
                      	},
                        identical: {
                          	field: 'newpassword',
                          	message: '两次密码必须一致'
                        }
                  	}
                }
            }
         });      
	}
	
	function updatePW(){
		// ajax 异步请求修改密码
		$.ajax({
			url:"user/userCenterServlet?action=updatePw",
			type : "POST",
			async : "true",
			data : $("#modifyform").serialize(),
			dataType : "json",
			success : function(data) {
				if(data.res==1){
                    alert(data.info);
					window.location.replace("index.jsp");
				}else if(data.res==2){
                    alert(data.info);
                    $(".text-warning").text("尊敬的用户:你输入的旧密码跟你原始注册的密码不一致，请重新输入！");
                    $("input[name='oldpassword']").val("");
				}else if(data.res==3){
                    alert(data.info);
                    $(".text-warning").text("尊敬的用户:新密码长度必须在6到30之间，请重新输入！");
                    $("input[name='newpassword']").val("");
                }else if(data.res==4){
                    alert(data.info);
                    $(".text-warning").text("尊敬的用户:确认密码长度必须在6到30之间且要跟新密码一致，请重新输入！");
                    $("input[name='newpassword2']").val("");
                }else if(data.res==5){
                    alert(data.info);
                    $(".text-warning").text("尊敬的用户：你输入的确认密码跟新密码不匹配，请仔细核对确认密码后再输入！");
                    $("input[name='newpassword2']").val("");
                }else if(data.res==9){
                    alert(data.info);
                    $(".text-warning").text("尊敬的用户:旧密码的长度必须在6到30之间，请仔细核对后重新输入！");
                    $("input[name='oldpassword']").val("");
                }else if(data.res==10){
                    alert(data.info);
                    $(".text-warning").text("尊敬的用户:你输入的新密码不合法，不能包含特殊字符和空格，请重新输入！");
                    $("input[name='newpassword']").val("");
                }
				else {
                    alert("修改失败！");
					$(".text-warning").text("修改失败！");
			 		$("input[name='oldpassword']").val("");
					$("input[name='newpassword']").val("");
					$("input[name='newpassword2']").val("");
				}
			}
		});
		return false;
	}
         function gg() {
        var bootstrapValidator=$("#modifyform").data("bootstrapValidator");
        //触发验证
        bootstrapValidator.validate();
        //如果验证通过，则调用login方法
        if(bootstrapValidator.isValid()){
            updatePW();
        }
    }
</script>
</head>
<body>
	<jsp:include flush="true" page="../header.jsp" />
	<div class="container">		
		<div class="row">
			<div class="col-sm-offset-3 col-sm-6 text-center">
				<h3>修改密码</h3>
			</div>
		</div>
		<form class="form-horizontal col-sm-offset-3" id="modifyform" method="post">
			
			<div class="form-group">
				<label for="oldpassword" class="col-sm-2 control-label">旧密码：</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" name="oldpassword" placeholder="请输入旧密码">
				</div>
			</div>
			<div class="form-group">
				<label for="newpassword" class="col-sm-2 control-label">新密码：</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" name="newpassword" placeholder="请输入新密码">
				</div>
			</div>
			<div class="form-group">
				<label for="newpassword2" class="col-sm-2 control-label">确认密码：</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" name="newpassword2" placeholder="请确认新密码">
				</div>
			</div>
			<div class="form-group has-error">
				<div class="col-sm-offset-2 col-sm-4 col-xs-6 ">
					<span class="text-warning" style="color: #a94442"></span>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-4 col-xs-12">
					<button type="button" class="btn btn-success btn-block" onclick="gg();">提交</button>
				</div>
			</div>
		</form>
	</div>
	<jsp:include flush="true" page="../footer.jsp" />
</body>
</html>