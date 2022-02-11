<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
 <c:if test="${sessionScope.user!= null}">
        <script type="text/javascript">
            $(function () {
                window.location.href="index.jsp";
            });
        </script>
    </c:if>
<script src="https://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js" type="text/javascript"></script>
<!-- 表单验证 -->
<script src="http://love.lidiwen.club/bootstrapValidator.min.js" type="text/javascript"></script>
<script src="js/gt.js"></script>
<title>爱之家网站答疑平台</title>
    <style>
        .show1 {
            display: block;
        }
        .hide2 {
            display: none;
        }
        #notice,.text-warning {
            color: red;
        }
        #wait {
            text-align: left;
            color: #666;
            margin: 0;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            validateForm();
        });

        function validateForm() {
            // 验证表单
            $("#loginform").bootstrapValidator({
                message: 'This value is not valid',
                feedbackIcons: {
                    /*输入框不同状态，显示图片的样式*/
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    /*验证*/
                    username: {
                        /*键名username和input name值对应*/
                        message: 'The username is not valid',
                        validators: {
                            notEmpty: {
                                /*非空提示*/
                                message: '用户名不能为空'
                            },
                            regexp: {
                                regexp: /^[a-zA-Z0-9_\.]+$/,
                                message: '用户名不合法, 请重新输入'
                            },
                            stringLength: {
                                /*长度提示*/
                                min: 6,
                                max: 30,
                                message: '用户名长度必须在6到30之间'
                            }/*最后一个没有逗号*/

                        }
                    },
                    password: {
                        message: '密码无效',
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
        }

        //记住密码复选框的点击事件
        function remember(){
            var remFlag = $("input[type='checkbox']").is(':checked');
            if(remFlag==true){ //如果选中设置remFlag为true
                var conFlag = confirm("记住密码功能不宜在公共场所(如网吧等)使用,以防密码泄露.您确定要使用此功能吗?");
                if(conFlag){ //确认标志
                    $('#rememberme').val(1);
                }else{
                    $("input[type='checkbox']").removeAttr('checked');
                    $('#rememberme').val(0);
                }
            }else{ //如果没选中设置remFlag为false
                $('#rememberme').val(0);
            }
        }
        function gg() {
            var bootstrapValidator = $("#loginform").data("bootstrapValidator");
            //触发验证
            bootstrapValidator.validate();
            //如果验证通过，则调用login方法
            if (bootstrapValidator.isValid()) {
                // login();
                $('#submit').click();
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
<jsp:include flush="true" page="header.jsp"/>
<div class="container">
    <div class="row">
        <div class="col-sm-offset-3 col-sm-6 text-center">
            <h3>用户登录</h3>
        </div>
    </div>
    <%
        String username = "";
        String password = "";
        //获取当前站点的所有Cookie
        Cookie[] cookies = request.getCookies();
	if(cookies!=null){
        for (int i = 0; i < cookies.length; i++) {//对cookies中的数据进行遍历，找到用户名、密码的数据
            if ("username".equals(cookies[i].getName())) {
                username = cookies[i].getValue();
            } else if ("password".equals(cookies[i].getName())) {
                password = cookies[i].getValue();
            }
        }
     }
    %>
    <form class="form-horizontal col-sm-offset-3" id="loginform" method="post">
        <div class="form-group">
            <label for="username" class="col-sm-2 control-label">账号：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="username" placeholder="请输入账号" id="username"
                       value="<%=username%>">
            </div>
        </div>
        <div class="form-group">
            <label for="password" class="col-sm-2 control-label">密码：</label>
            <div class="col-sm-4">
                <input type="password" class="form-control" name="password" placeholder="请输入密码" id="password"
                       value="<%=password%>">
            </div>
        </div>
        <div class="form-group">
                <label class="col-sm-2 control-label">验证：</label>
                <div id="captcha" class="col-sm-4">
                    <p id="wait" class="show1">正在加载验证码.....</p>
                </div>
        </div>
        <div class="form-group has-error">
            <div class="col-sm-offset-2 col-sm-4 col-xs-6 ">
                <span class="text-warning"><p id="notice" class="hide2">请先完成验证</p></span>
                <p>
                <label for="rememberme"><input name="rememberme" type="checkbox" id="rememberme" value="0"
                                               style="cursor:pointer;" onclick="remember();"/> 记住密码</label>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-2 col-xs-6">
                <button class="btn btn-success btn-block" onclick="gg();" id="btnLogin">登录</button>
            </div>
            <div class="col-sm-2  col-xs-6">
                <a class="btn btn-warning btn-block" href="register.jsp">注册</a>
            </div>
            <div class="row">
                <div class="col-sm-offset-3 col-sm-6 text-center" style="width: 40%;">
                    <h3><a href="changepassword.jsp"
                           style="color: #2f72ff;font-size:medium;align:center;margin-left: auto;">忘记密码?</a></h3>
                </div>
            </div>
        </div>
        <input class="btn" id="submit" type="hidden" value="提交">
    </form>
    <script>
        var msgId =<%=msgId%>;
        var handler2 = function (captchaObj) {
            $("#submit").click(function (e) {
                var result = captchaObj.getValidate();
                if (!result) {
                    $("#notice").show();
                    setTimeout(function () {
                        $("#notice").hide();
                    }, 2000);
                } else {
                    $('#btnLogin').attr('disabled',true);
                    $.ajax({
                        url: 'userServlet?action=VerifyLogin',
                        type: 'POST',
                        dataType: 'json',
                        data: {
                            username: $('#username').val(),
                            password: $('#password').val(),
                            rememberme: $('#rememberme').val(),
                            geetest_challenge: result.geetest_challenge,
                            geetest_validate: result.geetest_validate,
                            geetest_seccode: result.geetest_seccode
                        },
                        success: function (data) {
                            if (data.res == 1) {
                                alert("登录成功");
                                 if(msgId!="-1"){
                                    window.location.replace("<%=basePath%>message.jsp?msgid=<%=msgId%>");
                                }else{
                                    window.location.replace("");
                                }
                            } else if (data.res == 2) {
                                alert(data.info);
                                 if(msgId!="-1") {
                                    window.location.replace("bindingmail.jsp?msgid=<%=msgId%>");
                                }else {
                                    window.location.replace("bindingmail.jsp");
                                }
                            }
                            else if (data.res == 6) {
                                $('#btnLogin').attr('disabled',false);
                                alert(data.info);
                                captchaObj.reset();
                            }
                            else if(data.info=="你的账号已被禁用！"){
                                $('#btnLogin').attr('disabled',false);
                                alert("你的账号由于违规发布不良信息已经被系统管理员禁用，如有疑问请联系管理员邮箱：1632029393@qq.com!");
                                captchaObj.reset();
                            }else {
                                $('#btnLogin').attr('disabled',false);
                                alert(data.info);
                                captchaObj.reset();
                            }
                        },
                        error: function () {
                            window.location.href = "login.jsp";
                        }
                    })
                }
                e.preventDefault();
            });
            // 将验证码加到id为captcha的元素里，同时会有三个input的值用于表单提交
            captchaObj.appendTo("#captcha");
            captchaObj.onReady(function () {
                $("#wait").hide();
            });
        };
            var username1 = (new Date()).getTime();
            $.ajax({
                url: "userServlet?action=StartCaptcha&username=" + username1 + "&t=" + (new Date()).getTime(), // 加随机数防止缓存
                type: "get",
                dataType: "json",
                success: function (data) {
                    // 调用 initGeetest 初始化参数
                    // 参数1：配置参数
                    // 参数2：回调，回调的第一个参数验证码对象，之后可以使用它调用相应的接口
                    initGeetest({
                        gt: data.gt,
                        challenge: data.challenge,
                        new_captcha: data.new_captcha, // 用于宕机时表示是新验证码的宕机
                        offline: !data.success, // 表示用户后台检测极验服务器是否宕机，一般不需要关注
                        product: "popup", // 产品形式，包括：float，popup
                        width: "100%"
                    }, handler2);
                }
            });
    </script>
</div>
<jsp:include flush="true" page="footer.jsp"/>
</body>
</html>
