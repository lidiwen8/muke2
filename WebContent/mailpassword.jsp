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
    <script src="jquery/autoMail.1.0.min.js"></script>
    <title>爱之家网站答疑平台</title>
    <script type="text/javascript">
        var code; //在全局 定义验证码
        function promot() {
            $('#mail').autoMail({
                emails: ['qq.com', '163.com', '126.com', 'sina.com', 'sohu.com','yahoo.com','hotmail.com','188.com']
            });
        }
        function createCode() {
            code = "";
            var codeLength = 4;//验证码的长度
            var checkCode = document.getElementById("checkCode");
            var selectChar = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9,'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z');//所有候选组成验证码的字符，当然也可以用中文的

            for (var i = 0; i < codeLength; i++) {
                var charIndex = Math.floor(Math.random() * 36);
                code += selectChar[charIndex];
            }
            //alert(code);
            if (checkCode) {
                checkCode.className = "form-control";
                checkCode.value = code;
            }
        }
        function load(){
            createCode();
            var checkCode = document.getElementById("checkCode");
            if (checkCode) {
                checkCode.value = code;
            }
        }

        $(function(){
            validateForm();
        });
          function apper(info) {
            $("#notice").text(info);
            $("#notice").show();
            setTimeout(function () {
                $("#notice").hide();
            }, 2000);
        }
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

                    verifyCode : {
                    messaage : 'The validate is not valid',
                    validators : {
                        notEmpty : {
                            message : '验证码不能为空'
                        },
                        stringLength: {
                            min: 4,
                            max: 4,
                            message: '验证码长度必须为四位'
                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9]+$/,
                            message: '验证码不合法, 请重新输入'
                        }
                    }
                }
            }
            });
        }
        function validate() {
            var inputCode = document.getElementById("input1").value.toUpperCase();
            if (inputCode.length <= 0) {
                $(".text-warning").text("验证码不能为空！");
                return false;
            } else if (inputCode != code) {
                $(".text-warning").text("验证码输入错误！");
                createCode();//刷新验证码
                document.getElementById("input1").value="";
                return false;
            } else {
                // alert("^-^ OK");
                return true;
            }
        }
        function _hyz() {
            /*

             1. 获取<img>元素

             2. 给它的src指向为/tools/VerifyCodeServlet

             */
            var img = document.getElementById("imgVerifyCode");

            // 需要给出一个参数，这个参数每次都不同，这样才能干掉浏览器缓存！

            img.src = "VerifyCodeServlet?a=" + new Date().getTime();

        }
        function updatePW() {
//            var flag = validate();
//            if (flag) {
                // ajax 异步请求修改密码
                $.ajax({
                    url: "userServlet?action=emailpass",
                    type: "POST",
                    async: "true",
                    data: $("#modifyform").serialize(),
                    dataType: "json",
                    beforeSend: function () {
                        // 禁用按钮防止重复提交，发送前响应
                        $("#mailbutton").attr({ disabled: "disabled" });
                        $('#mailbutton').text("邮件正在发送...");
                    },
                    success: function (data) {
                        if(data.res!=1){
                            _hyz();
                            $("#mailbutton").removeAttr("disabled");
                        }
                        if (data.res == 1) {
                            alert(data.info);
                            $('#mailbutton').text("邮件发送成功");
                            window.location.replace("login.jsp");
                        }  else if (data.res == 6) {
                            alert(data.info);
                            $('#mailbutton').text("提交");
                            apper(data.info);
                        } else if (data.res == 7) {
                            alert(data.info);
                            // $(".text-warning").text("尊敬的用户:验证码输入错误，请重新输入！");
                            $("input[name='verifyCode']").val("");
                            $('#mailbutton').text("提交");
                            apper(data.info);
                        }
                        else if (data.res == 10) {
                            alert(data.info);
                            // $(".text-warning").text("尊敬的用户:你输入的邮箱格式不正确，请重新输入！");
                            $('#mailbutton').text("提交");
                            apper(data.info);
                        }
                        else if (data.res == 8) {
                            alert(data.info);
                            // $(".text-warning").text("用户身份验证成功，发送邮件失败，可能服务器出了点问题，请及时联系网站管理员！");
                            $('#mailbutton').text("邮件发送失败");
                            apper(data.info);
                        } else if (data.res ==27) {
                            alert(data.info);
                            // $(".text-warning").text("尊敬的用户:你的该邮箱账号并没有被激活，不能通过此方式来找回密码，请激活后再通过邮箱账号重置密码！");
                            $('#mailbutton').text("邮件未发送");
                            apper(data.info);
                            window.location.replace("bindingmail.jsp");
                        }
                        else {
                            alert(data.info);
                            // $(".text-warning").text("尊敬的用户:你输入的邮箱号并没有被注册或者激活!用户认证失败，请重新输入！");
                            $("input[name='mail']").val("");
                            $('#mailbutton').text("邮件发送失败");
                            apper(data.info);
                        }
                    }
                });
                return false;
//            }else {
//                alert("验证码输入错误！");
//                $(".text-warning").text("验证码输入错误！");
//            }
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
<jsp:include flush="true" page="header.jsp" />
<div class="container">
    <div class="row">
        <div class="col-sm-offset-3 col-sm-6 text-center">
            <h3>忘记密码-通过电子邮件重置密码.</h3>
        </div>
    </div>
    <form class="form-horizontal col-sm-offset-3" id="modifyform" method="post">

        <div class="form-group">
            <label for="mail" class="col-sm-2 control-label">邮箱：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="mail" id="mail" placeholder="请输入你注册时的邮箱">
            </div>
        </div>
        <%--<div class="form-group">--%>
            <%--<label for="input1" class="col-sm-2 control-label">验证码：</label>--%>
            <%--<div class="col-sm-4">--%>
                <%--<input type="text" class="form-control"  name="input1" onblur="validate()" id="input1" placeholder="请输入验证码"/> <input type="text" class="form-control" onclick="createCode()" readonly="readonly" id="checkCode"  placeholder="刷新一下"  style="color: #171719;width: 117px;background-color: #0044cc" />--%>
            <%--</div>--%>
        <%--</div>--%>
        <div class="form-group">
            <label for="input1" class="col-sm-2 control-label">验证码：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control"  name="verifyCode" id="input1" placeholder="请输入验证码"/>
                <div style="display:inline-block;vertical-align:middle;align-items: center;justify-content: center;">
                    <img src="VerifyCodeServlet" id="imgVerifyCode" class="form-control" style="float:left;width: 117px;margin-top: 15px;"/>
                   <%--<span style="float: right;margin-left: 120px;">--%>
                         <a href="javascript:_hyz()" style=" float: left; display: inline;margin-top: 22px;margin-left: 10px;text-decoration: none">【换一张】</a>
                   <%--</span>--%>
                </div>

            </div>
        </div>
        <div class="form-group has-error">
            <div class="col-sm-offset-2 col-sm-4 col-xs-6 ">
                 <span class="text-warning" style="color: #a94442"><p id="notice" style="display: none;"></p></span>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-4 col-xs-12">
                <button type="button" class="btn btn-success btn-block" onclick="gg();" id="mailbutton" name="mailbutton">提交</button>
            </div>
        </div>
    </form>
</div>
<jsp:include flush="fasle" page="footer.jsp" />
</body>
</html>