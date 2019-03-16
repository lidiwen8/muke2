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
<link rel="stylesheet" href="css/site.css">
<script src="jquery/jquery-2.2.4.min.js" type="text/javascript"></script>
<script src="bootstrap-3.3.7-dist/js/bootstrap.min.js" type="text/javascript"></script>
<title>爱之家网站答疑平台</title>
	<style>
		#to_top{right: 30px; bottom: 30px; position: fixed; cursor: pointer;}
	</style>
<script type="text/javascript">
	var pageNum = 1;
	
	$(function(){
		getMyMsg();//获取我的问题 
	});

    function deleteMymsg(msgid,msgtopic){
        if(confirm("确认删除该<"+msgtopic+">帖子吗？")){
            $.ajax({
                url:"user/userMessageServlet",
                type:"post",
                data:{"action":"deleteMymsg","msgid" : msgid},
                dataType:"json",
                success:function(data){
                    if(data.res==1){
                        alert ("删除成功");
                        window.location.replace("<%=basePath%>user/mymsg.jsp");
                    }else{
                        alert(data.info);
                    }
                }
            });
        };
    }


    function restoreMymsg(msgid){
        $.ajax({
            url : "user/userMessageServlet",
            type : "post",
            async : "true",
            data : {"action" : "restoreMymsg", "msgid" : msgid},
            dataType : "json",
            success : function(data){
                if (data.res == 1){
                    alert ("恢复成功");
                    window.location.replace("<%=basePath%>user/mymsg.jsp");
                }
                else {
                    alert(data.info);
                }
            }
        });
    }

	function getMyMsg(){
		// ajax 异步获取我的问题
		$.get("user/userMessageServlet",
				{
			"action":"getMyMsg",
			"pageNum":pageNum
			},
			function(data){
			
				if(data.res==1){
				    $("#kk").text("我的问题-"+data.message.rows+"个");
					$.each(data.message.data,function(index,element){
						var msg=$(".template").clone();//复制模版
						msg.show();//显示
						msg.removeClass("template");//移除模版
						msg.find(".title").text(element.msgtopic);//帖子标题
						 //添加href属性
						msg.find(".title").attr("href", "<%=basePath%>message.jsp?msgid=" + element.msgid);
						msg.find(".time").text(element.msgtime);//发帖时间
						msg.find(".count").text(element.accessCount+" • "+element.replyCount+" • "+element.likecount);//浏览量和回复量
                        msg.find(".edit_btn").attr("onclick", "getMsg("+element.msgid+")");
                        msg.find(".delete_btn").attr("onclick", "deleteMymsg("+element.msgid+",'"+element.msgtopic+"')");
                        msg.find(".restore_btn").attr("onclick", "restoreMymsg("+element.msgid+")");
                        if (element.state==3){
                            msg.find(".edit_btn").hide();
                            msg.find(".delete_btn").hide();
                            msg.find(".restore_btn").show();
                        }else if(element.state==-1){
                            msg.find(".edit_btn").hide();
                            msg.find(".delete_btn").hide();
                            msg.find(".restore_btn").hide();
						}

                        else {
                            msg.find(".edit_btn").show();
                            msg.find(".delete_btn").show();
                            msg.find(".restore_btn").hide();
                        }
						$(".list").append(msg);//将帖子信息添加到list中
					});
					//加载更多
					pageNum++;
					if(parseInt(data.message.totalPage)>=parseInt(pageNum)){
						
						$("#loadmore").html("加载更多...");
						$("#loadmore").removeAttr("disabled");
					}else{
						$("#loadmore").html("没有更多数据了");
						$("#loadmore").attr("disabled","disabled");
					}
				}
			
			},"json");
	}
    window.onscroll = function(){

        var distance = document.documentElement.scrollTop || document.body.scrollTop; //距离页面顶部的距离

        if( distance >= 300 ) { //当距离顶部超过300px时，显示图片
            document.getElementById('to_top').style.display = "";
        } else { //距离顶部小于300px，隐藏图片
            document.getElementById('to_top').style.display = "none";
        }

        var toTop = document.getElementById("to_top"); //获取图片所在的div

        toTop.onclick = function(){ //点击图片时触发的点击事件
            document.documentElement.scrollTop = document.body.scrollTop = 0; //页面移动到顶部
        }
    }
    //模态框修改自己的问题
    function getMsg(msgid){
        // ajax 异步修改自己的问题
        $.ajax({
            url:"user/userMessageServlet",
            type:"post",
            data:{"action":"getMsg","msgid" : msgid},
            dataType:"json",
            success:function(data){
                if(data.res==1){
                    alert(data.info);
                    window.location.replace("<%=basePath%>user/editmsg.jsp");
                }
                else {
                    alert(data.info);
                }
            }
        });
        return false;
    }

</script>
</head>
<body>
	<jsp:include flush="fasle" page="../header.jsp" />
	<div id="to_top" title="返回顶部">
		<img src="../images/top.png" width="40" height="40" />
	</div>
	<div class="container">
		<br>
		<div class="row">
			<div class="col-sm-12 msgtitle"><h3 id="kk">我的问题</h3></div>
		</div>
		<div class="row">
			<div class="col-sm-8 col-xs-8"><h4>标题</h4></div>
			<div class="col-sm-2 col-xs-4 text-center"><h4>时间</h4></div>
			<div class="col-sm-2 hidden-xs text-center"><h4>浏览 • 回复 • 点赞</h4></div>
			<%--<div class="col-sm-2 col-xs-2 text-center"><h4>操作</h4></div>--%>
		</div>
		<div class="row msglist template">
			<div class="col-sm-12">
				<div class="col-sm-8 col-xs-8 text-limit">
					<a class="title">标题标题标题标题标题标题</a>
				</div>
				<div class="col-sm-2  col-xs-4 text-center time">时间</div>
				<div class="col-sm-2 hidden-xs text-center count">浏览/回复</div>
				<button class="btn btn-primary btn-sm edit_btn"><span class="glyphicon glyphicon-pencil">编辑</span></button>
				<button class="btn btn-danger btn-sm delete_btn"><span class="glyphicon glyphicon-trash">删除</span></button>
				<button class="btn btn-warning restore_btn">恢复</button>
			</div>
		</div>
		<div class="list">
		
		</div>
	
		<div class="row p">
			<div class="col-sm-12">
				<br/>
				<button id="loadmore" disabled="disabled" type="button" class="btn btn-default btn-lg btn-block" 
				onclick="javascript:getMyMsg();">加载更多...</button>
			</div>
		</div>
	</div>
	<jsp:include flush="fasle" page="../footer.jsp" />
</body>
</html>