package com.muke.servlet;

import com.muke.pojo.User;
import com.muke.service.IUserService;
import com.muke.service.impl.UserServiceImpl;
import com.muke.util.FileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

@WebServlet("/ImageSaveServlet")
public class ImageSaveServlet extends HttpServlet {
    private String name = null;//标题
    private String virtualPath;
    private IUserService userService = new UserServiceImpl();

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");//解决编码问题
        HttpSession session = request.getSession();
        User user1 = (User) session.getAttribute("user");//获取发布者
        String username = user1.getUsername();
        String user_img = user1.getUser_img();
        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            request.setAttribute("error", "请选择一张图片上传！");
            request.getRequestDispatcher("user/upload1.jsp").forward(request, response);
            return;
        }

        // 获取路径来存储文件
        String path = this.getServletContext().getRealPath("/") + "image";
        // 根据路径名创建一个 File实例
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();        // 如果不存在则创建此路径的目录
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 中文处理
        upload.setHeaderEncoding("utf-8");
        try {
            // 解析请求的内容提取文件数据
            List<FileItem> formItems = upload.parseRequest(request);
            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (item.isFormField()) {
                        name = item.getString();
                        name = new String(name.getBytes("ISO-8859-1"), "utf-8");
                    }
                    if (!item.isFormField()) {
                        String fileName = item.getName();
                        if (fileName.equals(null) || fileName.equals("")) {
                            response.getWriter().print("{\"res\": -1, \"info\":\"请选择一张图片上传！\"}");
                            return;
                        }
                        String filetype = fileName.substring(fileName.indexOf("."), fileName.length());
                        if (!(filetype.equals(".jpg") || filetype.equals(".png") || filetype.equals(".gif"))) {
                            response.getWriter().print("{\"res\": -1, \"info\":\"你上传的图片格式不正确,请重新上传\"}");
                            return;
                        }
                        upload.setFileSizeMax(1024 * 1024);// 设置图片上传的最大不能超过1M
                        factory.setSizeThreshold(1024 * 512);//超过512k设置临时文件缓存
                        float size = item.getSize() / 1024;
                        if (size > 1024) {
                            response.getWriter().print("{\"res\": -1, \"info\":\"上传的图片大小最大不能超过1M！\"}");
                            return;
                        }
                        // 获取文件名后缀, 返回 "."在文件名最后出现的索引, 就是文件后缀名
                        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        // 存储的文件名根据获取的id来唯一确定, 这里测试使用 "test"
                        // id可以绑定到session或request变量等等，自己根据需要来扩展
                        //删除之前上传的图片
                        if (!(user_img.equals("image/nan.png") || user_img.equals("image/nu.png"))) {
                            FileUtil.deleteFile(user_img);
                        }
                        String fileSaveName = FileUtil.getRandomFileName() + "." + prefix; // id.后缀
                        // 获取文件输入流
                        InputStream inputStream = item.getInputStream();
                        // 创建文件输出流，用于向指定文件名的文件写入数据
                        FileOutputStream fileOutputStream = new FileOutputStream(path + "/" + fileSaveName);
                        int index = 0;

                        // 从输入流读取数据的下一个字节，到末尾时返回 -1
                        while ((index = inputStream.read()) != -1) {
                            fileOutputStream.write(index);    // 将指定字节写入此文件输出流
                        }
                        // 关闭流
                        inputStream.close();
                        fileOutputStream.close();
                        item.delete();
                        // 设置图片存储的虚拟路径
                        virtualPath = "image/" + fileSaveName;
                        int ok = userService.saveUserimg(username, virtualPath);
                        if (ok == 1) {
                            User user = userService.username(username);
                            HttpSession session1 = request.getSession();
                            session1.setAttribute("user", user);
                            response.getWriter().print("{\"res\": 1, \"info\":\"成功上传\",\"src\": \"" + virtualPath + "\"}");
                            return;
                        } else {
                            response.getWriter().print("{\"res\": -1, \"info\":\"上传失败\"}");
                            return;
                        } // if (ok == 1)
                    }
                    // if (!item.isFormField())
                } // for
            } // try
        } catch (FileUploadException e) {
            response.getWriter().print("{\"res\": -1, \"info\":\"上传的图片大小最大不能超过1M！\"}");
            return;
        } catch (Exception e) {
            response.getWriter().print("{\"res\": -1, \"info\":\"图片上传错误，请检查网络是否连接正常！\"}");
            return;
        }
        request.setAttribute("user.user_img", virtualPath);
        request.getRequestDispatcher("user/center.jsp").forward(request, response);
        // 重定向到显示图片页面
    }

}
