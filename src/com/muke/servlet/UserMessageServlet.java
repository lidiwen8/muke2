package com.muke.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muke.pojo.*;
import com.muke.pojo.MessageCriteria.OrderRuleEnum;
import com.muke.service.ICountService;
import com.muke.service.IMessageService;
import com.muke.service.IReplyService;
import com.muke.service.IThemeService;
import com.muke.service.impl.ICountServiceImpl;
import com.muke.service.impl.IReplyServiceImpl;
import com.muke.service.impl.MessageServiceImpl;
import com.muke.service.impl.ThemeServiceImpl;
import com.muke.util.*;
import javax.websocket.Session;


/**
 * Servlet implementation class UserMessageServlet
 */
//该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket，类似Servlet的注解mapping；
@ServerEndpoint(value = "/websocket")
@WebServlet("/user/userMessageServlet")
public class UserMessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IMessageService messageservice = new MessageServiceImpl();
    private IThemeService themeService = new ThemeServiceImpl();
    private IReplyService iReplyService = new IReplyServiceImpl();
    private ICountService iCountService = new ICountServiceImpl();
    private static final int pageNumber=new Page().getPageNumber();
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<UserMessageServlet> webSocketSet = new CopyOnWriteArraySet<UserMessageServlet>();
    //这个session不是Httpsession，相当于用户的唯一标识，用它进行与指定用户通讯
    private Session session=null;
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //发布帖子  恢复帖子  查看我的帖子
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String action = request.getParameter("action");
        try {
            //使用反射定义方法
            Method method = getClass().getDeclaredMethod(action, HttpServletRequest.class,
                    HttpServletResponse.class);
            //调用方法
            method.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发帖
    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msgtopic = request.getParameter("msgtopic");//帖子标题
        String theid = request.getParameter("theid");//主题ID
        String msgcontents = request.getParameter("msgcontents");//主题内容
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userid = user.getUserid();//用户ID
        String msgip = IPUtil.getIP(request);//发帖人的IP
        msgcontents = EmojiUtil.resolveToByteFromEmoji(msgcontents);//表情包转化为编码
        String str = msgcontents.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim();
//			msgcontents=HTMLReplace.replace(msgcontents);//帖子内容转换为HTML格式
        if (str.equals("") || str.length() == 0 || str.equals(null)) {
            response.getWriter().print("{\"res\":2,\"info\":\"尊敬的" + user.getUsername() + "用户：提问内容不能全部输入空格或者回车,发帖失败\"}");
            return;
        }
        ToolsUtil toolsUtil = new ToolsUtil();
        String str2 = toolsUtil.delHTMLTag(str).replaceAll(" ", "").replaceAll("\\s*", "");
        if (str2.trim().length() < 6 || str2.trim().length() > 1000) {
            response.getWriter().print("{\"res\":3,\"info\":\"尊敬的" + user.getUsername() + "用户：问题内容长度必须在6到1000之间\"}");
            return;
        }
        Message message = new Message();
        message.setMsgtopic(msgtopic);
        message.setTheid(Integer.parseInt(theid));
        message.setMsgcontents(msgcontents);
        message.setUserid(userid);
        message.setMsgip(msgip);
        int rs = messageservice.addMsg(message);
        if (rs > 0) {
            //发送更新信号
            sendMessage("add");
            response.getWriter().print("{\"res\":1,\"info\":\"发帖成功\"}");
            int count = messageservice.msgCountBytheid(Integer.parseInt(theid));
            themeService.updateCount(count, Integer.parseInt(theid));
        } else {
            response.getWriter().print("{\"res\":-1,\"info\":\"发帖失败\"}");
        }
    }

    //查询
    private void getMyMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageNum = request.getParameter("pageNum");
        if (pageNum == null || pageNum.equals("")) {
            pageNum = "1";
        }
        Page page = new Page();
        page.setCurPage(Integer.parseInt(pageNum));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userid = user.getUserid();
        //创建封装查询条件对象
        MessageCriteria messageCriteria = new MessageCriteria();
        messageCriteria.setUserid(userid);
        messageCriteria.setOrderRule(OrderRuleEnum.ORDER_BY_MSG_TIME);//排序条件
        messageCriteria.setState(0);//查询非禁用状态
        page = messageservice.searchUserMyMsg(messageCriteria, page);
        Gson gson = new GsonBuilder().setDateFormat("yy-MM-dd").create();
        String json = gson.toJson(page);
        response.getWriter().print("{\"res\":1,\"message\":" + json + "}");

    }

    //回帖
    private void replyMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String msgId = request.getParameter("msgId");
        String replycontent = request.getParameter("replycontent");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userid = user.getUserid();//用户ID
        try {
            int state = messageservice.queryMsgState(Integer.parseInt(msgId));
            int Msguserid=messageservice.getMsgNoincreaseCount(Integer.parseInt(msgId)).getUserid();
            if (state == -1) {
                response.getWriter().print("{\"res\":-1,\"message\":\"该帖子已经被管理员屏蔽了,你无法回复！如有疑问，请联系管理员QQ:1632029393!\"}");
                return;
            } else if (state == 3) {
                if (messageservice.getMsgNoincreaseCount(Integer.parseInt(msgId)).getUserid() != userid) {
                    response.getWriter().print("{\"res\":-1,\"message\":\"该帖子已经被楼主屏蔽了,你无法回复！如有疑问，请联系楼主邮箱:" + user.getEmail() + "!\"}");
                    return;
                }
            }
            if (messageservice.queryMsgReplyident(Integer.parseInt(msgId)) == -1&&Msguserid!=userid) {
                if (user.getMailstate() == 1) {
                    response.getWriter().print("{\"res\":-1,\"info\":\"该帖子已经被该楼主关闭回复了，你暂时无法回复！如有疑问，请联系楼主邮箱：" + user.getEmail() + "！\"}");
                    return;
                }
                response.getWriter().print("{\"res\":-1,\"info\":\"该帖子已经被该楼主关闭回复了，你暂时无法回复！\"}");
                return;
            }
            String replyip = IPUtil.getIP(request);//获取IP
            replycontent = EmojiUtil.resolveToByteFromEmoji(replycontent);//表情包转化为编码
//		replycontent=HTMLReplace.replace(replycontent);
            if (replycontent.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim().equals("") || replycontent.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim().length() == 0 || replycontent.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim().equals(null)) {
                response.getWriter().print("{\"res\":2,\"info\":\"尊敬的" + user.getUsername() + "用户：不能全部输入空格或者回车,回帖失败\"}");
                return;
            }
            Reply reply = new Reply();
            reply.setUserid(userid);
            reply.setMsgid(Integer.parseInt(msgId));
            reply.setReplycontents(replycontent);
            reply.setReplyip(replyip);
            int rs = iReplyService.replyMsg(reply);
            int relpycout= (int) iReplyService.queryReplyConutBymsgid(Integer.parseInt(msgId));
            int page,AutherPage;
            if(Msguserid==userid){
                int AutherRelpycout= (int) iReplyService.queryAutherReplyConutBymsgid(Integer.parseInt(msgId),Msguserid);
                if(AutherRelpycout<=pageNumber){
                    AutherPage=1;
                }else if(AutherRelpycout%pageNumber==0){
                    AutherPage=AutherRelpycout/pageNumber;
                }else {
                    AutherPage=AutherRelpycout/pageNumber+1;
                }
            }else {
                AutherPage=-1;
            }
            if(relpycout<=pageNumber){
                page=1;
            }else if(relpycout%pageNumber==0){
                page=relpycout/pageNumber;
            }else {
                page=relpycout/pageNumber+1;
            }
            if (rs > 0) {
                //发送帖子更新的信号
                response.getWriter().print("{\"res\":1,\"info\":\"回帖成功\",\"LastPage\":"+page+"}");
                sendMessage("ReplyAdd"+msgId+","+page+","+userid+","+AutherPage);
            } else {
                response.getWriter().print("{\"res\":-1,\"info\":\"回帖失败\"}");
            }
        }catch (NullPointerException e){
            response.getWriter().print("{\"res\":-1,\"info\":\"未找到有关该帖子的任何信息，可能该帖子已经被管理员永久删除了！\"}");
            return;
        }
    }

    //搜索我的问题，根据帖子标题
    private void searchUserMyMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageNum = request.getParameter("pageNum");
        String theid = request.getParameter("theid");
        String key = request.getParameter("key");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userid = user.getUserid();//用户ID
        try {
            if (pageNum == null || pageNum.equals("")) {
                pageNum = "1";
            }
            if (theid == null || theid.equals("")) {
                theid = "-1";
            }
            Page page = new Page();
            page.setCurPage(Integer.parseInt(pageNum));
            //创建封装查询条件对象
            MessageCriteria messageCriteria = new MessageCriteria();
            messageCriteria.setUserid(userid);
            messageCriteria.setKey(key);
            messageCriteria.setTheid(Integer.parseInt(theid));
            messageCriteria.setOrderRule(MessageCriteria.OrderRuleEnum.ORDER_BY_MSG_TIME);//排序条件
            messageCriteria.setState(0);//查询非禁用状态
            page = messageservice.searchUserAllMyMsg(messageCriteria, page);
            if (page == null) {
                response.getWriter().print("{\"res\":-1,\"message\":不存在与'"+key+"'有关的帖子问题，请换个关键词试试！}");
                return;
            }
            Gson gson = new GsonBuilder().setDateFormat("yy-MM-dd").create();
            String json = gson.toJson(page);
            response.getWriter().print("{\"res\":1,\"message\":" + json + "}");
        } catch (NullPointerException e) {
            response.getWriter().print("{\"res\":-1,\"message\":不存在与'"+key+"'有关的帖子问题，请换个关键词试试！}");
            return;
        }
    }

    //修改问题
    private void updateMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int msgId = Integer.parseInt(request.getParameter("msgid"));
        String msgcontents = request.getParameter("msgcontents");
        String theid = request.getParameter("theid");
        String msgtopic = request.getParameter("msgtopic");
        String msgip = IPUtil.getIP(request);//发帖人的IP
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        msgcontents = EmojiUtil.resolveToByteFromEmoji(msgcontents);//表情包转化为编码
        String str = msgcontents.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim();
        if (str.equals("") || str.length() == 0 || str.equals(null)) {
            response.getWriter().print("{\"res\":2,\"info\":\"提问内容不能全部输入空格或者回车,更新问题失败！\"}");
            return;
        }
        ToolsUtil toolsUtil = new ToolsUtil();
        String str2 = toolsUtil.delHTMLTag(str).replaceAll(" ", "").replaceAll("\\s*", "");
        if (str2.trim().length() < 6 || str2.trim().length() > 1000) {
            response.getWriter().print("{\"res\":3,\"info\":\"问题内容长度必须在6到1000之间,更新问题失败！\"}");
            return;
        }
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userid = user.getUserid();
        MessageInfo messageInfo = messageservice.getMsgNoincreaseCount(msgId);
        int pastTheid;//之前的theid
        pastTheid=messageInfo.getTheid();
        if (msgcontents.equals(messageInfo.getMsgcontents())) {
            flag1 = true;
        }
        if (msgtopic.equals(messageInfo.getMsgtopic())) {
            flag2 = true;
        }
        if (Integer.parseInt(theid) == pastTheid) {
            flag3 = true;
        }
        messageInfo.setMsgcontents(msgcontents);
        messageInfo.setMsgip(msgip);
        messageInfo.setMsgtopic(msgtopic);
        messageInfo.setTheid(Integer.parseInt(theid));
        if (userid == messageInfo.getUserid()) {//只有本人才有资格修改自己的问题
            if (messageInfo.getState() == -1) {
                response.getWriter().print("{\"res\":-1,\"info\":\"该帖子由于存在不良信息已被管理员屏蔽，你暂时没有修改权限!如有疑问，请联系管理员QQ:1632029393!\"}");
                return;
            }
            if (flag1 && flag2 && flag3) {
                response.getWriter().print("{\"res\":-1,\"info\":\"问题未做任何修改,没有更新哟\"}");
                return;
            }
            messageInfo.setMsgupdatetime(System.currentTimeMillis());//插入修改时间戳
            int rs = messageservice.updateMsg(messageInfo);
            if (rs > 0) {
                //向客户端发送更新信号
                sendMessage("add"+msgId);
                response.getWriter().print("{\"res\":1,\"info\":\"更新成功\"}");
                //如果主题theid编辑了,要重新统计主题帖子数,过去的theid以及现在的theid都要
                if(!flag3){
                    int count = messageservice.msgCountBytheid(pastTheid);
                    themeService.updateCount(count, pastTheid);
                }
                //新theid统计主题帖子数
                int count = messageservice.msgCountBytheid(Integer.parseInt(theid));
                themeService.updateCount(count, Integer.parseInt(theid));
            } else {
                response.getWriter().print("{\"res\":-1,\"info\":\"更新失败\"}");
            }
        } else {
            response.getWriter().print("{\"res\":-1,\"info\":\"权限不足，更新失败\"}");
        }
    }

    //得到问题
    private void getMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int msgId = Integer.parseInt(request.getParameter("msgid"));
        MessageInfo messageInfo = messageservice.getMsgNoincreaseCount(msgId);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userid = user.getUserid();
        if (userid == messageInfo.getUserid()) {//只有本人才有资格修改自己的问题
            if (messageInfo.getState() == -1) {
                response.getWriter().print("{\"res\":-1,\"info\":\"该帖子由于存在不良信息已被管理员屏蔽，你暂时没有修改权限!如有疑问，请联系管理员QQ:1632029393!\"}");
                return;
            }
//			request.setAttribute("msg",messageInfo);
            session.setAttribute("msg", messageInfo);
//			request.getRequestDispatcher("editmsg.jsp").forward(request,response);
            response.getWriter().print("{\"res\":1,\"info\":\"获取我的问题信息成功\"}");
        } else {
            response.getWriter().print("{\"res\":-1,\"info\":\"权限不足，更新失败\"}");
        }
    }

    private void deleteMymsg(HttpServletRequest request, HttpServletResponse response) throws IOException, MessagingException {
        String msgid = request.getParameter("msgid");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userid = user.getUserid();
        if (msgid == null || msgid.equals("")) {
            msgid = "-1";
        }
        int finalMsgid = Integer.parseInt(msgid);
        MessageInfo messageInfo = messageservice.getMsgNoincreaseCount(finalMsgid);
        if(messageInfo!=null) {
            if (userid == messageInfo.getUserid()) {//只有本人才有资格删除自己的问题
                //管理员已禁用的帖子不允许用户自己操作
                if (messageInfo.getState() == -1) {
                    response.getWriter().print("{\"res\":-1,\"info\":\"该帖子由于存在不良信息已被管理员屏蔽，你暂时没有删除权限!如有疑问，请联系管理员QQ:1632029393!\"}");
                    return;
                }
                int count = iCountService.getReplyCount(finalMsgid);
                int res = messageservice.userdeleteMsg(finalMsgid);
                if (res == 1) {
                    //发送更新信号
                    sendMessage("adddelete"+msgid);
                    response.getWriter().print("{\"res\": 1, \"info\":\"删除成功\"}");
                    if (count > 0) {
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (user.getMailstate() == 1) {
                                    List emailList = iReplyService.getReplyUseremail(finalMsgid, userid);
                                    if (emailList.size() != 0 && emailList != null) {
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
                                        String time = df.format(new Date());
                                        MessageEmail messageEmail = new MessageEmail();
                                        messageEmail.setFrom("1632029393@qq.com");
                                        messageEmail.setSubject("[爱之家科技有限公司]删除帖子通知");
                                        try {
                                            messageEmail.setMsg(SendEmail.SendDeleteMsgmail("http://www.lidiwen.club/muke_Web/message.jsp?msgid=" + finalMsgid, messageInfo.getMsgtopic(), user.getUsername(), user.getEmail(), time));
                                        } catch (MessagingException e) {
                                            e.printStackTrace();
                                        }
                                        List<String> str = new ArrayList<String>();
                                        for (int i = 0; i < emailList.size(); i++) {
                                            String email = StrUtil.getMapToString2((Map<String, String>) emailList.get(i));
                                            str.add(email.replace("email=", ""));
                                        }
                                        messageEmail.setTo(str);
                                        try {
                                            SendEmail.sslSend(messageEmail);//发送邮件
                                        } catch (MessagingException | IOException e) {
                                            System.out.println("删除帖子时发送邮件失败");
                                            return;
                                        }
                                    } else {
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            }
                        });
                        t.start();
                    } else {
                        return;
                    }
                } else {
                    response.getWriter().print("{\"res\": " + res + ", \"info\":\"删除失败！\"}");
                }
            } else {
                response.getWriter().print("{\"res\":-1,\"info\":\"权限不足，删除失败！\"}");
            }
        }else {
            response.getWriter().print("{\"res\":-1,\"info\":\"该帖子信息不存在，删除失败！\"}");
        }
    }

    private void restoreMymsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String msgid = request.getParameter("msgid");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userid = user.getUserid();
        MessageInfo messageInfo = messageservice.getMsgNoincreaseCount(Integer.parseInt(msgid));
        if (userid == messageInfo.getUserid()) {//只有本人才有资格恢复自己的问题
            if (msgid == null || msgid.equals("")) {
                msgid = "-1";
            }
            if (messageInfo.getState() == -1) {
                response.getWriter().print("{\"res\":-1,\"info\":\"该帖子由于存在不良信息已被管理员屏蔽，你暂时没有恢复权限!如有疑问，请联系管理员QQ:1632029393!\"}");
                return;
            }
            int res = messageservice.restoreMsg(Integer.parseInt(msgid));
            if (res == 1) {
                //发送更新信号
                sendMessage("addrestore"+msgid);
                response.getWriter().print("{\"res\": 1, \"info\":\"恢复成功\"}");
            } else {
                response.getWriter().print("{\"res\": " + res + ", \"info\":\"恢复失败\"}");
            }
        } else {
            response.getWriter().print("{\"res\":-1,\"info\":\"权限不足，恢复失败\"}");
        }
    }

    private void getReplyInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int replyid = Integer.parseInt(request.getParameter("replyid"));
        HttpSession session = request.getSession();
        Reply reply = iReplyService.queryid(replyid);
        try {
            User user = (User) session.getAttribute("user");
            int userid = user.getUserid();
            if (reply != null) {
                if (reply.getUserid() == userid) {
                    Gson gson = new GsonBuilder().setDateFormat("MM-dd HH:mm").create();
                    String dataJSON = gson.toJson(reply);
                    response.getWriter().print("{\"res\": 1, \"reply\":" + dataJSON + "}");
//                  response.getWriter().print("{\"res\":1,\"theid\":"+theme.getTheid()+",\"thename\":'"+theme.getThename()+"'}");
                } else {
                    response.getWriter().print("{\"res\":-1,\"info\":\"你没有权限编辑其它用户回复的信息，编辑失败！\"}");
                }
            } else {
                response.getWriter().print("{\"res\":-1,\"info\":\"获取该回复信息失败！\"}");
            }
        } catch (NullPointerException e) {
            Gson gson = new GsonBuilder().setDateFormat("MM-dd HH:mm").create();
            String dataJSON = gson.toJson(reply);
            response.getWriter().print("{\"res\": 1, \"reply\":" + dataJSON + "}");
        }
    }

    private void updateReply(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int replyid = Integer.parseInt(request.getParameter("replyid"));
        String replycontent = request.getParameter("replycontent");
        HttpSession session = request.getSession();
        Reply reply = iReplyService.queryid(replyid);
        try {
            User user = (User) session.getAttribute("user");
            int userid = user.getUserid();//用户ID
            int autherid=messageservice.getMsgNoincreaseCount(reply.getMsgid()).getUserid();//楼主userid
            if (reply.getUserid() == userid) {
//          String replyip = IPUtil.getIP(request);//编辑回复信息时不改变其发表时间以及回复ip地址
                replycontent = EmojiUtil.resolveToByteFromEmoji(replycontent);//表情包转化为编码
                if (replycontent.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim().equals("") || replycontent.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim().length() == 0 || replycontent.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim().equals(null)) {
                    response.getWriter().print("{\"res\":2,\"info\":\"尊敬的" + user.getUsername() + "用户：不能全部输入空格或者回车,编辑回复信息失败\"}");
                    return;
                }
                if (reply.getReplycontents().equals(replycontent)) {
                    response.getWriter().print("{\"res\":-1,\"info\":\"回复信息未做任何修改,没有更新哟！\"}");
                    return;
                }
                reply.setReplyupdatetime(System.currentTimeMillis());//修改时间
                reply.setReplycontents(replycontent);
                int rs = iReplyService.updateReply(reply);
                int relpycout= (int) iReplyService.queryReplyConutInTotalByreplytime(reply.getMsgid(),reply.getReplytime());
                int page,n,AutherPage,nAuther;
                int AutherRelpycout= (int) iReplyService.queryAutherReplyConutBymsgid(reply.getMsgid(),reply.getUserid());
                    if(AutherRelpycout<=pageNumber){
                        AutherPage=1;
                        nAuther=AutherRelpycout;//代表第几条
                    }else if(AutherRelpycout%pageNumber==0){
                        AutherPage=AutherRelpycout/pageNumber;
                        nAuther=pageNumber;
                    }else {
                        AutherPage=AutherRelpycout/pageNumber+1;
                        nAuther=AutherRelpycout%pageNumber;
                    }
                if(relpycout<=pageNumber){
                    page=1;
                    n=relpycout;//代表第几条
                }else if(relpycout%pageNumber==0){
                    page=relpycout/pageNumber;
                    n=pageNumber;
                }else {
                    page=relpycout/pageNumber+1;
                    n=relpycout%pageNumber;
                }
//                int userFlag;//标记是否是本人修改回复1是-0不是
                if (rs > 0) {
                    //发送更新信号
                    if(userid==autherid){//楼主更新自己的回复
                        sendMessage("ReplyUpdateAuther"+reply.getMsgid()+","+page+","+n+","+userid+","+AutherPage+","+nAuther+","+relpycout+","+AutherRelpycout);
                    }else{
                        sendMessage("ReplyUpdate"+reply.getMsgid()+","+page+","+n+","+userid+","+AutherPage+","+nAuther+","+relpycout+","+AutherRelpycout);
                    }

                    response.getWriter().print("{\"res\":1,\"info\":\"编辑回复信息成功\"}");
                } else {
                    response.getWriter().print("{\"res\":-1,\"info\":\"编辑回复信息失败\"}");
                }
            } else {
                response.getWriter().print("{\"res\":-1,\"info\":\"你没有权限编辑其它用户回复的信息，编辑失败！\"}");
            }
        } catch (NullPointerException e) {
            replycontent = EmojiUtil.resolveToByteFromEmoji(replycontent);//表情包转化为编码
            if (replycontent.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim().equals("") || replycontent.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim().length() == 0 || replycontent.replaceAll("(?:&nbsp;|<p>|</p>)", "").trim().equals(null)) {
                response.getWriter().print("{\"res\":2,\"info\":\"尊敬的" + "管理员" + "：不能全部输入空格或者回车,编辑回复信息失败\"}");
                return;
            }
            if (reply.getReplycontents().equals(replycontent)) {
                response.getWriter().print("{\"res\":-1,\"info\":\"回复信息未做任何修改,没有更新哟！\"}");
                return;
            }
            reply.setReplycontents(replycontent);
            int rs = iReplyService.updateReply2(reply);
            if (rs > 0) {
                //发送更新信号
                sendMessage(reply.getMsgid()+"");
                response.getWriter().print("{\"res\":1,\"info\":\"编辑回复信息成功\"}");
            } else {
                response.getWriter().print("{\"res\":-1,\"info\":\"编辑回复信息失败\"}");
            }
        }
    }

    private void replyzan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int likecount = Integer.parseInt(request.getParameter("likecount"));
        int replyid = Integer.parseInt(request.getParameter("replyid"));
        HttpSession session = request.getSession();
        Reply reply = new Reply();
        reply.setReplyid(replyid);
        try {
            User user = (User) session.getAttribute("user");
            if(iReplyService.queryid(replyid).getUserid()==user.getUserid()){
                response.getWriter().print("{\"res\":-1,\"info\":\"您不能对自己的回帖进行点赞！\"}");
                return;
            }
            List list = iReplyService.getReplylikeUserid(replyid);
            String likeuserid = list.get(0).toString().substring(12, list.get(0).toString().length() - 1);
            int relpycout= (int) iReplyService.queryReplyConutInTotalByreplytime(reply.getMsgid(),reply.getReplytime());
            int page,n;
            if(relpycout<=pageNumber){
                page=1;
                n=relpycout;//代表第几条
            }else if(relpycout%pageNumber==0){
                page=relpycout/pageNumber;
                n=pageNumber;
            }else {
                page=relpycout/pageNumber+1;
                n=relpycout%pageNumber;
            }
            if (!(likeuserid.equals("null") || likeuserid.equals(""))) {
                String userid[] = likeuserid.split(",");
                for (int i = 0; i < userid.length; i++) {
                    if (user.getUserid() == Integer.parseInt(userid[i])) {
                        response.getWriter().print("{\"res\":-1,\"info\":\"你已经点过赞了！\"}");
                        return;
                    }
                }
                reply.setLikecount(likecount + 1);
                reply.setLikeuserid(likeuserid + user.getUserid() + ",");
                if (iReplyService.updateReplylike(reply) > 0) {
                    //发送更新信号
                    sendMessage("Replyzan"+iReplyService.queryid(replyid).getMsgid()+","+page+","+n+","+iReplyService.queryid(replyid).getUserid());
                    response.getWriter().print("{\"res\":1,\"info\":\"操作成功\"}");

                } else {
                    response.getWriter().print("{\"res\":-1,\"info\":\"操作失败\"}");
                }

            } else {
                reply.setLikecount(likecount + 1);
                reply.setLikeuserid(user.getUserid() + ",");
                if (iReplyService.updateReplylike(reply) > 0) {
                    //发送更新信号
                    sendMessage("Replyzan"+iReplyService.queryid(replyid).getMsgid()+","+page+","+n+","+iReplyService.queryid(replyid).getUserid()+","+relpycout);
                    response.getWriter().print("{\"res\":1,\"info\":\"操作成功\"}");
                } else {
                    response.getWriter().print("{\"res\":-1,\"info\":\"操作失败\"}");
                }
            }
        } catch (NullPointerException e) {
            response.getWriter().print("{\"res\":-1,\"info\":\"管理员不可点赞！\"}");
        }
    }


    private void cancelreplyzan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int likecount = Integer.parseInt(request.getParameter("likecount"));
        int replyid = Integer.parseInt(request.getParameter("replyid"));
        HttpSession session = request.getSession();
        Reply reply = new Reply();
        reply.setReplyid(replyid);
        try {
            User user = (User) session.getAttribute("user");
            List list = iReplyService.getReplylikeUserid(replyid);
            boolean flag=false;
            String likeuserid = list.get(0).toString().substring(12, list.get(0).toString().length() - 1);
            int relpycout= (int) iReplyService.queryReplyConutInTotalByreplytime(reply.getMsgid(),reply.getReplytime());
            int page,n;
            if(relpycout<=pageNumber){
                page=1;
                n=relpycout;//代表第几条
            }else if(relpycout%pageNumber==0){
                page=relpycout/pageNumber;
                n=pageNumber;
            }else {
                page=relpycout/pageNumber+1;
                n=relpycout%pageNumber;
            }
            if (!(likeuserid.equals("null") || likeuserid.equals(""))) {
                String userid[] = likeuserid.split(",");
                String newlikeuserid = null;
                for (int i = 0; i < userid.length; i++) {
                    if (user.getUserid() == Integer.parseInt(userid[i])) {
                       flag=true;
                    }else {
                        newlikeuserid=newlikeuserid+userid[i]+",";
                    }
                }
                if(flag){
                    reply.setLikecount(likecount - 1);
                    reply.setLikeuserid(newlikeuserid);
                    if (iReplyService.updateReplylike(reply) > 0) {
                        //发送更新信号
                        sendMessage("Replycancelzan"+iReplyService.queryid(replyid).getMsgid()+","+page+","+n+","+iReplyService.queryid(replyid).getUserid()+","+relpycout);
                        response.getWriter().print("{\"res\":1,\"info\":\"操作成功\"}");
                    } else {
                        response.getWriter().print("{\"res\":-1,\"info\":\"操作失败\"}");
                    }
                }else {
                    response.getWriter().print("{\"res\":-1,\"info\":\"你之前并没有点赞，不可取消点赞！\"}");
                    return;
                }
            } else {
                    response.getWriter().print("{\"res\":1,\"info\":\"你之前并没有点赞，不可取消点赞！\"}");
                    return;
            }
        } catch (NullPointerException e) {
            response.getWriter().print("{\"res\":-1,\"info\":\"管理员不可取消点赞！\"}");
        }
    }

    private void messagezan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int likecount = Integer.parseInt(request.getParameter("likecount"));
        int msgid = Integer.parseInt(request.getParameter("msgid"));
        HttpSession session = request.getSession();
        Message message = new Message();
        message.setMsgid(msgid);
        try {
            User user = (User) session.getAttribute("user");
            if(messageservice.getMsgNoincreaseCount(msgid).getUserid()==user.getUserid()){
                response.getWriter().print("{\"res\":-1,\"info\":\"您不能对自己的帖子进行点赞！\"}");
                return;
            }
            List list = messageservice.getMessagelikeUserid(msgid);
            String likeuserid = list.get(0).toString().substring(12, list.get(0).toString().length() - 1);
            if (!(likeuserid.equals("null") || likeuserid.equals(""))) {
                String userid[] = likeuserid.split(",");
                for (int i = 0; i < userid.length; i++) {
                    if (user.getUserid() == Integer.parseInt(userid[i])) {
                        response.getWriter().print("{\"res\":-1,\"info\":\"你已经点过赞了！\"}");
                        return;
                    }
                }
                message.setLikecount(likecount + 1);
                message.setLikeuserid(likeuserid + user.getUserid() + ",");
                if (messageservice.updateMessagelike(message) > 0) {
                    //发送更新信号
                    sendMessage("messagezan"+msgid+","+messageservice.getMsgNoincreaseCount(msgid).getUserid());
                    response.getWriter().print("{\"res\":1,\"info\":\"操作成功\"}");
                } else {
                    response.getWriter().print("{\"res\":-1,\"info\":\"操作失败\"}");
                }
            } else {
                message.setLikecount(likecount + 1);
                message.setLikeuserid(user.getUserid() + ",");
                if (messageservice.updateMessagelike(message) > 0) {
                    //发送更新信号
                    sendMessage("messagezan"+msgid+","+messageservice.getMsgNoincreaseCount(msgid).getUserid());
                    response.getWriter().print("{\"res\":1,\"info\":\"操作成功\"}");
                } else {
                    response.getWriter().print("{\"res\":-1,\"info\":\"操作失败\"}");
                }
            }
        } catch (NullPointerException e) {
            response.getWriter().print("{\"res\":-1,\"info\":\"管理员不可点赞！\"}");
        }
    }

    private void cancelmessagezan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int likecount = Integer.parseInt(request.getParameter("likecount"));
        int msgid = Integer.parseInt(request.getParameter("msgid"));
        HttpSession session = request.getSession();
        Message message = new Message();
        message.setMsgid(msgid);
        try {
            User user = (User) session.getAttribute("user");
            List list = messageservice.getMessagelikeUserid(msgid);
            boolean flag=false;
            String likeuserid = list.get(0).toString().substring(12, list.get(0).toString().length() - 1);
            if (!(likeuserid.equals("null") || likeuserid.equals(""))) {
                String userid[] = likeuserid.split(",");
                String newlikeuserid = null;
                for (int i = 0; i < userid.length; i++) {
                    if (user.getUserid() == Integer.parseInt(userid[i])) {
                        flag=true;
                    }else {
                        newlikeuserid=newlikeuserid+userid[i]+",";
                    }
                }
                if(flag){
                    message.setLikecount(likecount - 1);
                    message.setLikeuserid(newlikeuserid);
                    if (messageservice.updateMessagelike(message) > 0) {
                        //发送更新信号
                        sendMessage("messagezancancel"+msgid+","+messageservice.getMsgNoincreaseCount(msgid).getUserid());
                        response.getWriter().print("{\"res\":1,\"info\":\"操作成功\"}");
                    } else {
                        response.getWriter().print("{\"res\":-1,\"info\":\"操作失败\"}");
                    }
                }else {
                    response.getWriter().print("{\"res\":-1,\"info\":\"你之前并没有点赞，不可取消点赞！\"}");
                    return;
                }
            }else {
                response.getWriter().print("{\"res\":-1,\"info\":\"你之前并没有点赞，不可取消点赞！\"}");
                return;
            }
        } catch (NullPointerException e) {
            response.getWriter().print("{\"res\":-1,\"info\":\"管理员不可取消点赞！\"}");
        }
    }

    private void deleteReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int replyid = Integer.parseInt(request.getParameter("replyid"));
        HttpSession session = request.getSession();
        Reply reply = iReplyService.queryid(replyid);
        try {
            User user = (User) session.getAttribute("user");
            int userid = user.getUserid();
            if (reply != null) {
                int autherid=messageservice.getMsgNoincreaseCount(reply.getMsgid()).getUserid();//楼主userid
                if (userid == reply.getUserid()||userid==autherid) {//只有本人才有资格删除自己的回复信息或者楼主
                    int relpycout= (int) iReplyService.queryReplyConutInTotalByreplytime(reply.getMsgid(),reply.getReplytime());
                    int AutherRelpycout= (int) iReplyService.queryAutherReplyConutBymsgid(reply.getMsgid(),reply.getUserid());
                    int res = iReplyService.deleteReply(replyid);
                    int res2 = iCountService.updateReplyCount(reply.getMsgid());
                    int page,n,AutherPage,nAuther;
                    if(relpycout<=pageNumber){
                        page=1;
                        n=relpycout;//代表第几条
                    }else if(relpycout%pageNumber==0){
                        page=relpycout/pageNumber;
                        n=pageNumber;
                    }else {
                        page=relpycout/pageNumber+1;
                        n=relpycout%pageNumber;
                    }
                    if(AutherRelpycout<=pageNumber){
                        AutherPage=1;
                        nAuther=AutherRelpycout;//代表第几条
                    }else if(AutherRelpycout%pageNumber==0){
                        AutherPage=AutherRelpycout/pageNumber;
                        nAuther=pageNumber;
                    }else {
                        AutherPage=AutherRelpycout/pageNumber+1;
                        nAuther=AutherRelpycout%pageNumber;
                    }
                    if (res == 1 && res2 == 1) {
                        //发送更新信号
                        if(userid != reply.getUserid()&&userid==autherid){//楼主删除别人的
                            sendMessage("ReplyDeleteByAuther"+reply.getMsgid()+","+page+","+n+","+userid+","+AutherPage+","+nAuther+","+relpycout+","+AutherRelpycout);
                        }else if(userid == reply.getUserid()&&userid!=autherid){//该用户不是楼主，自己删除自己的回帖
                            sendMessage("ReplyDelete"+reply.getMsgid()+","+page+","+n+","+userid+","+AutherPage+","+nAuther+","+relpycout+","+AutherRelpycout);
                        }
                        else{//楼主删除自己的
                            sendMessage("ReplyDeleteAuther"+reply.getMsgid()+","+page+","+n+","+userid+","+AutherPage+","+nAuther+","+relpycout+","+AutherRelpycout);
                        }
                        response.getWriter().print("{\"res\": 1, \"info\":\"删除成功!\"}");
                    } else {
                        response.getWriter().print("{\"res\": " + res + ", \"info\":\"删除失败!\"}");
                    }
                } else {
                    response.getWriter().print("{\"res\":-1,\"info\":\"你没有权限删除其它用户回复的信息，删除失败！\"}");
                }
            } else {
                response.getWriter().print("{\"res\":-1,\"info\":\"操作失败\"}");
            }
        } catch (NullPointerException e) {
            int res = iReplyService.deleteReply(replyid);
            int res2 = iCountService.updateReplyCount(reply.getMsgid());
            if (res == 1 && res2 == 1) {
                //发送更新信号
                sendMessage(reply.getMsgid()+"");
                response.getWriter().print("{\"res\": 1, \"info\":\"管理员删除成功!\"}");
            } else {
                response.getWriter().print("{\"res\": " + res + ", \"info\":\"管理员删除失败!\"}");
            }
        }
    }

    private void allowreply(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int msgid = Integer.parseInt(request.getParameter("msgid"));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userid = user.getUserid();
        if(messageservice.queryMsgState(msgid)==3){
            response.getWriter().print("{\"res\": -2, \"info\":\"该帖子之前已经被你删除了，请先恢复再操作!\"}");
            return;
        }
        if(messageservice.queryMsgReplyident(msgid)==0){
            response.getWriter().print("{\"res\": 1, \"info\":\"该帖子之前已经开启回复了，无需重复操作!\"}");
            return;
        }
        //允许回复
        if(messageservice.upadateReplyident(msgid,0)>0){
            response.getWriter().print("{\"res\": 1, \"info\":\"开启回复成功，允许所有人回复你的帖子!\"}");
            return;
        }else {
            response.getWriter().print("{\"res\": -1, \"info\":\"开启回复失败!\"}");
            return;
        }
    }

    private void Notallowreply(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int msgid = Integer.parseInt(request.getParameter("msgid"));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int userid = user.getUserid();
        if(messageservice.queryMsgState(msgid)==3){
            response.getWriter().print("{\"res\": -2, \"info\":\"该帖子之前已经被你删除了，请先恢复再操作!\"}");
            return;
        }
        if(messageservice.queryMsgReplyident(msgid)==-1){
            response.getWriter().print("{\"res\": 1, \"info\":\"该帖子之前已经关闭回复了，无需重复操作!\"}");
            return;
        }
        //允许回复
        if(messageservice.upadateReplyident(msgid,-1)>0){
            response.getWriter().print("{\"res\": 1, \"info\":\"关闭回复成功，并且仅你自己可以回复，其它用户禁止回复该帖子!\"}");
            return;
        }else {
            response.getWriter().print("{\"res\": -1, \"info\":\"关闭回复失败!\"}");
            return;
        }
    }

//
//    }
//    private void SendMail(HttpServletRequest request, HttpServletResponse response,int msgid,int userid,String title) throws ServletException, IOException {
//
//    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was
     * successful.
     * 建立websocket连接时调用
     */
    @OnOpen
    public void onOpen(Session session){
//        System.out.println("Session " + session.getId() + " has opened a connection");
        try {
            this.session=session;
            webSocketSet.add(this);     //加入set中
            if(session.isOpen()){
                session.getBasicRemote().sendText("Connection Established");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     * 接收到客户端消息时使用，这个例子里没用
     */
    @OnMessage
    public void onMessage(String message, Session session){
        if(message.equals("ping")){
            try {
                session.getBasicRemote().sendText("pong");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
//            System.out.println("Message from " + session.getId() + ": " + message);
        }
    }

    /**
     * The user closes the connection.
     *
     * Note: you can't send messages to the client from this method
     * 关闭连接时调用
     */
    @OnClose
    public void onClose(Session session){
          this.session=session;
          webSocketSet.remove(this);  //从set中删除
          try {
            session.close();
         } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("Session " +session.getId()+" has closed!");
    }

    /**
     * 注意: OnError() 只能出现一次.   其中的参数都是可选的。
     * @param session
     * @param t
     */
    @OnError
    public void onError(Session session, Throwable t) {
        t.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @throws IOException
     * 发送自定义信号，“1”表示告诉前台，数据库发生改变了，需要刷新
     * 1代表回复帖子的内容发生变化(回复帖子信息的删除编辑新增等，不包括帖子的删除，帖子的点赞，回复的点赞)
     * 2代表有用户新发帖子或者删除帖子、恢复帖子(删除包括管理员删除、用户删除；恢复包括管理员恢复、用户恢复)
     * 3代表帖子的问题头发生变化(包括帖子主题、帖子问题内容修改或者编辑)
     */
    public static void sendMessage(String info) throws IOException{
        //群发消息
        for(UserMessageServlet item: webSocketSet){
            try {
                if(!item.session.isOpen()){
//                    System.out.println("s.session为空----------------");
                }else{
                    item.session.getBasicRemote().sendText(info);
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }

    }

}
