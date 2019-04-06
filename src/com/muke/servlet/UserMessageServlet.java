package com.muke.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

/**
 * Servlet implementation class UserMessageServlet
 */
@WebServlet("/user/userMessageServlet")
public class UserMessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private IMessageService messageservice = new MessageServiceImpl();
    private IThemeService themeService = new ThemeServiceImpl();
    private IReplyService iReplyService = new IReplyServiceImpl();
    private ICountService iCountService = new ICountServiceImpl();

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
        if (rs > 0) {
            response.getWriter().print("{\"res\":1,\"info\":\"回帖成功\"}");
        } else {
            response.getWriter().print("{\"res\":-1,\"info\":\"回帖失败\"}");
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
        MessageInfo messageInfo = messageservice.getMsgNoincreaseCount(Integer.parseInt(msgid));
        if(messageInfo!=null) {
            if (userid == messageInfo.getUserid()) {//只有本人才有资格删除自己的问题
                //管理员已禁用的帖子不允许用户自己操作
                if (messageInfo.getState() == -1) {
                    response.getWriter().print("{\"res\":-1,\"info\":\"该帖子由于存在不良信息已被管理员屏蔽，你暂时没有删除权限!如有疑问，请联系管理员QQ:1632029393!\"}");
                    return;
                }

                if (msgid == null || msgid.equals("")) {
                    msgid = "-1";
                }
                int res = messageservice.userdeleteMsg(Integer.parseInt(msgid));
                if (res == 1) {
                    response.getWriter().print("{\"res\": 1, \"info\":\"删除成功\"}");
                    if (messageInfo.getReplyCount() > 0) {
                        String finalMsgid = msgid;
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (user.getMailstate() == 1) {
                                    List emailList = iReplyService.getReplyUseremail(Integer.parseInt(finalMsgid), userid);
                                    if (emailList.size() != 0 && emailList != null) {
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
                                        String time = df.format(new Date());
                                        MessageEmail messageEmail = new MessageEmail();
                                        messageEmail.setFrom("1632029393@qq.com");
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
                                            return;
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
                    response.getWriter().print("{\"res\": " + res + ", \"info\":\"删除失败\"}");
                }
            } else {
                response.getWriter().print("{\"res\":-1,\"info\":\"权限不足，删除失败\"}");
            }
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
                if (rs > 0) {
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
            List list = iReplyService.getReplylikeUserid(replyid);
            String likeuserid = list.get(0).toString().substring(12, list.get(0).toString().length() - 1);
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
                    response.getWriter().print("{\"res\":1,\"info\":\"操作成功\"}");
                } else {
                    response.getWriter().print("{\"res\":-1,\"info\":\"操作失败\"}");
                }

            } else {
                reply.setLikecount(likecount + 1);
                reply.setLikeuserid(user.getUserid() + ",");
                if (iReplyService.updateReplylike(reply) > 0) {
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
                    response.getWriter().print("{\"res\":1,\"info\":\"操作成功\"}");
                } else {
                    response.getWriter().print("{\"res\":-1,\"info\":\"操作失败\"}");
                }
            } else {
                message.setLikecount(likecount + 1);
                message.setLikeuserid(user.getUserid() + ",");
                if (messageservice.updateMessagelike(message) > 0) {
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
                if (userid == reply.getUserid()) {//只有本人才有资格删除自己的回复信息
                    int res = iReplyService.deleteReply(replyid);
                    int res2 = iCountService.updateReplyCount(reply.getMsgid());
                    if (res == 1 && res2 == 1) {
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
                response.getWriter().print("{\"res\": 1, \"info\":\"管理员删除成功!\"}");
            } else {
                response.getWriter().print("{\"res\": " + res + ", \"info\":\"管理员删除失败!\"}");
            }
        }
    }

//    private void SendMail(HttpServletRequest request, HttpServletResponse response,int msgid,int userid,String title) throws ServletException, IOException {
//
//    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
