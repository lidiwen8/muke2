<%--
  Created by IntelliJ IDEA.
  User: lidiwen
  Date: 2019/2/20
  Time: 22:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="bootstrap-3.3.7-dist/css/bootstrap.css">
    <script src="jquery/jquery-2.2.4.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="jquery/emoji.js"></script>
    <link rel="stylesheet" href="css/site.css">
    <script src="bootstrap-3.3.7-dist/js/bootstrap.min.js" type="text/javascript"></script>
    <title>爱之家网站答疑平台</title>
    <script type="text/javascript">
        var page = 1;
        var key = "";
        var username = "";
        var theid = -1;
        var pageNum = 1;
        $(function () {
            // 加载数据
            $('.emoji').emoji();
            searchMsg2();
        });
        function searchMsg2() {
            if("${param.key}"!=null||"${param.key}"!=""){
                key = "${param.key}";
            }
            if("${param.username}"!=null||"${param.username}"!=""){
               username = "${param.username}";
            }
            var theid = ${param.theid};
            $.ajax({
                url: "messageServlet",
                type: "POST",
                async: "true",
                data: {"action": "searchMsg", "pageNum": pageNum, "key": key, "username": username, "theid": theid},
                dataType: "json",
                success: function (data) {
                    $('.emoji').emoji();
                    if (data.res == 1) {
                        $.each(data.data.data, function (index, msgItem) {
                            var msg = $(".template").clone();
                            msg.show();
                            msg.removeClass("template");
                            $('.emoji').emoji();
                            msg.find(".msg-title").text(msgItem.msgtopic);
                            msg.find(".msg-title").attr("href", "message.jsp?msgid=" + msgItem.msgid);
                            msg.find(".author").text(msgItem.realname + " •  " + msgItem.msgtime);
                            $('.emoji').emoji();
                            msg.find(".msgcontent").html(msgItem.msgcontents);
                            $('.emoji').emoji();
                            msg.find(".count").text(msgItem.accessCount + "次浏览 • " + msgItem.replyCount + "个回复 • " + msgItem.likecount + "个赞 • ");
                            msg.find(".msglink").attr("href", "message.jsp?msgid=" + msgItem.msgid);

                            $("#msgList").append(msg);
                        });
                        $('.emoji').emoji();
                        pageNum++;
                        // 加载更多
                        if (parseInt(data.data.totalPage) >= parseInt(pageNum)) {
                            $("#loadmore").html("加载更多...");
                            $("#loadmore").removeAttr("disabled");
                            $('.emoji').emoji();
                        }else if(parseInt(data.data.totalPage)==0){
                            $("#loadmore").html("没有相关的帖子，请你换个关键字或者主题试试！");
                            $("#loadmore").attr("disabled", "disabled");
                        }
                        else {
                            $("#loadmore").html("没有更多数据了！");
                            $("#loadmore").attr("disabled", "disabled");
                        }
                    }
                }

            });
        }
        window.onscroll = function () {

            var distance = document.documentElement.scrollTop || document.body.scrollTop; //距离页面顶部的距离

            if (distance >= 300) { //当距离顶部超过300px时，显示图片
                document.getElementById('to_top').style.display = "";
            } else { //距离顶部小于300px，隐藏图片
                document.getElementById('to_top').style.display = "none";
            }

            var toTop = document.getElementById("to_top"); //获取图片所在的div

            toTop.onclick = function () { //点击图片时触发的点击事件
                document.documentElement.scrollTop = document.body.scrollTop = 0; //页面移动到顶部
            }
        }

    </script>
</head>
<body>
<jsp:include flush="fasle" page="header.jsp"/>
<div id="to_top" title="返回顶部">
    <img src="images/top.png" width="40" height="40"/>
</div>
<div class="container">
    <div class="row" id="msgList">
        <div class="col-sm-12 msgitem template">
            <div class="emoji">
                <h3><a class="msg-title"></a></h3>
                <p class="author"></p>
                <p class="msgcontent"></p>
                <script type="text/javascript">
                    $('.emoji').emoji();
                </script>
            </div>
            <div class="rightinfo">
                <span class="count"></span>
                <a class="msglink">详细&gt;&gt;</a>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <br/>
            <button id="loadmore" type="button" class="btn btn-default btn-lg btn-block"
                    onclick="javascript:searchMsg2();">加载更多...
            </button>
        </div>
    </div>
</div>
<jsp:include flush="fasle" page="footer.jsp"/>
</body>
</html>
