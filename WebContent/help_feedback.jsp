<%--
  Created by IntelliJ IDEA.
  User: DuanJiaNing
  Date: 2018/4/6
  Time: 9:50
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%><%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <link rel="stylesheet" href="css/help_feedback.css">
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="bootstrap-3.3.7-dist/css/bootstrap.css">
    <link rel="stylesheet" href="bootstrapvalidator/css/bootstrapValidator.css">
    <link rel="stylesheet" href="css/site.css">
    <script src="jquery/jquery-2.2.4.min.js" type="text/javascript"></script>
    <script src="bootstrap-3.3.7-dist/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- 表单验证 -->
    <script src="bootstrapvalidator/js/bootstrapValidator.js" type="text/javascript"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 要在最前面引入-->
    <script type="application/javascript" src="jquery/common.js"></script>

    <title>帮助与反馈</title>
    <script>
        function send() {
            var advise = $('#adviceOrOpinion').val();
            var number = $("input[name='number']").val();
            var input  = /^[\s]*$/;
            if (isStrEmpty(advise)||advise=="") {
                error('请输入问题或建议', 'sendFeedbackErrorMsg', true, 3000);
                return;
            }
                if (input.test(advise)){
                    error('请输入问题或建议', 'sendFeedbackErrorMsg', true, 3000);
                    return;
                }
				if(advise.trim().length<4){
					error('请输入至少四个字', 'sendFeedbackErrorMsg', true, 3000);
                    return;
				}
                if(number!="") {
                var p1 = /^(13[0-9]\d{8}|15[0-35-9]\d{8}|18[0-9]\{8}|14[57]\d{8})$/;
                var b = false;
                if (p1.test(number) == b) {
                    error('请输入正确的手机号码!', 'sendFeedbackErrorMsg', true, 3000);
                    return;
                }
            }
            disableButton(false, 'sendFeedbackBtn', '正在提交...', "button-disable");
            // ajax 用户异步请求绑定邮箱
            $.ajax({
                url: "userServlet?action=help",
                type: "POST",
                async: "true",
                data: {"action": "help", "advise": advise, "number": number},
                dataType: "json",
                beforeSend: function () {
                    // 禁用按钮防止重复提交，发送前响应
                    $('#sendFeedbackBtn').text("反馈正在发送。。");
                },
                success: function (data) {
                    if (data.res == 1) {
                        alert(data.info);
                        disableButton(false, 'sendFeedbackBtn', '提交成功', "button-disable");
                        setTimeout(function () {
                            disableButton(true, 'sendFeedbackBtn', '提交', "button-disable");
                            $('#adviceOrOpinion').val('');
                            $('#contactInfo').val('');
                        }, 1000);
//                        $('#sendFeedbackBtn').text("反馈发送成功");
//                        $("#sendFeedbackBtn").attr({ disabled: "disabled" });
                    }
                    else {
                        alert(data.info);
                        disableButton(true, 'sendFeedbackBtn', '提交', "button-disable");
                        error('发送失败', 'sendFeedbackErrorMsg', true, 3000);
//
                    }
                }
            });
            return false;
        }
    </script>
</head>

<body>
<%--<jsp:include flush="false" page="header.jsp" />--%>
<div class="container border" style="min-height: 100%;background-color: white;padding: 5% 10%;">
    <div>
        <h3>反馈和建议</h3>
        <hr class="default-line">
        <br>
        <p style="color: gray;font-size: smaller">描述您的问题或建议，填入下方输入框中，点击提交发送给我。</p>
        <textarea style="width: 80%"  id="adviceOrOpinion"  name="advise" class="jianshu-style-textarea" ></textarea>
        <br>
        <br>
        <p style="color: gray;font-size: smaller">联系方式
            <small>（可不填）</small>
        </p>
        <input type="text"  id="contactInfo" name="number" class="jianshu-style-input">
        <br>

        <br>
        <button id="sendFeedbackBtn"  name="sendFeedbackBtn" class="button-save" onclick="send();">提交
        </button>
        &nbsp;&nbsp;<small style="color: darkgray;">也可直接发送邮件到我的邮箱
        <mark>1632029393@qq.com</mark>
    </small>

        <br>
        <br>
        <span class="error-msg" id="sendFeedbackErrorMsg" name="sendFeedbackErrorMsg"></span>

    </div>
    <br>
    <br>

</div>
<jsp:include flush="false" page="footer.jsp" />
</body>

</html>
