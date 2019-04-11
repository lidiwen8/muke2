<%@page import="java.io.File" %>
<%@page import="org.apache.commons.fileupload.FileItem" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@page import="org.apache.commons.fileupload.FileItemFactory" %>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page import="com.muke.pojo.User" %>
<%@ page import="com.muke.util.FileUtil" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="net.sf.json.JSONObject" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">

    <title>图片上传</title>
</head>
<body>
<%
    try {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        if (ServletFileUpload.isMultipartContent(request)) {
            String type = "";

            if (request.getParameter("type") != null)//获取文件分类
                type = request.getParameter("type").toLowerCase() + "/";
//            response.setContentType("text/html;charset=UTF-8");
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
            servletFileUpload.setHeaderEncoding("UTF-8");  //解决中文乱码
            String callback = request.getParameter("ckCsrfToken");
            List<FileItem> fileItemsList = servletFileUpload.parseRequest(request);
            for (FileItem item : fileItemsList) {
                if (!item.isFormField()) {
                    String fileName = item.getName();
                    fileName = FileUtil.getRandomFileName() + fileName.substring(fileName.lastIndexOf("."));
                    String filetype = fileName.substring(fileName.lastIndexOf("."));
                    if (!(filetype.equals(".jpg") || filetype.equals(".png") || filetype.equals(".gif") || filetype.equals(".jpeg"))) {
                        out.println("<script>");
                        out.println("alert('请上传图片格式的文件！');");
                        out.println("</script>");
//                        out.println("<h3>请上传图片格式的文件。</h3>");
//                        out.println("<script type='text/javascript'>window.CKEDITOR.tools.callFunction(”请上传图片格式的文件“)</script>");
                        return;
                    }
                    HttpSession session1 = ((HttpServletRequest) request).getSession();
                    User user = (User) session1.getAttribute("user");
                    //定义文件路径，根据你的文件夹结构，可能需要做修改
                    String clientPath = "ckeditor/uploader/upload/" + user.getUserid() + "/" + type + fileName;
                    //保存文件到服务器上
                    File file = new File(request.getSession().getServletContext().getRealPath(clientPath));
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    item.write(file);
//                    response.addHeader("Content-Type", "text/html; charset=UTF-8");
//                    out.println("<script type=\"text/javascript\">");
//                    out.println("window.parent.CKEDITOR.tools.callFunction(" + callback + ",'" + clientPath + "',''" + ")");
//                    out.println("</script>");
//                    //打印一段JS，调用parent页面的CKEditor的函数，传递函数编号和上传后文件的路径；这句很重要，成败在此一句
//                    out.flush();

                    JSONObject obj = new JSONObject();
                    obj.put("uploaded", 1);
                    obj.put("fileName", fileName);
                    obj.put("url", clientPath);
                    out.write(obj.toString());
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (out != null) {
            out.close();
        }
    }
%>
</body>
</html>