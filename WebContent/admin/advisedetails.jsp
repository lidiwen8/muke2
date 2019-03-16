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
    <title>反馈内容</title>
    <script>
        var id =${param.id};
        $(function (){
            getAdvise();
        });

        function getAdvise(){
            $.get("admin/adminUserServlet",
                {
                    "action":"getAdvisedetails",
                    "id":id
                },
                function(data){
                    if (data.res==1){
                        var advise=data.advise;
//                            $(".advise").html(advise.description);//建议内容
//                            $(".number").html(advise.number);//联系方式
                        document.getElementById("advise").value=advise.description;//建议内容
                        if(advise.number!=null&&advise.number!=""){
                            document.getElementById("number").value=advise.number;//联系方式
                        }else{
                            document.getElementById("number").value="暂无";//联系方式
                        }


                    }else if(data.res==-1){
                        alert(data.info);
                        window.location.replace("admin/advisemanager.jsp");
                    }
                    else{
                        alert(data.info);
                    }
                },"json");

        }
    </script>

</head>
<body>
<jsp:include flush="fasle" page="header.jsp" />
<div class="container border" style="min-height: 100%;background-color: white;padding: 5% 10%;">
    <div>
        <h3>建议详情页</h3>
        <hr class="default-line">
        <br>
        <p style="color: gray;font-size: smaller">建议内容</p>
        <textarea style="width: 80%;height: auto;"  id="advise"  name="advise" class="jianshu-style-textarea" readonly="readonly"></textarea>
        <br>
        <br>
        <p style="color: gray;font-size: smaller">联系方式</p>
        <input type="text"  id="number" name="number" class="jianshu-style-input" readonly="readonly">
        <br>
    </div>
</div>
<jsp:include flush="fasle" page="footer.jsp" />
</body>

</html>
