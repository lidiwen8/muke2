<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<script src="page/pagetool.js" type="text/javascript"></script>
<script type="text/javascript">
	var themid;
	$(function(){
		getTheme(1);//查询主题信息
	});
	
	function getTheme(pageNum){
		var	key=$("#thename").val();//搜索关键字
		$("#thename").val("");
		$.ajax({
			url:"admin/adminThemeServlet",
			type:"get",
			data:{"action":"searchTheme","key":key,"pageNum":pageNum},
			dataType:"json",
			success:function(data) {
                if (data.res == 5) {
                    alert(data.info);
                    window.location.replace("admin/login.jsp");
                } else {
                    $(".listTheme").empty();//清空主题信息
                    $(data.theme.data).each(function (index, element) {
                        var theme = $(".template").clone();//复制模版
						/* theme.show();//显示模版 */
                        theme.removeClass("template");//移除样式

                        theme.find(".num").text(index + 1);//编号
                        theme.find(".title").text(this.thename);//主题名称
                        theme.find(".count").text("主题贴数"+ "•" +element.count+"个");//主题帖数
                        theme.find(".edit").attr("onclick", "getThemeMsg(" + element.theid + ")");//编辑按钮
                        theme.find(".delete").attr("onclick", "deleteTheme(" + element.theid + ")");//删除按钮

                        $(".listTheme").append(theme);
                    });
                    setPage(pageNum, data.theme.totalPage, "getTheme");
                }
            }
		});

	}
	
	function add(){
		if(confirm("确认添加吗？")){
			var	thename=$("#thename1").val();//添加主题名
			if(thename.length<=0||thename.replace(/\s+/g,"")==""||thename==null){
				alert("主题名不能为空");
				return;
			}
            if(thename.length>30){
                alert("主题名长度不能超过30");
                return;
            }
			$("#thename1").val("");//清空文本框中的主题名信息
			$.ajax({
				url:"admin/adminThemeServlet",
				type:"get",
				data:{"action":"add","thename":thename},
				dataType:"json",
				success:function(data){
					if(data.res==1){
						//添加主题成功
						alert(data.info);
						getTheme(1);
					}else{
						//添加主题失败
                        alert(data.info);
						$(".text-warning").text(data.info);
					}
				}
			});
		};
	}

    function getThemeMsg(theid){
        $.ajax({
            url:"admin/adminThemeServlet",
            async : true,
            type:"post",
            data:{"action":"getThemeMsg","theid":theid},
            dataType:"json",
            success:function(data){
                if(data.res==1){
                    var theme=data.theme;
                    //获取主题信息成功
                    $("#thename2").val(theme.thename);
                    $("#theid").val(theme.theid);
                    $('#edit').modal('show');
                    $('#edit').on('shown.bs.modal',function(e){
                        $('#thename2').focus();       //获取焦点
                    });
                }else{
                    //获取主题信息失败
                    alert(data.info);
                }
            }
        });
	}

    function edit(){
        var	thename=$("#thename2").val();//添加主题名
        var theid=$("#theid").val();
        if(thename.length<=0||thename.replace(/\s+/g,"")==""||thename==null){
            alert("主题名不能为空");
            return;
        }
        if(thename.length>30){
            alert("主题名长度不能超过30");
            return;
        }
        if(confirm("确认保存该《"+thename+"》主题名吗？")){
            $("#thename2").val("");//清空文本框中的主题名信息
            $.ajax({
                url:"admin/adminThemeServlet",
                type:"post",
                data:{"action":"edit","thename":thename,"theid":theid},
                dataType:"json",
                success:function(data){
                    if(data.res==1){
                        //编辑主题成功
                        alert(data.info);
                        getTheme(1);
                    }else{
                        //编辑主题失败
                        alert(data.info);
                        $(".text-warning").text(data.info);
                    }
                }
            });
        };
    }

	function deleteTheme(theid){
		if(confirm("确认删除吗？")){
			$.ajax({
				url:"admin/adminThemeServlet",
				type:"get",
				data:{"action":"delete","theid":theid},
				dataType:"json",
				success:function(data){
					if(data.res==1){
						//删除主题成功
						alert(data.info);
						getTheme(1);
					}else if(data.res==2){
                        if(confirm(data.info)){
                            $.ajax({
                                url:"admin/adminThemeServlet",
                                type:"get",
                                data:{"action":"delete1","theid":theid},
                                dataType:"json",
                                success:function(data){
                                    if(data.res==1){
                                        //删除主题成功
                                        alert(data.info);
                                        getTheme(1);
                                    }
                                    else{
                                        //删除主题失败
                                        $(".text-warning").text(data.info);
                                    }
                                }
                            });
                        };
					}
					else{
						//删除主题失败
						$(".text-warning").text(data.info);
					}
				}
			});
		};
	}
	
</script>
<title>爱之家网站管理后台</title>
</head>
<body>
	<jsp:include flush="fasle" page="header.jsp" />
	<div class="container">		
		<div class="row">
			<div class="col-sm-offset-2 col-sm-8 msgtitle">
				<h3>
					<span class="title">主题管理</span>
				</h3>
				<div class="replybtn">
					<button type="button" class="btn btn-warning" data-toggle="modal"
							data-target="#add">添加</button>
					<button type="button" class="btn btn-success" data-toggle="modal"
							data-target="#search">搜索</button>
					
				</div>
			</div>
		</div>
		<div class="row template" >
			<div class="col-sm-offset-2 col-sm-8 col-xs-12 msglist">
				<div class="col-sm-2 col-xs-2 num">1</div>
				<div class="col-sm-8 col-xs-7 title">Java</div>
				<div class="col-sm-9 col-xs-7 count">3</div>
				<div>
					<%--<button class="btn btn-primary edit">编辑</button>--%>
					    <button class="btn btn-primary btn-sm edit"><span class="glyphicon glyphicon-pencil">编辑</span></button>
						&nbsp;
						<button class="btn btn-danger btn-sm delete"><span class="glyphicon glyphicon-trash">删除</span></button>
					<%--<button class="btn btn-danger delete">删除</button>--%>
				</div>
			</div>
		</div>
		
		<div  class="listTheme">
		
		</div>
		
		<!-- 显示分页 -->
		<div class="row" style="text-align: center">
			<jsp:include  page="/page/pagetool.jsp" />
		</div>

	</div>
	
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="search" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content modalcenter">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title">搜索</h4>
	            </div>
	            <div class="modal-body">
					<form role="form">
						<div class="form-group">
							<label for="thename">主题名：</label>
							<input type="text" class="form-control" id="thename" placeholder="">
						</div>
					</form>
				</div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="getTheme()">搜索</button>
	            </div>
	        </div><!-- /.modal-content -->
	    </div><!-- /.modal -->
	</div>
	
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="add" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	    <div class="modal-dialog">
	        <div class="modal-content modalcenter">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	                <h4 class="modal-title">添加主题</h4>
	            </div>
	            <div class="modal-body">
					<form role="form">
						<div class="form-group">
							<label for="thename">主题名：</label>
							<input type="text" class="form-control" id="thename1" placeholder="">
						</div>
					</form>
				</div>
	            <div class="modal-footer">
	                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="add()">添加</button>
	            </div>
	        </div><!-- /.modal-content -->
	    </div><!-- /.modal -->
	</div>

	<!-- 模态框（Modal） -->
	<div class="modal fade" id="edit" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content modalcenter">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">编辑主题</h4>
				</div>
				<div class="modal-body">
					<form role="form">
						<div class="form-group">
							<label for="thename">主题名：</label>
							<input type="text" class="form-control" id="thename2" name="thename2" autofocus="autofocus">
							<input type="hidden" class="form-control" id="theid">
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="edit()">保存</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal -->
	</div>
	<jsp:include flush="fasle" page="footer.jsp" />
</body>
</html>