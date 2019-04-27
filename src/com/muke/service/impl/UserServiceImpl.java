package com.muke.service.impl;

import com.muke.dao.IUserDao;
import com.muke.dao.impl.UserDaoImpl;
import com.muke.pojo.Advise;
import com.muke.pojo.User;
import com.muke.pojo.Userlog;
import com.muke.service.IUserService;
import com.muke.util.Page;

import java.util.List;

public class UserServiceImpl implements IUserService {

    private IUserDao iUserDao = new UserDaoImpl();

    @Override
    public User userLogin(String username, String pw) {
        // TODO Auto-generated method stub
        return iUserDao.query(username, pw);
    }

    public User userPass(String username, String email) {
        // TODO Auto-generated method stub
        return iUserDao.query1(username, email);
    }

    @Override
    public User useremail(String email) {
        // TODO Auto-generated method stub
        return iUserDao.queryemail(email);
    }

    @Override
    public User username(String username) {
        // TODO Auto-generated method stub
        return iUserDao.queryusername(username);
    }

    @Override
    public Advise getAdvisedetails(int id) {
        return iUserDao.getAdvisedetails(id);
    }

    @Override
    public Page queryUserlogbyUserid(int userid, Page page) {
        return iUserDao.queryUserlogbyUserid(userid, page);
    }

    @Override
    public Page queryUserLog(Page page) {
        return iUserDao.queryUserLog(page);
    }

    @Override
    public int update(User user) {
        // TODO Auto-generated method stub
        return iUserDao.update(user);
    }

    @Override
    public int userRegister(User user) {
        // TODO Auto-generated method stub
        return iUserDao.add(user);
    }

    @Override
    public Page searchByName(String username, Page page) {
        return iUserDao.queryByName(username, page);
    }

    @Override
    public Page queryUserLogByName(String username, Page page) {
        return iUserDao.queryUserLogByName(username, page);
    }

    @Override
    public Page searchAdvice(Page page) {
        return iUserDao.queryAdvice(page);
    }

    @Override
    public int deleteUser(int userid) {
        return iUserDao.updateState(userid, -1);
    }

    @Override
    public int delete(int userid) {
        return iUserDao.deleteUser(userid);
    }

    @Override
    public int deleteAdvise(int id) {
        return iUserDao.updateAdvise(id, -1);
    }

    @Override
    public int restoreUser(int userid) {
        return iUserDao.updateState(userid, 0);
    }

    @Override
    public int restoreAdvise(int id) {
        return iUserDao.updateAdvise(id, 0);
    }

    @Override
    public int updatePw(User user) {
        return iUserDao.updatePw(user);
    }

    @Override
    public int updatePwBynewPass(String newPassword, String email) {
        return iUserDao.updatePwBynewPass(newPassword, email);
    }

    @Override
    public int modifyUser(int userid, int state) {
        return iUserDao.updateState(userid, state);
    }

    @Override
    public boolean isExist(String username) {
        return iUserDao.isExist(username);
    }

    @Override
    public boolean isExistmail(String mail) {
        return iUserDao.isExistmail(mail);
    }

    @Override
    public boolean isExistmailBind(String mail) {
        return iUserDao.isExistmailBind(mail);
    }

    @Override
    public int updatemail(String username, String mail, int state) {
        return iUserDao.updatemail(username, mail, state);
    }//更新邮箱

    @Override
    public int saveAdvise(String advise, String number) {//存网站建议和反馈
        return iUserDao.saveAdvise(advise, number);
    }

    @Override
    public int saveUserimg(String username, String user_img) {
        return iUserDao.saveUserimg(username, user_img);
    }

    @Override
    public int deleteUserimg(int userid) {
        return iUserDao.deleteUserimg(userid);
    }

    @Override
    public User queryuserbyid(int userid) {
        return iUserDao.queryuserbyid(userid);
    }

    @Override
    //插入登陆时间
    public int insertlogintime(String name, String logintime) {
        return iUserDao.insertlogintime(name, logintime);
    }

    @Override
    public int insertloginLog(Userlog userlog) {
        return iUserDao.insertloginLog(userlog);
    }

    @Override
    public Page queryAdvise(String key, Page page) {
        return iUserDao.queryAdvise(key, page);
    }

    @Override
    public boolean queryMessageByid(int userid) {
        return iUserDao.queryMessageByid(userid);
    }

    @Override
    public boolean queryReplyByid(int userid) {
        return iUserDao.queryReplyByid(userid);
    }

    @Override
    public int cascadeleteUserAndMessage(int userid) {
        return iUserDao.cascadeleteUserAndMessage(userid);
    }

    @Override
    public int cascadeleteUserAndReply(int userid) {
        return iUserDao.cascadeleteUserAndReply(userid);
    }

    @Override
    public int cascadeleteUserAndReplyAndMessage(int userid) {
        return iUserDao.cascadeleteUserAndReplyAndMessage(userid);
    }

    @Override
    public int insertLoginNum(String username) {
        return iUserDao.insertLoginNum(username);
    }

    @Override
    public int deleteReply(int userid) {
        return iUserDao.deleteReply(userid);
    }

    @Override
    public int deleteCount(int msgid) {
        return iUserDao.deleteCount(msgid);
    }

    @Override
    public int queryReplyCountByMsgid(int msgid, int userid) {
        return iUserDao.queryReplyCountByMsgid(msgid, userid);
    }

    @Override
    public int updateCountReplyCount(int msgid, int replyCount) {
        return iUserDao.updateCountReplyCount(msgid, replyCount);
    }

    public List queryMsgid(int userid) {
        return iUserDao.queryMsgid(userid);
    }

    @Override
    public List getLikeMsgid(int userid){
        return iUserDao.getLikeMsgid(userid);
    }

    @Override
    public int updateLikemsgid(int userid,String likemsgid){
        return iUserDao.updateLikemsgid(userid,likemsgid);
    }
}
