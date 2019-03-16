package com.muke.servlet;

import com.muke.pojo.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
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
import java.io.PrintWriter;
import java.util.Iterator;
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
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
            servletFileUpload.setHeaderEncoding("UTF-8");  //解决中文乱码
            List<FileItem> fileItemsList = null;
            try {
                fileItemsList = servletFileUpload.parseRequest(request);
            } catch (FileUploadException e) {
                response.getWriter().print("{\"uploaded\": 0, \"error\":\"上传文件异常，请重试！\"}");
                return;
            }
            for (FileItem item : fileItemsList) {
                if (!item.isFormField()) {
                    String fileName = item.getName();
                    fileName = "file" + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));
                    String filetype = fileName.substring(fileName.lastIndexOf("."));
                    if (!(filetype.equals(".jpg") || filetype.equals(".png") || filetype.equals(".gif") || filetype.equals(".jpeg"))) {
                        response.getWriter().print("{\"uploaded\": 0, \"error\":\"请上传图片格式的文件！\"}");
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
                    try {
                        item.write(file);
                    } catch (Exception e) {
                        response.getWriter().print("{\"uploaded\": 0, \"error\":\"上传的图片大小最大不能超过1M！\"}");
                        return;
                    }

                    response.setContentType("text/html;charset=UTF-8");
                    String callback = request.getParameter("CKEditorFuncNum");
                    PrintWriter out = response.getWriter();
                    out.println("<script type=\"text/javascript\">");
                    out.println("window.parent.CKEDITOR.tools.callFunction(" + 1 + ",'" + clientPath + "',''" + ")");
                    out.println("</script>");
                    out.flush();
                    out.close();
//                    response.getWriter().print("{\"uploaded\": 1, \"url\":'"+clientPath+"'}");
//                    return;
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
