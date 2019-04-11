<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>七牛云上传</title>
<script src="jquery/jquery-2.2.4.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/qiniu.min.js"></script>
<!--<script type="text/javascript" src="https://unpkg.com/qiniu-js@2.5.4/dist/qiniu.min.js"></script>-->
<script type="text/javascript">

    $(function () {
        $("#upload").on("click", function () {
            var obj = $("#file");
            var fileName = obj.val();		                                           //上传的本地文件绝对路径
            var suffix = fileName.substring(fileName.lastIndexOf("."),fileName.length);//后缀名
            var file = obj.get(0).files[0];	                                           //上传的文件
            var size = file.size > 1024 ? file.size / 1024 > 1024 ? file.size / (1024 * 1024) > 1024 ? (file.size / (1024 * 1024 * 1024)).toFixed(2) + 'GB' : (file.size
                / (1024 * 1024)).toFixed(2) + 'MB' : (file.size
                / 1024).toFixed(2) + 'KB' : (file.size).toFixed(2) + 'B';		   //文件上传大小
            //七牛云上传
            $.ajax({
                type:'post',
                url: "/QiniuUploadServlet?action=QiniuUpToken",
                data:{"suffix":suffix},
                dataType:'json',
                success: function(result){
                    if(result.success == 1){
                        alert(result.token);
                        var observer = {                         //设置上传过程的监听函数
                            next(result){                        //上传中(result参数带有total字段的 object，包含loaded、total、percent三个属性)
                                Math.floor(result.total.percent);//查看进度[loaded:已上传大小(字节);total:本次上传总大小;percent:当前上传进度(0-100)]
                                $("#jindu").text("已上传："+result.total.percent);
                                $("#sumsize").text("本次上传总大小："+result.total.total);

                            },
                            error(err){                          //失败后
                                alert(err.message);
                            },
                            complete(res){                       //成功后
                                // ?imageView2/2/h/100：展示缩略图，不加显示原图
                                // ?vframe/jpg/offset/0/w/480/h/360：用于获取视频截图的后缀，0：秒，w：宽，h：高
                                $("#image").attr("src",result.domain+result.imgUrl+"?imageView2/2/w/400/h/400/q/100");
                            }
                        };
                        var putExtra = {
                            fname: "",                          //原文件名
                            params: {},                         //用来放置自定义变量
                            mimeType: null                      //限制上传文件类型
                        };
                        var config = {
                            region:qiniu.region.z2,             //存储区域(z0:代表华东;z2:代表华南,不写默认自动识别)
                            concurrentRequestLimit:3            //分片上传的并发请求量
                        };
                        var observable = qiniu.upload(file,result.imgUrl,result.token,putExtra,config);
                        var subscription = observable.subscribe(observer);          // 上传开始
                        // 取消上传
                        // subscription.unsubscribe();
                    }else{
                        alert(1);
                        alert(result.message);                  //获取凭证失败
                    }
                },error:function(){                             //服务器响应失败处理函数
                    alert("服务器繁忙");
                }
            });
        })
    })
</script>
</head>
<body>
<input type="file" name="image" id="file" accept="image/*">
<input type="button" id="upload" value="upload">
<img id="image" src="#" alt="">
<div id="jindu"></div>
<div id="sumsize"></div>
</body>
</html>