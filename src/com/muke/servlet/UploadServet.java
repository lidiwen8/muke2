package com.muke.servlet;

import com.muke.pojo.User;
import com.muke.util.CkeditorResponseUtil;
import com.muke.util.FileUtil;
import net.sf.json.JSONObject;
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
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class UploadServet
 */
@WebServlet("/uploadServlet")
public class UploadServet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UploadServet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        if (ServletFileUpload.isMultipartContent(request)) {
            String type = "";
            if (request.getParameter("type") != null)//获取文件分类
                type = request.getParameter("type").toLowerCase() + "/";
            response.setContentType("text/html;charset=UTF-8");
            CkeditorResponseUtil ckeditorResponseUtil=new CkeditorResponseUtil();
            //上传时生成的临时文件保存目录
            String tempPath = request.getSession().getServletContext().getRealPath("ckeditor/uploader/upload/temp");
            File tmpFile = new File(tempPath);
            if(!tmpFile.exists()){
                //如果临时目录不存在，创建临时目录
                tmpFile.getParentFile().mkdir();
            }
//            FileItemFactory factory = new DiskFileItemFactory();
            DiskFileItemFactory factory = new DiskFileItemFactory();
           //设置工厂的缓冲区的大小，当上传的文件大小超过缓冲区的大小时，就会生成一个临时文件存放到指定的临时目录当中。
           //设置缓冲区的大小为1024KB，如果不指定，那么缓冲区的大小默认是10KB
            factory.setSizeThreshold(1024*1024*5);
             //设置上传时生成的临时文件的保存目录
            factory.setRepository(tmpFile);

            ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
            servletFileUpload.setHeaderEncoding("UTF-8");  //解决中文乱码
            JSONObject obj = new JSONObject();
            List<FileItem> fileItemsList = null;
            try {
                fileItemsList = servletFileUpload.parseRequest(request);
            } catch (FileUploadException e) {
                String error = ckeditorResponseUtil.error(0, "上传的图片大小最大不能超过5M！");
                response.getWriter().print(error);
                return;
            }
            for (FileItem item : fileItemsList) {
                if (!item.isFormField()) {
                    String fileName = item.getName();
                    fileName = FileUtil.getRandomFileName() + fileName.substring(fileName.lastIndexOf("."));
                    String filetype = fileName.substring(fileName.lastIndexOf("."));
                    if (!(filetype.equals(".jpg") || filetype.equals(".png") || filetype.equals(".gif") || filetype.equals(".jpeg")||filetype.equals(".bmp"))) {
                        String error = ckeditorResponseUtil.error(0, "请上传图片格式的文件（必须为.jpg/.gif/.bmp/.png/.jpeg文件）!");
                        response.getWriter().print(error);
                        return;
                    }
                    HttpSession session = request.getSession();
                    User user = (User) session.getAttribute("user");
                    //定义文件路径，根据你的文件夹结构，可能需要做修改
                    String clientPath = "ckeditor/uploader/upload/" + user.getUserid() + "/" + type + fileName;
                    //保存文件到服务器上
                    try {
                        File file = new File(request.getSession().getServletContext().getRealPath(clientPath));
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        try {
                            item.write(file);
                        } catch (Exception e) {
                            String error = ckeditorResponseUtil.error(0, "上传的图片大小最大不能超过5M！");
                            response.getWriter().print(error);
                            return;
                        }
                    }catch (NullPointerException e){
                        String error = ckeditorResponseUtil.error(0, "请选择一张图片上传！");
                        response.getWriter().print(error);
                        return;
                    }
                    basePath=basePath.replaceAll("user/","");
                    String success = ckeditorResponseUtil.success(1, fileName, basePath+clientPath, "图片上传成功！");
                    response.getWriter().print(success);
                     return;
                }
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
