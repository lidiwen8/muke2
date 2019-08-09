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
<link rel="stylesheet" href="bootstrap-datetimepicker/css/bootstrap-datetimepicker.css">
<script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js" type="text/javascript"></script>
<script src="http://love.lidiwen.club/bootstrap.min.js" type="text/javascript"></script>
<script src="bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
<script src="bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>
<!-- 表单验证 -->
<script src="http://love.lidiwen.club/bootstrapValidator.min.js" type="text/javascript"></script>
<script src="jquery/autoMail.1.0.min.js"></script>
 <script src="js/gt.js"></script>
    <title>爱之家网站答疑平台</title>
    <style>
        .show1 {
            display: block;
        }

        .hide2 {
            display: none;
        }

        #notice, .text-warning {
            color: red;
        }

        #wait {
            text-align: left;
            color: #666;
            margin: 0;
        }
    </style>
    <script type="text/javascript">
        function promot() {
            $('#email').autoMail({
                emails: ['qq.com', '163.com', '126.com', 'sina.com', 'sohu.com', 'yahoo.com', 'hotmail.com', '188.com']
            });
        }

        $(function () {
            validateForm();
            $("#username").keyup(function () {
                var len = $("#username").val().length;
                if (len >= 6 && len <= 30) {
                    validate_empName("#username");
                }
            });
            $(".form_datetime").datetimepicker({
                format: 'yyyy-mm-dd',
                minView: 'month',
                language: 'zh-CN',
                autoclose: true,//选中自动关闭
                startDate: '1900-01-01',
                todayBtn: true//显示今日按钮
            });

        });

        function validateForm() {
            // 验证表单
            $("#registerform").bootstrapValidator({
                message: 'This value is not valid',
                feedbackIcons: {
                    /*输入框不同状态，显示图片的样式*/
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                /*验证*/
                fields: {
                    username: {
                        /*键名username和input name值对应*/
                        message: 'The username is not valid',
                        validators: {
                            notEmpty: {
                                message: '用户名不能为空',

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

                            },
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
                    },
                    password2: {
                        messaage: 'The two password must be consistent',
                        validators: {
                            notEmpty: {
                                message: '确认密码不能为空'
                            },
                            identical: {
                                field: 'password',
                                message: '两次密码必须一致'
                            }
                        }
                    },
                    realname: {
                        messaage: 'The realname is not valid',
                        validators: {
                            notEmpty: {
                                message: '真实姓名不能为空'
                            },
                            stringLength: {
                                max: 30,
                                message: '真实姓名的长度必须小于30'
                            }
                        }
                    },
                    sex: {
                        messaage: 'The sex is not valid',
                        validators: {
                            notEmpty: {
                                message: '性别不能为空'
                            }

                        }
                    },
                    hobbys: {
                        messaage: 'The hobbys is not valid',
                        validators: {
                            notEmpty: {
                                message: '爱好不能为空'
                            }

                        }
                    },
                    email: {
                        messaage: 'The email is not valid',
                        validators: {
                            notEmpty: {
                                message: '邮箱不能为空'
                            },
                            /*emailAddress: {
                                message: '邮箱地址格式有误'
                            }*/

                        }
                    },
                    qq: {
                        messaage: 'The QQ is not valid',
                        validators: {
                            notEmpty: {
                                message: 'QQ不能为空'
                            },
                            regexp: {
                                regexp: /^[1-9][0-9]{4,11}$/,
                                message: 'QQ号不合法, 请重新输入'
                            }
                        }
                    }
                }
            });
        }

        // ajax校验用户名
        function validate_empName(ele) {
            //发送ajax请求校验用户名是否可用
            var username = $(ele).val();
            $.ajax({
                url: "userServlet",
                type: "POST",
                async: "true",
                data: {"action": "checkUsername", "username": username},
                dataType: "json",
                success: function (data) {
                    if (data.res == 1) {
                        show_validate_msg(ele, "success", "该账号可用");
                        $("#btn btn-success btn-block").attr("ajax-va", "success");
                    } else if (data.res == -1) {
                        show_validate_msg(ele, "error", "账号已存在");
                        $("#btn btn-success btn-block").attr("ajax-va", "error");
                    }
                }
            });
        }

        //显示校验结果的提示信息
        function show_validate_msg(ele, status, msg) {
            //清除当前元素的校验状态
            $(ele).parent().removeClass("has-success has-error");
//       $(ele).next("span").text("");
            $(".help-block1").text("");
            if (status == "success") {
                $(ele).parent().addClass("has-success");
//           $(ele).next("span").text(msg);
//           $(".help-block").text(msg);
            } else if (status == "error") {
                //渲染输入框,清空这个元素之前的样式
                $(ele).parent().addClass("has-error");
                //显示错误提示
//           $(ele).next("span").text(msg);
                $(".help-block1").text(msg);
            }
        }

        function gg() {
            var bootstrapValidator = $("#registerform").data("bootstrapValidator");
            //触发验证
            bootstrapValidator.validate();
            //如果验证通过，则调用login方法
            if (bootstrapValidator.isValid()) {
                $('#submit').click();
            }
        }
          function apper(info){
            $("#notice").text(info);
            $("#notice").show();
            setTimeout(function () {
                $("#notice").hide();
            }, 2000);
        }

        var handler2 = function (captchaObj) {
            $("#submit").click(function (e) {
                var result = captchaObj.getValidate();
                if (!result) {
                    $("#notice").text("请先完成验证");
                    $("#notice").show();
                    setTimeout(function () {
                        $("#notice").hide();
                    }, 2000);
                } else {
                    var hobbys ="";
                    $('input[name="hobbys"]:checked').each(function(){
                        hobbys=hobbys+$(this).val()+",";
                    });
                    $.ajax({
                        url: 'userServlet?action=VerifyRegister',
                        type: 'POST',
                        dataType: 'json',
                        data: {
                            username: $("input[name='username']").val(),
                            password: $("input[name='password']").val(),
                            password2: $("input[name='password2']").val(),
                            realname: $("input[name='realname']").val(),
                            sex: $("input[name='sex']").val(),
                            hobbys: hobbys,
                            birthday: $("input[name='birthday']").val(),
                            city: $("input[name='city']").val(),
                            email: $("input[name='email']").val(),
                            qq: $("input[name='qq']").val(),
                            geetest_challenge: result.geetest_challenge,
                            geetest_validate: result.geetest_validate,
                            geetest_seccode: result.geetest_seccode
                        },
                        success: function (data) {
                            if(data.res!=1){
                                captchaObj.reset();
                            }
                            if (data.res == 1) {
                                alert(data.info);
                                window.location.replace("");
                            } else if (data.res == 3) {
                                alert(data.info);
                                apper(data.info);
                                // $(".text-warning").text("尊敬的用户:用户账号不能为空，请重新输入！");
                                $("input[name='username']").focus();
                            } else if (data.res == 8) {
                                alert(data.info);
                                $("input[name='username']").val("");
                                $("input[name='username']").focus();
                                apper(data.info);
                                // $(".text-warning").text("尊敬的用户:用户账号长度必须在6到30之间且不包含特殊符号与中文，请重新输入！");
                            } else if (data.res == 4) {
                                alert(data.info);
                                $("input[name='password']").val("");
                                $("input[name='password']").focus();
                                apper(data.info);
                                // $(".text-warning").text("尊敬的用户:密码长度必须在6到30之间，请重新输入！");
                            } else if (data.res == 9) {
                                alert(data.info);
                                $("input[name='password2']").val("");
                                $("input[name='password2']").focus();
                                apper(data.info);
                                // $(".text-warning").text("尊敬的用户:确认密码长度必须在6到30之间，且要跟新密码一致，请重新输入！");
                            } else if (data.res == 5) {
                                alert(data.info);
                                $("input[name='password2']").val("");
                                $("input[name='password2']").focus();
                                apper(data.info);
                                // $(".text-warning").text("尊敬的用户:确认密码跟新密码不匹配，请重新输入确认密码！");
                            } else if (data.res == 2) {
                                alert(data.info);
                                $("input[name='realname']").val("");
                                $("input[name='realname']").focus();
                                apper(data.info);
                                // $(".text-warning").text("尊敬的用户:为了保证你的合法性，真实姓名不能为空，请如实填写！");
                            } else if (data.res == 7) {
                                alert(data.info);
                                apper(data.info);
                                // $(".text-warning").text("尊敬的用户:所在城市不能为空，请点击城市下拉列表选择你所在的城市！");
                            }
                            else if (data.res == 13) {
                                alert(data.info);
                                apper(data.info);
                                // $(".text-warning").text("尊敬的用户:爱好不能为空，请至少勾选一个爱好！");
                            } else if (data.res == 6) {
                                alert(data.info);
                                $("input[name='email']").focus();
                                apper(data.info);
                                // $(".text-warning").text("尊敬的用户:邮箱号输入不能为空，请重新输入！");
                            } else if (data.res == 10) {
                                alert(data.info);
                                $("input[name='email']").val("");
                                $("input[name='email']").focus();
                                apper(data.info);
                                // $(".text-warning").text("尊敬的用户:你输入的邮箱格式不正确，请重新输入！");
                            } else if (data.res == 11) {
                                alert(data.info);
                                $("input[name='qq']").focus();
                                apper(data.info);
                                // $(".text-warning").text("尊敬的用户:QQ号输入不能为空，请重新输入！");
                            }
                            else if (data.res == 12) {
                                alert(data.info);
                                $("input[name='qq']").val("");
                                $("input[name='qq']").focus();
                                apper(data.info);
                                // $(".text-warning").text("尊敬的用户:你输入的QQ格式不正确，请重新输入！");
                            } else if (data.res == 28) {
                                alert(data.info);
                                $("input[name='password']").val("");
                                $("input[name='password']").focus();
                                apper(data.info);
                                // $(".text-warning").text("尊敬的用户:你输入的密码不合法，不能包含特殊字符和空格，请重新输入！");
                            } else if (data.res == 20) {
                                alert(data.info);
                                // $(".text-warning").text(data.info);
                                $("input[name='email']").val("");
                                $("input[name='email']").focus();
                                apper(data.info);
                            } else if (data.res == 30) {
                                alert(data.info);
                                $("input[name='realname']").val("");
                                $("input[name='realname']").focus();
                                apper(data.info);
                            } else if (data.res == -2) {
                                alert(data.info);
                                apper(data.info);
                            }
                            else {
                                alert(data.info);
                                apper(data.info);
                                // $(".text-warning").text(data.info);
                                // $("input[name='username']").val("");
                                /*$("input[name='password']").val("");
                                $("input[name='password2']").val("");
                                $("input[name='realname']").val("");
                                $("input[name='sex']").val("");
                                $("input[name='hobbys']").val("");
                                $("input[name='birthday']").val("");
                                $("input[name='city']").val("");
                                $("input[name='email']").val("");
                                $("input[name='qq']").val("");*/
                            }
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
        $.ajax({
            url: "userServlet?action=StartCaptchaRegister&t=" + (new Date()).getTime(), // 加随机数防止缓存
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
</head>
<body onload="promot()">
<jsp:include flush="true" page="header.jsp"/>
<div class="container">
    <div class="row">
        <div class="col-sm-offset-3 col-sm-6 text-center">
            <h3>用户注册</h3>
        </div>
    </div>
    <form class="form-horizontal col-sm-offset-3" id="registerform" method="post">
        <div class="form-group">
            <label for="username" class="col-sm-2 control-label">账号：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="username" id="username" placeholder="请输入账号"
                       autofocus="autofocus">
                <span class="help-block1" style="color: #a94442"></span>
            </div>
        </div>
        <div class="form-group">
            <label for="password" class="col-sm-2 control-label">密码：</label>
            <div class="col-sm-4">
                <input type="password" class="form-control" name="password" placeholder="请输入密码">
            </div>
        </div>
        <div class="form-group">
            <label for="password2" class="col-sm-2 control-label">确认密码：</label>
            <div class="col-sm-4">
                <input type="password" class="form-control" name="password2" placeholder="请确认密码">
            </div>
        </div>
        <div class="form-group">
            <label for="realname" class="col-sm-2 control-label">真实姓名：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="realname" placeholder="请输入真实姓名">
            </div>
        </div>
        <div class="form-group">
            <label for="sex" class="col-sm-2 control-label">性别：</label>
            <div class="col-sm-4">
                <label class="radio-inline">
                    <input type="radio" name="sex" value="男" checked> 男
                </label>
                <label class="radio-inline">
                    <input type="radio" name="sex" value="女"> 女
                </label>
            </div>
        </div>
        <div class="form-group">
            <label for="hobbys" class="col-sm-2 control-label">爱好：</label>
            <div class="col-sm-4">
                <label class="checkbox-inline">
                    <input type="checkbox" name="hobbys" value="吃饭">吃饭
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
                    <input class="form-control" size="16" type="text" name="birthday" value="2000-01-01" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label for="city" class="col-sm-2 control-label">城市：</label>
            <div class="col-sm-4">
                <select id="province" size=1>
                </select>
                <select id="city">
                </select>
                <br>
                <input type="text" class="form-control" name="city" id="pro_city" readonly="readonly" value="北京-东城"
                       placeholder="请选择自己所在的城市">

                <script src="jquery/city2.js" type="text/javascript"></script>
            </div>
        </div>
        <div class="form-group">
            <label for="email" class="col-sm-2 control-label">邮箱：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="email" id="email" placeholder="请输入邮箱">
            </div>
        </div>
        <div class="form-group">
            <label for="qq" class="col-sm-2 control-label">QQ：</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" name="qq" placeholder="请输入QQ">
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
                <span class="text-warning" style="color: #a94442"><p id="notice" class="hide2">请先完成验证</p></span>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-4 col-xs-12">
                <button class="btn btn-success btn-block" id="btn btn-success btn-block" onclick="gg();">注册</button>
               <!--<button class="btn btn-success btn-block" onclick="window.location.href='bindingmail.jsp'">绑定邮箱</button>-->
            </div>
        </div>
        <input class="btn" id="submit" type="hidden" value="提交">
    </form>
</div>
<jsp:include flush="true" page="footer.jsp"/>
</body>
</html>