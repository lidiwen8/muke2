<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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
              function promot() {
                   $('#mail').autoMail({
                       emails: ['qq.com', '163.com', '126.com', 'sina.com', 'sohu.com','yahoo.com','hotmail.com','188.com']
                   });
               }
        $(function () {
            validateForm();
        });

        function validateForm() {
            // 验证表单
            $("#modifyform").bootstrapValidator({
                message: 'This value is not valid',
                feedbackIcons: {
                    /*输入框不同状态，显示图片的样式*/
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    mail: {
                        messaage: 'The email is not valid',
                        validators: {
                            notEmpty: {
                                message: '邮箱不能为空'
                            },
                            emailAddress: {
                                message: '邮箱地址格式有误'
                            }
                        }
                    },
                    code: {
                        /*键名username和input name值对应*/
                        message: 'The code is not valid',
                        messaage: 'The validate is not valid',
                        validators: {
                            notEmpty: {
                                message: '验证码不能为空'
                            },
                            stringLength: {
                                /*长度提示*/
                                min: 6,
                                max: 6,
                                message: '验证码长度必须为6位'
                            }/*最后一个没有逗号*/
                        }
                    },

                    newpassword: {
                        message: '密码无效',
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
                    newpassword2: {
                        messaage: 'The two password must be consistent',
                        validators: {
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

        function apper(info) {
            $("#notice").text(info);
            $("#notice").show();
            setTimeout(function () {
                $("#notice").hide();
            }, 2000);
        }

        var InterValObj; //timer变量，控制时间
        var count = 50; //间隔函数，1秒执行
        var curCount;//当前剩余秒数
        function send(){
            var mail = $("input[name='mail']").val();
            if(mail==""){
                alert("请输入邮箱");
                apper("请输入邮箱");
            }else if(!mail.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/)){
                alert("请输入正确格式的邮箱");
                apper("无效邮箱");
            }
            else
            {
               sendCode(mail);
               curCount = count;
               $("#verificationss").attr("disabled", "disabled");
               $("#verificationss").text(curCount + "秒后重新获取");
               InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次请求后台发送验证码 TODO
            }
        }

        function sendCode(mail) {
            // ajax 用户异步请求绑定邮箱
            $.ajax({
                url: "userServlet?action=sendResetPassCode",
                type: "POST",
                async: "true",
                data: {"action": "sendResetPassCode", "mail": mail},
                dataType: "json",
                beforeSend: function () {
                    // 禁用按钮防止重复提交，发送前响应
                    // $("#verificationss").attr({disabled: "disabled"});
                    // $('#verificationss').text("验证码正在发送。。");
                },
                success: function (data) {
                    if (data.res != 1) {
                        curCount = 0;
                    }
                    if (data.res == 1) {
                        alert(data.info);
                    }else if(data.res == -2){
                        alert(data.info);
                        window.location.replace("bindingmail.jsp");
                    }
                    else {
                        alert(data.info);
                        apper(data.info);
                    }
                }
            });
            return false;
        }

        //timer处理函数
        function SetRemainTime() {
            if (curCount == 0) {
                window.clearInterval(InterValObj);//停止计时器
                $("#verificationss").removeAttr("disabled");//启用按钮
                $("#verificationss").text("重新获取验证码");
            }
            else {
                curCount--;
                $("#verificationss").text(curCount + "秒后重新获取");
            }
        }

        function updatePW() {
            // ajax 异步请求修改密码
            $.ajax({
                url: "userServlet?action=updatePw2",
                type: "POST",
                async: "true",
                data: $("#modifyform").serialize(),
                dataType: "json",
                success: function (data) {
                    if (data.res == 1) {
                        alert(data.info);
                        window.location.replace("login.jsp");
                    }
                    else {
                        alert(data.info);
                        apper(data.info);
                    }
                }
            });
            return false;
        }

        function gg() {
            var bootstrapValidator = $("#modifyform").data("bootstrapValidator");
            //触发验证
            bootstrapValidator.validate();
            //如果验证通过，则调用login方法
            if (bootstrapValidator.isValid()) {
                updatePW();
            }
        }
    </script>
</head>
<body onload="promot()">
<jsp:include flush="fasle" page="header.jsp"/>
<div class="container">
    <div class="row">
        <div class="col-sm-offset-3 col-sm-6 text-center">
            <h3>重置密码</h3>
        </div>
    </div>
    <form class="form-horizontal col-sm-offset-3" id="modifyform" method="post">
        <div class="form-group">
            <label for="mail" class="col-sm-2 control-label">邮箱号：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="mail" id="mail" placeholder="请输入你注册时的邮箱号码">
            </div>
        </div>
        <div class="form-group">
            <label for="code" class="col-sm-2 control-label">验证码：</label>
            <div class="col-sm-4">
                <input type="number" class="form-control" name="code" id="code" placeholder="请输入邮箱验证码"/>
                <button class="btn btn-success btn-block" onclick="send();" id="verificationss"
                        name="verificationss"
                        style="color: #171719;width: 117px;background-color: #0044cc"><img src="images/youjian.png"
                                                                                           style="width:15px;height:15px;margin-right:5px;margin-bottom:5px;">获取验证码
                </button>
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
                <span class="text-warning" style="color: #a94442"><p id="notice" style="display: none;"></p></span>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-4 col-xs-12">
                <button type="submit" class="btn btn-success btn-block" onclick="gg();">提交</button>
                <button type="button" class="btn btn-success btn-block"
                        onclick="window.location.href='mailpassword.jsp'">邮箱找回
                </button>
            </div>
        </div>
    </form>
</div>
<jsp:include flush="fasle" page="footer.jsp"/>
</body>
</html>