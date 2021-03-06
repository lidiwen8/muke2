﻿<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String msgId = request.getParameter("msgid");
 if (msgId == null || msgId.equals("")){
		msgId = "-1";
	}
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
    <script src="jquery/autoMail.1.0.min.js"></script>
    <title>爱之家网站答疑平台</title>
    <script type="text/javascript">
        function promot1() {
            $('#mail').autoMail({
                emails: ['qq.com', '163.com', '126.com', 'sina.com', 'sohu.com','yahoo.com','hotmail.com','188.com']
            });
        }

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
                    },

                    input1 : {
                        messaage : 'The validate is not valid',
                        validators : {
                            notEmpty : {
                                message : '验证码不能为空'
                            }
                        }
                    }
                }
            });
        }
        var msgId =<%=msgId%>;
        var InterValObj; //timer变量，控制时间
        var count = 50; //间隔函数，1秒执行
        var curCount;//当前剩余秒数
        function sendCode(){
            var username = $("input[name='username']").val();
            var mail = $("input[name='mail']").val();
            if(username==""){
                alert("请输入用户名");
                apper("请输入用户名");
            }else if(mail==""){
                alert("请输入邮箱");
                apper("请输入邮箱");
            }else if(!mail.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/)){
                alert("请输入正确格式的邮箱");
                apper("无效邮箱");
            }
            else
            {
                send(username,mail);
                curCount = count;
                $("#verificationss").attr("disabled", "true");
                $("#verificationss").text(curCount + "秒后重新获取");
                InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次请求后台发送验证码 TODO
            }
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

        function apper(info) {
            $("#notice").text(info);
            $("#notice").show();
            setTimeout(function () {
                $("#notice").hide();
            }, 2000);
        }
        function send(username,mail) {
            // ajax 用户异步请求绑定邮箱
            $.ajax({
                url: "userServlet?action=bindingmail",
                type: "POST",
                async: "true",
                data: {"action": "bindingmail", "username": username, "mail": mail},
                dataType: "json",
                beforeSend: function () {
                    // 禁用按钮防止重复提交，发送前响应
                    // $("#verificationss").attr({disabled: "disabled"});
                    // $('#verificationss').text("验证码正在发送。。");
                },
                success: function (data) {
                    if (data.res != 1) {
                        curCount=0;
                    }
                    if (data.res == 1) {
                        alert(data.info);
                        // $('#verificationss').text("验证码发送成功");
                        // $("#verificationss").attr({disabled: "disabled"});
                    }
                    else if (data.res == 6) {
                        alert(data.info);
                        // $('#verificationss').text("发送验证码");
                        apper(data.info);
                    } else if (data.res == 10) {
                        alert(data.info);
                        // $('#verificationss').text("发送验证码");
                        apper(data.info);
                    }
                    else if (data.res == 8) {
                        alert(data.info);
                        // $('#verificationss').text("邮件发送失败");
                        apper(data.info);
                    } else if (data.res == 21) {
                        alert(data.info);
                        window.location.replace("login.jsp");
                    } else if (data.res == 3) {
                        alert(data.info);
                        // $('#verificationss').text("发送验证码");
                        apper(data.info);
                    } else if (data.res == 9) {
                        alert(data.info);
                        // $('#verificationss').text("发送验证码");
                        apper(data.info);
                    } else if (data.res == 2) {
                        alert(data.info);
                        // $('#verificationss').text("发送验证码");
                        apper(data.info);
                    } else if (data.res == 23) {
                        alert(data.info);
                        // $('#verificationss').text("发送验证码");
                        apper(data.info);
                    } else if (data.res == 17) {
                        alert(data.info);
                        // $('#verificationss').text("发送验证码");
                        apper(data.info);
                    }
                    else {
                        alert(data.info);
                        apper(data.info);
                        $("input[name='mail']").val("");
                        window.location.replace("register.jsp");
                    }
                }
            });
            return false;
        }

        function bindingmail() {
            // Ajax 异步请求登录
            var username = $("input[name='username']").val();
            var mail = $("input[name='mail']").val();
            var input1 = $("input[name='input1']").val();
                $.ajax({
                    url: "userServlet?action=binding",
                    type: "POST",
                    async: "true",
                    data: {"action": "binding", "username": username, "mail": mail,"input1":input1},
                    dataType: "json",
                    success: function (data) {
                        if (data.res == 1) {
                            alert("绑定邮箱成功");
                            $('#submitbutton').text("绑定邮箱成功");
                            $("#submitbutton").attr({ disabled: "disabled" });
                            apper("");
                            if(msgId!="-1"){
                              window.location.replace("<%=basePath%>message.jsp?msgid="+msgId);
                            }else {
                                window.location.replace("index.jsp");
                              } 
                      }
                      else if (data.res == 6) {
                        alert(data.info);
                        apper(data.info);
                        // $(".text-warning").text("尊敬的用户:邮箱号输入不能为空，请重新输入！");
                    } else if (data.res == 10) {
                        alert(data.info);
                        apper(data.info);
                    } else if (data.res == 20) {
                        alert(data.info);
                        apper(data.info);
                        window.location.replace("register.jsp");
                    } else if (data.res == 17) {
                        alert(data.info);
                        apper(data.info);
                        $('#submitbutton').text("服务器错误");
                    } else if (data.res == 21) {
                        alert(data.info);
                        window.location.replace("login.jsp");
                    } else if (data.res == 3) {
                        alert(data.info);
                        apper(data.info);
                    } else if (data.res == 9) {
                        alert(data.info);
                        apper(data.info);
                    }
                    else {
                        alert(data.info);
                        $("input[name='input1']").val("");
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
                bindingmail();
            }
        }
    </script>
</head>
<body>
<jsp:include flush="fasle" page="header.jsp" />
<div class="container">
    <div class="row">
        <div class="col-sm-offset-3 col-sm-6 text-center">
            <h3>绑定邮箱</h3>
        </div>
    </div>
    <form class="form-horizontal col-sm-offset-3" id="modifyform" method="post">
        <div class="form-group">
            <label for="username" class="col-sm-2 control-label">账户：</label>
            <div class="col-sm-4">
                 <c:if test="${sessionScope.user!= null}">
                    <input type="text" class="form-control" name="username" value="${sessionScope.user.username}" placeholder="请输入你注册时的账户号" id="username" readonly="readonly">
                </c:if>
                <c:if test="${sessionScope.user== null}">
                    <input type="text" class="form-control" name="username" placeholder="请输入你注册时的账户号" id="username" autofocus="autofocus">
                </c:if>
            </div>
        </div>
        <div class="form-group">
            <label for="mail" class="col-sm-2 control-label">邮箱：</label>
            <div class="col-sm-4">
               <c:if test="${sessionScope.user!= null}">
                <input type="text" class="form-control" name="mail" id="mail" placeholder="请输入你注册时的邮箱" value="${sessionScope.user.email}">
                </c:if>
                <c:if test="${sessionScope.user== null}">
                    <input type="text" class="form-control" name="mail" id="mail" placeholder="请输入你注册时的邮箱">
                </c:if>
            </div>
        </div>
        <div class="form-group">
            <label for="input1" class="col-sm-2 control-label">验证码：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control"  name="input1" id="input1" placeholder="请输入邮箱验证码"/>
                <button class="btn btn-success btn-block" onclick="sendCode();" id="verificationss" name="verificationss" style="color: #171719;width: 117px;background-color: #0044cc"><img src="images/youjian.png" style="width:15px;height:15px;margin-right:5px;margin-bottom:5px;">发送验证码</button>
            </div>
        </div>
        <div class="form-group has-error">
            <div class="col-sm-offset-2 col-sm-4 col-xs-6 ">
                 <span class="text-warning" style="color: #a94442"><p id="notice" style="display: none;"></p></span>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-4 col-xs-12">
                <button type="button" class="btn btn-success btn-block" onclick="gg();" id="submitbutton">提交</button>
            </div>
        </div>
    </form>
</div>
<jsp:include flush="fasle" page="footer.jsp" />
</body>
</html>