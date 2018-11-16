<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE>
<html>
  <head>
    <title>头像上传</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <script src="/jquery/jquery-2.2.4.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/bootstrap-3.3.7-dist/css/bootstrap.css">
  </head>
  <script>
    $(document).ready(function () {
        $("#picture").change(function () {
            var pic=this.files[0];
            preview_picture(pic);
        });
    });
    function preview_picture(pic) {
        var r=new FileReader();
        r.readAsDataURL(pic);
        r.onload = function (ev) {
          $(".k > div > img").attr("src",this.result).show();
        };
    }
      function submit() {
          var name = $("input[name='name']").val();
          var file = $("input[name='uploadFile']").val();
          if(name==null||name==""){
              alert("标题不能为空！");
              $('#name').focus();
              return false;
          }
          if(name.length>20||name.length<2){
              alert("图片的标题长度必须要在2到20之间！");
              $('#name').focus();
              return false;
          }
          if(file==null){
              alert("请选择一个图片上传！");
              $('#uploadFile').focus();
              return false;
          }
          return true;

      }
      function loadmsg() {
          var msg ="<%=request.getAttribute("error")%>";
          if (msg != "null") {
              alert(msg);
          }
      }
  </script>
  <body onload="loadmsg()">
  <div class="row">
    <div class="col-sm-offset-3 col-sm-6 text-center">
      <%
        HttpSession session1=request.getSession();
        String username= (String) session1.getAttribute("username");//获取发布者
        if(username.equals("admin")) {
           %>   <h3>欢迎管理员：<%=session.getAttribute("username")%>-上传头像</h3>
      <%
        }else {
         %><h3>欢迎用户：<%=session.getAttribute("username")%>-上传头像</h3>
       <% }
      %>
    </div>
  </div>
    <form role="form" method="post" action="/ImageSaveServlet" enctype="multipart/form-data" onsubmit="return submit()">
      <div class="form-group">
        <label>标题：</label>
        <input type="text" class="form-control" name="name" placeholder="请输入标题">
      </div>
      <div class="form-group">
        <div class="k">
        <label>选择一张图片:</label>
        <input type="file" name="uploadFile" id="picture">
        <p class="help-block">只能选一张图片哟！</p>
        <div>
          <img src=""  style="display:none;width: 100px;height: 100px;"/>
        </div>
      </div>
      </div>
      <button type="submit" class="btn btn-default">&nbsp;&nbsp;保存</button>
      <button type="button" class="btn btn-warning" onclick="javascript:window.location.href='/user/upload1.jsp'">裁剪上传</button>
      <%
      if(username.equals("admin")){
    %> <button type="button" class="btn btn-success" onclick="javascript:window.location.href='/user/show_img_src.jsp'">查看图片</button>
      <%
        }else{
      %>
      <button type="button" class="btn btn-success" onclick="javascript:window.location.href='/ImgServlet?method=findUserImg'">查看图片</button>
      <%
        }
      %>
    </form>
  <br>
  <form action="center.jsp" method="post" style="float: left">
    <div class="col-sm-offset-2 col-sm-2 col-xs-6">
      <button class="btn btn-info" type="submit">返回</button>
    </div>
  </form>
  </body>
</html>
