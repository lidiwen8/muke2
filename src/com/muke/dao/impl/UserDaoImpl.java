package com.muke.dao.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.muke.dao.IUserDao;
import com.muke.pojo.*;
import com.muke.util.DBUtil;
import com.muke.util.Page;

public class UserDaoImpl implements IUserDao {
    DBUtil dbutil = new DBUtil();

    @Override
    public User query(String username, String pw) {
        String sql = "SELECT * FROM user WHERE username = ? and password = ?";
        Object[] params = {username, pw};

        User user = null;

        try {
            user = (User) dbutil.getObject(User.class, sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User query1(String username, String email) {
        String sql = "SELECT * FROM user WHERE username = ? and email = ?";
        Object[] params = {username, email};

        User user = null;

        try {
            user = (User) dbutil.getObject(User.class, sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User queryemail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        Object[] params = {email};

        User user = null;

        try {
            user = (User) dbutil.getObject(User.class, sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public User queryusername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        Object[] params = {username};

        User user = null;

        try {
            user = (User) dbutil.getObject(User.class, sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return user;
    }

    @Override
   public ShortMessageInfo getMsg(int msgid){
        String sql = "SELECT message.msgid,accessCount,msgtopic,msgtime,thename,likecount,replyCount,message.state FROM message,count,theme  WHERE message.msgid= ? and message.msgid=count.msgid and theme.theid=message.theid and state>=0 and state<3";
        Object[] params = {msgid};

        ShortMessageInfo shortMessageInfo = null;

        try {
            shortMessageInfo = (ShortMessageInfo) dbutil.getObject(ShortMessageInfo.class, sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return shortMessageInfo;
    }

    @Override
    public User queryuserbyid(int userid) {
        String sql = "SELECT * FROM user WHERE userid = ?";
        Object[] params = {userid};
        User user = null;
        try {
            user = (User) dbutil.getObject(User.class, sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public int add(User user) {
        String sql = "INSERT INTO user (username, password, realname, sex, hobbys, birthday, city, email, qq, user_img,logintime,loginNum) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] params = {user.getUsername(), user.getPassword(), user.getRealname(), user.getSex(),
                user.getHobbys(), user.getBirthday(), user.getCity(), user.getEmail(), user.getQq(), user.getUser_img(), user.getLogintime(), user.getLoginNum()};
        int res = 0;
        try {
            res = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public int update(User user) {
        String sql = "UPDATE user SET "
                + "password = ?, realname = ?, sex = ?, "
                + "hobbys = ?, birthday = ?, city = ?, email = ?, qq = ?, mailstate = ? ,description = ?  "
                + "WHERE userid = ?";
        Object[] params = {user.getPassword(), user.getRealname(), user.getSex(),
                user.getHobbys(), user.getBirthday(), user.getCity(), user.getEmail(), user.getQq(), user.getMailstate(), user.getDescription(),
                user.getUserid()};

        int res = 0;

        try {
            res = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int insertLoginNum(String username) {
        String sql = "UPDATE user SET loginNum=loginNum+1 WHERE username=?";
        Object[] params = {username};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public Page queryByName(String username, Page page) {
        String sql = "SELECT * FROM user WHERE username like ? ORDER BY createtime DESC";
        Object[] params = {"%" + username + "%"};

        Page resPage = null;

        try {
            resPage = dbutil.getQueryPage(User.class, sql, params, page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resPage;
    }

    @Override
    public Page queryAdvice(Page page) {
        String sql = "SELECT * FROM advise ORDER BY createDate DESC";
        Page resPage = null;
        try {
            resPage = dbutil.getQueryPage(Advise.class, sql, null, page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resPage;
    }

    //删除用户
    @Override
    public int updateState(int userid, int state) {
        String sql = "UPDATE user SET "
                + "state = ? "
                + "WHERE userid = ?";
        Object[] params = {state, userid};

        int res = 0;

        try {
            res = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int updateAdvise(int id, int state) {
        String sql = "UPDATE advise SET "
                + "states = ? "
                + "WHERE id = ?";
        Object[] params = {state, id};

        int res = 0;

        try {
            res = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int updatemail(String username, String mail, int mailstate) {
        String sql = "UPDATE user SET "
                + "email = ? ,mailstate = ? "
                + "WHERE username = ?";
        Object[] params = {mail, mailstate, username};
        int res = 0;

        try {
            res = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public Advise getAdvisedetails(int id) {
        StringBuffer sBuffer = new StringBuffer();
        Advise advise = null;
        sBuffer.append("select * from advise where id= ?");
        try {
            advise = (Advise) dbutil.getObject(Advise.class, sBuffer.toString(), new Object[]{id});
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return advise;
    }

    @Override
    public Page queryUserlogbyUserid(int userid, Page page) {
        String sql = "select userid,ip,logintime,place from userlog where userid=? order by id desc";
        Object[] params = {userid};
        Page respage = null;
        respage = dbutil.getQueryPage(Userlog.class, sql, params, page);
        return respage;
    }

    @Override
    public int updatePw(User user) {
        String sql = "UPDATE `user` SET  `password`=?  WHERE (`username`=?) ";
        Object[] params = {user.getPassword(), user.getUsername()};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public int updatePwBynewPass(String newPassword, String email){
        String sql = "UPDATE user SET password=? WHERE email=? and mailstate=1";
        Object[] params = {newPassword, email};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public boolean isExist(String username) {
        String sql = "select count(*) as count from user where binary username=? ";
        boolean result = true;
        Map<String, Object> map = null;
        try {
            map = dbutil.getObject(sql, new Object[]{username});
            int count = Integer.parseInt(map.get("count").toString());
            if (count > 0) {
                result = false;//用户名已存在
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean isExistmail(String mail) {
        String sql = "select count(*) as count from user where email=? ";
        boolean result = true;
        Map<String, Object> map = null;
        try {
            map = dbutil.getObject(sql, new Object[]{mail});
            int count = Integer.parseInt(map.get("count").toString());
            if (count > 0) {
                result = false;//邮箱号已存在
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean isExistmailBind(String mail) {
        String sql = "select count(*) as count from user where email=? and mailstate=1";
        boolean result = false;
        Map<String, Object> map = null;
        try {
            map = dbutil.getObject(sql, new Object[]{mail});
            int count = Integer.parseInt(map.get("count").toString());
            if (count > 0) {
                result = true;//邮箱号已存在且被绑定
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int saveAdvise(String advise, String number) {
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "INSERT INTO advise (description,number,createDate) VALUES (?, ?, ?)";
        Object[] params = {advise, number, df.format(day)};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public int deleteUser(int userid) {
        String sql = "delete from user WHERE userid =?";
        Object[] params = {userid};
        int res = 0;
        try {
            res = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return res;
    }

    @Override
    public int deleteUserimg(int userid) {
        String sql = "update user set user_img =? where userid =?";
        Object[] params = {null, userid};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public int saveUserimg(String username, String user_img) {
        String sql = "update user set user_img =? where username =?";
        Object[] params = {user_img, username};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public int insertlogintime(String name, String logintime) {
        String sql = "update user set logintime =? where username =?";
        Object[] params = {logintime, name};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public int insertloginLog(Userlog userlog) {
        String sql = "insert into userlog (userid,ip,place,logintime) VALUES (?,?,?,?)";
        Object[] params = {userlog.getUserid(), userlog.getIp(), userlog.getPlace(), userlog.getLogintime()};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public Page queryAdvise(String key, Page page) {
        String sql = "SELECT * FROM advise WHERE description LIKE ?";
        Object[] params = {"%" + key + "%"};
        Page respage = null;
        respage = dbutil.getQueryPage(Advise.class, sql, params, page);
        return respage;
    }

    @Override
    public Page queryUserLog(Page page) {
        String sql = "SELECT username,realname,userlog.logintime,ip,place from userlog,user WHERE userlog.userid=`user`.userid order by id desc";
        Object[] params = {};
        Page respage = null;
        respage = dbutil.getQueryPage(UserlogInfo.class, sql, params, page);
        return respage;
    }

    @Override
    public Page queryUserLogByName(String username, Page page) {
        String sql = "SELECT username,realname,userlog.logintime,ip,place from userlog,user WHERE userlog.userid=`user`.userid and username like ? order by id desc";
        Object[] params = {"%" + username + "%"};
        Page resPage = null;
        try {
            resPage = dbutil.getQueryPage(UserlogInfo.class, sql, params, page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resPage;
    }


    @Override
    public boolean queryMessageByid(int userid) {
        String sql = "select count(*) as count from message where userid=? ";
        boolean result = false;
        Map<String, Object> map = null;
        try {
            map = dbutil.getObject(sql, new Object[]{userid});
            int count = Integer.parseInt(map.get("count").toString());
            if (count > 0) {
                result = true;//该用户发表过帖子
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean queryReplyByid(int userid) {
        String sql = "select count(*) as count from reply where userid=? ";
        boolean result = false;
        Map<String, Object> map = null;
        try {
            map = dbutil.getObject(sql, new Object[]{userid});
            int count = Integer.parseInt(map.get("count").toString());
            if (count > 0) {
                result = true;//该用户回复过帖子
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int cascadeleteUserAndMessage(int userid) {
        int flag = 0;
        Object[] params = {userid};
        try {
            String sql = "delete r.*,s.*,u.*,c.* FROM user r INNER JOIN message u on r.userid=u.userid left join reply s on s.msgid=u.msgid left join count c on c.msgid=u.msgid WHERE r.userid=?";
//			String sql = "delete c.*,s.* from user c left join message s on c.userid=s.userid where c.userid=?";
            dbutil.execute(sql, params);
            flag = 1;
        } catch (Exception e) {
            e.printStackTrace();
            //事务回滚
            flag = 0;
        }
        return flag;
    }

    @Override
    public int cascadeleteUserAndReply(int userid) {
        int flag = 0;
        Object[] params = {userid};
        try {
            String sql = "delete c.*,s.* from user c left join reply s on c.userid=s.userid where c.userid=?";
            dbutil.execute(sql, params);
            flag = 1;
        } catch (Exception e) {
            e.printStackTrace();
            //事务回滚
            flag = 0;
        }
        return flag;
    }

    @Override
    public int cascadeleteUserAndReplyAndMessage(int userid) {
        int flag = 0;
        Object[] params = {userid};
        try {
            String sql = "delete a.*,b.*,c.*,d.* from user a left join message b on b.userid=a.userid left join reply c on b.msgid=c.msgid left join count d on d.msgid=c.msgid where a.userid=?";
            dbutil.execute(sql, params);
            flag = 1;
        } catch (Exception e) {
            e.printStackTrace();
            //事务回滚
            flag = 0;
        }
        return flag;
    }

    @Override
    public int deleteReply(int userid) {
        String sql = "delete from reply WHERE userid =?";
        Object[] params = {userid};
        int res = 0;
        try {
            res = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int deleteCount(int msgid) {
        String sql = "delete from count WHERE msgid =?";
        Object[] params = {msgid};
        int res = 0;
        try {
            res = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int updateCountReplyCount(int msgid, int replyCount) {
        String sql = "update count set replyCount=? WHERE msgid =?";
        Object[] params = {replyCount, msgid};
        int res = 0;
        try {
            res = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List queryMsgid(int userid) {
        String sql = "SELECT msgid from message WHERE msgid in (SELECT distinct msgid AS msgid FROM reply where userid=?) and msgid not in (SELECT msgid from message where userid=?)";
        Object[] params = {userid, userid};
        List res = null;
        try {
            res = dbutil.getQueryList(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public int queryReplyCountByMsgid(int msgid, int userid) {
        String sql = "SELECT count(*) as count from reply where msgid=? and userid not in (SELECT userid from reply where userid=?)";
        Object[] params = {msgid, userid};
        Map map = null;
        try {
            map = dbutil.getObject(sql, params);
            Number count = (Number) map.get("count");
            return count.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List getLikeMsgid(int userid){
        String sql = "SELECT likemsgid FROM user where userid=?";
        Object[] params = {userid};
        List list = null;
        try {
            list = dbutil.getQueryList(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public int updateLikemsgid(int userid,String likemsgid){
        String sql = "update user set likemsgid=? where userid=?";
        Object[] params = {likemsgid,userid};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public int queryUseridByMsgid(int msgid){
        String sql = "select userid from message where msgid=?";
        Object[] params = {msgid};
        Map map = null;
        try {
            map = dbutil.getObject(sql, params);
            int userid = (int) map.get("userid");
            return userid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int queryMsgStateByMsgid(int msgid){
        String sql = "select state from message where msgid=?";
        Object[] params = {msgid};
        Map map = null;
        try {
            map = dbutil.getObject(sql, params);
            int state = (int) map.get("state");
            return state;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -3;
    }

    @Override
    public int updateMailLinkInfo(String username,String active_key, String active_time,String new_pass){
        String sql = "update user set active_key=?,active_time=?,new_pass=? where username=?";
        Object[] params = {active_key,active_time,new_pass,username};
        int rs = 0;
        try {
            rs = dbutil.execute(sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public UserMailLinkInfo queryMailLinkInfo(String username){
        String sql = "SELECT username,active_key,active_time,new_pass FROM user WHERE username = ?";
        Object[] params = {username};
        UserMailLinkInfo userMailLinkInfo = null;
        try {
            userMailLinkInfo = (UserMailLinkInfo) dbutil.getObject(UserMailLinkInfo.class, sql, params);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return userMailLinkInfo;
    }

}
