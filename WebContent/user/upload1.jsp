<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE>
<html>
  <head>
    <title>File Upload</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <script src="/jquery/jquery-2.2.4.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/bootstrap-3.3.7-dist/css/bootstrap.css">
    <link rel="stylesheet" href="/css/cropper.min.css">
    <link rel="stylesheet" href="/css/ImgCropping.css">
  </head>
  <style>

    .str {

      width: 150px;

      height: 180px;

      border: solid 1px #e3e3e3;

      padding: 5px;

      margin-top: 10px

    }

  </style>
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
           %>   <h3>欢迎管理员：<%=session.getAttribute("username")%>-裁剪上传头像</h3>
      <%
        }else {
         %><h3>欢迎用户：<%=session.getAttribute("username")%>-裁剪上传头像</h3>
       <% }
      %>
    </div>
  </div>
    <form role="form" method="post" action="/ImageSaveServlet" enctype="multipart/form-data" onsubmit="return submit()">
      <div class="form-group">
        <label>标题：</label>
        <input type="text" class="form-control" name="name" placeholder="请输入标题">
      </div>
      <label title="上传图片" for="chooseImg" class="l-btn choose-btn">
        <input type="file" accept="image/jpg,image/jpeg,image/png" name="file" id="chooseImg" class="hidden" onchange="selectImg(this)">
        选择图片
      </label>
      <div class="str">
        <img id="finalImg" src="" width="100%" name="finalImg">
      </div>
      <!--图片裁剪框 start-->
      <div style="display: none" class="tailoring-container">
        <div class="black-cloth" onclick="closeTailor(this)"></div>
        <div class="tailoring-content">
          <div class="tailoring-content-one">
            <div class="close-tailoring" onclick="closeTailor(this)">×</div>
          </div>
          <div class="tailoring-content-two">

            <div class="tailoring-box-parcel">

              <img id="tailoringImg">

            </div>

            <div class="preview-box-parcel">
              <p>图片预览：</p>
              <div class="square previewImg"></div>
              <!--  <div class="circular previewImg"></div>-->
            </div>
          </div>
          <div class="tailoring-content-three">
            <button class="l-btn cropper-reset-btn">复位</button>
            <button class="l-btn cropper-rotate-btn">旋转</button>
            <button class="l-btn cropper-scaleX-btn">换向</button>
            <button class="l-btn sureCut" id="sureCut">确定</button>
          </div>
        </div>
      </div>
      <button type="submit" class="btn btn-default">&nbsp;&nbsp;保存</button>
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
  <script src="http://www.jq22.com/jquery/jquery-1.10.2.js"></script>
  <script src="/js/cropper.min.js"></script>
  <script type="text/javascript">
      //弹出框水平垂直居中
      (window.onresize = function() {
          var win_height = $(window).height();
          var win_width = $(window).width();
          if (win_width <= 768) {
              $(".tailoring-content").css(
                  {
                      "top" : (win_height - $(".tailoring-content")
                          .outerHeight()) / 2,
                      "left" : 0
                  });
          } else {
              $(".tailoring-content").css(
                  {
                      "top" : (win_height - $(".tailoring-content")
                          .outerHeight()) / 2,
                      "left" : (win_width - $(".tailoring-content")
                          .outerWidth()) / 2
                  });
          }
      })();
      // 选择文件触发事件
      function selectImg(file) {
          //文件为空，返回
          if (!file.files || !file.files[0]) {
              return;
          }
          $(".tailoring-container").toggle();
          var reader = new FileReader();
          reader.onload = function(evt) {
              var replaceSrc = evt.target.result;
              // 更换cropper的图片
              $('#tailoringImg').cropper('replace', replaceSrc, false);// 默认false，适应高度，不失真
          }
          reader.readAsDataURL(file.files[0]);
      }
      // cropper图片裁剪
      $('#tailoringImg').cropper({
          aspectRatio : 1 / 1,// 默认比例
          preview : '.previewImg',// 预览视图
          guides : false, // 裁剪框的虚线(九宫格)
          autoCropArea : 0.5, // 0-1之间的数值，定义自动剪裁区域的大小，默认0.8
          movable : false, // 是否允许移动图片
          dragCrop : true, // 是否允许移除当前的剪裁框，并通过拖动来新建一个剪裁框区域
          movable : true, // 是否允许移动剪裁框
          resizable : true, // 是否允许改变裁剪框的大小
          zoomable : false, // 是否允许缩放图片大小
          mouseWheelZoom : false, // 是否允许通过鼠标滚轮来缩放图片
          touchDragZoom : true, // 是否允许通过触摸移动来缩放图片
          rotatable : true, // 是否允许旋转图片
          crop : function(e) {
              // 输出结果数据裁剪图像。
          }

      });

      // 旋转

      $(".cropper-rotate-btn").on("click", function() {

          $('#tailoringImg').cropper("rotate", 45);
          return false;

      });

      // 复位

      $(".cropper-reset-btn").on("click", function() {

          $('#tailoringImg').cropper("reset");
          return false;

      });

      // 换向

      var flagX = true;

      $(".cropper-scaleX-btn").on("click", function() {

          if (flagX) {

              $('#tailoringImg').cropper("scaleX", -1);

              flagX = false;

          } else {

              $('#tailoringImg').cropper("scaleX", 1);

              flagX = true;

          }

          flagX != flagX;
          return false;

      });


      // 确定按钮点击事件
      $("#sureCut").on("click", function() {
          if ($("#tailoringImg").attr("src") == null) {
              return false;
          } else {
              var cas = $('#tailoringImg').cropper('getCroppedCanvas');// 获取被裁剪后的canvas
              $('#tailoringImg').show();
              var base64 = cas.toDataURL('image/jpeg'); // 转换为base64
              $("#finalImg").prop("src", base64);// 显示图片
               uploadFile(encodeURIComponent(base64))//编码后上传服务器
              closeTailor();// 关闭裁剪框
             return false;
          }

      });



      // 关闭裁剪框

      function closeTailor() {

          $(".tailoring-container").toggle();

      }



      //ajax请求上传

      function uploadFile(file) {

          $.ajax({

              url : '/ImageSaveServlet',

              type : 'POST',

              data : "file=" + file,

              async : true,

              success : function(data) {

                  console.log(data)
              }
          });
      }
  </script>
  </body>
</html>
