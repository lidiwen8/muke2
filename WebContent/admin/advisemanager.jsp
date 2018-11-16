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
    <title>爱之家网站管理后台</title>
    <script type="text/javascript">
        var page = 1;
        var username = "";

        $(function(){
            getAdvise(page);
        });

        function getAdvise(pageNum){
            $.ajax({
                url : "adminUserServlet",
                type : "post",
                async : "true",
                data : {"action" : "getAdvise", "pageNum" : pageNum},
                dataType : "json",
                success : function(data){
                    if (data.res==1){
                        $(".list").html("");
                        $.each(data.data.data, function(index, adviseItem) {
                            var advise = $(".template").clone();
                            advise.show();
                            advise.removeClass("template");
                            advise.find(".num").text(index+1);
                            advise.find(".id").text(adviseItem.id);
                            advise.find(".description").text(adviseItem.description.substring(0,6)+"....");
                            advise.find(".description").attr("href", "admin/advisedetails.jsp?id="+adviseItem.id);
                            if(adviseItem.number!=null&&adviseItem.number!=""){
                                advise.find(".number").text(adviseItem.number);
                            }else{
                                advise.find(".number").text("暂无");
                            }
                            advise.find(".createDate").text(adviseItem.createDate);
                            advise.find(".delete").attr("onclick", "deleteAdvise("+adviseItem.id+")");
                            advise.find(".restore").attr("onclick", "restoreAdvise("+adviseItem.id+")");

                            if (adviseItem.states == -1){
                                advise.find(".delete").hide();
                                advise.find(".restore").show();
                            }
                            else {
                                advise.find(".delete").show();
                                advise.find(".restore").hide();
                            }

                            $(".list").append(advise);
                        });

                        page = setPage(pageNum, data.data.totalPage, "getAdvise");

                    } else if (data.res==5){
                        alert(data.info);
                        window.location.replace("admin/login.jsp");
                    }else{
                        alert(data.info);
                    }

                }
            });
        }
        function deleteAdvise(id){
            if(confirm("确认删除吗？")){
                $.ajax({
                    url: "adminUserServlet",
                    type: "post",
                    async: "true",
                    data: {"action": "deleteAdvise", "id": id},
                    dataType: "json",
                    success: function (data) {
                        if (data.res == 1) {
                            alert("删除成功");
                            getAdvise(page);
                        }
                        else {
                            alert(data.info);
                        }
                    }
                });
            };
        }


        function restoreAdvise(id){
            $.ajax({
                url : "adminUserServlet",
                type : "post",
                async : "true",
                data : {"action" : "restoreAdvise", "id" : id},
                dataType : "json",
                success : function(data){
                    if (data.res == 1){
                        alert ("恢复成功");
                        getAdvise(page);
                    }
                    else {
                        alert(data.info);
                    }
                }
            });
        }

        function searchAdvise(){
            username = $("#username").val();
            getAdvise(1);
        }

    </script>
</head>
<body>
<jsp:include flush="fasle" page="header.jsp" />
<div class="container">
    <div class="row">
        <div class="col-sm-12 msgtitle">
            <h3 class="pull-left">建议管理
            </h3>
            <div class="replybtn">
                <button type="button" class="btn btn-success" data-toggle="modal"
                        data-target="#search">搜索</button>
            </div>
        </div>
    </div>
    <div class="col-sm-12">
        <table class="table table-striped">
            <thead>
            <th>编号</th>
            <th>建议内容</th>
            <th>联系方式</th>
            <th>创建日期</th>
            <th>操作</th>
            </thead>
            <tbody class="list">
            </tbody>
        </table>
    </div>
    <table class="table table-striped">
        <%--<tr class="template">--%>
            <%--<th>编号</th>--%>
            <%--<th>建议内容</th>--%>
            <%--<th>联系方式</th>--%>
            <%--<th>创建日期</th>--%>
            <%--<th>操作</th>--%>
        <%--</tr>--%>
        <tr class="template">
            <td class="adviseinfo num">序号</td>
            <td><a class="adviseinfo description" target="_blank">建议</a></td>
            <td class="adviseinfo number">联系方式</td>
            <td class="adviseinfo createDate">创建日期</td>
            <td>
                <button class="btn btn-danger delete">删除</button>
                <button class="btn btn-warning restore">恢复</button>
            </td>
        </tr>
    </table>
    <div class="row" style="text-align: center">
        <jsp:include page="/page/pagetool.jsp"></jsp:include>
    </div>

</div>

<!-- 模态框（Modal） -->
<div class="modal fade" id="search" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content modalcenter">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="replyLabel">搜索</h4>
            </div>
            <div class="modal-body">
                <form role="form">
                    <div class="form-group">
                        <label for="username">序号：</label>
                        <input type="text" class="form-control" id="username" placeholder="">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="searchAdvise()">搜索</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<jsp:include flush="fasle" page="footer.jsp" />
</body>
</html>