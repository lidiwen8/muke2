package com.muke.service;

import com.muke.pojo.Advise;
import com.muke.pojo.User;
import com.muke.pojo.Userlog;
import com.muke.util.Page;

import java.util.List;

public interface IUserService {
    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    int userRegister(User user);

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    User userLogin(String username, String password);

    User userPass(String username, String email);

    User useremail(String email);

    User queryuserbyid(int userid);

    User username(String username);

    Advise getAdvisedetails(int id);

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    int update(User user);

    /**
     * 根据用户名和分页信息查询数据
     *
     * @param username
     * @param page
     * @return
     */
    Page searchByName(String username, Page page);

    Page searchAdvice(Page page);//查询建议

    Page queryUserLogByName(String username, Page page);


    /**
     * 删除信息
     *
     * @param userid
     * @return
     */
    int deleteUser(int userid);

    int delete(int userid);//删除用户

    int deleteAdvise(int id);

    /**
     * 恢复状态
     *
     * @param userid
     * @return
     */
    int restoreUser(int userid);

    int restoreAdvise(int id);

    //修改密码
    int updatePw(User user);

    int updatePwBynewPass(String newPassword,String email);

    int updatemail(String username, String mail, int state);//更新邮箱

    int modifyUser(int userid, int state);

    /**
     * 验证用户名是否存在
     *
     * @param username 用户名
     * @return true：存在 false：不存在
     */
    boolean isExist(String username);

    boolean isExistmail(String mail);

    boolean isExistmailBind(String mail);

    int saveAdvise(String advise, String number);

    int saveUserimg(String username, String user_img);

    int deleteUserimg(int userid);

    int insertlogintime(String name, String logintime);//插入登陆时间

    int insertloginLog(Userlog userlog);//插入登录日志

    Page queryAdvise(String key, Page page);

    Page queryUserlogbyUserid(int userid, Page page);

    boolean queryMessageByid(int userid);

    boolean queryReplyByid(int userid);

    int cascadeleteUserAndMessage(int userid);

    int cascadeleteUserAndReply(int userid);

    int cascadeleteUserAndReplyAndMessage(int userid);

    int insertLoginNum(String username);

    int deleteReply(int userid);

    int deleteCount(int msgid);

    Page queryUserLog(Page page);

    int queryReplyCountByMsgid(int msgid, int userid);//根据msgid统计回复数量,自己回复reply记录的忽略不计

    int updateCountReplyCount(int msgid, int replyCount);//更新count表的replyCount字段

    List queryMsgid(int userid);//先找到要删除用户之前回复过的所有帖子得到列表msgid（自己发的帖子忽略不计）
}

 
