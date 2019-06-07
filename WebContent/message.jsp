﻿<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String basePath2 = request.getServerName() + ":" + request.getServerPort() + path;
%>
<%
    String msgId = request.getParameter("msgid");
    if (msgId == null || msgId.equals("")) {
        msgId = "1";
    }

%>
<!DOCTYPE>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0">
    <link rel="stylesheet" href="bootstrap-3.3.7-dist/css/bootstrap.css">
    <script src="jquery/jquery-2.2.4.min.js" type="text/javascript"></script>
    <script src="bootstrap-3.3.7-dist/js/bootstrap.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="jquery/emoji.js"></script>
    <link href="https://cdn.bootcss.com/highlight.js/8.0/styles/monokai_sublime.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/highlight.js/8.0/highlight.min.js"></script>
      <c:if test="${sessionScope.user!= null ||sessionScope.admin!=null }">
        <script type="text/javascript" src="ckeditor/ckeditor.js"></script>
        <%--<script type="text/javascript" src="http://cdn.ckeditor.com/4.11.4/standard/ckeditor.js"></script>--%>
    </c:if>
    <link href="css/Style.css" rel="stylesheet" type="text/css"/>
    <script src="js/scroll_tools.js" type="text/javascript"></script>
    <script type="text/javascript">
        //插件调用
        $(function () {
            $(".scrolltools").scrolltools({alwaysShowToTop: true, alwaysShowToBottom: true});
        });
    </script>
    <link rel="stylesheet" href="css/site.css">
    <title>爱之家网站</title>
    <script type="text/javascript">
        $.fn.modal.Constructor.prototype.enforceFocus = function () {
            modal_this = this
            $(document).on('focusin.modal', function (e) {
                if (modal_this.$element[0] !== e.target && !modal_this.$element.has(e.target).length
                    &&
                    !$(e.target.parentNode).hasClass('cke_dialog_ui_input_select') && !$(e.target.parentNode).hasClass('cke_dialog_ui_input_text')) {
                    modal_this.$element.focus()
                }
            })
        };


        var $modalElement = this.$element;

        $(document).on('focusin.modal', function (e) {

            var $parent = $(e.target.parentNode);

            if ($modalElement[0] !== e.target && !$modalElement.has(e.target).length

                // add whatever conditions you need here:

                &&

                !$parent.hasClass('cke_dialog_ui_input_select') && !$parent.hasClass('cke_dialog_ui_input_text')) {

                $modalElement.focus()
            }
        });
    </script>
    <script type="text/javascript">
        var pageNum = 1;
        var msgId =${param.msgid};
        var userid;
        $(function () {
            getMsg();//查询帖子详细信息
        });

        function getMsg() {
            // Ajax 获取问题信息
            $.get("messageServlet",
                {
                    "action": "getMsg",
                    "msgId": msgId
                },
                function (data) {
                    $('.emoji').emoji();
                    var message = data.message;
                    if (data.res == -1) {
                        alert(data.message);
                        window.location.replace("<%=basePath%>index.jsp");
                    } else {
                        userid = message.userid;
                        $(".title").html(message.msgtopic);//帖子标题
                        $(".badge").html(message.thename);//主题名称
                        var msg = $(".template").clone();//复制模版
                        msg.show();
                        msg.removeClass("template");
                        msg.find(".order").text("\uD83C\uDFC1" + "楼主");
                        if (message.sex == '女') {
                            msg.find(".author").text(message.realname + "\uD83D\uDC78");
                            msg.find(".username").attr("href", "user.jsp?username=" + message.username);
                        } else {
                            msg.find(".author").text(message.realname + "\uD83D\uDD75");
                            msg.find(".username").attr("href", "user.jsp?username=" + message.username);
                        }
                        msg.find(".userimg").attr("src", message.user_img);
                        msg.find(".userimg1").attr("href", "user.jsp?username=" + message.username);
                        msg.find(".sex").text(message.sex);//性别
                        msg.find(".city").text(message.city);
                        $('.emoji').emoji();
                        msg.find(".msgcontent").html("\uD83D\uDE4F" + message.msgcontents);
                        hljs.initHighlightingOnLoad();
                        msg.find(".biaoti").text("\uD83C\uDFC6");
                        msg.find(".time").text(message.msgip + "•" + message.msgtime);
                        if (message.msgupdatetime != null && message.msgupdatetime != "") {
                            msg.find(".replyupdatetime").text(beautify_time(message.msgupdatetime) + "修改 by ");
                            msg.find(".relname").attr("href", "user.jsp?username=" + message.username);
                            msg.find(".relname").text(message.realname);
                        }
                        msg.find(".likecount").text("("+message.likecount+")");
                        msg.find(".zan").attr("onclick", "messagezan(" + message.msgid + "," + message.likecount + ")");
                        $("#msgList").append(msg);
                        getReply();//查询帖子回复信息
                    }
                }, "json");
        }

        function getReply() {
            // Ajax 获取回复信息
            $.get("messageServlet",
                {
                    "action": "getReply",
                    "msgId": msgId,//帖子ID
                    "pageNum": pageNum
                },
                function (data) {
                    if (data.res == -1) {
                        alert("未登录！");
                    }else {

                    $('.emoji').emoji();
                    $.each(data.reply.data, function (index, element) {
                        var reply = $(".template").clone();
                        reply.show();
                        reply.removeClass("template");
                        var number = "\uD83C\uDFA4" + (index + 1 + data.reply.pageNumber * (data.reply.curPage - 1)) + "楼";
                        reply.find(".order").text(number);//楼层
                        reply.find(".userimg").attr("src", this.user_img);
                        reply.find(".userimg1").attr("href", "user.jsp?username=" + this.username);
                        if (this.userid == userid) {
                            if (this.sex == '女') {
                                reply.find(".author").html(this.realname + "\uD83D\uDC78");
                            } else {
                                reply.find(".author").html(this.realname + "\uD83D\uDD75");
                            }
                        } else {
                            reply.find(".author").html(this.realname);
                        }
                        reply.find(".username").attr("href", "user.jsp?username=" + this.username);
                        reply.find(".sex").text(this.sex);//性别
                        reply.find(".city").text(this.city);//城市this.city
                        <c:if test="${empty sessionScope.admin}">
                        <c:if test="${sessionScope.user!= null}">
                        if ($("#userid").val() == this.userid) {
                            reply.find(".edit2").attr("onclick", "getReplyInfo(" + this.replyid + ")");
                            reply.find(".edit2").attr("style", "display:block;");
                            reply.find(".delete_btn").attr("onclick", "deleteReply(" + this.replyid + ",'" + this.replytime + "')");
                            reply.find(".delete_btn").attr("style", "display:block;");
                        }
                        </c:if>
                        </c:if>
                        <c:if test="${sessionScope.admin!= null}">
                        reply.find(".edit2").attr("onclick", "getReplyInfo(" + this.replyid + ")");
                        reply.find(".edit2").attr("style", "display:block;");
                        reply.find(".delete_btn").attr("onclick", "deleteReply(" + this.replyid + ",'" + this.replytime + "')");
                        reply.find(".delete_btn").attr("style", "display:block;");
                        </c:if>
                        if (this.replyupdatetime != null && this.replyupdatetime != "") {
                            reply.find(".replyupdatetime").text(beautify_time(this.replyupdatetime) + "修改 by ");
                            reply.find(".relname").attr("href", "user.jsp?username=" + this.username);
                            reply.find(".relname").text(this.realname);
                        } else {
                            reply.find(".br").append("</br>");
                        }
                        $('.emoji').emoji();
                        reply.find(".msgcontent").html(this.replycontents);//回复内容
                        hljs.initHighlightingOnLoad();
                        reply.find(".biaoti").text("\uD83D\uDD90");
                        reply.find(".time").text(this.replyip + "•" + this.replytime);//IP和时间
                        reply.find(".likecount").text("("+this.likecount+")");
                        reply.find(".zan").attr("onclick", "replyzan(" + this.replyid + "," + this.likecount + ")");
                        $("#msgList").append(reply);
                        $('.emoji').emoji();
                    });
                    //加载更多
                    pageNum++;
                    if (parseInt(data.reply.totalPage) >= parseInt(pageNum)) {
                        $("#loadmore").html("加载更多...");
                        $("#loadmore").removeAttr("disabled");
                        $('.emoji').emoji();

                    } else {
                        $("#loadmore").html("没有更多数据了！");
                        $("#loadmore").attr("disabled", "disabled");
                        $('.emoji').emoji();
                    }
                }
                }, "json");
            $('.emoji').emoji();

        }

        //传入时间戳单位秒
        function beautify_time(timestamp) {
            var mistiming = Math.round(new Date() / 1000) - timestamp / 1000;
            var postfix = mistiming > 0 ? '前' : '后'
            mistiming = Math.abs(mistiming)
            var arrr = ['年', '个月', '星期', '天', '小时', '分钟', '刚刚'];
            var arrn = [31536000, 2592000, 604800, 86400, 3600, 60, 1];

            for (var i = 0; i < 7; i++) {
                var inm = Math.floor(mistiming / arrn[i]);
                if (inm != 0) {
                    if (i == 6) {
                        return arrr[i]
                    }
                    return inm + arrr[i] + postfix
                }
            }
        }

        function replyMsg() {
            showText();
            var replycontents = CKEDITOR.instances.replycontent.getData();
            if ($.trim(replycontents).length == 0) {
                alert('输入不能为空,回帖失败!');
                return;
            }
            else {
                $("textarea[name='replycontent']").val("");
                // if (replycontents.length > 0 && replycontents != null && replycontents != "" && replycontents.replace(/(^s*)|(s*$)/g, "").length != 0) {
                $.post("user/userMessageServlet",
                    {
                        "action": "replyMsg",
                        "msgId": msgId,
                        "replycontent": replycontents
                    },
                    function (data) {
                        if (data.res == 1) {
                            alert(data.info);
                        } else if (data.res == -1) {
                            alert(data.info);
                        } else {
                            alert(data.info);
                        }

                        //加载更多
                        pageNum++;
                        if (parseInt(data.reply.totalPage) >= parseInt(pageNum)) {
                            $("#loadmore").html("加载更多...");
                            $("#loadmore").removeAttr("disabled");

                        } else {
                            $("#loadmore").html("没有更多数据了！");
                            $("#loadmore").attr("disabled", "disabled");
                        }
                    }, "json");
            }
            // Ajax 回复问题

            return false;
            // }
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

        function filterText(sText) {
            var reBadWords = /badword|anotherbadword|爱女人|爱液|按摩棒|拔出来|爆草|包二奶|暴干|暴奸|暴乳|爆乳|暴淫|屄|被操|被插|被干|逼奸|仓井空|插暴|操逼|操黑|操烂|肏你|肏死|操死|操我|厕奴|插比|插b|插逼|插进|插你|插我|插阴|潮吹|潮喷|成人dv|成人电影|成人论坛|成人小说|成人电|成人电影|成人卡通|成人聊|成人片|成人视|成人图|成人文|成人小|成人电影|成人论坛|成人色情|成人网站|成人文学|成人小说|艳情小说|成人游戏|吃精|赤裸|抽插|扌由插|抽一插|春药|大波|大力抽送|大乳|荡妇|荡女|盗撮|多人轮|发浪|放尿|肥逼|粉穴|封面女郎|风月大陆|干死你|干穴|肛交|肛门|龟头|裹本|国产av|好嫩|豪乳|黑逼|后庭|后穴|虎骑|花花公子|换妻俱乐部|黄片|几吧|鸡吧|鸡巴|鸡奸|寂寞男|寂寞女|妓女|激情|集体淫|奸情|叫床|脚交|金鳞岂是池中物|金麟岂是池中物|精液|就去日|巨屌|菊花洞|菊门|巨奶|巨乳|菊穴|开苞|口爆|口活|口交|口射|口淫|裤袜|狂操|狂插|浪逼|浪妇|浪叫|浪女|狼友|聊性|流淫|铃木麻|凌辱|漏乳|露b|乱交|乱伦|轮暴|轮操|轮奸|裸陪|买春|美逼|美少妇|美乳|美腿|美穴|美幼|秘唇|迷奸|密穴|蜜穴|蜜液|摸奶|摸胸|母奸|奈美|奶子|男奴|内射|嫩逼|嫩女|嫩穴|捏弄|女优|炮友|砲友|喷精|屁眼|品香堂|前凸后翘|强jian|强暴|强奸处女|情趣用品|情色|拳交|全裸|群交|惹火身材|人妻|人兽|日逼|日烂|肉棒|肉逼|肉唇|肉洞|肉缝|肉棍|肉茎|肉具|揉乳|肉穴|肉欲|乳爆|乳房|乳沟|乳交|乳头|三级片|骚逼|骚比|骚女|骚水|骚穴|色逼|色界|色猫|色盟|色情网站|色区|色色|色诱|色欲|色b|少年阿宾|少修正|射爽|射颜|食精|释欲|兽奸|兽交|手淫|兽欲|熟妇|熟母|熟女|爽片|爽死我了|双臀|死逼|丝袜|丝诱|松岛枫|酥痒|汤加丽|套弄|体奸|体位|舔脚|舔阴|调教|偷欢|偷拍|推油|脱内裤|文做|我就色|无码|舞女|无修正|吸精|夏川纯|相奸|小逼|校鸡|小穴|小xue|写真|性感妖娆|性感诱惑|性虎|性饥渴|性技巧|性交|性奴|性虐|性息|性欲|胸推|穴口|学生妹|穴图|亚情|颜射|阳具|杨思敏|要射了|夜勤病栋|一本道|一夜欢|一夜情|一ye情|阴部|淫虫|阴唇|淫荡|阴道|淫电影|阴阜|淫妇|淫河|阴核|阴户|淫贱|淫叫|淫教师|阴茎|阴精|淫浪|淫媚|淫糜|淫魔|淫母|淫女|淫虐|淫妻|淫情|淫色|淫声浪语|淫兽学园|淫书|淫术炼金士|淫水|淫娃|淫威|淫亵|淫样|淫液|淫照|阴b|应召|幼交|幼男|幼女|欲火|欲女|玉女心经|玉蒲团|玉乳|欲仙欲死|玉穴|援交|原味内衣|援助交际|张筱雨|招鸡|招妓|中年美妇|抓胸|自拍|自慰|作爱|18禁|99bb|a4u|a4y|adult|amateur|anal|a片|fuck|gay片|g点|g片|hardcore|h动画|h动漫|incest|porn|secom|sexinsex|sm女王|xiao77|xing伴侣|tokyohot|yin荡|贱人|装b|大sb|傻逼|傻b|煞逼|煞笔|刹笔|傻比|沙比|欠干|婊子养的|我日你|我操|我草|卧艹|卧槽|爆你菊|艹你|cao你|你他妈|真他妈|别他吗|草你吗|草你丫|操你妈|擦你妈|操你娘|操他妈|日你妈|干你妈|干你娘|娘西皮|狗操|狗草|狗杂种|狗日的|操你祖宗|操你全家|操你大爷|妈逼|你麻痹|麻痹的|妈了个逼|马勒|狗娘养|贱比|贱b|下贱|死全家|全家死光|全家不得好死|全家死绝|白痴|无耻|sb|杀b|你吗b|你妈的|婊子|贱货|人渣|混蛋|媚外|和弦|兼职|限量|铃声|性伴侣|男公关|火辣|精子|射精|诱奸|强奸|做爱|性爱|发生关系|按摩|快感|处男|猛男|少妇|屌|屁股|下体|a片|内裤|浑圆|咪咪|发情|刺激|白嫩|粉嫩|兽性|风骚|呻吟|阉割|高潮|裸露|不穿|一丝不挂|脱光|干你|干死|我干|裙中性运动|乱奸|乱伦|乱伦类|乱伦小|伦理大|伦理电影|伦理毛|伦理片|裸聊|裸聊网|裸体写真|裸舞视|裸照|美女裸体|美女写真|美女上门|美艳少妇|妹按摩|妹上门|迷幻药|迷幻藥|迷昏口|迷昏药|迷昏藥|迷魂香|迷魂药|迷魂藥|迷奸粉|迷奸药|迷情粉|迷情水|迷情药|迷药|迷藥|谜奸药|骚妇|骚货|骚浪|骚女|骚嘴|色电影|色妹妹|色情表演|色情电影|色情服务|色情图片|色情小说|色情影片|色情表演|色情电影|色情服务|色情片|色视频|色小说|性伴侣|性服务|性福情|性感少|性伙伴|性交|性交视频|性交图片|性奴|性奴集中营|性虐|阴唇|阴道|阴蒂|阴户|阴间来电|阴茎|阴茎增大|阴茎助勃|阴毛|陰唇|陰道|陰戶|淫荡|淫荡美女|淫荡视频|淫荡照片|淫乱|淫靡|淫魔|淫魔舞|淫女|淫情女|淫肉|淫騷妹|淫兽|淫兽学|淫水|淫穴|morphine|摇头丸|迷药|乖乖粉|narcotic|麻醉药|精神药品|爱女人|爱液|按摩棒|拔出来|爆草|包二奶|暴干|暴奸|暴乳|爆乳|暴淫|屄|被操|被插|被干|逼奸|仓井空|插暴|操逼|操黑|操烂|肏你|肏死|操死|操我|厕奴|插比|插b|插逼|插进|插你|插我|插阴|潮吹|潮喷|成人电影|成人论坛|成人色情|成人网站|成人文学|成人小说|艳情小说|成人游戏|吃精|赤裸|抽插|扌由插|抽一插|春药|大波|大力抽送|大乳|荡妇|荡女|盗撮|多人轮|发浪|放尿|肥逼|粉穴|封面女郎|风月大陆|干死你|干穴|肛交|肛门|龟头|裹本|国产av|好嫩|豪乳|黑逼|后庭|后穴|虎骑|花花公子|换妻俱乐部|黄片|几吧|鸡吧|鸡巴|鸡奸|寂寞男|寂寞女|妓女|激情|集体淫|奸情|叫床|脚交|金鳞岂是池中物|金麟岂是池中物|精液|就去日|巨屌|菊花洞|菊门|巨奶|巨乳|菊穴|开苞|口爆|口活|口交|口射|口淫|裤袜|狂操|狂插|浪逼|浪妇|浪叫|浪女|狼友|聊性|流淫|铃木麻|凌辱|漏乳|露b|乱交|乱伦|轮暴|轮操|轮奸|裸陪|买春|美逼|美少妇|美乳|美腿|美穴|美幼|秘唇|迷奸|密穴|蜜穴|蜜液|摸奶|摸胸|母奸|奈美|奶子|男奴|内射|嫩逼|嫩女|嫩穴|捏弄|女优|炮友|砲友|喷精|屁眼|品香堂|前凸后翘|强jian|强暴|强奸处女|情趣用品|情色|拳交|全裸|群交|惹火身材|人妻|人兽|日逼|日烂|肉棒|肉逼|肉唇|肉洞|肉缝|肉棍|肉茎|肉具|揉乳|肉穴|肉欲|乳爆|乳房|乳沟|乳交|乳头|三级片|骚逼|骚比|骚女|骚水|骚穴|色逼|色界|色猫|色盟|色情网站|色区|色色|色诱|色欲|色b|少年阿宾|少修正|射爽|射颜|食精|释欲|兽奸|兽交|手淫|兽欲|熟妇|熟母|熟女|爽片|爽死我了|双臀|死逼|丝袜|丝诱|松岛枫|酥痒|汤加丽|套弄|体奸|体位|舔脚|舔阴|调教|偷欢|偷拍|推油|脱内裤|文做|我就色|无码|舞女|无修正|吸精|夏川纯|相奸|小逼|校鸡|小穴|小xue|写真|性感妖娆|性感诱惑|性虎|性饥渴|性技巧|性交|性奴|性虐|性息|性欲|胸推|穴口|学生妹|穴图|亚情|颜射|阳具|杨思敏|要射了|夜勤病栋|一本道|一夜欢|一夜情|一ye情|阴部|淫虫|阴唇|淫荡|阴道|淫电影|阴阜|淫妇|淫河|阴核|阴户|淫贱|淫叫|淫教师|阴茎|阴精|淫浪|淫媚|淫糜|淫魔|淫母|淫女|淫虐|淫妻|淫情|淫色|淫声浪语|淫兽学园|淫书|淫术炼金士|淫水|淫娃|淫威|淫亵|淫样|淫液|淫照|阴b|应召|幼交|幼男|幼女|欲火|欲女|玉女心经|玉蒲团|玉乳|欲仙欲死|玉穴|援交|原味内衣|援助交际|张筱雨|招鸡|招妓|中年美妇|抓胸|自拍|自慰|作爱|18禁|99bb|a4u|a4y|adult|amateur|anal|a片|fuck|gay片|g点|g片|hardcore|h动画|h动漫|incest|porn|secom|sexinsex|sm女王|xiao77|xing伴侣|tokyohot|yin荡|腐败中国|三个呆婊|你办事我放心|社会主义灭亡|打倒中国|打倒共产党|打倒共产主义|打倒胡锦涛|打倒江泽民|打倒江主席|打倒李鹏|打倒罗干|打倒温家宝|打倒中共|打倒朱镕|抵制共产党|抵制共产主义|抵制胡锦涛|抵制江泽民|抵制江主席|抵制李鹏|抵制罗干|抵制温家宝|抵制中共|抵制朱镕基|灭亡中国|亡党亡国|粉碎四人帮|激流中国|特供|特贡|特共|zf大楼|殃视|贪污腐败|强制拆除|形式主义|政治风波|太子党|上海帮|北京帮|清华帮|红色贵族|权贵集团|河蟹社会|喝血社会|九风|9风|十七大|十7大|17da|九学|9学|四风|4风|双规|南街村|最淫官员|警匪|官匪|独夫民贼|官商勾结|城管暴力执法|强制捐款|毒豺|一党执政|一党专制|一党专政|专制政权|宪法法院|胡平|苏晓康|贺卫方|谭作人|焦国标|万润南|张志新|辛灝年|高勤荣|王炳章|高智晟|司马璐|刘晓竹|刘宾雁|魏京生|寻找林昭的灵魂|别梦成灰|谁是新中国|讨伐中宣部|异议人士|民运人士|启蒙派|选国家主席|民一主|min主|民竹|民珠|民猪|chinesedemocracy|大赦国际|国际特赦|da选|投公|公头|宪政|平反|党章|维权|昝爱宗|宪章|08宪|08xz|抿主|敏主|人拳|人木又|人quan|renquan|中国人权|中国新民党|群体事件|群体性事件|上中央|去中央|讨说法|请愿|请命|公开信|联名上书|万人大签名|万人骚动|截访|上访|shangfang|信访|访民|集合|集会|组织集体|静坐|静zuo|jing坐|示威|示wei|游行|you行|油行|游xing|youxing|官逼民反|反party|反共|抗议|亢议|抵制|低制|底制|di制|抵zhi|dizhi|boycott|血书|焚烧中国国旗|baoluan|流血冲突|出现暴动|发生暴动|引起暴动|baodong|灭共|杀毙|罢工|霸工|罢考|罢餐|霸餐|罢参|罢饭|罢吃|罢食|罢课|罢ke|霸课|ba课|罢教|罢学|罢运|网特|网评员|网络评论员|五毛党|五毛们|5毛党|戒严|jieyan|jie严|戒yan|8的平方事件|知道64|八九年|贰拾年|2o年|20和谐年|贰拾周年|六四|六河蟹四|六百度四|六和谐四|陆四|陆肆|198964|5月35|89年春夏之交|64惨案|64时期|64运动|4事件|四事件|北京风波|学潮|学chao|xuechao|学百度潮|门安天|天按门|坦克压大学生|民主女神|历史的伤口|高自联|北高联|血洗京城|四二六社论|王丹|柴玲|沈彤|封从德|王超华|王维林|吾尔开希|吾尔开西|侯德健|阎明复|方励之|蒋捷连|丁子霖|辛灏年|蒋彦永|严家其|陈一咨|中华局域网|党的喉舌|互联网审查|当局严密封锁|新闻封锁|封锁消息|爱国者同盟|关闭所有论坛|网络封锁|金盾工程|gfw|无界浏览|无界网络|自由门|何清涟|中国的陷阱|汪兆钧|记者无疆界|境外媒体|维基百科|纽约时报|bbc中文网|华盛顿邮报|世界日报|东森新闻网|东森电视|星岛日报|wikipedia|youtube|googleblogger|美国广播公司|英国金融时报|自由亚洲|自由时报|中国时报|反分裂|威胁论|左翼联盟|钓鱼岛|保钓组织|主权|弓单|火乍|木仓|石肖|核蛋|步qiang|bao炸|爆zha|baozha|zha药|zha弹|炸dan|炸yao|zhadan|zhayao|hmtd|三硝基甲苯|六氟化铀|炸药配方|弹药配方|炸弹配方|皮箱炸弹|火药配方|人体炸弹|人肉炸弹|解放军|兵力部署|军转|军事社|8341部队|第21集团军|七大军区|7大军区|北京军区|沈阳军区|济南军区|成都军区|广州军区|南京军区|兰州军区|颜色革命|规模冲突|塔利班|基地组织|恐怖分子|恐怖份子|三股势力|印尼屠华|印尼事件|蒋公纪念歌|马英九|mayingjiu|李天羽|苏贞昌|林文漪|陈水扁|陈s扁|陈随便|阿扁|a扁|告全国同胞书|台百度湾|台完|台wan|taiwan|台弯|湾台|台湾国|台湾共和国|台军|台独|台毒|台du|taidu|twdl|一中一台|打台湾|两岸战争|攻占台湾|支持台湾|进攻台湾|占领台湾|统一台湾|收复台湾|登陆台湾|解放台湾|解放tw|解决台湾|光复民国|台湾独立|台湾问题|台海问题|台海危机|台海统一|台海大战|台海战争|台海局势|入联|入耳关|中华联邦|国民党|x民党|民进党|青天白日|闹独立|duli|fenlie|日本万岁|小泽一郎|劣等民族|汉人|汉维|维汉|维吾|吾尔|热比娅|伊力哈木|疆独|东突厥斯坦解放组织|东突解放组织|蒙古分裂分子|列确|阿旺晋美|藏人|臧人|zang人|藏民|藏m|达赖|赖达|dalai|哒赖|dl喇嘛|丹增嘉措|打砸抢|西独|藏独|葬独|臧独|藏毒|藏du|zangdu|支持zd|藏暴乱|藏青会|雪山狮子旗|拉萨|啦萨|啦沙|啦撒|拉sa|lasa|la萨|西藏|藏西|藏春阁|藏獨|藏独|藏独立|藏妇会|藏青会|藏字石|xizang|xi藏|x藏|西z|tibet|希葬|希藏|硒藏|稀藏|西脏|西奘|西葬|西臧|援藏|bjork|王千源|安拉|回教|回族|回回|回民|穆斯林|穆罕穆德|穆罕默德|默罕默德|伊斯兰|圣战组织|清真|清zhen|qingzhen|真主|阿拉伯|高丽棒子|韩国狗|满洲第三帝国|满狗|鞑子|江丑闻|江嫡系|江毒|江独裁|江蛤蟆|江核心|江黑心|江胡内斗|江祸心|江家帮|江绵恒|江派和胡派|江派人马|江泉集团|江人马|江三条腿|江氏集团|江氏家族|江氏政治局|江氏政治委员|江梳头|江太上|江戏子|江系人|江系人马|江宰民|江贼|江贼民|江主席|麻果丸|麻将透|麻醉弹|麻醉狗|麻醉枪|麻醉槍|麻醉药|麻醉藥|台独|台湾版假币|台湾独立|台湾国|台湾应该独立|台湾有权独立|天灭中共|中共帮凶|中共保命|中共裁|中共党文化|中共腐败|中共的血旗|中共的罪恶|中共帝国|中共独裁|中共封锁|中共封网|中共腐败|中共黑|中共黑帮|中共解体|中共近期权力斗争|中共恐惧|中共权力斗争|中共任用|中共退党|中共洗脑|中共邪教|中共邪毒素|中共政治游戏|福音会|中国教徒|统一教|观音法门|清海无上师|盘古|李洪志|志洪李|李宏志|轮功|法轮|轮法功|三去车仑|氵去车仑|发论工|法x功|法o功|法一轮一功|轮子功|车仑工力|法lun|fa轮|法lg|flg|falungong|大法弟子|大纪元|dajiyuan|明慧网|明慧周报|正见网|新唐人|伪火|退党|tuidang|退dang|超越红墙|自fen|真善忍|九评|9评|9ping|九ping|jiuping|藏字石|集体自杀|自sha|zi杀|suicide|titor|逢8必灾|逢八必灾|逢9必乱|逢九必乱|朱瑟里诺|根达亚文明|诺查丹玛斯|人类灭亡进程表|按照马雅历法|推背图|推bei图|济世灵文|诸世纪|电狗|电话定位器|电话拦截器|电话窃听|电话监|电话交友|电话追杀系统|电击枪|电鸡|电警棒|枪出售|枪的制|枪货到|枪决女犯|枪模|枪手|枪销售|枪械制|枪子弹|售步枪|售纯度|售单管|售弹簧刀|售防身|售狗子|售虎头|售火药|售假币|售健卫|售军用|售猎枪|售氯胺|售麻醉|售枪支|售热武|售三棱|售手枪|售五四|售一元硬|售子弹|售左轮|亚砷（酸）酐|亚砷酸钾|亚砷酸钠|亚硒酸|亚硒酸二钠|亚硒酸镁|亚硒酸钠|亚硒酸氢钠|亚硝酸乙酯|亚硝酰乙氧|氧化二丁基锡|氧化汞|氧化铊|氧化亚铊|氧氯化磷|原装弹|原子弹方法|原子弹清单|安眠酮|代血浆|普萘洛尔|呋塞米|西布曲明|testosterone|胰岛素样生长因子|促红细胞生成素|地西泮|尼可刹米|甲睾酮|adrenaline|erythropoietin|地奈德|莫达非尼|氯噻嗪|苯巴比妥|促性腺激素|泼尼松|麻黄草|雄烯二醇|地塞米松|tamoxifen|strychnine|androst|新型毒品|杜冷丁|兴奋剂|mdma|海洛因|海luo因|heroin|diamorphine|diacetylmorphine|鸦片|阿芙蓉|咖啡因|cocain|三唑仑|美沙酮|麻古|k粉|凯他敏|ketamine|冰毒|苯丙胺|cannabis|大麻|爱他死|氯胺酮|benzodiazepines|甲基安非他明|安非他命|吗啡|打人|打人|拆迁|拆迁|纠纷|纠纷|盗窃|盗窃|安眠酮|代药物毒品类：血浆|普萘洛尔|呋塞米|西布曲明|testosterone|胰岛素样生长因子|促红细胞生成素|地西泮|尼可刹米|甲睾酮|adrenaline|erythropoietin|地奈德|莫达非尼|氯噻嗪|苯巴比妥|促性腺激素|泼尼松|麻黄草|雄烯二醇|地塞米松|tamoxifen|strychnine|androst|新型毒品|杜冷丁|兴奋剂|mdma|海洛因|海luo因|heroin|diamorphine|diacetylmorphine|鸦片|阿芙蓉|咖啡因|cocain|三唑仑|美沙酮|麻古|k粉|凯他敏|ketamine|冰毒|苯丙胺|cannabis|大麻|爱他死|氯胺酮|benzodiazepines|甲基安非他明|安非他命|吗啡|morphine|摇头丸|迷药|乖乖粉|narcotic|麻醉药|精神药品|专业代理|帮忙点一下|帮忙点下|请点击进入|详情请进入|私人侦探|私家侦探|针孔摄象|调查婚外情|信用卡提现|无抵押贷款|广告代理|原音铃声|借腹生子|找个妈妈|找个爸爸|代孕妈妈|代生孩子|代开发票|腾讯客服电话|销售热线|免费订购热线|低价出售|款到发货|回复可见|连锁加盟|加盟连锁|免费二级域名|免费使用|免费索取|蚁力神|婴儿汤|售肾|刻章办|买小车|套牌车|玛雅网|电脑传讯|视频来源|下载速度|高清在线|全集在线|在线播放|txt下载|六位qq|6位qq|位的qq|个qb|送qb|用刀横向切腹|完全自杀手册|四海帮|足球投注|地下钱庄|中国复兴党|阿波罗网|曾道人|六合彩|改卷内幕|替考试|隐形耳机|出售答案|考中答案|答an|da案|资金周转|救市|股市圈钱|崩盘|资金短缺|证监会|质押贷款|小额贷款|周小川|刘明康|尚福林|孔丹|汉芯造假|杨树宽|中印边界谈判结果|喂奶门|摸nai门|酒瓶门|脱裤门|75事件|乌鲁木齐|新疆骚乱|针刺|打针|食堂涨价|饭菜涨价|h1n1|瘟疫爆发|yangjia|y佳|yang佳|杨佳|杨j|袭警|杀警|武侯祠|川b26931|贺立旗|周正毅|px项目|骂四川|家l福|家le福|加了服|麦当劳被砸|豆腐渣|这不是天灾|龙小霞|震其国土|yuce|提前预测|地震预测|隐瞒地震|李四光预测|蟾蜍迁徙|地震来得更猛烈|八级地震毫无预报|踩踏事故|聂树斌|万里大造林|陈相贵|张丹红|尹方明|李树菲|王奉友|零八奥运艰|惨奥|奥晕|凹晕|懊运|懊孕|奥孕|奥你妈的运|反奥|628事件|weng安|wengan|翁安|瓮安事件|化工厂爆炸|讨回工资|代办发票|代办各|代办文|代办学|代办制|代辦|代表烦|代开发票|代開|代考|代理发票|代理票据|代您考|代讨债|代写毕|代写论文|代孕|代追债|考后付款|考机构|考考邓|考联盟|考前答案|考前付|考前密卷|考前预测|考试,答案|考试,作弊器|考试包过|考试保|考试答案|考试机构|考试联盟|考试枪|考试作弊|考试作弊器|考研考中|考中答案|透视功能|透视镜|透视扑|透视器|透视眼睛|透视眼镜|透视药|透视仪|打死经过|打死人|打砸办公|打砸抢|安眠酮|代血浆|普萘洛尔|呋塞米|西布曲明|testosterone|胰岛素样生长因子|促红细胞生成素|地西泮|尼可刹米|甲睾酮|adrenaline|erythropoietin|地奈德|莫达非尼|氯噻嗪|苯巴比妥|促性腺激素|泼尼松|麻黄草|雄烯二醇|地塞米松|tamoxifen|strychnine|androst|新型毒品|杜冷丁|兴奋剂|mdma|海洛因|海luo因|heroin|diamorphine|diacetylmorphine|鸦片|阿芙蓉|咖啡因|cocain|三唑仑|美沙酮|麻古|k粉|凯他敏|ketamine|冰毒|苯丙胺|cannabis|大麻|爱他死|氯胺酮|benzodiazepines|甲基安非他明|安非他命|吗啡|KC短信|KC嘉年华|短信广告|短信群发|短信群发器|小6灵通|短信商务广告|段录定|无界浏览|无界浏览器|无界|无网界|无网界浏览|无帮国|KC提示|KC网站|UP8新势力|白皮书|UP新势力|移民|易达网络卡|安魂网|罢工|罢课|纽崔莱七折|手机复制|手机铃声|网关|神通加持法|全1球通|如6意通|清仓|灵动卡|答案卫星接收机|高薪养廉|考后付款|佳静安定片|航空母舰|航空售票|号码百事通|考前发放|成本价|诚信通手机商城|高利贷|联4通|黑庄|黑手党|黑车|联通贵宾卡|联总|联总这声传单|联总之声传单|高息贷款|高干子弟|恭喜你的号码|恭喜您的号码|高干子女|各个银行全称|各种发票|高官|高官互调|高官子女|喝一送一|卡号|复制|监听王|传单|旦科|钓鱼岛|钓鱼台|当官靠后台|党校安插亲信|传九促三|客户端非法字符|刻章|大麻树脂|大麻油|大法|大法弟子|dpp大法|fa lun|falu|发抡|发抡功|洗脑|下法轮|发轮|发伦|发伦功|发仑|发沦|发纶|发论|发论功|发论公|发正念|发囵|发愣|发瞟|罚抡|罚伦|罚仑|罚沦|罚纶|罚囵|筏抡|筏轮|筏伦|筏仑|筏沦|筏纶|筏论|筏囵|伐抡|伐轮|伐伦|伐仑|伐沦|伐论|伐囵|乏抡|乏轮|乏伦|乏仑|乏沦|乏纶|乏论|乏囵|阀抡|阀伦|阀仑|阀沦|阀纶|阀论|阀囵|法 轮|法.轮.功|法L功|法lun功|法功|法会|法抡|法抡功|法轮|法轮大法|法轮佛法|法轮功|法伦|法仑|法沦|法纶|法论|法十轮十功|法西斯|法院|法正|法谪|法谪功|法輪|法囵|法愣|珐.輪功|珐抡|珐轮|珐伦|珐仑|珐沦|五不|五不争鸣论坛|五出三进|五套功法|邝锦文|垡抡|垡轮|垡伦|垡仑|垡沦|垡纶|垡论|垡囵|茳澤民|荭志|闳志|闵维方|氵去|氵去车仑工力|转法轮|砝抡|砝轮|砝伦|砝仑|砝沦|砝纶|真、善、忍|真理教|真善美|真善忍|砝论|砝囵|泓志|屙民|珐纶|珐论|珐囵|falun|Falundafa|fa轮|Flg|弟子|地下教会|炼功|梦网洪志|轮大|抡功|轮功|伦功|摩门教|三水法轮|三唑仑|天皇|天怒|天葬|车臣|达赖|功法|讲法|基督|基督教|护法|回教|教徒|功友|大师|达赖喇嘛|虹志|鸿志|洪传|贯通两极法|光祖|洪吟|洪哲胜|洪志|宏法|观世音|宏志|弘志|古兰经|红志|车库|车仑|经文|腐败|贪污|gcd|共贪党|gongchandang|阿共|共一产一党|产党共|公产党|工产党|共c党|共x党|共铲|供产|共惨|供铲党|供铲谠|供铲裆|共残党|共残主义|共产主义的幽灵|拱铲|老共|中珙|中gong|gc党|贡挡|gong党|g产|狗产蛋|共残裆|恶党|邪党|共产专制|共产王朝|裆中央|土共|土g|共狗|g匪|共匪|仇共|共产党腐败|共产党专制|共产党的报应|共产党的末日|共产党专制|communistparty|症腐|政腐|政付|正府|政俯|政f|zhengfu|政zhi|挡中央|档中央|中国zf|中央zf|国wu院|中华帝国|gong和|大陆官方|北京政权|刘志军|张曙|刘志军|买别墅|玩女人|贪20亿|许宗衡|贪财物|李启红|贪腐财富|落马|高官名单|陈希同|贪污|玩忽职守|有期徒刑|陈良宇|受贿罪|滥用职权|有期徒刑|没收个人财产|成克杰|死刑|程维高|严重违纪|开除党籍|撤销职务|刘方仁|无期徒刑|倪献策|徇私舞弊|梁湘|以权谋私|撤职。|李嘉廷|死刑缓期|张国光|韩桂芝|宋平顺|自杀|黄瑶|双规|陈绍基|判处死刑|剥夺政治权利终身|没收个人全部财产|石兆彬|侯伍杰|王昭耀|剥夺政治权利|杜世成|沈图|叛逃美国|罗云光|起诉|张辛泰|李效时|边少斌|徐鹏航|违纪|收受股票|王乐毅|李纪周|郑光迪|田凤山。|邱晓华|郑筱萸|孙鹤龄|蓝田造假案|于幼军|留党察看|何洪达|朱志刚|杨汇泉|官僚主义|徐炳松|托乎提沙比尔|王宝森|经济犯罪|畏罪自杀。|陈水文|孟庆平|胡长清|朱川|许运鸿|丘广钟|刘知炳|丛福奎|王怀忠|巨额财产|来源不明罪|李达昌|刘长贵|王钟麓|阿曼哈吉|付晓光|自动辞|刘克田|吕德彬|刘维明|双开|刘志华|孙瑜|李堂堂|韩福才 青海|欧阳德 广东|韦泽芳 海南|铁英 北京|辛业江 海南|于飞 广东|姜殿武 河北|秦昌典 重庆|范广举 黑龙江|张凯广东|王厚宏海南|陈维席安徽|王有杰河南|王武龙江苏|米凤君吉林|宋勇辽宁|张家盟浙江|马烈孙宁夏|黄纪诚北京|常征贵州|王式惠重庆|周文吉|王庆录广西|潘广田山东|朱作勇甘肃|孙善武河南|宋晨光江西|梁春禄广西政协|鲁家善 中国交通|金德琴 中信|李大强 神华|吴文英 纺织|查克明 华能|朱小华光大|高严 国家电力|王雪冰|林孔兴|刘金宝|张恩照|陈同海|康日新|王益|张春江|洪清源|平义杰|李恩潮|孙小虹|陈忠|慕绥新|田凤岐|麦崇楷|柴王群|吴振汉|张秋阳|徐衍东|徐发 黑龙江|张宗海|丁鑫发|徐国健|李宝金|单平|段义和|荆福生|陈少勇|黄松有|皮黔生|王华元|王守业|刘连昆|孙晋美|邵松高|肖怀枢|刘广智 空军|姬胜德 总参|廖伯年 北京|《动向》|《争鸣》|《中国民主》|322攻台作战计划|38集团军|3D轮盘|64大屠杀|6合彩|70天大事记|89学潮大屠杀|89学潮血腥屠杀|BB弹|BB枪|fa轮|FL大法|jiuping|SIM卡复制器|SM用品|t牌车|阿宾|阿凡提机|挨了一炮|爱国运动正名|爱国者同盟网站|爱液横流|安眠藥|案的准确|暗访包|八九民|八九学|八九政治|把病人整|把邓小平|把学生整|罢工门|白黄牙签|白小姐|百家乐|百乐二呓|败培训|拜大哥|斑蝥|办本科|办理本科|办理各种|办理票据|办理文凭|办理真实|办理证件|办理证书|办理资格|办文凭|办怔|办证|半刺刀|辦毕业|辦證|帮人怀孕|谤罪获刑|磅解码器|磅遥控器|包办色情娱乐服务|包青天机|包养|宝在甘肃修|保过答案|报复执法|报码|暴菊|暴力袭警|暴力执法|爆发骚|爆菊|北姑|北京帮|北京独立|北京风波|北京黑幕|北京市委黑幕|北京政坛清华名人|北美巡回讲法|北省委门|被指抄袭|被中共|本无码|避孕膜|鞭满|变牌|变牌绝|辩词与梦|冰毒|冰火毒|冰火佳|冰火九重|冰火漫|冰淫传|冰在火上|波胆|波推龙|博彩|博彩娱|博会暂停|博园区伪|不查都|不查全|布卖淫女|部忙组阁|部是这样|才知道只生|财众科技|采花堂|彩宝|彩票|彩票选号机|彩票预测|踩踏事|苍山兰|苍蝇水|操了嫂|操你妈|操嫂子|厕奴|策没有不|插屁屁|察象蚂|拆迁灭|禅密功|长狗|车仑大法|车牌隐|车用电子狗|成佛做主|城管灭|惩公安|惩贪难|持枪证|充气娃|冲凉死|抽着大中|抽着芙蓉|出成绩付|出售发票|出售假币|出售军|出售器官|出售肾|穿透仪器|传九退三|传送答案|吹箭|春宫|春宫图|春水横溢|春药|纯度白|纯度黄|慈悲功|次通过考|催眠水|催情粉|催情药|催情藥|挫仑|达毕业证|达赖|达米宣教会|答案包|答案提供|打标语|打错门|打飞机专|大法弟子|大法轮|大法修炼者|大鸡巴|大雞巴|大纪元|大妓院|大揭露|大奶子|大批贪官|大肉棒|大学暴动|大圆满法|大庄|大嘴歌|贷借款|贷开|戴海静|弹种|当代七整|当官要精|当官在于|党风日下|党棍|党后萎|党禁|党内权力|党内权争|党内危机|党内言事潮|党前干劲|荡妇|刀架保安|导爆索|导的情人|导叫失|导人的最|导人最|导小商|到花心|盗号|盗取qq|盗取密码|得财兼|的同修|灯草和|等级證|等人老百|等人是老|等人手术|邓爷爷转|邓颖超日记|邓玉娇|抵制北京奥运|地磅解码器|地磅遥控器|地产之歌|地下先烈|帝国之梦|递进民主|递纸死|第二首都|第三次世界大战|第五代红人|第五代接班梯队|颠覆,共产党|颠覆,共产主义|颠覆,胡锦涛|颠覆,温家宝|颠覆,中共|颠覆,朱镕基|点金商务|点数优惠|电子狗导航手机|甸果敢|蝶舞按|丁香社|丁子霖|顶花心|顶贴机|顶帖器|东北独立|东方闪电|东复活|东京热|东突|东伊运|東京熱|董元辰|洞小口紧|都当警|都当小姐|都进中央|毒蛇钻|独夫民贼|独立台湾|赌博机|赌恒指|赌具|赌球|赌球网|短信截|短信猫|短信群发|短信投票业务|段桂清|对共产党清算|对日强硬|多党执政|多美康|躲猫猫|俄罗斯轮盘|俄羅斯|恶党|恶搞人民币|恶警|恶势力操|恶势力插|恩氟烷|儿园惨|儿园砍|儿园杀|儿园凶|二奶大|二十四事件|发仑da发|发伦工|发轮功|发轮功陈果|发牌绝|发票出|发票代|发票代开|发票销|发贴工具|发贴机|發票|法0功|法车仑|法功|法拉盛|法拉盛缅街|法力像佛|法仑功|法伦|法伦功|法轮|法论功|法能功|法维权|法西藏主义|法一轮|法院给废|法正乾|珐(工力)学|反测速雷|反对共产党|反对共产主义|反腐总攻|反攻大陆|反共传单|反共言论|反雷达测|反雷达测速|反屏蔽|反社会主义|反中共黑色暴力|范燕琼|方迷香|芳香型智悟气功|防电子眼|防身枪|防身药水|房贷给废|仿真枪|仿真手枪|仿真证|费私服|封锁消|佛怀煽仇录|佛同修|夫妻交换|福尔马林|福娃的預|福娃頭上|福香巴|府包庇|府集中领|妇销魂|附送枪|复印件生|复印件制|赴港生子|富民穷|富婆给废|改号软件|干扰器|感扑克|冈本真|肛交|肛门是邻|岡本真|钢针狗|钢珠枪|港澳博球|港馬會|港鑫華|高就在政|高考,答案|高考黑|高莺莺|搞媛交|告长期|告洋状|格证考试|蛤蟆转世|个人圆满说|个四小码|个邪的党|各类考试|各类文凭|根浴|跟帖器|跟踪器|工程吞得|工力人|弓弩|公安错打|公开小姐|攻官小姐|供应发票|共产主义黑皮书|共狗|共和国2049|共青背景|共青团派|共王储|共字玄机|狗粮|狗娘养的|狗屁专家|狗日的|狗友|古方迷香|鼓动一些|乖乖粉|观音法门|官匪一家|官商勾|官也不容|官因发帖|光学真题|广安事件|广场事件|跪真相|滚圆大乳|国办发|国际投注|国家妓|国家软弱|国家吞得|国库折|国统纲领|国统会|国一九五七|國內美|哈狗帮|哈药直销|海访民|海洛因|豪圈钱|号屏蔽器|合彩|和狗交|和狗性|和狗做|黑火药的|黑社会|红病历|红床|红客联盟|红色恐怖|红外,透视|红外透视|洪志|紅色恐|胡的接班人|胡江风云|胡江关系|胡江内斗|胡江曾|胡江争斗|胡紧掏|胡紧套|胡锦涛,腐败|胡錦濤|胡进涛|胡派军委|胡派人马|胡适眼|胡下台|胡耀邦|湖淫娘|虎头猎|护卫团|华藏功|华国锋|华门开|华闻|化学扫盲|划老公|话在肉身显现|还会吹萧|还看锦涛|环球证件|换妻|皇冠投注|皇家轮盘|黄冰|黄菊遗孀|黄色,电影|黄色电影|黄色小电影|回汉冲突|回民暴动|回民猪|回忆六四|昏药|浑圆豪乳|活不起|活体取肾|活摘器官|火车也疯|机定位器|机号定|机号卫|机卡密|机屏蔽器|鸡巴|积克馆|基本靠吼|基督灵恩布道团|绩过后付|激光气|激光汽|激流中国|激情,电影|激情,图片|激情电|激情电话|激情电影|激情短|激情交友|激情妹|激情炮|激情视频|激情小电影|级办理|级答案|急需嫖|疾病业债说|集体打砸|集体腐|集体抗议|挤乳汁|擠乳汁|记号扑克|纪念达赖喇嘛流亡49周年|纪念文革|妓女|妓女的口号|寂寞少妇|加油机干扰器|佳静安定|佳静安定片|家一样饱|家属被打|甲虫跳|甲流了|假币出售|假发票|假文凭|假证件|奸成瘾|奸杀|兼职上门|监听器|监听头|监听王|监狱管理局|监狱里的斗争|简易炸|贱货|贱人|江z民|疆獨|疆独|讲法传功|蒋彦永|叫鸡|叫自慰|揭贪难|姐包夜|姐服务|姐兼职|姐上门|解码开锁|解密软件|解体的命运|解体中共|金扎金|金钟气|津大地震|津地震|津人治津|进来的罪|禁书|禁网禁片|京地震|京夫子|京要地震|经典谎言|精子射在|警察被|警察的幌|警察殴打|警察说保|警车雷达|警方包庇|警匪一家|警徽|警民冲突|警用品|径步枪|敬请忍|靖国神社|究生答案|九龙论坛|九评共|九十三运动|酒象喝汤|酒像喝汤|救度众生说|就爱插|就要色|菊暴|菊爆|菊花洞|举国体|巨乳|据说全民|绝食声|军长发威|军刺|军品特|军用手|军转|卡辛纳大道和三福大道交界处|开苞|开邓选|开平,轮奸|开平,受辱|开锁工具|开天目|開碼|開票|砍杀幼|砍伤儿|看JJ|康没有不|康生丹|康跳楼|抗议磁悬浮|抗议中共当局|磕彰|克分析|克千术|克透视|嗑药|空和雅典|空中民主墙|孔摄像|恐共|控诉世博|控制媒|口交|口手枪|口淫|骷髅死|矿难不公|昆仑女神功|拉帮游说|拉登说|拉开水晶|拉票贿选|拉萨僧人接连抗议|拉线飞机|来福猎|拦截器|狼全部跪|浪穴|老虎机|乐透码|雷管|雷人女官|类准确答|黎阳平|李大轮子|李红痔|李洪X|李洪志|李鹏|李伟信的笔供|李晓英|李咏曰|理各种证|理是影帝|理证件|理做帐报|力骗中央|力月西|历史的伤口|丽媛离|利他林|连发手|联盟党|聯繫電|练功群众|炼大法|两岸才子|两会代|两会又三|聊斋艳|了件渔袍|猎好帮手|猎枪|猎枪销|临震预报|领导忽悠百姓叫号召|领土拿|流亡藏人|流血事件|留四进三|六HE彩|六代接班人|六合采|六合彩|六死|六四内部日记|六四事|六四事件|六四受难者家属证辞|六四信息|六四资料馆|六月联盟|龙虎斗|龙湾事件|隆手指|漏题|陆封锁|陆同修|旅游新报|氯胺酮|轮手枪|轮子小报|论文代|罗干|罗斯小姐|落霞缀|妈了个逼|麻古|麻果配|麻醉乙醚|蟆叫专家|买春|买断|买官卖官|买枪|买肾|买真枪|卖QQ号码|卖春|卖地财政|卖发票|卖枪|卖身|卖肾|卖血|卖银行卡|卖自考|漫步丝|忙爱国|猫眼工具|毛时代大饥荒揭秘|毛一鲜|毛泽东复活|毛泽东侄子|毛贼|毛主席的嫡孙|媒体封锁|每周一死|门按摩|门保健|门徒会|門服務|氓培训|蒙古回归|蒙汗药|蒙汗药粉|蜜穴|免电灯|免费电影|灭共|灭绝罪|灭中共|民储害|民九亿商|民抗议|民殇|民运|民运分子|民主不能等待|民主还专政|民主女神|民族问题|明hui|明慧网|明镜出版社|铭记印尼|摩小姐|魔难论|末世劫难|末世论|牟新生|母乳家|母子乱伦|木齐针|幕没有不|幕前戲|内射|内争人权|男妓|男奴|南充针|嫩逼|嫩穴|嫩阴|泥马之歌|你的西域|拟涛哥|娘两腿之间|鸟巢最少死|妞上门|纽扣摄像机|浓精|怒的志愿|女被人家搞|女激情|女技师|女奴|女人费|女人和狗|女任职名|女上门|女神教|女士服务|女伟哥|女優|鸥之歌|拍肩,药|拍肩神|拍肩神药|拍肩型|拍肩醉迷药|牌技网|派系斗争|盘古乐队|盘口|炮的小蜜|跑官要官|泡友|陪考枪|陪聊|赔率|配有消|喷尿|嫖俄罗|嫖鸡|嫖妓|平惨案|平反六四|平叫到床|仆不怕饮|普提功|普通嘌|期货配|奇迹的黄|奇淫散|骑单车出|气狗|气枪|汽车解码器|汽车走表器|汽狗|汽枪|氣槍|器官贩卖|千禧弘法|铅弹|钱三字经|强权政府|强效失忆药|强硬发言|抢其火炬|切听器|窃听|窃听器|亲共|亲共分子|亲共媒体|侵犯国外专利|钦点接班人|禽流感了|勤捞致|沁园春血|青海无上师|氢弹手|清场内幕|清除负面|清純壆|清官团|清海师父|清海无上师|清华帮|情聊天室|情妹妹|情色,论坛|情色论坛|情杀|情视频|情自拍|氰化钾|氰化钠|请集会|请示威|琼花问|区的雷人|娶韩国|全范围教会|全真证|全自动开锁器|犬交|群发广告机|群发软件|群奸暴|群交|群起抗暴|群体灭绝|群体性事|群众冲击|绕过封锁|惹的国|人弹|人祸|人类灭亡时间表|人类罪恶论|人民币恶搞|人权保护|人宇特能功|人在云上|人真钱|认牌绝|任于斯国|日你妈|日月气功|容弹量|柔胸粉|肉棒|肉洞|肉棍|如厕死|乳交|软弱的国|软弱外交|瑞安事件|萨斯病|赛后骚|赛克网|三班仆人派|三挫|三股势力|三级,电影|三级,影片|三级电影|三级片|三陪|三三九乘元功|三网友|三唑|三唑仑|扫了爷爷|杀害学生|杀指南|沙皇李长春|傻逼|山涉黑|煽动不明|煽动群众|商务短信|商务快车|上海帮|上海独立|上门激|上网文凭|烧公安局|烧瓶的|韶关斗|韶关玩|韶关旭|少妇自拍|社会混|社会主义灭亡|射网枪|涉台政局|涉嫌抄袭|身份证生成器|深喉冰|神的教会|神七假|神韵艺术|神州忏悔录|沈昌功|沈昌人体科技|肾源|升达毕业证|生被砍|生踩踏|生孩子没屁眼|生命树的分叉|生肖中特|生意宝|圣殿教|圣火护卫|圣灵重建教会|圣战不息|盛行在舞|剩火|尸博|失身水|失意药|师涛|狮子旗|十八大接班人|十八等|十大独裁|十大谎|十大禁|十个预言|十类人不|十七大幕|十七大权力争霸战|十七大人事安排|十七位老部长|实毕业证|实际神|实体娃|实学历文|士的年|士的宁|士康事件|独裁者|世界之门|式粉推|视解密|手变牌|手狗|手机,定位器|手机,窃听|手机复制|手机跟|手机跟踪定位器|手机监|手机监听|手机监听器|手机卡复制器|手机魔卡|手机窃听器|手机追|手木仓|手枪|守所死法|兽交|书办理|熟妇|术牌具|双管立|双管平|双筒|谁是胡的接班人|谁是新中国|谁为腐败晚餐买单|水阎王|税务总局致歉|丝护士|丝情侣|丝袜保|丝袜恋|丝袜美|丝袜妹|丝袜网|丝足按|司长期有|司法黑|司考答案|司马璐回忆录|私房写真|私服|私家侦探服务|死法分布|死刑现场|死要见毛|四博会|四川大地震异象揭密|四川朱昱|四大扯|四二六社论|四六级,答案|饲养基地|苏家屯|苏家屯集|诉讼集团|素女,自拍|素女心|速代办|速取证|酸羟亚胺|缩阴|蹋纳税|太王四神|太王四神记|泰兴幼|泰兴镇中|泰州幼|贪官也辛|探测狗|涛共产|涛一样胡|讨厌中国|套牌车|特别党费|特工资|特码|特上门|特务机构|体透视镜|替考|替人体|天安门事件|天朝特|天鹅之旅|天推广歌|天要灭|天音功|田罢工|田田桑|田停工|铁血师|庭审直播|通钢总经|同盟党|统一教|统治术|偷電器|偷啪,dv|偷肃贪|偷听器|偷偷贪|头双管|投毒杀人|投注站|骰子|秃鹰汽|突破封锁|突破网路|屠杀学生|土炮|土枪|推翻独裁|推油按|脱衣舞|脱衣艳|瓦斯手|袜按摩|外蒙回归|外透视镜|外围赌球|万法归一功|万能钥匙|万人骚动|王立军|王益案|网络封锁|网络警察|网民案|网民获刑|网民诬|网站推广软件|微型,监控|微型摄像|为党不为国|围攻上海|维汉员|维权基|维权人|维权谈|伪基百科|伪政府|委坐船|卫星高频头|卫星广播器材|谓的和谐|魏京生|温加饱|温家宝,腐败|温家堡|温切斯特|温休曾退|温影帝|溫家寶|瘟加饱|瘟家宝|瘟假饱|文凭证|纹了毛|闻被控制|闻封锁|瓮安|我的西域|我搞台独|我们自杀吧|我虽死去|乌蝇水|乌云其木格|无帮国|无耻语录|无官正|无界浏览器|无码|无人性的政权|无网界|无线,监听|吾爾開希|五套功|五月天|午夜电|午夜极|武警暴|武警殴|武警已增|武力镇压|武装镇压|务员答案|务员考试|雾型迷|西藏,独立|西藏独立|西藏国家民主党|西藏人民大起义|西藏限|西藏作家组织|西服进去|希脏|习进平|习晋平|席复活|席临终前|席指着护|洗澡死|喜贪赃|先烈纷纷|现大地震|现金投注|现在的党|现在的公安|现在的共产党|现在的警察|现在的社会|现在的政府|线透视镜|限制言|陷害案|陷害罪|霰弹|香港彩|香港独立|香港马会|香港一类|香港总彩|向巴平措|消防灭火枪|消防枪|消业之说|硝化甘|小电影|小活佛|小姐按摩|小姐上门|小穴|肖中特|校骚乱|写两会|泄漏的内|新疆暴乱|新疆独立|新疆叛|新疆限|新金瓶|新生网|新唐人|新搪人|态度蛮横|新中华战记|信访专班|信用卡套现|兴华论谈|兴中心幼|行长王益|形透视镜|性推广歌|性息|胸主席|修炼大法|徐玉元|学骚乱|学生领袖|学位證|血溅人民天堂|血染的风采|血色京机|血色京畿|血腥清场|循环轮回论|丫与王益|严晓玲|言被劳教|言论罪|盐酸曲|颜射|眼镜,透视|燕玲论坛|恙虫病|摇头丸|遥控信号拦截器|要射精了|要射了|要泄了|业力回报|业力轮|夜半加税|夜激情|液体炸|一党独裁|一党私利|一党执政|一党专政|一卡多号|一氯丙酮|一氯乙醛|一码中特|一通功|一通健康法|一小撮别|一肖|一氧化二氟|一氧化汞|一夜激情|伊皮恩|遗情书|乙酰替硫脲|乙酰亚砷酸铜|异硫氰酸烯丙酯|异氰酸苯酯|异氰酸甲酯|因毒磷|因毒硫磷|银氰化钾|银行卡复制设备|隐蔽式摄像机|隐形,耳机|隐形,摄像机|隐形耳|隐形耳机|隐形喷剂|隐形摄像机|应子弹|婴儿命|婴儿汤|罂粟壳|罂粟籽|蝇毒|影子政府|雍战胜|永伏虫|咏妓|用手枪|优化官员|幽谷三|游精佑|游戏机破解|有偿服务|有偿捐献|有偿肾|有码|有奶不一|右转是政|幼齿类|幼交|娱乐透视|愚民同|愚民政|与狗性|宇宙大法|宇宙毁灭|宇宙主佛|玉蒲团|育部女官|预测答案|冤民大|鸳鸯洗|渊盖苏文|元极功|原砷酸|原一九五七|袁伟民|援藏网|援交|晕倒型|韵徐娘|赞成,西藏,独立|脏毒|脏独|遭便衣|遭到警|遭警察|遭武警|择油录|曾道人|炸弹教|炸弹遥控|炸广州|炸立交|炸药的制|炸药配|炸药制|粘氯酸|张春桥|张宏宝|张宏堡|张文中|张志新|找枪手|找援交|找政法委副|赵紫阳|针刺案|针刺伤|针刺事|针刺死|针孔摄象机|针孔摄像机|侦探设备|真钱,百家乐|真钱斗地|真钱投注|真善忍|真实文凭|震惊一个民|震其国土|证到付款|证件公司|证件集团|证生成器|证书办|政府无能|政论区|政治风波|政治局十七|政治人祸的源头|支那|支那猪|植物冰|指纹考勤|指纹膜|指纹套|制服诱|制手枪|制证定金|制作证件|治疗红斑狼疮|治疗性病|治疗乙肝|治疗肿瘤|中办发|中的班禅|中国不强|中国复兴党|中国高层权力斗争|中国共和党|中国官场情杀案|中国过渡政府|中国海外腐败兵团|中国没有自由|中国人民党|中国实行血腥教育|中国贪官在海外|中国网络审查|中国舆论监督网周洪|中国正义党|中国政府封锁消息|中国支配下的朝鲜经济|中国猪|中华昆仑女神功|中华养生益智功|中华养生益智气|中南海的权力游戏|中南海斗争|中南海恩仇录|中南海黑幕|中南海权力斗争|中石油国家电网倒数|中特|中央黑幕|中正纪念歌|中组部前部长直言|种公务员|种学历证|众像羔|重亚硒酸钠|重阳兵变|州惨案|州大批贪|州三箭|宙最高法|昼将近|朱镕基,腐败|主神教|主席忏|属灵教|住英国房|助考|助考网|转法轮|转法论|转是政府|赚钱资料|庄家|装弹甲|装枪套|装消音|追债公司|追踪,定位|梓健特药|自动群发|自己找枪|自杀手册|自杀指南|自慰用|自由门|自由圣|自由西藏|自由西藏学生运动|总会美女|走私车|足交|足球,博彩|足球玩法|最后圆满|醉钢枪|醉迷药|醉乙醚|尊爵粉|左棍|左转是政|作弊器|作各种证|作硝化甘/gi;
            return sText.replace(reBadWords, "****");
        }

        function showText() {
            var oInput1 = CKEDITOR.instances.replycontent.getData();
            oInput1 = filterText(oInput1);
            CKEDITOR.instances.replycontent.setData(oInput1);
        }

        function showText2() {
            var oInput1 = CKEDITOR.instances.replycontent2.getData();
            oInput1 = filterText(oInput1);
            CKEDITOR.instances.replycontent2.setData(oInput1);
        }


        function replyzan(replyid, likecount) {
            $.ajax({
                url: "user/userMessageServlet",
                type: "POST",
                async: "true",
                data: {"action": "replyzan", "likecount": likecount, "replyid": replyid},
                dataType: "json",
                success: function (data) {
                    if (data.res == 1) {
                        // location.reload();//重新加载页面
                    } else {
                        alert(data.info);
                    }
                },
                error: function () {
                    window.location.href = "login.jsp";
                }

            });
        }

        function messagezan(msgid, likecount) {
            $.ajax({
                url: "user/userMessageServlet",
                type: "POST",
                async: "true",
                data: {"action": "messagezan", "likecount": likecount, "msgid": msgid},
                dataType: "json",
                success: function (data) {
                    if (data.res == 1) {
                        // location.reload();//重新加载页面
                    } else {
                        alert(data.info);
                    }
                },
                error: function () {
                    window.location.href = "login.jsp";
                }
            });
        }

        function getReplyInfo(replyid) {
            $.ajax({
                url: "user/userMessageServlet",
                async: true,
                type: "post",
                data: {"action": "getReplyInfo", "replyid": replyid},
                dataType: "json",
                success: function (data) {
                    if (data.res == 1) {
                        var reply = data.reply;
                        // ckeditor的textarea赋值必须用下面的方法
                        CKEDITOR.instances.replycontent2.setData(reply.replycontents);
                        $("#replyid").val(reply.replyid);
                        $('#edit').modal('show');
                        $('#edit').on('shown.bs.modal', function (e) {
                            CKEDITOR.instances.replycontent2.focus(); //获取焦点
                        });
                    } else {
                        //获取回复信息失败
                        alert(data.info);
                    }
                }
            });
        }

        function updateReply() {
            showText2();
            //帖子内容
            var replycontent = CKEDITOR.instances.replycontent2.getData();
            var replyid = $("#replyid").val();
            if ($.trim(replycontent).length == 0) {
                alert('输入不能为空,编辑回复信息失败!');
                return;
            }
            else {
                $.ajax({
                    url: "user/userMessageServlet",
                    type: "post",
                    data: {
                        "action": "updateReply",
                        "replycontent": replycontent,
                        "replyid": replyid
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.res == 1) {
                            alert(data.info);
                            // location.reload();//重新加载页面
                        } else {
                            alert(data.info);
                        }

                    }
                });
                return false;
            }
        }

        function deleteReply(replyid, replytime) {
            if (confirm("确认删除你于" + replytime + "时所发布的回复信息吗？")) {
                $.ajax({
                    url: "user/userMessageServlet",
                    type: "post",
                    data: {"action": "deleteReply", "replyid": replyid},
                    dataType: "json",
                    success: function (data) {
                        if (data.res == 1) {
                            alert(data.info);
                            // location.reload();//重新加载页面
                        } else {
                            alert(data.info);
                        }
                    }
                });
            }
            ;
        }

        function collection(){
            $.get("user/userCenterServlet",
                {
                    "action": "UsercollectionMsgid",
                    "msgid": msgId
                },
                function (data) {
                    if (data.res == 1) {
                        alert(data.info);
                    } else {
                        alert(data.info);
                    }
        }, "json");
        }

        var websocket = null;
        var wsUrl = "ws://<%=basePath2%>/websocket";
        var lockReconnect = false;  //避免ws重复连接
        var reconnectcount=0;
        createWebSocket(wsUrl);   //连接ws
        //判断当前浏览器是否支持WebSocket
        function createWebSocket(wsUrl) {
            try {
                if ('WebSocket' in window) {
                    //建立连接，这里的/websocket ，是ManagerServlet中开头注解中的那个值
                    websocket = new WebSocket(wsUrl);
                } else if ('MozWebSocket' in window) {
                    websocket = new MozWebSocket(wsUrl);
                }
                else {
                    alert('您的浏览器不支持websocket协议,建议使用新版谷歌、火狐等浏览器，请勿使用IE10以下浏览器，360浏览器请使用极速模式，不要使用兼容模式！');
                }
                //连接发生错误的回调方法
                websocket.onerror = function () {
                    reconnect(wsUrl);
                    setMessageInnerHTML("WebSocket连接发生错误");
                };
                //连接成功建立的回调方法
                websocket.onopen = function () {
                    heartCheck.reset().start();      //心跳检测重置
                    setMessageInnerHTML("WebSocket连接成功");
                }
                //接收到消息的回调方法
                websocket.onmessage = function (event) {
                    heartCheck.reset().start();      //拿到任何消息都说明当前连接是正常的
                    setMessageInnerHTML(event.data);
                    if(event.data==msgId||event.data=="add"+msgId){
                        location.reload();
                    }
                }
                //连接关闭的回调方法
                websocket.onclose = function () {
                    reconnect(wsUrl);
                    setMessageInnerHTML("WebSocket连接关闭");
                }
            } catch (e) {
                reconnect(wsUrl);
            }
        }
        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function () {
            closeWebSocket();
        }
        //将消息显示在网页上
        function setMessageInnerHTML(innerHTML) {
            $('message').innerHTML += innerHTML + '<br/>';
        }
        //关闭WebSocket连接
        function closeWebSocket() {
            websocket.close();
        }
        function reconnect(wsUrl) {
            if(lockReconnect) return;
            lockReconnect = true;
            if(reconnectcount>=10){
                websocket.close();//重连超过10次共30秒自动放弃连接请求
                return;
            }
            setTimeout(function () {     //没连接上会一直重连，设置延迟避免请求过多
                createWebSocket(wsUrl);
                reconnectcount++;
                lockReconnect = false;
            }, 3000);
        }
        //心跳检测
        var heartCheck = {
            timeout: 540000,        //9分钟发一次心跳
            timeoutObj: null,
            serverTimeoutObj: null,
            reset: function(){
                clearTimeout(this.timeoutObj);
                clearTimeout(this.serverTimeoutObj);
                return this;
            },
            start: function(){
                var self = this;
                this.timeoutObj = setTimeout(function(){
                    //这里发送一个心跳，后端收到后，返回一个心跳消息，
                    //onmessage拿到返回的心跳就说明连接正常
                    websocket.send("ping");
                    self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了
                        websocket.close();     //如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
                    }, self.timeout)
                }, this.timeout)
            }
        }
    </script>
</head>
<body>
<jsp:include flush="true" page="header.jsp"/>
<div class="scrolltools">
            <span>
                <a title="返回顶部" class="toTop"></a>
            </span>
    <span>
                <a title="返回底部" class="toBottom"></a>
            </span>
</div>
<div id="to_top" title="返回顶部">
    <img src="images/top.png" width="40" height="40"/>
</div>
<div class="container" id="msgList">
    <div class="row">
        <div class="col-sm-12 msgtitle">
            <h3>
                <span class="title">该帖子不存在</span>&nbsp;&nbsp;<span class="badge">暂无</span>
            </h3>
            <div class="replybtn">
                <c:if test="${sessionScope.user == null}">
                    <button type="button" class="btn btn-success" data-toggle="modal"
                            onclick="alert('请先登录！')">回复
                    </button>
                </c:if>
                <c:if test="${sessionScope.user != null}">
                    <button type="button" class="btn btn-success" data-toggle="modal"
                            data-target="#reply">回复
                    </button>
                </c:if>
                <c:if test="${sessionScope.user == null}">
                    <button type="button" class="btn btn-info" data-toggle="modal"
                            onclick="alert('请先登录！')">收藏
                    </button>
                </c:if>
                <c:if test="${sessionScope.user != null}">
                    <button type="button" class="btn btn-info" onclick="collection()">收藏</button>
                </c:if>

            </div>
        </div>
    </div>
    <div class="row reply template">
        <div class="col-sm-12" style="overflow: hidden;">
            <div class="emoji">
                <div class="rightinfo order">0楼</div>
            </div>
            <script type="text/javascript">
                $('.emoji').emoji();
            </script>
        </div>
        <div class="col-sm-2 col-xs-2">
            <a href='' class="username">
                <div class="author">用户已经隐藏</div>
            </a>
            <div>
                <a href='' class="userimg1">
                    <img style="border-radius:50%" width="80px" height="80px" class="userimg" name="userimg"
                         id="userimg" style="vertical-align:middle">
                </a>
            </div>
            <div class="sex">未知</div>
            <div class="city">未知</div>
        </div>
        <div class="col-sm-10 col-xs-10">
            <div class="emoji">
                <div class="msgcontent"></div>
                <div align="left" style="float:left">
                    <button class="btn btn-primary btn-sm edit2" style="display: none"><span
                            class="glyphicon glyphicon-pencil">编辑</span></button>
                </div>
                <div align="right">
                    <button class="btn btn-danger btn-sm delete_btn" style="display: none"><span
                            class="glyphicon glyphicon-trash">删除</span></button>
                </div>
                <span class="br"></span>
                <span class="replyupdatetime" style="white-space:nowrap;"></span><a class="relname"></a>
                <div>
                    <button class="btn btn-white btn-xs zan"
                            style="height: 22px;float: right;">
                        <i class="fa fa-thumbs-up"></i><img alt=""
                                                             src="ckeditor/plugins/smiley/images/79.gif"
                                                             style="height:17px; width:17px" title=""/>&nbsp;点赞&nbsp;
                        <span class="likecount"></span>
                    </button>
                </div>
                <div class="biaoti"></div>
                <script type="text/javascript">
                    $('.emoji').emoji();
                </script>
            </div>
        </div>
        <div class="col-sm-12" style="overflow: hidden;">
            <div class="rightinfo time"></div>
        </div>
    </div>
</div>
<div class="container">
    <div class="row">
        <div class="col-sm-12">
            <br/>
            <c:choose>
            <c:when test="${sessionScope.user!= null ||sessionScope.admin!=null }">
            <button id="loadmore" type="button" class="btn btn-default btn-lg btn-block"
                    onclick="javascript:getReply();" disabled="disabled">加载更多...
                <br/>
                <%--<div id="cyReward" role="cylabs" data-use="reward" style="text-align: center"></div>--%>
                <%--<script type="text/javascript" charset="utf-8" src="https://changyan.itc.cn/js/lib/jquery.js"></script>--%>
                <%--<script type="text/javascript" charset="utf-8" src="https://changyan.sohu.com/js/changyan.labs.https.js?appid=cytLOVtYY"></script>--%>
            </button>
            </c:when>
            <c:otherwise>
                <button id="loadmore" type="button" class="btn btn-default btn-lg btn-block" data-toggle="modal"
                        onclick="alert('请先登录！登陆后查看更多内容')" disabled="disabled">登录后查看更多！
                </button>
            </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<c:if test="${sessionScope.user!= null ||sessionScope.admin!=null }">
<!-- 模态框（Modal） -->
<div class="modal fade" id="reply" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content modalcenter">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="replyLabel">回复：</h4>
            </div>
            <div class="modal-body">
                <form id="replyform">
                    <div class="form-group">
                        <textarea style="width:100%" rows="5" cols="0" name="replycontent" id="replycontent"
                                  onblur="showText();" class="ckeditor"></textarea>
                        <script type="text/javascript">
                            CKEDITOR.replace('replycontent');
                        </script>
                        <ckeditor:replace replace="id_1" basePath="ckeditor/"/>
                    </div>
                    <div class="text-right">
                        <span id="returnMessage" class="glyhicon"></span>
                        <p></p>
                        <button class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button class="btn btn-primary" data-dismiss="modal" onclick="replyMsg();">提交</button>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

    <!-- 模态框（Modal） -->
    <div class="modal fade" id="edit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content modalcenter">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="replyLabel2">编辑：</h4>
                </div>
                <div class="modal-body">
                    <form id="replyform2">
                        <div class="form-group">
                            <textarea style="width:100%" rows="5" cols="0" name="replycontent2" id="replycontent2"
                                      onblur="showText2();" class="ckeditor"></textarea>
                            <input type="hidden" class="form-control" id="replyid">
                            <script type="text/javascript">
                                CKEDITOR.replace('replycontent2');
                            </script>
                        </div>
                        <div class="text-right">
                            <span id="returnMessage2" class="glyhicon"></span>
                            <p></p>
                            <button class="btn btn-default" data-dismiss="modal">关闭</button>
                            <button class="btn btn-primary" data-dismiss="modal" onclick="updateReply();">保存</button>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
</c:if>
<input type="hidden" id="userid" value="${sessionScope.user.userid}">
<span id="message"></span>
<jsp:include flush="false" page="footer.jsp"/>
</body>
<script type="text/javascript" src="jquery/emoji.js"></script>
</html>