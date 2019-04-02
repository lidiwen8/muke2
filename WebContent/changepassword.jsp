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
    <script src="jquery/jquery-2.2.4.min.js" type="text/javascript"></script>
    <script src="bootstrap-3.3.7-dist/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- 表单验证 -->
    <script src="bootstrapvalidator/js/bootstrapValidator.js" type="text/javascript"></script>
    <script src="jquery/autoMail.1.0.min.js"></script>
    <title>爱之家网站答疑平台</title>
    <script type="text/javascript">
//       function promot() {
//            $('#mail').autoMail({
//                emails: ['qq.com', '163.com', '126.com', 'sina.com', 'sohu.com','yahoo.com','hotmail.com','188.com']
//            });
//        }
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
                    },
                    mail : {
                        messaage : 'The email is not valid',
                        validators : {
                            notEmpty : {
                                message : '邮箱不能为空'
                            },
                            emailAddress: {
                                message: '邮箱地址格式有误'
                            }
                        }
                    }
                }
            });
        }

        function updatePW(){
            // ajax 异步请求修改密码
            $.ajax({
                url:"userServlet?action=updatePw2",
                type : "POST",
                async : "true",
                data : $("#modifyform").serialize(),
                dataType : "json",
                success : function(data) {
                    if(data.res==-1){
                        alert(data.info);
                        window.location.replace("mailpassword.jsp");
                    }else if(data.res==3){
                        alert(data.info);
//                        $(".text-warning").innerHTML="尊敬的用户:用户账号不能为空，请重新输入！".fontcolor("red");
                        $(".text-warning").text("尊敬的用户:用户账号不能为空，请重新输入！");
                    }else if(data.res==8){
                        alert(data.info);
                        $(".text-warning").text("尊敬的用户:用户账号长度必须在6到30之间且不包含特殊符号与中文，请重新输入！");
                    }else if(data.res==6){
                        alert(data.info);
                        $(".text-warning").text("尊敬的用户:邮箱号输入不能为空，请重新输入！");
                    }else if(data.res==7){
                        alert(data.info);
                        $(".text-warning").text("尊敬的用户:你输入的邮箱格式不正确，请重新输入！");
                    }else if(data.res==4){
                        alert(data.info);
                        $(".text-warning").text("尊敬的用户:新密码长度必须在6到30之间，请重新输入！");
                    }else if(data.res==9){
                        alert(data.info);
                        $(".text-warning").text("尊敬的用户:确认密码长度必须在6到30之间，且要跟新密码一致，请重新输入确认密码！");
                    }else if(data.res==5){
                        alert(data.info);
                        $(".text-warning").text("尊敬的用户:你输入的确认密码跟新密码不匹配，请仔细核对确认密码后再输入！");
                    }else if(data.res==19){
                        alert(data.info);
                        $(".text-warning").text("尊敬的用户:你输入的新密码不合法，不能包含特殊字符和空格，请重新输入！");
                    }else if(data.res==27){
                        alert(data.info);
                        $(".text-warning").text("尊敬的用户:你的该邮箱没有激活，不能通过此方式来更改密码，请激活后再修改！");
                        window.location.replace("bindingmail.jsp");
                    }

                    else {
                        alert(data.info);
                        $(".text-warning").text("你输入的账户或者邮箱账号跟你注册时的信息不匹配，请改正后再提交！");
                        $("input[name='username']").val("");
                        $("input[name='mail']").val("");
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
<body onload="promot()">
<jsp:include flush="fasle" page="header.jsp" />
<div class="container">
    <div class="row">
        <div class="col-sm-offset-3 col-sm-6 text-center">
            <h3>忘记密码</h3>
        </div>
    </div>
    <form class="form-horizontal col-sm-offset-3" id="modifyform" method="post">

        <div class="form-group">
            <label for="username" class="col-sm-2 control-label">账户：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="username" placeholder="请输入你注册时的账户号">
            </div>
        </div>
        <div class="form-group">
            <label for="mail" class="col-sm-2 control-label">邮箱号：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="mail" id="mail" placeholder="请输入你注册时的邮箱号码">
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
                <button type="button" class="btn btn-success btn-block" onclick="window.location.href='mailpassword.jsp'">邮箱找回</button>
            </div>
        </div>
    </form>
</div>
<jsp:include flush="fasle" page="footer.jsp" />
</body>
</html>