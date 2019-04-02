<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"  isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%
    String username = request.getParameter("username");
    if (username == null || username.equals("")) {
        username = "";
    }

%>
<html>
<base href="<%=basePath%>">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,user-scalable=no,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge,Chrome=1" />
    <meta name="description" content="爱之家网站答疑平台" />
    <meta name="keywords" content="爱之家网站答疑平台" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="jquery/jquery-2.2.4.min.js" type="text/javascript"></script>
    <%--<script src="https://libs.baidu.com/jquery/2.1.4/jquery.min.js" type="text/javascript"></script>--%>
    <link rel="stylesheet" href="bootstrap-3.3.7-dist/css/bootstrap.css">
    <%--https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css--%>
    <link rel="stylesheet" href="semantic/semantic.min.css">
    <%--<link rel="stylesheet" href="https://oj.bnuz.edu.cn/static/assets/semantic/semantic.min.css">--%>
    <link rel="stylesheet" href="semantic/app.css">
    <link rel="stylesheet" href="semantic/jquery.Jcrop.min.css">
    <link rel="stylesheet" href="css/site.css">
    <title id="title">个人中心</title>
    <style type="text/css">
        #bg_particles{

            background: #2980b9;

            height: auto;
            min-height: 200px;
            margin-bottom: 30px;
        }
        .wj_page_container{
            width:100%;
        }
        .my_page_container {
            width:95%;
            min-height: 100%;
            height: auto;
            margin: 0 auto;
        }
        .ui.inverted.stackable.menu{
            margin-bottom: 0;
        }
        .space_head_container{
            width: 80%;
            margin: 0 auto;
            padding: 30px 0;
        }
        .space_username{
            color: #fff;
            text-shadow: 1px 1px 5px #333;
        }
        .space_username > h1{
            line-height: 75px;
        }
        .space_username > span{
            font-size: 1.5rem;
        }
        .pg-canvas{
            position: absolute;
        }
    </style>
    <script type="text/javascript" src="semantic/js/jquery.Jcrop.min.js"></script>
    <script type="text/javascript" src="semantic/js/jquery.particleground.min.js"></script>
    <script type="text/javascript">
        var pageNum = 1;
        var pageNum2 = 1;
        $(function () {
            $('#bg_particles').particleground({
                dotColor: '#3498db',
                lineColor: '#3498db',
                parallax:false
            });
            getUser();
            getReply();
        });
        function getUser() {
            $.get("userServlet", {"action": "getUser","username": "${param.username}","pageNum":pageNum}, function (data) {
                var message = data.message;
                var message2 = data.message2;
                var replycount=data.replycount;
                var replydistinctcount=data.replydistinctcount;
                var createtime=data.createtime;
                var flag=data.flag;
                if (data.res == 1) {
                    $("#title").text(message.username+"-个人中心");
                    $("#img").attr("src",message.user_img);
                    if(flag==1){
                        $("#description").text(message.description);
                    }
                    $("#k1").text(message.username+"-个人中心");
                    $("#user").attr("href","user.jsp?username="+message.username);
                    $("#user").text(message.username);
                    if(message.loginNum<=0){
                        $("#loginNum").text(1);
                    }else {
                        $("#loginNum").text(message.loginNum);
                    }
                    var grade=data.replycount+message2.rows;
                    if(grade==0){
                        $("#identiy").text("初来乍到");
                    }
                    else if(grade<5){
                        $("#identiy").text("倔强青铜");
                    }else if(grade>=5&&grade<10){
                        $("#identiy").text("白银");
                    }else if(grade>=10&&grade<50){
                        $("#identiy").text("黄金");
                    }else if(grade>=50&&grade<100){
                        $("#identiy").text("铂金");
                    }else if(grade>=100&&grade<200){
                        $("#identiy").text("钻石");
                    }else if(grade>=200&&grade<400){
                        if(message2.rows>=300&&message2.rows>data.replycount){
                            $("#identiy").text("发帖狂魔");
                        }else if(data.replycount>=300&&message2.rows<data.replycount){
                            $("#identiy").text("回帖狂魔");
                        }else {
                            $("#identiy").text("星耀");
                        }
                    }else if(grade>=400&&grade<600){
                        $("#identiy").text("王者");
                    }
                    else {
                        $("#identiy").text("最强王者");
                    }
                    $("#reply").text(replycount);
                    $("#replydistinctcount").text(replydistinctcount);
                    $("#realname").text(message.realname);
                    if(message.mailstate==0){
                        $("#email").text(message.email+"（未验证）");
                    }else{
                        $("#email").text(message.email);
                    }
                    $("#qq").text(message.qq);
                    $("#birthday").text(message.birthday);
                    $("#pro_city").text(message.city);
                    $("#sex").text(message.sex);
                    $("#hobbys").text(message.hobbys);
                    $("#k2").text("发布的帖子-"+message2.rows+"个");
                    $("#msg").text(message2.rows);
                    $("#createtime").text(createtime);
                    $("#logintime").text(message.logintime);

                    $.each(message2.data,function(index,element){
                        var msg=$("#template").clone();//复制模版
                        msg.show();//显示
                        msg.removeClass("template");//移除模版
                        msg.find("#t").text(element.msgtopic);//帖子标题
                        //添加href属性
                        msg.find("#t").attr("href", "<%=basePath%>message.jsp?msgid=" + element.msgid);
                        msg.find("#time").text(element.msgtime);//发帖时间
                        msg.find("#count").text(element.accessCount+" • "+element.replyCount+" • "+element.likecount);//浏览量和回复量以及点赞量
                        $("#list").append(msg);//将帖子信息添加到list中
                    });
                    //加载更多
                    pageNum++;
                    if(parseInt(message2.totalPage)>=parseInt(pageNum)){

                        $("#loadmore").html("加载更多...");
                        $("#loadmore").removeAttr("disabled");
                    }else{
                        if(message2.rows==0){
                            $("#loadmore").html("这人很懒，没有发过一个帖子...");
                            $("#loadmore").attr("disabled","disabled");
                        }else {
                            $("#loadmore").html("没有更多数据了");
                            $("#loadmore").attr("disabled","disabled");
                        }

                    }

                } else {
                    alert(data.message);
                    window.location.href="index.jsp";
                }
            }, "json");
        }

        function getReply() {
            $.get("userServlet", {"action": "getReply","username": "${param.username}","pageNum":pageNum2}, function (data) {
                var message = data.message;
                var day=data.day;
                if (data.res == 1) {
                    $("#k3").text("回复过的帖子-"+message.rows+"个");
                    if(day<=0){
                        $("#day").text(1);
                    }else {
                        $("#day").text(day);
                    }
                    $.each(message.data,function(index,element){
                        var msg=$("#template2").clone();//复制模版
                        msg.show();//显示
                        msg.removeClass("template");//移除模版
                        msg.find("#t2").text(element.msgtopic);//帖子标题
                        //添加href属性
                        msg.find("#t2").attr("href", "<%=basePath%>message.jsp?msgid=" + element.msgid);
                        msg.find("#time2").text(element.msgtime);//发帖时间
                        msg.find("#count2").text(element.accessCount+" • "+element.replyCount+" • "+element.likecount);//浏览量和回复量
                        $("#list2").append(msg);//将帖子信息添加到list中
                    });
                    //加载更多
                    pageNum2++;
                    if(parseInt(message.totalPage)>=parseInt(pageNum2)){

                        $("#loadmore2").html("加载更多...");
                        $("#loadmore2").removeAttr("disabled");
                    }else{
                        if(message.rows==0){
                            $("#loadmore2").html("这人很懒，没有回复过一个帖子...");
                            $("#loadmore2").attr("disabled","disabled");
                        }else {
                            $("#loadmore2").html("没有更多数据了");
                            $("#loadmore2").attr("disabled","disabled");
                        }

                    }

                } else {
                    // alert(data.message);
                    window.location.href="index.jsp";
                }
            }, "json");
        }
    </script>
</head>
<body>
<%--<jsp:include flush="true" page="header.jsp" />--%>
<div class="wj_page_container">
    <div id="bg_particles">
        <div class="space_head_container">
            <div class="ui stackable grid">
                <div class="four wide column">
                    <img class="ui centered aligned small circular image" id="img" src="">
                </div>
                <div class="twelve wide column space_username">
                    <h1 id="k1">

                    </h1>
                    <span title="None" id="description">这人很懒，什么也没写...</span>
                </div>
            </div>
        </div>
    </div>
    <div class="my_page_container">
        <div class="ui breadcrumb" id="wejudge_breadcrumb_navbar" style="margin-bottom: 10px;">
            <a href="index.jsp"><i class="home icon"></i> 爱之家首页</a>
            <i class="right chevron icon divider"></i>
            账户中心
            <i class="right chevron icon divider"></i>
            <a id="user" class="section"></a>
            <i class="right chevron icon divider"></i>
            个人中心
        </div>
        <div class="ui secondary pointing menu" id="MainNavList">
            <a class="active item" data-tab="home">
                <i class="user icon"></i>
                账户信息
            </a>
        </div>
        <div class="ui active tab" data-tab="home">
            <div class="ui stackable grid">
                <div class="five wide column">

                    <div class="ui two statistics">
                        <div class="statistic">
                            <div class="value" id="msg">
                                124
                            </div>
                            <div class="label">
                                发布帖子
                            </div>
                        </div>
                        <div class="statistic">
                            <div class="value" id="reply">
                                146
                            </div>
                            <div class="label">
                                回帖总数
                            </div>
                        </div>
                        <div class="statistic">
                            <div class="value" id="replydistinctcount">
                                146
                            </div>
                            <div class="label">
                                回复过的帖子
                            </div>
                        </div>
                        <div class="statistic">
                            <div class="value" id="day">
                                13
                            </div>
                            <div class="label">
                                已来到爱之家(天)
                            </div>
                        </div>
                    </div>

                    <table class="ui definition table">

                        <tr>
                            <td>真实姓名</td>
                            <td id="realname"></td>
                        </tr>
                        <tr>
                            <td>账户身份</td>
                            <td id="ti">普通用户</td>
                        </tr>
                        <tr>
                            <td>称号</td>
                            <td id="identiy">初来乍到</td>
                        </tr>
                        <tr>
                            <td>性别</td>
                            <td id="sex">男</td>
                        </tr>
                        <tr>
                            <td>爱好</td>
                            <td id="hobbys"></td>
                        </tr>
                        <tr>
                            <td>生日</td>
                            <td id="birthday"></td>
                        </tr>
                        <tr>
                            <td>所在城市</td>
                            <td id="pro_city"></td>
                        </tr>
                        <tr>
                            <td>邮箱</td>
                            <td id="email"></td>
                        </tr>
                        <tr>
                            <td>QQ</td>
                            <td id="qq"></td>
                        </tr>
                        <tr>
                            <td>创建时间</td>
                            <td id="createtime">2018年9月7日 23:29</td>
                        </tr>
                        <tr>
                            <td>上次登录</td>
                            <td id="logintime">2019-01-10 12:17:58</td>
                        </tr>
                        <tr>
                            <td>登录次数</td>
                            <td id="loginNum">5</td>
                        </tr>
                    </table>
                </div>
                <div class="eleven wide column">
                    <div id="sv_container">
                        <div class="row">
                            <div class="col-sm-12 msgtitle"><h3 id="k2">发布的帖子</h3></div>
                        </div>
                        <br>
                        <div class="row">
                            <div class="col-sm-8 col-xs-8"><h4>标题</h4></div>
                            <div class="col-sm-2 col-xs-4 text-center"><h4>时间</h4></div>
                            <div class="col-sm-2 hidden-xs text-center"><h4>浏览 • 回复 • 点赞</h4></div>
                            <%--<div class="col-sm-2 col-xs-2 text-center"><h4>操作</h4></div>--%>
                        </div>
                        <div class="row msglist template" id="template">
                            <div class="col-sm-12">
                                <div class="col-sm-8 col-xs-8 text-limit">
                                    <a class="title" id="t">标题标题标题标题标题标题</a>
                                </div>
                                <div class="col-sm-2  col-xs-4 text-center time" id="time">时间</div>
                                <div class="col-sm-2 hidden-xs text-center count" id="count">浏览/回复</div>
                            </div>
                        </div>
                        <div class="list" id="list">

                        </div>
                        <div class="row p">
                            <div class="col-sm-12">
                                <br/>
                                <button id="loadmore" disabled="disabled" type="button" class="btn btn-default btn-lg btn-block"
                                        onclick="javascript:getUser();">加载更多...</button>
                            </div>
                        </div>
                    </div>
                    <br>
                    <div id="sv_container2">
                        <div class="row">
                            <div class="col-sm-12 msgtitle"><h3 id="k3">回复过的帖子</h3></div>
                        </div>
                        <br>
                        <div class="row">
                            <div class="col-sm-8 col-xs-8"><h4>标题</h4></div>
                            <div class="col-sm-2 col-xs-4 text-center"><h4>时间</h4></div>
                            <div class="col-sm-2 hidden-xs text-center"><h4>浏览 • 回复 • 点赞</h4></div>
                            <%--<div class="col-sm-2 col-xs-2 text-center"><h4>操作</h4></div>--%>
                        </div>
                        <div class="row msglist template" id="template2">
                            <div class="col-sm-12">
                                <div class="col-sm-8 col-xs-8 text-limit">
                                    <a class="title" id="t2">标题标题标题标题标题标题</a>
                                </div>
                                <div class="col-sm-2  col-xs-4 text-center time" id="time2">时间</div>
                                <div class="col-sm-2 hidden-xs text-center count" id="count2">浏览/回复</div>
                            </div>
                        </div>
                        <div class="list" id="list2">

                        </div>
                        <div class="row p">
                            <div class="col-sm-12">
                                <br/>
                                <button id="loadmore2" disabled="disabled" type="button" class="btn btn-default btn-lg btn-block"
                                        onclick="javascript:getReply();">加载更多...</button>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

<div id="WeJudgeGlobalContainer">
    <div id="wj_global_login_container"></div>
    <div id="wj_global_register_container"></div>
    <div id="wj_global_account_card_container"></div>
</div>
<jsp:include flush="true" page="footer.jsp"/>
</body>
</html>
