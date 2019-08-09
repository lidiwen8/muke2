<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<base href="<%=basePath%>">
	<title>尾巴</title>
	<SCRIPT language=javascript>
       
         <!--
        //document.write("");
        function show_date_time(){
            window.setTimeout("show_date_time()", 1000);
            BirthDay=new Date("05-2-2018"); //这里的07-31-2009是指建站日期
            today=new Date();
            timeold=(today.getTime()-BirthDay.getTime());
            sectimeold=timeold/1000
            secondsold=Math.floor(sectimeold);
            msPerDay=24*60*60*1000
            e_daysold=timeold/msPerDay
            daysold=Math.floor(e_daysold);
            e_hrsold=(daysold-e_daysold)*-24;
            hrsold=Math.floor(e_hrsold);
            e_minsold=(hrsold-e_hrsold)*-60;
            minsold=Math.floor((hrsold-e_hrsold)*-60);
            seconds=Math.floor((minsold-e_minsold)*-60);
            span_dt_dt.innerHTML=daysold+"天"+hrsold+"小时"+minsold+"分"+seconds+"秒" ;
        }
        show_date_time();
        //-->
  
	</SCRIPT>
	<style>
		yunxing{
			clear:both; text-align:center; border:1px solid #0099FF;
			background:#FFFFFF; line-height:20px; padding:6px 5px 3px; color:#666;}
	</style>
		</head>
<body>
<div class="footer">
	<div class="container">
		<div class="row footer-top">
			<div class="col-sm-6 col-lg-6">
				<h3>爱之家网站答疑平台</h3>
				<p>爱之家网站是为了方便用户通过输入自己的相关信息来获取想要信息，主要提供一些基本的查询。</p>
				<p>爱之家网站的爱情宣言是
					<a href="http://www.lidiwen.club/CustomerManagement-master_war/huangxiaoqi.html">我爱黄晓琪
						--(李弟文)。</a></p>
			</div>
			<div class="col-sm-6  col-lg-5 col-lg-offset-1">
				<div class="row about">
					<div class="col-xs-3">
						<h4>关于</h4>
						<ul class="list-unstyled">
							<li><a href="http://www.lidiwen.club/CustomerManagement-master_war/XieYi.html">关于我们</a></li>
							<li><a href="https://www.500d.me/cvresume/5246633084/" _blank>个人简介</a></li>
							 <li>
								<a title="微信" id="weixin_btn" rel="external nofollow"  href="javascript:se()">
								<i class="fa fa-weixin" id="weixin_code">广告合作</i>
							   </a>
							</li>
							<li><a href="http://www.lidiwen.club/ForestBlog/applyLink">申请友链</a></li>
							<li><a href="sponsor.jsp">赞助</a></li>
							<li><a href="http://www.lidiwen.club/CustomerManagement-master_war/aiqing.html">照片墙</a></li>
						</ul>
					</div>
					<div class="col-xs-3">
						<h4>联系方式</h4>
						<ul class="list-unstyled">
							<li><a href="http://www.lidiwen.club/CustomerManagement-master_war/login.html"
								   title="官方微博" target="_blank">新浪微博</a></li>
							<li><a href="mailto:#">电子邮件</a></li>
							<li><a href="messag.jsp">我要留言</a></li>
                                                        <li><a href="help_feedback.jsp">我要反馈</a></li>   
						</ul>
					</div>
					<div class="col-xs-3">
						<h4>旗下网站</h4>
						<ul class="list-unstyled">
							<li><a href="http://www.lidiwen.club/CustomerManagement-master_war/login.html" target="_blank">爱之家</a></li>
							<li><a href="http://www.lidiwen.club/ForestBlog/" target="_blank">晓文博客</a></li>
                                                        <li><a href="http://www.lidiwen.club/ForestBlog2.0/" target="_blank">晓文博客V2.0</a></li>
                                                        <li><a href="http://www.lidiwen.club/mybaby/fore_login" target="_blank">相册集</a></li>
                                                        <li><a href="http://lidiwen.club/LongBlog-v2.0-master/index.html" target="_blank">竹林听雨</a></li>
							<li><a href="http://www.lidiwen.club/CustomerManagement-master_war/jumpGame.html" target="_blank">跳跃之战</a></li>
						</ul>
					</div>
					<div class="col-xs-3">
						<h4>赞助商</h4>
						<ul class="list-unstyled">
							<li>
								<a title="黄晓琪" id="weixin_btn" rel="external nofollow"  href="javascript:se2()">
								<i class="fa fa-weixin" id="weixin_code">黄晓琪</i>
							   </a>
							</li>
							<li>
								<a title="李弟文" id="weixin_btn" rel="external nofollow"  href="javascript:se3()">
								<i class="fa fa-weixin" id="weixin_code">李弟文</i>
							   </a>
							</li>
						</ul>
					</div>

				</div>

			</div>

		</div>
		<hr/>
		<div class="row footer-bottom">
			<ul class="list-inline text-center">
				<li><a href="http://www.lidiwen.club/muke_Web/statistics.jsp">网站统计</a>--<a href="http://www.miibeian.gov.cn" target="_blank">粤ICP备18050207号</a></li>
				<br><div class="yunxing">网站已平稳运行<span id="span_dt_dt" style='border:0px solid black;background-color:#FFFFFF' ></span></div>
			</ul>
		</div>
	</div>
</div>

   <script src="http://love.lidiwen.club/layer.js"></script>
<script>
    function se() {
        var img = "<img width='200px' height='200px' src='http://www.lidiwen.club/ForestBlog/img/weixin.jpg'/>";
        layer.open({
            type: 1,
            title:'微信', //显示标题
            shadeClose:true,
            closeBtn: 0,
            area: '200px',
            skin: 'layui-layer-nobg', //没有背景色
            content: img, //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
        });
    }
    function se2() {
        var img = "<img width='220px' height='350px' src='images/晓琪.jpg'/>";
        layer.open({
            type: 1,
            title: false, //不显示标题
            shadeClose:true,
            closeBtn: 0,
            area: '220px',
            skin: 'layui-layer-nobg', //没有背景色
            content: img, //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
        });
    }
function se3() {
        var img = "<img width='220px' height='350px' src='images/李弟文.jpg'/>";
        layer.open({
            type: 1,
            title: false, //不显示标题
            shadeClose:true,
            closeBtn: 0,
            area: '220px',
            skin: 'layui-layer-nobg', //没有背景色
            content: img, //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
        });
    }
</script>
</body>
</html>