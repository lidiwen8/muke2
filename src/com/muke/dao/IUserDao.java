package com.muke.dao;

import com.muke.pojo.Advise;
import com.muke.pojo.User;
import com.muke.pojo.Userlog;
import com.muke.util.Page;

import java.util.List;

public interface IUserDao {
    int add(User user);

    int update(User user);

    User query(String username, String pw);

    Advise getAdvisedetails(int id);

    Page queryUserlogbyUserid(int userid, Page page);

    Page queryByName(String username, Page page);

    Page queryAdvice(Page page);

    /**
     * 更新用户状态
     *
     * @param userid
     * @param state
     * @return
     */
    int updateState(int userid, int state);

    int updateAdvise(int id, int state);

    int deleteUser(int userid);

    int updatemail(String username, String mail, int state);//更新邮箱

    /**
     * 更新用户密码
     *
     * @param user
     * @return
     */
    int updatePw(User user);


    int updatePwBynewPass(String newPassword, String email);

    /**
     * 验证用户名是否存在
     *
     * @param username 用户名
     * @return true：存在 false：不存在
     */
    boolean isExist(String username);

    boolean isExistmail(String mail);//邮箱是不是已经存在

    boolean isExistmailBind(String mail);//邮箱是不是已经存在且被绑定

    User query1(String username, String email);

    User queryemail(String email);//根据唯一的邮箱号返回user对象

    User queryusername(String username);

    int saveAdvise(String advise, String number);//网站建议和反馈

    User queryuserbyid(int userid);

    int saveUserimg(String username, String user_img);//保存用户头像路径

    int deleteUserimg(int userid);//删除用户头像路径

    int insertlogintime(String name, String logintime);//插入登陆时间

    int insertloginLog(Userlog userlog);

    Page queryAdvise(String key, Page page);

    Page queryUserLog(Page page);

    Page queryUserLogByName(String username, Page page);

    boolean queryMessageByid(int userid);

    boolean queryReplyByid(int userid);

    int cascadeleteUserAndMessage(int userid);

    int cascadeleteUserAndReply(int userid);

    int cascadeleteUserAndReplyAndMessage(int userid);

    int insertLoginNum(String username);

    int deleteReply(int userid);

    int deleteCount(int msgid);

    int updateCountReplyCount(int msgid, int replyCount);

    List queryMsgid(int userid);

    int queryReplyCountByMsgid(int msgid, int userid);//根据msgid统计回复数量，自己回复的reply记录不算

    List getLikeMsgid(int userid);//得到用户收藏的帖子列表

    int updateLikemsgid(int userid,String likemsgid);//用户收藏帖子
}
